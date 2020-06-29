package zigma;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ButtonType;

import java.io.StringWriter;
import java.io.PrintWriter;

import lib.dir.ValidateConsole;

/**
 * Main class of Console program
 */
public class Console extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("console_fx_sketch.fxml"));
			loader.setController(new ConsoleController());

			Parent root = loader.load();

			primaryStage.setScene(new Scene(root));
			primaryStage.setTitle("Console[Z I G M A]");
			primaryStage.show();
		} catch(Exception ex) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Start up error");
			alert.setHeaderText("Something went wrong with content loading");
			alert.setContentText(ex.getMessage());

			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));

			Label lbl = new Label("The exception stack trace was:");
			TextArea txtArea = new TextArea(sw.toString());
			txtArea.setEditable(false);
			txtArea.setWrapText(true);

			txtArea.setMaxWidth(Double.MAX_VALUE);
			txtArea.setMaxHeight(Double.MAX_VALUE);
			GridPane.setVgrow(txtArea, Priority.ALWAYS);
			GridPane.setHgrow(txtArea, Priority.ALWAYS);

			GridPane pane = new GridPane();
			pane.setMaxWidth(Double.MAX_VALUE);
			pane.add(lbl, 0, 0);
			pane.add(txtArea, 0, 1);

			alert.getDialogPane().setExpandableContent(pane);
			alert.showAndWait();
		}
	}

	public static void main(String[] args) {
		ValidateConsole vc = new ValidateConsole();
		if(vc.validate()) {
			launch(args);
		} else {
			System.err.println("Invalid software!");
		}
	}
}