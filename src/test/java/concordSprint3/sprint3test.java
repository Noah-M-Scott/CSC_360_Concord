package concordSprint3;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.Start;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sprint3.model;
import sprint3.sprint3;
import sprint3.viewController;

class sprint3test {

	@Start
	private void start(Stage stage) {
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(sprint3.class.getResource("view.fxml"));
		
		
		BorderPane view = null;
		try {
			view = loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		viewController cont = loader.getController();
		
		model vm = new model();
		cont.setModel(vm);
		
		Scene s = new Scene(view);
		stage.setScene(s);
		stage.show();
		
	}
	
	
	
	@Test
	public void test() throws InterruptedException {

		
		
	}

}
