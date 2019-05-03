package tours;

import java.util.ArrayList;

public class Tour{
	
	ArrayList<Nodo> t;
	boolean taken;
	
	public Tour(int size) {
		t=new ArrayList<>();
		taken=false;
	}
	
	public boolean isTaken() {
		return taken;
	}

	public void setTaken(boolean taken) {
		this.taken = taken;
	}

	public Tour() {
		t=new ArrayList<Nodo>();
	}
	
	public ArrayList<Nodo> getT(){
		return t;
	}
	
	public void setT(ArrayList<Nodo> t) {
		this.t=t;
	}
	
}
