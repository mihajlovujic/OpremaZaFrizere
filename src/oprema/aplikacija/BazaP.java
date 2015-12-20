package oprema.aplikacija;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import oprema.model.Kupac;
import oprema.model.Proizvodi;
import oprema.model.ProizvodiServis;

public class BazaP extends Stage{

	private ProizvodiServis ps;
	private Upozorenje up;

	public BazaP(ProizvodiServis p, Upozorenje u) throws IOException{
		ps=p;
		up=u;

		this.initModality(Modality.APPLICATION_MODAL);
		FXMLLoader loader=new FXMLLoader(this.getClass().getClassLoader().getResource("PregledBaza.fxml"));

		loader.setController(this);
		this.setScene(new Scene(loader.load()));
	}

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField dodajNazivK;

    @FXML
    private TextField traziNazivK;

    @FXML
    private TextField traziNazivP;

    @FXML
    private TableView<Proizvodi> tabelaP;

    @FXML
    private TextField dodajCijenaP;

    @FXML
    private TextField dodajNazivP;

    @FXML
    private TextField dodajPIBK;

    @FXML
    private TableView<Kupac> tabelaK;

    @FXML
    private TextField dodajMjestoK;

    @FXML
    private Button unesiK;

    @FXML
    private TextField dodajRabatP;

    @FXML
    private TextField dodajMaliMagacinP;

    @FXML
    private TextField traziPIBK;

    @FXML
    private Button unosP;

    @FXML
    private TextField traziSifraP;

    @FXML
    private TextField dodajSifraP;

    @FXML
    private TextField dodajVelikiMagacinP;

    @FXML
    private TextField dodajAdresaK;

    @FXML
    private TextField dodajPDVP;

    @FXML
    void traziSifraPAkcija(ActionEvent event) {
    	List<Proizvodi> rez=ps.getProizvodSifraLike(traziSifraP.getText());
    	tabelaP.setItems(FXCollections.observableArrayList(rez));
    	tabelaP.refresh();
    }

    @FXML
    void traziNazivPAkcija(ActionEvent event) {
    	List<Proizvodi> rez=ps.getProizvodNazivLike(traziNazivP.getText());
    	tabelaP.setItems(FXCollections.observableArrayList(rez));
    	tabelaP.refresh();
    }

    @FXML
    void dodajProizvodAkcija(ActionEvent event) {
    	String sifra=dodajSifraP.getText();
    	if(sifra.length()==0 || ps.getProizvodPoSifri(sifra)!=null){
    		up.setPoruka("Ne može se dodati u bazu proizvod sa datom šifrom");
    		up.prikazi();
    		return;
    	}
    	Proizvodi rez=new Proizvodi();
    	rez.setSifra(sifra);
    	rez.setNaziv(dodajNazivP.getText());
    	try{
    		rez.setCijena(Double.parseDouble(dodajCijenaP.getText()));
    	}
    	catch(NumberFormatException e){
    		up.setPoruka("Cijena nije dobro upisana");
    		up.prikazi();
    		return;
    	}
    	try{
    		rez.setRabat(Integer.parseInt(dodajRabatP.getText()));
    	}
    	catch(NumberFormatException e){
    		up.setPoruka("Rabat nije dobro upisan");
    		up.prikazi();
    		return;
    	}
    	try{
    		rez.setPdv(Integer.parseInt(dodajPDVP.getText()));
    	}
    	catch(NumberFormatException e){
    		up.setPoruka("PDV nije dobro upisan");
    		up.prikazi();
    		return;
    	}
    	try{
    		rez.setMaliStanje(Integer.parseInt(dodajMaliMagacinP.getText()));
    	}
    	catch(NumberFormatException e){
    		up.setPoruka("Stanje u malom magacinu nije dobro upisano");
    		up.prikazi();
    		return;
    	}
    	try{
    		rez.setVelikiStanje(Integer.parseInt(dodajVelikiMagacinP.getText()));
    	}
    	catch(NumberFormatException e){
    		up.setPoruka("Stanje u velikom magacinu nije dobro upisano");
    		up.prikazi();
    		return;
    	}
    	ps.apdejtuj(rez);
    	tabelaP.getItems().add(rez);
    	tabelaP.refresh();
    }

    @FXML
    void traziPIBKAkcija(ActionEvent event) {
    	List<Kupac> rez=ps.poPibuLike(traziPIBK.getText());
    	tabelaK.setItems(FXCollections.observableArrayList(rez));
    	tabelaK.refresh();
    }

    @FXML
    void traziNazivKAkcija(ActionEvent event) {
    	List<Kupac> rez=ps.poNazivuLike(traziNazivK.getText());
    	tabelaK.setItems(FXCollections.observableArrayList(rez));
    	tabelaK.refresh();
    }

    @FXML
    void unesiKupcaAkcija(ActionEvent event) {
    	Kupac k=new Kupac();
    	if(dodajPIBK.getText().length()==0){
    		up.setPoruka("Nije upisan PIB za novog kupca");
    		up.prikazi();
    		return;
    	}
    	k.setPib(dodajPIBK.getText());
    	k.setNaziv(dodajNazivK.getText());
    	k.setAdresa(dodajAdresaK.getText());
    	k.setMjesto(dodajMjestoK.getText());
    	ps.apdejtujKupca(k);
    	tabelaK.getItems().add(k);
    	tabelaK.refresh();
    }

    @FXML
    void initialize() {
        assert dodajNazivK != null : "fx:id=\"dodajNazivK\" was not injected: check your FXML file 'PregledBaza.fxml'.";
        assert traziNazivK != null : "fx:id=\"traziNazivK\" was not injected: check your FXML file 'PregledBaza.fxml'.";
        assert traziNazivP != null : "fx:id=\"traziNazivP\" was not injected: check your FXML file 'PregledBaza.fxml'.";
        assert tabelaP != null : "fx:id=\"tabelaP\" was not injected: check your FXML file 'PregledBaza.fxml'.";
        assert dodajCijenaP != null : "fx:id=\"dodajCijenaP\" was not injected: check your FXML file 'PregledBaza.fxml'.";
        assert dodajNazivP != null : "fx:id=\"dodajNazivP\" was not injected: check your FXML file 'PregledBaza.fxml'.";
        assert dodajPIBK != null : "fx:id=\"dodajPIBK\" was not injected: check your FXML file 'PregledBaza.fxml'.";
        assert tabelaK != null : "fx:id=\"tabelaK\" was not injected: check your FXML file 'PregledBaza.fxml'.";
        assert dodajMjestoK != null : "fx:id=\"dodajMjestoK\" was not injected: check your FXML file 'PregledBaza.fxml'.";
        assert unesiK != null : "fx:id=\"unesiK\" was not injected: check your FXML file 'PregledBaza.fxml'.";
        assert dodajRabatP != null : "fx:id=\"dodajRabatP\" was not injected: check your FXML file 'PregledBaza.fxml'.";
        assert dodajMaliMagacinP != null : "fx:id=\"dodajMaliMagacinP\" was not injected: check your FXML file 'PregledBaza.fxml'.";
        assert traziPIBK != null : "fx:id=\"traziPIBK\" was not injected: check your FXML file 'PregledBaza.fxml'.";
        assert unosP != null : "fx:id=\"unosP\" was not injected: check your FXML file 'PregledBaza.fxml'.";
        assert traziSifraP != null : "fx:id=\"traziSifraP\" was not injected: check your FXML file 'PregledBaza.fxml'.";
        assert dodajSifraP != null : "fx:id=\"dodajSifraP\" was not injected: check your FXML file 'PregledBaza.fxml'.";
        assert dodajVelikiMagacinP != null : "fx:id=\"dodajVelikiMagacinP\" was not injected: check your FXML file 'PregledBaza.fxml'.";
        assert dodajAdresaK != null : "fx:id=\"dodajAdresaK\" was not injected: check your FXML file 'PregledBaza.fxml'.";
        assert dodajPDVP != null : "fx:id=\"dodajPDVP\" was not injected: check your FXML file 'PregledBaza.fxml'.";
        napraviTabeluProizvoda();
        napraviTabeluKupaca();
    }

    private void napraviTabeluKupaca() {
		TableColumn<Kupac, String> pibKol=new TableColumn<Kupac, String>("PIB");
		TableColumn<Kupac, String> nazivKol=new TableColumn<Kupac, String>("Naziv");
		TableColumn<Kupac, String> adresaKol=new TableColumn<Kupac, String>("Adresa");
		TableColumn<Kupac, String> mjestoKol=new TableColumn<Kupac, String>("Mesto");

		pibKol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Kupac,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Kupac, String> param) {
				SimpleStringProperty sp=new SimpleStringProperty(param.getValue().getPib());
				return sp;
			}
		});

		pibKol.setCellFactory(TextFieldTableCell.forTableColumn());
		pibKol.setEditable(false);

		nazivKol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Kupac,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Kupac, String> param) {
				SimpleStringProperty sp=new SimpleStringProperty(param.getValue().getNaziv());
				sp.addListener(new ChangeListener<String>() {

					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {
						param.getValue().setNaziv(newValue);
						ps.apdejtujKupca(param.getValue());
						tabelaK.refresh();
					}
				});
				return sp;
			}
		});

		nazivKol.setCellFactory(TextFieldTableCell.forTableColumn());
		nazivKol.setEditable(true);


		adresaKol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Kupac,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Kupac, String> param) {
				SimpleStringProperty sp=new SimpleStringProperty(param.getValue().getAdresa());
				sp.addListener(new ChangeListener<String>() {

					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {
						param.getValue().setAdresa(newValue);
						ps.apdejtujKupca(param.getValue());
						tabelaK.refresh();
					}
				});
				return sp;
			}
		});

		adresaKol.setCellFactory(TextFieldTableCell.forTableColumn());
		adresaKol.setEditable(true);

		mjestoKol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Kupac,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Kupac, String> param) {
				SimpleStringProperty sp=new SimpleStringProperty(param.getValue().getMjesto());
				sp.addListener(new ChangeListener<String>() {

					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {
						param.getValue().setMjesto(newValue);
						ps.apdejtujKupca(param.getValue());
					}
				});
				return sp;
			}
		});
		mjestoKol.setCellFactory(TextFieldTableCell.forTableColumn());
		mjestoKol.setEditable(true);


		tabelaK.getSelectionModel().setCellSelectionEnabled(true);
		tabelaK.setEditable(true);

		tabelaK.getColumns().addAll(pibKol, nazivKol, adresaKol, mjestoKol);

		tabelaK.getItems().addAll(ps.sviKupci());

	}

	private void napraviTabeluProizvoda() {
		TableColumn<Proizvodi, String> sifraK=new TableColumn<>("Šifra proizvoda");
		TableColumn<Proizvodi, String> nazivK=new TableColumn<>("Naziv proizvoda");
		TableColumn<Proizvodi, Number> cenaK=new TableColumn<>("Cena");
		TableColumn<Proizvodi, Integer> rabatK=new TableColumn<>("Rabat");
		TableColumn<Proizvodi, Integer> pdvK=new TableColumn<>("PDV");
		TableColumn<Proizvodi, Integer> maliMK=new TableColumn<>("Mali M");
		TableColumn<Proizvodi, Integer> velikiMK=new TableColumn<>("Veliki M");
		TableColumn<Proizvodi, Boolean> izbaciK=new TableColumn<>("Izbaci");

		nazivK.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Proizvodi, String> param) {
				SimpleStringProperty sp=new SimpleStringProperty(param.getValue().getNaziv());
				return sp;
			}
		});
		nazivK.setCellFactory(TextFieldTableCell.forTableColumn());
		nazivK.setPrefWidth(150);
		nazivK.setMaxWidth(Double.MAX_VALUE);
		nazivK.setEditable(true);
		nazivK.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Proizvodi,String>>() {

			@Override
			public void handle(CellEditEvent<Proizvodi, String> event) {
				event.getRowValue().setNaziv(event.getNewValue());
				ps.apdejtuj(event.getRowValue());
				tabelaP.refresh();
			}
		});

		sifraK.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Proizvodi, String> param) {
				SimpleStringProperty sp=new SimpleStringProperty(param.getValue().getSifra());

				return sp;
			}
		});
		sifraK.setCellFactory(TextFieldTableCell.forTableColumn());
		sifraK.setPrefWidth(200);
		sifraK.setEditable(false);



		rabatK.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,Integer>, ObservableValue<Integer>>() {

			@Override
			public ObservableValue<Integer> call(CellDataFeatures<Proizvodi, Integer> param) {
				ObservableValue<Integer> si=new SimpleIntegerProperty(param.getValue().getRabat()).asObject();
				si.addListener(new ChangeListener<Number>() {

					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue,
							Number newValue) {
						param.getValue().setRabat(newValue.intValue());
						ps.apdejtuj(param.getValue());
						tabelaP.refresh();
					}
				});
				return si;
			}
		});
		rabatK.setCellFactory(new Callback<TableColumn<Proizvodi,Integer>, TableCell<Proizvodi,Integer>>() {

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
							return o;
						}
						catch(NumberFormatException e){
							up.setPoruka("Unesite validan ceo broj");
							up.prikazi();
							tf.setText("0");
							return 0;
						}
					}
				});
				return tf;
			}
		});
		rabatK.setEditable(true);


		pdvK.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,Integer>, ObservableValue<Integer>>() {

			@Override
			public ObservableValue<Integer> call(CellDataFeatures<Proizvodi, Integer> param) {
				ObservableValue<Integer> si=new SimpleIntegerProperty(param.getValue().getPdv()).asObject();
				si.addListener(new ChangeListener<Number>() {

					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue,
							Number newValue) {
						param.getValue().setPdv(newValue.intValue());
						ps.apdejtuj(param.getValue());
						tabelaP.refresh();
					}
				});
				return si;
			}
		});
		pdvK.setCellFactory(new Callback<TableColumn<Proizvodi,Integer>, TableCell<Proizvodi,Integer>>() {

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
		pdvK.setEditable(true);


		maliMK.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,Integer>, ObservableValue<Integer>>() {

			@Override
			public ObservableValue<Integer> call(CellDataFeatures<Proizvodi, Integer> param) {
				ObservableValue<Integer> si=new SimpleIntegerProperty(param.getValue().getMaliStanje()).asObject();
				si.addListener(new ChangeListener<Number>() {

					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue,
							Number newValue) {
						param.getValue().setMaliStanje(newValue.intValue());
						ps.apdejtuj(param.getValue());
						tabelaP.refresh();
					}
				});
				return si;
			}
		});
		maliMK.setCellFactory(new Callback<TableColumn<Proizvodi,Integer>, TableCell<Proizvodi,Integer>>() {

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
		maliMK.setEditable(true);


		velikiMK.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,Integer>, ObservableValue<Integer>>() {

			@Override
			public ObservableValue<Integer> call(CellDataFeatures<Proizvodi, Integer> param) {
				ObservableValue<Integer> si=new SimpleIntegerProperty(param.getValue().getVelikiStanje()).asObject();
				si.addListener(new ChangeListener<Number>() {

					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue,
							Number newValue) {
						param.getValue().setVelikiStanje(newValue.intValue());
						ps.apdejtuj(param.getValue());
						tabelaP.refresh();
					}
				});
				return si;
			}
		});
		velikiMK.setCellFactory(new Callback<TableColumn<Proizvodi,Integer>, TableCell<Proizvodi,Integer>>() {

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
		velikiMK.setEditable(true);

		cenaK.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,Number>, ObservableValue<Number>>() {

			@Override
			public ObservableValue<Number> call(CellDataFeatures<Proizvodi, Number> param) {
				SimpleDoubleProperty sp=new SimpleDoubleProperty(param.getValue().getCijenaDb());
				sp.addListener(new ChangeListener<Number>() {

					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue,
							Number newValue) {
						// TODO Auto-generated method stub
						param.getValue().setCijena(newValue.doubleValue());
						ps.apdejtuj(param.getValue());
						tabelaP.refresh();
					}
				});
				return sp;
			}
		});
		cenaK.setCellFactory(new Callback<TableColumn<Proizvodi,Number>, TableCell<Proizvodi,Number>>() {

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
		cenaK.setEditable(true);


		izbaciK.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Proizvodi,Boolean>, ObservableValue<Boolean>>() {

			@Override
			public ObservableValue<Boolean> call(CellDataFeatures<Proizvodi, Boolean> param) {
				// TODO Auto-generated method stub
				return new SimpleBooleanProperty(param.getValue()!=null);
			}
		});
		izbaciK.setCellFactory(new Callback<TableColumn<Proizvodi,Boolean>, TableCell<Proizvodi,Boolean>>() {

			@Override
			public TableCell<Proizvodi, Boolean> call(TableColumn<Proizvodi, Boolean> param) {
				ButtonCelija bc=new ButtonCelija();
				bc.dugme.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						Proizvodi tr=bc.getTableView().getItems().get(bc.getTableRow().getIndex());
						bc.getTableView().getItems().remove(bc.getTableRow().getIndex());
						ps.izbrisiProizvod(tr);
						tabelaP.refresh();
					}
				});
				return bc;
			}
		});

		tabelaP.setEditable(true);
		tabelaP.getSelectionModel().setCellSelectionEnabled(true);
		tabelaP.getColumns().addAll(sifraK, nazivK, cenaK, rabatK, pdvK, maliMK, velikiMK, izbaciK);
		tabelaP.getItems().addAll(ps.sviProizvodi());
	}

	public void prikazi(){
    	this.centerOnScreen();
    	this.setMaximized(true);
    	this.show();
    }
}

