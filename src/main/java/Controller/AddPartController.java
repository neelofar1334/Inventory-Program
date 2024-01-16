package Controller;

import Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Parent;


/**
 * Controller class for adding a new part.
 * This class handles user interactions on the add part form.
 */
public class AddPartController implements Initializable {

    Stage stage;
    Parent scene;

    /**
     * Initializes controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("I am initialized");
    }

        @FXML
        private RadioButton inHouse;

        @FXML
        private TextField inv;

        @FXML
        private TextField dynamicTextField;

        @FXML
        private Label dynamicLabel;

        @FXML
        private TextField partMax;

        @FXML
        private TextField partMin;

        @FXML
        private TextField partName;

        @FXML
        private TextField partPrice;

    /**
     * cancelHandler closes the add part form and returns to the main form.
     * @param event the event that triggered the method.
     * @throws IOException if that occurs.
     */
    @FXML
        void cancelHandler(ActionEvent event) {
            System.out.println("cancel button clicked");
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainFormfxml.fxml"));
                AnchorPane root = loader.load();
                MainFormController mainFormController = loader.getController();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    /**
     * Handles action for selecting inHouse radio button.
     * Sets a dynamic text field and label so the form can switch to inHouse specific fields.
     * @param event
     */
    @FXML
        void handleInHouseButton(ActionEvent event) {
            dynamicLabel.setText("Machine ID");
            dynamicTextField.setPromptText("Enter Machine ID");
            dynamicTextField.setVisible(true);
        }

    /**
     * Handles action for selecting outsourced radio button.
     * Sets a dynamic text field and label so the form can switch to outsourced specific fields.
     * @param event
     */
        @FXML
        void handleOutsourcedButton(ActionEvent event) {
            dynamicLabel.setText("Company Name");
            dynamicTextField.setPromptText("Enter Company Name");
            dynamicTextField.setVisible(true);
        }

    /**
     * Handles save button. Validates input fields, creates new part object (inHouse or outsourced),
     * adds part to inventory, redirects to main form after saving.
     * RUNTIME_ERROR: NumberFormatException
     * error description: if a user tries to input a value in a field that is not consistent with the data type in that field, this exception will be thrown.
     * how it was corrected: I implemented a try-catch block in this save handler (and in other controllers) along with an error message to the user in the UI.
     * I created a Dialogs utility class with my validation handling methods, which I applied to similar methods as the save method.
     * @param event
     * @throws IOException
     */

    @FXML
        void saveHandler(ActionEvent event) throws IOException {

            //Validate form--check for empty name field
            if (!Dialogs.validateTextField(partName, "A part name is required")) {
                return;
            }

            try {
            //Retrieve values from form's text fields
            String name = partName.getText();
            double price = Double.parseDouble(partPrice.getText());
            int stock = Integer.parseInt(inv.getText());
            int min = Integer.parseInt(partMin.getText());
            int max = Integer.parseInt(partMax.getText());

            //Check if there are negative numbers in min, max, or inv, prints error if so
                if (stock < 0 || min < 0 || max < 0) {
                    Dialogs.showErrorDialog("Input Error", "Inventory, Min, and Max must be non-negative.");
                    return;
                }

            //Check if min is greater than max, prints error if it is
            if(min > max) {
                Dialogs.showErrorDialog("Invalid Input", "min should be less than or equal to max");
                return;
            }

            //Check if inventory is between max and min, prints error if it isn't
            if (stock < min || stock > max) {
                Dialogs.showErrorDialog("Invalid Inventory", "inventory should be between min and max");
                return;
            }

            //variable declaration for a new part
            Part newPart;

            //Determine if inHouse button is selected
            if (inHouse.isSelected()) {
                String machineIdText = dynamicTextField.getText(); //retrieve machineID from dynamicTextField
                if (machineIdText == null || machineIdText.isEmpty()) { //check for empty or null machineID, prints error if it is
                    Dialogs.showErrorDialog("Invalid selection","Machine ID is required");
                    return;
                }
                int machineId;
                try {
                    machineId = Integer.parseInt(machineIdText.trim()); //parses machineID as an int, trims whitespace from beginning&end
                } catch (NumberFormatException e) { //check for non-numbers in machineID, prints error if so
                    Dialogs.showErrorDialog("Invalid input", "Machine ID must be a number");
                    return;
                }

                //Creates new InHouse part
                newPart = new InHouse(Inventory.getNewPartID(), name, price, stock, min, max, machineId);
                System.out.println("InHouse part created successfully");

            } else {
                //retrieves text (company name) from dynamicTextField for outsourced parts. trims whitespace from beginning&end
                String companyName = dynamicTextField.getText().trim();
                if (companyName.isEmpty()) { //checks if company name field is empty, prints error if so.
                    Dialogs.showErrorDialog("Invalid input", "Company Name is required");
                    return;
                }

                //Creates new Outsourced part
                newPart = new Outsourced(Inventory.getNewPartID(), name, price, stock, min, max, companyName);
                System.out.println("Outsourced part created successfully");
            }

            //Add new part to inventory
            Inventory.addPart(newPart);

            //Redirect to main form after saving part
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainFormfxml.fxml"));
                AnchorPane root = loader.load();
                MainFormController mainFormController = loader.getController();
                stage.setScene(new Scene(root));
                stage.show();

                //catches and prints string to number conversion error
            } catch (NumberFormatException e) {
                Dialogs.showErrorDialog("Input Error","Please enter valid values in text fields");

                //catches and prints error for input/output operation issue
            } catch (IOException e) {
                e.printStackTrace(); //prints more details about error
            }


            }

}
