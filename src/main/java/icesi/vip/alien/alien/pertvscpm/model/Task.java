package icesi.vip.alien.alien.pertvscpm.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * @author jcampaz
 *
 */
/**
 * @author jcampaz
 *
 */

public class Task
{

	// General attributes for all tasks

	/**
	 * The identifier of the task, the identifier is not repeatable
	 */

	private Integer id;

	/**
	 * the name of the task
	 */

	private String name;

	/**
	 * the standard duration used to compute the earliest and latest times for the
	 * CPM, in PERT this duration is computed using the distribution of the tasks
	 * and its specific parameters
	 */

	private Double duration;

	// CPM attributes

	/**
	 * The earliest start time of the task, for the starting task is 0, for the
	 * other tasks is computed using the longest earliest finish time between the
	 * predecessors 
	 */

	private Double earliestStart;

	/**
	 * The earliest finishing time of the task, for the starting task is the
	 * starting time plus the duration, for the other tasks is computed using the
	 * maximum earliest finish time between the predecessors  plus the duration of
	 * the task
	 */

	private Double earliestFinish;

	/**
	 * The latest start time of the task, for the finishing task is the earliest
	 * finishing time minus the duration, for the other tasks is computed using the
	 * minimum latest start time between the successors plus the duration of
	 * the taskla
	 */
	private Double latestStart;

	/**
	 * 
	 */
	private Double latestFinish;

	/**
	 * 
	 */
	private Double slack;

	/**
	 * 
	 */
	private Boolean isCritical;

	@JsonBackReference("Successors")
	private List<Transition> Successors;

	@JsonBackReference("predecessors")
	private List<Transition> predecessors;

	// PERT attributes

	private TaskDistribution distribution;

	public Task()
	{
		super();
		Successors = new ArrayList<Transition>();
		predecessors = new ArrayList<Transition>();
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param name
	 * @param duration
	 */
	public Task(int id, String name, Double duration)
	{
		super();
		this.id = id;
		this.name = name;
		this.duration = duration;
		Successors = new ArrayList<Transition>();
		predecessors = new ArrayList<Transition>();

	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Double getDuration()
	{
		return duration;
	}

	public void setDuration(Double duration)
	{
		this.duration = duration;
	}

	/**
	 * @return the earliestStart
	 */
	public Double getEarliestStart()
	{
		return earliestStart;
	}

	/**
	 * @param earliestStart the earliestStart to set
	 */
	public void setEarliestStart(Double earliestStart)
	{
		this.earliestStart = earliestStart;
	}

	/**
	 * @return the earliestFinish
	 */
	public Double getEarliestFinish()
	{
		return earliestFinish;
	}

	/**
	 * @param earliestFinish the earliestFinish to set
	 */
	public void setEarliestFinish(Double earliestFinish)
	{
		this.earliestFinish = earliestFinish;
	}

	/**
	 * @return the latestStart
	 */
	public Double getLatestStart()
	{
		return latestStart;
	}

	/**
	 * @param latestStart the latestStart to set
	 */
	public void setLatestStart(Double latestStart)
	{
		this.latestStart = latestStart;
	}

	/**
	 * @return the latestFinish
	 */
	public Double getLatestFinish()
	{
		return latestFinish;
	}

	/**
	 * @param latestFinish the latestFinish to set
	 */
	public void setLatestFinish(Double latestFinish)
	{
		this.latestFinish = latestFinish;
	}

	/**
	 * @return the slack
	 */
	public Double getSlack()
	{
		return slack;
	}

	/**
	 * @param slack the slack to set
	 */
	public void setSlack(Double slack)
	{
		this.slack = slack;
	}

	/**
	 * @return the isCritical
	 */
	public Boolean getIsCritical()
	{
		return isCritical;
	}

	/**
	 * @param isCritical the isCritical to set
	 */
	public void setIsCritical(Boolean isCritical)
	{
		this.isCritical = isCritical;
	}

	/**
	 * @return the Successors
	 */
	public List<Transition> getSuccessors()
	{
		return Successors;
	}

	/**
	 * @param Successors the Successors to set
	 */
	public void setSuccessors(List<Transition> Successors)
	{
		this.Successors = Successors;
	}

	/**
	 * @return the predecessors
	 */
	public List<Transition> getPredecessors()
	{
		return predecessors;
	}

	/**
	 * @param predecessors the predecessors to set
	 */
	public void setPredecessors(List<Transition> predecessors)
	{
		this.predecessors = predecessors;
	}

	/**
	 * @return the distribution
	 */
	public TaskDistribution getDistribution()
	{
		return distribution;
	}

	/**
	 * @param distribution the distribution to set
	 */
	public void setDistribution(TaskDistribution distribution)
	{
		this.distribution = distribution;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id)
	{
		this.id = id;
	}

	@Override
	public String toString()
	{
		return "[id=" + id + " name: " + name + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
