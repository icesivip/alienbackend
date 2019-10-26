const Url = "http://localhost:8080/tasks";

var taskList = [];

/* Global var for counter */

var lastId = 0;

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

  $(".dropdown-menu a").click(function() {
    var selText = $(this).text();
    $(this)
      .parents(".btn-group")
      .find(".dropdown-toggle")
      .html(selText + ' <span class="caret"></span>');
  });
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
  data.forEach(toLoad => {
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
        taskSuccessors += "," + suc;
      } else {
        taskSuccessors += suc;
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

  var data = new FormData(document.getElementById("tasksForm"));

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

  var totalScenarios = data.get("scenarios");

  var pertTasksUrl = Url + "/pert/" + totalScenarios;
  $.ajax({
    url: pertTasksUrl,
    type: "POST",
    data: JSON.stringify(taskList),
    contentType: "application/json"
  }).then(response => {
    console.log(response);
  });
}

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

    //   $.ajax({
    //     url: urlFile
    //   }).then(
    //     function(data) {
    //       console.log(data);
    //     },
    //     function(error) {
    //       alert("Failed to process request.");
    //       console.log(error);
    //     }
    //   );
    $("#uploadModal").modal("hide");
    $("body").removeClass("modal-open");
    $(".modal-backdrop").remove();
  });
