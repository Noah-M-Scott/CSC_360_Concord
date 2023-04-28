package sprint3;

import java.rmi.RemoteException;

import ConcordData.GroupData;
import ConcordData.UserData;
import concordSprint2.ClientObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;


public class loginController {

	@FXML
	Button Login, Reg;
	
	sprint3 callback;
	
	model theModel;
	
	@FXML
	TextField nameBox, passwordBox;
	
	ClientObject Client;
	
	public void init(sprint3 callbackIn, ClientObject in, model vm) {
		Client = in;
		callback = callbackIn;
		theModel = vm;
	}
	
	
	@FXML
    void login(ActionEvent event) throws RemoteException {
		if( Client.login(nameBox.getText(), passwordBox.getText()).equals("ok") ) {
			theModel.currentUser = Client.CurrentUser;
			callback.nextScene();
			theModel.init(Client);
			return;
		}
		
		nameBox.setText("Could not log in");
		passwordBox.setText("Could not log in");
    }
	
	@FXML
    void register(ActionEvent event) throws RemoteException {
		UserData temp = new UserData();
		temp.DisplayName = nameBox.getText();
		temp.Password = passwordBox.getText();
		if( Client.addUser(temp).equals("ok") ) {
			theModel.currentUser = Client.CurrentUser;
			callback.nextScene();
			theModel.init(Client);
			return;
		}
		
		nameBox.setText("Name Taken");
		passwordBox.setText("Name Taken");
    }
	
}
