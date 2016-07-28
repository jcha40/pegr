<h3>Samples</h3>
<p>The number of Samples: ${sampleDTOs?.size()}</p>
<ul class="nav nav-tabs">
    <li class="active"><a data-toggle="tab" href="#sample">Description</a></li>
    <li><a data-toggle="tab" href="#epitope">Epitope Tags &amp; FastQC</a></li>
</ul>

<div class="tab-content">
    <div id="sample" class="tab-pane fade in active">
        <g:render template="/report/sampleTable" model="['sampleList':sampleDTOs]" />
    </div>
    <div id="epitope" class="tab-pane fade">
        <g:render template="/report/epitopeTable" model="['sampleList':sampleDTOs]" />
    </div>
</div>
<h3>Mapping Statistics</h3>
<ul class="nav nav-tabs">
    <li class="active"><a data-toggle="tab" href="#alignment"> Number of Tags</a></li>
    <li><a data-toggle="tab" href="#alignment2"> Percentage of Tags</a></li>
    <li><a data-toggle="tab" href="#alignment3"> Additional Information</a></li>
</ul>

<div class="tab-content">
    <div id="alignment" class="tab-pane in active">
        <g:render template="/report/alignmentTable" model="['sampleList':sampleDTOs]" />
    </div>
    <div id="alignment2" class="tab-pane fade">
        <g:render template="/report/alignmentTable2" model="['sampleList':sampleDTOs]" />
    </div>
    <div id="alignment3" class="tab-pane fade">
        <g:render template="/report/alignmentTable3" model="['sampleList':sampleDTOs]" />
    </div>
</div>
<h3>Downstream Analysis</h3>
<ul class="nav nav-tabs">
    <li class="active"><a data-toggle="tab" href="#peak">Peak Statistics</a></li>
</ul>

<div class="tab-content">
    <div id="peak" class="tab-pane fade in active">
        <g:render template="/report/peakTable" model="['sampleList':sampleDTOs]" />
    </div>
</div>
<ul>
    <li>
        <h4>MEME Motifs</h4>
        <g:each in="${sampleDTOs}" var="sample">
            <g:each in="${sample.experiments}" var="experiment">
                <g:each in="${experiment.alignments}" var="alignment">
                    <h5>Sample ${sample.id} Run ${experiment.runId} Genome ${alignment.genome}</h5>
                    <iframe src="/pegr/report/meme?url=${alignment.memeFile}" width=800 height=200 scrolling=no frameBorder=0></iframe>
                </g:each>
            </g:each>
        </g:each>
    </li>
</ul> 