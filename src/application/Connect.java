package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Connect {
	PasswordAuthentication pw = new PasswordAuthentication();
	private static Connection conn = null;
	public static int first = 0;

	public void showAlertWithHeaderText(String messg,String title,String iconName) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		ImageView icon = new ImageView("/application/Images/"+iconName+".png");
		icon.setFitHeight(48);
        icon.setFitWidth(48);
		alert.getDialogPane().setGraphic(icon);
		alert.setHeaderText(null);
		alert.setContentText(messg);
		 Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
	      stage.getIcons().add(new Image("/application/Images/"+iconName+".png"));

		alert.showAndWait();
	}

	public void connect() {
		try {
			if (conn == null) {
				// db parameters
				String url = "jdbc:sqlite:C:/sqlite/JTP.db";
				// create a connection to the database
				conn = DriverManager.getConnection(url);
				System.out.println("Connection to SQLite has been established.");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void createNewTable() {

		// SQL statement for creating a new table
		String sql = "CREATE TABLE IF NOT EXISTS items (\n" + " id integer PRIMARY KEY AUTOINCREMENT,\n"
				+ " barcode varchar(20) NOT NULL,\n" + " cost varchar(20) NOT NULL,\n" + "name varchar(20),\n"
				+ "quantity integer,\n" + "UNIQUE('barcode')\n" + ");";

		String sql2 = "CREATE TABLE IF NOT EXISTS sold (\n" + " id integer PRIMARY KEY AUTOINCREMENT,\n"
				+ "date INTERGER ,\n" + "quantity INTEGER,\n"+"total REAL,\n" + "unique_id varchar(20),\n" + "item_id INTEGER,\n" + "FOREIGN KEY (item_id)\n" + "REFERENCES items (id)\n"
				+ "ON UPDATE SET DEFAULT \n" + "ON DELETE SET DEFAULT \n" + ");";
		String sql3 = "CREATE TABLE IF NOT EXISTS auth (\n" + "username varchar(20) NOT NULL,\n"+ "password varchar(20) NOT NULL,\n"+"UNIQUE(username)\n" +");";

		try {
			connect();
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
			stmt.execute(sql2);
			stmt.execute(sql3);
		
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (conn != null) {
				try {

					conn.close();
					System.out.println("Connection Closed\n");
					conn = null;

				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		}

	}
	public int checkBarcode(String barcode) {
		int qu = -1;
		String sql = "SELECT quantity FROM items where barcode = ?";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, barcode);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				qu = rs.getInt("quantity");
			}
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			if (conn != null) {
				try {

					conn.close();
					System.out.println("Connection Closed\n");
					conn = null;

				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		}
		return qu;
	}
	public void insertItem(String name, int quantity, String barcode, String cost) {
		String sql = "INSERT INTO items(barcode,cost,name,quantity) VALUES(?,?,?,?)";
		
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, barcode);
			pstmt.setString(2, cost);
			pstmt.setString(3, name);
			pstmt.setInt(4, quantity);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (conn != null) {
				try {

					conn.close();
					System.out.println("Connection Closed\n");
					conn = null;

				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		}
	}

	public ArrayList<String> sellItem(String barcode) {
		ArrayList<String> results = new ArrayList<String>();
		boolean flag = false;
		try {
			connect();
			String sql = "SELECT (COUNT(*) > 0) FROM items where barcode = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, barcode);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				boolean found = rs.getBoolean(1);

				if (found) {
					String sql2 = "SELECT * FROM items where barcode=  ?";
					PreparedStatement pstmt2 = conn.prepareStatement(sql2);
					pstmt2.setString(1, barcode);
					ResultSet rs2 = pstmt2.executeQuery();
					if (rs2.getInt("quantity") > 0) {
						results.add(rs2.getString("barcode"));
						results.add(rs2.getString("cost"));
						results.add(rs2.getString("name"));
						results.add(rs2.getString("quantity"));

					} else {
						showAlertWithHeaderText("Quantity Of The Product = 0","Warning","warning");
						flag = true;
					}
				} else {
					showAlertWithHeaderText("Please Check Barcode Product Not Found !!","Error","error");
					flag = true;
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (conn != null) {
				try {

					conn.close();
					System.out.println("Connection Closed\n");
					conn = null;

				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}

		}
		if (flag) {
			return null;
		}
		return results;
	}

	public void insertSold(String barcode,int quantity, float total,String unique) {
		try {
			connect();
			String sql = "SELECT * FROM items WHERE barcode = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, barcode);
			ResultSet rs = pstmt.executeQuery();
			int newQuantity= rs.getInt("quantity") - quantity;
			if ( newQuantity < 0 ) {
				showAlertWithHeaderText("No such quantity of this item "+ rs.getString("barcode"),"Error","error" );
				conn.close();
				conn = null;
				return ;
			}
			String sql2 = "UPDATE items set quantity = ? where barcode = ?";
			PreparedStatement pstmt2 = conn.prepareStatement(sql2);
			pstmt2.setInt(1, newQuantity);
			pstmt2.setString(2, barcode);
			pstmt2.executeUpdate();
			String sql3 = "INSERT INTO sold (date,item_id,quantity,total,unique_id) VALUES (?,?,?,?,?)";
			PreparedStatement pstmt3 = conn.prepareStatement(sql3);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			LocalDateTime now = LocalDateTime.now();

			pstmt3.setString(1, dtf.format(now));
			pstmt3.setInt(2, rs.getInt("id"));
			pstmt3.setInt(3, quantity);
			pstmt3.setFloat(4, total);
			pstmt3.setString(5, unique);
			pstmt3.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());

		} finally {
			if (conn != null) {
				try {

					conn.close();
					System.out.println("Connection Closed\n");
					conn = null;

				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}

		}
	}
	public int confirmQ(String barcode, int quantity) {
		String sql = "SELECT * FROM items where barcode = ?";
				try {
					connect();
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, barcode);
					ResultSet rs = pstmt.executeQuery();
					if(rs.next()) {
						int oldQ = rs.getInt("quantity");
						if(oldQ < quantity) {
							quantity = oldQ;
						}
					}
				}catch(SQLException e) {
					System.out.println(e.getMessage());
				}finally {
					if (conn != null) {
						try {

							conn.close();
							System.out.println("Connection Closed\n");
							conn = null;

						} catch (SQLException ex) {
							System.out.println(ex.getMessage());
						}
					}
	
				}
				return quantity;
	}

	public ArrayList<String> Select() {
		ArrayList<String> results = new ArrayList<String>();
		try {
		String sql = "SELECT * FROM items";
		connect();
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			results.add(rs.getString("barcode"));
			results.add(rs.getString("name"));
			results.add(rs.getString("cost"));
			results.add(String.valueOf(rs.getInt("quantity")));
			
		}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			if (conn != null) {
				try {

					conn.close();
					System.out.println("Connection Closed\n");
					conn = null;

				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		}
		return results;
		
	}
	
	public void Update(String barcode, String name, int quantity, String price,String oldBarcode) {
		try {
			connect();
			String sql = "UPDATE items SET barcode = ?,name = ?, quantity = ? , cost = ? WHERE barcode = ? ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, barcode);
			pstmt.setString(2, name);
			pstmt.setInt(3, quantity);
			pstmt.setString(4, price);
			pstmt.setString(5, oldBarcode);
			pstmt.executeUpdate();
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			if (conn != null) {
				try {

					conn.close();
					System.out.println("Connection Closed\n");
					conn = null;

				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		}
	}
	public void Delete(String barcode) {
		try {
			connect();
			String sql = "DELETE FROM items WHERE barcode = ? ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, barcode);
			pstmt.executeUpdate();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			if (conn != null) {
				try {

					conn.close();
					System.out.println("Connection Closed\n");
					conn = null;

				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		}
	}

	public float refundItem(String barcode,String date) {
		ArrayList<String> result = new ArrayList<String>();
		boolean check = false;
		try {
			connect();
		String sql = "SELECT sold.id, items.quantity,sold.quantity as Q, sold.unique_id, items.cost, sold.total FROM items,sold WHERE sold.item_id = items.id AND items.barcode = ? AND sold.date > ? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, barcode);
		pstmt.setString(2, date);
		ResultSet rs = pstmt.executeQuery();
		
		if(rs.next()) {
			result.add(rs.getString("id"));
			result.add(rs.getString("quantity"));
			result.add(rs.getString("Q"));
			result.add(rs.getString("cost"));
			result.add(rs.getString("total"));
			result.add(rs.getString("unique_id"));
		}
		if(!result.isEmpty()) {
			String sql2 = "UPDATE items SET quantity = ? WHERE barcode = ? ";
			PreparedStatement pstmt2 = conn.prepareStatement(sql2);
			int itemQua = Integer.parseInt(result.get(1));
			itemQua += 1;
			pstmt2.setInt(1,itemQua);
			pstmt2.setString(2, barcode);
			pstmt2.executeUpdate();
			float tot = Float.parseFloat(result.get(4));
			tot -= Float.parseFloat(result.get(3));
			String sql5 = "UPDATE sold SET total = ? WHERE unique_id = ?";
			PreparedStatement pstmt5 = conn.prepareStatement(sql5);
			pstmt5.setFloat(1, tot);
			pstmt5.setString(2, result.get(5));
			pstmt5.executeUpdate();
			String sql3 = "UPDATE sold SET quantity = ? WHERE id = ? ";
			PreparedStatement pstmt3 = conn.prepareStatement(sql3);
			int soldQua = Integer.parseInt(result.get(2));
			soldQua -= 1;
			
			if (soldQua >= 1 ) {
				pstmt3.setInt(1, soldQua);
				pstmt3.setInt(2,Integer.parseInt(result.get(0)));
				pstmt3.executeUpdate();
			}
			else {
				String sql4 = "DELETE FROM sold where id = ? ";
				PreparedStatement pstmt4 = conn.prepareStatement(sql4);
				pstmt4.setInt(1, Integer.parseInt(result.get(0)));
				pstmt4.executeUpdate();
			}
			
		}
		else {
			showAlertWithHeaderText("ERROR in barcode","Error","error");
			check = true;
		}
		
		
		
		
		}catch(SQLException e){
			System.out.println(e.getMessage());
		}finally {
			if (conn != null) {
				try {

					conn.close();
					System.out.println("Connection Closed\n");
					conn = null;

				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		}
		if(check) {
			return 0;
		}
		return Float.parseFloat(result.get(3));
	}
	
	public ArrayList<String> History(String date) {
		ArrayList<String> results = new ArrayList<String>();
		try {
			connect();
			String sql = "SELECT items.name, items.barcode, sold.quantity, sold.total,sold.date FROM items,sold WHERE sold.item_id = items.id";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				results.add(rs.getString("barcode"));
				results.add(rs.getString("name"));
				results.add(rs.getString("quantity"));
				results.add(rs.getString("total"));
				results.add(rs.getString("date"));
			}
			if(!results.isEmpty()) {
				String sql2 = "SELECT total FROM sold GROUP BY unique_id";
				float total = 0;
				PreparedStatement pstmt2 = conn.prepareStatement(sql2);
				ResultSet rs2 = pstmt2.executeQuery();
				
				while(rs2.next()) {
					 total += rs2.getFloat("total");
				}
				results.add(String.valueOf(total));
			}
			
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			if (conn != null) {
				try {

					conn.close();
					System.out.println("Connection Closed\n");
					conn = null;
				
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		}
		return results;
	}
	
	@SuppressWarnings("deprecation")
	public boolean confirmAuth(String username,String password) {
		String sql = "SELECT * FROM auth WHERE username = ?";
		boolean confirm = false;
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				String hashedPass = rs.getString("password");
				confirm = pw.authenticate(password, hashedPass);
			}
		}catch(SQLException e) {
			System.out.println("err");
			System.out.println(e.getMessage());
		}finally {
			if (conn != null) {
				try {

					conn.close();
					System.out.println("Connection Closed\n");
					conn = null;
				
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		}	
	return confirm;
		
	}
	@SuppressWarnings("deprecation")
	public void insertAuth(String username, String password) {
		String sql = "INSERT INTO auth (username,password) VALUES(?,?)";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			pstmt.setString(2, pw.hash(password));
			pstmt.executeUpdate();
		}catch(SQLException e) {
			System.out.println("err2");
			System.out.println(e.getMessage());
		}finally {
			if (conn != null) {
				try {

					conn.close();
					System.out.println("Connection Closed\n");
					conn = null;
				
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		}
	}
	@SuppressWarnings("deprecation")
	public boolean checkBas(String username,String password,int flag) {
		String sql = "SELECT username FROM auth WHERE username = 'program'";
		String admin = "program";
		String adminPass = pw.hash("HappyLife");
		boolean confirm = false;
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			if(!rs.next() && flag == 0 ) {
				confirm = true;
				if( !admin.equals(username) && !adminPass.equals(pw.hash(password)) ){
					showAlertWithHeaderText("Admin Must Sign In First Time", "Error", "error");
					confirm = false;
					System.out.println(confirm);
				}
				username = "program";
				password = pw.hash("HappyLife");
				String sql2 = "INSERT INTO auth (username,password) VALUES(?,?)";
				PreparedStatement pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, username);
				pstmt2.setString(2, password);
				pstmt2.executeUpdate();
				
				
				System.out.println(confirm);
				
			}
			else {
				confirm = confirmAuth(username, password);
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			if (conn != null) {
				try {

					conn.close();
					System.out.println("Connection Closed\n");
					conn = null;
				
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		}
		return confirm;
	}
public boolean CreateUser(String username,String password) {
	String sql = "SELECT username from auth WHERE username = ?";
	boolean check = true;
			try {
				
				connect();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, username);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					check = false;
				}
				else {
					insertAuth(username, password);
				}
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}finally {
				if (conn != null) {
					try {

						conn.close();
						System.out.println("Connection Closed\n");
						conn = null;
					
					} catch (SQLException ex) {
						System.out.println(ex.getMessage());
					}
				}
			}
			return check;
}
	
public boolean DeleteUser(String username) {
	String sql = "SELECT username from auth WHERE username = ?";
	boolean check = true;
			try {
				
				connect();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, username);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					sql = "DELETE FROM auth WHERE username = ?";
					PreparedStatement pstmt2 = conn.prepareStatement(sql);
					pstmt2.setString(1, username);
					pstmt2.executeUpdate();
				}
				else {
					check = false;
				}
	}catch(SQLException e) {
		System.out.println(e.getMessage());
	}finally {
		if (conn != null) {
			try {

				conn.close();
				System.out.println("Connection Closed\n");
				conn = null;
			
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
	
	
	return check;
}
}
/**
 * @param args the command line arguments
 */
