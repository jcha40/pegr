package pegr
import grails.transaction.Transactional

class SequenceRunException extends RuntimeException {
    String message
}

class SequenceRunService {
    def springSecurityService
    
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
        def messages = []
        def samples = fetchSamplesInPool(poolItemId)
        samples.each {
            if (SequencingExperiment.findBySampleAndSequenceRun(it, run)) {
                messages.push("Sample already included in this run!")
            } else {
                def experiment = new SequencingExperiment(sample: it, sequenceRun: run) 
                if (!experiment.save(flush: true)) {
                    message.push("Error including this sample to the sequence run!")
                } 
            }
        }
        return messages
    }
    
    List fetchSamplesInPool(Long poolItemId) {
        def pool = Item.get(poolItemId)
        if (!pool) {
            throw new SequenceRunException(message: "Pool not found!")
        }
        def instance = ProtocolInstanceItems.findAllByItem(pool).last()
        return instance.bag.tracedSamples
    }
    
    @Transactional
    void removeExperiment(Long experimentId) {
        def experiment = SequencingExperiment.get(experimentId)
        if (experiment) {
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
        
        def walleService = new WalleService()
        walleService.addToQueue(runId)
        
        // start the run by creating a job on remote server
        run.status = RunStatus.QUEUE
        run.save()
    }
}