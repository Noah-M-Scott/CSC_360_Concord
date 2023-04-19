package sprint3;

import ConcordData.ChatListing;
import ConcordData.GroupData;
import ConcordData.GroupDataRepo;
import ConcordData.MsgData;
import ConcordData.UserData;
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
	GroupDataRepo currentGroupDataRepo = new GroupDataRepo();
	
	
	//-----------------the stuff in this box is all changed to use the server-------------------------
	
	public void init() {
		currentUser.DisplayName = "demoUser";
		//here you init all the current data
	}
	
	public void makeGroup() {
		GroupData newGroup = new GroupData();
		currentGroupDataRepo.addGroup(newGroup);
		currentUser.JoinedGroupIds.add(newGroup.GroupId);
	}
	
	public void makeChn() {
		ChatListing newChat = new ChatListing();
		currentGroup.addChat(newChat);
	}
	
	public void renameChn(String in) {
		currentChannel.ChatName = in;
	}
	
	//change to sprint one, groupData now has a public "Name" string
	public void renameGrp(String in) {
		currentGroup.Name = in;
	}
	
	public void makeMsg(String message) {
		MsgData newMsg = new MsgData();
		newMsg.deleted = false;
		newMsg.Text = currentUser.DisplayName + ": " + message;
		currentChannel.addMsg(newMsg);
	}

    void deleteGrp() {
    	currentGroupDataRepo.deleteGroup(currentGroup.GroupId);
    	currentUser.JoinedGroupIds.remove(userGroupIndex);
    	selectGroup(0);
    }

    void deleteChn() {
    	currentGroup.deleteChat(currentChannel.ChatId);
    	selectChannel(0);
    }
    
    void deleteMsg(int inIndex) {
    	currentChannel.Chat.get(currentChannel.Chat.size() - 1 - (msgIndex + inIndex)).Text = "deleted";
    	currentChannel.Chat.get(currentChannel.Chat.size() - 1 - (msgIndex + inIndex)).deleted = true;
    }
    
	
	//-------------------------------------------------------------------
	
	
    //change the current chat/channel to the one clicked, array position equal to chnIndex + offset
	public void selectChannel(int offset) {
		if(offset >= currentGroup.Chats.size()) {
			currentChannel = new ChatListing();
			return;
		}
		
		currentChannel = currentGroup.Chats.get(grpIndex + offset);
		msgIndex = 0;
	}
	
	//change the current group to the one clicked, array position equal to grpIndex + offset
	public void selectGroup(int offset) {
		if(offset >= currentUser.JoinedGroupIds.size())
			return;
		
		currentGroup = currentGroupDataRepo.Groups.get(currentUser.JoinedGroupIds.get(grpIndex + offset));
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
			return currentGroupDataRepo.Groups.get(currentUser.JoinedGroupIds.get(grpIndex + hold)).Name;
		}
		
		if(grpIndex + grpstep >= currentUser.JoinedGroupIds.size()) {
			grpstep++;
			return "";
		}
		
		userGroupIndex = grpIndex + grpstep;
		return currentGroupDataRepo.Groups.get(currentUser.JoinedGroupIds.get(grpIndex + grpstep++)).Name;
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
