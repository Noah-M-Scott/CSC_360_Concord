package concordSprint2;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import ConcordData.*;

public class ServerObject extends UnicastRemoteObject implements serverInterface {

	private static final long serialVersionUID = -2;
	
	GroupDataRepo GroupDataRepository = new GroupDataRepo();
	HashMap<Long, ClientInterface> RMIClientList = new HashMap<Long, ClientInterface>();
	UserDataRepo currentUserRepository = new UserDataRepo();
	
	
	public ServerObject() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
		
	}

	public void saveToDisk(String storageFileName) {
		XMLEncoder encoder=null;
		try{
		encoder=new XMLEncoder(new BufferedOutputStream(new FileOutputStream(storageFileName)));
		}catch(FileNotFoundException fileNotFound){
			System.out.println("ERROR: While Creating or Opening File");
		}
		encoder.writeObject(GroupDataRepository);
		encoder.writeObject(currentUserRepository);
		encoder.close();
	}
	
	
	public void loadFromDisk(String storageFileName) {
		XMLDecoder decoder=null;
		try {
			decoder=new XMLDecoder(new BufferedInputStream(new FileInputStream(storageFileName)));
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File not found");
		}
		GroupDataRepository = (GroupDataRepo)decoder.readObject();
		currentUserRepository = (UserDataRepo)decoder.readObject();
	}
	
	@Override
	public UserData updateUserData(long UserId, UserData In) {
		currentUserRepository.Users.replace(UserId, In);
		return In;
	}
	
	@Override
	public UserData login(String Name, String Password, ClientInterface client) throws RemoteException {
		UserData logClient = currentUserRepository.findUserByName(Name);
		if (logClient == null)
			return null;
		if (logClient.Password.equals(Password)) {
			RMIClientList.put(logClient.UserId, client);
			return logClient;
		}else
			return null;
	}

	@Override
	public GroupData getGroupData(long UserId, long GroupID) throws RemoteException {
		return checkPerms(UserId, GroupID, null) ? GroupDataRepository.findGroupById(GroupID) : null;
	}

	@Override
	public GroupData sendMsg(long UserId, long GroupId, long ChatId, MsgData message) throws RemoteException {
		ChatListing chat;
		if( checkPerms(UserId, GroupId, "make msg") == true ) {
			chat = GroupDataRepository.findGroupById(GroupId).findChatById(ChatId);
			chat.addMsg(message);
			return GroupDataRepository.findGroupById(GroupId);
		} else
			return null;
	}

	@Override
	public GroupData renameGroup(long UserId, long GroupId, String Name) throws RemoteException {
		GroupData group;
		if( checkPerms(UserId, GroupId, "rename group") == true ) {
			group = GroupDataRepository.findGroupById(GroupId);
			group.Name = Name;
			return group;
		} else
			return null;
	}

	@Override
	public GroupData renameChat(long UserId, long GroupId, long ChatId, String Name) throws RemoteException {
		ChatListing chat;
		if( checkPerms(UserId, GroupId, "rename chat") == true ) {
			chat = GroupDataRepository.findGroupById(GroupId).findChatById(ChatId);
			chat.ChatName = Name;
			return GroupDataRepository.findGroupById(GroupId);
		} else
			return null;
	}
	
	
	
	@Override
	public GroupData makeGroup(long UserId, GroupData newGroup) throws RemoteException {
		if(newGroup == null)
			return null;
		
		GroupDataRepository.addGroup(newGroup);
		return newGroup;
	}

	@Override
	public GroupData makeRole(long UserId, long GroupId, Role newRole) throws RemoteException {
		GroupData group = GroupDataRepository.findGroupById(GroupId);
		if(checkPerms(UserId, GroupId, "make role")) {
			group.Roles.add(newRole);
			return group;
		}else
			return null;
	}

	@Override
	public GroupData makeChatListing(long UserId, long GroupId, ChatListing newChat) throws RemoteException {
		if( checkPerms(UserId, GroupId, "make chat") == true ) {
			GroupDataRepository.findGroupById(GroupId).addChat(newChat);
			return GroupDataRepository.findGroupById(GroupId);
		} else
			return null;
	}

	@Override
	public GroupData inviteUser(long UserId, long GroupId, long InvitedUserId) throws RemoteException {
		if( checkPerms(UserId, GroupId, "invite user") == true ) {
			UserData newUser = currentUserRepository.findUserById(InvitedUserId);
			if(newUser == null)
				return null;
			
			GroupDataRepository.findGroupById(GroupId).addUser(newUser);
			newUser.JoinedGroupIds.add(GroupId);
			return GroupDataRepository.findGroupById(GroupId);
		} else
			return null;
	}

	@Override
	public GroupData deleteMsg(long UserId, long GroupId, long ChatId, long MsgId) throws RemoteException {
		if( checkPerms(UserId, GroupId, "delete msg") == true ) {
			GroupDataRepository.findGroupById(GroupId).findChatById(ChatId).deleteMsg(MsgId);
			return GroupDataRepository.findGroupById(GroupId);
		} else
			return null;
	}

	@Override
	public GroupData deleteChatListing(long UserId, long GroupId, long ChatId) throws RemoteException {
		if( checkPerms(UserId, GroupId, "delete chat") == true ) {
			GroupDataRepository.findGroupById(GroupId).deleteChat(ChatId);
			return GroupDataRepository.findGroupById(GroupId);
		} else
			return null;
	}

	@Override
	public boolean deleteGroup(long UserId, long GroupId) throws RemoteException {
		if( checkPerms(UserId, GroupId, "delete group") == true ) {
			GroupDataRepository.deleteGroup(GroupId);
			return true;
		} else
			return false;
	}

	@Override
	public GroupData deleteRole(long UserId, long GroupId, String RoleName) throws RemoteException {
		if( checkPerms(UserId, GroupId, "delete role") == true ) {
			for(int i = 0; i < GroupDataRepository.findGroupById(GroupId).Roles.size(); i++) {
				if(GroupDataRepository.findGroupById(GroupId).Roles.get(i).Name.equals(RoleName)) {
					GroupDataRepository.findGroupById(GroupId).Roles.remove(i);
					return GroupDataRepository.findGroupById(GroupId);
				}
			}
			return null;
		} else
			return null;
	}

	@Override
	public boolean deleteUser(long UserId, String Password) throws RemoteException {
		UserData logClient = currentUserRepository.findUserById(UserId);
		if (logClient.Password.equals(Password)) {
			currentUserRepository.deleteUser(UserId);
			return true;
		}else
			return false;
	}

	@Override
	public UserData addUser(UserData newUser) throws RemoteException {
		if(newUser == null)
			return null;
		
		if(currentUserRepository.Names.containsKey(newUser.DisplayName) != false)
			return null;
		
		currentUserRepository.addUser(newUser);
		return currentUserRepository.findUserByName(newUser.DisplayName);
	}

	@Override
	public GroupData giveTakeRole(long UserId, long GroupId, long TargetUser, String RoleName, boolean giveOrTake){
		if( checkPerms(UserId, GroupId, "edit role") == true ) {
			GroupData group = GroupDataRepository.findGroupById(GroupId);
			if(giveOrTake == false)
				for(int i = 0; i < group.findGroupUserById(TargetUser).Roles.size(); i++) {
					if(group.findGroupUserById(TargetUser).Roles.get(i).Name.equals(RoleName)) {
						group.findGroupUserById(TargetUser).Roles.remove(i);
						return GroupDataRepository.findGroupById(GroupId);
					}
				}
			else
				for(int i = 0; i < group.Roles.size(); i++) {
					if(group.Roles.get(i).Name.equals(RoleName)) {
						group.findGroupUserById(TargetUser).Roles.add(group.Roles.get(i));
						return GroupDataRepository.findGroupById(GroupId);
					}
				}
				
				
			return null;
		} else
			return null;
	}
	
	private boolean checkPerms(long UserId, long GroupId, String PermName){
		GroupData group = GroupDataRepository.findGroupById(GroupId);
		if(group == null)
			return false;
		
		GroupUserData user = group.findGroupUserById(UserId);
		if (user != null)
			if(PermName != null)
				if(user.checkPerm(PermName))
					return true;
				else
					return false;
			else
				return true;
		else
			return false;
	}
	
	public void sendOutUpdate(long GroupId) {
		for(long key: RMIClientList.keySet()){
			try {
				RMIClientList.get(key).processUpdate(GroupDataRepository.findGroupById(GroupId));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		}
	}
	
	public void alertStatus(long UserId, long Status) {
		if (Status == 0)
			RMIClientList.remove(UserId);
	}
	
	public static void main(String[] args){
		try {
			ServerObject M = new ServerObject();
			Naming.rebind("concord-s", M);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
