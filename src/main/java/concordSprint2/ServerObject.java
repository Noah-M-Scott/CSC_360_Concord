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
import java.util.ArrayList;
import java.util.HashMap;

import ConcordData.*;

public class ServerObject extends UnicastRemoteObject implements serverInterface {

	private static final long serialVersionUID = -2;
	
	GroupDataRepo GroupDataRepository = new GroupDataRepo();
	HashMap<Long, ClientInterface> RMIClientList = new HashMap<>();
	UserDataRepo currentUserRepository = new UserDataRepo();
	String storageFileName;
	ArrayList<TextCheck> AvailibleCheck = new ArrayList<>();
	
	
	public ServerObject(String storageFileName) throws RemoteException {
		super();
		
		String[] badWords = {"funny", "bug", "badword"};
		AvailibleCheck.add(new AutoCensor(badWords));
		
		String[] Abbr = {"lol", "btw"};
		String[] Full = {"laugh out loud", "by the way"};
		AvailibleCheck.add(new AutoExpand(Abbr, Full));
		
		this.storageFileName = storageFileName;
		
	}

	
	//every mutating function calls this
	public void saveToDisk() {
		XMLEncoder encoder=null;
		try{
			encoder=new XMLEncoder(new BufferedOutputStream(new FileOutputStream(storageFileName)));
			encoder.writeObject(GroupDataRepository);
			encoder.writeObject(currentUserRepository);
			encoder.close();
		}catch(FileNotFoundException fileNotFound){
			System.out.println("ERROR: While Creating or Opening File");
		}
	}
	
	
	public void loadFromDisk(String storageFileName) {
		XMLDecoder decoder=null;
		try {
			decoder=new XMLDecoder(new BufferedInputStream(new FileInputStream(storageFileName)));
			GroupDataRepository = (GroupDataRepo)decoder.readObject();
			currentUserRepository = (UserDataRepo)decoder.readObject();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File not found");
		}
	}
	
	@Override
	public UserData updateUserData(long UserId, UserData In) {
		currentUserRepository.getUsers().replace(UserId, In);
		saveToDisk();
		return In;
	}
	
	@Override
	public UserData login(String Name, String Password, ClientInterface client) throws RemoteException {
		UserData logClient = currentUserRepository.findUserByName(Name);
		if (logClient == null)
			return null;
		if (logClient.getPassword().equals(Password)) {
			RMIClientList.put(logClient.getUserId(), client);
			saveToDisk();
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
		if( checkPerms(UserId, GroupId, "make msg") ) {
			chat = GroupDataRepository.findGroupById(GroupId).findChatById(ChatId);
			chat.addMsg(message);
			saveToDisk();
			return GroupDataRepository.findGroupById(GroupId);
		} else
			return null;
	}

	@Override
	public GroupData addACheck(long UserId, long GroupId, String CheckName) throws RemoteException {
		GroupData group;
		if( checkPerms(UserId, GroupId, "edit checks") ) {
			group = GroupDataRepository.findGroupById(GroupId);
			
			for(int i = 0; i < group.getCheck().size(); i++)
				if(group.getCheck().get(i).getName().equals(CheckName))
					return null;
			
			for(int i = 0; i < AvailibleCheck.size(); i++)
				if(AvailibleCheck.get(i).getName().equals(CheckName)) {
					group.getCheck().add(AvailibleCheck.get(i));
					saveToDisk();
					return group;
				}
		}
		
		return null;
	}
	
	@Override
	public GroupData takeACheck(long UserId, long GroupId, String CheckName) throws RemoteException {
		GroupData group;
		if( checkPerms(UserId, GroupId, "edit checks") ) {
			group = GroupDataRepository.findGroupById(GroupId);
			
			for(int i = 0; i < group.getCheck().size(); i++) {
				if(group.getCheck().get(i).getName().equals(CheckName)) {
					group.getCheck().remove(i);
					saveToDisk();
					return group;
				}
			}
		}
		
		return null;
	}
	
	@Override
	public GroupData renameGroup(long UserId, long GroupId, String Name) throws RemoteException {
		GroupData group;
		if( checkPerms(UserId, GroupId, "rename group") ) {
			group = GroupDataRepository.findGroupById(GroupId);
			group.setName(Name);
			saveToDisk();
			return group;
		} else
			return null;
	}

	@Override
	public GroupData renameChat(long UserId, long GroupId, long ChatId, String Name) throws RemoteException {
		ChatListing chat;
		if( checkPerms(UserId, GroupId, "rename chat") ) {
			chat = GroupDataRepository.findGroupById(GroupId).findChatById(ChatId);
			chat.setChatName(Name);
			saveToDisk();
			return GroupDataRepository.findGroupById(GroupId);
		} else
			return null;
	}
	
	
	
	@Override
	public GroupData makeGroup(long UserId, GroupData newGroup) throws RemoteException {
		if(newGroup == null)
			return null;
		
		GroupDataRepository.addGroup(newGroup);
		saveToDisk();
		return newGroup;
	}

	@Override
	public GroupData makeRole(long UserId, long GroupId, Role newRole) throws RemoteException {
		GroupData group = GroupDataRepository.findGroupById(GroupId);
		if(checkPerms(UserId, GroupId, "make role")) {
			group.getRoles().add(newRole);
			saveToDisk();
			return group;
		}else
			return null;
	}

	@Override
	public GroupData makeChatListing(long UserId, long GroupId, ChatListing newChat) throws RemoteException {
		if( checkPerms(UserId, GroupId, "make chat") ) {
			GroupDataRepository.findGroupById(GroupId).addChat(newChat);
			saveToDisk();
			return GroupDataRepository.findGroupById(GroupId);
		} else
			return null;
	}

	@Override
	public GroupData inviteUser(long UserId, long GroupId, long InvitedUserId) throws RemoteException {
		if( checkPerms(UserId, GroupId, "invite user") ) {
			UserData newUser = currentUserRepository.findUserById(InvitedUserId);
			if(newUser == null)
				return null;
			
			GroupDataRepository.findGroupById(GroupId).addUser(newUser);
			newUser.getJoinedGroupIds().add(GroupId);
			saveToDisk();
			return GroupDataRepository.findGroupById(GroupId);
		} else
			return null;
	}

	@Override
	public GroupData deleteMsg(long UserId, long GroupId, long ChatId, long MsgId) throws RemoteException {
		if( checkPerms(UserId, GroupId, "delete msg") ) {
			GroupDataRepository.findGroupById(GroupId).findChatById(ChatId).deleteMsg(MsgId);
			saveToDisk();
			return GroupDataRepository.findGroupById(GroupId);
		} else
			return null;
	}

	@Override
	public GroupData deleteChatListing(long UserId, long GroupId, long ChatId) throws RemoteException {
		if( checkPerms(UserId, GroupId, "delete chat") ) {
			GroupDataRepository.findGroupById(GroupId).deleteChat(ChatId);
			saveToDisk();
			return GroupDataRepository.findGroupById(GroupId);
		} else
			return null;
	}

	@Override
	public boolean deleteGroup(long UserId, long GroupId) throws RemoteException {
		if( checkPerms(UserId, GroupId, "delete group") ) {
			GroupDataRepository.deleteGroup(GroupId);
			saveToDisk();
			return true;
		} else
			return false;
	}

	@Override
	public GroupData deleteRole(long UserId, long GroupId, String RoleName) throws RemoteException {
		if( checkPerms(UserId, GroupId, "delete role") ) {
			for(int i = 0; i < GroupDataRepository.findGroupById(GroupId).getRoles().size(); i++) {
				if(GroupDataRepository.findGroupById(GroupId).getRoles().get(i).Name.equals(RoleName)) {
					GroupDataRepository.findGroupById(GroupId).getRoles().remove(i);
					saveToDisk();
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
		if (logClient.getPassword().equals(Password)) {
			currentUserRepository.deleteUser(UserId);
			saveToDisk();
			return true;
		}else
			return false;
	}

	@Override
	public UserData addUser(UserData newUser) throws RemoteException {
		if(newUser == null)
			return null;
		
		if(currentUserRepository.getNames().containsKey(newUser.getDisplayName()))
			return null;
		
		currentUserRepository.addUser(newUser);
		saveToDisk();
		return currentUserRepository.findUserByName(newUser.getDisplayName());
	}

	@Override
	public GroupData giveRole(long UserId, long GroupId, long TargetUser, String RoleName){
		if( !checkPerms(UserId, GroupId, "edit role") ) {
			return null;
		}
		
		GroupData group = GroupDataRepository.findGroupById(GroupId);

		//Give Role
		for(int i = 0; i < group.getRoles().size(); i++) {
			if(group.getRoles().get(i).Name.equals(RoleName)) {
				group.findGroupUserById(TargetUser).Roles.add(group.getRoles().get(i));
				saveToDisk();
				return GroupDataRepository.findGroupById(GroupId);
			}
		}
		
		return null;
	}
	
	@Override
	public GroupData takeRole(long UserId, long GroupId, long TargetUser, String RoleName){
		if( !checkPerms(UserId, GroupId, "edit role") ) {
			return null;
		}
		
		GroupData group = GroupDataRepository.findGroupById(GroupId);
		//Take away role
		for(int i = 0; i < group.findGroupUserById(TargetUser).Roles.size(); i++) {
			if(group.findGroupUserById(TargetUser).Roles.get(i).Name.equals(RoleName)) {
				group.findGroupUserById(TargetUser).Roles.remove(i);
				saveToDisk();
				return GroupDataRepository.findGroupById(GroupId);
			}
		}

		
		return null;
	}
	
	
	private boolean checkPerms(long UserId, long GroupId, String PermName){
		GroupData group = GroupDataRepository.findGroupById(GroupId);
		if(group == null)
			return false;
		
		GroupUserData user = group.findGroupUserById(UserId);
		if (user != null) {
			if(PermName != null) {
				return user.checkPerm(PermName);
			}else
				return true;
		}else
			return false;
	}
	
	public void sendOutUpdate(long GroupId) {
		for(long key: RMIClientList.keySet()){
			try {
				RMIClientList.get(key).processUpdate(GroupDataRepository.findGroupById(GroupId));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void alertStatus(long UserId, long Status) {
		if (Status == 0) {
			RMIClientList.remove(UserId);
			saveToDisk();
		}
	}
	
	public static void main(String[] args){
		try {
			ServerObject M = new ServerObject("test.xml");
			Naming.rebind("concord-s", M);
		} catch (RemoteException | MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
