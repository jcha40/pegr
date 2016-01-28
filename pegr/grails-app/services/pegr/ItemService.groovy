package pegr
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import grails.transaction.Transactional

class ItemException extends RuntimeException {
    String message
    Item item
}

class ItemService {

    def grailsApplication
    
    @Transactional
    def save(GrailsParameterMap params){
        try {
            def item = new Item(params)
            def itemType = ItemType.get(item.type.id)
            if(itemType?.objectType){
                def dc = getClassFromObjectType(itemType.objectType)
                def object = dc.clazz.newInstance(params)
                object.save(flush: true)
                item.referenceId = object.id
            }
            item.save(flush: true)
        }catch(Exception e) {
            log.error "Error: ${e.message}", e
            throw new ItemException(message: "Error saving this item!")
        }
    }
    
    @Transactional
    def update(GrailsParameterMap params){
        try {
            def item = Item.get(params.long('itemId'))
            item.properties = params
            def object = getObjectFromItem(item.type.objectType, item.referenceId)
            object.properties = params
            object.save(flush: true)   
            item.save(flush: true)
        }catch(Exception e) {
            log.error "Error: ${e.message}", e
            throw new ItemException(message: "Error saving this item!")
        }
    }
    
    def getClassFromObjectType(String type) {
        return grailsApplication.getDomainClass(grails.util.Metadata.current.getApplicationName()+ "." + type)
    }
    
    def getObjectFromItem(String type, Long referenceId) {
        try {
            def c = grailsApplication.getDomainClass(grails.util.Metadata.current.getApplicationName()+ "." + type)
            def object = c.clazz.get(referenceId)
            return object
        }catch(Exception e) {
            return null
        }
    }
}
