package pegr
import static org.springframework.http.HttpStatus.*
import org.springframework.web.multipart.MultipartHttpServletRequest 
import javax.imageio.ImageIO
import java.awt.image.BufferedImage


class YepController {
	//takes the text from the sampleID.txt and throws the info into a map for the template function
	def textinfo(id){
		String filecontents = new File('grails-app/views/yep/'+id+'.txt').text //text to string
		def filearr = filecontents.split('\n') //remove the headers
		filecontents = filearr[1] //recast into string
		filearr=filecontents.split('\t') //remove the tabs
		Map wordMap=[:]
		wordMap['COMMONSYSTEMATIC']=filearr[0] + '/'+ filearr[1]
		wordMap['SAMPLEID']=filearr[2]
		wordMap['CONDITION']=filearr[3]
		wordMap['DESCRIPTION']=filearr[4]
		wordMap['XSITES']=filearr[5]
		wordMap['YSITES']=filearr[6]
		wordMap['ZSITES']=filearr[7]
		return wordMap

	}
	

	def template() {
		def sampId = request.getParameter("id")
		render (view: "mastertemplate.gsp", model: [sampleID: sampId, text:textinfo(sampId)])
	}
	
}