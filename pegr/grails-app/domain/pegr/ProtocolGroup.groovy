package pegr

class ProtocolGroup {

	String name
	List protocols
	Date dateCreated
    
	String toString() {
		name
	}
	
	static hasMany = [protocols: Protocol]
	
    static constraints = {
    	name unique: true
	}
    
    static mapping = {
        sort dateCreated:"desc"
    }
}
