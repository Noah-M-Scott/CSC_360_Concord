package ConcordData;

import java.util.ArrayList;

public class GroupData {
	public ArrayList<GroupUserData> Users = new ArrayList<GroupUserData>();
	public ArrayList<ChatListing> Chats   = new ArrayList<ChatListing>();
	public ArrayList<Role> Roles          = new ArrayList<Role>();   
	public long GroupId;
	
	public long addChat(ChatListing in) {
		Chats.add(in);
		return Chats.size() - 1;
	}
	
	public boolean deleteChat(long ChatId) {
		if(Chats.size() - 1 < ChatId)
			return false;
		for(int i = 0; i < Chats.get((int) ChatId).Chat.size(); i++)
			Chats.get((int) ChatId).deleteMsg(Chats.get((int) ChatId).Chat.get(i).MsgIndex);
		return true;
	}
	
	public ChatListing findChatById(long ChatId){
		return Chats.get((int) ChatId);
	}

	public GroupUserData addUser(UserData in){
		Users.add(new GroupUserData());
		int index = Users.size() - 1;
		Users.get(index).UserId = in.UserId;
		Users.get(index).Nickname = in.DisplayName;
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
		return Users.get((int) GroupUserId);
	}

}
