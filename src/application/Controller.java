package application;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

public class Controller {
public Button check;
public Button exit;
public Button cost;
public Button refund;
public Button sell;
public Button add;
public Button history;
public Button create;
public Button delete;
public CheckBox checkBox;
public TextField usernameField;
public PasswordField passwordField;
public TextField newUserField;
public TextField pwf;
public TextField confirmpwf;
public PasswordField newPasswordField;
public PasswordField checkPasswordField;
public Button confirmButton;
public Button deleteConfirm;
public TextField delUserField;
public static Stage s;

public Button loginButton;
Connect c = new Connect();

public static int flag = 0;
public void Login(ActionEvent event) throws Exception{
	boolean check = false; 
	if(!usernameField.getText().trim().isEmpty()  && !passwordField.getText().trim().isEmpty() ) {
		String username = usernameField.getText().toLowerCase();
	check = c.checkBas(username,passwordField.getText(),flag);
	if(check && flag == 0) {
		Stage stage = (Stage) loginButton.getScene().getWindow();
		stage.close();
		newscene("/application/View/Start.fxml",758,237,"Start",stage);
	}
	else if(check && flag == 1) {
		Stage stage = (Stage) loginButton.getScene().getWindow();
		stage.close();
		newscene("/application/View/CreateUser.fxml",413,221,"Create User",s);
	}
	else if(check && flag == 2) {
		Stage stage = (Stage) loginButton.getScene().getWindow();
		stage.close();
		newscene("/application/View/DeleteUser.fxml",298,163,"Delete User",s);	
	}
	else {
		c.showAlertWithHeaderText("WRONG ID OR PASSWORD","Wrong","error");
	}
	}
	
	else {
		c.showAlertWithHeaderText("PLEASE FILL USERNAME AND PASSWORD FIELDS","Warning","warning");
	}

}


public void createConfirm(ActionEvent event) throws Exception{
	boolean confirm = true;
	int flag = 0;
	if(checkBox.isSelected()) {
		if(!newUserField.getText().trim().isEmpty() && !pwf.getText().trim().isEmpty()  && !confirmpwf.getText().trim().isEmpty() ) {
			if( !pwf.getText().equals(confirmpwf.getText())) {
				c.showAlertWithHeaderText("Password Doesn't Match","Warning","error");
			}
			else {
				confirm = c.CreateUser(newUserField.getText(), pwf.getText());
				flag = 1;
			}
	}
		else {
			c.showAlertWithHeaderText("Please Fill Username and Passwords Fields","Warning","warning");
		}
}else {
	if(!newUserField.getText().trim().isEmpty() && !newPasswordField.getText().trim().isEmpty()  && !checkPasswordField.getText().trim().isEmpty() ) {
		if( !newPasswordField.getText().equals(checkPasswordField.getText())) {
			c.showAlertWithHeaderText("Password Doesn't Match","Wrong","Error");
		}
		else {
		confirm = c.CreateUser(newUserField.getText(), newPasswordField.getText());
		flag = 1;
		}
}
	else {
		c.showAlertWithHeaderText("Please Fill Username and Passwords Fields","Warning","warning");
	}
}
	if(confirm && flag == 1) {
		c.showAlertWithHeaderText("User Added Successfully","Successfully","confirm");
		Stage stage = (Stage) confirmButton.getScene().getWindow();
		stage.close();
	}
	else if (flag == 1 && !confirm){
		c.showAlertWithHeaderText("User Name Is Already In","Warning","Error");
	}
	
}

public void togglePassword(ActionEvent event) throws Exception{
	if(checkBox.isSelected()) {
		pwf.setDisable(false);
		confirmpwf.setDisable(false);
		pwf.setText(newPasswordField.getText());
		pwf.toFront();
		confirmpwf.setText(checkPasswordField.getText());
		confirmpwf.toFront();
		newPasswordField.setDisable(true);
		checkPasswordField.setDisable(true);
	}
	else{
		newPasswordField.setDisable(false);
		checkPasswordField.setDisable(false);
		newPasswordField.setText(pwf.getText());
		newPasswordField.toFront();
		checkPasswordField.setText(confirmpwf.getText());
		checkPasswordField.toFront();
		pwf.setDisable(true);
		confirmpwf.setDisable(true);
	}
}

public void deleteConfirm(ActionEvent event) throws Exception{
	boolean check = false;
	if(!delUserField.getText().trim().isEmpty()) {
		check = c.DeleteUser(delUserField.getText());
	}
	else {
		c.showAlertWithHeaderText("Please Fill Username Filed","Warning","warning");
	}
	
	if (check) {
		c.showAlertWithHeaderText("User Successfully Deleted","Successfully","confirm");
		Stage stage = (Stage) deleteConfirm.getScene().getWindow();
		stage.close();
	}
	else {
		c.showAlertWithHeaderText("No User Found With This Username","Error","error");
	}
}


public void Add(ActionEvent event) throws Exception{
	Stage stage = (Stage) add.getScene().getWindow();
	newscene("/application/View/Add.fxml",550,385,"Add Items",stage);
}
public void Sell(ActionEvent event) throws Exception{
	Stage stage = (Stage) sell.getScene().getWindow();
	newscene("/application/View/Sell.fxml",593,400,"Sell Items",stage);
}
public void Edit(ActionEvent event) throws Exception {
	Stage stage = (Stage) check.getScene().getWindow();
	newscene("/application/View/Check.fxml",761,534,"Items List",stage);
}
public void Refund(ActionEvent event) throws Exception{
	Stage stage = (Stage) refund.getScene().getWindow();
	newscene("/application/View/Refund.fxml",461,365,"Refund Item",stage);
}
public void History(ActionEvent event) throws Exception{
	Stage stage = (Stage) history.getScene().getWindow();
	newscene("/application/View/History.fxml",717,539,"Sell List",stage);
}
public void Create(ActionEvent event) throws Exception{
	flag = 1;
	s=(Stage) create.getScene().getWindow();
	newscene("/application/View/Login.fxml",414,212,"Confirm Login",s);
}
public void Delete(ActionEvent event) throws Exception{
	flag = 2 ;
	s = (Stage) delete.getScene().getWindow();
	newscene("/application/View/Login.fxml",414,212,"Confirm Login",s);
}
public void Exit(ActionEvent event) throws Exception {
	System.exit(0);
}





public void newscene(String scenename,int width, int height, String title,Stage stage) throws Exception {
	Stage primaryStage = new Stage();
	primaryStage.getIcons().add(new Image("/application/Images/icon.png"));
	Parent root = FXMLLoader.load(getClass().getResource(scenename));
	Scene scene = new Scene(root, width, height);
	scene.getStylesheets().add(getClass().getResource("/application/css/application.css").toExternalForm());
	primaryStage.initOwner(stage);
    primaryStage.initModality(Modality.WINDOW_MODAL);
	primaryStage.setTitle(title);
	primaryStage.setScene(scene);
	primaryStage.show();
}


public void pressed() {
	loginButton.setOnKeyPressed(new EventHandler<KeyEvent>() {
	    @Override
	    public void handle(KeyEvent k) {
	        if (k.getCode().equals(KeyCode.ENTER)) {
	          try {
				Login(null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        }
	    }
	});
}
public void pressed2() {
	confirmButton.setOnKeyPressed(new EventHandler<KeyEvent>() {
	    @Override
	    public void handle(KeyEvent k) {
	        if (k.getCode().equals(KeyCode.ENTER)) {
	          try {
				createConfirm(null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        }
	    }
	});


}
public void pressed3() {
	deleteConfirm.setOnKeyPressed(new EventHandler<KeyEvent>() {
	    @Override
	    public void handle(KeyEvent k) {
	        if (k.getCode().equals(KeyCode.ENTER)) {
	          try {
				deleteConfirm(null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        }
	    }
	});
	}




}
