package oprema.aplikacija;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Upozorenje extends Stage implements EventHandler<ActionEvent>{

	public Upozorenje(String poruka){
		this.initModality(Modality.APPLICATION_MODAL);
		VBox kontrole=new VBox();
		kontrole.getChildren().add(new Label(poruka));
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
	public void prikazi(){
		this.show();
		this.centerOnScreen();
	}
	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
		this.close();
	}
}
