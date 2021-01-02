package ChatLan;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Server {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		serverPanel server_panel = new serverPanel();
		
		Config servidor = new Config("VanitCode   -   Servidor", server_panel);
	}

}


class serverPanel extends JPanelConfig implements Runnable{

	public serverPanel() {
		
		super();
		texto_servidor= new JTextArea();
		scroll_text_cl = new JScrollPane(texto_servidor);
		add(scroll_text_cl,BorderLayout.CENTER);
		
		Thread miHilo = new Thread(this);
		miHilo.start();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		

		try {
			ServerSocket miServidor = new ServerSocket(9999); // Puerto a la escucha
			
			String nick, ip, mensaje;
			ArrayList<String> ipOnlineList = new ArrayList<String>();
			
			EnvioPaqueteDatos paqueteRecibido;
			
			//Abriremos y cerraremos sockets infinitamente para crear siempre la conexión
			//No consume recursos porque al igual que se abren se cierran
			while(true) {
				Socket miSocket = miServidor.accept();//Acepta las conexiones al puerto 9999
				
				ObjectInputStream flujoDatosEntrada = new ObjectInputStream(miSocket.getInputStream());
				
				paqueteRecibido = (EnvioPaqueteDatos) flujoDatosEntrada.readObject();
				
				nick = paqueteRecibido.getNick();
				ip = paqueteRecibido.getIp();
				mensaje = paqueteRecibido.getTextoCliente();
				
				if(!mensaje.equals(" online")) {
					
					texto_servidor.append("\n" + "nick: " + nick + " Mensaje: " + mensaje + " IP: " + ip);
					
					//Vamos a enviar el paquete recibido. Primero el Scket de salida
					Socket reenvioDestinatario= new Socket(ip, 9090);
					//Segundo, el flujo. Un ObjectStream
					ObjectOutputStream paqueteReenvio = new ObjectOutputStream(reenvioDestinatario.getOutputStream());
					//Tercero, escribimos el Objeto que tiene que viajar
					paqueteReenvio.writeObject(paqueteRecibido);
					
					
					reenvioDestinatario.close();
					miSocket.close();
					
			}else {				
					InetAddress dirClients = miSocket.getInetAddress();
					String ipOnlineClients = dirClients.getHostAddress();
					ipOnlineList.add(ipOnlineClients);
					texto_servidor.append(ipOnlineClients + " is online. Alias: " + nick);
					paqueteRecibido.setIPs(ipOnlineList);
					for(String ips: ipOnlineList) {
						
						System.out.println("ArrayList: " + ips);
						Socket reenvioDestinatario= new Socket(ips, 9090);
						//Segundo, el flujo. Un ObjectStream
						ObjectOutputStream paqueteReenvio = new ObjectOutputStream(reenvioDestinatario.getOutputStream());
						//Tercero, escribimos el Objeto que tiene que viajar
						paqueteReenvio.writeObject(paqueteRecibido);
						
						
						reenvioDestinatario.close();
						miSocket.close();
					}
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
	
	private JScrollPane scroll_text_cl;
	private JTextArea texto_servidor;
}