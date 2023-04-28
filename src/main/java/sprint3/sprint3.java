package sprint3;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


import ConcordData.UserData;
import concordSprint2.ClientObject;
import concordSprint2.ServerObject;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class sprint3 extends Application {

	ServerObject s;
	static ClientObject c;
	Registry registry;
	loginController lcont;
	viewController cont;
	model vm; 
	
	Scene sc, mc;
	Stage mainStage;
	
	public void start(Stage stage) throws Exception {
		
		s = new ServerObject();
		registry = LocateRegistry.createRegistry(2084);
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
		
		lcont = Loginloader.getController();
		cont = loader.getController();
		
		vm = new model();
		vm.init(c);
		cont.setModel(vm, this);
		
		
		lcont.init(this, c, vm);
		
		sc = new Scene(view);
		mc = new Scene(LoginView);
		
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
	
	public static void main(String[] args) {
		launch(args);
	}

	
}
