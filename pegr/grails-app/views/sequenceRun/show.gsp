<html>
<head>
    <title>Workbench</title> 
    <meta name="layout" content="main"/>
</head>
<body>
<div class="container-fluid">
    <g:link action="index"><span class="glyphicon glyphicon-home"></span> Sequencing Run List</g:link>   
    <h2>Sequence Run #${run.runNum}  <small><span class="label label-default">${run.status}</span></small></h2>
    <h3>Summary</h3>
    <g:render template="summaryDetails"></g:render>    
    <h3>Samples</h3>
    <table class="table table-striped">
        <thead>
            <tr>
                <th>Sample ID</th>
                <th>Strain</th>
                <th>Antibody</th>
                <th>Index</th>
                <th>Genome Build</th>
            </tr>
        </thead>
        <tbody>
            <g:each in="${run.experiments}">
                <tr>
                    <td><g:link controller="sample" action="show" id="${it.sample.id}">${it.sample?.id}</g:link></td>
                    <td>${it.sample?.cellSource?.strain}</td>
                    <td>${it.sample?.antibody}</td>
                    <td>${it.sample?.sequenceIndicesDetailString}</td>
                    <td>${it.genomesString}</td>                        
                </tr>
            </g:each>              
            <tr>
                <td colspan="5"></td>
            </tr>
        </tbody>
    </table>

    <script>
        $("#nav-bench").addClass("active");
     </script>
</div>
</body>
</html>