package Model;

/**
 * represents an outsourced part.
 * extends the part class and adds a company name to a specified outsourced product.
 */
public class Outsourced extends Part {
    private String companyName;

    /**
     * constructs an outsourced part with the specified attributes.
     *
     * @param id ID of part.
     * @param name name of part.
     * @param price price of part.
     * @param stock inventory level of part.
     * @param min minimum allowable stock for the part.
     * @param max maximum allowable stock for the part.
     * @param companyName the name of the company that provides the part.
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
            super(id, name, price, stock, min, max);
            this.companyName = companyName;
    }

    /**
     * sets company name for the outsourced part.
     * @param companyName
     */
    public void setCompanyName(String companyName) {

        this.companyName = companyName;
    }

    /**
     * gets the company name for the outsourced part.
     * @return
     */
    public String getCompanyName() {

        return companyName;
    }
}
