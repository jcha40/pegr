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
        <h4>Quality Control Analysis</h4>
        <g:each in="${sampleDTOs}" var="sample">
          <g:each in="${sample.experiments}" var="experiment">
            <g:each in="${experiment.alignments}" var="alignment">
              <h5>Sample <u>${sample.id} ${sample.naturalId}</u> &nbsp; Run <u>${experiment.runId}</u> &nbsp; Genome <u>${alignment.genome}</u> &nbsp; Target <u>${sample.target}</u> 
              </h5>
              <ul class="nav nav-tabs">
                <li class="active"><a data-toggle="tab" href="#${alignment.id}-meme-table">MEME Motifs</a></li>
                <g:each in="${0..<alignment.featureAnalysis.size()}" var="m">
                  <g:if test="${alignment.featureAnalysis[m]}">
                  <li><a data-toggle="tab" href="#${alignment.id}-composite${m}">${alignment.featureAnalysis[m].title}</a></li>
                  </g:if>
                </g:each>
              </ul>
              <div class="tab-content">
                <div id="${alignment.id}-meme-table" class="tab-pane in active"> 
                  <table class="table table-bordered meme-table">
                    <thead>
                        <tr>
                            <th rowspan="2">ID</th>
                            <th colspan="4" class="text-center"><a href="${alignment.memeFig}" target="_blank">Meme <span class="glyphicon glyphicon-picture"></span></a><span class="meme-url" hidden="hidden">${alignment.memeFile}</span></th>
                            <th rowspan="2">Four-Color</th>
                            <th rowspan="2">Composite</th>
                        </tr>
                        <tr>
                            <th>Logo</th>
                            <th>E-value</th>
                            <th>Sites</th>
                            <th>Width</th>
                        </tr>
                    </thead>
                    <tbody>
                        <g:if test="${alignment.motifCount}">
                        <g:each in="${(0..<alignment.motifCount)}" var="n">
                            <tr>
                                <td class="meme-id" style="width:20px"></td>
                                <td class="meme-fig" style="width:350px"><i class="fa fa-spinner fa-spin"></i></td>
                                <td class="meme-evalue" style="width:100px"></td>
                                <td class="meme-sites" style="width:100px"></td>
                                <td class="meme-width" style="width:100px"></td>
                                <td style="width:100px"><a href="${alignment.fourColor[n]}" target="_blank"><span class="glyphicon glyphicon-picture" style="font-size: 2em"></span></a></td>
                                <td class="composite" style="width:320px">
                                  <g:if test="${alignment.composite[n]}">
                                    <g:link controller="report" action="composite" params="[url: alignment.composite[n]]" target="_blank" class="pull-right"><span class="glyphicon glyphicon-fullscreen" style="z-index: 100"></span></g:link>
                                    <i class="fa fa-spinner fa-spin"></i>
                                    <span class="composite-url" hidden="hidden">${alignment.composite[n]}</span>
                                    <div class="composite-fig"></div>
                                  </g:if>
                                </td>
                            </tr>
                        </g:each>
                        </g:if>
                    </tbody>
                  </table> 
                </div>
                <g:each in="${0..<alignment.featureAnalysis.size()}" var="m">
                  <g:if test="${alignment.featureAnalysis[m]}">
                    <div id="${alignment.id}-composite${m}" class="featureAnalysis tab-pane fade">
                      <i class="fa fa-spinner fa-spin"></i>
                      <span class="composite-url" hidden="hidden">${alignment.featureAnalysis[m].tabular}</span>
                      <span class="composite-xlabel" hidden="hidden">${alignment.featureAnalysis[m].xlabel}</span>
                      <div class="composite-fig" style="width: 512px; height: 300px"></div>
                    </div>
                  </g:if>
                </g:each>
              </div>
            </g:each>
          </g:each>
        </g:each>
    </li>
</ul>
<script>
    $(function() {
        // plot meme
        $(".meme-table").each(function(){
            var memeTable = $(this);
            var memeUrl = $(this).find(".meme-url").text();
            $.ajax({ 
                url: "/pegr/report/fetchMemeDataAjax?url=" + memeUrl,
                success: 
                    function(result) {
                        memeTable.find(".meme-id").each(function(index, memeId) {
                            if (index < result.length) {
                                $(memeId).html(result[index].id);
                            }
                        });
                        memeTable.find(".meme-fig").each(function(index, memeFig) {
                            $(memeFig).find("i").remove();
                            if (index < result.length) {
                                const memeDrawer = new MemeDrawer();
                                memeDrawer.make_motif(memeFig, result[index]);
                            } else {
                                $(memeFig).html("No MEME data found!");
                            }                            
                        });
                        memeTable.find(".meme-evalue").each(function(index, memeEvalue) {
                            if (index < result.length) {
                                $(memeEvalue).html(result[index].evalue);
                            }
                        });
                        memeTable.find(".meme-sites").each(function(index, memeSites) {
                            if (index < result.length) {
                                $(memeSites).html(result[index].nsites);
                            }
                        });
                        memeTable.find(".meme-width").each(function(index, memeWidth) {
                            if (index < result.length) {
                                $(memeWidth).html(result[index].len);
                            }
                        });
                    },
            });  
        });
        
        // plot composites
        google.charts.load('current', {'packages':['corechart']});
        var t = 0;
        $(".composite").each(function(){
            t += 200;
            var compositeTd = $(this);
            var spinner = $(this).find("i");
            var compositeUrl = $(this).find(".composite-url").text();
            var container = $(this).find(".composite-fig")[0];
            setTimeout(function(){
                google.charts.setOnLoadCallback(function(){
                    $.ajax({
                        url: "/pegr/report/fetchCompositeDataAjax?url=" + compositeUrl,
                        dataType: "json"
                    }).done(function(jsonData){
                        if (jsonData["error"]) {
                            $(compositeTd).empty();
                            $(compositeTd).html(jsonData["error"]);
                        } else {
                            // Create our data table out of JSON data loaded from server.
                            var data = new google.visualization.arrayToDataTable(jsonData);

                            // Instantiate and draw our chart, passing in some options.
                            var chart = new google.visualization.LineChart(container);
                            var options = { width: 300, 
                                           height: 150, 
                                           legend: { position: 'none'}, 
                                           vAxis: { gridlines: {count: 3 } },
                                           hAxis: { gridlines: {count: 3 } },
                                          };
                            google.visualization.events.addListener(chart, 'ready', function () {
                                container.innerHTML = '<img src="' + chart.getImageURI() + '">';
                            });                 
                            chart.draw(data, options);   
                            $(spinner).remove();
                        }
                    });
                });
            }, t);
        });   
        
        $(".featureAnalysis").each(function(){
            t += 200;
            var compositeTd = $(this);
            var spinner = $(this).find("i");
            var compositeUrl = $(this).find(".composite-url").text();
            var xlabel = $(this).find(".composite-xlabel").text();
            var container = $(this).find(".composite-fig")[0];
            setTimeout(function(){
                google.charts.setOnLoadCallback(function(){
                    $.ajax({
                        url: "/pegr/report/fetchCompositeDataAjax?url=" + compositeUrl,
                        dataType: "json"
                    }).done(function(jsonData){
                        if (jsonData["error"]) {
                            $(compositeTd).empty();
                            $(compositeTd).html(jsonData["error"]);
                        } else {
                            // Create our data table out of JSON data loaded from server.
                            var data = new google.visualization.arrayToDataTable(jsonData);

                            // Instantiate and draw our chart, passing in some options.
                            var chart = new google.visualization.LineChart(container);
                            var options = { width: 512, 
                                            height: 300, 
                                            hAxis: { title: xlabel, 
                                                   titleTextStyle: {fontSize:14,italic:false},
                                                   gridlines: { color: '#DDDDDD', count:8 },
                                            },
                                            vAxis: { title: 'Tag frequency', 
                                                   titleTextStyle: {fontSize:14,italic:false},
                                                   gridlines: { color: '#DDDDDD', count:7 },
                                                    format: '#.##'
                                            },
                                            curveType: 'function',
                                            legend: { position: 'right' }
                                          };                            
                            chart.draw(data, options);   
                            $(spinner).remove();
                        }
                    });
                });
            }, t);
        });   
    });
</script>