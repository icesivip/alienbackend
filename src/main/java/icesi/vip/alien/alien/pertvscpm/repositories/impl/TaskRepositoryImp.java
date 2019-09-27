package icesi.vip.alien.alien.pertvscpm.repositories.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
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
		loadTasksFromFile();

		return tasks.values().stream().collect(Collectors.toList());
	}

	
	public void loadTasksFromFile()
	{
		// TODO Auto-generated method stub
		Resource graph = resourceLoader.getResource("classpath:static/graph.txt");
		if (tasks.isEmpty())
		{
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
		}
	}

	@Override
	public Task findById(int id)
	{
		if (tasks.isEmpty())
			findAll();
		
			return tasks.get(id);
	}

	@Override
	public Task save(Task task)
	{
		if (tasks.isEmpty())
			findAll();

		if (tasks.containsKey(task.getId()))
		{
			tasks.replace(task.getId(), task);
		}
		else
		{
			Integer lastId = Collections.max(tasks.keySet(), null);
			task.setId(lastId + 1);
			tasks.put(task.getId(), task);
		}
		Resource graph = resourceLoader.getResource("classpath:graph.txt");

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(graph.getFile(), true)))
		{
			String taskString = "" + task.getId() + ":" + task.getName() + ":" + task.getDuration() + ":";
			writer.write(taskString);
			writer.flush();
			LOG.info("Added the task" + task.toString());
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return task;
	}

	@Override
	public void delete(Task task)
	{

	}

}
