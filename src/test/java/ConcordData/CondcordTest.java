package ConcordData;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CondcordTest {
	ChatListing demoList;
	GroupDataRepo demoRepo;
	UserData demoUser;
	UserDataRepo demoUserRepo;
	
	@Before
	public void setUp() throws Exception {
		demoList = new ChatListing();
		demoList.ChatName = "demoListing";
		demoRepo = new GroupDataRepo();
		demoUser = new UserData();
		demoUser.DisplayName = "demo";
		demoUser.UserId = 42;
		demoUserRepo = new UserDataRepo();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		MsgData testMsg = new MsgData();
		testMsg.deleted = false;
		testMsg.Text = "demo";
		
		assertEquals(demoList.addMsg(testMsg), 0);
		assertEquals(demoList.findMsgById(0).MsgIndex, 0);
		assertEquals(demoList.findMsgById(0).Text, "demo");
		assertEquals(demoList.findMsgById(0).deleted, false);
		
		assertEquals(demoList.deleteMsg(0), true);
		assertEquals(demoList.deleteMsg(1), false);
		assertEquals(demoList.findMsgById(0).MsgIndex, 0);
		assertEquals(demoList.findMsgById(0).Text, "deleted");
		assertEquals(demoList.findMsgById(0).deleted, true);
		
		GroupData demoGroup = new GroupData();
		demoGroup.GroupId = 0;
		
		assertEquals(demoGroup.addChat(demoList), 0);
		assertEquals(demoGroup.findChatById(0).ChatName, "demoListing");
		assertEquals(demoGroup.deleteChat(0), true);
		assertEquals(demoGroup.deleteChat(1), false);
		
		demoUserRepo.addUser(demoUser);                      
		assertEquals(demoUserRepo.findUserById(42).UserId, 42);
		demoUserRepo.deleteUser(0);                           
		assertEquals(demoUserRepo.findUserById(0), null);     
		
		
		GroupUserData demoGroupUser = demoGroup.addUser(demoUser);
		assertEquals(demoGroupUser.UserId, 42);
		assertEquals(demoGroup.findGroupUserById(0).Nickname, "demo");
		demoGroup.Roles.add(new Role());
		demoGroup.Roles.get(0).Name = "can post";
		demoGroup.Roles.get(0).Perms.add(new Pair<String, Boolean>("can post", true));
		demoGroupUser.Roles.add(demoGroup.Roles.get(0));
		
		assertEquals(demoGroupUser.checkPerm("can post"), true);
		assertEquals(demoGroupUser.checkPerm("can delete"), false);
		
		demoGroup.Roles.get(0).Perms.get(0).setValue(false);
		assertEquals(demoGroupUser.checkPerm("can post"), false);
		
		assertEquals(demoGroup.removeUserFromGroup(0), true);
		assertEquals(demoGroup.removeUserFromGroup(1), false);
		
		demoRepo.addGroup(demoGroup);
		assertEquals(demoRepo.findGroupById(0).GroupId, 0);
		demoRepo.deleteGroup(0);
		assertEquals(demoRepo.findGroupById(0), null);
	}

}
