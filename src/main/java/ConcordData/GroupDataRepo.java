package ConcordData;

import java.util.HashMap;

public class GroupDataRepo {
	private HashMap<Long, GroupData> Groups = new HashMap<>();
	private long idTally = 0;
	
	public GroupData findGroupById(long id){
		return Groups.get(id);
	}
	
	public void deleteGroup(long id){
		Groups.remove(id);
	}
	
	public void addGroup(GroupData in){
		in.setGroupId(idTally);
		idTally++;
		Groups.put(in.getGroupId(), in);
	}

	public HashMap<Long, GroupData> getGroups() {
		return Groups;
	}

	public void setGroups(HashMap<Long, GroupData> groups) {
		Groups = groups;
	}

	public long getIdTally() {
		return idTally;
	}

	public void setIdTally(long idTally) {
		this.idTally = idTally;
	}
}
