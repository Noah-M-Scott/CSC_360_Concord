package ConcordData;

import java.io.Serializable;
import java.util.ArrayList;

public class GroupData implements Serializable {
	private static final long serialVersionUID = 1212353120908531298L;
	private ArrayList<GroupUserData> Users = new ArrayList<>();
	private ArrayList<ChatListing> Chats   = new ArrayList<>();
	private ArrayList<Role> Roles          = new ArrayList<>();
	private ArrayList<TextCheck>	Check     = new ArrayList<>();
	private String Name = "NewGroup";
	private long GroupId;
	
	public ArrayList<TextCheck> getCheck() {
		return Check;
	}

	public void setCheck(ArrayList<TextCheck> check) {
		Check = check;
	}

	public ArrayList<GroupUserData> getUsers() {
		return Users;
	}

	public void setUsers(ArrayList<GroupUserData> users) {
		Users = users;
	}

	public ArrayList<ChatListing> getChats() {
		return Chats;
	}

	public void setChats(ArrayList<ChatListing> chats) {
		Chats = chats;
	}

	public ArrayList<Role> getRoles() {
		return Roles;
	}

	public void setRoles(ArrayList<Role> roles) {
		Roles = roles;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public long getGroupId() {
		return GroupId;
	}

	public void setGroupId(long groupId) {
		GroupId = groupId;
	}

	public long getChatIdCounter() {
		return chatIdCounter;
	}

	public void setChatIdCounter(long chatIdCounter) {
		this.chatIdCounter = chatIdCounter;
	}

	private long chatIdCounter;
	public long addChat(ChatListing in) {
		in.setCheck(Check);
		in.setChatId(chatIdCounter++);
		Chats.add(in);
		return Chats.size() - (long)1;
	}
	
	public boolean deleteChat(long ChatId) {
		for(int i = 0; i < Chats.size(); i++) {
			if (Chats.get(i).getChatId() == ChatId) {
				Chats.remove(Chats.get(i));
				return true;
			}
		}
		return false; 
	}
	
	public ChatListing findChatById(long ChatId){
		for(int i = 0; i < Chats.size(); i++) {
			if (Chats.get(i).getChatId() == ChatId)
				return Chats.get(i);
		}
		return null; 
	}

	public GroupUserData addUser(UserData in){
		Users.add(new GroupUserData());
		int index = Users.size() - 1;
		Users.get(index).UserId = in.getUserId();
		Users.get(index).Nickname = in.getDisplayName();
		return Users.get(index);
	}
	
	public boolean removeUserFromGroup(long in){
		if(Users.size() - 1 < in)
			return false;
		
		Users.get((int) in).UserId = -1;       
		Users.get((int) in).Nickname = "removed";
		return true;
	}

	public GroupUserData findGroupUserById(long GroupUserId){
		for(int i = 0; i < Users.size(); i++)
			if(Users.get(i).UserId == GroupUserId)
				return Users.get(i);
		return null;
	}

}
