package pegr

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*
import pegr.AdminCategory
import pegr.IgType

class IgTypeAdminController {

    IgTypeService igTypeService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max, String str) {
        if (str && IgType.hasProperty("name")) {
            def c = IgType.createCriteria()
            def listParams = [
                    max: max ?: 25,
                    sort: params.sort ?: "id",
                    order: params.order ?: "desc",
                    offset: params.offset
                ]
            def likeStr = "%" + str + "%"
            def items = c.list(listParams) {
                or {
                    ilike "name", likeStr
                }
            }
            respond items, model:[igTypeCount: items.totalCount, str: str]
        } else {       
            params.max = Math.min(max ?: 25, 100)
            respond IgType.list(params), model:[igTypeCount: IgType.count()]
        }
    }
    def show(Long id) {
        respond igTypeService.get(id)
    }

    def create() {
        respond new IgType(params)
    }

    def save(IgType igType) {
        if (igType == null) {
            notFound()
            return
        }

        try {
            igTypeService.save(igType)
        } catch (ValidationException e) {
            respond igType.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'igType.label', default: 'IgType'), igType.id])
                redirect action: "show", id: igType.id
            }
            '*' { respond igType, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond igTypeService.get(id)
    }

    def update(IgType igType) {
        if (igType == null) {
            notFound()
            return
        }

        try {
            igTypeService.save(igType)
        } catch (ValidationException e) {
            respond igType.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'igType.label', default: 'IgType'), igType.id])
                redirect action: "show", id: igType.id
            }
            '*'{ respond igType, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        igTypeService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'igType.label', default: 'IgType'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'igType.label', default: 'IgType'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
    
	public static AdminCategory category = AdminCategory.BIO_SPECIFICATIONS
}
