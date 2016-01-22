package oprema.aplikacija;

import java.io.IOException;

import exelProba.Proba;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoaderWindow extends Stage {
	private Proba gen;
	public LoaderWindow(Proba gen) throws IOException{
		FXMLLoader load=new FXMLLoader(this.getClass().getClassLoader().getResource("loader.fxml"));
		VBox vb=(VBox)load.load();
		Scene scena=new Scene(vb);
		this.setScene(scena);
		this.sizeToScene();
		this.initModality(Modality.APPLICATION_MODAL);
		this.centerOnScreen();
		this.gen=gen;
	}

	public void odradi(){
		Stage ovaj=this;
		Thread upis=new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000l);
					gen.generisi();
					Platform.runLater(()->{ovaj.close();});
				}catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					Upozorenje up=new Upozorenje();
					up.setPoruka("Ne može se upisati račun u izabrani fajl, probajte opet");
					up.prikazi();
				}
			}
		});
		upis.start();
		this.showAndWait();



	}



}
