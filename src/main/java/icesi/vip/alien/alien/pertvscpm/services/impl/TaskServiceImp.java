package icesi.vip.alien.alien.pertvscpm.services.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.TriangularDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import icesi.vip.alien.alien.pertvscpm.model.Task;
import icesi.vip.alien.alien.pertvscpm.model.TaskDistribution;
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
	public List<Task> findScenarioByid(int id)
	{

		return repo.findScenarioById(id);
	}

	@Override
	public List<Task> computeEarliestTimes(List<Task> tasks, Task start)
	{
		Queue<Task> taskQueue = new LinkedList<Task>();
		start.setEarliestStart(0.0);
		start.setEarliestFinish(start.getDuration());
		queueSuccessors(taskQueue, start.getId());

		while (taskQueue.peek() != null)
		{
			Task cursor = taskQueue.poll();

			double maxPredecesorDuration = getMaxPredecesorES(cursor.getPredecessors());
			cursor.setEarliestStart(maxPredecesorDuration);
			cursor.setEarliestFinish(maxPredecesorDuration + cursor.getDuration());
			queueSuccessors(taskQueue, cursor.getId());
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
		queuePredecessors(taskQueue, finish.getId());

		while (taskQueue.peek() != null)
		{
			Task cursor = taskQueue.poll();
			double minSuccessorDuration = getMinSuccessorLS(cursor.getSuccessors());
			cursor.setLatestFinish(minSuccessorDuration);
			cursor.setLatestStart(minSuccessorDuration - cursor.getDuration());
			cursor.setSlack(cursor.getLatestFinish() - cursor.getEarliestFinish());
			cursor.setIsCritical(cursor.getSlack() == 0.0);
			queuePredecessors(taskQueue, cursor.getId());
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
		Task maxTask = repo.findById(predecessors.get(0).getPredecesor().getId());

		double max = maxTask.getEarliestStart() + maxTask.getDuration();

		for (int i = 1; i < predecessors.size(); i++)
		{
			Task cursor = repo.findById(predecessors.get(i).getPredecesor().getId());

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
	 * Retrieves the minimum of the latest finish between the given list of
	 * successors of a node<br>
	 * <b>pre:</b> the list of successors is already initialized
	 * 
	 * @param sucessors - the list of successors where will be executed the search
	 *                  of the minimum value the latest finish
	 * @return the minimum value the latest finish between the given list of
	 *         successors
	 */
	private double getMinSuccessorLS(List<Transition> sucessors)
	{
		Task minTask = repo.findById(sucessors.get(0).getSuccessor().getId());
		double min;
		if (minTask.getLatestFinish() != null)
			min = minTask.getLatestFinish() - minTask.getDuration();
		else
			min = getMinSuccessorLS(minTask.getSuccessors());

		for (int i = 0; i < sucessors.size(); i++)
		{
			Task cursor = repo.findById(sucessors.get(i).getSuccessor().getId());
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
	 * Add the list of successors of the task with the given Id to the task queue to
	 * be the next in the width first search<br>
	 * 
	 * @param taskQueue - the queue containing the tasks
	 * @param next      - the Id of the current node predecessor of the nodes to be
	 *                  queued
	 * @return - the same task queue in case of no successors
	 */
	private Queue<Task> queueSuccessors(Queue<Task> taskQueue, int next)
	{
		Task successor = repo.findById(next);

		successor.getSuccessors().forEach(nextLv ->
		{
			Task toAdd = repo.findById(nextLv.getSuccessor().getId());
			taskQueue.add(toAdd);
		});

		return taskQueue;
	}

	/**
	 * Add the list of predecessors to the task queue
	 * 
	 * @param taskQueue - the queue containing the tasks
	 * @param next      - the task which will queue the successors
	 * @return - the same task queue in case of no successors
	 */
	private Queue<Task> queuePredecessors(Queue<Task> taskQueue, int next)
	{
		Task nextTask = repo.findById(next);

		nextTask.getPredecessors().forEach(predecessorEdge ->
		{
			Task toAdd = repo.findById(predecessorEdge.getPredecesor().getId());
			taskQueue.add(toAdd);
		});

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
	public Map<Integer, List<Task>> generateScenarios(List<Task> tasks, int numberOfScenarios)
	{
		Map<Integer, List<Task>> scenarios = repo.initScenarios(numberOfScenarios);

		AbstractRealDistribution distribution = null;

		for (Task task : tasks)
		{
			TaskDistribution taskDist = task.getDistribution();

			switch (taskDist.getDistributionType())
			{

			case NORMAL:
				distribution = new NormalDistribution(taskDist.getParam1(), taskDist.getParam2());
				break;

			case BETA:
				distribution = new BetaDistribution(taskDist.getParam1(), taskDist.getParam2());
				break;

			case LOG_NORMAL:
				distribution = new LogNormalDistribution(taskDist.getParam1(), taskDist.getParam2());
				break;

			case UNIFORM:
				distribution = new UniformRealDistribution(taskDist.getParam1(), taskDist.getParam2());
				break;

			case TRIANGULAR:
				distribution = new TriangularDistribution(taskDist.getParam1(), taskDist.getParam2(),
						taskDist.getParam3());
				break;

			default:
				distribution = new NormalDistribution();
				break;
			}

			double[] durations = distribution.sample(numberOfScenarios);

			for (int i = 0; i < durations.length; i++)
			{
				List<Task> scenario = repo.findScenarioById(i);
				Task taskScenario = new Task(task.getId(), task.getName(), durations[i]);
				if (scenario == null)
				{
					scenario = new ArrayList<Task>(tasks.size());
					repo.addScenario(i, scenario);
				}
				scenario.add(taskScenario);
			}
		}

		for (Task task : tasks)
		{
			for (int i = 0; i < numberOfScenarios; i++)
			{
				List<Task> scenario = scenarios.get(i);
				Task cursor = scenario.get(task.getId());
				List<Transition> successors = task.getSuccessors();
				for (Transition transition : successors)
				{
					Task successor = scenario.get(transition.getSuccessor().getId());
					Transition scenarioSuccessor = new Transition(cursor, successor);
					cursor.getSuccessors().add(scenarioSuccessor);
					successor.getPredecessors().add(scenarioSuccessor);

				}

			}
		}

		return repo.getAllScenarios();
	}

	@Override
	public List<Task> buildGraph(List<Task> tasks)
	{
		if (!tasks.isEmpty())
			repo.clearAllTasks();

		for (Task task : tasks)
		{
			repo.save(task);
		}

		return repo.findAll();
	}

	public List<Task> loadSampleTaks(String fileUrl)
	{
		return repo.loadTasksFromFile(fileUrl);
	}

	@Override
	public List<Task> loadPertSampleTasks(String fileUrl)
	{
		return repo.loadPertTasksFromFile(fileUrl);
	}

	@Override
	public List<Task> executePERTCPM(int scenarioId)
	{

		computeScenarioEarliestTimes(scenarioId);
		computeScenarioLatestTimesAndSlack(scenarioId);

		return repo.findScenarioById(scenarioId);
	}

	/**
	 * For each task in the scenario with the given Id, computes the values of the
	 * latest start, latest finish, slack and determines if the task is critical<br>
	 * <b>pre:</b> The scenarios area already initialized and the id of the scenario
	 * is contained in the array of scenarios resulting from simulation<br>
	 * <b>post:</b> After the execution of this method, all the tasks within the
	 * scenario with the given id, contains all the values needed for the CPM
	 * 
	 * @param scenarioId - the identifier of the scenario where is being executed
	 *                   the CPM and is going to be computed the latest times
	 *                   values, slacks and determine if the task is critical in the
	 *                   scenario <br>
	 * @return the scenario with all the values needed for the CPM computed
	 */
	private List<Task> computeScenarioLatestTimesAndSlack(int scenarioId)
	{
		List<Task> scenario = repo.findScenarioById(scenarioId);
		Task finish = scenario.get(scenario.size() - 1);
		Queue<Task> taskQueue = new LinkedList<Task>();
		finish.setLatestFinish(finish.getEarliestFinish());
		finish.setLatestStart(finish.getLatestFinish() - finish.getDuration());
		finish.setSlack(finish.getLatestFinish() - finish.getEarliestFinish());
		finish.setIsCritical(true);
		queueScenarioPredecessors(taskQueue, scenario, finish.getId());

		while (taskQueue.peek() != null)
		{
			Task cursor = taskQueue.poll();
			double minSuccessorDuration = getMinScenarioSuccessorLS(scenarioId,cursor.getSuccessors());
			cursor.setLatestFinish(minSuccessorDuration);
			cursor.setLatestStart(minSuccessorDuration - cursor.getDuration());
			cursor.setSlack(cursor.getLatestFinish() - cursor.getEarliestFinish());
			cursor.setIsCritical(cursor.getSlack() <= 0.0);
			queueScenarioPredecessors(taskQueue, scenario, cursor.getId());
		}
		return scenario;

	}

	private double getMinScenarioSuccessorLS(int scenarioId, List<Transition> sucessors)
	{
		List<Task> scenario = repo.findScenarioById(scenarioId);
		Task minTask = scenario.get(sucessors.get(0).getSuccessor().getId());
		double min;
		if (minTask.getLatestFinish() != null)
			min = minTask.getLatestFinish() - minTask.getDuration();
		else
			min = getMinScenarioSuccessorLS(scenarioId,minTask.getSuccessors());

		for (int i = 0; i < sucessors.size(); i++)
		{
			Task cursor = scenario.get(sucessors.get(i).getSuccessor().getId());
			double minCursor = 0.0;
			if (cursor.getLatestFinish() == null)
				minCursor = getMinScenarioSuccessorLS(scenarioId,cursor.getSuccessors()) - cursor.getDuration();
			else
				minCursor = cursor.getLatestFinish() - cursor.getDuration();
			min = (minCursor < min) ? minCursor : min;
		}
		return min;
	}

	private List<Task> queueScenarioPredecessors(Queue<Task> taskQueue, List<Task> scenario, int id)
	{
		Task current = scenario.get(id);

		current.getPredecessors().forEach(predecessorEdge ->
		{
			Task toAdd = scenario.get(predecessorEdge.getPredecesor().getId());
			taskQueue.add(toAdd);
		});
		return scenario;
	}

	private List<Task> computeScenarioEarliestTimes(int scenarioId)
	{
		List<Task> scenario = repo.findScenarioById(scenarioId);
		Task start = scenario.get(0);
		Queue<Task> taskQueue = new LinkedList<Task>();
		start.setEarliestStart(0.0);
		start.setEarliestFinish(start.getDuration());
		queueScenarioSuccessors(taskQueue, scenario, start.getId());

		while (taskQueue.peek() != null)
		{
			Task cursor = taskQueue.poll();

			double maxPredecesorDuration = getMaxScenarioPredecesorES(scenarioId, cursor.getPredecessors());
			cursor.setEarliestStart(maxPredecesorDuration);
			cursor.setEarliestFinish(maxPredecesorDuration + cursor.getDuration());
			queueScenarioSuccessors(taskQueue, scenario, cursor.getId());
		}
		return scenario;

	}

	private double getMaxScenarioPredecesorES(int scenarioId, List<Transition> predecessors)
	{
		List<Task> scenario =repo.findScenarioById(scenarioId);
		Task maxTask = scenario.get(predecessors.get(0).getPredecesor().getId());

		double max = maxTask.getEarliestStart() + maxTask.getDuration();

		for (int i = 1; i < predecessors.size(); i++)
		{
			Task cursor = scenario.get(predecessors.get(i).getPredecesor().getId());

			double maxCursor = 0.0;

			if (cursor.getEarliestStart() == null)
				maxCursor = getMaxScenarioPredecesorES(scenarioId, cursor.getPredecessors()) + cursor.getDuration();
			else
				maxCursor = cursor.getEarliestStart() + cursor.getDuration();

			max = (maxCursor > max) ? maxCursor : max;
		}
		return max;

	}

	/**
	 * Queue the successors of the node with the given Id in the given scenario, to
	 * be the next in the graph width first search<br>
	 * <b>pre:</b> the queue is already initialized<br>
	 * <b>post:</b> after the execution of the method the successors of the node
	 * with the given Id are included in the given queue<br>
	 * 
	 * @param taskQueue - The queue which
	 * @param scenario
	 * @param id
	 */
	private void queueScenarioSuccessors(Queue<Task> taskQueue, List<Task> scenario, int id)
	{
		Task current = scenario.get(id);
		current.getSuccessors().forEach(nextLv ->
		{
			Task toAdd = scenario.get(nextLv.getSuccessor().getId());
			taskQueue.add(toAdd);
		});
	}
}
