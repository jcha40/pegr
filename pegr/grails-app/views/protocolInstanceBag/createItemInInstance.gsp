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
    <h4>Add Item </h4>
    <p>Item not found! Do you want to add it as a new item?</p>
    <button class="btn btn-primary" onClick="$('form').show()">Yes</button>
    <g:link class="btn btn-default" action="searchItemForInstance" params="[instanceId:instanceId]">No</g:link>

    <g:form action="saveItemInInstance" class="fields" role="form" method="post">
        <g:hiddenField name="instanceId" value="${instanceId}"/>
        <g:render template="/item/form" bean="${item}" var="item"></g:render>
        <g:submitButton class="btn btn-primary" name="save" value="Save"/>
    </g:form>

    <script>
        $("#nav-bench").addClass("active");
        $("form").hide();
     </script>
</div>
</body>
</html>