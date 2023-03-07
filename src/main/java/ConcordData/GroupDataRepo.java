package ConcordData;

import java.util.HashMap;

public class GroupDataRepo {
	public HashMap<Long, GroupData> Groups = new HashMap<Long, GroupData>();
	
	public GroupData findGroupById(long id){
		return Groups.get(id);
	}
	
	public void deleteGroup(long id){
		Groups.remove(id);
	}
	
	public void addGroup(GroupData in){
		Groups.put(in.GroupId, in);
	}
}
