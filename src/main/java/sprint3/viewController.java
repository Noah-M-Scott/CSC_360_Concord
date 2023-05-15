package sprint3;

import java.rmi.RemoteException;

import ConcordData.Pair;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class viewController {

	model theModel;
	
	//fxml label ids
    @FXML
    Label msgBox0, msgBox1, msgBox2, msgBox3, msgBox4, msgBox5, msgBox6,
    chnBox7, chnBox6, chnBox5, chnBox4, chnBox3, chnBox2, chnBox1, chnBox0, 
    grpBox7, grpBox6, grpBox5, grpBox4, grpBox3, grpBox2, grpBox1, grpBox0,
    channelLabel, grpLabel, grpMain;
    
    //the text field id
    @FXML
    TextField msgField;
    
    @FXML
    ContextMenu msgContext;
    
    sprint3 callback;
    
    //all the many context menu ids
    @FXML
    MenuItem msgContext0r, msgContext0d,
    msgContext1r, msgContext1d,
    msgContext2r, msgContext2d,
    msgContext3r, msgContext3d,
    msgContext4r, msgContext4d,
    msgContext5r, msgContext5d,
    msgContext6r, msgContext6d,
    chnContext6, chnContext0,
    chnContext1, chnContext2,
    chnContext3, chnContext4,
    chnContext5, chnBoxContext0, 
    chnBoxContext1, chnBoxContext2,
    chnBoxContext3, chnBoxContext4,
    chnBoxContext5, chnBoxContext6,
    chnBoxContext7, grpContext0,
    grpContext1;
    
    @FXML
    VBox msgList;
    
    @FXML
    HBox BoxBar;
    
    Button makeRole, deleteRole;
    CheckBox editChat, editRoles, editMsg, inviteUser, admin;
    TextField roleField;
    
    
    boolean inRoleMode = false;
    
    int msgBoxLimit = 7;
    
    boolean canChats, canRoles, canMsg, canUser, canGroup;
    
    @FXML
    void viewCheck(ActionEvent event) throws RemoteException {
    	exitRoleMode();
    	
    	((Button)BoxBar.getChildren().get(2)).setText("Add");
    	((Button)BoxBar.getChildren().get(2)).setOnAction(e -> {try {
			theModel.addCheck(msgField.getText());
			drawMsgBoxes();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}});
    	
    	((Button)BoxBar.getChildren().get(0)).setText("Take");
    	((Button)BoxBar.getChildren().get(0)).setOnAction(e -> {try {
    		theModel.removeCheck(msgField.getText());
			drawMsgBoxes();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}});
    	
    	theModel.checkView();
    	drawMsgBoxes();
    }
    
    @FXML
    void giveRole(ActionEvent event) throws RemoteException {
    	channelLabel.setText("");
    	channelLabel.setVisible(false);
    	
    	inRoleMode = true;
    	
    	Label[] msgBoxPass = {msgBox0, msgBox1, msgBox2, msgBox3, msgBox4, msgBox5, msgBox6};
    	for(int i = 0; i < msgBoxPass.length; i++)
    		msgList.getChildren().remove(msgBoxPass[i]);
    	
    	canChats = false;
    	canRoles = false;
    	canMsg = false;
    	canUser = false;
    	canGroup = false;
    	
    	editChat = new CheckBox();
    	msgList.getChildren().add(editChat);
    	editChat.setText("can edit chats");
    	editChat.setOnAction(e -> {canChats = !canChats;} );
    	editChat.setId("editChatCheck");
    	
    	editRoles = new CheckBox();
    	msgList.getChildren().add(editRoles);
    	editRoles.setText("can edit roles");
    	editRoles.setOnAction(e -> {canRoles = !canRoles;} );
    	editRoles.setId("editRolesCheck");
    	
    	editMsg = new CheckBox();
    	msgList.getChildren().add(editMsg);
    	editMsg.setText("can send and delete msgs");
    	editMsg.setOnAction(e -> {canMsg = !canMsg;} );
    	editMsg.setId("editMsgCheck");
    	
    	inviteUser = new CheckBox();
    	msgList.getChildren().add(inviteUser);
    	inviteUser.setText("can invite users");
    	inviteUser.setOnAction(e -> {canUser = !canUser;} );
    	inviteUser.setId("inviteUserCheck");
    	
    	admin = new CheckBox();
    	msgList.getChildren().add(admin);
    	admin.setText("can rename and delete group");
    	admin.setOnAction(e -> {canGroup = !canGroup;} );
    	admin.setId("editGroupCheck");
    	
    	roleField = new TextField();
    	msgList.getChildren().add(roleField);
    	roleField.setText("enter role name here");
    	roleField.setId("roleField");
    	
    	makeRole = new Button();
    	msgList.getChildren().add(makeRole);
    	makeRole.setText("Make Role");
    	makeRole.setOnAction(e -> {try {
			if(theModel.makeRole(roleField.getText(), canChats, canRoles, canMsg, canUser, canGroup) != true)
				roleField.setText("role name taken");
			drawMsgBoxes();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}});
    	makeRole.setId("makeRole");
    	
    	deleteRole = new Button();
    	msgList.getChildren().add(deleteRole);
    	deleteRole.setText("Delete Role");
    	deleteRole.setOnAction(e -> {try {
			theModel.deleteRole(roleField.getText());
			drawMsgBoxes();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}});
    	deleteRole.setId("deleteRole");
    	
    	msgField.setText("enter nickname:rolename here");
    	
    	((Button)BoxBar.getChildren().get(2)).setText("Give");
    	((Button)BoxBar.getChildren().get(2)).setOnAction(e -> {try {
			theModel.giveTakeRole(msgField.getText(), true);
			drawMsgBoxes();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}});
    	
    	((Button)BoxBar.getChildren().get(0)).setText("Take");
    	((Button)BoxBar.getChildren().get(0)).setOnAction(e -> {try {
			theModel.giveTakeRole(msgField.getText(), false);
			drawMsgBoxes();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}});
    	
    	msgBoxLimit = 4;
    	for(int i = 0; i < msgBoxLimit; i++) 
    		msgList.getChildren().add(msgBoxPass[msgBoxLimit - 1 - i]);
    	
    	theModel.setMsgWindow(msgBoxLimit);
    	
    	theModel.roleView();
    	drawMsgBoxes();
    }
    
    void exitRoleMode() {
    	if(inRoleMode){
			msgList.getChildren().remove(makeRole);
			msgList.getChildren().remove(deleteRole);
			msgList.getChildren().remove(editChat);
			msgList.getChildren().remove(editMsg);
			msgList.getChildren().remove(inviteUser);
			msgList.getChildren().remove(admin);
			msgList.getChildren().remove(editRoles);
			msgList.getChildren().remove(roleField);
			msgList.getChildren().remove(msgBox0);
			msgList.getChildren().remove(msgBox1);
			msgList.getChildren().remove(msgBox2);
			msgList.getChildren().remove(msgBox3);
			
			msgList.getChildren().add(msgBox6);
			msgList.getChildren().add(msgBox5);
			msgList.getChildren().add(msgBox4);
			msgList.getChildren().add(msgBox3);
			msgList.getChildren().add(msgBox2);
			msgList.getChildren().add(msgBox1);
			msgList.getChildren().add(msgBox0);
	    	msgField.setText("");    	
		}
    	((Button)BoxBar.getChildren().get(2)).setText("Send");
    	((Button)BoxBar.getChildren().get(0)).setText("+");
    	((Button)BoxBar.getChildren().get(2)).setOnAction(e -> {
			try {
				onMsgSend(e);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
    	((Button)BoxBar.getChildren().get(0)).setOnAction(e -> onPlus(e));
    	

    	theModel.setMsgWindow(7);
    	inRoleMode = false;
    	msgBoxLimit = 7;
    }
    
    
    //goes through each label and sets it's text to what the model says
	private void drawMsgBoxes() {
		Label[] msgBoxPass = {msgBox0, msgBox1, msgBox2, msgBox3, msgBox4, msgBox5, msgBox6};
		
		
		
		for(int i = 0; i < msgBoxLimit; i++) {
			
			msgBoxPass[i].setText(theModel.getMsgWindow()); //get text from model
			if(msgBoxPass[i].getText().equals(""))
				msgBoxPass[i].setVisible(false);
			else
				msgBoxPass[i].setVisible(true);
		}
	}
	
	//same as above
	private void drawChnBoxes() {
		Label[] chnBoxPass = {chnBox0, chnBox1, chnBox2, chnBox3, chnBox4, chnBox5, chnBox6, chnBox7};
		
		for(int i = 0; i < chnBoxPass.length; i++) {
			chnBoxPass[i].setText(theModel.getChnWindow()); //call model
			if(chnBoxPass[i].getText().equals(""))
				chnBoxPass[i].setVisible(false);
			else
				chnBoxPass[i].setVisible(true);
		}
	}
	
	//same as above
	private void drawGrpBoxes() {
		Label[] grpBoxPass = {grpBox0, grpBox1, grpBox2, grpBox3, grpBox4, grpBox5, grpBox6, grpBox7};

		for(int i = 0; i < grpBoxPass.length; i++) {
			grpBoxPass[i].setText(theModel.getGrpWindow()); //call model
			if(grpBoxPass[i].getText().equals(""))
				grpBoxPass[i].setVisible(false);
			else
				grpBoxPass[i].setVisible(true);
		}
	}
    
    //make a new group and update labels
    @FXML
    void makeGroup(ActionEvent event) throws RemoteException {
    	theModel.makeGroup();
    	drawGrpBoxes();
    }
    
    //make a new channel and update labels
    @FXML
    void makeChn(ActionEvent event) throws RemoteException {
    	theModel.makeChn();
    	drawChnBoxes();
    }
    
    //make a send a msg and update labels
    @FXML
    void onMsgSend(ActionEvent event) throws RemoteException {
    	theModel.makeMsg(msgField.getText()); //get the text from the text box
    	msgField.clear(); //clear the text afterwards
    	drawMsgBoxes();
    }
    
    //rename a channel
    @FXML
    void renameChn(ActionEvent event) throws RemoteException {
    	theModel.renameChn(msgField.getText()); //get the text from the text box
    	msgField.clear(); //clear the text afterwards
    	drawChnBoxes();
    }
    
    //rename a group
    @FXML
    void renameGrp(ActionEvent event) throws RemoteException {
    	theModel.renameGrp(msgField.getText()); //get the text from the text box
    	msgField.clear(); //clear the text afterwards
    	drawGrpBoxes();
    }
    
    //delte the user
    @FXML
    void deleteUser(ActionEvent event) throws RemoteException {
    	theModel.deleteUser(msgField.getText());
    	logOut();
    }
    
    @FXML
    void qUserId() {
    	msgField.setText("Your userID is: " + theModel.qUserId());
    }
    
    @FXML
    void logOut() {
    	msgField.setText("");
    	exitRoleMode();
    	callback.backScene();
    }
    

    @FXML
    void addUser(ActionEvent event) throws NumberFormatException, RemoteException {
    	theModel.addUser(Integer.valueOf(msgField.getText()));
    }
    
    
    
    
   // @FXML
    //void kickUser(ActionEvent event) {
    	//System.out.println( ((MenuItem)event.getSource()).toString() );
   // }

    //reply to the message.addChat(newChat)
    @FXML
    void replyMsg(ActionEvent event) throws NumberFormatException, RemoteException {
    	Label[] msgBoxPass = {msgBox0, msgBox1, msgBox2, msgBox3, msgBox4, msgBox5, msgBox6};
    	
    	//cludge, menu items cannot speak to their parents, clip their id number, that they share with their parent label
    	theModel.makeMsg( "\" " + msgBoxPass[Integer.valueOf(String.valueOf( ((MenuItem)event.getSource()).toString().charAt(22)))].getText() + " \"" );
    	drawMsgBoxes();
    }
    
    //delete a message and update labels
    @FXML
    void deleteMsg(ActionEvent event) throws NumberFormatException, RemoteException {
    	//same cludge as above
    	theModel.deleteMsg( Integer.valueOf(String.valueOf( ((MenuItem)event.getSource()).toString().charAt(22))) );
    	drawMsgBoxes();
    }
    
    //delete the currently selected group
    @FXML
    void deleteGrp(ActionEvent event) throws RemoteException {
    	theModel.deleteGrp();
    	drawGrpBoxes();
    	drawChnBoxes();
    	exitRoleMode();
    	drawMsgBoxes();
    	grpLabel.setText("");
    	grpLabel.setVisible(false);
    	channelLabel.setText("");
    	channelLabel.setVisible(false);
    }
    
    //delete the currently selected channel
    @FXML
    void deleteChn(ActionEvent event) throws RemoteException {
    	theModel.deleteChn();
    	drawChnBoxes();
    	exitRoleMode();
    	drawMsgBoxes();
    	channelLabel.setText("");
    	channelLabel.setVisible(false);
    }
    
    
    //select a channel
    @FXML
    void onChnSelect(MouseEvent event) {
    	channelLabel.setText(((Label)event.getSource()).getText()); //get the clicked label's text, set the chat heading to it
    	
    	if(channelLabel.getText().equals(""))
    		channelLabel.setVisible(false);
		else
			channelLabel.setVisible(true);
    	
    	//cludge, get the label's id number to get it's index (see model), pass it to model
    	theModel.selectChannel( Integer.valueOf(String.valueOf( ((Label)event.getSource()).toString().charAt(15))) );
    	drawChnBoxes();
    	exitRoleMode();
    	drawMsgBoxes();
    }
    
    //select a group
    @FXML
    void onGrpSelect(MouseEvent event) {
    	grpLabel.setText(((Label)event.getSource()).getText()); //same as above
    	
    	if(grpLabel.getText().equals(""))
    		grpLabel.setVisible(false);
		else
			grpLabel.setVisible(true);
    	
    	
    	//same as above
    	theModel.selectGroup( Integer.valueOf(String.valueOf( ((Label)event.getSource()).toString().charAt(15))) );
    	drawGrpBoxes();
    	drawChnBoxes();
    }

    //on scroll, increment our window index (see model) up or down
    @FXML
    void onMsgScroll(ScrollEvent event) {
    	if(event.getDeltaY() > 0) {
    		theModel.incMsgWindow();
    	} else {
    		theModel.decMsgWindow();
    	}

    	drawMsgBoxes();                        
    }
    
    //same as above
    @FXML
    void onChnScroll(ScrollEvent event) {
    	if(event.getDeltaY() > 0) {
    		theModel.incChnWindow();
    	} else {
    		theModel.decChnWindow();
    	}
    	
    	drawChnBoxes();                        
    }
    
    //same as above
    @FXML
    void onGrpScroll(ScrollEvent event) {
    	if(event.getDeltaY() > 0) {
    		theModel.incGrpWindow();
    	} else {
    		theModel.decGrpWindow();
    	}
    	
    	drawGrpBoxes();                        
    }

    //does nothing yet
    @FXML
    void onPlus(ActionEvent event) {

    }
    
    //init stuff
	public void setModel(model vm, sprint3 callback) {
		this.callback = callback;
		theModel = vm;
		theModel.setMsgWindow(7);
		theModel.setChnWindow(8);
		theModel.setGrpWindow(8);
		theModel.selectGroup(0);
		theModel.selectChannel(0);
		
		msgBox0.setStyle("-fx-background-color: #3b3f7d;\n");
		msgBox1.setStyle("-fx-background-color: #3b3f7d;\n");
		msgBox2.setStyle("-fx-background-color: #3b3f7d;\n");
		msgBox3.setStyle("-fx-background-color: #3b3f7d;\n");
		msgBox4.setStyle("-fx-background-color: #3b3f7d;\n");
		msgBox5.setStyle("-fx-background-color: #3b3f7d;\n");
		msgBox6.setStyle("-fx-background-color: #3b3f7d;\n");
		
		chnBox0.setStyle("-fx-background-color: #3b3f7d;\n");
		chnBox1.setStyle("-fx-background-color: #3b3f7d;\n");
		chnBox2.setStyle("-fx-background-color: #3b3f7d;\n");
		chnBox3.setStyle("-fx-background-color: #3b3f7d;\n");
		chnBox4.setStyle("-fx-background-color: #3b3f7d;\n");
		chnBox5.setStyle("-fx-background-color: #3b3f7d;\n");
		chnBox6.setStyle("-fx-background-color: #3b3f7d;\n");
		chnBox7.setStyle("-fx-background-color: #3b3f7d;\n");
		
		grpBox0.setStyle("-fx-background-color: #3b3f7d;\n");
		grpBox1.setStyle("-fx-background-color: #3b3f7d;\n");
		grpBox2.setStyle("-fx-background-color: #3b3f7d;\n");
		grpBox3.setStyle("-fx-background-color: #3b3f7d;\n");
		grpBox4.setStyle("-fx-background-color: #3b3f7d;\n");
		grpBox5.setStyle("-fx-background-color: #3b3f7d;\n");
		grpBox6.setStyle("-fx-background-color: #3b3f7d;\n");
		grpBox7.setStyle("-fx-background-color: #3b3f7d;\n");
		
		drawMsgBoxes();
		drawChnBoxes();
		drawGrpBoxes();
		channelLabel.setText(chnBox0.getText());
		if(channelLabel.getText().equals(""))
    		channelLabel.setVisible(false);
		else
			channelLabel.setVisible(true);
		grpLabel.setText(grpBox0.getText());
		if(grpLabel.getText().equals(""))
    		grpLabel.setVisible(false);
		else
			grpLabel.setVisible(true);
		grpMain.setText(theModel.Client.CurrentUser.DisplayName);
	}
	
}

