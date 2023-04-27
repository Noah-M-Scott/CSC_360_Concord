package ConcordData;

import java.util.ArrayList;


public class Role {
	public String Name = "null";
	public ArrayList< Pair<String, Boolean> > Perms = new ArrayList< Pair<String, Boolean> >();
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public ArrayList<Pair<String, Boolean>> getPerms() {
		return Perms;
	}
	public void setPerms(ArrayList<Pair<String, Boolean>> perms) {
		Perms = perms;
	}
	

}
