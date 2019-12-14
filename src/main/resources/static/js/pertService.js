const Url = "http://localhost:8080/tasks";

var taskList = [];

/* Global var for counter */

var lastId = 0;

var totalScenarios;

$(document).ready(function() {
  var table = $("#tasksTable").dataTable({
    select: true
  });

  $("#tasksTable tbody").on("click", "tr", function() {
    if ($(this).hasClass("selected")) {
      $(this).removeClass("selected");
    } else {
      table.$("tr.selected").removeClass("selected");
      $(this).addClass("selected");
    }
  });
});

document
  .getElementById("upload-file-form")
  .addEventListener("submit", function(e) {
    e.preventDefault();
    var file = document.getElementById("fileUpload").files[0];

    urlFile = window.URL.createObjectURL(file);
    $.ajax({
      url: urlFile
    }).then(
      function(data) {
        loadTasks(data);
      },
      function(error) {
        console.log(error); 
      }
    );
    $("#uploadModal").modal("hide");
    $("body").removeClass("modal-open");
    $(".modal-backdrop").remove();
  });

function addPertTask() {
  event.preventDefault();

  $("#tasksTable")
    .dataTable()
    .fnAddData([
      lastId,
      '<input required placeholder="task name" type="text" name="task' +
        lastId +
        'Name" class="form-control">',
      '<input placeholder="task Successors" type="text" name="task' +
        lastId +
        'Successor" class="form-control">',
      '<select class="browser-default custom-select" id="task' +
        lastId +
        'distribution" name="task' +
        lastId +
        'distribution">\n<option>Normal</option>\n<option>Beta</option>\n<option>Log Normal</option>\n<option>Uniform</option>\n<option>Triangular</option></select>',
      '<div class="row justify-content-md-center"><input type="number" class="form-control col-md-3 ml-1" name="task' +
        lastId +
        'param1" min="0" max="any" step="0.01" placeholder="param 1"><input type="number" class="form-control col-md-4 ml-1" name="task' +
        lastId +
        'param2" min="0" max="any" step="0.01" placeholder="param 2"><input type="number" class="form-control col-md-4 ml-1" name="task' +
        lastId +
        'param3" min="0" max="any" step="0.01" placeholder="param 3"></div>'
    ]);
  lastId++;
}

function delTask() {
  event.preventDefault();

  var table = $("#tasksTable").DataTable();
  $(this).addClass("selected");
  $("#tasksTable")
    .DataTable()
    .row(".selected")
    .remove()
    .draw(false);
}

function loadTasks(data) {
  if (data.scenarios != null) {
    totalScenarios = data.scenarios;
    document.getElementById("scenarios").innerHTML=totalScenarios;
  }
  data.tasks.forEach(toLoad => {
    var taskId = toLoad.id;
    var taskName = toLoad.name;
    var taskSuccessors = "";
    var taskDistType = toLoad.distribution.distributionType;
    var taskDistParam1 = toLoad.distribution.param1;
    var taskDistParam2 = toLoad.distribution.param2;
    var taskDistParam3 = toLoad.distribution.param3;    
    var colParams = '<div class="row justify-content-md-center"><input type="number" class="form-control col-md-4" name="task' +
    taskId +
    'param1" min="0" max="any" step="0.01" value="' +
    taskDistParam1 +
    '"><input type="number" class="form-control col-md-3 ml-1 mr-1" name="task' +
    taskId +
    'param2" min="0" max="any" step="0.01" value="' +
    taskDistParam2 +
    '"><input type="number" class="form-control col-md-3" name="task' +
    taskId +
    'param3" min="0" max="any" step="0.01" value="' +
    taskDistParam3 +
    '"></div></div>';
    
    toLoad.successors.forEach(suc => {
      if (taskSuccessors.length > 0) {
        taskSuccessors += "," + suc.successor.id;
      } else {
        taskSuccessors += suc.successor.id;
      }
    });

    $("#tasksTable")
      .dataTable()
      .fnAddData([
        taskId,
        '<input type="text" name="task' +
          taskId +
          'Name" class="form-control" value="' +
          taskName +
          '">',
        '<input placeholder="successors" type="text" name="task' +
          taskId +
          'Successors" class="form-control" value="' +
          taskSuccessors +
          '">',
        '<select class="browser-default custom-select" name="task' +
          taskId +
          'distribution" id="task' +
          taskId +
          'distribution">\n<option selected>Normal</option>\n<option>Beta</option>\n<option>Log Normal</option>\n<option>Uniform</option>\n<option>Triangular</option></select>',
        colParams        
      ]);
    lastId++;
  });
}

function submitPertTasks() 
{
  event.preventDefault();
  readFormData();

  var pertTasksUrl = Url + "/pert/" + totalScenarios;
  $.ajax({
    url: pertTasksUrl,
    type: "POST",
    data: JSON.stringify(taskList),
    contentType: "application/json"
  }).then( function (data)
   {
    document.getElementById('FeedBack').innerHTML='Pert evaluated successfully';
    $('#FeedBack').addClass('alert-success show');
    displayChart(data);
  }, function(error)
  {
    console.log(error);
    var errorMessage='There are problems defining the tasks:\n';
    var errors=error.responseJSON.message.replace('The task','\n The task');
    $('#FeedBack').addClass('alert-danger show');
    document.getElementById('FeedBack').innerHTML=errorMessage;
  });
}


function displayChart(data) {
  var myBarChart = null;
  var frequencies =[];
  var taskNames = [];
  var durations =[];
  
  taskList.forEach(task =>{
    taskNames.push(task.name);
  });
  
  
  for (var i = 0; i <taskList.length; i++) 
  {
    var criticalCount = 0.0;

    console.log('task '+i);
    for(var j=0;j< totalScenarios;j++)
    {
      var iterated=data[j][i];
      if (iterated.isCritical)
      {
        criticalCount++;
      }
    }
    var frequency=(criticalCount/totalScenarios)*100;
    frequencies.push(frequency);

  }
  

  var ctx = document.getElementById("histogram");
  myBarChart = new Chart(ctx, {
    type: "bar",
    data: {
      labels: taskNames,
      datasets: [
        {
          label: "Frequency",
          backgroundColor: "#4e73df",
          hoverBackgroundColor: "#2e59d9",
          borderColor: "#4e73df",
          data: frequencies
        }
      ]
    },
    options: {
      maintainAspectRatio: false,
      layout: {
        padding: {
          left: 10,
          right: 25,
          top: 25,
          bottom: 0
        }
      },
      scales: {
        xAxes: [
          {
            time: {
              unit: "month"
            },
            gridLines: {
              display: false,
              drawBorder: false
            },
            ticks: {
              maxTicksLimit: totalScenarios
            },
            maxBarThickness: 25
          }
        ],
        yAxes: [
          {
            ticks: {
              min: 0,
              max: 100,
              maxTicksLimit: 5,
              padding: 10,
              // Include a dollar sign in the ticks
              callback: function(value, index, values) {
                return "" + number_format(value);
              }
            },
            gridLines: {
              color: "rgb(234, 236, 244)",
              zeroLineColor: "rgb(234, 236, 244)",
              drawBorder: false,
              borderDash: [2],
              zeroLineBorderDash: [2]
            }
          }
        ]
      },
      legend: {
        display: false
      },
      tooltips: {
        titleMarginBottom: 10,
        titleFontColor: "#6e707e",
        titleFontSize: 14,
        backgroundColor: "rgb(255,255,255)",
        bodyFontColor: "#858796",
        borderColor: "#dddfeb",
        borderWidth: 1,
        xPadding: 15,
        yPadding: 15,
        displayColors: false,
        caretPadding: 10,
        callbacks: {
          label: function(tooltipItem, chart) {
            var datasetLabel =
              chart.datasets[tooltipItem.datasetIndex].label || "";
            return datasetLabel + ": " + number_format(tooltipItem.yLabel)+"%";
          }
        }
      }
    }
  });
}

function saveProblem() {
  if (taskList.length == 0) {
    readFormData();
  }
  console.log("the total number of scenarios is: " + totalScenarios);
  console.log(taskList);
  var objectData = { tasks: taskList, scenarios: totalScenarios };
  console.log(objectData);
  exportToJson(objectData);
}

function readFormData() {
  var data = new FormData(document.getElementById("tasksForm"));

  totalScenarios = data.get("scenarios");

  for (var i = 0; i < lastId; i++) {
    var currentTask = {
      id: i,
      name: data.get("task" + i + "Name"),
      distribution: {
        distributionType: document
          .getElementById("task" + i + "distribution")
          .value.replace(" ", "_")
          .toUpperCase(),
        param1: data.get("task" + i + "param1"),
        param2: data.get("task" + i + "param2"),
        param3: data.get("task" + i + "param3")
      },
      predecessors: [],
      successors: []
    };
    taskList.push(currentTask);
  }

  for (var i = 0; i < lastId; i++) {
    var taskSuccessors = data.get("task" + i + "Successors");
    if (taskSuccessors != null && taskSuccessors != "") {
      var sucIds = data.get("task" + i + "Successors").split(",");
      sucIds.forEach(suc => {
        var transition = {
          type: "FS",
          predecesor: {
            id: taskList[i].id,
            name: taskList[i].name,
            distribution: {
              distributionType: taskList[i].distribution.distributionType
                .replace(" ", "_")
                .toUpperCase(),
              param1: taskList[i].distribution.param1,
              param2: taskList[i].distribution.param2,
              param3: taskList[i].distribution.param3,
            }
          },
          successor: {
            id: taskList[suc].id,
            name: taskList[suc].name,
            distribution: {
              distributionType: taskList[
                suc
              ].distribution.distributionType.toUpperCase(),
              param1: taskList[suc].distribution.param1,
              param2: taskList[suc].distribution.param2,
              param3: taskList[suc].distribution.param3
            }
          }
        };
        taskList[i].successors.push(transition);
        taskList[suc].predecessors.push(transition);
      });
    }
  }
  totalScenarios = data.get("scenarios");
  console.log("the number of scenarios is set to " + totalScenarios);
  console.log("And the list of tasks is ");
  console.log(taskList);
  
}

function exportToJson(objectData) {
  console.log(objectData);
  let filename = "pert_problem.json";
  let contentType = "application/json;charset=utf-8;";
  if (window.navigator && window.navigator.msSaveOrOpenBlob) {
    var blob = new Blob(
      [decodeURIComponent(encodeURI(JSON.stringify(objectData)))],
      { type: contentType }
    );
    navigator.msSaveOrOpenBlob(blob, filename);
  } else {
    var a = document.createElement("a");
    a.download = filename;
    a.href =
      "data:" +
      contentType +
      "," +
      encodeURIComponent(JSON.stringify(objectData));
    a.target = "_blank";
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
  }
}
