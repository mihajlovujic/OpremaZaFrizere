package oprema.aplikacija;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import oprema.model.Proizvodi;

public class ButtonCelija extends TableCell<Proizvodi, Boolean> {
	Button dugme=new Button("Pritisni");


	public ButtonCelija() {
		super();
		this.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode().equals(KeyCode.ENTER))
					dugme.fire();
			}
		});
	}


	@Override
	protected void updateItem(Boolean item, boolean empty) {
		// TODO Auto-generated method stub
		super.updateItem(item, empty);
		if(!empty){

			setGraphic(dugme);
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		}
	}

}
