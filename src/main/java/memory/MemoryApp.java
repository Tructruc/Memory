package memory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import memory.view.gameController;

import java.io.IOException;

public class MemoryApp extends Application {


	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("view/game.fxml"));

			BorderPane root = loader.load();

			gameController ctrl = loader.getController();

			Scene scene = new Scene(root);

			primaryStage.setTitle("Memory");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setOnCloseRequest(ctrl::onQuitter);

			primaryStage.show();
		} catch(IOException e){
			System.out.println("Ressource FXML non disponible : game.fxml");
			System.exit(1);
		}



		}

		public static void main2(String[] args) {
			Application.launch(args);
		}

}
