package oprema.aplikacija;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.sun.javafx.scene.control.skin.BehaviorSkinBase;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import oprema.model.Proizvodi;
import oprema.model.ProizvodiServis;

public class Program extends Application {
	ProizvodiServis ps=new ProizvodiServis();
	Proizvodi unos=null;
	Upozorenje up=new Upozorenje();

	 @FXML
	    private ResourceBundle resources;

	    @FXML
	    private URL location;

	    @FXML
	    private Button generisiRacun;

	    @FXML
	    private TextField ukupnoG;

	    @FXML
	    private TextField pdv;

	    @FXML
	    private TextField maliMagacin;

	    @FXML
	    private TextField ukupnoC;

	    @FXML
	    private TextField naziv;

	    @FXML
	    private Button napraviKupca;

	    @FXML
	    private TextField kolicina;

	    @FXML
	    private TextField kupacNaziv;

	    @FXML
	    private TextField ukupnoPDV;

	    @FXML
	    private TextField saRabatom;

	    @FXML
	    private TextField rabat;

	    @FXML
	    private TableView<Proizvodi> tabela;

	    @FXML
	    private Button magaciniB;

	    @FXML
	    private TextField ukupno;

	    @FXML
	    private TextField kupacMjesto;

	    @FXML
	    private Button dodaj;

	    @FXML
	    private TextField pdvUkupno;

	    @FXML
	    private TextField kupacAdresa;

	    @FXML
	    private TextField sifra;

	    @FXML
	    private TextField velikiMagacin;

	    @FXML
	    private TextField kupacPIB;

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
					unesiUtabelu();
				else{
					up.setPoruka("Ne može se uneti nepostojeći proizvod");
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



	protected void unesiUtabelu() {
		// TODO Auto-generated method stub
		tabela.getItems().add(unos);
	}



	private boolean popuniProizvodZaUnos() {
		unos=ps.getProizvodPoSifri(sifra.getText());
		if(unos==null){
			up.setPoruka("Ne postoji proizvod sa traženom šifrom u bazi");
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

	@FXML
    void initialize() {
        assert generisiRacun != null : "fx:id=\"generisiRacun\" was not injected: check your FXML file 'layout.fxml'.";
        assert ukupnoG != null : "fx:id=\"ukupnoG\" was not injected: check your FXML file 'layout.fxml'.";
        assert pdv != null : "fx:id=\"pdv\" was not injected: check your FXML file 'layout.fxml'.";
        assert maliMagacin != null : "fx:id=\"maliMagacin\" was not injected: check your FXML file 'layout.fxml'.";
        assert ukupnoC != null : "fx:id=\"ukupnoC\" was not injected: check your FXML file 'layout.fxml'.";
        assert naziv != null : "fx:id=\"naziv\" was not injected: check your FXML file 'layout.fxml'.";
        assert napraviKupca != null : "fx:id=\"napraviKupca\" was not injected: check your FXML file 'layout.fxml'.";
        assert kolicina != null : "fx:id=\"kolicina\" was not injected: check your FXML file 'layout.fxml'.";
        assert kupacNaziv != null : "fx:id=\"kupacNaziv\" was not injected: check your FXML file 'layout.fxml'.";
        assert ukupnoPDV != null : "fx:id=\"ukupnoPDV\" was not injected: check your FXML file 'layout.fxml'.";
        assert saRabatom != null : "fx:id=\"saRabatom\" was not injected: check your FXML file 'layout.fxml'.";
        assert rabat != null : "fx:id=\"rabat\" was not injected: check your FXML file 'layout.fxml'.";
        assert tabela != null : "fx:id=\"tabela\" was not injected: check your FXML file 'layout.fxml'.";
        assert magaciniB != null : "fx:id=\"magaciniB\" was not injected: check your FXML file 'layout.fxml'.";
        assert ukupno != null : "fx:id=\"ukupno\" was not injected: check your FXML file 'layout.fxml'.";
        assert kupacMjesto != null : "fx:id=\"kupacMjesto\" was not injected: check your FXML file 'layout.fxml'.";
        assert dodaj != null : "fx:id=\"dodaj\" was not injected: check your FXML file 'layout.fxml'.";
        assert pdvUkupno != null : "fx:id=\"pdvUkupno\" was not injected: check your FXML file 'layout.fxml'.";
        assert kupacAdresa != null : "fx:id=\"kupacAdresa\" was not injected: check your FXML file 'layout.fxml'.";
        assert sifra != null : "fx:id=\"sifra\" was not injected: check your FXML file 'layout.fxml'.";
        assert velikiMagacin != null : "fx:id=\"velikiMagacin\" was not injected: check your FXML file 'layout.fxml'.";
        assert kupacPIB != null : "fx:id=\"kupacPIB\" was not injected: check your FXML file 'layout.fxml'.";
        assert cijena != null : "fx:id=\"cijena\" was not injected: check your FXML file 'layout.fxml'.";

        generisiRacun.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				for(Proizvodi a : tabela.getItems()){
					System.out.println(a);
				}
			}
		});
        napraviTabelu(tabela);
    }

	private void napraviTabelu(TableView<Proizvodi> tabela2) {
		TableColumn<Proizvodi, String> sifraKol=new TableColumn<>("Šifra");
		TableColumn<Proizvodi, String> nazivKol=new TableColumn<>("Ime");
		TableColumn<Proizvodi, Integer> kolicinaKol=new TableColumn<>("Kol.");
		TableColumn<Proizvodi, Integer> rabatKol=new TableColumn<>("Rabat");
		TableColumn<Proizvodi, Integer> pdvKol=new TableColumn<>("PDV");
		TableColumn<Proizvodi, Integer> maliMKol=new TableColumn<>("Mali M");
		TableColumn<Proizvodi, Integer> velikiMKol=new TableColumn<>("Veliki M");
		TableColumn<Proizvodi, Double> cenaKol=new TableColumn<>("Cena");
		TableColumn<Proizvodi, Double> cenaRabKol=new TableColumn<>("Cena sa R");
		TableColumn<Proizvodi, Double> cenaPDVKol=new TableColumn<>("PDV Cena");
		TableColumn<Proizvodi, Double> ukupnoKol=new TableColumn<>("Ukupno");


		nazivKol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Proizvodi, String> param) {
				SimpleStringProperty sp=new SimpleStringProperty(param.getValue().getNaziv());
				sp.addListener(new ChangeListener<String>() {

					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {
						// TODO Auto-generated method stub
						param.getValue().setNaziv(newValue);
					}
				});
				return sp;
			}
		});
		nazivKol.setCellFactory(TextFieldTableCell.forTableColumn());
		nazivKol.setEditable(false);

		sifraKol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Proizvodi, String> param) {
				SimpleStringProperty sp=new SimpleStringProperty(param.getValue().getSifra());

				return sp;
			}
		});
		sifraKol.setCellFactory(TextFieldTableCell.forTableColumn());
		sifraKol.setEditable(false);



		kolicinaKol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,Integer>, ObservableValue<Integer>>() {

			@Override
			public ObservableValue<Integer> call(CellDataFeatures<Proizvodi, Integer> param) {
				ObservableValue<Integer> si=new SimpleIntegerProperty(param.getValue().getKolicina()).asObject();
				si.addListener(new ChangeListener<Number>() {

					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue,
							Number newValue) {
						param.getValue().setKolicina(newValue.intValue());
					}
				});
				return si;
			}
		});
		kolicinaKol.setCellFactory(new Callback<TableColumn<Proizvodi,Integer>, TableCell<Proizvodi,Integer>>() {

			@Override
			public TableCell<Proizvodi, Integer> call(TableColumn<Proizvodi, Integer> param) {
				TextFieldTableCell<Proizvodi, Integer> tf=new TextFieldTableCell<>();
				tf.setConverter(new StringConverter<Integer>() {

					@Override
					public String toString(Integer object) {
						// TODO Auto-generated method stub
						return object.toString();
					}

					@Override
					public Integer fromString(String string) {
						Integer o;
						try{
							o=Integer.parseInt(string);
						}
						catch(NumberFormatException e){
							up.setPoruka("Unesite validan ceo broj");
							up.prikazi();
							tf.setText("0");
							return 0;
						}
						return o;
					}
				});
				return tf;
			}
		});
		kolicinaKol.setEditable(true);


		rabatKol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,Integer>, ObservableValue<Integer>>() {

			@Override
			public ObservableValue<Integer> call(CellDataFeatures<Proizvodi, Integer> param) {
				ObservableValue<Integer> si=new SimpleIntegerProperty(param.getValue().getRabat()).asObject();
				si.addListener(new ChangeListener<Number>() {

					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue,
							Number newValue) {
						param.getValue().setRabat(newValue.intValue());
					}
				});
				return si;
			}
		});
		rabatKol.setCellFactory(new Callback<TableColumn<Proizvodi,Integer>, TableCell<Proizvodi,Integer>>() {

			@Override
			public TableCell<Proizvodi, Integer> call(TableColumn<Proizvodi, Integer> param) {
				TextFieldTableCell<Proizvodi, Integer> tf=new TextFieldTableCell<>();
				tf.setConverter(new StringConverter<Integer>() {

					@Override
					public String toString(Integer object) {
						// TODO Auto-generated method stub
						return object.toString();
					}

					@Override
					public Integer fromString(String string) {
						Integer o;
						try{
							o=Integer.parseInt(string);
						}
						catch(NumberFormatException e){
							up.setPoruka("Unesite validan ceo broj");
							up.prikazi();
							tf.setText("0");
							return 0;
						}
						return o;
					}
				});
				return tf;
			}
		});
		rabatKol.setEditable(true);


		pdvKol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,Integer>, ObservableValue<Integer>>() {

			@Override
			public ObservableValue<Integer> call(CellDataFeatures<Proizvodi, Integer> param) {
				ObservableValue<Integer> si=new SimpleIntegerProperty(param.getValue().getPdv()).asObject();
				si.addListener(new ChangeListener<Number>() {

					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue,
							Number newValue) {
						param.getValue().setPdv(newValue.intValue());
					}
				});
				return si;
			}
		});
		pdvKol.setCellFactory(new Callback<TableColumn<Proizvodi,Integer>, TableCell<Proizvodi,Integer>>() {

			@Override
			public TableCell<Proizvodi, Integer> call(TableColumn<Proizvodi, Integer> param) {
				TextFieldTableCell<Proizvodi, Integer> tf=new TextFieldTableCell<>();
				tf.setConverter(new StringConverter<Integer>() {

					@Override
					public String toString(Integer object) {
						// TODO Auto-generated method stub
						return object.toString();
					}

					@Override
					public Integer fromString(String string) {
						Integer o;
						try{
							o=Integer.parseInt(string);
						}
						catch(NumberFormatException e){
							up.setPoruka("Unesite validan ceo broj");
							up.prikazi();
							tf.setText("0");
							return 0;
						}
						return o;
					}
				});
				return tf;
			}
		});
		pdvKol.setEditable(true);



		tabela2.setEditable(true);
		tabela2.getSelectionModel().cellSelectionEnabledProperty();
		tabela2.getColumns().addAll(sifraKol, nazivKol,kolicinaKol,rabatKol,pdvKol);
	}



	public static void main(String[] args) {
		launch(args);
	}

}
