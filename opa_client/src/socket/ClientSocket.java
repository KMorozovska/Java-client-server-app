package socket;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import socket.Protocol;

public class ClientSocket implements Runnable {

	/**
	 * Gniazdo serwera
	 */
	private Socket socket;

	/**
	 * Buforowany czytnik strumienia znakow przychodzacego od serwera
	 */
	private BufferedReader input;
	/**
	 * Obiekt obslugujacy wysylanie strumien znakow do serwera
	 */
	private PrintWriter output;

	/**
	 * Czytnik strumienia danych
	 */
	private FileInputStream fis;
	/**
	 * Buforowany czytnik strumienia danych przychodzacych z serwera
	 */
	private BufferedInputStream bis;

	/**
	 * Strumien danych do pliku od serwera
	 */
	private FileOutputStream fos;

	private Integer port;

	private String host;

	public ClientSocket(String host2, Integer port2) {

		host = host2;
		port = port2;
	}

	@Override
	public void run() {
		while (true)
			try {
				String command = receive();
				
				System.out.println("dzia³a run ClientSocket");
				
				if (!handleCommand(command)) {
					getSocket().close();
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

	}

	/**
	 * Metoda sluzaca do odebrania komendy od serwera
	 * 
	 * @return odebrana komenda
	 */
	public String receive() {
		if (input != null) {
			try {
				return input.readLine();
			} catch (IOException e) {
				System.out.println("Error reading serwer.");
			}
		}
		return Protocol.NULLCOMMAND;
	}

	private boolean handleCommand(String command) throws IOException {

		StringTokenizer st = new StringTokenizer(command);
		String cd = st.nextToken();

		if (cd.equals(Protocol.SENDINGFILE)) {
			//String filePath = st.nextToken();
			String fileName = st.nextToken();
			long fileSize = Long.parseLong(st.nextToken());
			System.out.println("Otrzymale info o pliku: " + ", "+ fileName + ", " + fileSize);
			sendFileToServer(fileName, fileSize, this.getSocket().getOutputStream());
		}

		return true;
	}

	/**
	 * Inicjacja strumieni odczytu i wysylania danych oraz rozpoczecie
	 * polaczenia z serwerems
	 * 
	 * @param host
	 *            adres serwera
	 * @param port
	 *            numer portu przez ktory klient probuje sie polaczyc z serwerem
	 * @throws IOException
	 *             rzucany wyjatek
	 */
	public synchronized void init() throws IOException {
		
		socket = new Socket(host, port);
		System.out.println("Sprawdzenie czy klient jest po³¹czony z serwerem : " + socket.isConnected());
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true);
		new Thread(this).start();
	}

	/**
	 * Funkcja, ktora umieszcza w strumieniu informacje na temat ¿¹dania klienta
	 * 
	 * @param command
	 * @throws IOException
	 */
	public void send(String command) throws IOException {
		if (output != null) {
			System.out.println("Wiadomosc do serwera: " + command);
			handleCommand(command);
			output.println(command);
		} else {
			JOptionPane.showMessageDialog(null, "Brak po³¹czenia z serwerem", "Uwaga", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void sendFileToServer(String filePath, long fileSize, OutputStream os) {
    	new Thread(new Runnable() {
    	    public void run() {
				byte[] bytes = new byte[64 * 1024];
				File file = new File(filePath);
				
				System.out.println("Wysylanie pliku o rozmiarze " + fileSize);

				try {
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
							
					long bytesRead = 0;
				    long totalRead=0;
				  
				    while(totalRead != fileSize){
				    	bytesRead = bis.read(bytes,0,bytes.length);
				        totalRead += bytesRead;
				       
				        os.write(bytes,0, (int)bytesRead);
				        os.flush();
					}
				    
				    System.out.println("Wysylanie zakonczone!");
				    			    
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e){
					e.printStackTrace();
				} finally {
					try {
						fis.close();
					    bis.close();					    
					} catch (IOException e){
						e.printStackTrace();
					}				    
				    
				}
    	    }
    	}).start();  	
    }

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

}
