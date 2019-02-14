package socket;


import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JFrame;

public class MyServer extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5886420253359037638L;

	/**
	 * Gniazdo serwera
	 */
	private ServerSocket serverSocket;

	private Properties properties;


	private Vector<Service> clients = new Vector<Service>();

	public MyServer(Properties p, String title) {
		super(title);
		properties = p;

		int port = Integer.parseInt(properties.getProperty("port"));

		try {
			serverSocket = new ServerSocket(port);
		} catch (Exception e) {
			System.err.println("Create server socket: " + e);
			return;
		}

		new Thread(this).start();
		pack();
		setSize(400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	@Override
	public void run() {
		while (true)
			try {
				Socket clientSocket = serverSocket.accept();
				System.out.println("bylo accept");
				Service clientService = new Service(clientSocket, this);
				addClientService(clientSocket, clientService);
				
			} catch (IOException e) {
				System.err.println("Error accepting connection. " + "Client will not be served...");
			}
	}

	synchronized void addClientService(Socket clientSocket, Service clientService) throws IOException {
		
		System.out.println("dziala add client service");
		
		clientService.init();
		clients.addElement(clientService);
		new Thread(clientService).start();
		
		//System.out.println("Add. " + clients.size());
	}

	public static void main(String args[]) {
		Properties p = new Properties();
		String pName = "IBServer.properties";
		try {
			p.load(new FileInputStream(pName));
		} catch (Exception e) {
			p.put("port", "40000");
			p.put("width", "250");
			p.put("height", "250");
		}
		try {
			p.store(new FileOutputStream(pName), null);
		} catch (Exception e) {
		}
		new MyServer(p, "Internet Board Server");
	}

	synchronized void removeClientService(Service clientService) {
        clients.removeElement(clientService);
        clientService.close();
    }
}
