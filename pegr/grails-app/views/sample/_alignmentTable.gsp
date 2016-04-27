<div class="table-responsive">
    <table class="table table-striped">
        <thead>
            <tr>
                <th>Genome</th>
                <th>Read Count</th>
                <th>Mapped Read Count</th>
                <th>Uniquely Mapped Count</th>
                <th>Deduplicated Count</th>
                <th>Index Mismatch</th>
                <th>Spike-in Count</th>
                <th>Adapter Count</th>
                <th>Avg. Insert Size</th>
                <th>IP Strength</th> 
                <th>Genome Coverage</th>   
                <th>Bam File</th>
            </tr>
        </thead>
        <tbody>
            <g:each in="${alignmentList}" var="alignment">
                <tr>
                    <td>${alignment.genome}</td>
                    <td>${alignment.alignmentStats?.totalReads}</td>
                    <td>${alignment.alignmentStats?.mappedReads}</td>
                    <td>${alignment.alignmentStats?.uniquelyMappedReads}</td>
                    <td>${alignment.alignmentStats?.dedupUniquelyMappedReads}</td>
                    <td>${alignment.alignmentStats?.indexMismatch}</td>
                    <td>${alignment.alignmentStats?.spikeInCount}</td>
                    <td>${alignment.alignmentStats?.adapterCount}</td>
                    <td>${alignment.alignmentStats?.avgInsertSize}</td>
                    <td>${alignment.alignmentStats?.ipStrength}</td>
                    <td>${alignment.alignmentStats?.genomeCoverage}</td>
                    <td>${alignment.bamFilePath}</td>
                </tr>
            </g:each>              
            <tr>
                <td colspan="7"></td>
            </tr>
        </tbody>
      </table>
</div>