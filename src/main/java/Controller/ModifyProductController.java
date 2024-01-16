package Controller;

import Model.Dialogs;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.scene.Parent;
import javafx.scene.control.*;

/**
 * controller for modifying a product with the modify product form.
 * allows users to change product details.
 */

public class ModifyProductController implements Initializable {

    Stage stage;
    Parent scene;

    private Product selectedProduct; // The product that was selected to modify

    /**
     * populates form with the selected product's data.
     * @param product the product to be modified.
     */
    public void setProduct(Product product) {
        selectedProduct = product;
        populateForm(product);
        populateAssociatedPartsTable();
    }

    /**
     * initializes controller class.
     * sets up table views and columns for the all parts and associated parts tables.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("I am initialized");

        //initialize allPartsTable with all parts from Inventory
        allPartsTable.setItems(Inventory.getAllParts());

        //initialize columns for allPartsTable
        allPartsIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        allPartsInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        //initialize associatedPartsTable with associated parts list from Inventory
        associatedPartsTable.setItems(Inventory.getAllParts());

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
    private TableView<Part> allPartsTable;

    @FXML
    private TableColumn<Part, Integer> associatedPartsIDColumn;

    @FXML
    private TableColumn<Part, Integer> associatedPartsInvColumn;

    @FXML
    private TableColumn<Part, String> associatedPartsNameColumn;

    @FXML
    private TableColumn<Part, Double> associatedPartsPriceColumn;

    @FXML
    private TableView<Part> associatedPartsTable;


    @FXML
    private TextField productID;

    @FXML
    private TextField productInv;

    @FXML
    private TextField productMax;

    @FXML
    private TextField productMin;

    @FXML
    private TextField productName;

    @FXML
    private TextField productPrice;

    @FXML
    private TextField search;

    /**
     * method for populating form with selected product's attributes.
     * @param product the selected product.
     */
    private void populateForm(Product product) {

        //set text fields with part's attributes
        if (product != null) {
            productID.setText(String.valueOf(product.getId()));
            productName.setText(product.getName());
            productInv.setText(String.valueOf(product.getStock()));
            productPrice.setText(String.valueOf(product.getPrice()));
            productMax.setText(String.valueOf(product.getMax()));
            productMin.setText(String.valueOf(product.getMin()));
        }

    }

    /**
     * method for populating associated parts table with the selected product.
     */
    private void populateAssociatedPartsTable() {
        if (selectedProduct != null) {
            associatedPartsTable.setItems(selectedProduct.getAllAssociatedParts());
        }
    }

    /**
     * handler for adding a part to the associated parts list of the product.
     * @param event
     */
    @FXML
    void addHandler(ActionEvent event) {

        //Get selected part from top table
        Part selectedPart = allPartsTable.getSelectionModel().getSelectedItem();

        //Check if part is selected
        if (selectedPart != null) {
            selectedProduct.addAssociatedPart(selectedPart); //add selected part to associated parts list
            populateAssociatedPartsTable(); //update bottom table
        } else {
            Dialogs.showErrorDialog("Invalid selection","Please select a part"); //if no part is selected, show message
        }

    }

    /**
     * handler for cancelling the modifications. redirects to main form.
     * @param event
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
     * handler for removing a part from the associated parts list of the product.
     * does not delete the part from the allparts table.
     * @param event
     */
    @FXML
    void removeHandler(ActionEvent event) {

        Part selectedPart = associatedPartsTable.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            boolean confirm = Dialogs.showConfirmationDialog("Confirm Remove", "Remove Part", "Are you sure you want to remove this part?");
            if (confirm) {
                selectedProduct.deleteAssociatedPart(selectedPart);
                populateAssociatedPartsTable(); // Update the associated parts table
            } else {
                Dialogs.showErrorDialog("Invalid Selection", "No part selected to remove."); // Show error dialog if no part is selected
            }
        }
    }

    /**
     * handler for saving the modifications.
     * validates form fields, updates product object, saves changes, redirects to main form.
     * @param event
     */
    @FXML
    void saveHandler(ActionEvent event) {

        //Validate form--check for empty name field
        if (!Dialogs.validateTextField(productName, "A product name is required")) {
            return;
        }

        try {
            //retrieve and parse part's info from text fields
            int id = Integer.parseInt(productID.getText());
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

            Product updatedProduct = new Product(id, name, price, stock, min, max);

            // Loop through associated parts of selected product
            for (Part part : selectedProduct.getAllAssociatedParts()) {
                updatedProduct.addAssociatedPart(part);
            }

            //add them to updated product
            Inventory.updateProduct(id, updatedProduct);


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

    /**
     * method to search top table for parts by ID or name (full or partial name).
     * @param searchText the text to search for.
     * @return observablelist of parts that match search criteria.
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
     * handler for searching in the top table of the modify product form.
     * @param event
     */
    @FXML
    void searchHandler(ActionEvent event) {
        String searchText = search.getText().trim();
        ObservableList<Part> foundParts = searchTopTable(searchText);


        if (foundParts.isEmpty()) {
            Dialogs.showErrorDialog("Alert", "No parts found");
            allPartsTable.setItems(Inventory.getAllParts()); //reset top table to show all parts
        } else {
            allPartsTable.setItems(foundParts); //display only found parts
        }
    }

}

