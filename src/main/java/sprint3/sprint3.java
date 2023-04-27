package sprint3;
import concordSprint2.ClientObject;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class sprint3 extends Application {

	Scene sc;
	Stage mainStage;
	public static ClientObject Client;
	
	public void start(Stage stage) throws Exception {
		
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
		vm.init(Client);
		
		lcont.init(this, Client, vm);
		
		sc = new Scene(view);
		Scene ls = new Scene(LoginView);
		
		stage.setScene(ls);
		stage.show();
	}
	
	public void nextScene() {
		mainStage.setScene(sc);
		mainStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	
}
