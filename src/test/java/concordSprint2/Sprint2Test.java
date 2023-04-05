package concordSprint2;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ConcordData.*;

class Sprint2Test {

	ServerObject s;
	ClientObject c;
	Registry registry; 
	

	@Test
	void test() throws Exception {
		
		CondcordTest sprint1Test = new CondcordTest();
		sprint1Test.setUp();
		sprint1Test.test();
		
		s = new ServerObject();
		registry = LocateRegistry.createRegistry(2099);
		registry.rebind("concord-s", s);
		c = new ClientObject(registry);
		
		UserData demoUser = new UserData();
		demoUser.DisplayName = "demo";
		demoUser.Password = "demo";
		c.addUser(demoUser);
		assertEquals(c.CurrentUser.UserId, 0);
		
		c.login("demo", "demo");
		
		GroupData demoGroup = new GroupData();
		demoGroup.addUser(demoUser);
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
		c.makeGroup(c.CurrentUser.UserId, demoGroup);
		
		ChatListing demoList = new ChatListing();
		demoList.ChatName = "demoListing";
		assertEquals(c.makeChatListing(c.CurrentUser.UserId, c.CurrentGroup.GroupId, demoList), "ok");
		
		assertEquals(c.getGroupData(c.CurrentUser.UserId, c.CurrentGroup.GroupId), "ok");
		
		MsgData demoMsg = new MsgData();
		demoMsg.deleted = false;
		demoMsg.Text = "demo";
		assertEquals(c.sendMsg(c.CurrentUser.UserId, c.CurrentGroup.GroupId, 0, demoMsg), "ok");
		assertEquals(c.deleteMsg(c.CurrentUser.UserId, c.CurrentGroup.GroupId, 0, 0), "ok");
		assertEquals(c.deleteChatListing(c.CurrentUser.UserId, c.CurrentGroup.GroupId, 0), "ok");
		
		Role demoRole = new Role();
		demoRole.Name = "demorole";
		demoRole.Perms.add(new Pair<String, Boolean>("make msg", true));
		assertEquals(c.makeRole(c.CurrentUser.UserId, c.CurrentGroup.GroupId, demoRole), "ok");
		
		
		UserData demoUser2 = new UserData();
		demoUser2.DisplayName = "demo2";
		demoUser2.Password = "demo2";
		demoUser2 = s.addUser(demoUser2);
		
		assertEquals(c.inviteUser(c.CurrentUser.UserId, c.CurrentGroup.GroupId, demoUser2.UserId), "ok");
		assertEquals(c.makeRole(c.CurrentUser.UserId, c.CurrentGroup.GroupId, demoRole), "ok");
		assertEquals(c.giveTakeRole(c.CurrentUser.UserId, c.CurrentGroup.GroupId, demoUser2.UserId, "demorole", true), "ok");
		
		s.saveToDisk("test.xml");
		
		assertEquals(c.deleteRole(c.CurrentUser.UserId, c.CurrentGroup.GroupId, "demorole"), "ok");
		
		s.sendOutUpdate(c.CurrentGroup.GroupId);
		c.alertStatus(c.CurrentUser.UserId, 0);
		
		assertEquals(c.deleteGroup(c.CurrentUser.UserId, c.CurrentGroup.GroupId), "ok");
		assertEquals(c.deleteUser(c.CurrentUser.UserId, "demo"), "ok");
		
		
		
	}

}
