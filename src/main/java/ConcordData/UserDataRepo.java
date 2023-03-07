package ConcordData;

import java.util.HashMap;

public class UserDataRepo {
	public HashMap<Long, UserData> Users = new HashMap<Long, UserData>();
	
	public UserData findUserById(long id){
		return Users.get(id);
	}
	
	public void deleteUser(long id){
		Users.remove(id);
	}
	
	public void addUser(UserData in){
		Users.put(in.UserId, in);
	}
}
