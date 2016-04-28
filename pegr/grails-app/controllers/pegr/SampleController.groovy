package pegr
import groovy.json.*
    
class SampleController {
    
    def springSecurityService
    
    def index(Integer max) {
        params.max = Math.min(max ?: 15, 100)
        if (!params.sort) {
            params.sort = "id"
            params.order = "desc"
        }
        def samples = Sample.where{status == SampleStatus.COMPLETED}.list(params)

        [sampleList: samples, sampleCount: Sample.count()]
    }
    
	def show(Long id) {
		def sample = Sample.get(id)
        if (sample) {
            def jsonSlurper = new JsonSlurper()
            def notes = [:]
            try {
                notes += jsonSlurper.parseText(sample.note)
            }catch(Exception e){

            }
            try {
                notes += jsonSlurper.parseText(sample.prtclInstSummary.note)
            }catch(Exception e){

            }        
            def protocols = []
            sample.bags.each{ linkedbag ->
                protocols.push([bag:linkedbag, protocolList:ProtocolInstance.where{bag.id == linkedbag.id}.list(sort: "bagIdx", order: "asc")])
            }
            def currentUser = springSecurityService.currentUser
            def authorized = currentUser.isAdmin() ||  (currentUser.authorities.any { it.authority == "ROLE_MEMBER" }) 
            [sample: sample, notes: notes, protocols: protocols, authorized: authorized]            
        } else {
            render status: 404
        }

	}
    
    def showChecked(){
        
        def ids
        if (session.checkedSample){
            session.checkedSample.append(params.checkedSample)
        }else{
            ids = params.checkedSample
        }
        
        def sampleList = []
        ids.each{
            def sample = Sample.get(Long.parseLong(it))
            if (sample) {
                sampleList.push(sample)
            }
        }
        session.checkedSample = null
        [sampleList: sampleList, ids: ids]
    }
    
    def searchForm() {
        
    }
    
    def search() {       
        def sampleProps = Sample.metaClass.properties*.name
        def c = Sample.createCriteria()
        params.max = 15
        def samples = c.list(params) {
            and {
               if (params.species) {
                    cellSource {
                        strain {
                            species {
                                ilike "name", "%${params.species}%"
                            }
                        }
                    }
                }
                if (params.strain) {
                    cellSource {
                        strain {
                            ilike "name", "%${params.strain}%"
                        }                    
                    }
                }
                if (params.antibody) {
                    antibody {
                        ilike "catalogNumber", "%${params.antibody}%"
                    }
                }
                if (params.id) {
                    eq "id", Long.parseLong(params.id)
                }
                if (params.sourceId) {
                    eq "sourceId", params.sourceId
                }
                if (params.source) {
                    ilike "source", "%${params.source}%"
                }
                if (params.target) {
                    target {
                        ilike "name", "%${params.target}%"
                    }
                }
                eq("status", SampleStatus.COMPLETED)
            }
            order("id", "desc")
        }
        [sampleList: samples]
    }
    
    def clearCheckedSampleAjax(){
        if (session.checkedSample) {
            session.checkedSample = null
        }
        return
    }
    
}
