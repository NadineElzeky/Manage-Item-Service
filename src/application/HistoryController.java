package application;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class HistoryController {
public Button loadButton;
public TextField profitField;
public TableColumn<Items, String> BarcodeColumn;
public TableColumn<Items, String> NameColumn;
public TableColumn<Items, String> TotalColumn;
public TableColumn<Items, String> QuantityColumn;
public TableColumn<Items, LocalDate> DateColumn;
public TableView<Items> Table;
Connect c = new Connect();


public void initialize() {
	BarcodeColumn.setCellValueFactory(new PropertyValueFactory<Items, String>("barcode"));
	NameColumn.setCellValueFactory(new PropertyValueFactory<Items, String>("name"));
	TotalColumn.setCellValueFactory(new PropertyValueFactory<Items, String>("cost"));
	QuantityColumn.setCellValueFactory(new PropertyValueFactory<Items, String>("quantity"));
	DateColumn.setCellValueFactory(new PropertyValueFactory<Items, LocalDate>("date"));
	Table.setItems(observableList);
}
ObservableList<Items> observableList = FXCollections.observableArrayList();

public void load(ActionEvent event) throws Exception{
	ArrayList<String> results = new ArrayList<String>();
	results = c.History(null);
	if(!results.isEmpty()) {
		for(int i=0 ; i < results.size() -1; i=i+5) {

			Items item = new Items(results.get(i), results.get(i+1), results.get(i+3), results.get(i+2),results.get(i+4) );
			Table.getItems().add(item);
		}
		profitField.setText(results.get(results.size() -1));
		loadButton.setDisable(true);
	}else {
		c.showAlertWithHeaderText("No Sold Items Yet!","Error","error");
	}
	
}


}
