<html>
<head>
    <title>Workbench</title> 
    <meta name="layout" content="main"/>
    <script type="text/javascript" >
        var changingHash=false;
    </script>
</head>
<body  onhashchange="getHash()">
<div class="container-fluid">
    <g:link action="showBag" id="${bagId}"><span class="glyphicon glyphicon-menu-left"></span> Back</g:link>

    <h4>Add Traced Sample </h4>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:form class="fields" role="form" action="previewItemAndBag" >
        <g:hiddenField name="bagId" value="${bagId}"/>
        <g:render template="/item/search" model="['currentSuperCategory':pegr.ItemTypeSuperCategory.TRACED_SAMPLE]"></g:render>
        <g:submitButton class="btn btn-primary" name="search" value="Search Or Create"/>
    </g:form>                    
    <script>
        $("#nav-experiments").addClass("active");
        $("select").select2();
     </script>
</div>
</body>
</html>