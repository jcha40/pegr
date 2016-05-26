<div class="table-responsive">
    <table class="table table-striped">
        <thead>
            <tr>
                <th>Sample ID</th>
                <th>Sequence Run</th>
                <th>Genome</th>
                <th>Read Count</th>
                <th>Index Mismatch</th>
                <th>Adapter Count</th>
                <th>Mapped Read Count</th>
                <th>Uniquely Mapped Count</th>
                <th>Deduplicated Count</th>
                <th>Duplication Level</th>
                <th>Insert Size</th>
                <th>Genome Coverage</th>   
            </tr>
        </thead>
        <tbody>
            <g:each in="${alignmentList}" var="alignment">
                <tr>
                    <td><g:link controller="sample" action="show" id="${alignment.sequencingExperiment?.sample?.id}">${alignment.sequencingExperiment?.sample?.id}</g:link></td>    
                    <td><g:link controller="sequenceRun" action="show" id="${alignment.sequencingExperiment?.sequenceRun?.id}">${alignment.sequencingExperiment?.sequenceRun?.id} (Old ${alignment.sequencingExperiment?.sequenceRun?.runNum}) </g:link></td>
                    <td>${alignment.genome}</td>
                    <td>${alignment.sequencingExperiment?.totalReads}</td>
                    <td>${alignment.sequencingExperiment?.indexMismatch}</td>
                    <td>${alignment.sequencingExperiment?.adapterCount}</td>
                    <td>${alignment.mappedReads}</td>
                    <td>${alignment.uniquelyMappedReads}</td>
                    <td>${alignment.dedupUniquelyMappedReads}</td>
                    <th>${alignment.seqDuplicationLevel}</th>
                    <td>${alignment.avgInsertSize} <g:if test="${alignment.stdDevInsertSize}">&plusmn;${alignment.stdDevInsertSize}</g:if></td>
                    <td>${alignment.genomeCoverage}</td>
                </tr>
            </g:each>              
            <tr>
                <td colspan="7"></td>
            </tr>
        </tbody>
      </table>
</div>