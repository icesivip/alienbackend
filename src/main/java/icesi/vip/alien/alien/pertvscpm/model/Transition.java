package icesi.vip.alien.alien.pertvscpm.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class Transition implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonBackReference("predecessors")
	private Task predecesor;

	@JsonBackReference("Successors")
	private Task succesor;

	private String type;

	public Transition()
	{
		super();
	}

	public Transition(Task predecesor, Task succesor)
	{
		super();
		this.predecesor = predecesor;
		this.succesor = succesor;
		this.type = "FS";
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public Task getPredecesor()
	{
		return predecesor;
	}

	public void setPredecesor(Task predecesor)
	{
		this.predecesor = predecesor;
	}

	public Task getSuccessor()
	{
		return succesor;
	}

	public void setSuccessor(Task successor)
	{
		this.succesor = successor;
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

		Transition other = (Transition) obj;

		return this.predecesor.getName().equals(other.getPredecesor().getName())
				&& this.succesor.getName().equals(other.getSuccessor().getName());
	}

	@Override
	public String toString()
	{
		return "[" + predecesor.getName() + "=>" + succesor.getName() + "]";
	}

}
