package application;


import javafx.beans.property.SimpleStringProperty;

public class Items {
protected SimpleStringProperty barcode;
private SimpleStringProperty name;
protected SimpleStringProperty cost;
protected SimpleStringProperty quantity;
private SimpleStringProperty date;
public Items(String barcode, String name, String cost,String quantity, String date) {
	super();
	this.barcode = new SimpleStringProperty(barcode);
	this.name = new SimpleStringProperty(name);
	this.cost = new SimpleStringProperty(cost);
	this.quantity = new SimpleStringProperty(quantity);
	this.date = new SimpleStringProperty(date);
}

public String getBarcode() {
	return barcode.get();
}
public void setBarCode(SimpleStringProperty barcode) {
	this.barcode = barcode;
}
public String getName() {
	return name.get();
}
public void setName(SimpleStringProperty name) {
	this.name = name;
}
public String getCost() {
	return cost.get();
}
public void setCost(SimpleStringProperty cost) {
	this.cost = cost;
}
public String getDate() {
	return date.get();
}
public void setDate(SimpleStringProperty date) {
	this.date = date;
}

public String getQuantity() {
	return quantity.get();
}
public void setQuantity(SimpleStringProperty quantity) {
	this.quantity = quantity;
}

}
