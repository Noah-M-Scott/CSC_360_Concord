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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
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
	
	Scene sc;
	Stage mainStage;
	
	@Start
	public void start(Stage stage) throws Exception {
		
		//test last sprint and sprint1
		Sprint2Test sprint2Test = new Sprint2Test();
		sprint2Test.test();
		
		s = new ServerObject();
		registry = LocateRegistry.createRegistry(2092);
		registry.rebind("concord-s", s);
		c = new ClientObject(registry);
		
		c.CurrentUser = new UserData();
		
		mainStage = stage;
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(sprint3.class.getResource("view.fxml"));
		
		FXMLLoader Loginloader = new FXMLLoader();
		Loginloader.setLocation(sprint3.class.getResource("login.fxml"));
		
		BorderPane LoginView = Loginloader.load();
		BorderPane view = loader.load();
		
		loginController lcont = Loginloader.getController();
		viewController cont = loader.getController();
		
		
		
		
		model vm = new model();
		cont.setModel(vm);
		vm.init(c);
		
		lcont.init(this, c, vm);
		
		sc = new Scene(view);
		Scene ls = new Scene(LoginView);
		
		stage.setScene(ls);
		stage.show();
	}
	
	public void nextScene() {
		mainStage.setScene(sc);
		mainStage.show();
	}
	
	@Test
	public void test(FxRobot robot) throws Exception, InterruptedException{

		

		robot.clickOn("#nameBox");
		robot.write("demo");
		
		robot.clickOn("#passwordBox");
		robot.write("demo");
		
		robot.clickOn("#Reg");
		
		//make and rename group
		robot.rightClickOn("#grpMain");
		robot.clickOn("#MakeGroup");
		robot.clickOn("#msgField");
		robot.write("my group");
		robot.rightClickOn("#grpBox0");
		robot.clickOn("#grpBoxContext");
		robot.clickOn("#grpBox0");
		
		
		//make and rename channel
		robot.rightClickOn("#chnMain");
		robot.clickOn("#chnContext6");
		robot.clickOn("#msgField");
		robot.write("my channel");
		robot.rightClickOn("#chnBox0");
		robot.clickOn("#chnBoxContext0");
		robot.clickOn("#chnBox0");
		
		//send a message, quote it, delete it
		robot.clickOn("#msgField");
		robot.write("hello world");
		robot.clickOn("#sendButton");
		robot.rightClickOn("#msgBox0");
		robot.clickOn("#msgContext0r");
		Thread.sleep(100L);
		robot.rightClickOn("#msgBox0");
		robot.clickOn("#msgContext0d");
		
		
		//check roles
		robot.rightClickOn("#chnMain");
		robot.clickOn("#chnContext2");
		robot.moveTo("#msgBox0");
		robot.scroll(40, VerticalDirection.UP);
		Thread.sleep(1000L);
		robot.scroll(40, VerticalDirection.DOWN);
		
		//make a role
		robot.clickOn("#msgField");
		robot.write("newrole:c");
		robot.clickOn("#sendButton");
		
		//give its self the role
		robot.clickOn("#msgField");
		robot.write(">demo:newrole");
		robot.clickOn("#sendButton");
		Thread.sleep(1000L);
		
		//give take it away
		robot.clickOn("#msgField");
		robot.write("<demo:newrole");
		robot.clickOn("#sendButton");
		Thread.sleep(1000L);
		
		
		
		//make and rename and delete a  channel
		robot.rightClickOn("#chnMain");
		robot.clickOn("#chnContext6");
		robot.clickOn("#msgField");
		robot.write("tb deleted");
		robot.rightClickOn("#chnBox1");
		robot.clickOn("#chnBoxContext1");
		robot.clickOn("#chnBox1");
		robot.rightClickOn("#serverChannelLabel");
		robot.clickOn("#delChn");
		
		
		robot.clickOn("#chnBox0");
		
		
		
		Thread.sleep(1000L);
		
		//logout
		robot.rightClickOn("#grpMain");
		robot.clickOn("#LogOut");
		
		
		//Assertions.assertThat(robot.lookup("#numberLabel").queryAs(Label.class)).hasText(number);
	}

}
