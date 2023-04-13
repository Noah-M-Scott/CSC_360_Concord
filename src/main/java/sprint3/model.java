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

	int msgIndex = 0;
	int msgWindow;
	ChatListing currentChannel = new ChatListing();
	
	int chnIndex = 0;
	int chnWindow;
	GroupData currentGroup = new GroupData();
	
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
	
	public void renameGrp(String in) {
		currentGroup.Name = in;
	}
	
	public void makeMsg(String message) {
		MsgData newMsg = new MsgData();
		newMsg.Text = currentUser.DisplayName + ": " + message;
		currentChannel.addMsg(newMsg);
	}

    void deleteGrp() {
    	currentGroupDataRepo.deleteGroup(currentGroup.GroupId);
    	currentUser.JoinedGroupIds.remove(userGroupIndex);
    }

    void deleteChn() {
    	currentGroup.deleteChat(currentChannel.ChatId);
    }
	
	//-------------------------------------------------------------------
	
	
	
	public void selectChannel(int channelIndex) {
		if(channelIndex >= currentGroup.Chats.size()) {
			currentChannel = new ChatListing();
			return;
		}
		
		currentChannel = currentGroup.Chats.get(grpIndex + channelIndex);
		msgIndex = 0;
	}
	
	public void selectGroup(int groupIndex) {
		if(groupIndex >= currentUser.JoinedGroupIds.size())
			return;
		
		currentGroup = currentGroupDataRepo.Groups.get(currentUser.JoinedGroupIds.get(grpIndex + groupIndex));
		chnIndex = 0;
	}
	
	
	
	public void setMsgWindow(int size) {
		msgWindow = size;
	}
	
	public void setChnWindow(int size) {
		chnWindow = size;
	}
	
	public void setGrpWindow(int size) {
		grpWindow = size;
	}
	
	int msgstep = 0;
	public String getMsgWindow() {
		if(msgstep == msgWindow - 1) {
			int hold = msgstep;
			msgstep = 0;
			
			if(msgIndex + hold >= currentChannel.Chat.size())
				return "";
			
			//if(currentChannel.Chat.get(currentChannel.Chat.size() - 1 - (msgIndex + hold)).deleted)
			//	return "deleted";
			
			return currentChannel.Chat.get(currentChannel.Chat.size() - 1 - (msgIndex + hold)).Text;
		}
		
		if(msgIndex + msgstep >= currentChannel.Chat.size()) {
			msgstep++;
			return "";
		}
		
		//if(currentChannel.Chat.get(currentChannel.Chat.size() - 1 - (msgIndex + msgstep++)).deleted)
		//	return "deleted";
		
		return currentChannel.Chat.get(currentChannel.Chat.size() - 1 - (msgIndex + msgstep++)).Text;
	}
	
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
	
	public void incMsgWindow() {
		msgIndex = msgIndex < (currentChannel.Chat.size() - msgWindow) ? msgIndex + 1 : msgIndex;
	}
	
	public void decMsgWindow() {
		msgIndex -= msgIndex > 0 ? 1 : 0;
	}
	
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
