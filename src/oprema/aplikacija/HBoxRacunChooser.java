package oprema.aplikacija;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;

public class HBoxRacunChooser extends HBox implements EventHandler<ActionEvent> {
	private String ime;
	private Label labela = new Label();
	private Button dupliraj = new Button();
	private Button izbrisi = new Button();
	private Button izmjeni = new Button();

	public HBoxRacunChooser(String ime){
		this.ime=ime;
		labela.setMaxWidth(Double.MAX_VALUE);
		labela.setText(ime);
		labela.prefWidthProperty().bind(this.widthProperty().subtract(250));
		dupliraj.setText("Dupliraj");
		dupliraj.setOnAction(this);
		dupliraj.setUserData(Akcija.DUPLIRAJ);
		izbrisi.setText("Poništi");
		izbrisi.setOnAction(this);
		izbrisi.setUserData(Akcija.IZBRISI);
		izmjeni.setText("Izmeni");
		izmjeni.setOnAction(this);
		izmjeni.setUserData(Akcija.IZMJENI);
		this.getChildren().addAll(labela,dupliraj,izmjeni,izbrisi);
		this.setFillHeight(true);
		this.setPadding(new Insets(5));
	}

	@Override
	public void handle(ActionEvent event) {
		Button b = (Button)event.getSource();
		Akcija akcija = (Akcija)b.getUserData();
		WindowRacunChooser par = (WindowRacunChooser) b.getScene().getWindow();
		par.odradi(this.ime, akcija);
	}

	public enum Akcija {
		DUPLIRAJ, IZMJENI, IZBRISI
	}
}
