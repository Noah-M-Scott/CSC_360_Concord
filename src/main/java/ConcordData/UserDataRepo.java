package ConcordData;

import java.util.HashMap;

public class UserDataRepo {
	private HashMap<Long, UserData> Users = new HashMap<>();
	private HashMap<String, Long> Names = new HashMap<>();
	private long idTally = 0;
	
	public UserData findUserById(long id){
		return Users.get(id);
	}
	
	public UserData findUserByName(String id){
		return Users.get(Names.get(id));
	}
	
	public void deleteUser(long id){
		Names.remove(Users.get(id).getDisplayName());
		Users.remove(id);
	}
	
	public void addUser(UserData in){
		in.setUserId(idTally++);
		Users.put(in.getUserId(), in);
		Names.put(in.getDisplayName(), in.getUserId());
	}

	public HashMap<Long, UserData> getUsers() {
		return Users;
	}

	public void setUsers(HashMap<Long, UserData> users) {
		Users = users;
	}

	public HashMap<String, Long> getNames() {
		return Names;
	}

	public void setNames(HashMap<String, Long> names) {
		Names = names;
	}

	public long getIdTally() {
		return idTally;
	}

	public void setIdTally(long idTally) {
		this.idTally = idTally;
	}
	
	
}
