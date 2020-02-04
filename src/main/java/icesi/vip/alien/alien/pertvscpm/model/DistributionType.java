package icesi.vip.alien.alien.pertvscpm.model;

public enum DistributionType
{
	NORMAL("Normal"),
	BETA("Beta"),
	LOG_NORMAL("Log Normal"),
	UNIFORM("Uniform"), 
	TRIANGULAR("Triangular");
	
	private String distributionType;
	
	DistributionType(String distType)
	{
		distributionType=distType;
	}
	
	public String getDistributionType()
	{
		return distributionType;
	}
}
