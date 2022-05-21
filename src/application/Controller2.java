package application;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class Controller2 {

	public TableColumn<Items, String> colBar;
	public TableColumn<Items, String> colName;
	public TableColumn<Items, String> colPrice;
	public TableColumn<Items, String> colQuantity;
	public TableColumn<Items, LocalDate> colDate;
	public TableView<Items> sellTable;
	public TextField sellBar;
	public TextField textQuantity;
	public TextField fieldTotal;
	public Button sellB;
	public RadioButton radioBtn;
	public TextField saleField;
	private float total;

	Connect c = new Connect();

// Sell Items	
	public void initialize() {
		colBar.setCellValueFactory(new PropertyValueFactory<Items, String>("barcode"));
		colName.setCellValueFactory(new PropertyValueFactory<Items, String>("name"));
		colPrice.setCellValueFactory(new PropertyValueFactory<Items, String>("cost"));
		colQuantity.setCellValueFactory(new PropertyValueFactory<Items, String>("quantity"));
		sellTable.setItems(observableList);

		sellTable.setEditable(true);
		colQuantity.setCellFactory(TextFieldTableCell.forTableColumn());
	}

	ObservableList<Items> observableList = FXCollections.observableArrayList();

	public void buttonAdd(ActionEvent event) throws Exception {
		ArrayList<String> result = new ArrayList<String>();
		result = c.sellItem(sellBar.getText());
		if (result != null) {
			Items item = new Items(result.get(0), result.get(2), result.get(1), "1", null);
			total = Float.parseFloat(result.get(1)) + total;
			fieldTotal.setText(String.valueOf(total));
			sellTable.getItems().add(item);
		}
	}
	public void edited() {
		sellBar.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

		        if (!newValue.matches("\\d*"))
		            {sellBar.setText(oldValue);
		            
		          //  \\d*(\\.\\d*)? decimal 
		            
		    }}
		}  );
		}
	public void toggle(ActionEvent event) throws Exception{
		if (saleField.isDisable()) {
			saleField.setDisable(false);
		}
		else {
			saleField.setDisable(true);
			fieldTotal.setText(String.valueOf(total));
			
		}
	}
	public void edited2() {
		saleField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

		        if (!newValue.matches("\\d*(\\.\\d*)?"))
		            {saleField.setText(oldValue);
		            
		          
		            
		    }}
		}  );
	}
	public void sale(MouseEvent event) throws Exception{
		if(!fieldTotal.getText().trim().isEmpty() && !saleField.getText().trim().isEmpty() && !saleField.isDisable()) {
			float newTotal = total - (total * Float.parseFloat(saleField.getText()) / 100);
			fieldTotal.setText(String.valueOf(newTotal));
		}
		else if (!fieldTotal.getText().trim().isEmpty()) {
			fieldTotal.setText(String.valueOf(total));
		}
	}
	public void buttonRemove(ActionEvent event) throws Exception {
		ObservableList<Items> allItems, singleItem;
		allItems = sellTable.getItems();
		singleItem = sellTable.getSelectionModel().getSelectedItems();
		if(!singleItem.isEmpty()) {
		float remove = Float.parseFloat(singleItem.get(0).cost.get());
		total -= remove;
		fieldTotal.setText(String.valueOf(total));
		singleItem.forEach(allItems::remove);
		}
		else {
			c.showAlertWithHeaderText("No Selected Item To Remove", "Error","error");
		}
	}
	public void pressed() {
		sellB.setOnKeyPressed(new EventHandler<KeyEvent>() {
		    @Override
		    public void handle(KeyEvent k) {
		        if (k.getCode().equals(KeyCode.ENTER)) {
		          try {
					sellButton(null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        }
		    }
		});
	}
	public void sellButton(ActionEvent event) throws Exception{
		ObservableList<Items> allItems;
		allItems = sellTable.getItems();
		if(!allItems.isEmpty()) {
		String uniqueId = UniqueRandomIdGenerator.getUniqueRandomId();
		for(int i = 0 ; i < allItems.size() ; i++) {
			c.insertSold(allItems.get(i).getBarcode(), Integer.parseInt(allItems.get(i).getQuantity()), total, uniqueId );
		}
		c.showAlertWithHeaderText("Sold Successfully", "Successfully", "confirm");
		Stage stage = (Stage) sellB.getScene().getWindow();
		stage.close();
		}
		else {
			c.showAlertWithHeaderText("No Items To Be Sold", "Error", "Error");
		}
		
	}

	public void onEditChange(TableColumn.CellEditEvent<Items, String> productStringCellEditEvent) {
		Items item = sellTable.getSelectionModel().getSelectedItem();
		boolean check = true;
		int Q = -1;
		
		if( !productStringCellEditEvent.getNewValue().matches("\\d{0,7}(\\d{0,4})?")) {
			item.setQuantity(new SimpleStringProperty(item.getQuantity()));
			sellTable.getColumns().get(0).setVisible(false);
			sellTable.getColumns().get(0).setVisible(true);
			check = false;
			return;
		}
		if(productStringCellEditEvent.getNewValue().isEmpty()) {
			item.setQuantity(new SimpleStringProperty(item.getQuantity()));
			sellTable.getColumns().get(0).setVisible(false);
			sellTable.getColumns().get(0).setVisible(true);
		check = false;
		return;
		}
		Q  = c.confirmQ(item.getBarcode() ,Integer.parseInt(productStringCellEditEvent.getNewValue()));
		
		if( Q != Integer.parseInt(productStringCellEditEvent.getNewValue()) && check ) {
			c.showAlertWithHeaderText("No Available Quantity Of " +Integer.parseInt(productStringCellEditEvent.getNewValue()) +" Only Available is " +Q,"Warning","error");
		}
	
		if(check) {
		if (Q > 0 ) {
			float defaultCost = Float.parseFloat(item.getCost()) / Integer.parseInt(item.getQuantity());
			item.setQuantity(new SimpleStringProperty(String.valueOf(Q)));
			float oldV = Float.parseFloat(item.getCost());
			float newV = Q * defaultCost;
			item.setCost(new SimpleStringProperty(String.valueOf(newV)));
			total = (total - oldV) + newV;
			fieldTotal.setText(String.valueOf(total));
			sellTable.getColumns().get(0).setVisible(false);
			sellTable.getColumns().get(0).setVisible(true);
		}
			
		else {
			ObservableList<Items> allItems, singleItem;
			allItems = sellTable.getItems();
			singleItem = sellTable.getSelectionModel().getSelectedItems();
			float remove = Float.parseFloat(singleItem.get(0).cost.get());
			total -= remove;
			fieldTotal.setText(String.valueOf(total));
			singleItem.forEach(allItems::remove);
		}
	}


}
}
