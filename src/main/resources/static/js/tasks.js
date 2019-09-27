const Url = "http://localhost:8080/tasks";

/* Global var for counter */
var lastId = 0;
var lastName = "";
var lastDuration =0.0;

$(document).ready(function() {
	$('#tasksTable').dataTable();
} );

function addTask() {
	event.preventDefault();
	$('#tasksTable').dataTable().fnAddData( [
		lastId+1,
		"<input type=\"text\" name=\"task"+lastId+1+"\" class=\"form-control\" min=\"0\" max=\"any\" value=\""+lastName+"\">",
		"<input type=\"number\" class=\"form-control\" min=\"0\" max=\"any\" value=\"0.0\" step=\""+lastDuration+"\"></input></td></tr>" ] );
		lastId++;
}

function delTask() {
	event.preventDefault();
	$('#tasksTable').dataTable().fnDeleteRow(lastId);
		lastId--;
}


function loadTasks() {
	event.preventDefault();
	console.log("Loading the tasks");
	fetch(Url).then( res=>{
		res.json().then(tasks=> {
			console.log(tasks);
			tasks.forEach(toLoad => {
				var taskId=toLoad.id;
				var taskName=toLoad.name;
				var taskDuration=toLoad.duration;
				$("#tasksTable").dataTable().fnAddData( [
					taskId,taskName,taskDuration]);
					lastId++;
					lastName=taskName;
					lastDuration=taskDuration;
					console.log("loaded " + taskName);
			});
			
			 
		})
	})
	
}
function submitTasks() {
	event.preventDefault();
	console.log("Loading the tasks");
	}
	

