package pegr

class SecurityFilters {

    def springSecurityService
    
    def filters = {
        
        // allow admin or anyone who has a role in this project to view 
        projectShow(controller:'project', action:'show') {
            before = {
                def currUser = springSecurityService.currentUser
                if (currUser.isAdmin()) {
                    return true
                }
                def projectId = params.long('id')
                if (ProjectUser.where {project.id == projectId && user == currUser}.get(max: 1)) {
                    return true                    
                } else {
                    render(view: '/login/denied')
                    return false
                }
            }
        }
        
        projectAllAndCreate(controller: 'project', action: 'create|all') {
            before = {
                def currUser = springSecurityService.currentUser
                if (currUser.isAdmin()) {
                    return true
                } else {
                    render(view: '/login/denied')
                    return false
                }
            }    
        }
        
        projectEdit(controller:'project', action:'edit|addUserAjax|removeUserAjax|editUserRoleAjax') {
            before = {
                def currUser = springSecurityService.currentUser
                if (currUser.isAdmin()) {
                    return true
                }
                def projectId = params.long('projectId')
                if (ProjectUser.where {project.id == projectId && user == currUser && projectRole == ProjectRole.OWNER}.get(max: 1)) {
                    return true                    
                } else {
                    render(view: '/login/denied')
                    return false
                }
            }
        }     
                
        AntibodyEdit(controller: 'antibody', action: 'edit|update|delete|editItem|updateItem|addBarcode') {
            before = {
                def antibodyId = params.long('antibodyId')
                def antibody = Antibody.get(antibodyId)
                def currUser = springSecurityService.currentUser
                if (currUser.isAdmin() || antibody?.item?.user == currUser) {
                    return true
                } else {
                    render(view: '/login/denied')
                    return false
                }
            }
        }

        CellSourceEdit(controller: 'cellSource', action: 'edit|update|addTreatment') {
            before = {
                def cellSourceId = params.long('cellSourceId')
                def cellSource = CellSource.get(cellSourceId)
                def currUser = springSecurityService.currentUser
                if (currUser.isAdmin() || cellSource?.item?.user == currUser) {
                    return true
                } else {
                    render(view: '/login/denied')
                    return false
                }
            }
        }
        
        ItemEdit(controller: 'item', action: 'edit|update|upload|deleteImage|delete') {
            before = {
                def itemId = params.long('itemId')
                def item = Item.get(itemId)
                def currUser = springSecurityService.currentUser
                if (currUser.isAdmin() || item?.user == currUser) {
                    return true
                } else {
                    render(view: '/login/denied')
                    return false
                }
            }
        }

        ProtocolInstanceBagEdit(controller: "protocolInstanceBag", action: "searchItemForBag|previewItemAndBag|addItemToBag|addSubBagToBag|removeItemFromBag") {
            before = {
                def bagId = params.long('bagId')
                def bag = ProtocolInstanceBag.get(bagId)
                if (bag.status != ProtocolStatus.COMPLETED) {
                    return true
                } else {
                    flash.message = "The protocol instance bag is completed and no changes are allowed. Please contact lab admin if you have further questions."
                    redirect(action: "showBag", id: bagId)
                }
            }    
        }
        
        ProtocolInstanceEdit(controller: "protocolInstanceBag", action: "searchItemForInstance|searchItemForTypeInstance|previewItemInInstance|addPoolToInstance|addItemToInstance|saveItemInInstance|removeItemFromInstance|addIndex|searchAntibody|previewAntibody|addAntibodyToSample|removeAntibody|addChild|removeChild") {
            before = {
                def instanceId = params.long('instanceId') 
                def instance = ProtocolInstance.get(instanceId)
                if (instance.user) {
                    def currUser = springSecurityService.currentUser
                    if (!currUser.isAdmin() && item?.user != currUser) {
                        render(view: '/login/denied')
                        return false
                    }
                }                
                if (instance?.bag) {
                    if (instance.bag.status == ProtocolStatus.COMPLETED) {
                        flash.message = "The protocol instance bag is completed and no changes are allowed. Please contact lab admin if you have further questions."
                        redirect(action: "showBag", id: instance.bag.id)
                    }
                } 
                return true
            }
        }
    }
}
