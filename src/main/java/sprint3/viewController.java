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
	
    @FXML
    Label msgBox0, msgBox1, msgBox2, msgBox3, msgBox4, msgBox5, msgBox6,
    chnBox7, chnBox6, chnBox5, chnBox4, chnBox3, chnBox2, chnBox1, chnBox0, 
    grpBox7, grpBox6, grpBox5, grpBox4, grpBox3, grpBox2, grpBox1, grpBox0,
    channelLabel, grpLabel;
    
    @FXML
    TextField msgField;
    
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
    
    
    
   
	private void drawMsgBoxes() {
		Label[] msgBoxPass = {msgBox0, msgBox1, msgBox2, msgBox3, msgBox4, msgBox5, msgBox6};
		
		for(int i = 0; i < msgBoxPass.length; i++) {
			msgBoxPass[i].setText(theModel.getMsgWindow());
			if(msgBoxPass[i].getText().equals(""))
				msgBoxPass[i].setVisible(false);
			else
				msgBoxPass[i].setVisible(true);
		}
	}
	
	private void drawChnBoxes() {
		Label[] chnBoxPass = {chnBox0, chnBox1, chnBox2, chnBox3, chnBox4, chnBox5, chnBox6, chnBox7};
		
		for(int i = 0; i < chnBoxPass.length; i++) {
			chnBoxPass[i].setText(theModel.getChnWindow());
			if(chnBoxPass[i].getText().equals(""))
				chnBoxPass[i].setVisible(false);
			else
				chnBoxPass[i].setVisible(true);
		}
	}
	
	private void drawGrpBoxes() {
		Label[] grpBoxPass = {grpBox0, grpBox1, grpBox2, grpBox3, grpBox4, grpBox5, grpBox6, grpBox7};
		
		for(int i = 0; i < grpBoxPass.length; i++) {
			grpBoxPass[i].setText(theModel.getGrpWindow());
			if(grpBoxPass[i].getText().equals(""))
				grpBoxPass[i].setVisible(false);
			else
				grpBoxPass[i].setVisible(true);
		}
	}
    
    @FXML
    void deleteMsg(ActionEvent event) {
    	//theModel.deleteMsg();
    	
    }
    
    @FXML
    void makeGroup(ActionEvent event) {
    	theModel.makeGroup();
    	drawGrpBoxes();
    }
    
    @FXML
    void makeChn(ActionEvent event) {
    	theModel.makeChn();
    	drawChnBoxes();
    }
    
    @FXML
    void onMsgSend(ActionEvent event) {
    	theModel.makeMsg(msgField.getText());
    	drawMsgBoxes();
    }
    
    @FXML
    void renameChn(ActionEvent event) {
    	theModel.renameChn(msgField.getText());
    	//channelLabel.setText( ((MenuItem)event.getSource()).getText() );
    	drawChnBoxes();
    }
    
    @FXML
    void renameGrp(ActionEvent event) {
    	theModel.renameGrp(msgField.getText());
    	//grpLabel.setText( ((MenuItem)event.getSource()).getText() );
    	drawGrpBoxes();
    }
    
    @FXML
    void deleteUser(ActionEvent event) {
    	System.out.println( ((Label)event.getSource()).toString() );
    }
    @FXML
    void addRole(ActionEvent event) {
    	System.out.println( ((Label)event.getSource()).toString() );
    }
    @FXML
    void addUser(ActionEvent event) {
    	System.out.println( ((Label)event.getSource()).toString() );
    }
    @FXML
    void giveRole(ActionEvent event) {
    	System.out.println( ((Label)event.getSource()).toString() );
    }
    @FXML
    void deleteRole(ActionEvent event) {
    	System.out.println( ((Label)event.getSource()).toString() );
    }
    @FXML
    void kickUser(ActionEvent event) {
    	System.out.println( ((Label)event.getSource()).toString() );
    }

    @FXML
    void replyMsg(ActionEvent event) {
    	Label[] msgBoxPass = {msgBox0, msgBox1, msgBox2, msgBox3, msgBox4, msgBox5, msgBox6};
    	theModel.makeMsg( "\" " + msgBoxPass[Integer.valueOf(String.valueOf( ((MenuItem)event.getSource()).toString().charAt(22)))].getText() + " \"" );
    	drawMsgBoxes();
    }
    @FXML
    void deleteGrp(ActionEvent event) {
    	theModel.deleteGrp();
    	theModel.selectGroup(0);
    	drawGrpBoxes();
    }
    @FXML
    void deleteChn(ActionEvent event) {
    	theModel.deleteChn();
    	theModel.selectChannel(0);
    	drawChnBoxes();
    }
    
    
    
    
    
    
    
    @FXML
    void onChnSelect(MouseEvent event) {
    	drawChnBoxes();
    	channelLabel.setText(((Label)event.getSource()).getText());
    	theModel.selectChannel( Integer.valueOf(String.valueOf( ((Label)event.getSource()).toString().charAt(15))) );
    	drawMsgBoxes();
    }
    
    @FXML
    void onGrpSelect(MouseEvent event) {
    	drawGrpBoxes();
    	grpLabel.setText(((Label)event.getSource()).getText());
    	theModel.selectGroup( Integer.valueOf(String.valueOf( ((Label)event.getSource()).toString().charAt(15))) );
    	drawChnBoxes();
    }

    @FXML
    void onMsgScroll(ScrollEvent event) {
    	if(event.getDeltaY() > 0) {
    		theModel.incMsgWindow();
    	} else {
    		theModel.decMsgWindow();
    	}
    	
    	drawMsgBoxes();                        
    }
    
    @FXML
    void onChnScroll(ScrollEvent event) {
    	if(event.getDeltaY() > 0) {
    		theModel.incChnWindow();
    	} else {
    		theModel.decChnWindow();
    	}
    	
    	drawChnBoxes();                        
    }
    
    @FXML
    void onGrpScroll(ScrollEvent event) {
    	if(event.getDeltaY() > 0) {
    		theModel.incGrpWindow();
    	} else {
    		theModel.decGrpWindow();
    	}
    	
    	drawGrpBoxes();                        
    }

    @FXML
    void onPlus(ActionEvent event) {

    }

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
		grpLabel.setText(grpBox0.getText());
	}
	
}

