package application;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class refundController {
public Button refund;
public TextField barField;
public TextField refundAmount;
Connect c = new Connect();


public void refund(ActionEvent event) throws Exception{
	if(!barField.getText().trim().isEmpty()) {
	String barcode = barField.getText();
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.DATE, -14);
	SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");
	String date = format1.format(cal.getTime());
	float amount = c.refundItem(barcode, date);
	refundAmount.setText(String.valueOf(amount));
	barField.clear();
	}
	else {
		c.showAlertWithHeaderText("Please Fill Field","Warning","warning");
	}
}
public void edited() {
	barField.textProperty().addListener(new ChangeListener<String>() {
	    @Override
	    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

	        if (!newValue.matches("\\d*"))
	            {barField.setText(oldValue);
	            
	          //  \\d*(\\.\\d*)? decimal 
	            
	    }}
	}  );
}

}
