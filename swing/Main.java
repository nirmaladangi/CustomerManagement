package swing;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.*;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Main {
	
	
	public static class ex{
		public static int days=0;
			}

public static void main(String[] args)
	{ 
		login();
		//create();
		 
	}
	public static void login() {
			
			JFrame f=new JFrame("Login");  
			JLabel l1,l2;  
		    l1=new JLabel("Username");  
		    l1.setBounds(300,170, 100,30); 
		    
		    l2=new JLabel("Password");  
		    l2.setBounds(300,210, 100,30); 	
			
			JTextField F_user = new JTextField(); 
			F_user.setBounds(400, 170, 200, 30);
				
			JPasswordField F_pass=new JPasswordField(); 
			F_pass.setBounds(400, 210, 200, 30);
			  
			JButton login_but=new JButton("Login");
			login_but.setBounds(450,250,80,25);   
			login_but.addActionListener(new ActionListener() {  
				
				public void actionPerformed(ActionEvent e){ 

				String username = F_user.getText(); 
				
			     String password = F_pass.getText(); 
				
				if(username.equals("")) 
				{
		            JOptionPane.showMessageDialog(null,"Please enter username");
				} 
				else if(password.equals("")) //If password is null
				{
		            JOptionPane.showMessageDialog(null,"Please enter password"); 
				}
				else { //If both the fields are present then to login the user, check wether the user exists already
					//System.out.println("Login connect");
					Connection connection=connect(); 
					try
					{
					Statement stmt = connection.createStatement();
				      stmt.executeUpdate("USE COMPANY"); 
				      String st = ("SELECT * FROM USERS WHERE USERNAME='"+username+"' AND PASSWORD='"+password+"'"); //Retreive username and passwords from users
				      ResultSet rs = stmt.executeQuery(st); 
				      if(rs.next()==false) { 
				    	  System.out.print("No user");  
				          JOptionPane.showMessageDialog(null,"Wrong Username/Password!"); 
				      }
				      else {
				    	  f.dispose();
				    	rs.beforeFirst();  
				    	while(rs.next())
				    	{
				    	  String admin = rs.getString("ADMIN"); 
				    	  //System.out.println(admin);
				    	  String UID = rs.getString("UID"); 
				    	  if(admin.equals("1")) { 
				    		  admin_menu(); 
				    	  }
				    	  else{
				    		  user_menu(UID); 
				    	  }
				      }
				      }
					}
					catch (Exception ex) {
				         ex.printStackTrace();
				}
				}
			}				
			});

			
			f.add(F_pass);
			f.add(login_but); 
			f.add(F_user);	
		    f.add(l1);  
		    f.add(l2); 
		    
			f.setSize(1000,500);  
			f.setLayout(null);  
			f.setVisible(true); 
			f.setLocationRelativeTo(null);
			
		}
	
	
public static Connection connect()
{
	
      try {
              Class.forName("com.mysql.jdbc.Driver");
              System.out.println("Loaded driver");
              Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/company?user=root&password=root");
              System.out.println("Connected to MySQL");
    	    
    	      return con;
       } 
       catch (Exception ex) {
              ex.printStackTrace();
       }
      return null;
      }


public static void create() {
	try {
	Connection connection=connect();
	System.out.println("connected");
	ResultSet resultSet = connection.getMetaData().getCatalogs();
	//iterate each catalog in the ResultSet
		while (resultSet.next()) {
		  // Get the database name, which is at position 1
		  String databaseName = resultSet.getString(1);
		  if(databaseName.equals("company")) {
			  //System.out.print("yes");
			  Statement stmt = connection.createStatement();
		      //Drop database if it pre-exists to reset the complete database
		      String sql = "DROP DATABASE COMPANY";
		      stmt.executeUpdate(sql);
		  }
		}
		
		
		  Statement stmt = connection.createStatement();
	      
	      String sql = "CREATE DATABASE COMPANY"; //Create Database
	      stmt.executeUpdate(sql); 
	      stmt.executeUpdate("USE COMPANY"); //Use Database
		  //Create Users Table
	      String sql1 = "CREATE TABLE USERS(UID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, USERNAME VARCHAR(30), PASSWORD VARCHAR(30), ADMIN BOOLEAN)";
	      stmt.executeUpdate(sql1);
		  //Insert into users table
	      stmt.executeUpdate("INSERT INTO USERS(USERNAME, PASSWORD, ADMIN) VALUES('admin','admin',TRUE)");
		  //Create Customer table
	      stmt.executeUpdate("CREATE TABLE CUSTOMER(CUSTID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, CUSTNAME VARCHAR(255) NOT NULL, EMAIL VARCHAR(255) NOT NULL UNIQUE, MOBILE_NUMBER VARCHAR(15) NOT NULL, CITY VARCHAR(100) NOT NULL)");
		  
	      stmt.executeUpdate("CREATE TABLE PURCHASEORDER(OID INT AUTO_INCREMENT PRIMARY KEY, PRODUCT_NAME VARCHAR(255) NOT NULL, QUANTITY INT NOT NULL, PRICING DECIMAL(10, 2) NOT NULL,MRP DECIMAL(10, 2) NOT NULL, CUSTID INT, FOREIGN KEY (CUSTID) REFERENCES Customer(CUSTID))");
		  
	      stmt.executeUpdate("CREATE TABLE SHIPPINGDETAILS( SHIPPIN_ID INT AUTO_INCREMENT PRIMARY KEY, ADDRESS VARCHAR(255) NOT NULL, CITY VARCHAR(100) NOT NULL, PINCODE VARCHAR(10) NOT NULL, OID INT, CUSTID INT, FOREIGN KEY (OID) REFERENCES PURCHASEORDER(OID), FOREIGN KEY (CUSTID) REFERENCES Customer(CUSTID))");
	      
	      
	      stmt.executeUpdate("INSERT INTO CUSTOMER(CUSTNAME, EMAIL, MOBILE_NUMBER, CITY) VALUES ('nirmala', 'nirmala@gmail.com', 1235456654,'indore'), ('dhapu', 'dhapu@gmail.com', 5623894100,'RAU'), ('nisha','nisha@gmail.com', 2365498712,'bhopal')");
	      
	           resultSet.close();
	      }
	 catch (Exception ex) {
         ex.printStackTrace();
}
}

public static void user_menu(String UID) {
	
	
	JFrame f=new JFrame("User Functions"); 
    //f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	JButton view_but=new JButton("View Customer");
	view_but.setBounds(20,20,120,25);//x axis, y axis, width, height 
	view_but.addActionListener(new ActionListener() { 
		public void actionPerformed(ActionEvent e){
			
		    JFrame f = new JFrame("customer Available"); 
		    //f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    
		    
			Connection connection = connect();
			String sql="select * from Shippingdetails"; 
	        try {
	        	Statement stmt = connection.createStatement(); 
			     stmt.executeUpdate("USE COMPANY"); 
	            stmt=connection.createStatement();
	            ResultSet rs=stmt.executeQuery(sql);
	            JTable book_list= new JTable(); 
	            book_list.setModel(DbUtils.resultSetToTableModel(rs)); 
	             
	            JScrollPane scrollPane = new JScrollPane(book_list); 

	            f.add(scrollPane); 
	            f.setSize(800, 400); 
	            f.setVisible(true);
	        	f.setLocationRelativeTo(null);
	        } catch (SQLException e1) {
	            // TODO Auto-generated catch block
	             JOptionPane.showMessageDialog(null, e1);
	        }   			
			
	}
	}
	);
	
	JButton my_cust=new JButton("My customer"); 
	my_cust.setBounds(150,20,120,25);//x axis, y axis, width, height 
	my_cust.addActionListener(new ActionListener() { 
		private String CUSTID;

		public void actionPerformed(ActionEvent e){
			
			  
			JFrame f = new JFrame("My customer"); 
		    
			int CUSTID_int = Integer.parseInt(CUSTID); 

		   
			Connection connection = connect(); //connect to database
		
			String sql="select distinct issued.*,books.custname,books.,bookemails.mobilenumber from issued,books " + "where ((issued.uid=" + CUSTID_int + ") and (books.bid in (select bid from issued where issued.uid="+CUSTID_int+"))) group by iid";
			String sql1 = "select bid from issued where uid="+CUSTID_int;
	        try {
	        	Statement stmt = connection.createStatement();
				//use database
			     stmt.executeUpdate("USE COMPANY");
	            stmt=connection.createStatement();
				//store in array
	            ArrayList books_list = new ArrayList<>();
 
	        
	            ResultSet rs=stmt.executeQuery(sql);
	            JTable book_list= new JTable(); 
	            book_list.setModel(DbUtils.resultSetToTableModel(rs)); 
	            //enable scroll bar
	            JScrollPane scrollPane = new JScrollPane(book_list);

	            f.add(scrollPane); 
	            f.setSize(800, 400); 
	            f.setVisible(true);
	        	f.setLocationRelativeTo(null);
	        } catch (SQLException e1) {
	            // TODO Auto-generated catch block
	             JOptionPane.showMessageDialog(null, e1);
	        }   			
				
	}
	}
	);
	
	
	
	f.add(my_cust); 
	f.add(view_but); 
	f.setSize(300,100);//400 width and 500 height  
	f.setLayout(null);//using no layout managers  
	f.setVisible(true);//making the frame visible 
	f.setLocationRelativeTo(null);
	}



public static void admin_menu() {
	
	
	JFrame f=new JFrame("Admin Functions"); 
    //f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    
    
    JButton create_but=new JButton("Create/Reset");
	create_but.setBounds(610,220,120,25);//x axis, y axis, width, height 
	create_but.addActionListener(new ActionListener() { 
		public void actionPerformed(ActionEvent e){
			
			create(); //Call create function
			JOptionPane.showMessageDialog(null,"Database Created/Reset!"); 
			
		}
	});
	
	JButton view_ship=new JButton("View shippingdetails");//creating instance of JButton to view books
	view_ship.setBounds(750,220,120,25);//x axis, y axis, width, height 
	view_ship.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			
		    JFrame f = new JFrame("shipping Available"); 
		    //f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    
		    
			Connection connection = connect(); //connect to database
			String sql="select * from shippingdetails"; 
	        try {
	        	Statement stmt = connection.createStatement();
			     stmt.executeUpdate("USE COMPANY"); //use database
	            stmt=connection.createStatement();
	            ResultSet rs=stmt.executeQuery(sql);
	            JTable cust_list= new JTable(); //view data in table format
	            cust_list.setModel(DbUtils.resultSetToTableModel(rs)); 
	            //mention scroll bar
	            JScrollPane scrollPane = new JScrollPane(cust_list); 

	            f.add(scrollPane); 
	            f.setSize(800, 400); 
	            f.setVisible(true);
	        	f.setLocationRelativeTo(null);
	        } catch (SQLException e1) {
	            // TODO Auto-generated catch block
	             JOptionPane.showMessageDialog(null, e1);
	        }   			
			
	}
	}
	);
	
    
	JButton view_cust=new JButton("View customers");//creating instance of JButton to view books
	view_cust.setBounds(180,180,120,25);//x axis, y axis, width, height 
	view_cust.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			
		    JFrame f = new JFrame("customer Available"); 
		    //f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    
		    
			Connection connection = connect(); //connect to database
			String sql="select * from customer"; //select all books 
	        try {
	        	Statement stmt = connection.createStatement();
			     stmt.executeUpdate("USE COMPANY"); //use database
	            stmt=connection.createStatement();
	            ResultSet rs=stmt.executeQuery(sql);
	            JTable cust_list= new JTable(); //view data in table format
	            cust_list.setModel(DbUtils.resultSetToTableModel(rs)); 
	            //mention scroll bar
	            JScrollPane scrollPane = new JScrollPane(cust_list); 

	            f.add(scrollPane); 
	            f.setSize(800, 400); 
	            f.setVisible(true);
	        	f.setLocationRelativeTo(null);
	        } catch (SQLException e1) {
	            // TODO Auto-generated catch block
	             JOptionPane.showMessageDialog(null, e1);
	        }   			
			
	}
	}
	);
	
	JButton users_but=new JButton("View Users");
	users_but.setBounds(310,180,120,25);//x axis, y axis, width, height 
	users_but.addActionListener(new ActionListener() { 
		public void actionPerformed(ActionEvent e){
				
			    JFrame f = new JFrame("Users List");
			    //f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    
			    
				Connection connection = connect();
				String sql="select * from users"; 
		        try {
		        	Statement stmt = connection.createStatement();
				     stmt.executeUpdate("USE COMPANY"); 
		            stmt=connection.createStatement();
		            ResultSet rs=stmt.executeQuery(sql);
		            JTable book_list= new JTable();
		            book_list.setModel(DbUtils.resultSetToTableModel(rs)); 
		            //mention scroll bar
		            JScrollPane scrollPane = new JScrollPane(book_list);

		            f.add(scrollPane); 
		            f.setSize(800, 400); 
		            f.setVisible(true);
		        	f.setLocationRelativeTo(null);
		        } catch (SQLException e1) {
		            // TODO Auto-generated catch block
		             JOptionPane.showMessageDialog(null, e1);
		        }   	
	 			
				
	}
		}
	);	
	
	JButton view_purchase=new JButton("View purchaseOrder");
	view_purchase.setBounds(440,180,160,25);//x axis, y axis, width, height 
	view_purchase.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
				
			    JFrame f = new JFrame("Users List");
			    //f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    
			    
				Connection connection = connect();
				String sql="select * from purchaseorder";
				
			

		        try {
		        	Statement stmt = connection.createStatement();
				     stmt.executeUpdate("USE COMPANY");
		            stmt=connection.createStatement();
		            ResultSet rs=stmt.executeQuery(sql);
		            JTable book_list= new JTable();
		            book_list.setModel(DbUtils.resultSetToTableModel(rs)); 
		            
		            JScrollPane scrollPane = new JScrollPane(book_list);

		            f.add(scrollPane);
		            f.setSize(800, 400);
		            f.setVisible(true);
		        	f.setLocationRelativeTo(null);
		           } 
		        catch (SQLException e1) {
		            // TODO Auto-generated catch block
		             JOptionPane.showMessageDialog(null, e1);
	                 }   	
	 						
            }
		}
	);
	
	
	JButton add_user=new JButton("Add User"); 
	add_user.setBounds(180,220,120,25); 
	
	add_user.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
				
			    JFrame g = new JFrame("Enter User Details"); 
			    //g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    //Create label 
			    JLabel l1,l2;  
			    l1=new JLabel("Username");  
			    l1.setBounds(30,15, 100,30); 
			    
			    
			    l2=new JLabel("Password");  
			    l2.setBounds(30,50, 100,30); 
				
				
				JTextField F_user = new JTextField();
				F_user.setBounds(110, 15, 200, 30);
				
				
				JPasswordField F_pass=new JPasswordField();
				F_pass.setBounds(110, 50, 200, 30);
				
				JRadioButton a1 = new JRadioButton("Admin");
				a1.setBounds(55, 80, 200,30);
				
				JRadioButton a2 = new JRadioButton("User");
				a2.setBounds(130, 80, 200,30);
				
				ButtonGroup bg=new ButtonGroup();    
				bg.add(a1);bg.add(a2);  
				
								
				JButton create_but=new JButton("Create");			
				create_but.setBounds(130,130,80,25);//x axis, y axis, width, height 
				create_but.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e){
					
					String username = F_user.getText();
					String password = F_pass.getText();
					Boolean admin = false;
					
					if(a1.isSelected()) {
						admin=true;
					}
					
					Connection connection = connect();
					
					try {
					Statement stmt = connection.createStatement();
				     stmt.executeUpdate("USE COMPANY");
				     stmt.executeUpdate("INSERT INTO USERS(USERNAME,PASSWORD,ADMIN) VALUES ('"+username+"','"+password+"',"+admin+")");
		             JOptionPane.showMessageDialog(null,"User added!");
		             g.dispose();
		             
					}
					
					catch (SQLException e1) {
			            // TODO Auto-generated catch block
			             JOptionPane.showMessageDialog(null, e1);
			        }   
					
					}
					
				});
					
				
					g.add(create_but);
					g.add(a2);
					g.add(a1);
					g.add(l1);
					g.add(l2);
					g.add(F_user);
					g.add(F_pass);
					g.setSize(350,200);//400 width and 500 height  
					g.setLayout(null); 
					g.setVisible(true);
					g.setLocationRelativeTo(null);
				
			    
	}
	});
		
	
	JButton add_cust=new JButton("Customer Management"); 
	add_cust.setBounds(310,220,120,25); 
	
	add_cust.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
				
			    JFrame g = new JFrame("Add customer details including");
			    //g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    
			    JLabel l1,l2,l3;  
			    l1=new JLabel("Customer Name");  
			    l1.setBounds(30,15, 100,30); 
			    
			    
			    l2=new JLabel("Email");  
			    l2.setBounds(30,53, 100,30); 
			    
			    l3=new JLabel("Mobile Number"); 
			    l3.setBounds(30,110, 100,30); 
			    
			    JLabel l4 = new JLabel("City");  
			    l4.setBounds(30,160, 100,30); 
				
				
				JTextField F_custname = new JTextField();
				F_custname.setBounds(130, 15, 200, 30);
				
				 
				JTextField F_email=new JTextField();
				F_email.setBounds(130, 53, 200, 30);
				
				JTextField F_mobilenumber=new JTextField();
				F_mobilenumber.setBounds(130, 110, 200, 30);
				
				JTextField F_city=new JTextField();
				F_city.setBounds(130, 160, 200, 30);
						
				
				JButton create_but=new JButton("Submit");
				create_but.setBounds(150,220,80,25);//x axis, y axis, width, height 
				create_but.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e){
					// assign the book name, genre, price
					String custname = F_custname.getText();
					String email = F_email.getText();
					String mobilenumber = F_mobilenumber.getText();
					String city = F_city.getText();
					
				
					
					Connection connection = connect();
					
					try {
					Statement stmt = connection.createStatement();
				     stmt.executeUpdate("USE COMPANY");
				     stmt.executeUpdate("INSERT INTO CUSTOMER(CUSTNAME,EMAIL,MOBILE_NUMBER,CITY) VALUES ('"+custname+"','"+email+"','"+mobilenumber+"','"+city+"')");
		             JOptionPane.showMessageDialog(null,"Customer Details added!");
		             g.dispose();
		             
					}
					
					catch (SQLException e1) {
			            // TODO Auto-generated catch block
			             JOptionPane.showMessageDialog(null, e1);
			        }   
					
					}
					
				});
								
					g.add(l3);
					g.add(l4);
					g.add(create_but);
					g.add(l1);
					g.add(l2);
					g.add(F_custname);
					g.add(F_email);
					g.add(F_mobilenumber);
					g.add(F_city);
					g.setSize(500,400);//400 width and 500 height  
					g.setLayout(null); 
					g.setVisible(true);
					g.setLocationRelativeTo(null);
						    
	}
	});
	
	
	JButton purchase=new JButton("Purchase Order"); 
	purchase.setBounds(610,180,120,25); 
	
	purchase.addActionListener(new ActionListener() {
		private JLabel l5;
		private JLabel l6;

		public void actionPerformed(ActionEvent e){
				
			    JFrame g = new JFrame("Enter Details");
			    //g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    
			    JLabel l1,l2,l3,l4;  
			    l1=new JLabel("Product Name"); 
			    l1.setBounds(30,15, 100,30); 
			    
			    
			    l2=new JLabel("Quantity");  
			    l2.setBounds(30,53, 100,30); 
			    
			    l3=new JLabel("Pricing");  
			    l3.setBounds(30,90, 100,30); 
			    
			    l4=new JLabel("MRP");  
			    l4.setBounds(30,130, 150,30); 
			    
			     l5= new JLabel("Custid");  
			     l5.setBounds(30,170, 150,30); 
				
				JTextField F_pname = new JTextField();
				F_pname.setBounds(110, 15, 200, 30);
				
				
				JTextField F_quantity=new JTextField();
				F_quantity.setBounds(110, 53, 200, 30);
				
				JTextField F_pricing=new JTextField();
				F_pricing.setBounds(110, 90, 200, 30);
				
				JTextField F_mrp=new JTextField();
				F_mrp.setBounds(110, 130, 200, 30);	
			
				JTextField F_custid=new JTextField();
				F_custid.setBounds(110, 170, 200, 30);	
				
				JButton create_but=new JButton("Submit");  
				create_but.setBounds(130,250,80,25);//x axis, y axis, width, height 
				create_but.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e){
					
					String pname = F_pname.getText();
					String quantity = F_quantity.getText();
					String pricing = F_pricing.getText();
					String mrp = F_mrp.getText();
					String custid = F_custid.getText();
					
					

					
					
					Connection connection = connect();
					
					try {
					Statement stmt = connection.createStatement();
				     stmt.executeUpdate("USE COMPANY");
				     stmt.executeUpdate("INSERT INTO PURCHASEORDER(PRODUCT_NAME,QUANTITY,PRICING,MRP,CUSTID) VALUES ('"+pname+"','"+quantity+"','"+pricing+"','"+mrp+"','"+custid+"')");
		             JOptionPane.showMessageDialog(null,"insert purchaseorder !");
		             g.dispose();
		             
					}
					
					catch (SQLException e1) {
			            // TODO Auto-generated catch block
			             JOptionPane.showMessageDialog(null, e1);
			        }   
					
					}
					
				});
					
				
					g.add(l3);
					g.add(l4);
					g.add(l5);
					g.add(create_but);
					g.add(l1);
					g.add(l2);
					g.add(F_pname);
					g.add(F_quantity);
					g.add(F_pricing);
					g.add(F_mrp);
					g.add(F_custid);
					g.setSize(500,400);//400 width and 500 height  
					g.setLayout(null);  
					g.setVisible(true); 
					g.setLocationRelativeTo(null);
				
			    
	}
	});
	
	
	JButton shipping_details=new JButton("Shipping details"); 
	shipping_details.setBounds(440,220,160,25); 
	
	shipping_details.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
				
			    JFrame g = new JFrame("Add shipping");
			    //g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    
			    //set labels 
			    JLabel l1,l2,l3,l4,l5;  
			    l1=new JLabel("Enter Address");  
			    l1.setBounds(30,15, 100,30); 
			   
			    
			    l4=new JLabel("City");  
			    l4.setBounds(30,50, 150,30); 
			    
			    l2=new JLabel("Pincode");  
			    l2.setBounds(30,90, 150,30); 
			    
			    l3=new JLabel("Purchaseorderid");  
			    l3.setBounds(30,130, 150,30); 
			    
			    l5=new JLabel("Customerid");  
			    l5.setBounds(30,180, 150,30); 
				
				JTextField F_address = new JTextField();
				F_address.setBounds(110, 15, 200, 30);
				
				
				JTextField F_city=new JTextField();
				F_city.setBounds(110, 50, 130, 30);
			    

				JTextField F_pincode=new JTextField();
				F_pincode.setBounds(110, 90, 130, 30);
				
				JTextField F_purchaseorderid=new JTextField();
				F_purchaseorderid.setBounds(110, 130, 130, 30);
				
				JTextField F_customerid=new JTextField();
				F_customerid.setBounds(110, 180, 130, 30);
			

				JButton create_but=new JButton("Submit");
				create_but.setBounds(130,250,80,25);//x axis, y axis, width, height 
				create_but.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e){					
					
					String address = F_address.getText();
					String city = F_city.getText();
					String pincode = F_pincode.getText();
					String purchaseorderid = F_purchaseorderid.getText();
					String customerid = F_customerid.getText();		
					
					
					Connection connection = connect();
					
					
					try {
						Statement stmt = connection.createStatement();
					     stmt.executeUpdate("USE COMPANY");
					     stmt.executeUpdate("INSERT INTO SHIPPINGDETAILS(ADDRESS,CITY,PINCODE,OID,CUSTID) VALUES ('"+address+"','"+city+"','"+pincode+"','"+purchaseorderid+"','"+customerid+"')");
			             JOptionPane.showMessageDialog(null,"insert ShippingDetails!");
			             g.dispose();
			             
						}
						
						catch (SQLException e1) {
				            // TODO Auto-generated catch block
				             JOptionPane.showMessageDialog(null, e1);
				        }   
						
						}
					
				});	
					g.add(l4);
					g.add(l2);
					g.add(l3);
					g.add(l5);
					g.add(create_but);
					g.add(l1);
					g.add(F_address);
					g.add(F_city);
					g.add(F_pincode);
					g.add(F_purchaseorderid);
					g.add(F_customerid);
					g.setSize(500,400);//400 width and 500 height  
					g.setLayout(null); 
					g.setVisible(true);
					g.setLocationRelativeTo(null);			    
	}
	});
	
	f.add(create_but);
	f.add(shipping_details);
	f.add(purchase);
	f.add(add_cust);
	f.add(view_purchase);
	f.add(users_but);
	f.add(view_cust);
	f.add(view_ship);
	f.add(add_user);
	f.setSize(1000,500);//400 width and 500 height  
	f.setLayout(null);//using no layout managers  
	f.setVisible(true);//making the frame visible 
	f.setLocationRelativeTo(null);
	
	}
}

