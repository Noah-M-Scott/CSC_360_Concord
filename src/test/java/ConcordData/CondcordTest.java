package ConcordData;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

class CondcordTest {
	ChatListing demoList;
	GroupDataRepo demoRepo;
	UserData demoUser;
	UserDataRepo demoUserRepo;
	
	@Test
	void test() {
		demoList = new ChatListing();
		demoList.setChatName("demoListing");
		demoRepo = new GroupDataRepo();
		demoUser = new UserData();
		demoUser.setDisplayName("demo");
		demoUser.setUserId(42);
		demoUserRepo = new UserDataRepo();
		
		MsgData testMsg = new MsgData();
		testMsg.deleted = false;
		testMsg.Text = "demo";
		
		assertEquals(0, demoList.addMsg(testMsg));
		assertEquals(0, demoList.findMsgById(0).MsgIndex);
		assertEquals("demo", demoList.findMsgById(0).Text);
		assertEquals(false, demoList.findMsgById(0).deleted);
		
		assertEquals(true, demoList.deleteMsg(0));
		assertEquals(false, demoList.deleteMsg(1));
		assertEquals(0, demoList.findMsgById(0).MsgIndex);
		assertEquals("deleted", demoList.findMsgById(0).Text);
		assertEquals(true, demoList.findMsgById(0).deleted);
		
		GroupData demoGroup = new GroupData();
		demoGroup.setGroupId(0);
		
		assertEquals(0, demoGroup.addChat(demoList));
		assertEquals("demoListing", demoGroup.findChatById(0).getChatName());
		assertEquals(true, demoGroup.deleteChat(0));
		assertEquals(false, demoGroup.deleteChat(1));
		
		demoUserRepo.addUser(demoUser);
		demoUserRepo.deleteUser(0);                           
		assertEquals(null, demoUserRepo.findUserById(0));     
		
		
		GroupUserData demoGroupUser = demoGroup.addUser(demoUser);
		assertEquals(0, demoGroupUser.UserId);
		assertEquals("demo", demoGroup.findGroupUserById(0).Nickname);
		demoGroup.getRoles().add(new Role());
		demoGroup.getRoles().get(0).Name = "can post";
		demoGroup.getRoles().get(0).Perms.add(new Pair<String, Boolean>("can post", true));
		demoGroupUser.Roles.add(demoGroup.getRoles().get(0));
		
		assertEquals(true, demoGroupUser.checkPerm("can post"));
		assertEquals(false, demoGroupUser.checkPerm("can delete"));
		
		demoGroup.getRoles().get(0).Perms.get(0).setValue(false);
		assertEquals(false, demoGroupUser.checkPerm("can post"));
		
		assertEquals(true, demoGroup.removeUserFromGroup(0));
		assertEquals(false, demoGroup.removeUserFromGroup(1));
		
		demoRepo.addGroup(demoGroup);
		assertEquals(0, demoRepo.findGroupById(0).getGroupId());
		demoRepo.deleteGroup(0);
		assertEquals(null, demoRepo.findGroupById(0));
	}

}
