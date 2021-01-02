package ChatLan;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String nick = JOptionPane.showInputDialog("Introduzca su nick");
		clientPanel client_panel = new clientPanel(nick);
		
		Config cliente = new Config("VanitCode   -   Cliente", client_panel);
		
		cliente.connected(nick);
	}

}

class clientPanel extends JPanelConfig implements Runnable{
	
	public clientPanel(String nick) {
		super();
				
				JPanel central_panel = new JPanel();
				JPanel north_panel = new JPanel();
				nickPanel = new JLabel();
				nickPanel.setText(nick);
				ip = new JComboBox();
				JLabel et1 = new JLabel("  ONLINE -> ");
				JTextArea texto_cliente = new JTextArea(4,24);
				JScrollPane scroll_text_cl = new JScrollPane(texto_cliente);
				JButton enviar = new JButton("Send");
				
				areaChat = new JTextArea(10,30);
				areaChat.setEditable(false);
				enviar.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						
						try {
							Socket miSocket = new Socket("192.168.0.119",9999); //Host,port
							
							EnvioPaqueteDatos datos = new EnvioPaqueteDatos();
							datos.setNick(nickPanel.getText());
							datos.setIp(ip.getSelectedItem().toString());
							datos.setTextoCliente(texto_cliente.getText());
							
							//Lo siguiente se puede hacer porque es Serializable
							ObjectOutputStream flujoSalidaPaquete = new ObjectOutputStream(miSocket.getOutputStream());
							flujoSalidaPaquete.writeObject(datos);
							
							miSocket.close();
							
							//Creación del flujo de datos
							/*
							DataOutputStream flujoSalida = new DataOutputStream(miSocket.getOutputStream());
							flujoSalida.writeUTF(texto_cliente.getText());
							flujoSalida.close();*/
						}catch (UnknownHostException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
				});
				
				north_panel.add(nickPanel);
				north_panel.add(et1);
				north_panel.add(ip);
				central_panel.add(areaChat);
				central_panel.add(scroll_text_cl);
				central_panel.add(enviar);
				
				add(north_panel, BorderLayout.NORTH);
				add(central_panel, BorderLayout.CENTER);
				
				Thread miHilo = new Thread(this);
				miHilo.start(); //llama al método run y ya siempre se hace lo de run
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ServerSocket escuchaCliente = new ServerSocket(9090);
			Socket cliente;
			EnvioPaqueteDatos paqueteRecibido;
			
			while(true) {
				cliente = escuchaCliente.accept();
				ObjectInputStream flujoEntrada = new ObjectInputStream (cliente.getInputStream());
				paqueteRecibido = (EnvioPaqueteDatos) flujoEntrada.readObject();
				
				if(paqueteRecibido.getTextoCliente().equals(" online")) {
					ArrayList<String> IPsJCombo = new ArrayList<String>();
					IPsJCombo = paqueteRecibido.getIPs();
					ip.removeAllItems();
					
					for(String ips: IPsJCombo) ip.addItem(ips);
				}
				else{
					areaChat.append("\n" + paqueteRecibido.getNick() + ": " + paqueteRecibido.getTextoCliente());
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private JComboBox ip;
	private JLabel nickPanel;
	private JTextArea areaChat;
}