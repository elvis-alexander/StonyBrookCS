
public class OrderList {
    private OrderListNode head;
    private OrderListNode tail;
    private OrderListNode cursor;
    private int size;


    /**
     * no-arg constructor
     */
    public OrderList() {
        this.head = null;
        this.tail = null;
        this.cursor = null;
        this.size = 0;
    }


    /**
     * gets cursor ref
     * @return
     */
    public OrderListNode getCursor() {
        return cursor;
    }

    /**
     *
     * @return size
     */
    public int numOrders() {
        return size;
    }

    /**
     *
     * @return cursor data
     */
    public Order getCursorOrder() {
        if(cursor == null)
            return null;
        return cursor.getData();
    }

    /**
     * resets cursor to head
     */
    public void resetCursorToHead() {
        this.cursor = this.head;
    }

    /**
     * resets cursor to tail
     */
    public void resetCursorToTail() {
        this.cursor = this.tail;
    }

    /**
     * moves cursor forward
     * @throws EndOfListException
     */
    public void cursorForward() throws EndOfListException {
        if(cursor == tail)
            throw new EndOfListException("Cursor is already at tail of list.");
        this.cursor = this.cursor.getNext();
    }

    /**
     * moves cursor backward
     * @throws EndOfListException
     */
    public void cursorBackward() throws EndOfListException {
        if(cursor == head)
            throw new EndOfListException("Cursor is already at head of list.");
        this.cursor = this.cursor.getPrev();
    }

    /**
     * inserts after cursor
     * @param newOrder
     * @throws IllegalArgumentException
     */
    public void insertAfterCursor(Order newOrder) throws IllegalArgumentException {
        if(newOrder == null)
            throw new IllegalArgumentException("NewOrder is null");
        OrderListNode newNode = new OrderListNode(newOrder);
        if(cursor == null) {
            this.head = newNode;
            this.tail = newNode;
        } else {
            OrderListNode next = this.cursor.getNext();
            newNode.setPrev(this.cursor);
            this.cursor.setNext(newNode);
            if(next != null) {
                next.setPrev(cursor);
                newNode.setNext(next);
            } else {
                tail = newNode;
            }
        }
        this.cursor = newNode;
        ++size;
    }

    /**
     * appends to tail
     * @param newOrder
     * @throws IllegalArgumentException
     */
    public void appendToTail(Order newOrder) throws IllegalArgumentException {
        if (newOrder == null)
            throw new IllegalArgumentException("NewOrder is null");
        OrderListNode newNode = new OrderListNode(newOrder);
        if(cursor == null) {
            this.head = newNode;
            this.cursor = newNode;
        } else {
            this.tail.setNext(newNode);
            newNode.setPrev(this.tail);
        }
        this.tail = newNode;
        ++size;
    }

    /**
     * adds head
     * @param newOrder
     * @throws IllegalArgumentException
     */
    public void addHead(Order newOrder) throws IllegalArgumentException {
        if (newOrder == null)
            throw new IllegalArgumentException("NewOrder is null");
        OrderListNode newNode = new OrderListNode(newOrder);
        if(cursor == null) {
            this.head = newNode;
            this.cursor = newNode;
            this.tail = newNode;
        } else {
            newNode.setNext(head);
            head.setPrev(newNode);
            head = newNode;
        }
        ++size;
    }

    /**
     * appends after similar node
     * @param newOrder
     * @return
     */
    public boolean appendAfterSimilar(Order newOrder) {

        boolean found = false;
        OrderListNode node = null;

        OrderListNode temp = head;
        while (temp != null) {
            if(temp.getData().equals(newOrder)) {
                found = true;
                node = temp;
                break;
            }
            temp = temp.getNext();
        }

        OrderListNode originalCursor = this.cursor;

        if(found) {
            this.cursor = node;
            insertAfterCursor(newOrder);
            this.cursor = originalCursor;
        }
        return found;

    }


    /**
     * removes cursor
     * @return
     * @throws EndOfListException
     */
    public Order removeCursor() throws EndOfListException {
        if(this.cursor == null)
            throw new EndOfListException("Cursor is null");

        Order order = this.cursor.getData();
        if(head == tail) {
            this.head = null;
            this.cursor = null;
            this.tail = null;
        } else {
            if(this.cursor.getPrev() != null)
                this.cursor.getPrev().setNext(cursor.getNext());
            if(cursor.getNext() != null)
                this.cursor.getNext().setPrev(cursor.getPrev());
            if(cursor == head) {
                head = cursor.getNext();
                cursor = head;
            } else {
                if(cursor == tail)
                    tail = cursor.getPrev();
                cursor = cursor.getPrev();
                if(cursor.getPrev() == null)
                    head = cursor;
            }
        }
        --size;
        return order;
    }

    /**
     * toString method
     * @return
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(String.format("%-30s%-60s%-40s\n", "Order Name", "Special Instructions", "Price"));
        buffer.append("-------------------------------------------------------------------------------------\n");
        OrderListNode temp =head;
        if(temp == null) {
            buffer.append("[empty].\n");
            return buffer.toString();
        }
        while (temp != null) {
            Order o = temp.getData();
            if(cursor == temp)
                buffer.append(String.format("->%-30s%-60s%-40.2f\n", o.getOrder(), o.getSpecialInstruction(), o.getPrice()));
            else
                buffer.append(String.format("%-30s%-60s%-40.2f\n", o.getOrder(), o.getSpecialInstruction(), o.getPrice()));
            temp = temp.getNext();
        }

        return buffer.toString();
    }


}





















