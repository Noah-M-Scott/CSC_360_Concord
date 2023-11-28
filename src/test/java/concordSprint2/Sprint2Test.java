package concordSprint2;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.junit.jupiter.api.Test;

import ConcordData.*;

class Sprint2Test {

	ServerObject s;
	ClientObject c;
	Registry registry; 
	

	@Test
	void test() throws Exception {
		
		//new server and client
		s = new ServerObject("test.xml");
		registry = LocateRegistry.createRegistry(2082);
		registry.rebind("concord-s", s);
		c = new ClientObject(registry);
		
		//make a new user
		UserData demoUser = new UserData();
		demoUser.setDisplayName("demo");
		demoUser.setPassword("demo");
		assertEquals("Failed to add user", c.addUser(null));
		assertEquals("ok", c.addUser(demoUser));
		assertEquals(0, c.CurrentUser.getUserId());
		
		//incorrect password vs correct one
		assertEquals("Failed to Login", c.login("demo", "notdemo"));
		assertEquals("ok", c.login("demo", "demo"));
		
		//new group + admin role
		GroupData demoGroup = new GroupData();
		demoGroup.addUser(demoUser);
		demoGroup.setName("demo");
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
		admin.Perms.add(new Pair<String, Boolean>("edit checks", true));
		demoGroup.getRoles().add(admin);
		demoGroup.findGroupUserById(c.CurrentUser.getUserId()).Roles.add(admin);
		assertEquals("ok", c.makeGroup(c.CurrentUser.getUserId(), demoGroup));
		
		//cant make a null group
		assertEquals("Failed to make group", c.makeGroup(c.CurrentUser.getUserId(), null));
		
		//new chat
		ChatListing demoList = new ChatListing();
		demoList.setChatName("demoListing");
		assertEquals("ok", c.makeChatListing(c.CurrentUser.getUserId(), c.CurrentGroup.getGroupId(), demoList));
		
		//cant get a groups data if it doesn't exist/your not in it
		assertEquals("Failed to get group", c.getGroupData(c.CurrentUser.getUserId(), 6));
		assertEquals("ok", c.getGroupData(c.CurrentUser.getUserId(), c.CurrentGroup.getGroupId()));
		
		//new msg
		MsgData demoMsg = new MsgData();
		demoMsg.deleted = false;
		demoMsg.Text = "demo";
		assertEquals("ok", c.sendMsg(c.CurrentUser.getUserId(), c.CurrentGroup.getGroupId(), 0, demoMsg));
		
		
		//SPRINT 4---------------------------------------------------------------------------------------------
		assertEquals("ok", c.addACheck(c.CurrentUser.getUserId(), c.CurrentGroup.getGroupId(), "AutoCensor"));
		
		//new bad msg
		demoMsg = new MsgData();
		demoMsg.deleted = false;
		demoMsg.Text = "bug";
		assertEquals("ok", c.sendMsg(c.CurrentUser.getUserId(), c.CurrentGroup.getGroupId(), 0, demoMsg));
		assertEquals("[redacted]", c.CurrentGroup.getChats().get(0).getChat().get(1).Text);
		//-----------------------------------------------------------------------------------------------------
		
		
		
		//new role
		Role demoRole = new Role();
		demoRole.Name = "demorole";
		assertEquals("ok", c.makeRole(c.CurrentUser.getUserId(), c.CurrentGroup.getGroupId(), demoRole));
		
		//new user
		UserData demoUser2 = new UserData();
		demoUser2.setDisplayName("demo2");
		demoUser2.setPassword("demo2");
		demoUser2 = s.addUser(demoUser2);
		
		//invite the new user, give them the new demo role
		assertEquals("Failed to invite user", c.inviteUser(c.CurrentUser.getUserId(), c.CurrentGroup.getGroupId(), 54545345));
		assertEquals("ok", c.inviteUser(c.CurrentUser.getUserId(), c.CurrentGroup.getGroupId(), demoUser2.getUserId()));
		assertEquals("ok", c.makeRole(c.CurrentUser.getUserId(), c.CurrentGroup.getGroupId(), demoRole));
		assertEquals("ok", c.giveTakeRole(c.CurrentUser.getUserId(), c.CurrentGroup.getGroupId(), demoUser2.getUserId(), "demorole", true));
		
		//cant give non existent roles
		assertEquals("Failed to give/take role", c.giveTakeRole(c.CurrentUser.getUserId(), c.CurrentGroup.getGroupId(), demoUser2.getUserId(), "notarole", true));
		assertEquals("Failed to give/take role", c.giveTakeRole(c.CurrentUser.getUserId(), c.CurrentGroup.getGroupId(), demoUser2.getUserId(), "notarole", false));
		
		//new user tries to make and delete things, they do not have the perms for, so it fails
		assertEquals("Failed to send msg", c.sendMsg(demoUser2.getUserId(), c.CurrentGroup.getGroupId(), 0, demoMsg));
		assertEquals("Failed to make role", c.makeRole(demoUser2.getUserId(), c.CurrentGroup.getGroupId(), demoRole));
		assertEquals("Failed to make chat", c.makeChatListing(demoUser2.getUserId(), c.CurrentGroup.getGroupId(), demoList));
		assertEquals("Failed to invite user", c.inviteUser(demoUser2.getUserId(), c.CurrentGroup.getGroupId(), 54545345));
		assertEquals("Failed to delete msg", c.deleteMsg(demoUser2.getUserId(), c.CurrentGroup.getGroupId(), 0, 0));
		assertEquals("Failed to delete chat", c.deleteChatListing(demoUser2.getUserId(), c.CurrentGroup.getGroupId(), 0));
		assertEquals("Failed to delete group", c.deleteGroup(demoUser2.getUserId(), c.CurrentGroup.getGroupId()));
		assertEquals("Failed to delete role", c.deleteRole(demoUser2.getUserId(), c.CurrentGroup.getGroupId(), "demorole"));
		assertEquals("Failed to give/take role", c.giveTakeRole(demoUser2.getUserId(), c.CurrentGroup.getGroupId(), demoUser2.getUserId(), "admin", true));
		assertEquals("ok", c.giveTakeRole(c.CurrentUser.getUserId(), c.CurrentGroup.getGroupId(), demoUser2.getUserId(), "demorole", false));
		
		//can only delete valid roles
		assertEquals("Failed to delete role", c.deleteRole(c.CurrentUser.getUserId(), c.CurrentGroup.getGroupId(), "notArole"));
		assertEquals("ok", c.deleteRole(c.CurrentUser.getUserId(), c.CurrentGroup.getGroupId(), "demorole"));
		
		
		
		
		//make a spy user
		ClientSpy cs = new ClientSpy(registry);
		UserData spyUser = new UserData();
		spyUser.setDisplayName("demospy");
		spyUser.setPassword("demo");
		assertEquals("ok", cs.addUser(spyUser));
		assertEquals("ok", cs.login("demospy", "demo"));
		
		
		assertEquals(0, cs.callCount);
		
		//send out an updated group to rmi observers
		s.sendOutUpdate(c.CurrentGroup.getGroupId());
		
		assertEquals(1, cs.callCount);
		
		
		
		//client lets the server know it's logging off
		c.alertStatus(c.CurrentUser.getUserId(), 1);
		c.alertStatus(c.CurrentUser.getUserId(), 0);
		
		//save server to disk
		s.saveToDisk();
		
		//restore from disk
		ServerObject s2 = new ServerObject("test.xml");
		
		//assert there are groups
		s2.loadFromDisk("test.xml");
		
		//assert there are groups
		assertEquals(false, s2.GroupDataRepository.getGroups().isEmpty());
		
		//assert there is a group named demo
		assertEquals("demo", s2.GroupDataRepository.getGroups().get(c.CurrentGroup.getGroupId()).getName());
		
		//assert there is a chat named demoListing in demo
		assertEquals("demoListing", s2.GroupDataRepository.getGroups().get(c.CurrentGroup.getGroupId()).getChats().get(0).getChatName());
		
		//assert there is a message "demo" in demoListing in demo
		assertEquals("demo", s2.GroupDataRepository.getGroups().get(c.CurrentGroup.getGroupId()).getChats().get(0).getChat().get(1).Text);
		
		//delete msg and chat
		assertEquals("ok", c.deleteMsg(c.CurrentUser.getUserId(), c.CurrentGroup.getGroupId(), 0, 0));
		assertEquals("ok", c.deleteChatListing(c.CurrentUser.getUserId(), c.CurrentGroup.getGroupId(), 0));
		
		//delete group, and user, only if you have the right password
		assertEquals("ok", c.deleteGroup(c.CurrentUser.getUserId(), c.CurrentGroup.getGroupId()));
		assertEquals("Failed to delete user", c.deleteUser(c.CurrentUser.getUserId(), "wrongPassword"));
		assertEquals("ok", c.deleteUser(c.CurrentUser.getUserId(), "demo"));
		
		
		//save server to disk
		s.saveToDisk();
		
		//show there are now no groups
		s2.loadFromDisk("test.xml");
		assertEquals(true, s2.GroupDataRepository.getGroups().isEmpty());
		
	}

}
