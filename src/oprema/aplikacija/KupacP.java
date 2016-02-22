package oprema.aplikacija;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import oprema.model.Kupac;
import oprema.model.ProizvodiServis;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class KupacP extends Stage implements EventHandler<ActionEvent> {
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
	private VBox kupciVBox;

	@FXML
	private TextField kupacPIB;

	@FXML
	private TextField kupacNaziv;

	@FXML
	private Button otkazB;

	private ProizvodiServis ps;

	private Kupac kupac = null;

	public KupacP(ProizvodiServis p) throws IOException {
		ps = p;

		FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("Kupac.fxml"));
		loader.setController(this);
		this.initModality(Modality.APPLICATION_MODAL);
		Scene scena = new Scene(loader.load());

		this.setScene(scena);
		this.setTitle("Unos kupca za račun");
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
		unosB.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER))
					Platform.runLater(() -> {
						unosB.fire();
					});
			}
		});
		otkazB.setOnAction(this);

		kupacPIB.setOnAction(this);

		this.setOnHiding(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				kupciVBox.getChildren().clear();
			}
		});

		this.setOnShowing(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				for (Kupac k : ps.sviKupci()) {
					Button kupac = new Button(k.getNaziv() + " (" + k.getPib() + ")");
					kupac.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							((KupacP) kupac.getScene().getWindow()).setKupac(k);
							kupac.getScene().getWindow().fireEvent(
									new WindowEvent(kupac.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
						}
					});
					kupac.prefWidthProperty().bind(kupciVBox.widthProperty());
					kupciVBox.getChildren().add(kupac);
				}
			}
		});

	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == unosB) {

			kupac = new Kupac();
			kupac.setAdresa(kupacAdresa.getText());
			kupac.setMjesto(kupacMjesto.getText());
			kupac.setNaziv(kupacNaziv.getText());
			kupac.setPib(kupacPIB.getText());

			this.fireEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSE_REQUEST));
		} else if (event.getSource() == otkazB) {
			kupac = null;
			this.fireEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSE_REQUEST));
		} else {

			kupac = ps.poPIBu(kupacPIB.getText());

			if (kupac != null) {
				kupacAdresa.setText(kupac.getAdresa());
				kupacMjesto.setText(kupac.getMjesto());
				kupacNaziv.setText(kupac.getNaziv());
			} else {
				Upozorenje up = new Upozorenje();
				up.setPoruka("Ne postoji kupac sa datim PIB-om u bazi");
				up.prikazi();
			}
		}

	}

	public Kupac getKupac() {
		return kupac;
	}

	public void setKupac(Kupac kupac) {
		this.kupac = kupac;
	}

}
