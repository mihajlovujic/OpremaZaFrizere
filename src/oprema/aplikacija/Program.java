package oprema.aplikacija;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import oprema.model.Proizvodi;
import oprema.model.ProizvodiServis;

public class Program extends Application {
	List<Proizvodi> trenutni=new ArrayList<>(100);
	ProizvodiServis ps=new ProizvodiServis();


	@FXML
    private TextField saRabatom;

    @FXML
    private TextField rabat;

    @FXML
    private TextField pdv;

    @FXML
    private TextField ukupno;

    @FXML
    private TextField maliMagacin;

    @FXML
    private Button dodaj;

    @FXML
    private TextField naziv;

    @FXML
    private TextField pdvUkupno;

    @FXML
    private TextField kolicina;

    @FXML
    private TextField sifra;

    @FXML
    private TextField velikiMagacin;

    @FXML
    private TextField cijena;

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader=new FXMLLoader(this.getClass().getClassLoader().getResource("layout.fxml"));
		loader.setController(this);
		VBox boxaca=loader.load();
		Scene scena=new Scene(boxaca);
		primaryStage.setScene(scena);
		primaryStage.sizeToScene();
		sifra.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				popuniProizvodZaUnos();
			}


		});
		ukupno.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!newValue)
					Platform.runLater(()->{dodaj.requestFocus();});
			}
		});
		dodaj.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				System.out.println(ps.getProizvodPoSifri(sifra.getText()));
			}
		});
		dodaj.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub
				if(event.getCode().equals(KeyCode.ENTER))
					Platform.runLater(()->{dodaj.fire();});
			}
		});
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				// TODO Auto-generated method stub
				ps.zatvoriKlasu();
				System.exit(0);
			}
		});
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}


	private void popuniProizvodZaUnos() {
		Proizvodi nadjeni=ps.getProizvodPoSifri(sifra.getText());
		if(nadjeni==null){
			Stage upozorenje=new Stage();
			upozorenje.initModality(Modality.APPLICATION_MODAL);
			VBox kontrole=new VBox();
			kontrole.getChildren().add(new Label("Ne postoji proizvod sa datom šifrom"));
			kontrole.setAlignment(Pos.CENTER);
			Button dugme=new Button("Potvrdi");
			dugme.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					upozorenje.close();
				}
			});
			dugme.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {
					// TODO Auto-generated method stub
					if(event.getCode().equals(KeyCode.ENTER))
						Platform.runLater(()->{dugme.fire();});
				}
			});
			kontrole.getChildren().add(dugme);
			upozorenje.setScene(new Scene(kontrole));
			upozorenje.sizeToScene();
			upozorenje.centerOnScreen();
			upozorenje.show();
			return;
		}
		naziv.setText(nadjeni.getNaziv());
		kolicina.setText("1");
		cijena.setText(nadjeni.getCijena()+"");
		rabat.setText(nadjeni.getRabat()+"");
		pdv.setText(nadjeni.getPdv()+"");
		maliMagacin.setText(nadjeni.getMaliStanje()+"");
		velikiMagacin.setText(nadjeni.getVelikiStanje()+"");

	}

}
