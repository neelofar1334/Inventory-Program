package Controller;
import Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Parent;

/**
 * controller class for modifying a part within the modify form.
 */
public class ModifyPartController implements Initializable {

    Stage stage;
    Parent scene;

    /**
     * method for populating the form fields with the selected part's data.
     * @param part the part to modify.
     */
    public void setPart(Part part) {
        // The part that was selected to modify
        populateForm(part);
        populateForm(part);
    }

    /**
     * initializes controller class.
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("I am initialized");
    }

    //set text fields with part's attributes
    @FXML
    private TextField partID;
    @FXML
    private TextField partName;
    @FXML
    private TextField partInv;
    @FXML
    private TextField partPrice;
    @FXML
    private TextField partMax;
    @FXML
    private TextField partMin;
    @FXML
    private TextField companyMachineIDField;
    @FXML
    private Label companyMachineIDLabel;
    @FXML
    private RadioButton inHouseRadioButton;
    @FXML
    private RadioButton outsourcedRadioButton;

    /**
     * method that populates modify part form with selected part's attributes.
     * @param part the part whose data is used.
     */
    private void populateForm(Part part) {

        //set text fields with part's attributes
        partID.setText(String.valueOf(part.getId()));
        partName.setText(part.getName());
        partInv.setText(String.valueOf(part.getStock()));
        partPrice.setText(String.valueOf(part.getPrice()));
        partMax.setText(String.valueOf(part.getMax()));
        partMin.setText(String.valueOf(part.getMin()));

        //determine if part is inHouse or outsourced, check appropriate button
        if (part instanceof InHouse) {
            inHouseRadioButton.setSelected(true);
            companyMachineIDLabel.setText("Machine ID");
            companyMachineIDField.setText(String.valueOf(((InHouse) part).getMachineId()));
        } else if (part instanceof Outsourced) {
            outsourcedRadioButton.setSelected(true);
            companyMachineIDLabel.setText("Company Name");
            companyMachineIDField.setText(((Outsourced) part).getCompanyName());
        }
    }

    /**
     * handler for cancel button. closes modify part form and returns to the main form.
     * @param event
     */
    @FXML
    void cancelHandler(ActionEvent event) {
        System.out.println("cancel button clicked");
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
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
     * handles inHouse radio button. sets label to "machine ID".
     * @param event
     */
    @FXML
    void handleInHouseButton(ActionEvent event) {

        outsourcedRadioButton.setSelected(false);
        companyMachineIDLabel.setText("Machine ID");
    }

    /**
     * handles outsourced radio button. sets label to "company name".
     * @param event
     */
    @FXML
    void handleOutsourcedButton(ActionEvent event) {

        inHouseRadioButton.setSelected(false);
        companyMachineIDLabel.setText("Company Name");

    }

    /**
     * handler for save button.
     * validates form fields, updates the part, saves the changes, redirects to main form.
     * @param event
     */
    @FXML
    void saveHandler(ActionEvent event) {

        //Validate form--check for empty name field
        if (!Dialogs.validateTextField(partName, "A part name is required")) {
            return;
        }

        try {
            //retrieve and parse part's info from text fields
            int id = Integer.parseInt(partID.getText());
            String name = partName.getText();
            double price = Double.parseDouble(partPrice.getText());
            int stock = Integer.parseInt(partInv.getText());
            int min = Integer.parseInt(partMin.getText());
            int max = Integer.parseInt(partMax.getText());

            //Check if there are negative numbers in min, max, or inv, prints error if so
            if (stock < 0 || min < 0 || max < 0) {
                Dialogs.showErrorDialog("Input Error", "Inventory, Min, and Max must be non-negative.");
                return;
            }

            //Check if min is greater than max, prints error if it is
            if (min > max) {
                Dialogs.showErrorDialog("Invalid Input", "min should be less than or equal to max");
                return;
            }

            //Check if inventory is between max and min, prints error if it isn't
            if (stock < min || stock > max) {
                Dialogs.showErrorDialog("Invalid Inventory", "inventory should be between min and max");
                return;
            }

            //check if inHouse button is selected then creates inHouse object
            if (inHouseRadioButton.isSelected()) {
                int machineId = Integer.parseInt(companyMachineIDField.getText());
                InHouse updatedPart = new InHouse(id, name, price, stock, min, max, machineId);
                Inventory.updatePart(id, updatedPart);

                //check if outsourced button is selected, then creates outsourced object
            } else if (outsourcedRadioButton.isSelected()) {
                String companyName = companyMachineIDField.getText();
                Outsourced updatedPart = new Outsourced(id, name, price, stock, min, max, companyName);
                Inventory.updatePart(id, updatedPart);
            }

            //Redirect to main form after saving part
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
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

