package oprema.aplikacija;

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.scene.control.skin.BehaviorSkinBase;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
	Proizvodi unos=null;


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
				if(popuniProizvodZaUnos())
					if( sifra.getSkin() instanceof BehaviorSkinBase) {
						((BehaviorSkinBase) sifra.getSkin()).getBehavior().traverseNext();
					}
			}


		});
		dodaj.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if(unos!=null)
					System.out.println(ps.getProizvodPoSifri(sifra.getText()));
				else{
					Upozorenje up=new Upozorenje("Ne može se u račun dodati nepostojeći proizvod");
					up.prikazi();
				}
				Platform.runLater(()->{sifra.requestFocus();});
			}
		});
		dodaj.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub
				if(event.getCode().equals(KeyCode.ENTER)){
					Platform.runLater(()->{dodaj.fire(); sifra.requestFocus();});
				}
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


	private boolean popuniProizvodZaUnos() {
		unos=ps.getProizvodPoSifri(sifra.getText());
		if(unos==null){
			Upozorenje up=new Upozorenje("Ne postoji proizvod sa traženom šifrom u bazi");
			up.prikazi();
			return false;
		}
		naziv.setText(unos.getNaziv());
		kolicina.setText("1");
		cijena.setText(unos.getCijena()+"");
		rabat.setText(unos.getRabat()+"");
		pdv.setText(unos.getPdv()+"");
		saRabatom.setText(unos.getCijenaSaRabatom()+"");
		pdvUkupno.setText(unos.getCijenaPDV()+"");
		ukupno.setText(unos.getCijenaUkupno()+"");
		maliMagacin.setText(unos.getMaliStanje()+"");
		velikiMagacin.setText(unos.getVelikiStanje()+"");
		return true;
	}

	@FXML
	void dajFokus(ActionEvent event) {
		TextField tf=(TextField)event.getSource();
		if( tf.getSkin() instanceof BehaviorSkinBase) {
			((BehaviorSkinBase) tf.getSkin()).getBehavior().traverseNext();
		}
	}

}
