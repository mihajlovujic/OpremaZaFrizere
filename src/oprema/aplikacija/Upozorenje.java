package oprema.aplikacija;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Upozorenje extends Stage implements EventHandler<ActionEvent>{
	private Label labela=new Label();
	private String poruka=new String();
	private TextArea ta=new TextArea();
	private VBox kontrole;

	public Upozorenje(){
		this.initModality(Modality.APPLICATION_MODAL);
		kontrole=new VBox();
		kontrole.getChildren().add(labela);
		kontrole.setAlignment(Pos.CENTER);
		Button dugme=new Button("Potvrdi");
		dugme.setOnAction(this);
		dugme.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub
				if(event.getCode().equals(KeyCode.ENTER))
					Platform.runLater(()->{dugme.fire();});
			}
		});
		kontrole.getChildren().add(dugme);
		this.setScene(new Scene(kontrole));
		this.sizeToScene();
	}

	public String getPoruka() {
		return poruka;
	}

	public void setPoruka(String por){
		this.poruka=por;
	}
	public void pripremiTekst(){
		ta.setText("");
	}
	public void dodajUArea(String tekst){
		ta.setText(ta.getText()+System.lineSeparator()+tekst);
	}
	public void prikazi(boolean... text){
		labela.setText(poruka);
		if(text.length>0){
			if(text[0]){
				if(!kontrole.getChildren().get(1).equals(ta))
					kontrole.getChildren().add(1, ta);
			}
		}
		else{
			if(kontrole.getChildren().get(1).equals(ta))
				kontrole.getChildren().remove(1);
		}
		this.show();
		this.centerOnScreen();
	}
	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
		this.close();
	}
}
