package sprint3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;


public class loginController {

	@FXML
	Button Login, Reg;
	
	sprint3 callback;
	
	void init(sprint3 callbackIn) {
		callback = callbackIn;
	}
	
	
	@FXML
    void login(ActionEvent event) {
		callback.nextScene();
    }
	
	@FXML
    void register(ActionEvent event) {
		callback.nextScene();
    }
	
}
