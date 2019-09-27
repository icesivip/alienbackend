package icesi.vip.alien.alien.pertvscpm.services.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import icesi.vip.alien.alien.pertvscpm.model.Task;
import icesi.vip.alien.alien.pertvscpm.model.Transition;
import icesi.vip.alien.alien.pertvscpm.repositories.TaskRepository;
import icesi.vip.alien.alien.pertvscpm.services.TaskService;

/**
 * @author jcampaz
 *
 */
@Service
public class TaskServiceImp implements TaskService
{

	static final Logger LOG = LoggerFactory.getLogger(TaskServiceImp.class);

	public enum DistributionType
	{
		NORMAL_DISTRIBUTION, BETA_DISTRIBUTION, LOG_NORMAL_DISTRIBUTION, UNIFORM_DISTRIBUTION
	}

	@Autowired
	private TaskRepository repo;

	@Override
	public List<Task> list()
	{

		return repo.findAll();
	}

	@Override
	public Task findById(int id)
	{
		Task found = repo.findById(id);
		LOG.info("The task " + found.toString() + "was found");
		return found;
	}

	@Override
	public Task add(Task task)
	{
		Task toAdd = repo.save(task);
		LOG.info("Task " + toAdd.toString() + "was aded");
		return toAdd;
	}

	@Override
	public Task edit(Task task)
	{
		return repo.save(task);
	}

	@Override
	public Task delete(int id)
	{
		Task toDelete = findById(id);
		repo.delete(toDelete);
		LOG.warn("Task " + toDelete.toString() + "was deleted");
		return toDelete;
	}

	@Override
	public List<Task> computeEarliestTimes(List<Task> tasks, Task start)
	{
		Queue<Task> taskQueue = new LinkedList<Task>();
		start.setEarliestStart(0.0);
		start.setEarliestFinish(start.getDuration());
		List<Transition> lv1Edges = start.getSuccessors();
		queueSuccessors(taskQueue, lv1Edges);

		while (taskQueue.peek() != null)
		{
			Task cursor = taskQueue.poll();
			double maxPredecesorDuration = getMaxPredecesorES(cursor.getPredecessors());
			cursor.setEarliestStart(maxPredecesorDuration);
			cursor.setEarliestFinish(maxPredecesorDuration + cursor.getDuration());
			List<Transition> nextLevel = cursor.getSuccessors();
			queueSuccessors(taskQueue, nextLevel);
		}
		return tasks;
	}

	@Override
	public List<Task> computeLatestTimesAndSlack(List<Task> tasks, Task finish)
	{
		Queue<Task> taskQueue = new LinkedList<Task>();
		finish.setLatestFinish(finish.getEarliestFinish());
		finish.setLatestStart(finish.getLatestFinish() - finish.getDuration());
		finish.setSlack(finish.getLatestFinish() - finish.getEarliestFinish());
		finish.setIsCritical(true);
		List<Transition> lv1Edges = finish.getPredecessors();
		queuePredecessors(taskQueue, lv1Edges);

		while (taskQueue.peek() != null)
		{
			Task cursor = taskQueue.poll();
			double minSuccessorDuration = getMinSuccessorLS(cursor.getSuccessors());
			cursor.setLatestFinish(minSuccessorDuration);
			cursor.setLatestStart(minSuccessorDuration - cursor.getDuration());
			cursor.setSlack(cursor.getLatestFinish() - cursor.getEarliestFinish());
			cursor.setIsCritical(cursor.getSlack() == 0.0);
			List<Transition> nextLevel = cursor.getPredecessors();
			queuePredecessors(taskQueue, nextLevel);
		}
		return tasks;
	}

	/**
	 * 
	 * @param predecessors
	 * @return
	 */

	private double getMaxPredecesorES(List<Transition> predecessors)
	{
		Task maxTask = predecessors.get(0).getPredecesor();
		double max = maxTask.getEarliestStart() + maxTask.getDuration();
		for (int i = 1; i < predecessors.size(); i++)
		{
			Task cursor = predecessors.get(i).getPredecesor();
			double maxCursor = 0.0;
			if (cursor.getEarliestStart() == null)
				maxCursor = getMaxPredecesorES(cursor.getPredecessors()) + cursor.getDuration();
			else
				maxCursor = cursor.getEarliestStart() + cursor.getDuration();
			max = (maxCursor > max) ? maxCursor : max;
		}
		return max;
	}

	/**
	 * @param sucessors
	 * @return
	 */
	private double getMinSuccessorLS(List<Transition> sucessors)
	{
		Task minTask = sucessors.get(0).getSuccessor();
		double min;
		if(minTask.getLatestFinish()!=null)
		min= minTask.getLatestFinish() - minTask.getDuration();
		else
			min = getMinSuccessorLS(minTask.getSuccessors());

		for (int i = 1; i < sucessors.size(); i++)
		{
			Task cursor = sucessors.get(i).getSuccessor();
			double minCursor = 0.0;
			if (cursor.getLatestFinish() == null)
				minCursor = getMinSuccessorLS(cursor.getSuccessors()) - cursor.getDuration();
			else
				minCursor = cursor.getLatestFinish() - cursor.getDuration();
			min = (minCursor < min) ? minCursor : min;
		}
		return min;
	}

	/**
	 * Add the list of successors to the task queue
	 * 
	 * @param taskQueue - the queue containing the tasks
	 * @param toBeAdded - the list with the transitions pointing to the successors
	 *                  to be added
	 * @return - the same task queue in case of no successors
	 */
	private Queue<Task> queueSuccessors(Queue<Task> taskQueue, List<Transition> toBeAdded)
	{
		for (Transition edge : toBeAdded)
		{
			taskQueue.add(edge.getSuccessor());
		}

		return taskQueue;
	}

	/**
	 * Add the list of predecessors to the task queue
	 * 
	 * @param taskQueue - the queue containing the tasks
	 * @param toBeAdded - the list with the transitions pointing to the successors
	 *                  to be added
	 * @return - the same task queue in case of no successors
	 */
	private Queue<Task> queuePredecessors(Queue<Task> taskQueue, List<Transition> toBeAdded)
	{
		for (Transition edge : toBeAdded)
		{
			taskQueue.add(edge.getPredecesor());
		}

		return taskQueue;
	}

	@Override
	public List<Task> executeCPM(List<Task> tasks, Task start, Task finish)
	{
		computeEarliestTimes(tasks, start);
		computeLatestTimesAndSlack(tasks, finish);

		return tasks;
	}

	@Override
	public Map<Integer, List<Task>> generateScenarios(List<Task> tasks, int numberOfScenarios, double param1,
			double param2, DistributionType distType)
	{
		Map<Integer, List<Task>> scenarios = new HashMap<Integer, List<Task>>(numberOfScenarios);

		AbstractRealDistribution distribution;

		switch (distType)
		{
		case NORMAL_DISTRIBUTION:
			distribution = new NormalDistribution(param1, param2);
			break;
		case BETA_DISTRIBUTION:
			distribution = new BetaDistribution(param1, param2);
			break;
		case LOG_NORMAL_DISTRIBUTION:
			distribution = new LogNormalDistribution(param1, param2);
			break;
		case UNIFORM_DISTRIBUTION:
			distribution = new UniformRealDistribution(param1, param2);
			break;
		default:
			distribution = new NormalDistribution();
			break;
		}

		for (int i = 0; i < numberOfScenarios; i++)
		{
			generateScenario(tasks, distribution);
		}

		return scenarios;
	}

	private List<Task> generateScenario(List<Task> tasks, AbstractRealDistribution dist)
	{

		for (Task task : tasks)
		{
			
		}

		return tasks;
	}

}
