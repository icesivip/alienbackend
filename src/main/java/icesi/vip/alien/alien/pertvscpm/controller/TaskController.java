package icesi.vip.alien.alien.pertvscpm.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import icesi.vip.alien.alien.pertvscpm.model.Task;
import icesi.vip.alien.alien.pertvscpm.repositories.impl.TaskRepositoryImp;
import icesi.vip.alien.alien.pertvscpm.services.TaskService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping(
{ "/tasks" })
@RestController
public class TaskController
{
//	private final Logger LOG = LoggerFactory.getLogger(TaskRepositoryImp.class);

	@Autowired
	TaskService service;

	@GetMapping
	public List<Task> list()
	{

		return service.list();
	}

	@GetMapping(value = "/sample")
	public List<Task> loadSampleTasks()
	{
		
		return service.loadSampleTaks("classpath:static/graph.txt");
	}

	@PostMapping(value = "/add")
	public List<Task> addTask(@RequestBody(required = true) ArrayList<Task> taskList)
	{
		List<Task> activities = service.buildGraph(taskList);
		Task start = activities.get(0);
		Task finish = activities.get(activities.size() - 1);
		List<Task> cpm = service.executeCPM(activities, start, finish);

		return cpm;
	}

	@PostMapping(value = "/pert/{scenarios}")
	public Map<Integer, List<Task>> pert(@RequestBody List<Task> taskList, @PathVariable("scenarios") int scenarios)
	{

		Map<Integer, List<Task>> simulatedScenarios = service.generateScenarios(taskList, scenarios);

		for (int i=0;i<scenarios;i++)
		{
		
			service.executePERTCPM(i);
		}

		return simulatedScenarios;
	}

	@GetMapping(path =
	{ "/{id}" })
	public Task listById(@PathVariable("id") int id)
	{
		return service.findById(id);
	}

	@PutMapping(path =
	{ "/{id}" })
	public Task update(@RequestBody Task task, @PathVariable("id") int id)
	{
		task.setId(id);
		return service.edit(task);
	}

	@GetMapping(path =
	{ "/criticalPathMethod/{startId}/{endId}" })
	public List<Task> criticalPathMethod(@PathVariable("startId") int startId, @PathVariable("endId") int endId)
	{
		List<Task> tasks = service.list();
		Task start = service.findById(startId);
		Task finish = service.findById(endId);
		return service.executeCPM(tasks, start, finish);
	}

	@DeleteMapping(value = "/delete/{delId}")
	public Task deleteTask(@PathVariable("delId") int delId)
	{
		return service.delete(delId);
	}
}
