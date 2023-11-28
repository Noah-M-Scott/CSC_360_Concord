package ConcordData;

import java.io.Serializable;
import java.util.ArrayList;

public class UserData implements Serializable {
	private static final long serialVersionUID = -94966710639738628L;
	private long UserId;
	private ArrayList<Long> JoinedGroupIds  = new ArrayList<> ();
	private int Status;
	private String DisplayName;
	private String Password;
	private String Email;
	
	public long getUserId() {
		return UserId;
	}
	public void setUserId(long userId) {
		UserId = userId;
	}
	public ArrayList<Long> getJoinedGroupIds() {
		return JoinedGroupIds;
	}
	public void setJoinedGroupIds(ArrayList<Long> joinedGroupIds) {
		JoinedGroupIds = joinedGroupIds;
	}
	public int getStatus() {
		return Status;
	}
	public void setStatus(int status) {
		Status = status;
	}
	public String getDisplayName() {
		return DisplayName;
	}
	public void setDisplayName(String displayName) {
		DisplayName = displayName;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}	
	
	
}
