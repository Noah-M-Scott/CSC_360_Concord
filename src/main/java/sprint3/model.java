package sprint3;

import java.rmi.RemoteException;

import ConcordData.ChatListing;
import ConcordData.GroupData;
import ConcordData.GroupDataRepo;
import ConcordData.MsgData;
import ConcordData.Pair;
import ConcordData.Role;
import ConcordData.UserData;
import concordSprint2.ClientObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;

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
		admin.Perms.add(new Pair<String, Boolean>("make chat", true));
		admin.Perms.add(new Pair<String, Boolean>("make role", true));
		admin.Perms.add(new Pair<String, Boolean>("edit role", true));
		admin.Perms.add(new Pair<String, Boolean>("make msg", true));
		admin.Perms.add(new Pair<String, Boolean>("invite user", true));
		admin.Perms.add(new Pair<String, Boolean>("delete chat", true));
		admin.Perms.add(new Pair<String, Boolean>("delete role", true));
		admin.Perms.add(new Pair<String, Boolean>("delete msg", true));
		admin.Perms.add(new Pair<String, Boolean>("delete group", true));
		admin.Perms.add(new Pair<String, Boolean>("rename chat", true));
		admin.Perms.add(new Pair<String, Boolean>("rename group", true));
		//admin.Perms.add(new Pair<String, Boolean>("kick user", true));
		newGroup.Roles.add(admin);
		newGroup.findGroupUserById(currentUser.UserId).Roles.add(admin);
		
		Client.makeGroup(currentUser.UserId, newGroup);
		currentUser.JoinedGroupIds = Client.CurrentUser.JoinedGroupIds;
	}
	
	//done
	public void makeChn() throws RemoteException {
		ChatListing newChat = new ChatListing();
		
		Client.makeChatListing(Client.CurrentUser.UserId, currentGroup.GroupId, newChat);
		
		currentGroup = Client.CurrentGroup;
	}
	
	//done
	public void renameChn(String in) throws RemoteException {
		Client.renameChat(currentUser.UserId, currentGroup.GroupId, currentChannel.ChatId, in);
	}
	
	//done
	public void renameGrp(String in) throws RemoteException {
		Client.renameGroup(currentUser.UserId, currentGroup.GroupId, in);
	}

	//done
    void deleteGrp() throws RemoteException {
    	
    	for(int i = 0; i < Client.CurrentUser.JoinedGroupIds.size(); i++)
    		if(Client.CurrentUser.JoinedGroupIds.get(i) == currentGroup.GroupId)
    			Client.CurrentUser.JoinedGroupIds.remove(i);
    	
    	Client.updateUserData(currentUser.UserId);
    	Client.deleteGroup(currentUser.UserId, currentGroup.GroupId);
    	selectGroup(0);
    	selectChannel(0);
    }

    //done
    void deleteChn() throws RemoteException {	
    	Client.deleteChatListing(currentUser.UserId, currentGroup.GroupId, currentChannel.ChatId);
    	selectChannel(0);
    }
    
    
    boolean inRoles = false;
    //done
    void deleteMsg(int inIndex) throws RemoteException {
    	if(inRoles == true)
    		return;
    	
    	Client.deleteMsg(currentUser.UserId, currentGroup.GroupId, currentChannel.ChatId, currentChannel.Chat.size() - 1 - (msgIndex + inIndex));
    }
    
    //done
  	public void makeMsg(String message) throws RemoteException {
  		if(inRoles == true) {
  			
  			Role temp = new Role();
  			temp.Name = "";
  			
  			if(message.charAt(0) == '>' || message.charAt(0) == '<') {
  				String username = new String();
  				String rolename = new String();
  				
  				int pivot = 1;
  	  			while(message.charAt(pivot) != ':') {
  	  				username += message.charAt(pivot);
  	  				pivot++;
  	  			}
  				
  	  			pivot++;
  	  			while(pivot < message.length()) {
	  				rolename += message.charAt(pivot);
	  				pivot++;
	  			}
  	  			
  	  			long targetuserid = 0;
  				
  	  			for(int i = 0; i < currentGroup.Users.size(); i++)
	  	  			if(currentGroup.Users.get(i).Nickname.equals(username))
	  	  				targetuserid = currentGroup.Users.get(i).UserId;
  	  			
  	  			if(message.charAt(0) == '>')
  	  				Client.giveTakeRole(currentUser.UserId, currentGroup.GroupId, targetuserid, rolename, true);
  	  			else
  	  				Client.giveTakeRole(currentUser.UserId, currentGroup.GroupId, targetuserid, rolename, false);
  	  			
  	  			roleView();
    		
  	  			return;
  			}
  			
  			
  			String[] a = {"make chat", "make role", "edit role", "make msg", "invite user", 
  					"delete chat", "delete role", "kick user", "delete msg", "delete group",
  					 "rename chat", "rename group"};
  			
  			String[] b = {"c", "r", "g", "m", "u", "d", "p", "k", "l", "p", "w", "j"};
  			
  			int pivot = 0;
  			while(message.charAt(pivot) != ':') {
  				temp.Name += message.charAt(pivot);
  				pivot++;
  			}
  			
  			for(int q = pivot; q < message.length(); q++)
	  			for(int i = 0; i < a.length; i++)
	  				if(b[i].equals(Character.toString(message.charAt(q))))
	  					temp.Perms.add(new Pair<String, Boolean>(a[i], true));
  			
  			
  			Client.makeRole(currentUser.UserId, currentGroup.GroupId, temp);
  			
  			
    		roleView();
    		
  			return;
  		}
  		
  		
  		MsgData newMsg = new MsgData();
  		newMsg.deleted = false;
  		newMsg.Text = currentUser.DisplayName + ": " + message;
  		Client.sendMsg(currentUser.UserId, currentGroup.GroupId, currentChannel.ChatId, newMsg);
  	}
	
    
    void roleView() throws RemoteException {
    	inRoles = true;
    	currentChannel = new ChatListing();
    	
    	MsgData temp = new MsgData();
    	temp.Text = "How to make a role:";
    	currentChannel.addMsg(temp);
    	
    	{
	    	temp = new MsgData();
	    	temp.Text = "Enter \"Name:Flags\"";
	    	currentChannel.addMsg(temp);
	    	
	    	temp = new MsgData();
	    	temp.Text = "Ex. user:mu";
	    	currentChannel.addMsg(temp);
	    	
	    	temp = new MsgData();
	    	temp.Text = "List of Flags:";
	    	currentChannel.addMsg(temp);
	    	
	    	temp = new MsgData();
	    	temp.Text = "make chat : c";
	    	currentChannel.addMsg(temp);
	    	
	    	temp = new MsgData();
			temp.Text = "make role : r";
			currentChannel.addMsg(temp);
			
			temp = new MsgData();
			temp.Text = "edit role : g";
			currentChannel.addMsg(temp);
			
			temp = new MsgData();
			temp.Text = "make msg : m";
			currentChannel.addMsg(temp);
			
			temp = new MsgData();
			temp.Text = "invite user : u";
			currentChannel.addMsg(temp);
			
			temp = new MsgData();
			temp.Text = "delete chat : d";
			currentChannel.addMsg(temp);
			
			temp = new MsgData();
			temp.Text = "delete role : p";
			currentChannel.addMsg(temp);
			
			//temp = new MsgData();
			//temp.Text = "kick user : k";
			//currentChannel.addMsg(temp);
			
			temp = new MsgData();
			temp.Text = "delete msg : l";
			currentChannel.addMsg(temp);
			
			temp = new MsgData();
			temp.Text = "delete group : q";
			currentChannel.addMsg(temp);
	    	
			temp = new MsgData();
			temp.Text = "rename chat : w";
			currentChannel.addMsg(temp);
			
			temp = new MsgData();
			temp.Text = "rename group : j";
			currentChannel.addMsg(temp);
			
			temp = new MsgData();
	    	temp.Text = "How to give a role:";
	    	currentChannel.addMsg(temp);
	    	
	    	temp = new MsgData();
	    	temp.Text = "Type: \">username:rolename\"";
	    	currentChannel.addMsg(temp);
	    	
	    	temp = new MsgData();
	    	temp.Text = "How to take a role:";
	    	currentChannel.addMsg(temp);
	    	
	    	temp = new MsgData();
	    	temp.Text = "Type: \"<username:rolename\"";
	    	currentChannel.addMsg(temp);
    	}
    	
    	for(int i = 0; i < Client.CurrentGroup.Roles.size(); i++) {
    			temp = new MsgData();
    			temp.Text = Client.CurrentGroup.Roles.get(i).Name + " { ";
    			
    			for(int j = 0; j < Client.CurrentGroup.Roles.get(i).Perms.size(); j++) {
    				temp.Text += Client.CurrentGroup.Roles.get(i).Perms.get(j).getKey() + ":";
    				temp.Text += Client.CurrentGroup.Roles.get(i).Perms.get(j).getValue().toString() + ", ";
    				
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
    			for(int j = 0; j < Client.CurrentGroup.Users.size(); j++) 
    				for(int k = 0; k < Client.CurrentGroup.Users.get(j).Roles.size(); k++)
    					if(Client.CurrentGroup.Users.get(j).Roles.get(k).Name.equals(Client.CurrentGroup.Roles.get(i).Name)) {
	    					temp.Text += Client.CurrentGroup.Users.get(j).Nickname + " has ";
	    					temp.Text += Client.CurrentGroup.Roles.get(i).Name;
	    	    			currentChannel.addMsg(temp);
	    	    			temp = new MsgData();
	    	    			temp.Text = "";
	    				}
    	}
    }
    
    public String qUserId() {
    	return Long.toString(currentUser.UserId);
    }
    
    public void deleteUser(String password) throws RemoteException {
    	
    	Client.deleteUser(currentUser.UserId, password);
    	
    }
    
    public void addUser(long userId) throws RemoteException {
    	
    	
    	if(!Client.inviteUser(currentUser.UserId, currentGroup.GroupId, userId).equals("ok"))
    		return;
    	
    	selectChannel(0);
    	MsgData newMsg = new MsgData();
  		newMsg.deleted = false;
  		newMsg.Text = userId + " has joined the group";
  		Client.sendMsg(currentUser.UserId, currentGroup.GroupId, currentChannel.ChatId, newMsg);
    }
    
    
    
	//-------------------------------------------------------------------
	
	
    //change the current chat/channel to the one clicked, array position equal to chnIndex + offset
	public void selectChannel(int offset) {
		inRoles = false;
		if(offset >= currentGroup.Chats.size()) {
			currentChannel = new ChatListing();
			return;
		}
		
		currentChannel = currentGroup.Chats.get(chnIndex + offset);
		msgIndex = 0;
	}
	
	//change the current group to the one clicked, array position equal to grpIndex + offset
	public void selectGroup(int offset) {
		inRoles = false;
		if(offset >= currentUser.JoinedGroupIds.size()) {
			currentGroup = new GroupData();
			return;
		}
		
		Client.getGroupData(currentUser.UserId, currentUser.JoinedGroupIds.get(grpIndex + offset));
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
			
			if(((msgIndex + hold)) >= currentChannel.Chat.size()) //non-existant message
				return "";
			
			
			//delete message text, see if you can get this to work, I gave up and rewrote the message to say deleted instead
			//else if(currentChannel.Chat.get(currentChannel.Chat.size() - 1 - (msgIndex + hold)).deleted == true) 
			//	return "deleted";
			
			return currentChannel.Chat.get(currentChannel.Chat.size() - 1 - (msgIndex + hold)).Text;
		}
		
		if((msgIndex + msgstep) >= currentChannel.Chat.size()) {
			msgstep++;
			return "";
		}
		
		//delete message text, see if you can get this to work, I gave up and rewrote the message to say deleted instead
		//else if(currentChannel.Chat.get(currentChannel.Chat.size() - 1 - (msgIndex + msgstep)).deleted == true)
		//	return "deleted";
		
		return currentChannel.Chat.get(currentChannel.Chat.size() - 1 - (msgIndex + msgstep++)).Text;
	}
	
	//see above
	int chnstep = 0;
	public String getChnWindow() {
		if(chnstep == chnWindow - 1) {
			int hold = chnstep;
			chnstep = 0;
			
			if(chnIndex + hold >= currentGroup.Chats.size())
				return "";
			
			return currentGroup.Chats.get(chnIndex + hold).ChatName;
		}
		
		if(chnIndex + chnstep >= currentGroup.Chats.size()) {
			chnstep++;
			return "";
		}
		
		return currentGroup.Chats.get(chnIndex + chnstep++).ChatName;
	}
	
	//see above
	int grpstep = 0;
	public String getGrpWindow() {
		if(grpstep == grpWindow - 1) {
			int hold = grpstep;
			grpstep = 0;
			if(grpIndex + hold >= currentUser.JoinedGroupIds.size())
				return "";
			
			userGroupIndex = grpIndex + hold;
			
			
			GroupData holdGroup = Client.CurrentGroup;
			
			Client.getGroupData(currentUser.UserId, currentUser.JoinedGroupIds.get(grpIndex + hold));
			
			String name = Client.CurrentGroup.Name;
			
			Client.CurrentGroup = holdGroup;
			
			return name;
		}
		
		if(grpIndex + grpstep >= currentUser.JoinedGroupIds.size()) {
			grpstep++;
			return "";
		}
		
		if(Client.CurrentGroup == null) {
			return "";
		}
		
		userGroupIndex = grpIndex + grpstep;
		
		GroupData holdGroup = Client.CurrentGroup;
		
		Client.getGroupData(currentUser.UserId, currentUser.JoinedGroupIds.get(grpIndex + grpstep++));
		
		String name = Client.CurrentGroup.Name;
		
		Client.CurrentGroup = holdGroup;		
		return name;
	}
	
	//increment the index for the message window, saturating
	public void incMsgWindow() {
		msgIndex = msgIndex < (currentChannel.Chat.size() - msgWindow) ? msgIndex + 1 : msgIndex;
	}
	
	//decrement the index for the message window, saturating
	public void decMsgWindow() {
		msgIndex -= msgIndex > 0 ? 1 : 0;
	}
	
	//see above, for bellow
	public void incChnWindow() {
		chnIndex = chnIndex < (currentGroup.Chats.size() - chnWindow) ? chnIndex + 1 : chnIndex;
	}
	
	public void decChnWindow() {
		chnIndex -= chnIndex > 0 ? 1 : 0;
	}
	
	public void incGrpWindow() {
		grpIndex = grpIndex < (currentUser.JoinedGroupIds.size() - grpWindow) ? grpIndex + 1 : grpIndex;
	}
	
	public void decGrpWindow() {
		grpIndex -= grpIndex > 0 ? 1 : 0;
	}
	
}
