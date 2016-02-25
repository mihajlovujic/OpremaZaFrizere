package oprema.aplikacija;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.transaction.NotSupportedException;

import com.sun.javafx.scene.control.skin.BehaviorSkinBase;

import exelProba.Proba;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import oprema.aplikacija.HBoxRacunChooser.Akcija;
import oprema.model.Kupac;
import oprema.model.Obracun;
import oprema.model.Proizvodi;
import oprema.model.ProizvodiServis;
import oprema.model.RacuniServis;

public class Program extends Application {
	ProizvodiServis ps=new ProizvodiServis();
	Proizvodi unos=null;
	Upozorenje up=new Upozorenje();
	Obracun obracun=new Obracun();
	BazaP  bazaProzor;
	KupacP kup;
	Kupac izabraniK;
	Stage glavniProzor;
	WindowRacunChooser wc = new WindowRacunChooser();

	@FXML
	private ResourceBundle resources;

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
	private ComboBox<String> naziv;

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
	private Button prethodniRacuni;

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

	private VBox boxaca;
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader=new FXMLLoader(this.getClass().getClassLoader().getResource("layout.fxml"));
		bazaProzor=new BazaP(ps,up);
		kup=new KupacP(ps);
		loader.setController(this);
		boxaca=loader.load();
		Scene scena=new Scene(boxaca);
		primaryStage.setScene(scena);
		primaryStage.sizeToScene();
		glavniProzor=primaryStage;
		RacuniServis.ps = ps;
		dodaj.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
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
				if(event.getCode().equals(KeyCode.ENTER)){
					Platform.runLater(()->{dodaj.fire(); sifra.requestFocus();});
				}
			}
		});
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				ps.zatvoriKlasu();
				System.exit(0);
			}
		});
		primaryStage.setTitle("Messina");
		primaryStage.getIcons().add(new Image(this.getClass().getClassLoader().getResourceAsStream("ikona.png")));
		primaryStage.show();
		primaryStage.setMaximized(true);
	}



	protected void unesiUtabelu() {
		if(obracun.sadrzi(unos)){
			up.setPoruka("U računu se već nalazi proizvod sa istom šifrom");
			up.prikazi();
			return;
		}
		try {
			unos.azurirajStanja();
		} catch (NotSupportedException e) {
			up.setPoruka(e.getMessage());
			up.prikazi();
			return;
		}
		tabela.getItems().add(unos);
		postaviZbirove(unos);
		unos=null;
		postaviPolja();

	}



	private boolean popuniProizvodZaUnos() {
		unos=ps.getProizvodPoSifri(sifra.getText());
		if(unos==null){
			up.setPoruka("Ne postoji proizvod sa traženom šifrom u bazi");
			up.prikazi();
			postaviPolja();
			return false;
		}
		postaviPolja();
		return true;
	}

	public boolean popuniPoNazivuZaUnos(String prenos){
		String sifraNaziv=prenos.substring(prenos.indexOf("(")+1, prenos.length()-1);
		System.out.println("Sifra: "+sifraNaziv);
		unos=ps.getProizvodPoSifri(sifraNaziv);
		if(unos==null){
			up.setPoruka("Ne postoji proizvod sa traženom šifrom u bazi");
			up.prikazi();
			postaviPolja();
			return false;
		}
		postaviPolja();
		return true;
	}


	public  void postaviPolja(){
		if(unos==null){
			sifra.setText("");
			naziv.setValue("");
			kolicina.setText("");
			cijena.setText("");
			rabat.setText("");
			pdv.setText("");
			saRabatom.setText("");
			pdvUkupno.setText("");
			ukupno.setText("");
			maliMagacin.setText("");
			velikiMagacin.setText("");
			return;
		}
		sifra.setText(unos.getSifra());
		naziv.setValue(unos.getNaziv());
		kolicina.setText(unos.getKolicina()+"");
		cijena.setText(unos.getCijenaDb()+"");
		rabat.setText(unos.getRabat()+"");
		pdv.setText(unos.getPdv()+"");
		saRabatom.setText(unos.getCijenaSaRabatomDb()+"");
		pdvUkupno.setText(unos.getCijenaPDVDb()+"");
		ukupno.setText(unos.getCijenaUkupnoDb()+"");
		maliMagacin.setText(unos.getMaliStanje()+"");
		velikiMagacin.setText(unos.getVelikiStanje()+"");
	}



	@SuppressWarnings("rawtypes")
	@FXML
	void dajFokus(ActionEvent event) {
		TextField tf=(TextField)event.getSource();
		if(tf==cijena){
			try{
				unos.setCijena(Double.parseDouble(cijena.getText()));
				postaviPolja();
			}
			catch(NumberFormatException e){
				up.setPoruka("Nije dobar unos cene");
				up.prikazi();
			}
		}
		else if(tf==rabat){
			try{
				unos.setRabat(Integer.parseInt(rabat.getText()));
				postaviPolja();
			}
			catch(NumberFormatException e){
				up.setPoruka("Nije dobar unos rabata");
				up.prikazi();
			}
		}
		else if(tf==kolicina){
			try{
				unos.setKolicina(Integer.parseInt(kolicina.getText()));
				postaviPolja();
			}
			catch(Exception e){
				up.setPoruka(e.getMessage());
				up.prikazi();
				return;
			}
		}
		else if(tf==pdv){
			try{
				unos.setPdv(Integer.parseInt(pdv.getText()));
				postaviPolja();
			}
			catch(NumberFormatException e){
				up.setPoruka("Nije dobar unos PDV-a");
				up.prikazi();
			}
		}
		else if(tf==sifra){
			if(!popuniProizvodZaUnos())
				return;
		}
		if( tf.getSkin() instanceof BehaviorSkinBase) {
			((BehaviorSkinBase) tf.getSkin()).getBehavior().traverseNext();
		}
	}


	@FXML
	void isprazniListu(ActionEvent event) {
		if(tabela.getItems()!=null){
			if(tabela.getItems().size()>0){
				tabela.getItems().clear();
				obracun.isprazni();
				postaviZbirove(null);
			}
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
		assert prethodniRacuni != null : "fx:id=\"prethodniRacuni\" was not injected: check your FXML file 'layout.fxml'.";
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

				if(tabela.getItems().size()==0){
					up.setPoruka("Nema proizvoda za račun");
					up.prikazi();
				}
				else if(izabraniK==null){
					up.setPoruka("Nije izabran kupac");
					up.prikazi();
				}
				else{
					FileChooser fc=new FileChooser();
					fc.setTitle("Izaberite lokaciju za čuvanje računa");
					fc.setInitialDirectory(new File(System.getProperty("user.home")));
					File f=fc.showSaveDialog(glavniProzor);
					if(f!=null){
						Proba gen=new Proba(tabela.getItems(), izabraniK, obracun, f);
						Platform.runLater(()->{

							LoaderWindow ld;
							try {
								ld = new LoaderWindow(gen);
								ld.odradi();
								ld.close();

								up.setPoruka("Proizvodi za doneti iz velikog magacina: ");
								boolean prikaz=false;
								for(Proizvodi a : tabela.getItems()){
									ps.apdejtujKolicine(a);
									if(a.getPozajmicaIzVelikog()>0){
										if(!prikaz)
											up.pripremiTekst();
										prikaz=true;
										String nadov=a.getSifra()+" : "+a.getNaziv()+"\t"+a.getPozajmicaIzVelikog();
										up.dodajUArea(nadov);
									}
								}
								for(Proizvodi a : RacuniServis.izbaceniProizvodi){
									a.vratiStanja();
									ps.apdejtujKolicine(a);
								}
								RacuniServis.izbaceniProizvodi.clear();
								if(RacuniServis.akcija == Akcija.IZMJENI){
									RacuniServis.dodajRacun(tabela.getItems(), izabraniK, RacuniServis.ucitaniRacun);
								} else{
									RacuniServis.dodajRacun(tabela.getItems(), izabraniK, f.getName());
								}
								if(prikaz){
									up.prikazi(true);
								}
								RacuniServis.akcija=null;
								RacuniServis.ucitaniKupac=null;
								RacuniServis.ucitaniProizvodi=null;
								RacuniServis.ucitaniRacun=null;
								if(boxaca.getChildren().size() == 6){
									boxaca.getChildren().remove(5);
								}
							} catch (Exception e) {
								up.setPoruka(e.toString());
								up.prikazi();
							}

						});

					}

				}

			}
		});
		napraviTabelu(tabela);
		postaviZbirove(null);

		napraviKupca.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				kup.setOnCloseRequest(new EventHandler<WindowEvent>() {

					@Override
					public void handle(WindowEvent event) {
						if(kup.getKupac()!=null){
							kupacPIB.setText(kup.getKupac().getPib()+"");
							kupacAdresa.setText(kup.getKupac().getAdresa());
							kupacMjesto.setText(kup.getKupac().getMjesto());
							kupacNaziv.setText(kup.getKupac().getNaziv());
							izabraniK=kup.getKupac();
						}
					}
				});
				kup.show();

			}
		});

		magaciniB.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				bazaProzor.prikazi();

			}
		});
		naziv.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode().equals(KeyCode.ENTER)){
					if(!naziv.isShowing())
						Platform.runLater(()->{naziv.show();});
				}
			}
		});
		Parent strijelica=(Parent) naziv.lookup(".arrow-button");
		if(strijelica!=null)
			strijelica.setStyle("visibility: 'hidden'");
		naziv.showingProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!newValue){
					if(!naziv.getSelectionModel().isEmpty()){
						String izbor=naziv.getValue();
						naziv.getSelectionModel().clearSelection();
						naziv.getItems().clear();
						popuniPoNazivuZaUnos(izbor);
						kolicina.requestFocus();
					}
				}
				else{
					naziv.getItems().clear();
					List<Proizvodi> pro=ps.getProizvodNazivLike(naziv.getValue());
					System.out.println("Size: "+pro.size());
					for(Proizvodi p : pro){
						naziv.getItems().add(p.getNaziv()+"("+p.getSifra()+")");
					}

				}
			}
		});

		prethodniRacuni.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Platform.runLater(()->{
					wc.prikazi();
				});
			}
		});

		wc.setOnHidden(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				if(RacuniServis.ucitaniRacun != null){
					if(boxaca.getChildren().size() == 6){
						boxaca.getChildren().remove(5);
					}
					HBox donjaTraka = new HBox();
					donjaTraka.setAlignment(Pos.CENTER);
					Label poruka = new Label();
					poruka.setText("Racun "+RacuniServis.ucitaniRacun+" je u režimu "+RacuniServis.akcija);
					Button pon = new Button("Poništi");
					pon.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							boxaca.getChildren().remove(donjaTraka);
							RacuniServis.ucitaniRacun=null;
							RacuniServis.akcija=null;
							resetujProizvode();
						}
					});
					donjaTraka.getChildren().addAll(poruka, pon);
					boxaca.getChildren().add(donjaTraka);
					if(RacuniServis.ucitaniKupac !=null){
						kupacPIB.setText(RacuniServis.ucitaniKupac.getPib()+"");
						kupacAdresa.setText(RacuniServis.ucitaniKupac.getAdresa());
						kupacMjesto.setText(RacuniServis.ucitaniKupac.getMjesto());
						kupacNaziv.setText(RacuniServis.ucitaniKupac.getNaziv());
						izabraniK=RacuniServis.ucitaniKupac;
					}
					tabela.getItems().clear();
					obracun.isprazni();
					for(Proizvodi p : RacuniServis.ucitaniProizvodi){
						Proizvodi izBaze = ps.getProizvodPoSifri(p.getSifra());
						if(izBaze != null){
							if(RacuniServis.akcija == Akcija.IZMJENI){
								izBaze.setMaliStanje(izBaze.getMaliStanje()+p.getKolicina()-p.getPozajmicaIzVelikog());
								izBaze.setVelikiStanje(izBaze.getVelikiStanje()+p.getPozajmicaIzVelikog());
							}
							izBaze.setKolicina(p.getKolicina());
							izBaze.setCijena(p.getCijenaDb());
							izBaze.setRabat(p.getRabat());
							izBaze.setPdv(p.getPdv());
							izBaze.setUsluge(p.isUsluge());
							try {
								izBaze.azurirajStanja();

							} catch (NotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							postaviZbirove(izBaze);
							tabela.getItems().add(izBaze);
						} else{
							p.setNaziv("");
							p.setMaliStanje(9999);
							p.setVelikiStanje(9999);
							p.postaviSve();
							postaviZbirove(p);
							tabela.getItems().add(p);
						}
					}


				}
				if(wc.izbrisani != null){
					if(wc.izbrisani.equals(RacuniServis.ucitaniRacun)){
						if(boxaca.getChildren().size() == 6){
							boxaca.getChildren().remove(5);
							RacuniServis.ucitaniRacun = null;
							RacuniServis.ucitaniProizvodi = null;
							RacuniServis.ucitaniKupac = null;
							RacuniServis.izbaceniProizvodi.clear();
							resetujProizvode();
							izabraniK = null;
							kupacAdresa.setText("");
							kupacMjesto.setText("");
							kupacNaziv.setText("");
							kupacPIB.setText("");
						}
					}
				}
			}
		});

	}

	private void napraviTabelu(TableView<Proizvodi> tabela2) {
		TableColumn<Proizvodi, String> sifraKol=new TableColumn<>("Šifra");
		TableColumn<Proizvodi, String> nazivKol=new TableColumn<>("Ime");
		TableColumn<Proizvodi, Integer> kolicinaKol=new TableColumn<>("Kol.");
		TableColumn<Proizvodi, Integer> rabatKol=new TableColumn<>("Rabat");
		TableColumn<Proizvodi, Integer> pdvKol=new TableColumn<>("PDV");
		TableColumn<Proizvodi, Integer> maliMKol=new TableColumn<>("Mali M");
		TableColumn<Proizvodi, Integer> velikiMKol=new TableColumn<>("Veliki M");
		TableColumn<Proizvodi, Number> cenaKol=new TableColumn<>("Cena");
		TableColumn<Proizvodi, Number> cenaRabKol=new TableColumn<>("Cena sa R");
		TableColumn<Proizvodi, Number> cenaPDVKol=new TableColumn<>("PDV Cena");
		TableColumn<Proizvodi, Number> ukupnoKol=new TableColumn<>("Ukupno");
		TableColumn<Proizvodi, Boolean> uslugeKol=new TableColumn<Proizvodi, Boolean>("Usluge");
		TableColumn<Proizvodi, Boolean> izbaci=new TableColumn<>("Izbaci");

		nazivKol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Proizvodi, String> param) {
				SimpleStringProperty sp=new SimpleStringProperty(param.getValue().getNaziv());
				sp.addListener(new ChangeListener<String>() {

					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {
						param.getValue().setNaziv(newValue);
					}
				});
				return sp;
			}
		});
		nazivKol.setCellFactory(TextFieldTableCell.forTableColumn());
		nazivKol.setPrefWidth(150);
		nazivKol.setMaxWidth(Double.MAX_VALUE);
		nazivKol.setEditable(true);

		sifraKol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Proizvodi, String> param) {
				SimpleStringProperty sp=new SimpleStringProperty(param.getValue().getSifra());

				return sp;
			}
		});
		sifraKol.setCellFactory(TextFieldTableCell.forTableColumn());
		sifraKol.setPrefWidth(200);
		sifraKol.setEditable(false);



		kolicinaKol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,Integer>, ObservableValue<Integer>>() {

			@Override
			public ObservableValue<Integer> call(CellDataFeatures<Proizvodi, Integer> param) {
				ObservableValue<Integer> si=new SimpleIntegerProperty(param.getValue().getKolicina()).asObject();
				si.addListener(new ChangeListener<Number>() {

					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue,
							Number newValue) {
						try {
							param.getValue().vratiStanja();
							param.getValue().setKolicina(newValue.intValue());
							param.getValue().azurirajStanja();

						} catch (Exception e) {
							param.getValue().setKolicina(oldValue.intValue());
							try {
								param.getValue().azurirajStanja();
							} catch (NotSupportedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							tabela2.refresh();
							up.setPoruka(e.getMessage());
							up.prikazi();
							return;
						}
						postaviZbirove(param.getValue());
					}
				});
				return si;
			}
		});
		kolicinaKol.setCellFactory(new Callback<TableColumn<Proizvodi,Integer>, TableCell<Proizvodi,Integer>>() {

			@Override
			public TableCell<Proizvodi, Integer> call(TableColumn<Proizvodi, Integer> param) {
				TextFieldTableCell<Proizvodi, Integer> tf=new TextFieldTableCell<>();
				tf.setAlignment(Pos.CENTER_RIGHT);
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
						postaviZbirove(param.getValue());
					}
				});
				return si;
			}
		});
		rabatKol.setCellFactory(new Callback<TableColumn<Proizvodi,Integer>, TableCell<Proizvodi,Integer>>() {

			@Override
			public TableCell<Proizvodi, Integer> call(TableColumn<Proizvodi, Integer> param) {
				TextFieldTableCell<Proizvodi, Integer> tf=new TextFieldTableCell<>();
				tf.setAlignment(Pos.CENTER_RIGHT);
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
						postaviZbirove(param.getValue());
					}
				});
				return si;
			}
		});
		pdvKol.setCellFactory(new Callback<TableColumn<Proizvodi,Integer>, TableCell<Proizvodi,Integer>>() {

			@Override
			public TableCell<Proizvodi, Integer> call(TableColumn<Proizvodi, Integer> param) {
				TextFieldTableCell<Proizvodi, Integer> tf=new TextFieldTableCell<>();
				tf.setAlignment(Pos.CENTER_RIGHT);
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



		Background bac=new Background(new BackgroundFill(Paint.valueOf("yellow"), new CornerRadii(2), new Insets(2)));
		maliMKol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,Integer>, ObservableValue<Integer>>() {

			@Override
			public ObservableValue<Integer> call(CellDataFeatures<Proizvodi, Integer> param) {
				ObservableValue<Integer> si=new SimpleIntegerProperty(param.getValue().getMaliStanje()).asObject();
				si.addListener(new ChangeListener<Number>() {

					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue,
							Number newValue) {
						param.getValue().setMaliStanje(newValue.intValue());
					}
				});
				return si;
			}
		});
		maliMKol.setCellFactory(new Callback<TableColumn<Proizvodi,Integer>, TableCell<Proizvodi,Integer>>() {

			@Override
			public TableCell<Proizvodi, Integer> call(TableColumn<Proizvodi, Integer> param) {
				TextFieldTableCell<Proizvodi, Integer> tf=new TextFieldTableCell<>();
				tf.setBackground(bac);
				tf.setAlignment(Pos.CENTER_RIGHT);
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
		maliMKol.setEditable(false);

		velikiMKol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,Integer>, ObservableValue<Integer>>() {

			@Override
			public ObservableValue<Integer> call(CellDataFeatures<Proizvodi, Integer> param) {
				ObservableValue<Integer> si=new SimpleIntegerProperty(param.getValue().getVelikiStanje()).asObject();
				si.addListener(new ChangeListener<Number>() {

					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue,
							Number newValue) {
						param.getValue().setVelikiStanje(newValue.intValue());
					}
				});
				return si;
			}
		});
		velikiMKol.setCellFactory(new Callback<TableColumn<Proizvodi,Integer>, TableCell<Proizvodi,Integer>>() {

			@Override
			public TableCell<Proizvodi, Integer> call(TableColumn<Proizvodi, Integer> param) {
				TextFieldTableCell<Proizvodi, Integer> tf=new TextFieldTableCell<>();
				tf.setBackground(bac);
				tf.setAlignment(Pos.CENTER_RIGHT);
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
		velikiMKol.setEditable(false);

		cenaKol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,Number>, ObservableValue<Number>>() {

			@Override
			public ObservableValue<Number> call(CellDataFeatures<Proizvodi, Number> param) {
				SimpleDoubleProperty sp=new SimpleDoubleProperty(param.getValue().getCijenaDb());
				sp.addListener(new ChangeListener<Number>() {

					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue,
							Number newValue) {
						// TODO Auto-generated method stub
						param.getValue().setCijena(newValue.doubleValue());
						postaviZbirove(param.getValue());
					}
				});
				return sp;
			}
		});
		cenaKol.setCellFactory(new Callback<TableColumn<Proizvodi,Number>, TableCell<Proizvodi,Number>>() {

			@Override
			public TableCell<Proizvodi, Number> call(TableColumn<Proizvodi, Number> param) {
				TextFieldTableCell<Proizvodi, Number> tf=new TextFieldTableCell<>();
				tf.setAlignment(Pos.CENTER_RIGHT);
				tf.setConverter(new StringConverter<Number>() {

					@Override
					public String toString(Number object) {
						// TODO Auto-generated method stub
						return object.toString();
					}

					@Override
					public Number fromString(String string) {
						Number a;
						try{
							a=Double.parseDouble(string);
						}
						catch(NumberFormatException e){
							up.setPoruka("Nije ispravan broj");
							up.prikazi();
							return 0;
						}
						return a;
					}
				});
				return tf;

			}
		});
		cenaKol.setEditable(true);

		cenaRabKol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,Number>, ObservableValue<Number>>() {

			@Override
			public ObservableValue<Number> call(CellDataFeatures<Proizvodi, Number> param) {
				SimpleDoubleProperty sp=new SimpleDoubleProperty(param.getValue().getCijenaSaRabatomDb());

				return sp;
			}
		});
		cenaRabKol.setCellFactory(new Callback<TableColumn<Proizvodi,Number>, TableCell<Proizvodi,Number>>() {

			@Override
			public TableCell<Proizvodi, Number> call(TableColumn<Proizvodi, Number> param) {
				TextFieldTableCell<Proizvodi, Number> tf=new TextFieldTableCell<>();
				tf.setAlignment(Pos.CENTER_RIGHT);
				tf.setConverter(new StringConverter<Number>() {

					@Override
					public String toString(Number object) {
						// TODO Auto-generated method stub
						return object.toString();
					}

					@Override
					public Number fromString(String string) {
						Number a;
						try{
							a=Double.parseDouble(string);
						}
						catch(NumberFormatException e){
							up.setPoruka("Nije ispravan broj");
							up.prikazi();
							return 0;
						}
						return a;
					}
				});
				return tf;

			}
		});
		cenaRabKol.setEditable(false);



		cenaPDVKol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,Number>, ObservableValue<Number>>() {

			@Override
			public ObservableValue<Number> call(CellDataFeatures<Proizvodi, Number> param) {
				SimpleDoubleProperty sp=new SimpleDoubleProperty(param.getValue().getCijenaPDVDb());

				return sp;
			}
		});
		cenaPDVKol.setCellFactory(new Callback<TableColumn<Proizvodi,Number>, TableCell<Proizvodi,Number>>() {

			@Override
			public TableCell<Proizvodi, Number> call(TableColumn<Proizvodi, Number> param) {
				TextFieldTableCell<Proizvodi, Number> tf=new TextFieldTableCell<>();
				tf.setAlignment(Pos.CENTER_RIGHT);
				tf.setConverter(new StringConverter<Number>() {

					@Override
					public String toString(Number object) {
						// TODO Auto-generated method stub
						return object.toString();
					}

					@Override
					public Number fromString(String string) {
						Number a;
						try{
							a=Double.parseDouble(string);
						}
						catch(NumberFormatException e){
							up.setPoruka("Nije ispravan broj");
							up.prikazi();
							return 0;
						}
						return a;
					}
				});
				return tf;

			}
		});
		cenaPDVKol.setEditable(false);



		ukupnoKol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,Number>, ObservableValue<Number>>() {

			@Override
			public ObservableValue<Number> call(CellDataFeatures<Proizvodi, Number> param) {
				SimpleDoubleProperty sp=new SimpleDoubleProperty(param.getValue().getCijenaUkupnoDb());

				return sp;
			}
		});
		ukupnoKol.setCellFactory(new Callback<TableColumn<Proizvodi,Number>, TableCell<Proizvodi,Number>>() {

			@Override
			public TableCell<Proizvodi, Number> call(TableColumn<Proizvodi, Number> param) {
				TextFieldTableCell<Proizvodi, Number> tf=new TextFieldTableCell<>();
				tf.setAlignment(Pos.CENTER_RIGHT);
				tf.setConverter(new StringConverter<Number>() {

					@Override
					public String toString(Number object) {
						// TODO Auto-generated method stub
						return object.toString();
					}

					@Override
					public Number fromString(String string) {
						Number a;
						try{
							a=Double.parseDouble(string);
						}
						catch(NumberFormatException e){
							up.setPoruka("Nije ispravan broj");
							up.prikazi();
							return 0;
						}
						return a;
					}
				});
				return tf;

			}
		});
		ukupnoKol.setEditable(false);


		izbaci.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,Boolean>, ObservableValue<Boolean>>() {

			@Override
			public ObservableValue<Boolean> call(CellDataFeatures<Proizvodi, Boolean> param) {
				// TODO Auto-generated method stub
				return new SimpleBooleanProperty(param.getValue()!=null);
			}
		});
		izbaci.setCellFactory(new Callback<TableColumn<Proizvodi,Boolean>, TableCell<Proizvodi,Boolean>>() {

			@Override
			public TableCell<Proizvodi, Boolean> call(TableColumn<Proizvodi, Boolean> param) {
				ButtonCelija bc=new ButtonCelija();
				bc.dugme.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						Proizvodi tr=bc.getTableView().getItems().get(bc.getTableRow().getIndex());
						bc.getTableView().getItems().remove(bc.getTableRow().getIndex());
						postaviZbirove(tr,true);
						if(RacuniServis.akcija == Akcija.IZMJENI){
							boolean cont=false;
							for(Proizvodi b : RacuniServis.ucitaniProizvodi){
								if(b.getSifra().equals(tr.getSifra()))
									cont=true;
							}
							if(cont){
								RacuniServis.izbaceniProizvodi.add(tr);
							}
						}
					}
				});
				return bc;
			}
		});

		uslugeKol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,Boolean>, ObservableValue<Boolean>>() {

			@Override
			public ObservableValue<Boolean> call(CellDataFeatures<Proizvodi, Boolean> param) {
				SimpleBooleanProperty sp=new SimpleBooleanProperty(param.getValue().isUsluge());

				sp.addListener(new ChangeListener<Boolean>() {

					@Override
					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
							Boolean newValue) {
						for(Proizvodi p : param.getTableView().getItems()){
							if(p.getSifra()!=param.getValue().getSifra() && p.isUsluge()){
								param.getValue().setUsluge(false);
								param.getTableView().refresh();
								return;
							}
						}
						param.getValue().setUsluge(newValue);
						postaviZbirove(param.getValue());
					}
				});
				return sp;
			}
		});
		uslugeKol.setCellFactory(new Callback<TableColumn<Proizvodi,Boolean>, TableCell<Proizvodi,Boolean>>() {

			@Override
			public TableCell<Proizvodi, Boolean> call(TableColumn<Proizvodi, Boolean> param) {
				CheckBoxTableCell<Proizvodi, Boolean> celija=new CheckBoxTableCell<>();
				return celija;
			}
		});
		uslugeKol.setEditable(true);

		tabela2.setEditable(true);
		tabela2.getSelectionModel().setCellSelectionEnabled(true);
		tabela2.getColumns().addAll(sifraKol, nazivKol,kolicinaKol,cenaKol, rabatKol, cenaRabKol, pdvKol, cenaPDVKol, ukupnoKol, maliMKol, velikiMKol, uslugeKol, izbaci);
	}


	public void postaviZbirove(Proizvodi unos2,boolean...bs){
		tabela.refresh();
		if(unos2!=null){
			if(bs.length>0){
				obracun.izbaci(unos2);
			}
			else{
				obracun.stavi(unos2);
			}
		}
		ukupnoG.setText(obracun.getGlavnicaUDb()+"");
		ukupnoPDV.setText(obracun.getPdvUDb()+"");
		ukupnoC.setText(obracun.getUkupnoSve()+"");
	}

	public void resetujProizvode(){
		tabela.getItems().clear();
		obracun.isprazni();

		ukupnoG.setText(obracun.getGlavnicaUDb()+"");
		ukupnoPDV.setText(obracun.getPdvUDb()+"");
		ukupnoC.setText(obracun.getUkupnoSve()+"");
	}
	public static void main(String[] args) {
		launch(args);
	}

}
