package concordSprint2;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import ConcordData.ChatListing;
import ConcordData.GroupData;
import ConcordData.MsgData;
import ConcordData.Role;
import ConcordData.UserData;

public class ClientObject implements ClientInterface, Serializable {

	private static final long serialVersionUID = 4343555364564564556L;
	
	public GroupData CurrentGroup;
	public UserData CurrentUser;
	public serverInterface ServerProxy;
	Registry registry;
	
	public static final String[] AvailibleCheckNames = {"AutoExpand", "AutoCensor"};
	
	public ClientObject(Registry in){
		registry = in;
		try {
			ServerProxy = (serverInterface)registry.lookup("concord-s");
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}  
	}
	
	@Override
	public void processUpdate(GroupData Update) throws RemoteException {
		CurrentGroup = Update;
	}

	public String login(String Name, String Password) throws RemoteException{
		try {
			CurrentUser = ServerProxy.login(Name, Password, this);
			if(CurrentUser == null)	
				return "Failed to Login";
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return "ok";
	}
	
	public String addACheck(long UserId, long GroupId, String CheckName) throws RemoteException {
		try {
			GroupData temp = ServerProxy.addACheck(UserId, GroupId, CheckName);
			if(temp == null)
				return "Failed to add check"; 
			else
				CurrentGroup = temp;
		} catch (RemoteException e) {                                    
			e.printStackTrace();                                         
		}
		return "ok";
	}
	
	public String takeACheck(long UserId, long GroupId, String CheckName) throws RemoteException {
		try {
			GroupData temp = ServerProxy.takeACheck(UserId, GroupId, CheckName);
			if(temp == null)
				return "Failed to take check";  
			else
				CurrentGroup = temp;
		} catch (RemoteException e) {                                    
			e.printStackTrace();                                         
		}
		return "ok";
	}
	
	
	public String getGroupData(long UserId, long GroupID){
		try {             
			GroupData temp = ServerProxy.getGroupData(UserId, GroupID);
			if(temp == null)
				return "Failed to get group";                   
			else
				CurrentGroup = temp;
		} catch (RemoteException e) {                                    
			e.printStackTrace();                                         
		}
		return "ok";
	}
	
	public String sendMsg(long UserId, long GroupId, long ChatId, MsgData message) throws RemoteException {
		try {             
			GroupData temp = ServerProxy.sendMsg(UserId, GroupId, ChatId, message);
			if(temp == null)
				return "Failed to send msg";                   
			else
				CurrentGroup = temp;
		} catch (RemoteException e) {                                    
			e.printStackTrace();                                         
		}
		return "ok";
	}
	
	public String updateUserData(long UserId) {
		try {
			UserData temp = ServerProxy.updateUserData(UserId, CurrentUser);
			if(temp == null)
				return "Failed to make group";  
			else
				CurrentUser = temp;
		} catch (RemoteException e) {                                    
			e.printStackTrace();                                         
		}
		return "ok";
	}
	
	public String renameGroup(long UserId, long GroupId, String Name) throws RemoteException {
		try {
			GroupData temp = ServerProxy.renameGroup(UserId, GroupId, Name);
			if(temp == null)
				return "Failed to make group";  
			else
				CurrentGroup = temp;
		} catch (RemoteException e) {                                    
			e.printStackTrace();                                         
		}
		return "ok";
	}
	
	public String renameChat(long UserId, long GroupId, long ChatId, String Name) throws RemoteException {
		try {
			GroupData temp = ServerProxy.renameChat(UserId, GroupId, ChatId, Name);
			if(temp == null)
				return "Failed to make group";  
			else
				CurrentGroup = temp;
		} catch (RemoteException e) {                                    
			e.printStackTrace();                                         
		}
		return "ok";
	}
	
	public String makeGroup(long UserId, GroupData newGroup) throws RemoteException {
		try {             
			GroupData temp = ServerProxy.makeGroup(UserId, newGroup);
			if(temp == null)
				return "Failed to make group";  
			else
				CurrentGroup = temp;
			
			CurrentUser.getJoinedGroupIds().add(newGroup.getGroupId());
			ServerProxy.updateUserData(UserId, CurrentUser);
		} catch (RemoteException e) {                                    
			e.printStackTrace();                                         
		}
		return "ok";
	}         
	
	public String makeRole(long UserId, long GroupId, Role newRole) throws RemoteException {
		try {             
			GroupData temp = ServerProxy.makeRole(UserId, GroupId, newRole);
			if(temp == null)
				return "Failed to make role";  
			else
				CurrentGroup = temp;
		} catch (RemoteException e) {                                    
			e.printStackTrace();                                         
		}
		return "ok";
	}
	
	public String makeChatListing(long UserId, long GroupId, ChatListing newChat) throws RemoteException {
		try {             
			GroupData temp = ServerProxy.makeChatListing(UserId, GroupId, newChat);
			if(temp == null)
				return "Failed to make chat";  
			else
				CurrentGroup = temp;
		} catch (RemoteException e) {                                    
			e.printStackTrace();                                         
		}
		return "ok";
	}    
	
	public String inviteUser(long UserId, long GroupId, long InvitedUserId) throws RemoteException {
		try {             
			GroupData temp = ServerProxy.inviteUser(UserId, GroupId, InvitedUserId);
			if(temp == null)
				return "Failed to invite user";  
			else
				CurrentGroup = temp;
		} catch (RemoteException e) {                                    
			e.printStackTrace();                                         
		}
		return "ok";
	}                      
	
	public String deleteMsg(long UserId, long GroupId, long ChatId, long MsgId) throws RemoteException {
		try {             
			GroupData temp = ServerProxy.deleteMsg(UserId, GroupId, ChatId, MsgId);
			if(temp == null)
				return "Failed to delete msg";  
			else
				CurrentGroup = temp;
		} catch (RemoteException e) {                                    
			e.printStackTrace();                                         
		}
		return "ok";
	}         
	
	public String deleteChatListing(long UserId, long GroupId, long ChatId) throws RemoteException {
		try {             
			GroupData temp = ServerProxy.deleteChatListing(UserId, GroupId, ChatId);
			if(temp == null)
				return "Failed to delete chat";  
			else
				CurrentGroup = temp;
		} catch (RemoteException e) {                                    
			e.printStackTrace();                                         
		}
		return "ok";
	}      
	
	public String deleteGroup(long UserId, long GroupId) throws RemoteException {
		try {             
			if(ServerProxy.deleteGroup(UserId, GroupId))
				CurrentGroup = null;
			else
				return "Failed to delete group";  
		} catch (RemoteException e) {                                    
			e.printStackTrace();                                         
		}
		return "ok";
	}       
	
	public String deleteRole(long UserId, long GroupId, String RoleName) throws RemoteException {
		try {             
			GroupData temp = ServerProxy.deleteRole(UserId, GroupId, RoleName);
			if(temp == null)
				return "Failed to delete role";  
			else
				CurrentGroup = temp;
		} catch (RemoteException e) {                                    
			e.printStackTrace();                                         
		}
		return "ok";
	}    
	
	public String deleteUser(long UserId, String Password) throws RemoteException {
		try {             
			if(ServerProxy.deleteUser(UserId, Password))
				CurrentUser = null;
			else
				return "Failed to delete user";  
		} catch (RemoteException e) {                                    
			e.printStackTrace();                                         
		}
		return "ok";
	}      
	
	public String addUser(UserData newUser) throws RemoteException {
		try {             
			UserData temp = ServerProxy.addUser(newUser);
			if(temp == null)
				return "Failed to add user";  
			else
				CurrentUser = temp;
		} catch (RemoteException e) {                                    
			e.printStackTrace();                                         
		}
		return "ok";
	}       
	
	public String giveTakeRole(long UserId, long GroupId, long TargetUser, String RoleName, boolean giveOrTake) throws RemoteException {
		try {             
			GroupData temp;
			if(!giveOrTake)
				//take a role
				temp = ServerProxy.takeRole(UserId, GroupId, TargetUser, RoleName);
			else
				//give a role
				temp = ServerProxy.giveRole(UserId, GroupId, TargetUser, RoleName);
			
			if(temp == null)
				return "Failed to give/take role";
			else
				CurrentGroup = temp;
		} catch (RemoteException e) {                                    
			e.printStackTrace();                                         
		}
		return "ok";
	} 	
	
	public String alertStatus(long UserId, long Status) {
		try {             
			ServerProxy.alertStatus(UserId, Status);
		} catch (RemoteException e) {                                    
			e.printStackTrace();                                         
		}
		return "ok";
	}
	
}


