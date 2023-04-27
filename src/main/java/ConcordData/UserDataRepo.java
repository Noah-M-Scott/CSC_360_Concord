package ConcordData;

import java.util.HashMap;

public class UserDataRepo {
	public HashMap<Long, UserData> Users = new HashMap<Long, UserData>();
	public HashMap<String, Long> Names = new HashMap<String, Long>();
	private long idTally = 0;
	
	public UserData findUserById(long id){
		return Users.get(id);
	}
	
	public UserData findUserByName(String id){
		return Users.get(Names.get(id));
	}
	
	public void deleteUser(long id){
		Names.remove(Users.get(id).DisplayName);
		Users.remove(id);
	}
	
	public void addUser(UserData in){
		in.UserId = idTally++;
		Users.put(in.UserId, in);
		Names.put(in.DisplayName, in.UserId);
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
