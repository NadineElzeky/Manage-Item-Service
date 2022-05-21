package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import java.io.File;  
 
public class Main extends Application {
		@Override
	public void start(Stage primaryStage) {
		try {
			Parent root=FXMLLoader.load(getClass().getResource("/application/View/Login.fxml"));
			Scene scene = new Scene(root,414,212);
			scene.getStylesheets().add(getClass().getResource("/application/css/application.css").toExternalForm());
			primaryStage.getIcons().add(new Image("/application/Images/icon.png"));
			primaryStage.setTitle("Login");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Connect c = new Connect();
		File file = new File("C:\\sqlite");
		file.mkdir();
		c.createNewTable();
		launch(args);
		
	}
}
