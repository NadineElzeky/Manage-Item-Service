package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class addController {
	public TextField barcode;
	public TextField price;
	public TextField name;
	public TextField quantity;
	public TextField profit;
	public TextField finalCost;
	public Button confirm;
	public Button yes;
	public Button no;
	public RadioButton set;
	public static int originalQ;
	Connect c = new Connect();
	Controller co = new Controller();
	public static String bar2;
	public static String name2;
	public static String finalCost2;
	public static String profit2;
	public static int q2;

	public void pressed() {
		confirm.setOnKeyPressed(new EventHandler<KeyEvent>() {
		    @Override
		    public void handle(KeyEvent k) {
		        if (k.getCode().equals(KeyCode.ENTER)) {
		          try {
					confirm(null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        }
		    }
		});
	}
		// Add Items	
		public void confirm(ActionEvent event) throws Exception {
			if((!profit.getText().trim().isEmpty() || !finalCost.getText().trim().isEmpty() ) && !price.getText().trim().isEmpty() && !barcode.getText().trim().isEmpty() && !quantity.getText().trim().isEmpty() && !name.getText().trim().isEmpty()) {
				originalQ = c.checkBarcode(barcode.getText());
				bar2 = barcode.getText();
				name2 = name.getText();
				finalCost2 = finalCost.getText();
				profit2 = profit.getText();
				q2 = Integer.parseInt(quantity.getText()) + originalQ;
				if(originalQ == -1) {
					c.insertItem(name.getText(), Integer.parseInt(quantity.getText()) , barcode.getText(), finalCost.getText());	
					profit.clear();
					finalCost.clear();
					price.clear();
					name.clear();
					barcode.clear();
					quantity.clear();
					c.showAlertWithHeaderText("Added Successfully","Successfully","confirm");
				}
				else {
					Stage stage = (Stage) confirm.getScene().getWindow();
					co.newscene("/application/View/YesOrNo.fxml",335,171,"Approve",stage);
					profit.clear();
					finalCost.clear();
					price.clear();
					name.clear();
					barcode.clear();
					quantity.clear();
					
				}
			}
			else {
				c.showAlertWithHeaderText("Please Fill All Fields","Warning","warning");
			}
			
		}
		
		public void Yes(ActionEvent event) throws Exception{
			Stage stage = (Stage) yes.getScene().getWindow();
			stage.close();
			c.Update(bar2, name2,q2, finalCost2, bar2);

			c.showAlertWithHeaderText("Updated Successfully","Successfully","confirm");
		}
		public void No(ActionEvent event) throws Exception{
			Stage stage = (Stage) no.getScene().getWindow();
			stage.close();
			c.showAlertWithHeaderText("Please Enter Another Data With Different Barcode","Warning","error");
		}
		


		public void checking(MouseEvent event) throws Exception {
			if (!profit.getText().trim().isEmpty() && !price.getText().trim().isEmpty()) {
				float x = (Float.parseFloat(price.getText()) * Float.parseFloat(profit.getText()) / 100)
						+ Float.parseFloat(price.getText());
				finalCost.setText(String.valueOf(x));
			}
		}
		
		public void edited() {
			barcode.textProperty().addListener(new ChangeListener<String>() {
			    @Override
			    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

			        if (!newValue.matches("\\d*"))
			            {barcode.setText(oldValue);
			            
			            
			    }}
			}  );
			
			quantity.textProperty().addListener(new ChangeListener<String>() {
			    @Override
			    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

			        if (!newValue.matches("\\d*"))
			            {quantity.setText(oldValue);
			            
			          //  \\d*(\\.\\d*)? decimal 
			            
			    }}
			}  );
			

			}
		public void edited2() {
			price.textProperty().addListener(new ChangeListener<String>() {
			    @Override
			    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

			        if (!newValue.matches("\\d*(\\.\\d*)?"))
			            {price.setText(oldValue);
			            
			          
			            
			    }}
			}  );
			finalCost.textProperty().addListener(new ChangeListener<String>() {
			    @Override
			    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

			        if (!newValue.matches("\\d*(\\.\\d*)?"))
			            {finalCost.setText(oldValue);
			            
			          
			            
			    }}
			}  );
			profit.textProperty().addListener(new ChangeListener<String>() {
			    @Override
			    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

			        if (!newValue.matches("\\d*(\\.\\d*)?"))
			            {profit.setText(oldValue);
			            
			          
			            
			    }}
			}  );
			
			}
		public void set(ActionEvent event) throws Exception {
			if (finalCost.isDisabled()) {
				finalCost.setDisable(false);
				profit.setDisable(true);
			} else {
				finalCost.setDisable(true);
				profit.setDisable(false);
			}
		}
		
}
