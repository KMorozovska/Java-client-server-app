package socket;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.util.StringTokenizer;


public class Service implements Runnable {

	private MyServer server;

	private Socket clientSocket;

	private BufferedReader input;

	private PrintWriter output;

	/**
	 * Folder danego uzytkownika
	 */
	private File clientDirectory;

	/**
	 * Sciezka do folderu zawierajacego pliki danego uzytkownika
	 */
	private String clientDirPath = "./data";

	/**
	 * Sciezka do pliku pobieranego od klienta
	 */
	private String downloadFilePath;

	/**
	 * Strumien danych do pliku od klienta
	 */
	private FileOutputStream fos;
	/**
	 * Strumien danych od klienta
	 */
	private DataOutputStream dos;

	public Service(Socket clientSocket, MyServer server) {
		this.server = server;
		this.clientSocket = clientSocket;
	}

	void init() throws IOException {
		Reader reader = new InputStreamReader(clientSocket.getInputStream());
		input = new BufferedReader(reader);
		output = new PrintWriter(clientSocket.getOutputStream(), true);
	}

	@Override
	public void run() {

		while (true) {

			String request = receive();
			System.out.println("Wiadomosc od klienta: " + request);
			StringTokenizer st = new StringTokenizer(request);
			String command = st.nextToken();

			/*
			 * W zaleznosci od otrzymanego zapytania od klienta wykonywana jest
			 * dana operacja
			 */

			if (command.equals("sendingfile")) {
				String fileName = st.nextToken();
				long fileSize = Long.parseLong(st.nextToken());
				try {
					waitingForFiles(fileName, fileSize);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		//server.removeClientService(this);
	}

	public String receive() {
		if (input != null) {
			try {
				return input.readLine();
			} catch (IOException e) {
				System.out.println("Error reading from client: " + e);
				;
			}
		}
		return null;
	}

	public void waitingForFiles(String fileName, long fileSize) throws IOException {

		System.out.println("wlaczylo sie waiting for files");
		try {
			downloadFilePath = clientDirPath + fileName;
			byte[] bytes = new byte[64 * 1024];
			InputStream is = clientSocket.getInputStream();

			fos = new FileOutputStream(downloadFilePath);
			dos = new DataOutputStream(fos);
			long bytesRead = 0;
			long totalRead = 0;
			System.out.println("pobieranie pliku o rozmiarze: " + fileSize);

			while (totalRead != fileSize) {
				System.out.println("zaczynam czytac. totalRead:  " + totalRead);
				System.out.println("zaczynam czytac. bytesRead:  " + bytesRead);
				System.out.println("to sie dzieje raz");
				bytesRead = is.read(bytes, 0, bytes.length);
				System.out.println("to sie nie dzieje wcale");
				
				totalRead += bytesRead;

				dos.write(bytes, 0, (int) bytesRead);
				dos.flush();
			}
			System.out.println("File " + fileName + " downloaded (" + totalRead + " bytes read)");
			// clientFiles = clientDirectory.listFiles();
			// send(Protocol.FILEARCHIVIZED);

		} catch (IOException e) {
			System.out.println("Exception: " + e);
			File file = new File(downloadFilePath);
			file.delete();
		} catch (Exception e) {
			System.out.println("Exception: " + e);
			File file = new File(downloadFilePath);
			file.delete();
		} finally {
			try {
				fos.close();
				dos.close();
			} catch (IOException e) {
				System.out.println("Exception: " + e);
			}
		}

	}

	public void close() {
		try {
			output.close();
			input.close();
			clientSocket.close();
		} catch (IOException e) {
			System.out.println("Error closing connection: " + e);
			;
		} finally {
			output = null;
			input = null;
			clientSocket = null;
		}
	}

}
