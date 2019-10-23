package icesi.vip.alien.alien.pertvscpm.model;

/**
 * @author jcampaz
 *
 */
public class TaskDistribution
{

	/**
	 * The real distribution type that will be used to generate the
	 * task duration, it can be:
	 * <ul>
	 * Normal <br>
	 * Log_Normal <br>
	 * Beta <br>
	 * Uniform
	 * </ul>
	 */
	private DistributionType distributionType;

	private Double param1;

	private Double param2;

	public TaskDistribution()
	{

	}

	public TaskDistribution(DistributionType distributiontype, Double param1, Double param2)
	{
		this.distributionType = distributiontype;
		this.param1 = param1;
		this.param2 = param2;
	}

	/**
	 * @return the distribution
	 */
	public DistributionType getDistributionType()
	{
		return distributionType;
	}

	/**
	 * @param distributionType the distribution to set
	 */
	public void setDistribution(DistributionType distributionType)
	{
		this.distributionType = distributionType;
	}

	/**
	 * @return the param1
	 */
	public Double getParam1()
	{
		return param1;
	}

	/**
	 * @param param1 the param1 to set
	 */
	public void setParam1(Double param1)
	{
		this.param1 = param1;
	}

	/**
	 * @return the param2
	 */
	public Double getParam2()
	{
		return param2;
	}

	/**
	 * @param param2 the param2 to set
	 */
	public void setParam2(Double param2)
	{
		this.param2 = param2;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((distributionType == null) ? 0 : distributionType.hashCode());
		result = prime * result + ((param1 == null) ? 0 : param1.hashCode());
		result = prime * result + ((param2 == null) ? 0 : param2.hashCode());
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
		TaskDistribution other = (TaskDistribution) obj;
		if (distributionType == null)
		{
			if (other.distributionType != null)
				return false;
		}
		else if (!distributionType.equals(other.distributionType))
			return false;
		if (param1 == null)
		{
			if (other.param1 != null)
				return false;
		}
		else if (!param1.equals(other.param1))
			return false;
		if (param2 == null)
		{
			if (other.param2 != null)
				return false;
		}
		else if (!param2.equals(other.param2))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "TaskDistribution [distribution=" + distributionType + ", param1=" + param1 + ", param2=" + param2 + "]";
	}

}
