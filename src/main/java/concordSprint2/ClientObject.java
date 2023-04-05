package concordSprint2;

import java.io.Serializable;
import java.rmi.AccessException;
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
	
	public ClientObject(Registry in){
		registry = in;
		try {
			ServerProxy = (serverInterface)registry.lookup("concord-s");
		} catch (AccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block                           
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
			// TODO Auto-generated catch block                           
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
		} catch (RemoteException e) {                                    
			// TODO Auto-generated catch block                           
			e.printStackTrace();                                         
		}
		return "ok";
	}         
	
	public String makeRole(long UserId, long GroupId, Role newRole) throws RemoteException {
		try {             
			GroupData temp = ServerProxy.makeRole(UserId, GroupId, newRole);
			if(temp == null)
				return "Failed to make group";  
			else
				CurrentGroup = temp;
		} catch (RemoteException e) {                                    
			// TODO Auto-generated catch block                           
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
			// TODO Auto-generated catch block                           
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
			// TODO Auto-generated catch block                           
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
			// TODO Auto-generated catch block                           
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
			// TODO Auto-generated catch block                           
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
			// TODO Auto-generated catch block                           
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
			// TODO Auto-generated catch block                           
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
			// TODO Auto-generated catch block                           
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
			// TODO Auto-generated catch block                           
			e.printStackTrace();                                         
		}
		return "ok";
	}       
	
	public String giveTakeRole(long UserId, long GroupId, long TargetUser, String RoleName, boolean giveOrTake) throws RemoteException {
		try {             
			GroupData temp = ServerProxy.giveTakeRole(UserId, GroupId, TargetUser, RoleName, giveOrTake);
			if(temp == null)
				return "Failed to give/take role";
			else
				CurrentGroup = temp;
		} catch (RemoteException e) {                                    
			// TODO Auto-generated catch block                           
			e.printStackTrace();                                         
		}
		return "ok";
	} 	
	
	public String alertStatus(long UserId, long Status) {
		try {             
			ServerProxy.alertStatus(UserId, Status);
		} catch (RemoteException e) {                                    
			// TODO Auto-generated catch block                           
			e.printStackTrace();                                         
		}
		return "ok";
	}
	
}


