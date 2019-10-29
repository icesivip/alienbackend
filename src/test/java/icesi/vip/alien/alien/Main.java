package icesi.vip.alien.alien;

import java.io.IOException;

import icesi.vip.alien.networks.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

	public static void main(String[] args) throws IOException {
		//AdjListGraph<Integer> graph = new AdjListGraph<>("src/main/java/icesi/vip/alien/networks/test.cases/case1.txt", true);
		launch(args);
		
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("menu.FXML"));
		Scene scene = new Scene(root);
		stage.setTitle("Menu");
		stage.setScene(scene);
		stage.show();
		
		
	}

}
