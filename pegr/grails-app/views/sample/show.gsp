
<%@ page import="pegr.Sample" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title>Sample</title>
</head>
<body>
    <div class="container-fluid">
        <div>
            <g:link action="index"><span class="glyphicon glyphicon-home"></span> Sample List</g:link>
        </div>     
        <h3>
            Sample ${sample.id} <g:if test="${sample.sourceId}">(${sample.source}#${sample.sourceId})</g:if>
            <small><span class="label label-default">${sample.status}</span></small> 
        </h3>
        <g:if test="${sample?.date}">
            <p>Date: <g:formatDate format="yyyy-MM-dd" date="${sample.date}"/></p>
        </g:if>
        <g:render template="details" model="['sample': sample]"></g:render>
        <div id="seq">
            <h4>Sequencing Experiments</h4>
            <ol>
                <g:each in="${sample.sequencingExperiments}" var="experiment">
                    <li>
                        <h5><g:link controller="sequenceRun" action="show" id="${experiment.sequenceRun.id}">Sequence Run ${experiment.sequenceRun.id}(Old No.${experiment.sequenceRun.runNum})</g:link></h5>
                        <g:render template="experimentTable" model="['experiment':experiment]"></g:render>
                        <h5>Alignments</h5>
                        <ul>
                            <g:each in="${experiment.alignments}" var="alignment">
                                <li>
                                    <g:render template="alignmentTable" model="['alignment':alignment]"></g:render>
                                </li>
                            </g:each>
                        </ul>
                    </li>
                </g:each>

            </ol>
        </div>

        <div id="bioRep">
            <h4>Biological Replicates</h4>
            <g:each in="${sample.bioReps}">${it}</g:each>
        </div>  
        <div id="techRep">
            <h4>Technical Replicates</h4>
            <g:each in="${sample.techReps}">${it}</g:each>
        </div> 
        <div id="project">
            <h4>Related Projects</h4>
            <ol>
            <g:each in="${sample.projects}">
                <li><g:link controller="project" action="show" id="${it.id}">${it}</g:link> <i>Created on <g:formatDate format="yyyy-MM-dd" date="${it.dateCreated}"/></i></li>
            </g:each>
            </ol>
        </div>
    </div>
    <script>
        $("#nav-metadata").addClass("active");      
    </script>
</body>
</html>
