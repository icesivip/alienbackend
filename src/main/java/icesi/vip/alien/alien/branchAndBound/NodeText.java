package icesi.vip.alien.alien.branchAndBound;

public class NodeText {

	/*
	 * Will be used for Level of Node
	 */
	private String name;
	/*
	 * Will be used for model description
	 */
	private String title;
	/*
	 * Will describe solution
	 */
	private String desc;
	
	
	public NodeText(String name, String title,String desc) {
		this.name=name;
		this.title=title;
		if(desc==null) {
			desc="UNFEASIBLE";
		}else {
			this.desc=desc;
		}
		
	}


	public String getName() {
		return name;
	}


	public String getTitle() {
		return title;
	}
	
	public String getDesc() {
		return desc;
	}
	
	
	
	
	
}
