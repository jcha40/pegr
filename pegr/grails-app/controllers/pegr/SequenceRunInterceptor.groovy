package pegr

class SequenceRunInterceptor {

    def springSecurityService
    
    boolean before() { 
        if (actionName in ['editRead',
                           'updateRead',
                           'editInfo',
                           'update',
                           'searchPool',
                           'addPool',
                           'removePool',
                           'addSamplesById',
                           'removeExperiment',
                           'editSamples',
                           'updateSamples',
                           'run',
                           'fetchProjectsAjax',
                           'updateExperimentCohortAjax',
                           'addProject',
                           'removeProject',
                           'uploadCohortImage',
                           'removeCohortImageAjax',
                           'deleteSample',
                           'deleteProject']) {
            def runId = params.long('runId')
            def run =  SequenceRun.get(runId)
            if (run?.user) {
                def currUser = springSecurityService.currentUser
                if (!currUser.isAdmin() && run.user != currUser) {
                    render(view: '/login/denied')
                    return false
                }
            }
            if (run?.status == RunStatus.COMPLETED) {
                def message = "This sequence run is completed and no changes are allowed. Please contact lab admin if you have further questions."
                render(view: '/login/denied', model: [message: message])
                return false
            }
        } 
        return true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
