package JDBCAPP;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.*;


public class Config {

	public Config(String title) {
		
		JFrameConfig frame = new JFrameConfig();
		frame.setTitle(title);
		frame.setBounds(550, 200, 800, 650);
		frame.setDefaultCloseOperation(3);
		frame.setResizable(false);
		frame.add(new JPanelConfig());
		frame.setVisible(true);
	}
}

class JPanelConfig extends JPanel{
	
	public JPanelConfig() {
		
		//------------Structure-------------------------------
		setLayout(new BorderLayout());
		
		//Boton inferior
		imagenFooter = new JButton();
		imagenFooter.setIcon(new ImageIcon("icons\\vanitcode.png"));
		imagenFooter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				runQuery();
			}
			
		});
		
		//Central panel
		panel_central = new JPanel();
		central = new JTextArea(20,50);
		central.setEditable(false);
		central_scroll = new JScrollPane(central);
		
		//Selectors
		seccion = new JComboBox<String>();
		pais = new JComboBox<String>();
		north_panel = new JPanel();
		seccion.addItem("Select a section");
		pais.addItem("Select a country");
		
		//----------Connection with BBDD----------------------------------
		BD = JOptionPane.showInputDialog("Enter the name of the Data Base");
		port = JOptionPane.showInputDialog("Enter the connection port with the local database");
		user = JOptionPane.showInputDialog("Enter your user");
		pass = JOptionPane.showInputDialog("Enter your password");
		try {
			miCon = DriverManager.getConnection("jdbc:mysql://localhost:" + port +"/" + BD + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",user,pass);
			System.out.println("Connection to the database successful");
			
			//seccion JComboBox
			Statement sentencia = miCon.createStatement();
			String query="SELECT DISTINCTROW SECCION FROM articulos";
			ResultSet RS=sentencia.executeQuery(query);
			
			while(RS.next()){
				
				seccion.addItem(RS.getString(1));
			}
			RS.close();
			
			//pais JComboBox
			query ="SELECT DISTINCTROW PAIS_ORIGEN FROM articulos";
			RS = sentencia.executeQuery(query);
			while(RS.next()){
				
				pais.addItem(RS.getString(1));
			}
			RS.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//----------ADDs-------------------------------------------------
		panel_central.add(central_scroll);
		north_panel.add(seccion);
		north_panel.add(pais);
		add(imagenFooter, BorderLayout.SOUTH);
		add(panel_central, BorderLayout.CENTER);
		add(north_panel, BorderLayout.NORTH);
		
	}
	
	private void runQuery() {
		
		ResultSet rs = null;
		
		try {
			central.setText("");
			String sec = (String) seccion.getSelectedItem();
			String country = (String) pais.getSelectedItem();
			if(!sec.equals("Select a section") && country.equals("Select a country")){				
				
				sendsectionQuery=miCon.prepareStatement(sectionQuery);
				sendsectionQuery.setString(1, sec);
				rs=sendsectionQuery.executeQuery();
					
			}else if(sec.equals("Select a section") && !country.equals("Select a country")){
				
				sendcountryQuery=miCon.prepareStatement(countryQuery);
				sendcountryQuery.setString(1, country);
				rs=sendcountryQuery.executeQuery();
			}else if(!sec.equals("Select a section") && !country.equals("Select a country")){
				
				sendgeneralQuery=miCon.prepareStatement(generalQuery);
				sendgeneralQuery.setString(1, sec);
				sendgeneralQuery.setString(2, country);
				rs=sendgeneralQuery.executeQuery();	
			}else {
				sendallQuery=miCon.createStatement();
				rs = sendallQuery.executeQuery(allQuery);
			}
			while(rs.next()){
				
				central.append(rs.getString(1));
				central.append(", ");
				central.append(rs.getString(2));
				central.append(", ");
				central.append(rs.getString(3));
				central.append(", ");
				central.append(rs.getString(4));
				central.append("\n");
			}
			rs.close();
		} catch (Exception e){
			System.out.println("An error occurred with the query");
		}
	}
	
	private JButton imagenFooter;
	private JPanel panel_central, north_panel;
	private JComboBox<String> seccion, pais;
	private JTextArea central;
	private JScrollPane central_scroll;
	private String port, BD, user, pass, table;
	private Connection miCon;
	private PreparedStatement sendsectionQuery;
	private PreparedStatement sendcountryQuery;
	private PreparedStatement sendgeneralQuery;
	private Statement sendallQuery;
	private final String sectionQuery="SELECT NOMBRE_ARTICULO, SECCION, PRECIO, PAIS_ORIGEN FROM articulos WHERE SECCION=?";
	private final String countryQuery="SELECT NOMBRE_ARTICULO, SECCION, PRECIO, PAIS_ORIGEN FROM articulos WHERE PAIS_ORIGEN=?";
	private final String allQuery="SELECT NOMBRE_ARTICULO, SECCION, PRECIO, PAIS_ORIGEN FROM articulos";
	private final String generalQuery="SELECT NOMBRE_ARTICULO, SECCION, PRECIO, PAIS_ORIGEN FROM articulos WHERE SECCION=? AND"
			+ " PAIS_ORIGEN=?";
}

class JFrameConfig extends JFrame{
	
	public JFrameConfig() {
		Toolkit miToolkit = Toolkit.getDefaultToolkit();
		Image logo = miToolkit.getImage("icons\\vanitcode.png");
		setIconImage(logo);
	}
}