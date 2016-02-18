package pegr

class AntibodyController {
    def itemService
    def antibodyService
    
    def index(){
        redirect(action: "list")
    }
    
    def list(Integer max) {
        params.max = Math.min(max ?: 15, 100)
        def itemType = ItemType.findByCategory(ItemTypeCategory.ANTIBODY)
        [objectList: Antibody.list(params), objectCount: Antibody.count(), currentType: itemType]
    }
    
    def show(Long id) {
        def antibody = Antibody.get(id)
        if (antibody) {
            [antibody: antibody]
        } else {
            render status: 404
        }
    }

    def create(Item item, Antibody antibody, String barcode) {
        if (!item) {
            def type = ItemType.findByName("Antibody")
            item = new Item(barcode: barcode, type: type)
        }                
        [item: item, antibody: antibody]
    }
    
    def save() {
        withForm{        
            def item = new Item(params)
            def object = new Antibody(params)
            try {
                itemService.save(item, object)
                flash.message = "New traced sample added!"
                redirect(action: "show", id: object.id)
            }catch(ItemException e) {
                request.message = e.message
                render(view: "create", model: [item:item, object: object])
            }catch(Exception e) {
                log.error "Error: ${e.message}", e
                request.message = "Error saving this item!"
                render(view: "create", model: [item:item, object: object])
            }
        }
    }
    
    def edit(Antibody antibody){
        [antibody: antibody]
    }
    
    def update() {
        def antibody = Antibody.get(params.long('id'))
        antibody.properties = params
        try {
            antibodyService.save(antibody)
            flash.message = "Antibody update!"
            redirect(action: "show", id: params.id)
        }catch(ItemException e) {
            request.message = e.message
            render(view:'edit', model:[antibody: antibody])
        }
    }
    
    def editItem(Long antibodyId) {
        def antibody = Antibody.get(antibodyId)
        if (antibody) {            
            def item = antibody.item
            if (!item) {
                redirect(action: "addBarcode", params: [antibodyId: antibodyId])
            } else {
                [item: antibody.item, antibodyId: antibodyId]
            }
        } else {
            flash.message = "Antibody not found!"
            redirect(action: 'list')
        }
    }
    
    def updateItem(Long antibodyId, Long itemId) {
        def item = Item.get(itemId)
        item.properties = params
        try {
            itemService.save(item)
            flash.message = "Antibody update!"
            redirect(action: "show", id: antibodyId)
        }catch(ItemException e) {
            request.message = e.message
            render(view:'editItem', model:[item: item, antibodyId: antibodyId])
        }
    }
    
    
    def addBarcode(Long antibodyId) {
        def object = Antibody.get(antibodyId)
        if (object) {        
            if (request.method == "POST") {
                withForm {
                    def item = new Item(params)
                    try {
                        itemService.save(item, object)
                        flash.message = "Barcode added!"
                        redirect(action: "show", id: antibodyId)
                    }catch(ItemException e) {
                        request.message = e.message
                        render(view: "addBarcode", model: [item:item, antibodyId: antibodyId])
                    }                    
                }
            } else {
                def type = ItemType.findByName("Antibody")
                def item = new Item(type: type)
                [item:item, antibodyId: antibodyId]
            }
        } else {
            flash.message = "Antibody not found!"
            redirect(action: 'list')
        }
    }
    
    def delete(Long id) {
        try {   
            antibodyService.delete(id)
            flash.message = "Antibody deleted!"
            redirect(action: 'list')
        }catch(AntibodyException e) {
            flash.message = e.message
            redirect(action: 'show', id: id)
        }        
    }
    
}