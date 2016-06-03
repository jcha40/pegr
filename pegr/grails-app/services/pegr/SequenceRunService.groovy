package pegr
import grails.transaction.Transactional
import groovy.time.TimeCategory

class SequenceRunException extends RuntimeException {
    String message
}

class DuplicatedSampleException extends RuntimeException {
    
}

class SequenceRunService {
    def springSecurityService
    def walleService
    
    @Transactional
    void save(SequenceRun run) {
        run.lane = 0
        run.user = springSecurityService.currentUser
        run.status = RunStatus.PREP
        if (run.runStats) {
            if (!run.runStats.save(flush: true)) {
                throw new SequenceRunException(message: "Invalid inputs for stats!")
            }
        }
        if(!run.save(flush: true)) {
            throw new SequenceRunException(message: "Invalid inputs for basic information!")
        }
    }
    
    @Transactional
    List addPool(Long poolItemId, Long runId) {
        def run = SequenceRun.get(runId)
        if (!run) {
            throw new SequenceRunException(message: "Sequence run not found!")
        }       
        run.poolItem = Item.get(poolItemId)
        run.save()
        def samples = fetchSamplesInPool(poolItemId)
        samples.each {
            addSampleToRun(it, run)
        }
    }
    
    @Transactional
    def addSampleToRun(Sample sample, SequenceRun run) {
        if (!SequencingExperiment.findBySampleAndSequenceRun(sample, run)) {
            def experiment = new SequencingExperiment(sample: sample, sequenceRun: run) 
            experiment.save(failOnError: true)
            if (sample.requestedGenomes && sample.requestedGenomes != "") {
                sample.requestedGenomes.split(",")*.trim().unique().each{ genomeName ->
                    def genome = Genome.findByName(genomeName)
                    if (genome) {                            
                        new SequenceAlignment(genome: genome, sequencingExperiment: experiment).save()
                    }
                }
            }
        }
    }
    
    @Transactional
    void removePool(Long runId) {
        def run = SequenceRun.get(runId)
        if (run) {
            def experiments = run.experiments 
            experiments.each {
                // remove all the sequencingExperiments from the sequence run
                removeExperiment(it.id)
            }
            // remove the pool from the sequence run
            run.poolItem = null
            run.save()
        } else {
            throw new SequenceRunException(message: "Pool not found!")
        }
    }
    
    List fetchSamplesInPool(Long poolItemId) {
        def pool = Item.get(poolItemId)
        if (!pool) {
            throw new SequenceRunException(message: "Pool not found!")
        }
        return pool.samplesInPool
    }
    
    @Transactional
    def addSamplesById(Long runId, String sampleIds) {
        def run = SequenceRun.get(runId)
        if (!run) {
            throw new SequenceRunException(message: "Sequence run not found!")
        }   
        if (sampleIds == null || sampleIds == "") {
            throw new SequenceRunException(message: "No sample ID!")
        }
        Set ids = []
        sampleIds.split(",")*.trim().each{ spanStr ->
            def span = spanStr.split("-")*.trim()
            if (span.size() == 1) {
                ids += stringToId(span[0])
            } else if (span.size() == 2) {
                def x1 = stringToId(span[0])
                def x2 = stringToId(span[1])
                if (x1 > x2) {
                    def y = x1
                    x1 = x2
                    x2 = y
                }
                (x1..x2).each {
                    ids << it
                }
            } else if(span.size() > 2) {
                throw new SequenceRunException(message: "Error in the sample IDs!")
            }
        }
        Set unknownSampleIds = []
        ids.each { id ->
            def sample = Sample.get(id)
            if (!sample) {
                unknownSampleIds << id
            } else {
                addSampleToRun(sample, run)
            }
        }
        return unknownSampleIds
    }
    
    def stringToId(String s) {
        def id
        try {
            id = Long.parseLong(s)
        } catch (Exception e){
            throw new SequenceRunException(message: "Error in the sample ID ${s}!")
        }
        return id
    }
    
    @Transactional
    void removeExperiment(Long experimentId) {
        def experiment = SequencingExperiment.get(experimentId)
        if (experiment) {
            // remove the associated alignments
            SequenceAlignment.executeUpdate("delete SequenceAlignment where sequencingExperiment.id = :experimentId", [experimentId: experimentId])
            // remove the experiment itself
            experiment.delete()
        } else {
            throw new SequenceRunException(message: "Experiment not found!")
        }
    }
    
    @Transactional
    void updateGenome(String experimentIdStr, List genomeIds) {
        Long experimentId = Long.parseLong(experimentIdStr) 
        def experiment = SequencingExperiment.get(experimentId) 
        if (!experiment) {
            throw new SequenceRunException(message: "Experiment not found!")
        }
        def toDelete = experiment.alignments
        def toAdd = []
        genomeIds.each { genomeIdStr ->
            def genomeId = Long.parseLong(genomeIdStr)
            def oldAlign = toDelete.find{it.genome.id == genomeId}
            if (oldAlign) {
                toDelete.remove(oldAlign)
            } else {
                def genome = Genome.get(genomeId)
                if (genome) {
                    toAdd.push(new SequenceAlignment(sequencingExperiment: experiment, genome: genome))
                } else {
                    throw new SequenceRunException(message: "Genome #${genomeId} not found for Sample ${experiment.sample.id}!")
                }
            }
        }
        toDelete.each{
            it.delete()
        }
        toAdd.each{
            it.save()
        }
    }
    
    @Transactional
    void run(Long runId) {        
        def run = SequenceRun.get(runId)
        if (!run) {
            throw new SequenceRunException(message: "Sequence run not found!")
        }        
 
        if (run.status == RunStatus.RUN) {
             throw new SequenceRunException(message: "Sequence run has already been submitted!")
        }
        
        walleService.addToQueue(runId)
        
        // start the run by creating a job on remote server
        run.status = RunStatus.QUEUE
        run.save()
    }
    
    def getCalendarTimeString(Date time) {
        return time.format("yyyyMMdd'T'HHmmss'Z'")
    }
    
    byte[] calendarEventAsBytes(Long runId, Date meetingTime) {        
        def now = new Date()
        def startTime = meetingTime
        def endTime 
        use(TimeCategory) {
            endTime = startTime + 1.hour
        }
        
        def nowStr = getCalendarTimeString(now)
        def startTimeStr = getCalendarTimeString(startTime)
        def endTimeStr = getCalendarTimeString(endTime)
        
        def organizer = "Pugh Lab Sequencing"
        def organizerEmail = "dus73@psu.edu"
        
        def ical = """BEGIN:VCALENDAR
PRODID:-
VERSION:2.0
CALSCALE:GREGORIAN
METHOD:REQUEST
BEGIN:VTIMEZONE
TZID:America/New_York
BEGIN:STANDARD
DTSTART:16010101T020000
TZOFFSETTO:-0500
TZOFFSETFROM:-0400
TZNAME:EST
END:STANDARD
BEGIN:DAYLIGHT
DTSTART:16010101T020000
TZOFFSETTO:-0400
TZOFFSETFROM:-0500
TZNAME:EDT
END:DAYLIGHT
END:VTIMEZONE
BEGIN:VEVENT
ORGANIZER;CN=${organizer}:mailto:${organizerEmail}
DTSTART;TZID="America/New_York":${startTimeStr}
DTEND;TZID="America/New_York":${endTimeStr}
TZOFFSETTO:-0500
TZOFFSETFROM:-0400
DTEND:${endTimeStr}
SUMMARY:Sequence Run ${runId} Meeting
UID:PEGR-SEQUENCING-MEETING-${nowStr}
DTSTAMP:${nowStr}
END:VEVENT
END:VCALENDAR
"""

        return ical.getBytes('UTF-8')
    }
}