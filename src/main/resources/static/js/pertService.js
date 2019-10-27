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
      '<tr><input required placeholder="task name" type="text" name="task' +
        lastId +
        'Name" class="form-control"></tr>',
      '<tr><input placeholder="task Successors" type="text" name="task' +
        lastId +
        'Successor" class="form-control"></tr>',
      '<tr><select class="browser-default custom-select" id="task' +
        lastId +
        'distribution" name="task' +
        lastId +
        'distribution">\n<option>NORMAL</option>\n<option>BETA</option>\n<option>LOG NORMAL</option>\n<option>UNIFORM</option>\n<option>TRIANGULAR</option></select><tr>',
      '<tr><input type="number" class="form-control" name="task' +
        lastId +
        'param1" min="0" max="any" step="0.01" value="0"></tr><tr><input type="number" class="form-control" name="task' +
        lastId +
        'param2" min="0" max="any" step="0.01" value="0"></tr>'
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
  if(data.scenarios!=null)
  {
    totalScenarios=data.scenarios;
    $("scenarios").val(totalScenarios);

  }
  data.tasks.forEach(toLoad => {
    var taskId = toLoad.id;
    var taskName = toLoad.name;
    var taskSuccessors = "";
    var taskDistType = toLoad.distribution.distributionType;
    var taskDistParam1 = toLoad.distribution.param1;
    var taskDistParam2 = toLoad.distribution.param2;
    var taskDistParam3;
    if (taskDistType) {
      taskDistParam3 == toLoad.distribution.param3;
    }

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
        '<tr><input type="text" name="task' +
          taskId +
          'Name" class="form-control" value="' +
          taskName +
          '"><tr>',
        '<tr><input placeholder="successors" type="text" name="task' +
          taskId +
          'Successors" class="form-control" value="' +
          taskSuccessors +
          '"><tr>',
        '<tr><select class="browser-default custom-select" name="task' +
          taskId +
          'distribution" id="task' +
          taskId +
          'distribution">\n<option selected>Normal</option>\n<option>Beta</option>\n<option>Log Normal</option>\n<option>Uniform</option>\n</select><tr>',
        '<tr><input type="number" class="form-control" name="task' +
          taskId +
          'param1" min="0" max="any" step="0.01" value="' +
          taskDistParam1 +
          '"></tr><tr><input type="number" class="form-control" name="task' +
          taskId +
          'param2" min="0" max="any" step="0.01" value="' +
          taskDistParam2 +
          '"></tr>'
      ]);
    lastId++;
  });
}

function submitPertTasks() {
  
  event.preventDefault();
  readFormData();

  var pertTasksUrl = Url + "/pert/" + totalScenarios;
  $.ajax({
    url: pertTasksUrl,
    type: "POST",
    data: JSON.stringify(taskList),
    contentType: "application/json"
  }).then(data => {
    displayChart(data);
  });
}

function displayChart(data) {
  var myChart = null;
  var durations = [];

  for (var i = 0; i < totalScenarios; i++) {
    var totalDuration = 0.0;
    data[i].forEach(task => {
      if (task.isCritical == true) {
        totalDuration += task.duration;
      }
    });
    durations.push(totalDuration);
  }

  var taskNames = [];
  taskList.forEach(task => taskNames.push(task.name));

  var ctx = document.getElementById("chart-area");
  if (myChart != null) {
    myChart.destroy();
  }

  var chartOptions = {
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
      yAxes: [
        {
          ticks: {
            beginAtZero: false
          },
          gridLines: {
            borderDash: [1, 2],
            zeroLineColor: "rgba(0,0,0,1)"
          }
        }
      ],
      xAxes: [
        {
          ticks: {
            beginAtZero: false
          },
          type: "linear",
          position: "bottom",
          gridLines: {
            borderDash: [1, 2],
            zeroLineColor: "rgba(0,0,0,1)"
          }
        }
      ]
    },
    tooltips: {
      backgroundColor: "rgb(255,255,255)",
      bodyFontColor: "#858796",
      titleMarginBottom: 10,
      titleFontColor: "#6e707e",
      titleFontSize: 14,
      borderColor: "#dddfeb",
      borderWidth: 1,
      xPadding: 15,
      yPadding: 15,
      displayColors: false,
      intersect: false,
      mode: "index",
      caretPadding: 10
    }
  };

  console.log(taskNames);

  console.log(durations);

  myChart = new Chart(ctx, {
    type: "line",
    data: {
      labels: taskNames,
      datasets: [
        {
          label: "Durations",
          backgroundColor: "rgba(78, 115, 223, 0.05)",
          borderColor: "rgba(78, 115, 223, 1)",
          pointBackgroundColor: "rgba(78, 115, 223, 1)",
          pointBorderColor: "rgba(78, 115, 223, 1)",
          pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
          pointHoverBorderColor: "rgba(78, 115, 223, 1)",
          data: durations
        }
      ]
    },
    options: chartOptions
  });
}

function saveProblem() {

  if ((taskList.length == 0)) {
    readFormData();
  }
  console.log("the total number of scenarios is: "+totalScenarios);
  console.log(taskList);
  var objectData= {tasks:taskList,scenarios:totalScenarios}
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
        param2: data.get("task" + i + "param2")
      },
      predecessors: [],
      successors: []
    };
    taskList.push(currentTask);
    console.log('added '+currentTask.name);
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
              param2: taskList[i].distribution.param2
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
              param2: taskList[suc].distribution.param2
            }
          }
        };
        taskList[i].successors.push(transition);
        taskList[suc].predecessors.push(transition);
      });
    }
  }
  totalScenarios = data.get("scenarios");
  console.log('the number of scenarios is set to '+totalScenarios);
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
