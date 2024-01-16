package karimi.firstscreen;

import Controller.MainFormController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;


import java.io.IOException;

/**
 * This is the main class. It extends the application and sets up a primary stage for the UI.
 *
 * FUTURE_ENHANCEMENT: allow users to export and import data.
 * Description:
 * enable users to read and write to files for data sharing, backup, and analysis purposes.
 * Implementation
 * export: users can select CSV or Excel file formats and the location they want to save the file. Each row on the exported file will be a part or product.
 * import: users can import data from a pre-formatted CSV or Excel file. New parts/products will be added to the inventory, exisiting ones will be updated.
 * error handling: validation checks will be in place to keep unique IDs. validating imported data for the correct headers and data types will also be needed.
 */

public class HelloApplication extends Application {

    /**
     * Starts the application and sets up the main window (stage).
     * @param stage primary stage for application.
     * @throws IOException if there is an error loading the FXML file for the main form.
     */
    @Override
    public void start(Stage stage) throws IOException {

        //Load main form
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainFormfxml.fxml"));
        Parent root = loader.load();

        //Get controller
        MainFormController controller = loader.getController();


        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main entry point, called after the initialization method has returned.
     * @param args command line args passed to application.
     */
        public static void main (String[]args){
            launch(args);
        }
}