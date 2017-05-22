public class OrderListNode {
    private Order data;
    private OrderListNode next;
    private OrderListNode prev;

    /**
     * constuctot
     * @param initData
     * @throws IllegalArgumentException
     */
    public OrderListNode(Order initData) throws IllegalArgumentException {
        if(initData == null)
            throw new IllegalArgumentException("Init Data is null");
        this.data = initData;
        this.next = null;
        this.prev = null;
    }

    /**
     *
     * @return data
     */
    public Order getData() {
        return data;
    }

    /**
     * sets data
     * @param data
     */
    public void setData(Order data) {
        this.data = data;
    }

    /**
     * gets next ref
     * @return
     */
    public OrderListNode getNext() {
        return next;
    }

    /**
     * sets next
     * @param next
     */
    public void setNext(OrderListNode next) {
        this.next = next;
    }

    /**
     * gets prev ref
     * @return
     */
    public OrderListNode getPrev() {
        return prev;
    }

    /**
     * sets prev ref
     * @param prev
     */
    public void setPrev(OrderListNode prev) {
        this.prev = prev;
    }

    /**
     * toString method
     * @return
     */
    @Override
    public String toString() {
        return "OrderListNode{" +
                "data=" + data +
                ", next=" + next +
                ", prev=" + prev +
                '}';
    }
}
