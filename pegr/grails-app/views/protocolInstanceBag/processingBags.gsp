<html>
<head>
  <title>Cell Source</title> 
  <meta name="layout" content="main"/>
</head>
<body>
    <h4>Protocol Instance Bags</h4>
    <ul class="nav nav-tabs">
        <li class="active"><a href="#">Processing</a></li>
        <li><g:link action="completedBags">Completed</g:link></li>   
        <li><g:link action='create'>New</g:link></li>
    </ul>
    <ul class="list-group">
        <g:each in="${bags}">
        <li class="list-group-item"><g:render template="/protocolInstanceBag/overview" bean="${it}"></g:render></li>
        </g:each>
    </ul>
    <div class="pagination">
        <g:paginate next="Next" prev="Prev" total="${bags.totalCount ?: 0}" />
    </div>
     <script>
        $("#nav-bench").addClass("active");
     </script>
</body>
</html>