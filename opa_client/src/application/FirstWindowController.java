package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import socket.Protocol;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import socket.ClientSocket;

public class FirstWindowController implements Initializable {

	@FXML
	private Button myButton;

	@FXML
	private TextField myTextField1;

	@FXML
	private TextField myTextField2;

	@FXML
	private Button sendToServerButton;

	@FXML
	private Button deleteFromServerButton;

	@FXML
	private Button copyFromServerButton;

	@FXML
	private TableView<HistoryOfEvents> clientHistoryArea = new TableView<HistoryOfEvents>();

	private TableColumn fileNameColumn;

	private TableColumn filePathColumn;

	private TableColumn fileDateColumn;

	@FXML
	private TextArea serverFilesArea = new TextArea();

	@FXML
	private AnchorPane firstWindow;
	
	@FXML
	private Button okLoginButton;

	@FXML
	private TextField loginTextField;

	@FXML
	private TextField passwordTextField;

	@FXML
	private AnchorPane login;


	private Stage stage;

	private File localFile;
	
	private String host;
	
	private Integer port;
	
	private static ClientSocket naszKlient;

	private ObservableList<HistoryOfEvents> historyOfEvents = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// TODO (don't really need to do anything here).

	}

	
	public void getPortAndHost(ActionEvent event) {
		System.out.println("Button Clicked!");
		
		if (myTextField1 != null && myTextField2 != null) {
			System.out.println("Wpisano: " + getPort(event) + " i " + getHost(event));
			try {
				port = Integer.parseInt(getPort(event));
			}
			catch (Exception e) {
	            myTextField1.setText("Wpisz numer portu");
	        }
			host = getHost(event);
			
		} else {
			System.out.println("Wype³nij oba pola");
		}

		try {
			
			if(host != null && port != null) {
				
				naszKlient = new ClientSocket(host,port);
				
				try {
					naszKlient.init();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Nie uda³o siê po³¹czyæ z serwerem!",
							"Uwaga!", JOptionPane.ERROR_MESSAGE);
				}
				showNextWindow(event, "finalWindow.fxml");
			}	

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@FXML
	private void sendToServerButtonAction(ActionEvent event) throws IOException {
		chooseFile();

		Date now = new Date();

		DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String dateTimeString = df.format(now);

		if(fileNameColumn == null) {
		
			fileNameColumn = new TableColumn("Nazwa pliku");
			fileNameColumn.setCellValueFactory(new PropertyValueFactory<HistoryOfEvents, String>("fileName"));

			filePathColumn = new TableColumn("Œcie¿ka do pliku");
			filePathColumn.setCellValueFactory(new PropertyValueFactory<HistoryOfEvents, String>("filePath"));
			
			fileDateColumn = new TableColumn("Data");
			fileDateColumn.setCellValueFactory(new PropertyValueFactory<HistoryOfEvents, String>("fileDate"));
			
			clientHistoryArea.getColumns().addAll(fileNameColumn, filePathColumn, fileDateColumn);
		}
		
		if (localFile != null) {

			HistoryOfEvents action = new HistoryOfEvents(localFile.getName(), localFile.getPath(), dateTimeString);
			historyOfEvents.add(action);
			System.out.println("Œciezka do pliku: " + localFile.getPath() + ", nazwa: " + localFile.getName());
			clientHistoryArea.setItems(historyOfEvents);
			naszKlient.send(Protocol.SENDINGFILE + " " + localFile.getName() 
			+ " " + localFile.length());
		}

	}

	@FXML
	private void copyFromServerButtonAction(ActionEvent event) {

	}

	@FXML
	private void deleteFromServerButtonAction(ActionEvent event) {

	}

	//@FXML
	public void showNextWindow(ActionEvent event, String fxml) throws IOException {

		// Load new FXML file and save root Node as "root":
		Parent root = (Parent) FXMLLoader.load(getClass().getResource(fxml));

		// Create a new Scene to display the root node just loaded:
		Scene scene = new Scene(root);

		// Get a reference to the existing stage (the window containing the
		// source of the event; the "show..." Button)
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

		// Set the new scene in the existing stage:
		stage.setScene(scene);

		// Show the existing stage (though it is already showing, I think):
		stage.show();
	}

	public String getPort(ActionEvent event) {
		System.out.println("Cos sie pojawilo w porcie");

		if (myTextField1 != null) {
			return myTextField1.getText();
		}

		return null;
	}

	public String getHost(ActionEvent event) {
		System.out.println("Cos sie pojawilo w hoscie");

		if (myTextField2 != null) {
			return myTextField2.getText();
		} else {
			return null;
		}

	}

	
	@FXML
	private void getLogin(ActionEvent event) {

	}
	@FXML
	private void getPassword(ActionEvent event) {

	}
	
	@FXML
	private void getPasswordAndLogin(ActionEvent event) {

		try {
			showNextWindow(event, "chooseHostAndPort.fxml");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void chooseFile() {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		localFile = fileChooser.showOpenDialog(stage);

	}

}