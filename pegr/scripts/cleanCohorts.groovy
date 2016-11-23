package pegr
import groovy.sql.Sql
import com.opencsv.CSVParser
import com.opencsv.CSVReader
import groovy.json.*
/*
def minRun = 550
def maxRun = 637

// import the images
def reportImages = [:]
def filename = "/Users/dus73/csvFiles/attached_images.csv"

def startLine = 1
def endLine = 111

def lineNo = 0
def file = new FileReader(filename)
CSVReader reader = new CSVReader(file);
String [] rawdata;

while ((rawdata = reader.readNext()) != null) {
    ++lineNo
    if (lineNo < startLine) {
        continue
    } else if (endLine > 0 && lineNo > endLine) {
        break
    }
    reportImages[rawdata[0]]= [sonication: rawdata[1]+".png", gel: rawdata[2]+".png"]
}

def dataSource = grailsApplication.mainContext.getBean('dataSource')
def sql = new Sql(dataSource)
(minRun..maxRun).each { runNum ->
    def pegrRun = SequenceRun.findByRunNum(runNum)
    def cmd = "select RUN_NAME, UNIQ_ID, SUMMARY_REPORT from pughlab.PughLabSequencingRunSampleInfo where run_name = ?"
    def sampleRows = sql.rows(cmd, [runNum.toString()])
    def reports = sampleRows.groupBy({ sample -> sample.SUMMARY_REPORT})
    reports.each { reportName, samples ->
        def cohort = null
        samples.each { sample ->
            def sourceId = sample.UNIQ_ID[-5..-1]
            def pegrSample = Sample.findBySourceId(sourceId)
            if (pegrSample) {
                def pegrExp = SequencingExperiment.findBySample(pegrSample)
                if (pegrExp) {
                    if (cohort) {
                        if (pegrExp.cohort != cohort) {
                            if (pegrExp.cohort) {
                                def oldProjectSample =  ProjectSamples.findByProjectAndSample(pegrExp.cohort.project, pegrSample)
                                if (oldProjectSample) {
                                    oldProjectSample.delete(failOnError: true)
                                }
                            }
                            new ProjectSamples(project: cohort.project, sample: pegrSample).save(failOnError: true)
                            pegrExp.cohort = cohort
                            pegrExp.save(failOnError: true)
                        } 
                    } else {
                        // parse reportName
                        def reportNameParts = reportName.tokenize("_-")
                        def username = reportNameParts[1]
                        def dateStr = reportNameParts[2]
                        // check the cohort
                        if (pegrExp.cohort) {
                            cohort = pegrExp.cohort
                        } else {                            
                            def project = Project.findByName(reportName)
                            if (!project) {
                                def date 
                                try {
                                    date = new Date().parse("yyMMdd", dateStr)
                                } catch(Exception e) {

                                }
                                if (!date) {
                                    date = pegrRun.date ?: new Date()
                                }
                                def user = User.findByUsername(username)
                                println dateStr
                                println date
                                project = new Project(name: reportName, dateCreated: date, lastUpdated: date)
                                project.save(failOnError: true, flush: true)
                                project.dateCreated = date
                                project.lastUpdated = date
                                project.save()
                                if (user) {
                                    new ProjectUser(project: project, user: user, projectRole: ProjectRole.OWNER).save(failOnError: true)
                                }
                                new ProjectSamples(project: project, sample: pegrSample).save(failOnError: true)
                            }
                            cohort = SequencingCohort.findByProjectAndRun(project, pegrRun)
                            if (!cohort) {
                                cohort = new SequencingCohort(project: project, run: pegrRun)
                                cohort.save(failOnError: true)
                            }                            
                            pegrExp.cohort = cohort
                            pegrExp.save(failOnError: true)
                        }
                        // update cohort images
                        if (reportImages.containsKey(reportName)) {
                            cohort.images = JsonOutput.toJson([gel: [reportImages[reportName].gel], sonication: [reportImages[reportName].sonication]])
                            cohort.save(failOnError: true)
                        }
                        cohort = pegrExp.cohort
                    }
                }
            }
        }
    }
}
*/
SequencingCohort.list().each {cohort ->
    if (cohort.experiments.size() == 0) {
        cohort.delete()     
    }
}

Project.list().each {project ->
    if (project.samples.size() == 0) {
        ProjectUser.executeUpdate("delete from ProjectUser where project.id=:projectId", [projectId: project.id])
        project.delete()
    }
}

SummaryReport.list().each {report ->
    if (!report.cohort) {
        report.delete()
    }
}
