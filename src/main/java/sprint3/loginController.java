package sprint3;

import java.rmi.RemoteException;


import ConcordData.UserData;
import concordSprint2.ClientObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class loginController {

	@FXML
	Button Login;
	@FXML
	Button Reg;
	
	sprint3 callback;
	
	model theModel;
	
	@FXML
	TextField nameBox;
	@FXML
	TextField passwordBox;
	
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
		temp.setDisplayName(nameBox.getText());
		temp.setPassword(passwordBox.getText());
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
