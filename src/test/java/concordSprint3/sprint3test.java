package concordSprint3;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import ConcordData.CondcordTest;
import ConcordData.UserData;
import concordSprint2.ClientObject;
import concordSprint2.ServerObject;
import concordSprint2.Sprint2Test;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HorizontalDirection;
import javafx.geometry.VerticalDirection;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sprint3.entrance;
import sprint3.loginController;
import sprint3.model;
import sprint3.sprint3;
import sprint3.viewController;

@ExtendWith(ApplicationExtension.class)
class sprint3test extends sprint3{

	ServerObject s;
	ClientObject c;
	Registry registry;
	loginController lcont;
	viewController cont;
	model vm; 
	
	Scene sc, mc;
	Stage mainStage;
	
	@Start
	public void start(Stage stage) throws Exception {
		
		s = new ServerObject("test.xml");
		registry = LocateRegistry.createRegistry(2098);
		registry.rebind("concord-s", s);
		c = new ClientObject(registry);
		
		c.CurrentUser = new UserData();
		
		mainStage = stage;
		
		mainStage.initStyle(StageStyle.DECORATED);
		mainStage.setTitle("Concord");
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(sprint3.class.getResource("view.fxml"));
		
		FXMLLoader Loginloader = new FXMLLoader();
		Loginloader.setLocation(sprint3.class.getResource("login.fxml"));
		
		BorderPane LoginView = Loginloader.load();
		BorderPane view = loader.load();
		
		lcont = Loginloader.getController();
		cont = loader.getController();
		
		vm = new model();
		vm.init(c);
		cont.setModel(vm, this);
		
		
		lcont.init(this, c, vm);
		
		sc = new Scene(view);
		mc = new Scene(LoginView);
		
		sc.getStylesheets().add("cssdemo.css");
		mc.getStylesheets().add("cssdemo.css");
		
		stage.setScene(mc);
		stage.show();
	}
	
	public void backScene() {
		c = new ClientObject(registry);
		vm = new model();
		
		vm.init(c);
		lcont.init(this, c, vm);
		mainStage.setScene(mc);
		mainStage.show();
	}
	
	
	public void nextScene() {
		mainStage.setScene(sc);
		cont.setModel(vm, this);
		mainStage.show();
	}
	
	@Test
	public void test(FxRobot robot) throws Exception, InterruptedException{

		
		//register
		robot.clickOn("#nameBox");
		robot.write("demo");
		Assertions.assertThat(robot.lookup("#nameBox").queryAs(TextField.class)).hasText("demo");
		
		//enter password
		robot.clickOn("#passwordBox");
		robot.write("demo");
		Assertions.assertThat(robot.lookup("#passwordBox").queryAs(TextField.class)).hasText("demo");
		
		//register
		robot.clickOn("#Reg");

		
		//Thread.sleep(10000000L);
		
		
		//make and rename group
		robot.rightClickOn("#grpMain");
		robot.clickOn("#MakeGroup");
		robot.clickOn("#msgField");
		robot.write("my group");
		Assertions.assertThat(robot.lookup("#msgField").queryAs(TextField.class)).hasText("my group");
		robot.rightClickOn("#grpBox0");
		robot.clickOn("#grpBoxContext");
		robot.clickOn("#grpBox0");
		Assertions.assertThat(robot.lookup("#grpBox0").queryAs(Label.class)).hasText("my group");
		
		//make and rename channel
		robot.rightClickOn("#chnMain");
		robot.clickOn("#chnContext6");
		Assertions.assertThat(robot.lookup("#chnBox0").queryAs(Label.class)).hasText("New Chat");
		robot.clickOn("#msgField");
		robot.write("my channel");
		robot.rightClickOn("#chnBox0");
		robot.clickOn("#chnBoxContext0");
		robot.clickOn("#chnBox0");
		Assertions.assertThat(robot.lookup("#chnBox0").queryAs(Label.class)).hasText("my channel");
		
		//send a message, quote it, delete it
		robot.clickOn("#msgField");
		robot.write("hello world");
		robot.clickOn("#sendButton");
		Assertions.assertThat(robot.lookup("#msgBox0").queryAs(Label.class)).hasText("demo: hello world");
		robot.rightClickOn("#msgBox0");
		robot.clickOn("#msgContext0r");
		Assertions.assertThat(robot.lookup("#msgBox0").queryAs(Label.class)).hasText("demo: \" demo: hello world \"");
		robot.rightClickOn("#msgBox0");
		robot.clickOn("#msgContext0d");
		Assertions.assertThat(robot.lookup("#msgBox0").queryAs(Label.class)).hasText("deleted");
		
		
		//SPRINT 4 TESTS---------------------------------------------------------
		
		//add a new check bot
		robot.rightClickOn("#chnMain");
		robot.clickOn("#chnContext4");
		Assertions.assertThat(robot.lookup("#msgBox2").queryAs(Label.class)).hasText("Current Checks:");
		robot.clickOn("#msgField");
		for(int i = 0; i < 20; i++) {
			robot.press(KeyCode.BACK_SPACE);
			robot.release(KeyCode.BACK_SPACE);
		}
		robot.write("AutoCensor");
		robot.clickOn("#sendButton");
		Assertions.assertThat(robot.lookup("#msgBox0").queryAs(Label.class)).hasText("AutoCensor : active");
		
		robot.clickOn("#msgField");
		for(int i = 0; i < 20; i++) {
			robot.press(KeyCode.BACK_SPACE);
			robot.release(KeyCode.BACK_SPACE);
		}
		robot.write("AutoExpand");
		robot.clickOn("#sendButton");
		Assertions.assertThat(robot.lookup("#msgBox1").queryAs(Label.class)).hasText("AutoExpand : active");
		
		
		
		robot.clickOn("#msgField");
		for(int i = 0; i < 20; i++) {
			robot.press(KeyCode.BACK_SPACE);
			robot.release(KeyCode.BACK_SPACE);
		}
		
		robot.clickOn("#chnBox0");
		robot.clickOn("#msgField");
		robot.write("badword");
		robot.clickOn("#sendButton");
		Assertions.assertThat(robot.lookup("#msgBox0").queryAs(Label.class)).hasText("demo: [redacted]");
		
		robot.clickOn("#msgField");
		robot.write("lol");
		robot.clickOn("#sendButton");
		Assertions.assertThat(robot.lookup("#msgBox0").queryAs(Label.class)).hasText("demo: laugh out loud");
		
		
		//remove a bot
		robot.rightClickOn("#chnMain");
		robot.clickOn("#chnContext4");
		robot.clickOn("#msgField");
		for(int i = 0; i < 20; i++) {
			robot.press(KeyCode.BACK_SPACE);
			robot.release(KeyCode.BACK_SPACE);
		}
		robot.write("AutoCensor");
		robot.clickOn("#plusButton");
		
		robot.clickOn("#msgField");
		for(int i = 0; i < 20; i++) {
			robot.press(KeyCode.BACK_SPACE);
			robot.release(KeyCode.BACK_SPACE);
		}
		robot.write("AutoExpand");
		robot.clickOn("#plusButton");
		
		robot.clickOn("#msgField");
		for(int i = 0; i < 20; i++) {
			robot.press(KeyCode.BACK_SPACE);
			robot.release(KeyCode.BACK_SPACE);
		}
		
		robot.clickOn("#chnBox0");
		robot.clickOn("#msgField");
		robot.write("badword");
		robot.clickOn("#sendButton");
		Assertions.assertThat(robot.lookup("#msgBox0").queryAs(Label.class)).hasText("demo: badword");
		robot.clickOn("#msgField");
		robot.write("lol");
		robot.clickOn("#sendButton");
		Assertions.assertThat(robot.lookup("#msgBox0").queryAs(Label.class)).hasText("demo: lol");
		
		
		//-----------------------------------------------------------------------
		
		
		
		
		//---new role tests---
		
		//check roles
		robot.rightClickOn("#chnMain");
		robot.clickOn("#chnContext2");
		robot.moveTo("#msgBox0");
		robot.scroll(40, VerticalDirection.UP);
		Assertions.assertThat(robot.lookup("#msgBox3").queryAs(Label.class)).hasText("Current Roles:");
		robot.scroll(40, VerticalDirection.DOWN);
		
		//make a role
		robot.clickOn("#editChatCheck");
		robot.clickOn("#editRolesCheck");
		robot.clickOn("#roleField");
		for(int i = 0; i < 20; i++) {
			robot.press(KeyCode.BACK_SPACE);
			robot.release(KeyCode.BACK_SPACE);
		}
		robot.write("newrole");
		robot.clickOn("#makeRole");
		Assertions.assertThat(robot.lookup("#msgBox0").queryAs(Label.class)).hasText("delete role:true, edit role:true, }");
		
		//can't make doubles
		Thread.sleep(100L);
		robot.clickOn("#makeRole");
		Assertions.assertThat(robot.lookup("#roleField").queryAs(TextField.class)).hasText("role name taken");
		
		//give its self the role
		robot.moveTo("#sendButton");
		robot.clickOn("#msgField");
		
		//robot clicks too far left of the field???!?!!?!?!?!
		for(int i = 0; i < 10; i++) {
			robot.press(KeyCode.RIGHT);
			robot.release(KeyCode.RIGHT);
		}
		
		for(int i = 0; i < 40; i++) {
			robot.press(KeyCode.BACK_SPACE);
			robot.release(KeyCode.BACK_SPACE);
		}
		robot.write("demo:newrole");
		robot.clickOn("#sendButton");
		Assertions.assertThat(robot.lookup("#msgBox0").queryAs(Label.class)).hasText("demo has newrole");
		
		//give take it away
		robot.clickOn("#plusButton");
		Assertions.assertThat(robot.lookup("#msgBox0").queryAs(Label.class)).hasText("delete role:true, edit role:true, }");
		
		//delete role
		robot.moveTo("#sendButton");
		robot.clickOn("#roleField");
		for(int i = 0; i < 10; i++) {
			robot.press(KeyCode.RIGHT);
			robot.release(KeyCode.RIGHT);
		}
		for(int i = 0; i < 40; i++) {
			robot.press(KeyCode.BACK_SPACE);
			robot.release(KeyCode.BACK_SPACE);
		}
		robot.write("newrole");
		robot.clickOn("#deleteRole");
		Assertions.assertThat(robot.lookup("#msgBox0").queryAs(Label.class)).hasText("demo has admin");
		
		//---------------------
		
		
		
		
		//make and rename and delete a  channel
		robot.rightClickOn("#chnMain");
		robot.clickOn("#chnContext6");
		robot.clickOn("#chnBox1");
		robot.clickOn("#msgField");
		robot.write("tb deleted");
		robot.rightClickOn("#chnBox1");
		robot.clickOn("#chnBoxContext1");
		robot.clickOn("#chnBox1");
		Assertions.assertThat(robot.lookup("#serverChannelLabel").queryAs(Label.class)).hasText("tb deleted");
		robot.rightClickOn("#serverChannelLabel");
		robot.clickOn("#delChn");
		Assertions.assertThat(robot.lookup("#chnBox1").queryAs(Label.class)).hasText("");
		robot.clickOn("#chnBox0");
		
		//test channel scroll
		
		//add a bunch of channels
		robot.rightClickOn("#chnMain");
		robot.clickOn("#chnContext6");
		robot.rightClickOn("#chnMain");
		robot.clickOn("#chnContext6");
		robot.rightClickOn("#chnMain");
		robot.clickOn("#chnContext6");
		robot.rightClickOn("#chnMain");
		robot.clickOn("#chnContext6");
		robot.rightClickOn("#chnMain");
		robot.clickOn("#chnContext6");
		robot.rightClickOn("#chnMain");
		robot.clickOn("#chnContext6");
		robot.rightClickOn("#chnMain");
		robot.clickOn("#chnContext6");
		robot.rightClickOn("#chnMain");
		robot.clickOn("#chnContext6");
		robot.rightClickOn("#chnMain");
		robot.clickOn("#chnContext6");
		
		//make sure their scrolling
		robot.moveTo("#chnBox0");
		robot.scroll(40, VerticalDirection.UP);
		Assertions.assertThat(robot.lookup("#chnBox0").queryAs(Label.class)).hasText("New Chat");
		robot.scroll(40, VerticalDirection.DOWN);
		Assertions.assertThat(robot.lookup("#chnBox0").queryAs(Label.class)).hasText("my channel");
		
		
		//test group scroll
		
		//add a bunch of groups
		robot.rightClickOn("#grpMain");
		robot.clickOn("#MakeGroup");
		robot.rightClickOn("#grpMain");
		robot.clickOn("#MakeGroup");
		robot.rightClickOn("#grpMain");
		robot.clickOn("#MakeGroup");
		robot.rightClickOn("#grpMain");
		robot.clickOn("#MakeGroup");
		robot.rightClickOn("#grpMain");
		robot.clickOn("#MakeGroup");
		robot.rightClickOn("#grpMain");
		robot.clickOn("#MakeGroup");
		robot.rightClickOn("#grpMain");
		robot.clickOn("#MakeGroup");
		robot.rightClickOn("#grpMain");
		robot.clickOn("#MakeGroup");
		robot.rightClickOn("#grpMain");
		robot.clickOn("#MakeGroup");
		
		//make sure it scrolls up and down
		robot.moveTo("#grpBox1");
		robot.scroll(40, VerticalDirection.UP);
		Assertions.assertThat(robot.lookup("#grpBox0").queryAs(Label.class)).hasText("NewGroup");
		robot.moveTo("#grpBox2");
		robot.scroll(40, VerticalDirection.DOWN);
		Assertions.assertThat(robot.lookup("#grpBox0").queryAs(Label.class)).hasText("my group");
		
		//check that chats are only shown in the correct group
		robot.clickOn("#grpBox0");
		Assertions.assertThat(robot.lookup("#chnBox0").queryAs(Label.class)).hasText("my channel");
		robot.clickOn("#grpBox1");
		Assertions.assertThat(robot.lookup("#chnBox0").queryAs(Label.class)).hasText("");
		
		//delete a channel
		robot.clickOn("#grpBox0");
		Assertions.assertThat(robot.lookup("#chnBox0").queryAs(Label.class)).hasText("my channel");
		robot.clickOn("#chnBox0");
		robot.rightClickOn("#serverChannelLabel");
		robot.clickOn("#delChn");
		Assertions.assertThat(robot.lookup("#chnBox0").queryAs(Label.class)).hasText("New Chat");
		
		//delete a group
		robot.clickOn("#grpBox0");
		robot.rightClickOn("#chnMain");
		robot.clickOn("#chnContext5");
		Assertions.assertThat(robot.lookup("#grpBox0").queryAs(Label.class)).hasText("NewGroup");
	
		
		//logout
		robot.rightClickOn("#grpMain");
		robot.clickOn("#LogOut");
		
		
		
		//fail to register (name taken)
		robot.clickOn("#nameBox");
		
		//this is how you can clear a text box
		for(int i = 0; i < 20; i++) {
			robot.press(KeyCode.BACK_SPACE);
			robot.release(KeyCode.BACK_SPACE);
		}
		robot.write("demo");
		Assertions.assertThat(robot.lookup("#nameBox").queryAs(TextField.class)).hasText("demo");
		robot.clickOn("#passwordBox");
		
		for(int i = 0; i < 20; i++) {
			robot.press(KeyCode.BACK_SPACE);
			robot.release(KeyCode.BACK_SPACE);
		}
		robot.write("demo");
		Assertions.assertThat(robot.lookup("#passwordBox").queryAs(TextField.class)).hasText("demo");
		robot.clickOn("#Reg");
		Assertions.assertThat(robot.lookup("#nameBox").queryAs(TextField.class)).hasText("Name Taken");
		Assertions.assertThat(robot.lookup("#passwordBox").queryAs(TextField.class)).hasText("Name Taken");
		
		
		//fail to login
		robot.clickOn("#nameBox");
		for(int i = 0; i < 20; i++) {
			robot.press(KeyCode.BACK_SPACE);
			robot.release(KeyCode.BACK_SPACE);
		}
		robot.write("demo");
		Assertions.assertThat(robot.lookup("#nameBox").queryAs(TextField.class)).hasText("demo");
		robot.clickOn("#passwordBox");
		
		for(int i = 0; i < 20; i++) {
			robot.press(KeyCode.BACK_SPACE);
			robot.release(KeyCode.BACK_SPACE);
		}
		robot.write("notdemo");
		Assertions.assertThat(robot.lookup("#passwordBox").queryAs(TextField.class)).hasText("notdemo");
		robot.clickOn("#Login");
		Assertions.assertThat(robot.lookup("#nameBox").queryAs(TextField.class)).hasText("Could not log in");
		Assertions.assertThat(robot.lookup("#passwordBox").queryAs(TextField.class)).hasText("Could not log in");
		

		//login
		robot.clickOn("#nameBox");
		for(int i = 0; i < 20; i++) {
			robot.press(KeyCode.BACK_SPACE);
			robot.release(KeyCode.BACK_SPACE);
		}
		robot.write("demo");
		Assertions.assertThat(robot.lookup("#nameBox").queryAs(TextField.class)).hasText("demo");
		robot.clickOn("#passwordBox");
		for(int i = 0; i < 20; i++) {
			robot.press(KeyCode.BACK_SPACE);
			robot.release(KeyCode.BACK_SPACE);
		}
		robot.write("demo");
		Assertions.assertThat(robot.lookup("#passwordBox").queryAs(TextField.class)).hasText("demo");
		robot.clickOn("#Login");
		
		
		//get userid, so we can add them to a group later
		robot.rightClickOn("#grpMain");
		robot.clickOn("#SeeUserID");
		Assertions.assertThat(robot.lookup("#msgField").queryAs(TextField.class)).hasText("Your userID is: 0");
		
		
		//logout
		robot.rightClickOn("#grpMain");
		robot.clickOn("#LogOut");
		
		
		//register as a new user
		robot.clickOn("#nameBox");
		for(int i = 0; i < 20; i++) {
			robot.press(KeyCode.BACK_SPACE);
			robot.release(KeyCode.BACK_SPACE);
		}
		robot.write("newdemo");
		Assertions.assertThat(robot.lookup("#nameBox").queryAs(TextField.class)).hasText("newdemo");
		
		robot.clickOn("#passwordBox");
		for(int i = 0; i < 20; i++) {
			robot.press(KeyCode.BACK_SPACE);
			robot.release(KeyCode.BACK_SPACE);
		}
		robot.write("newdemo");
		Assertions.assertThat(robot.lookup("#passwordBox").queryAs(TextField.class)).hasText("newdemo");
		
		robot.clickOn("#Reg");
		
		
		//make a group named invited, and invite our other account
		robot.rightClickOn("#grpMain");
		robot.clickOn("#MakeGroup");

		robot.clickOn("#grpBox0");
		
		robot.clickOn("#msgField");
		for(int i = 0; i < 1; i++) {
			robot.press(KeyCode.BACK_SPACE);
			robot.release(KeyCode.BACK_SPACE);
		}
		robot.write("invited");
		Assertions.assertThat(robot.lookup("#msgField").queryAs(TextField.class)).hasText("invited");
		robot.rightClickOn("#grpBox0");
		robot.clickOn("#grpBoxContext");
		robot.clickOn("#grpBox0");
		
		robot.rightClickOn("#chnMain");
		robot.clickOn("#chnContext6");
		robot.clickOn("#chnBox0");
		
		robot.write("0");
		robot.rightClickOn("#chnMain");
		robot.clickOn("#chnContext1");
		robot.clickOn("#chnBox0");
		
		//make sure they joined
		Assertions.assertThat(robot.lookup("#msgBox0").queryAs(Label.class)).hasText("demo has joined the group");
		
		
		
		//give admin, to test roles on other users
		robot.rightClickOn("#chnMain");
		robot.clickOn("#chnContext2");
		robot.clickOn("#msgField");
		
		for(int i = 0; i < 10; i++) {
			robot.press(KeyCode.RIGHT);
			robot.release(KeyCode.RIGHT);
		}
		for(int i = 0; i < 40; i++) {
			robot.press(KeyCode.BACK_SPACE);
			robot.release(KeyCode.BACK_SPACE);
		}
		
		robot.write("demo:admin");
		robot.clickOn("#sendButton");
		Assertions.assertThat(robot.lookup("#msgBox0").queryAs(Label.class)).hasText("demo has admin");
		
		
		//logout
		robot.rightClickOn("#grpMain");
		robot.clickOn("#LogOut");
		
		
		
		
		//make sure we're now in the new group we where invited to
		robot.clickOn("#nameBox");
		
		for(int i = 0; i < 20; i++) {
			robot.press(KeyCode.BACK_SPACE);
			robot.release(KeyCode.BACK_SPACE);
		}

		
		robot.write("demo");
		Assertions.assertThat(robot.lookup("#nameBox").queryAs(TextField.class)).hasText("demo");
		robot.clickOn("#passwordBox");
		
		for(int i = 0; i < 20; i++) {
			robot.press(KeyCode.BACK_SPACE);
			robot.release(KeyCode.BACK_SPACE);
		}

		
		robot.write("demo");
		Assertions.assertThat(robot.lookup("#passwordBox").queryAs(TextField.class)).hasText("demo");
		robot.clickOn("#Login");
		
		//check that the channels are in there
		robot.moveTo("#grpBox1");                                                                
		robot.scroll(40, VerticalDirection.UP);                                                  
		Assertions.assertThat(robot.lookup("#grpBox7").queryAs(Label.class)).hasText("invited");
		robot.clickOn("#grpBox7");
		robot.clickOn("#chnBox0");
		
		//and that the channels have text
		Assertions.assertThat(robot.lookup("#msgBox0").queryAs(Label.class)).hasText("demo has joined the group");
		
		
		Thread.sleep(1000L);
		
		
	}

}
