package Controller;

import Model.Dialogs;
import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 * controller class for adding a new product.
 * handles user interactions on add product form.
 */
public class AddProductController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML private TextField productName;
    @FXML private TextField productInv;
    @FXML private TextField productPrice;
    @FXML private TextField productMax;
    @FXML private TextField productMin;
    @FXML private TextField searchTopTable;
    @FXML private TableView<Part> allPartsTable;
    @FXML private TableView<Part> associatedPartsTable;

    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * initializes controller class.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //initialize allPartsTable with all parts from Inventory
        allPartsTable.setItems(Inventory.getAllParts());

        //initialize columns for allPartsTable
        allPartsIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        allPartsInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        //initialize associatedPartsTable with associated parts list from Inventory
        associatedPartsTable.setItems(associatedParts);

        //initialize columns for associatedPartsTable
        associatedPartsIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        associatedPartsInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }

    @FXML
    private TableColumn<Part, Integer> allPartsIDColumn;

    @FXML
    private TableColumn<Part, Integer> allPartsInvColumn;

    @FXML
    private TableColumn<Part, String> allPartsNameColumn;

    @FXML
    private TableColumn<Part, Double> allPartsPriceColumn;

    @FXML
    private TableColumn<Part, Integer> associatedPartsIDColumn;

    @FXML
    private TableColumn<Part, Integer> associatedPartsInvColumn;

    @FXML
    private TableColumn<Part, String> associatedPartsNameColumn;

    @FXML
    private TableColumn<Part, Double> associatedPartsPriceColumn;

    /**
     * handler for adding a part to the associated parts list.
     * @param event
     */
    @FXML
    void addHandler(ActionEvent event) {

        //Get selected part from top table
        Part selectedPart = allPartsTable.getSelectionModel().getSelectedItem();

        //Check if part is selected
        if (selectedPart != null) {
            associatedParts.add(selectedPart); //add selected part to associated parts list
            associatedPartsTable.setItems(associatedParts); //update bottom table
        } else {
            Dialogs.showErrorDialog("Invalid selection", "Please select a part"); //if no part is selected, show message
        }
    }

    /**
     * handler for cancel button. closes add product form and returns to main form.
     * @param event
     * @throws IOException
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
     * handler for removing a part from associated parts list.
     * @param event
     */
    @FXML
    void removeHandler(ActionEvent event) {

        //Get selected part from bottom table
        Part selectedPart = associatedPartsTable.getSelectionModel().getSelectedItem();

        //Check if a part is selected
        if (selectedPart != null) {
            boolean confirm = Dialogs.showConfirmationDialog("Confirm Remove", "Remove Part", "Are you sure you want to remove this part?");
            if (confirm) {
                associatedParts.remove(selectedPart); //remove selected part from associated parts list
                associatedPartsTable.setItems(associatedParts); //update bottom table
            } else {
                Dialogs.showErrorDialog("Invalid selection", "Please select a part to remove"); //show message if no part is selected
            }
        }
    }

    /**
     * Handles save button. Validates input fields, creates new product object,
     * adds product to inventory, redirects to main form after saving.
     * @param event
     * @throws IOException
     */
    @FXML
    void saveHandler(ActionEvent event) throws IOException {

        //Validate form--check for empty name field
        if (!Dialogs.validateTextField(productName, "A product name is required")) {
            return;
        }

        try {
            //Retrieve values from form's text fields
            String name = productName.getText();
            double price = Double.parseDouble(productPrice.getText());
            int stock = Integer.parseInt(productInv.getText());
            int min = Integer.parseInt(productMin.getText());
            int max = Integer.parseInt(productMax.getText());

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

            //Create new product object with gathered values
            Product newProduct = new Product(Inventory.getNewProductID(), name, price, stock, min, max);

            //Add new product to inventory
            Inventory.addProduct(newProduct);

            //Redirect to main form after saving product
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/MainFormfxml.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }

            //catches and prints string to number conversion error
            catch(NumberFormatException e) {
                Dialogs.showErrorDialog("Input Error","Please enter valid values in text fields");
            }

            //catches and prints error for input/output operation issue
            catch(IOException e) {
                e.printStackTrace(); //prints more details about error
            }

    }

    /**
     * Method to search top table for parts by ID or name.
     * @param searchText the text to search for.
     * @return an observablelist of parts that match search criteria.
     */
    public ObservableList<Part> searchTopTable(String searchText) {
        ObservableList<Part> foundParts = FXCollections.observableArrayList();
        for (Part part : Inventory.getAllParts()) {
            boolean matchesId = String.valueOf(part.getId()).equals(searchText);
            boolean matchesName = part.getName().toLowerCase().contains(searchText.toLowerCase());
            if (matchesId || matchesName) {
                foundParts.add(part);
            }
        }
        return foundParts; //empty if no parts are found
    }


    /**
     * handler for searching in the top table of the add product form.
     * @param event
     */
    @FXML
    void searchHandler(ActionEvent event) {
        String searchText = searchTopTable.getText().trim();
        ObservableList<Part> foundParts = searchTopTable(searchText);


        if (foundParts.isEmpty()) {
            Dialogs.showErrorDialog("Invalid Selection", "No parts found");
            allPartsTable.setItems(Inventory.getAllParts()); //reset top table to show all parts
        } else {
            allPartsTable.setItems(foundParts); //display only found parts
        }
    }



}

