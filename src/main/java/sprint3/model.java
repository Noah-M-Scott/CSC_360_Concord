package sprint3;

import java.rmi.RemoteException;

import ConcordData.ChatListing;
import ConcordData.GroupData;
import ConcordData.MsgData;
import ConcordData.Pair;
import ConcordData.Role;
import ConcordData.UserData;
import concordSprint2.ClientObject;

public class model {

	//this is the current chat listing the msg window uses
	int msgIndex = 0;
	int msgWindow;
	ChatListing currentChannel = new ChatListing();
	
	//this is the current group data the chn window uses
	int chnIndex = 0;
	int chnWindow;
	GroupData currentGroup = new GroupData();
	
	//this is the current groupDataRepo the grp window uses
	int grpIndex = 0;
	int userGroupIndex = 0;
	int grpWindow;	
	UserData currentUser = new UserData();
	
	ClientObject Client;
	
	
	//-----------------the stuff in this box is all changed to use the server-------------------------
	
	//done
	public void init(ClientObject in) {
		Client = in;
		currentUser = Client.CurrentUser;
		currentGroup = Client.CurrentGroup;
		currentChannel = new ChatListing();
		chnIndex = 0;
		userGroupIndex = 0;
		grpIndex = 0;
		msgIndex = 0;
		//here you init all the current data
	}
	
	//done
	public void makeGroup() throws RemoteException {
		GroupData newGroup = new GroupData();
		
		newGroup.addUser(currentUser);
		
		Role admin = new Role();
		admin.Name = "admin";
		admin.Perms.add(new Pair<>("make chat", true));
		admin.Perms.add(new Pair<>("make role", true));
		admin.Perms.add(new Pair<>("edit role", true));
		admin.Perms.add(new Pair<>("make msg", true));
		admin.Perms.add(new Pair<>("invite user", true));
		admin.Perms.add(new Pair<>("delete chat", true));
		admin.Perms.add(new Pair<>("delete role", true));
		admin.Perms.add(new Pair<>("delete msg", true));
		admin.Perms.add(new Pair<>("delete group", true));
		admin.Perms.add(new Pair<>("rename chat", true));
		admin.Perms.add(new Pair<>("rename group", true));
		admin.Perms.add(new Pair<>("edit checks", true));
		newGroup.getRoles().add(admin);
		newGroup.findGroupUserById(currentUser.getUserId()).Roles.add(admin);
		
		Client.makeGroup(currentUser.getUserId(), newGroup);
		currentUser.setJoinedGroupIds(Client.CurrentUser.getJoinedGroupIds());
	}
	
	//done
	public void makeChn() throws RemoteException {
		ChatListing newChat = new ChatListing();
		
		Client.makeChatListing(Client.CurrentUser.getUserId(), currentGroup.getGroupId(), newChat);
		
		currentGroup = Client.CurrentGroup;
	}
	
	//done
	public void renameChn(String in) throws RemoteException {
		Client.renameChat(currentUser.getUserId(), currentGroup.getGroupId(), currentChannel.getChatId(), in);
	}
	
	//done
	public void renameGrp(String in) throws RemoteException {
		Client.renameGroup(currentUser.getUserId(), currentGroup.getGroupId(), in);
	}

	//done
    void deleteGrp() throws RemoteException {
    	
    	for(int i = 0; i < Client.CurrentUser.getJoinedGroupIds().size(); i++)
    		if(Client.CurrentUser.getJoinedGroupIds().get(i) == currentGroup.getGroupId())
    			Client.CurrentUser.getJoinedGroupIds().remove(i);
    	
    	Client.updateUserData(currentUser.getUserId());
    	Client.deleteGroup(currentUser.getUserId(), currentGroup.getGroupId());
    	selectGroup(0);
    	selectChannel(0);
    }

    //done
    void deleteChn() throws RemoteException {	
    	Client.deleteChatListing(currentUser.getUserId(), currentGroup.getGroupId(), currentChannel.getChatId());
    	selectChannel(0);
    }
    
    
    boolean inRoles = false;
    //done
    void deleteMsg(int inIndex) throws RemoteException {
    	if(inRoles)
    		return;
    	
    	Client.deleteMsg(currentUser.getUserId(), currentGroup.getGroupId(), currentChannel.getChatId(), currentChannel.getChat().size() - 1 - (long)(msgIndex + inIndex));
    }
    
    public void deleteRole(String name) throws RemoteException {
    	Client.deleteRole(currentUser.getUserId(), currentGroup.getGroupId(), name);
    	roleView();
    	return;
    }
    
    public boolean makeRole(String name, boolean canChats, boolean canRoles, boolean canMsg, boolean canUser, boolean canGroup) throws RemoteException {	
    	Role temp = new Role();
		temp.Name = name;
		
		for(int i = 0; i < currentGroup.getRoles().size(); i++)
			if(currentGroup.getRoles().get(i).Name.equals(name))
				return false;
		
		
  		if(canChats){
  			temp.Perms.add(new Pair<>("make chat", true));
  			temp.Perms.add(new Pair<>("delete chat", true));
  			temp.Perms.add(new Pair<>("rename chat", true));
  		}
  		
  		if(canRoles){
  			temp.Perms.add(new Pair<>("make role", true));
  			temp.Perms.add(new Pair<>("delete role", true));
  			temp.Perms.add(new Pair<>("edit role", true));
  		}
  		
  		if(canMsg){
  			temp.Perms.add(new Pair<>("make msg", true));
  			temp.Perms.add(new Pair<>("delete msg", true));
  		}
  		
  		if(canUser){
  			temp.Perms.add(new Pair<>("invite user", true));
  		}
  		
  		if(canGroup){
  			temp.Perms.add(new Pair<>("rename group", true));
  			temp.Perms.add(new Pair<>("delete group", true));
  			temp.Perms.add(new Pair<>("edit checks", true));
  		}
		
		Client.makeRole(currentUser.getUserId(), currentGroup.getGroupId(), temp);
		
		roleView();
		
		return true;
    }
    
    
    void addCheck(String name) throws RemoteException {
    	Client.addACheck(currentUser.getUserId(), currentGroup.getGroupId(), name);
    	checkView();
    }
    
    void removeCheck(String name) throws RemoteException {
    	Client.takeACheck(currentUser.getUserId(), currentGroup.getGroupId(), name);
    	checkView();
    }
    
    void checkView() {
    	inRoles = true;
    	currentChannel = new ChatListing();
    	
    	MsgData temp = new MsgData();
    	temp.Text = "Current Checks:";
    	currentChannel.addMsg(temp);
    	
    	for(int i = 0; i < ClientObject.AvailibleCheckNames.length; i++) {
    		
    		boolean flag = false;
    		for(int j = 0; j < Client.CurrentGroup.getCheck().size(); j++) {
    			
    			if(Client.CurrentGroup.getCheck().get(j).getName().equals(ClientObject.AvailibleCheckNames[i])) {
    				temp = new MsgData();
    				temp.Text = Client.CurrentGroup.getCheck().get(j).getName() + " : active";
    		    	currentChannel.addMsg(temp);
    		    	flag = true;
    		    	break;
    			}
    		}
    		
    		if(!flag) {
				temp = new MsgData();
				temp.Text = ClientObject.AvailibleCheckNames[i] + " : available";
		    	currentChannel.addMsg(temp);
			}
    	}
    }
    
    public void giveTakeRole(String Target, boolean giveQ) throws RemoteException {
    	long targetuserid = 0;
		
    	String username = "";
    	String rolename = "";
    	
    	//split the string here
    	int pivot = 0;
    	for(; (pivot < Target.length()) && (Target.charAt(pivot) != ':'); pivot++)
    		username += Target.charAt(pivot);
    	
    	pivot++;
    	
    	if(pivot >= Target.length() - 1) {
    		return;
    	}
    	
    	for(int i = 0; i < currentGroup.getUsers().size(); i++) {
    		if(currentGroup.getUsers().get(i).Nickname.equals(username))
    			break;
    		if(i == currentGroup.getUsers().size() - 1)
    			return;
    	}
    	
    	
    	
    	for(; pivot < Target.length(); pivot++)
    		rolename += Target.charAt(pivot);
    	
		for(int i = 0; i < currentGroup.getUsers().size(); i++)
	  		if(currentGroup.getUsers().get(i).Nickname.equals(username))
	  			targetuserid = currentGroup.getUsers().get(i).UserId;
		
		Client.giveTakeRole(currentUser.getUserId(), currentGroup.getGroupId(), targetuserid, rolename, giveQ);
		
		roleView();
    }
    
    
    //done
  	public void makeMsg(String message) throws RemoteException {
  		
  		MsgData newMsg = new MsgData();
  		newMsg.deleted = false;
  		newMsg.Text = currentUser.getDisplayName() + ": " + message;
  		Client.sendMsg(currentUser.getUserId(), currentGroup.getGroupId(), currentChannel.getChatId(), newMsg);
  	}
	
    
    void roleView() {
    	inRoles = true;
    	currentChannel = new ChatListing();
    	
    	MsgData temp = new MsgData();
    	temp.Text = "Current Roles:";
    	currentChannel.addMsg(temp);
    	
    	for(int i = 0; i < Client.CurrentGroup.getRoles().size(); i++) {
    			temp = new MsgData();
    			temp.Text = Client.CurrentGroup.getRoles().get(i).Name + " { ";
    			
    			for(int j = 0; j < Client.CurrentGroup.getRoles().get(i).Perms.size(); j++) {
    				temp.Text += Client.CurrentGroup.getRoles().get(i).Perms.get(j).getKey() + ":";
    				temp.Text += Client.CurrentGroup.getRoles().get(i).Perms.get(j).getValue().toString() + ", ";
    				
    				if(j % 3 == 0) {
    	    			currentChannel.addMsg(temp);
    	    			temp = new MsgData();
    	    			temp.Text = "";
    				}
    			}
    			temp.Text += "}";
    			
    			currentChannel.addMsg(temp);
    			
    			
    			
    			
    			temp = new MsgData();
	    		temp.Text = "";
    			for(int j = 0; j < Client.CurrentGroup.getUsers().size(); j++) 
    				for(int k = 0; k < Client.CurrentGroup.getUsers().get(j).Roles.size(); k++)
    					if(Client.CurrentGroup.getUsers().get(j).Roles.get(k).Name.equals(Client.CurrentGroup.getRoles().get(i).Name)) {
	    					temp.Text += Client.CurrentGroup.getUsers().get(j).Nickname + " has ";
	    					temp.Text += Client.CurrentGroup.getRoles().get(i).Name;
	    	    			currentChannel.addMsg(temp);
	    	    			temp = new MsgData();
	    	    			temp.Text = "";
	    				}
    	}
    }
    
    public String qUserId() {
    	return Long.toString(currentUser.getUserId());
    }
    
    public void deleteUser(String password) throws RemoteException {
    	
    	Client.deleteUser(currentUser.getUserId(), password);
    	
    }
    
    public void addUser(long userId) throws RemoteException {
    	
    	
    	if(!Client.inviteUser(currentUser.getUserId(), currentGroup.getGroupId(), userId).equals("ok"))
    		return;
    	
    	String username = "";
    	
    	for(int i = 0; i < currentGroup.getUsers().size(); i++)
    		if(currentGroup.getUsers().get(i).UserId == userId)
    			username = currentGroup.getUsers().get(i).Nickname;
    	
    	selectChannel(0);
    	MsgData newMsg = new MsgData();
  		newMsg.deleted = false;
  		newMsg.Text =  username + " has joined the group";
  		Client.sendMsg(currentUser.getUserId(), currentGroup.getGroupId(), currentChannel.getChatId(), newMsg);
    }
    
    
    
	//-------------------------------------------------------------------
	
	
    //change the current chat/channel to the one clicked, array position equal to chnIndex + offset
	public void selectChannel(int offset) {
		inRoles = false;
		if(offset >= currentGroup.getChats().size()) {
			currentChannel = new ChatListing();
			return;
		}
		
		currentChannel = currentGroup.getChats().get(chnIndex + offset);
		msgIndex = 0;
	}
	
	//change the current group to the one clicked, array position equal to grpIndex + offset
	public void selectGroup(int offset) {
		inRoles = false;
		if(offset >= currentUser.getJoinedGroupIds().size()) {
			currentGroup = new GroupData();
			return;
		}
		
		Client.getGroupData(currentUser.getUserId(), currentUser.getJoinedGroupIds().get(grpIndex + offset));
		currentGroup = Client.CurrentGroup;
		chnIndex = 0;
	}
	
	
	//set how many labels are in the msg, chn, and grp windows
	public void setMsgWindow(int size) {
		msgWindow = size;
	}
	
	public void setChnWindow(int size) {
		chnWindow = size;
	}
	
	public void setGrpWindow(int size) {
		grpWindow = size;
	}
	
	//call this to step from the index, grabbing messages
	int msgstep = 0;
	public String getMsgWindow() {
		if(msgstep == msgWindow - 1) { //reset the step to zero after calling the size of the window
			int hold = msgstep;
			msgstep = 0;
			
			if((msgIndex + hold) >= currentChannel.getChat().size()) //non-existant message
				return "";
			
			
			return currentChannel.getChat().get(currentChannel.getChat().size() - 1 - (msgIndex + hold)).Text;
		}
		
		if((msgIndex + msgstep) >= currentChannel.getChat().size()) {
			msgstep++;
			return "";
		}
		

		return currentChannel.getChat().get(currentChannel.getChat().size() - 1 - (msgIndex + msgstep++)).Text;
	}
	
	//see above
	int chnstep = 0;
	public String getChnWindow() {
		if(chnstep == chnWindow - 1) {
			int hold = chnstep;
			chnstep = 0;
			
			if(chnIndex + hold >= currentGroup.getChats().size())
				return "";
			
			return currentGroup.getChats().get(chnIndex + hold).getChatName();
		}
		
		if(chnIndex + chnstep >= currentGroup.getChats().size()) {
			chnstep++;
			return "";
		}
		
		return currentGroup.getChats().get(chnIndex + chnstep++).getChatName();
	}
	
	//see above
	int grpstep = 0;
	public String getGrpWindow() {
		if(grpstep == grpWindow - 1) {
			int hold = grpstep;
			grpstep = 0;
			if(grpIndex + hold >= currentUser.getJoinedGroupIds().size())
				return "";
			
			userGroupIndex = grpIndex + hold;
			
			
			GroupData holdGroup = Client.CurrentGroup;
			
			Client.getGroupData(currentUser.getUserId(), currentUser.getJoinedGroupIds().get(grpIndex + hold));
			
			String name = Client.CurrentGroup.getName();
			
			Client.CurrentGroup = holdGroup;
			
			return name;
		}
		
		if(grpIndex + grpstep >= currentUser.getJoinedGroupIds().size()) {
			grpstep++;
			return "";
		}
		
		if(Client.CurrentGroup == null) {
			return "";
		}
		
		userGroupIndex = grpIndex + grpstep;
		
		GroupData holdGroup = Client.CurrentGroup;
		
		Client.getGroupData(currentUser.getUserId(), currentUser.getJoinedGroupIds().get(grpIndex + grpstep++));
		
		String name = Client.CurrentGroup.getName();
		
		Client.CurrentGroup = holdGroup;		
		return name;
	}
	
	//increment the index for the message window, saturating
	public void incMsgWindow() {
		msgIndex = msgIndex < (currentChannel.getChat().size() - msgWindow) ? msgIndex + 1 : msgIndex;
	}
	
	//decrement the index for the message window, saturating
	public void decMsgWindow() {
		msgIndex -= msgIndex > 0 ? 1 : 0;
	}
	
	//see above, for bellow
	public void incChnWindow() {
		chnIndex = chnIndex < (currentGroup.getChats().size() - chnWindow) ? chnIndex + 1 : chnIndex;
	}
	
	public void decChnWindow() {
		chnIndex -= chnIndex > 0 ? 1 : 0;
	}
	
	public void incGrpWindow() {
		grpIndex = grpIndex < (currentUser.getJoinedGroupIds().size() - grpWindow) ? grpIndex + 1 : grpIndex;
	}
	
	public void decGrpWindow() {
		grpIndex -= grpIndex > 0 ? 1 : 0;
	}
	
}
