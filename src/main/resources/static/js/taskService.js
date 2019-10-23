

const Url = "http://localhost:8080/tasks";

/* Global var for counter */
var lastId = 0;
var lastName = "";
var lastDuration =0.0;


$(document).ready(function() 
{
	
	var table =$('#tasksTable').dataTable( {
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

function addTask() 
{
	event.preventDefault();

	
	$('#tasksTable').dataTable().fnAddData( [
		lastId,
		"<tr><input required placeholder=\"Type the task name\" type=\"text\" name=\"task"+lastId+"Name\" class=\"form-control\"></tr>",
		"<tr><input type=\"number\" class=\"form-control\" name=\"task"+lastId+"Duration\" min=\"0\" max=\"any\" value=0 step=\"0.01\"><tr>","<tr><input required placeholder=\"Type the task Successors\" type=\"text\" name=\"task"+lastId+"Successor\" class=\"form-control\"></tr>" ] );
		lastId++;
	}


function delTask() {
	event.preventDefault();
	
		var table = $('#tasksTable').DataTable();
		
		$(this).addClass('selected');
		
		var toDel =table.row('.selected').data();
		if(data!=null)
		{

			console.log(toDel[0]);
		
			var delTaskUrl=Url+'/delete/'+toDel[0];

			console.log(delTaskUrl);
			
			$.ajax({

				url:delTaskUrl,
				type: 'DELETE',
				data: JSON.stringify(toDel),
				contentType: 'application/json'
				
			}).then(function (data) {
				console.log(data);

			}, function (error) {
				console.log(error);
			});
		
			$('#tasksTable').DataTable().row('.selected').remove().draw( false );
		
		}
		
		
	}


function loadTasks() 
{
	event.preventDefault();

	fetch(Url+"/sample").then( res=>{
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

	for(var i=0;i<lastId;i++)
	{
		var tSuccessors=[];
		
		var currentTask=
		{
			id:i,
			isCritical:false,
			name:data.get("task"+i+"Name"),
			duration:parseFloat(data.get("task"+i+"Duration")),
			earliestStart:0.0,
			earliestFinish:0.0,
			latestStart:0.0,
			latestFinish:0.0,
			slack:0.0,
			successors:tSuccessors,
		};
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
						duration:parseFloat(data.get("task"+suc+"Duration")),
						earliestStart:0.0,
						earliestFinish:0.0,
						latestStart:0.0,
						latestFinish:0.0,
						slack:0.0,
						isCritical:false
					}
				}
				tSuccessors.push(transition);
			})
		}

		
		taskList.push(currentTask);
	}
	console.log(taskList);
	
	var addTasksUrl=Url+"/add"
	$.ajax({
		url:addTasksUrl,
		type: "POST",
		data: JSON.stringify(taskList),
		
		contentType: 'application/json'
	}).then(function (data) {
		displayCPMData(data);

		
	}, function (error) {
		console.log(error);
	});
}

function displayCPMData(data)
{
	$("#CPMTable").dataTable().fnClearTable();

	console.log(data);
	data.forEach(toLoad=> 
		{
			console.log(toLoad);
			var taskId=toLoad.id;
			var taskName=toLoad.name;
			var taskDuration=toLoad.duration;
			var es=toLoad.earliestStart;
			var ef=toLoad.earliestFinish;
			var ls=toLoad.latestStart;
			var lf=toLoad.latestFinish;
			var slack=toLoad.slack;
			var critical=toLoad.isCritical;

			$("#CPMTable").dataTable().fnAddData( [
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
	})	
}