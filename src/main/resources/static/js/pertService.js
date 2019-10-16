
const Url = "http://localhost:8080/tasks";

/* Global var for counter */
var lastId = 0;
var lastName = "";
var lastDuration =0.0;


$(document).ready(function() 
{
	
	var table =$('#tasksTable').dataTable({
		select: true
	});

	$('#tasksTable tbody').on( 'click', 'tr', function () 
	{
	
		if ( $(this).hasClass('selected') ) 
		{
			$(this).removeClass('selected');
		}
		else 
		{
			table.$('tr.selected').removeClass('selected');
			$(this).addClass('selected');
		}
	})
});

function addPertTask() 
{
	event.preventDefault();

	lastId++;

	$('#tasksTable').dataTable().fnAddData( [
		lastId,
		"<tr><input required placeholder=\"Type the task name\" type=\"text\" name=\"task"+lastId+"Name\" class=\"form-control\"></tr>",
		"<tr><input type=\"number\" class=\"form-control\" name=\"task"+lastId+"Duration\" min=\"0\" max=\"any\" value=0 step=\"0.01\"><tr>","<tr><input required placeholder=\"Type the task Successors\" type=\"text\" name=\"task"+lastId+"Successor\" class=\"form-control\"></tr>","<tr><input type=\"text\" name=\"task"+lastId+"distribution\" class=\"form-control\" placeholder=\"Type the task name\"><tr>","<tr><input type=\"number\" class=\"form-control\" name=\"task"+lastId+"param1\" min=\"0\" max=\"any\" step=\"0.01\" value=\"0\"></tr><tr><input type=\"number\" class=\"form-control\" name=\"task"+lastId+"param2\" min=\"0\" max=\"any\" step=\"0.01\" value=\"0\"></tr>" ] );
}

function delTask() {
	event.preventDefault();
	
		var table = $('#tasksTable').DataTable();
		$(this).addClass('selected');
		$('#tasksTable').DataTable().row('.selected').remove().draw( false );
	}


function loadPertTasks() 
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
					"<tr><input placeholder=\"Distribution\" type=\"text\" name=\"task"+taskId+"Successors\" class=\"form-control\" value=\""+taskSuccessors+"\"><tr>","<tr><button class=\"btn btn-secondary dropdown-toggle\" type=\"button\" id=\"task"+taskId+"distribution\" name=\"task"+taskId+"distribution\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\"> Distribution</button> <div class=\"dropdown-menu\" aria-labelledby=\"task"+taskId+"distribution\">\n<a class=\"dropdown-item\">Normal</a>\n<a class=\"dropdown-item\">Beta</a>\n<a class=\"dropdown-item\">Log Normal</a>\n<a class=\"dropdown-item\">Uniform</a>\n</div><tr>","<tr><input type=\"number\" class=\"form-control\" name=\"task"+taskId+"param1\" min=\"0\" max=\"any\" step=\"0.01\" value=\"0\"></tr><tr><input type=\"number\" class=\"form-control\" name=\"task"+taskId+"param2\" min=\"0\" max=\"any\" step=\"0.01\" value=\"0\"></tr>"]);
					lastId++;
					lastName=taskName;
					lastDuration=taskDuration;
					// console.log("loaded " + taskName);
			});
		})
	})
	
}

function submitPertTasks() 
{
	event.preventDefault();
	console.log(" submit the tasks");
	var data= new FormData(document.getElementById("tasksForm"));	
	var taskList=[];

	for(var i=1;i<=lastId;i++)
	{
		var trans=[];
		if(data.get("task"+i+"Successors")!=null)
		{
			var sucNames=data.get("task"+i+"Successors").split(",");
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
		}
		
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
	var postHeaders={
		"Content-Type": "application/json",
		"dataType": "json"
	};
	var addTasksUrl=Url+"/add"
	 fetch(addTasksUrl,{
	 	method:'POST',
		 body:JSON.stringify(taskList),
		 headers:postHeaders
	 });
}