package application;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class CheckandEditController {
	public Button checkButton;
	public TextField barText;
	public Button loadButton;
	public TableColumn<Items, String> BarcodeColumn;
	public TableColumn<Items, String> NameColumn;
	public TableColumn<Items, String> PriceColumn;
	public TableColumn<Items, String> QuantityColumn;
	public TableView<Items> Table;
	int index = 0;
	int flag = 0;
	Connect c = new Connect();

	public void initialize() throws Exception {

		BarcodeColumn.setCellValueFactory(new PropertyValueFactory<Items, String>("barcode"));
		NameColumn.setCellValueFactory(new PropertyValueFactory<Items, String>("name"));
		PriceColumn.setCellValueFactory(new PropertyValueFactory<Items, String>("cost"));
		QuantityColumn.setCellValueFactory(new PropertyValueFactory<Items, String>("quantity"));
		Table.setItems(observableList);

		Table.setEditable(true);
		BarcodeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		NameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		PriceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		QuantityColumn.setCellFactory(TextFieldTableCell.forTableColumn());
	}

	ObservableList<Items> observableList = FXCollections.observableArrayList();

	public void load(ActionEvent event) throws Exception {
		ArrayList<String> results = new ArrayList<String>();
		flag = 1;
		results = c.Select();
		if (!results.isEmpty()) {
			for (int i = 0; i < results.size(); i = i + 4) {
				Items item = new Items(results.get(i), results.get(i + 1), results.get(i + 2), results.get(i + 3),
						null);
				Table.getItems().add(item);
			}
			loadButton.setDisable(true);
		} else {
			c.showAlertWithHeaderText("No Items To Preview","Error","error");
		}
		
	}

	public void checkButton(ActionEvent event) throws Exception {
		if (!barText.getText().trim().isEmpty()) {
			if (flag == 0) {
				load(event);
			}
			ArrayList<String> result = new ArrayList<String>();
			ObservableList<Items> allItems;
			allItems = Table.getItems();
			result = c.sellItem(barText.getText());
			if (result != null) {
				c.showAlertWithHeaderText("Item Found With Quantity = " + result.get(3),"Check","confirm");
				for (int i = 0; i < allItems.size(); i++) {
					if (allItems.get(i).getBarcode().equals(result.get(0))) {
						index = i;
						Platform.runLater(() -> {
							Table.requestFocus();
							Table.getSelectionModel().select(index);
							Table.scrollTo(index);
						});
						break;
					}

				}
			}
		} else {
			c.showAlertWithHeaderText("Please Check Barcode","Error","error");

		}
	}

	public void onEditBarcode(TableColumn.CellEditEvent<Items, String> productStringCellEditEvent) {
		Items item = Table.getSelectionModel().getSelectedItem();
		boolean check = true;
		if (!productStringCellEditEvent.getNewValue().matches("\\d{0,7}(\\d{0,4})?")) {
			item.setBarCode(new SimpleStringProperty(item.getBarcode()));
			Table.getColumns().get(0).setVisible(false);
			Table.getColumns().get(0).setVisible(true);
			check = false;
		}
		if (check) {

			String newBar = productStringCellEditEvent.getNewValue();
			String oldBar = item.getBarcode();
			if (!newBar.isEmpty()) {
				item.setBarCode(new SimpleStringProperty(newBar));
				c.Update(newBar, item.getName(), Integer.parseInt(item.getQuantity()), item.getCost(), oldBar);
			} else {
				Table.getColumns().get(0).setVisible(false);
				Table.getColumns().get(0).setVisible(true);

			}
		}
	}

	public void edited() {
		barText.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (!newValue.matches("\\d*")) {
					barText.setText(oldValue);

					// \\d*(\\.\\d*)? decimal

				}
			}
		});
	}

	public void editName(TableColumn.CellEditEvent<Items, String> productStringCellEditEvent) {
		Items item = Table.getSelectionModel().getSelectedItem();
		String newName = productStringCellEditEvent.getNewValue();
		if (!newName.isEmpty()) {
			item.setName(new SimpleStringProperty(newName));
			c.Update(item.getBarcode(), newName, Integer.parseInt(item.getQuantity()), item.getCost(),
					item.getBarcode());
		} else {
			Table.getColumns().get(0).setVisible(false);
			Table.getColumns().get(0).setVisible(true);

		}
	}

	public void editQuantity(TableColumn.CellEditEvent<Items, String> productStringCellEditEvent) {
		Items item = Table.getSelectionModel().getSelectedItem();
		boolean check = true;
		if (!productStringCellEditEvent.getNewValue().matches("\\d{0,7}(\\d{0,4})?")) {
			item.setQuantity(new SimpleStringProperty(item.getQuantity()));
			Table.getColumns().get(0).setVisible(false);
			Table.getColumns().get(0).setVisible(true);
			check = false;
		}
		if(productStringCellEditEvent.getNewValue().isEmpty()) {
			Table.getColumns().get(0).setVisible(false);
			Table.getColumns().get(0).setVisible(true);
			check = false;
		}
		if (check) {
			if (Integer.parseInt(productStringCellEditEvent.getNewValue()) > 0) {
				item.setQuantity(new SimpleStringProperty(productStringCellEditEvent.getNewValue()));
				c.Update(item.getBarcode(), item.getName(), Integer.parseInt(productStringCellEditEvent.getNewValue()), item.getCost(), item.getBarcode());
			} else {
				c.showAlertWithHeaderText("Please Enter Postive Quantity","Warning","warning");
				Table.getColumns().get(0).setVisible(false);
				Table.getColumns().get(0).setVisible(true);
			}
		}
	}

	public void editPrice(TableColumn.CellEditEvent<Items, String> productStringCellEditEvent) {
		Items item = Table.getSelectionModel().getSelectedItem();
		boolean check = true;
		if (!productStringCellEditEvent.getNewValue().matches("\\d*(\\.\\d*)?")) {
			item.setBarCode(new SimpleStringProperty(item.getBarcode()));
			Table.getColumns().get(0).setVisible(false);
			Table.getColumns().get(0).setVisible(true);
			check = false;
		}
		if (check) {
			String newPrice = productStringCellEditEvent.getNewValue();
			if (!newPrice.isEmpty()) {
				item.setCost(new SimpleStringProperty(newPrice));
				c.Update(item.getBarcode(), item.getName(), Integer.parseInt(item.getQuantity()), item.getCost(),
						item.getBarcode());
			} else {
				Table.getColumns().get(0).setVisible(false);
				Table.getColumns().get(0).setVisible(true);
			}
		}
	}

	public void remove(ActionEvent event) throws Exception {
		ObservableList<Items> allItems, singleItem;
		allItems = Table.getItems();
		singleItem = Table.getSelectionModel().getSelectedItems();
		c.Delete(singleItem.get(0).getBarcode());
		singleItem.forEach(allItems::remove);

	}

}
