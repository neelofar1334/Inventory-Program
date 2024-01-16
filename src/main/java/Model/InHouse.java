package Model;

/**
 * represents an inHouse part.
 * extends the Part class and adds a machine ID to inHouse specific items.
 */
public class InHouse extends Part {
    private int machineId;

    /**
     * constructs an inHouse part with the specified attributes.
     * @param id ID of part.
     * @param name name of part.
     * @param price price of part.
     * @param stock inventory level of part.
     * @param min minimum allowable inventory/stock of part.
     * @param max maximum allowable inventory/stock of part.
     * @param machineId machineID associated with this part.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * sets machineID for this inHouse part.
     * @param machineId
     */
    public void setMachineId(int machineId) {

        this.machineId = machineId;
    }

    /**
     * returns machineID for this inHouse part.
     * @return
     */
    public int getMachineId() {

        return machineId;
    }
}
