package Controller;

import Model.Dialogs;
import Model.Part;
import Model.Product;
import Model.Inventory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Parent;

/**
 * controller class for the main form.
 * handles actions for the parts and products panes.
 * allows users to add, modify, delete, and search for products and parts.
 */
public class MainFormController extends javafx.scene.Parent implements Initializable {

    Stage stage;
    Parent scene;

    /**
     * initializes controller class.
     * sets up the table views and columns for parts and products.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //set tableviews
        partsTable.setItems(Inventory.getAllParts());
        productTable.setItems(Inventory.getAllProducts());

        //set columns for tableviews
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceParts.setCellValueFactory(new PropertyValueFactory<>("price"));
        inventoryLevelParts.setCellValueFactory(new PropertyValueFactory<>("stock"));

        productID.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceProduct.setCellValueFactory(new PropertyValueFactory<>("price"));
        inventoryLevelProduct.setCellValueFactory(new PropertyValueFactory<>("stock"));

    }

    @FXML
    private TextField searchParts;

    @FXML
    private TextField searchProducts;

    @FXML
    private TableColumn<Part, Integer> inventoryLevelParts;

    @FXML
    private TableColumn<Product, Integer> inventoryLevelProduct;

    @FXML
    private TableColumn<Part, Integer> partIDColumn;

    @FXML
    private TableColumn<Part, String> partName;

    @FXML
    private TableView<Part> partsTable;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Part, Double> priceParts;

    @FXML
    private TableColumn<Product, Double> priceProduct;

    @FXML
    private TableColumn<Product, Integer> productID;

    @FXML
    private TableColumn<Product, String> productName;

    /**
     * handles for adding parts.
     * opens the add part form.
     * @param event
     */
    @FXML
    void addPartsHandler(ActionEvent event) {
        System.out.println("add part button clicked");

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddPartForm.fxml"));
            AnchorPane root = loader.load();

            // Access the controller of the AddPartForm.fxml if needed
            AddPartController addPartController = loader.getController();

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * handler for adding products.
     * opens the add product form.
     * @param event
     */
    @FXML
    void addProductHandler(ActionEvent event) {
        System.out.println("add product button clicked");
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddProductForm.fxml"));
            AnchorPane root = loader.load();
            AddProductController addProductController = loader.getController();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * handler for deleting parts.
     * confirms and processes deletion of selected part.
     * @param event
     */
    @FXML
    void deletePartsHandler(ActionEvent event) {

        //Get selected part from parts table
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();

        //Check if part is actually selected
        if (selectedPart != null) {
            //confirm deletion of part
            boolean confirm = Dialogs.showConfirmationDialog("Confirm Delete", "Delete Part", "Are you sure you want to delete this part?");
            if (confirm) {
                boolean deleted = Inventory.deletePart(selectedPart); //delete part from inventory
                if (deleted) {
                    partsTable.getItems().remove(selectedPart); //if deleted, update parts table to remove the part
                } else {
                    Dialogs.showErrorDialog("Error", "The part could not be deleted");
                }
            } else {
                Dialogs.showErrorDialog("Selection Error", "No part is selected"); //show error message if no part is selected
            }
        }
    }

    /**
     * handler for deleting products.
     * confirms and processes deletion of selected product.
     * @param event
     */
    @FXML
    void deleteProductHandler(ActionEvent event) {
        //Get selected product from products table
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();

        //Check if product is actually selected
        if (selectedProduct != null) {
            if (!selectedProduct.getAllAssociatedParts().isEmpty()) { //check if product has any associated parts
                Dialogs.showErrorDialog("Cannot delete product", "Product has associated parts"); //show if product has parts
            } else {
                //confirm deletion of product
                boolean confirm = Dialogs.showConfirmationDialog("Confirm Delete", "Delete Product", "Are you sure you want to delete this product?");
                if (confirm) {
                    Inventory.deleteProduct(selectedProduct); //proceed with deletion
                    productTable.setItems(Inventory.getAllProducts()); //update tableview
                }
            }
        } else {
            Dialogs.showErrorDialog("Selection Error", "No product is selected"); //show error if no product is selected
        }
    }

    /**
     * handler for exiting the application.
     * @param event
     */
    @FXML
    void exitHandler(ActionEvent event) {

        System.exit(0);
    }

    /**
     * handler for modifying parts.
     * opens the modify part form for the selected part.
     * @param event
     */
    @FXML
    void modifyPartsHandler(ActionEvent event) {
        System.out.println("modify part button clicked");

        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ModifyPartForm.fxml"));
                AnchorPane root = loader.load();

                ModifyPartController modifyPartController = loader.getController();
                modifyPartController.setPart(selectedPart); // selectedPart to modify

                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * handler for modifying products.
     * opens the modify product form for the selected product.
     * @param event
     */
    @FXML
    void modifyProductHandler(ActionEvent event) {
        System.out.println("modify product button clicked");

        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ModifyProductForm.fxml"));
                AnchorPane root = loader.load();

                ModifyProductController modifyProductController = loader.getController();
                modifyProductController.setProduct(selectedProduct); // selected product to modify

                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to search for parts by ID or name.
     * @param searchText the text to search for.
     * @return an observablelist of parts that match the search criteria.
     */
    public ObservableList<Part> searchParts(String searchText) {
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
     * handler for searching for parts.
     * searches for parts by ID, name, or partial name.
     * updates parts table accordingly.
     * @param event
     */
    @FXML
    void searchHandlerParts(ActionEvent event) {

        String searchText = searchParts.getText().trim();
        ObservableList<Part> foundParts = searchParts(searchText);


        if (foundParts.isEmpty()) {
            Dialogs.showErrorDialog("Alert", "No parts found");
            partsTable.setItems(Inventory.getAllParts()); //reset table to show all parts
        } else {
            partsTable.setItems(foundParts); //display only found parts
        }
    }


    /**
     * Method to search for products by ID or name
     * @param searchText
     * @return an observablelist of products that match the search criteria
     */
    public ObservableList<Product> searchProducts(String searchText) {
        ObservableList<Product> foundProducts = FXCollections.observableArrayList();
        for (Product product : Inventory.getAllProducts()) {
            boolean matchesId = String.valueOf(product.getId()).equals(searchText);
            boolean matchesName = product.getName().toLowerCase().contains(searchText.toLowerCase());
            if (matchesId || matchesName) {
                foundProducts.add(product);
            }
        }
        return foundProducts; //empty if no products are found
    }

    /**
     * handler for searching for products.
     * searches for products by ID, name, or partial name.
     * updates products table accordingly.
     * @param event
     */
    @FXML
    void searchHandlerProducts(ActionEvent event) {
        String searchText = searchProducts.getText().trim();
        ObservableList<Product> foundProducts = searchProducts(searchText);


        if (foundProducts.isEmpty()) {
            Dialogs.showErrorDialog("Alert","No products found");
            productTable.setItems(Inventory.getAllProducts()); //resets table to show all products
        } else {
            productTable.setItems(foundProducts); //display only found products
        }
    }

}
