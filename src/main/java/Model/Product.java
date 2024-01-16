package Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * represents a product in the inventory system.
 * a product can have multiple associated parts.
 */
public class Product {
    private Part part;
    private final ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * constructs a new product with the specified details.
     * @param id ID of product.
     * @param name name of product.
     * @param price price of product.
     * @param stock inventory level of product.
     * @param min minimum allowable stock for product.
     * @param max maximum allowable stock for product.
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        this.associatedParts = FXCollections.observableArrayList();
    }

    /**
     *
     * @param part
     */
    public void setPart (Part part) {

        this.part = part;
    }

    /**
     *
     * @return part
     */
    public Part getPart() {

        return part;
    }
    /**
     * @return the id
     */
    public int getId() {

        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {

        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {

        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {

        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {

        this.price = price;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {

        this.stock = stock;
    }

    /**
     * @return the min
     */
    public int getMin() {

        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {

        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {

        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {

        this.max = max;
    }

    /**
     * adds associated part to this product.
     * @param part the part associated with this product.
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * deletes associated part from this product.
     * @param selectedAssociatedPart the part to be removed.
     * @return true if part was removed, false otherwise.
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * retrieves all parts associated with this product.
     * @return an observable list containing the associated parts.
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
