package sprint3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class viewController {

	model theModel;
	
	//fxml label ids
    @FXML
    Label msgBox0, msgBox1, msgBox2, msgBox3, msgBox4, msgBox5, msgBox6,
    chnBox7, chnBox6, chnBox5, chnBox4, chnBox3, chnBox2, chnBox1, chnBox0, 
    grpBox7, grpBox6, grpBox5, grpBox4, grpBox3, grpBox2, grpBox1, grpBox0,
    channelLabel, grpLabel;
    
    //the text field id
    @FXML
    TextField msgField;
    
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
    
    
    
    //goes through each label and sets it's text to what the model says
	private void drawMsgBoxes() {
		Label[] msgBoxPass = {msgBox0, msgBox1, msgBox2, msgBox3, msgBox4, msgBox5, msgBox6};
		
		for(int i = 0; i < msgBoxPass.length; i++) {
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
    void makeGroup(ActionEvent event) {
    	theModel.makeGroup();
    	drawGrpBoxes();
    }
    
    //make a new channel and update labels
    @FXML
    void makeChn(ActionEvent event) {
    	theModel.makeChn();
    	drawChnBoxes();
    }
    
    //make a send a msg and update labels
    @FXML
    void onMsgSend(ActionEvent event) {
    	theModel.makeMsg(msgField.getText()); //get the text from the text box
    	msgField.clear(); //clear the text afterwards
    	drawMsgBoxes();
    }
    
    //rename a channel
    @FXML
    void renameChn(ActionEvent event) {
    	theModel.renameChn(msgField.getText()); //get the text from the text box
    	msgField.clear(); //clear the text afterwards
    	drawChnBoxes();
    }
    
    //rename a group
    @FXML
    void renameGrp(ActionEvent event) {
    	theModel.renameGrp(msgField.getText()); //get the text from the text box
    	msgField.clear(); //clear the text afterwards
    	drawGrpBoxes();
    }
    
    //delte the user
    @FXML
    void deleteUser(ActionEvent event) {
    	System.out.println( ((MenuItem)event.getSource()).toString() );
    }
    
    //these all stubs, implement however
    @FXML
    void addRole(ActionEvent event) {
    	//System.out.println( ((MenuItem)event.getSource()).toString() );
    }
    @FXML
    void addUser(ActionEvent event) {
    	//System.out.println( ((MenuItem)event.getSource()).toString() );
    }
    @FXML
    void giveRole(ActionEvent event) {
    	//System.out.println( ((MenuItem)event.getSource()).toString() );
    }
    @FXML
    void deleteRole(ActionEvent event) {
    	//System.out.println( ((MenuItem)event.getSource()).toString() );
    }
    @FXML
    void kickUser(ActionEvent event) {
    	//System.out.println( ((MenuItem)event.getSource()).toString() );
    }

    //reply to the message
    @FXML
    void replyMsg(ActionEvent event) {
    	Label[] msgBoxPass = {msgBox0, msgBox1, msgBox2, msgBox3, msgBox4, msgBox5, msgBox6};
    	
    	//cludge, menu items cannot speak to their parents, clip their id number, that they share with their parent label
    	theModel.makeMsg( "\" " + msgBoxPass[Integer.valueOf(String.valueOf( ((MenuItem)event.getSource()).toString().charAt(22)))].getText() + " \"" );
    	drawMsgBoxes();
    }
    
    //delete a message and update labels
    @FXML
    void deleteMsg(ActionEvent event) {
    	//same cludge as above
    	theModel.deleteMsg( Integer.valueOf(String.valueOf( ((MenuItem)event.getSource()).toString().charAt(22))) );
    	drawMsgBoxes();
    }
    
    //delete the currently selected group
    @FXML
    void deleteGrp(ActionEvent event) {
    	theModel.deleteGrp();
    	drawGrpBoxes();
    	grpLabel.setText("");
    	grpLabel.setVisible(false);
    }
    
    //delete the currently selected channel
    @FXML
    void deleteChn(ActionEvent event) {
    	theModel.deleteChn();
    	drawChnBoxes();
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
	public void setModel(model vm) {
		theModel = vm;
		theModel.setMsgWindow(7);
		theModel.setChnWindow(8);
		theModel.setGrpWindow(8);
		theModel.selectGroup(0);
		theModel.selectChannel(0);
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
	}
	
}

