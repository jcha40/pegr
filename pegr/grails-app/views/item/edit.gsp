<html>
<head>
    <title>Workbench</title> 
    <meta name="layout" content="main"/>
</head>
<body>
<div>
    <g:if test="${request.message}">
        <div class="message" role="status">${request.message}</div>
    </g:if>
    <g:hasErrors>
        <div class="errors">
            <g:renderErrors bean="${item}" as="list"/>
            <g:renderErrors bean="${object}" as="list"/>
        </div>
    </g:hasErrors>
    <g:form action="update" class="fields" role="form" method="post">
        <g:hiddenField name="itemId" value="${item.id}"></g:hiddenField>
        <g:render template="/item/form" model="['item': item]"></g:render>
        <g:submitButton class="btn btn-primary" name="save" value="Save"/>
        <g:link class="btn btn-default" action="show" id="${item.id}">Cancel</g:link>
    </g:form>

    <script>
        $("#nav-bench").addClass("active");
     </script>
</div>
</body>
</html>