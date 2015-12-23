<!DOCTYPE html>
<html>
	<head>

		<g:set var="entityName" value="${message(code: 'item.label', default: 'Item')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
		<script type="text/javascript" >
		function getScan(){
		    var href=window.location.href;
		    var ptr=href.lastIndexOf("#");
		    if(ptr>0){
		        href=href.substr(0,ptr);
		    }
		    window.location.href="zxing://scan/?ret="+escape(href+"#{CODE}");
		}
		
		var changingHash=false;
		function getHash(){
		    if(!changingHash){
		        changingHash=true;
		        var hash=window.location.hash.substr(1);
		        document.getElementById('barcode').value=unescape(hash);
		        changingHash=false;
		    }else{
		        //Do something with barcode here
		    }
		}
		</script>
	</head>
	<body onhashchange="getHash()">
		<ul class="nav nav-pills">
			<li><a class="home" href="${createLink(uri: '/admin/')}"><g:message code="default.home.label"/></a></li>
			<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
		</ul>
		<div id="create-item" class="content scaffold-create" role="main">
			<h1><g:message code="default.create.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${itemInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${itemInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form action='save' >
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
