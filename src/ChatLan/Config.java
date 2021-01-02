package ChatLan;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Config {

	public Config(String title, JPanel panel) {
		
		FrameConfig frame = new FrameConfig();
		frame.setTitle(title);
		frame.setBounds(300, 200, 800, 450);
		frame.setDefaultCloseOperation(3);
		frame.setResizable(false);
		frame.add(panel);
		frame.setVisible(true);
	}
	
	void connected(String nick) {
		try {
			Socket miSocket = new Socket("192.168.0.119",9999);
			EnvioPaqueteDatos datos = new EnvioPaqueteDatos();
			datos.setTextoCliente(" online");
			datos.setNick(nick);
			ObjectOutputStream flujoSalidaPaquete = new ObjectOutputStream(miSocket.getOutputStream());
			flujoSalidaPaquete.writeObject(datos);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class FrameConfig extends JFrame{
	
	public FrameConfig() {
		Toolkit miToolkit = Toolkit.getDefaultToolkit();
		Image logo = miToolkit.getImage("icons\\vanitcode.png");
		setIconImage(logo);
	}
}

class JPanelConfig extends JPanel{
	
	public JPanelConfig() {
		setLayout(new BorderLayout());
		JLabel imagenFooter = new JLabel();
		imagenFooter.setIcon(new ImageIcon("icons\\vanitcode.png"));
		add(imagenFooter, BorderLayout.SOUTH);
	}
}

class EnvioPaqueteDatos implements Serializable{ //Para que sea susceptible de serializarse y poder enviar el paquete
	
	private String nick, ip, textoCliente;
	private ArrayList<String> IPs;

	public ArrayList<String> getIPs() {
		return IPs;
	}

	public void setIPs(ArrayList<String> iPs) {
		IPs = iPs;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getTextoCliente() {
		return textoCliente;
	}

	public void setTextoCliente(String textoCliente) {
		this.textoCliente = textoCliente;
	}
	
	
}