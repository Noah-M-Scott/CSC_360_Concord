package concordSprint2;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ConcordData.*;

public class Sprint2Test {

	ServerObject s;
	ClientObject c;
	Registry registry; 
	

	@Test
	public
	void test() throws Exception {
		
		//new server and client
		s = new ServerObject();
		registry = LocateRegistry.createRegistry(2082);
		registry.rebind("concord-s", s);
		c = new ClientObject(registry);
		
		//make a new user
		UserData demoUser = new UserData();
		demoUser.DisplayName = "demo";
		demoUser.Password = "demo";
		assertEquals(c.addUser(null), "Failed to add user");
		assertEquals(c.addUser(demoUser), "ok");
		assertEquals(c.CurrentUser.UserId, 0);
		
		//incorrect password vs correct one
		assertEquals(c.login("demo", "notdemo"), "Failed to Login");
		assertEquals(c.login("demo", "demo"), "ok");
		
		//new group + admin role
		GroupData demoGroup = new GroupData();
		demoGroup.addUser(demoUser);
		demoGroup.Name = "demo";
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
		demoGroup.Roles.add(admin);
		demoGroup.findGroupUserById(c.CurrentUser.UserId).Roles.add(admin);
		assertEquals(c.makeGroup(c.CurrentUser.UserId, demoGroup), "ok");
		
		//cant make a null group
		assertEquals(c.makeGroup(c.CurrentUser.UserId, null), "Failed to make group");
		
		//new chat
		ChatListing demoList = new ChatListing();
		demoList.ChatName = "demoListing";
		assertEquals(c.makeChatListing(c.CurrentUser.UserId, c.CurrentGroup.GroupId, demoList), "ok");
		
		//cant get a groups data if it doesn't exist/your not in it
		assertEquals(c.getGroupData(c.CurrentUser.UserId, 6), "Failed to get group");
		assertEquals(c.getGroupData(c.CurrentUser.UserId, c.CurrentGroup.GroupId), "ok");
		
		//new msg
		MsgData demoMsg = new MsgData();
		demoMsg.deleted = false;
		demoMsg.Text = "demo";
		assertEquals(c.sendMsg(c.CurrentUser.UserId, c.CurrentGroup.GroupId, 0, demoMsg), "ok");
		
		//new role
		Role demoRole = new Role();
		demoRole.Name = "demorole";
		assertEquals(c.makeRole(c.CurrentUser.UserId, c.CurrentGroup.GroupId, demoRole), "ok");
		
		//new user
		UserData demoUser2 = new UserData();
		demoUser2.DisplayName = "demo2";
		demoUser2.Password = "demo2";
		demoUser2 = s.addUser(demoUser2);
		
		//invite the new user, give them the new demo role
		assertEquals(c.inviteUser(c.CurrentUser.UserId, c.CurrentGroup.GroupId, 54545345), "Failed to invite user");
		assertEquals(c.inviteUser(c.CurrentUser.UserId, c.CurrentGroup.GroupId, demoUser2.UserId), "ok");
		assertEquals(c.makeRole(c.CurrentUser.UserId, c.CurrentGroup.GroupId, demoRole), "ok");
		assertEquals(c.giveTakeRole(c.CurrentUser.UserId, c.CurrentGroup.GroupId, demoUser2.UserId, "demorole", true), "ok");
		
		//cant give non existent roles
		assertEquals(c.giveTakeRole(c.CurrentUser.UserId, c.CurrentGroup.GroupId, demoUser2.UserId, "notarole", true), "Failed to give/take role");
		assertEquals(c.giveTakeRole(c.CurrentUser.UserId, c.CurrentGroup.GroupId, demoUser2.UserId, "notarole", false), "Failed to give/take role");
		
		//new user tries to make and delete things, they do not have the perms for, so it fails
		assertEquals(c.sendMsg(demoUser2.UserId, c.CurrentGroup.GroupId, 0, demoMsg), "Failed to send msg");
		assertEquals(c.makeRole(demoUser2.UserId, c.CurrentGroup.GroupId, demoRole), "Failed to make role");
		assertEquals(c.makeChatListing(demoUser2.UserId, c.CurrentGroup.GroupId, demoList), "Failed to make chat");
		assertEquals(c.inviteUser(demoUser2.UserId, c.CurrentGroup.GroupId, 54545345), "Failed to invite user");
		assertEquals(c.deleteMsg(demoUser2.UserId, c.CurrentGroup.GroupId, 0, 0), "Failed to delete msg");
		assertEquals(c.deleteChatListing(demoUser2.UserId, c.CurrentGroup.GroupId, 0), "Failed to delete chat");
		assertEquals(c.deleteGroup(demoUser2.UserId, c.CurrentGroup.GroupId), "Failed to delete group");
		assertEquals(c.deleteRole(demoUser2.UserId, c.CurrentGroup.GroupId, "demorole"), "Failed to delete role");
		assertEquals(c.giveTakeRole(demoUser2.UserId, c.CurrentGroup.GroupId, demoUser2.UserId, "admin", true), "Failed to give/take role");
		assertEquals(c.giveTakeRole(c.CurrentUser.UserId, c.CurrentGroup.GroupId, demoUser2.UserId, "demorole", false), "ok");
		
		//can only delete valid roles
		assertEquals(c.deleteRole(c.CurrentUser.UserId, c.CurrentGroup.GroupId, "notArole"), "Failed to delete role");
		assertEquals(c.deleteRole(c.CurrentUser.UserId, c.CurrentGroup.GroupId, "demorole"), "ok");
		
		
		
		
		//make a spy user
		ClientSpy cs = new ClientSpy(registry);
		UserData spyUser = new UserData();
		spyUser.DisplayName = "demospy";
		spyUser.Password = "demo";
		assertEquals(cs.addUser(spyUser), "ok");
		assertEquals(cs.login("demospy", "demo"), "ok");
		
		
		assertEquals(cs.callCount, 0);
		
		//send out an updated group to rmi observers
		s.sendOutUpdate(c.CurrentGroup.GroupId);
		
		assertEquals(cs.callCount, 1);
		
		
		
		//client lets the server know it's logging off
		c.alertStatus(c.CurrentUser.UserId, 1);
		c.alertStatus(c.CurrentUser.UserId, 0);
		
		//save server to disk
		s.saveToDisk("test.xml");
		
		//restore from disk
		ServerObject s2 = new ServerObject();
		
		//assert there are groups
		s2.loadFromDisk("test.xml");
		
		//assert there are groups
		assertEquals(s2.GroupDataRepository.Groups.isEmpty(), false);
		
		//assert there is a group named demo
		assertEquals(s2.GroupDataRepository.Groups.get(c.CurrentGroup.GroupId).Name, "demo");
		
		//assert there is a chat named demoListing in demo
		assertEquals(s2.GroupDataRepository.Groups.get(c.CurrentGroup.GroupId).Chats.get(0).ChatName, "demoListing");
		
		//assert there is a message "demo" in demoListing in demo
		assertEquals(s2.GroupDataRepository.Groups.get(c.CurrentGroup.GroupId).Chats.get(0).Chat.get(0).Text, "demo");
		
		//delete msg and chat
		assertEquals(c.deleteMsg(c.CurrentUser.UserId, c.CurrentGroup.GroupId, 0, 0), "ok");
		assertEquals(c.deleteChatListing(c.CurrentUser.UserId, c.CurrentGroup.GroupId, 0), "ok");
		
		//delete group, and user, only if you have the right password
		assertEquals(c.deleteGroup(c.CurrentUser.UserId, c.CurrentGroup.GroupId), "ok");
		assertEquals(c.deleteUser(c.CurrentUser.UserId, "wrongPassword"), "Failed to delete user");
		assertEquals(c.deleteUser(c.CurrentUser.UserId, "demo"), "ok");
		
		
		//save server to disk
		s.saveToDisk("test.xml");
		
		//show there are now no groups
		s2.loadFromDisk("test.xml");
		assertEquals(s2.GroupDataRepository.Groups.isEmpty(), true);
		
	}

}
