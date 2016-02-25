package oprema.aplikacija;

import java.util.List;

import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import oprema.aplikacija.HBoxRacunChooser.Akcija;
import oprema.model.RacuniServis;

public class WindowRacunChooser extends Stage {

	private VBox svi;
	public String izbrisani = null;
	public WindowRacunChooser(){

		this.initModality(Modality.APPLICATION_MODAL);
		this.setWidth(500);
		this.setHeight(500);
		VBox svi = new VBox(15);
		ScrollPane skrol=new ScrollPane(svi);
		svi.prefWidthProperty().bind(skrol.widthProperty());
		this.svi=svi;
		this.setScene(new Scene(skrol));

	}



	public void prikazi(){
		List<String> imena = RacuniServis.napravljeniRacuni();
		svi.getChildren().clear();
		for(String ime : imena){
			HBoxRacunChooser novi=new HBoxRacunChooser(ime);
			novi.prefWidthProperty().bind(svi.widthProperty());
			svi.getChildren().add(novi);
		}
		izbrisani = null;
		this.show();
	}



	public void odradi(String ime, Akcija akcija) {
		if(akcija.equals(Akcija.IZBRISI)){
			RacuniServis.izbrisi(ime);
			this.izbrisani = ime;
		}
		else{
			RacuniServis.ucitajRacun(ime);
			RacuniServis.akcija = akcija;

		}
		this.close();
	}
}
