package icesi.vip.alien.alien.pertvscpm.repositories.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import icesi.vip.alien.alien.pertvscpm.model.Task;
import icesi.vip.alien.alien.pertvscpm.model.Transition;
import icesi.vip.alien.alien.pertvscpm.repositories.TaskRepository;

@Repository
public class TaskRepositoryImp implements TaskRepository
{

	private final Logger LOG = LoggerFactory.getLogger(TaskRepositoryImp.class);

	@Autowired
	ResourceLoader resourceLoader;

	private Map<Integer, Task> tasks = new HashMap<Integer, Task>();

	@Override
	public List<Task> findAll()
	{
		return tasks.values().stream().collect(Collectors.toList());
	}

	@Override
	public List<Task> loadTasksFromFile()
	{
		tasks.clear();
		Resource graph = resourceLoader.getResource("classpath:static/graph.txt");
		
		
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(graph.getInputStream())))
			{
				String line;

				while ((line = reader.readLine()) != null && !line.startsWith("-"))
				{
					String[] data = line.split(":");
					int id = Integer.parseInt(data[0]);
					String name = data[1];
					double duration = Double.valueOf(data[2]);
					Task task = new Task(id, name, duration);
					tasks.put(id, task);
					LOG.info("Successfully loaded the task "+task.getName());
				}

				while ((line = reader.readLine()) != null)
				{
					String[] data = line.split(":");
					int id = Integer.parseInt(data[0]);
					String[] succesorsIds = data[1].split(",");
					Task taskPredecessor = findById(id);
					for (String successorId : succesorsIds)
					{
						Task taskSuccessor = findById(Integer.parseInt(successorId));
						Transition edge = new Transition(taskPredecessor, taskSuccessor);
						taskPredecessor.getSuccessors().add(edge);
						taskSuccessor.getPredecessors().add(edge);
					}
				}
				
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			return tasks.values().stream().collect(Collectors.toList()); 
		}
		
	

	@Override
	public Task findById(int id)
	{
		if (tasks.isEmpty())
			return null;

		return tasks.get(id);
	}

	@Override
	public Task save(Task task)
	{
		if (!tasks.containsKey(task.getId()))
			tasks.put(task.getId(), task);
		return task;
	}

	@Override
	public Task delete(Task task)
	{
		return tasks.remove(task.getId());
	}

}
