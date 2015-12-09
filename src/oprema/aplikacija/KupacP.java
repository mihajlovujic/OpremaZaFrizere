package oprema.aplikacija;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import oprema.model.Kupac;
import oprema.model.ProizvodiServis;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class KupacP extends Stage implements EventHandler<ActionEvent>{
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button unosB;

	@FXML
	private TextField kupacMjesto;

	@FXML
	private TextField kupacAdresa;

	@FXML
	private TextField kupacPIB;

	@FXML
	private TextField kupacNaziv;

	@FXML
	private Button otkazB;

	private ProizvodiServis ps;

	private Kupac kupac=null;

	public KupacP(ProizvodiServis p) throws IOException {
		ps=p;

		FXMLLoader loader=new FXMLLoader(this.getClass().getClassLoader().getResource("Kupac.fxml"));
		loader.setController(this);
		this.initModality(Modality.APPLICATION_MODAL);
		Scene scena=new Scene(loader.load());

		this.setScene(scena);
		this.sizeToScene();

	}

	@FXML
	void initialize() {
		assert unosB != null : "fx:id=\"unosB\" was not injected: check your FXML file 'Kupac.fxml'.";
		assert kupacMjesto != null : "fx:id=\"kupacMjesto\" was not injected: check your FXML file 'Kupac.fxml'.";
		assert kupacAdresa != null : "fx:id=\"kupacAdresa\" was not injected: check your FXML file 'Kupac.fxml'.";
		assert kupacPIB != null : "fx:id=\"kupacPIB\" was not injected: check your FXML file 'Kupac.fxml'.";
		assert kupacNaziv != null : "fx:id=\"kupacNaziv\" was not injected: check your FXML file 'Kupac.fxml'.";
		assert otkazB != null : "fx:id=\"otkazB\" was not injected: check your FXML file 'Kupac.fxml'.";

		unosB.setOnAction(this);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource()==unosB){
			kupac=new Kupac();
			kupac.setAdresa(kupacAdresa.getText());
			kupac.setMjesto(kupacMjesto.getText());
			kupac.setNaziv(kupacNaziv.getText());
			kupac.setPib(Integer.parseInt(kupacPIB.getText()));
			this.fireEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSE_REQUEST));
		}
	}

	public Kupac getKupac() {
		return kupac;
	}




}