public class Order {
    private String order;
    private String specialInstruction;
    private double price;

    /**
     * no-arg constructor
     */
    public Order() {}

    /**
     * Constructor
     * @param order
     * @param price
     */
    public Order(String order, double price) {
        this.order = order;
        this.price = price;
    }

    /**
     * Equals method
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order1 = (Order) o;
        if (Double.compare(order1.price, price) != 0) return false;
        if (order != null ? !order.equals(order1.order) : order1.order != null) return false;
        return specialInstruction != null ? specialInstruction.equals(order1.specialInstruction) : order1.specialInstruction == null;
    }

    /**
     * gets order
     * @return order
     */
    public String getOrder() {
        return order;
    }

    /**
     * sets order
     * @param order
     */
    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * gets special instructions
     * @return
     */
    public String getSpecialInstruction() {
        return specialInstruction;
    }

    /**
     * sets special instruction
     * @param specialInstruction
     */
    public void setSpecialInstruction(String specialInstruction) {
        this.specialInstruction = specialInstruction;
    }

    /**
     * gets price
     * @return
     */
    public double getPrice() {
        return price;
    }

    /**
     * sets price
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * toString method
     * @return
     */
    @Override
    public String toString() {
        return "Order{" +
                "order='" + order + '\'' +
                ", specialInstruction='" + specialInstruction + '\'' +
                ", price=" + price +
                '}';
    }
}
