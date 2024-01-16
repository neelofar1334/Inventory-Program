package Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class contains the inventory system responsible for storage, management, and retrieval of parts and products.
 */
public class Inventory {
    private static int lastPartID = 0;
    private static int lastProductID = 0;
    private static final ObservableList<Part> allParts;
    private static final ObservableList<Product> allProducts;

    static {
        allParts = FXCollections.observableArrayList(

        );

        allProducts = FXCollections.observableArrayList(

        );
    }

    /**
     * Generate new unique partID.
     * @return the next available partID.
     */
    public static int getNewPartID() {
        lastPartID++;
        return lastPartID;
    }

    /**
     * Generate new unique productID.
     * @return the next available productID.
     */
    public static int getNewProductID() {
        lastProductID++;
        return lastProductID;
    }


     /**
     * adds new part to the inventory.
     * @param newPart the new part object to be added
     */
    public static void addPart(Part newPart) {

        allParts.add(newPart);
    }

    /**
     * adds new product to the inventory.
     * @param newProduct the new product object to be added
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * searches for a part in the inventory by its ID.
     * @param partId the ID of the part to look up.
     * @return part or null
     */
    public static Part lookupPart(int partId) {
        for (Part part : allParts) {    //for-each loop
            if (part.getId() == partId) {
                return part;
            }
        }
        return null;
    }

    /**
     * searches for a product in the inventory by its ID.
     * @param productId the ID of the product to look up.
     * @return product or null
     */
    public static Product lookupProduct(int productId) {
        for (Product product : allProducts) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    /**
     * searches for parts by name (case-insensitive).
     * @param partName name of Part to search for.
     * @return ObservableList containing found Part objects
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> foundParts = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if (part.getName().toLowerCase().contains(partName.toLowerCase())) {
                foundParts.add(part);
            }
        }
        return foundParts;
    }

    /**
     * searches for product by name (case-insensitive).
     * @param productName name of Product to search for.
     * @return ObservableList of found Product objects.
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> foundProducts = FXCollections.observableArrayList();
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(productName.toLowerCase())) {
                foundProducts.add(product);
            }
        }
        return foundProducts;
    }

    /**
     * updates a part in inventory, searches by part ID.
     * @param id the ID of the part to update.
     * @param updatedPart new part object to replace the old one.
     * @throws IllegalArgumentException if part with the specified ID is not found.
     */
    public static void updatePart(int id, Part updatedPart) {

        for(int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getId() == id) {
            allParts.set(i, updatedPart);
            return;
            }
        }
        throw new IllegalArgumentException("Part with ID " + id + " not found.");
    }

    /**
     * updates a product in inventory, searches by product ID.
     * @param id the ID of the product to update.
     * @param newProduct new product object to replace the old one.
     * @throws IllegalArgumentException if product with the specified ID is not found.
     */
    public static void updateProduct(int id, Product newProduct) {

        for(int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId() == id) {
                allProducts.set(i, newProduct);
                return;
            }
        }
        throw new IllegalArgumentException("Product with ID " + id + " not found.");
    }

    /**
     * removes specified part from the inventory.
     * @param selectedPart the part object to remove.
     * @return {@code true} if part is successfully removed, or false otherwise.
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * removes specified product from the inventory.
     * @param selectedProduct the product object to remove.
     * @return {@code true} if product is successfully removed, or false otherwise.
     */
    public static boolean deleteProduct(Product selectedProduct) {

        return allProducts.remove(selectedProduct);
    }

    /**
     * returns complete list of all parts in inventory.
     * @return ObservableList containing all parts.
     */
    public static ObservableList<Part> getAllParts(){

        return allParts;
    }

    /**
     * returns complete list of all products in inventory.
     * @return ObservableList containing all products.
     */
    public static ObservableList<Product> getAllProducts(){

        return allProducts;
    }
}
