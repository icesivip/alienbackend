
const Url = "http://localhost:8080/tasks";

/* Global var for counter */
var lastId = 0;
var lastName = "";
var lastDuration =0.0;


$(document).ready(function() {
	
	var table =$('#tasksTable').dataTable();
	$('#tasksTable tbody').on( 'click', 'tr', function () {
	
		if ( $(this).hasClass('selected') ) {
			$(this).removeClass('selected');
		}
		else {
			table.$('tr.selected').removeClass('selected');
			$(this).addClass('selected');
		}
	})
});

function addTask() {
	event.preventDefault();
	lastId++;
	$('#tasksTable').dataTable().fnAddData( [
		lastId,
		"<tr><input required placeholder=\"Type the task name\" type=\"text\" name=\"task"+lastId+"Name\" class=\"form-control\"></tr>",
		"<tr><input type=\"number\" class=\"form-control\" name=\"task"+lastId+"Duration\" min=\"0\" max=\"any\" value=0 step=\"0.01\"><tr>","<tr><input required placeholder=\"Type the task Successors\" type=\"text\" name=\"task"+lastId+"Successor\" class=\"form-control\"></tr>" ] );
}

function delTask() {
	event.preventDefault();
	
		var table = $('#tasksTable').DataTable();
		$(this).addClass('selected');
		$('#tasksTable').DataTable().row('.selected').remove().draw( false );
	}


function loadTasks() 
{
	event.preventDefault();
	// console.log("Loading the tasks");
	fetch(Url).then( res=>{
		res.json().then(tasks=> {
			console.log(tasks);
			tasks.forEach(toLoad => {
				var taskId=toLoad.id;
				var taskName=toLoad.name;
				var taskDuration=toLoad.duration;
				var taskSuccessors="";
				toLoad.successors.forEach(tran =>{
					if(taskSuccessors.length>0)
					{
						taskSuccessors+=","+tran.successor.id
					}
					else
					{
						taskSuccessors+=tran.successor.id
					}

				});
				
				$("#tasksTable").dataTable().fnAddData( [
					taskId,
					"<tr><input type=\"text\" name=\"task"+taskId+"Name\" class=\"form-control\" value=\""+taskName+"\"><tr>",
					"<tr><input type=\"number\" class=\"form-control\" name=\"task"+taskId+"Duration\" min=\"0\" max=\"any\" step=\"0.01\" value=\""+taskDuration+"\"></tr>",
					"<tr><input type=\"text\" name=\"task"+taskId+"Successors\" class=\"form-control\" value=\""+taskSuccessors+"\"><tr>"]);
					lastId++;
					lastName=taskName;
					lastDuration=taskDuration;
					// console.log("loaded " + taskName);
			});
		})
	})
	
}

function submitTasks() 
{
	event.preventDefault();
	console.log(" submit the tasks");
	var data= new FormData(document.getElementById("tasksForm"));	
	var taskList=[];

	for(var i=1;i<=lastId;i++)
	{
		var sucNames=data.get("task"+i+"Successors").split(",");
		var trans=[];
		sucNames.forEach( suc=>
		{
			var transition=
			{
				type: "FS", 
				successor:
				{
					id:suc,
					name:data.get("task"+suc+"Name"),
					duration:data.get("task"+suc+"Duration"),
					earliestStart:0.0,
					earliestFinish:0.0,
					latestStart:0.0,
					latestFinish:0.0,
					slack:0.0,
					isCritical:false
				}
			}
			trans.push(transition);
		})
		

		var currentTask=
		{
			id:i,
			name:data.get("task"+i+"Name"),
			duration:data.get("task"+i+"Duration"),
			earliestStart:0.0,
			earliestFinish:0.0,
			latestStart:0.0,
			latestFinish:0.0,
			slack:0.0,
			isCritical:false,
			successors:trans
			
		};
		taskList.push(currentTask);
	}
	// console.log("final list is: ");
	console.log(taskList);
	// var postHeaders={
	// 	"Content-Type": "application/json",
	// 	"dataType": "json"
	// };
	// var addTasksUrl=Url+"/add"
	//  fetch(addTasksUrl,{
	//  	method:'POST',
	// 	 body:taskList,
	// 	 headers:postHeaders
	//  });
}

