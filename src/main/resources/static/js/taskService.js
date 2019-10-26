const Url = "http://localhost:8080/tasks";

/* Global var for counter */
var lastId = 0;
var lastName = "";
var lastDuration = 0.0;

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

function addTask() {
  event.preventDefault();

  $("#tasksTable")
    .dataTable()
    .fnAddData([
      lastId,
      '<tr><input required placeholder="Type the task name" type="text" name="task' +
        lastId +
        'Name" class="form-control"></tr>',
      '<tr><input type="number" class="form-control" name="task' +
        lastId +
        'Duration" min="0" max="any" value=0 step="0.01"><tr>',
      '<tr><input required placeholder="Type the task Successors" type="text" name="task' +
        lastId +
        'Successor" class="form-control"></tr>'
    ]);
  lastId++;
}

function delTask() {
  event.preventDefault();

  var table = $("#tasksTable").DataTable();

  $(this).addClass("selected");

  var toDel = table.row(".selected").data();
  if (data != null) {
    console.log(toDel[0]);

    var delTaskUrl = Url + "/delete/" + toDel[0];

    console.log(delTaskUrl);

    $.ajax({
      url: delTaskUrl,
      type: "DELETE",
      data: JSON.stringify(toDel),
      contentType: "application/json"
    }).then(
      function(data) {
        console.log(data);
      },
      function(error) {
        console.log(error);
      }
    );

    $("#tasksTable")
      .DataTable()
      .row(".selected")
      .remove()
      .draw(false);
  }
}

function loadTasks(data) {
  // event.preventDefault();

  data.forEach(toLoad => {
    var taskId = toLoad.id;
    var taskName = toLoad.name;
    var taskDuration = toLoad.duration;
    var taskSuccessors = "";
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
        '<tr><input type="number" class="form-control" name="task' +
          taskId +
          'Duration" min="0" max="any" step="0.01" value="' +
          taskDuration +
          '"></tr>',
        '<tr><input type="text" name="task' +
          taskId +
          'Successors" class="form-control" value="' +
          taskSuccessors +
          '"><tr>'
      ]);
    lastId++;
    lastName = taskName;
    lastDuration = taskDuration;
  });
}

function submitTasks() {
  event.preventDefault();

  var data = new FormData(document.getElementById("tasksForm"));
  var taskList = [];

  for (var i = 0; i < lastId; i++) {
    var currentTask = {
      id: i,
      name: data.get("task" + i + "Name"),
      duration: parseFloat(data.get("task" + i + "Duration")),
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
            duration: taskList[i].duration
          },
          successor: {
            id: taskList[suc].id,
            name: taskList[suc].name,
            duration: taskList[suc].duration
          }
        };
        taskList[i].successors.push(transition);
        taskList[suc].predecessors.push(transition);
      });
    }
  }
  console.log("created the transitions");
  console.log(taskList);

  var addTasksUrl = Url + "/add";
  $.ajax({
    url: addTasksUrl,
    type: "POST",
    data: JSON.stringify(taskList),
    contentType: "application/json"
  }).then(
    function(data) {
      displayCPMData(data);
    },
    function(error) {
      console.log(error);
    }
  );
}

function displayCPMData(data) {
  $("#CPMTable")
    .dataTable()
    .fnClearTable();

  data.forEach(toLoad => {
    var taskId = toLoad.id;
    var taskName = toLoad.name;
    var taskDuration = toLoad.duration;
    var es = toLoad.earliestStart;
    var ef = toLoad.earliestFinish;
    var ls = toLoad.latestStart;
    var lf = toLoad.latestFinish;
    var slack = toLoad.slack;
    var critical = toLoad.isCritical;

    $("#CPMTable")
      .dataTable()
      .fnAddData([
        taskId,
        taskName,
        taskDuration,
        es,
        ef,
        ls,
        lf,
        slack,
        critical
      ]);
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
