import java.util.Scanner;

public class CoffeeOrderManager {
    private static Scanner input = new Scanner(System.in);
    private static OrderList barista1 = new OrderList();
    private static OrderList barista2 = new OrderList();
    private static OrderList currBarista = null;
    private static Order cutOrder = null;

    public static void main(String[] args) {
        init();
    }

    private static void init() {
        boolean run = true;
        while (run) {
            printMainMenu();
            switch (getInputOption()) {
                case 'O':
                    optionO();
                    break;
                case 'P':
                    optionP();
                    break;
                case 'C':
                    optionC();
                    break;
                case 'Q':
                    System.out.println("Only traitors go to Dunkin, see you soon!\n");
                    run = false;
                    break;
                default:
                    System.out.println("Option not valid");
                    break;
            }
        }
    }

    private static void optionO() {
        Order newOrder = new Order();
        System.out.print("Please enter drink name: ");
        newOrder.setOrder(input.nextLine());
        System.out.print("Please enter special requests: ");
        newOrder.setSpecialInstruction(input.nextLine());
        System.out.print("Please enter the price: ");
        newOrder.setPrice(Double.parseDouble(input.nextLine()));

        System.out.print("Please select Barista (1 or 2): ");
        setBarista(Integer.parseInt(input.nextLine()));


        System.out.println("Where should the order be added? Options: F - Front of List, B - Back of List, A - After Cursor, S - After Similar Order (default: end of list)");
        System.out.print("Please select an option: ");
        char addOption = input.nextLine().toUpperCase().charAt(0);
        switch (addOption) {
            case 'F':
                currBarista.addHead(newOrder);
                break;
            case 'B':
                currBarista.appendToTail(newOrder);
                break;
            case 'A':
                currBarista.insertAfterCursor(newOrder);
                break;
            case 'S':
                if(!currBarista.appendAfterSimilar(newOrder))
                    currBarista.appendToTail(newOrder);
                break;
        }
    }

    private static void optionP() {
        System.out.println("Barista 1:");
        System.out.println(barista1.toString());
        System.out.println("Barista 2:");
        System.out.println(barista2.toString());
    }

    private static void optionC() {
        System.out.print("Please select a cursor (1 or 2): ");
        setBarista(Integer.parseInt(input.nextLine()));

        System.out.println("Cursor options: F - Forward, B - Backward, H-To Head, T - To Tail, E - Edit, R - Remove, C - Cut, P - Paste.");
        System.out.print("Please select an option: ");
        char cursorOption = input.nextLine().toUpperCase().charAt(0);
        switch (cursorOption) {
            case 'F':
                try {
                    currBarista.cursorForward();
                    System.out.println("Cursor has moved forward.");
                } catch (EndOfListException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 'B':
                try {
                    currBarista.cursorBackward();
                    System.out.println("Cursor has moved backward.");
                } catch (EndOfListException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 'H':
                System.out.println("Cursor to head");
                currBarista.resetCursorToHead();
                break;
            case 'T':
                System.out.println("Cursor to tail");
                currBarista.resetCursorToTail();
                break;
            case 'E':
                OrderListNode o = currBarista.getCursor();
                if(o != null) {
                    if(o.getData() != null) {
                        System.out.print("Please enter drink name: ");
                        o.getData().setOrder(input.nextLine());
                        System.out.print("Please enter special requests: ");
                        o.getData().setSpecialInstruction(input.nextLine());
                        System.out.print("Please enter the price: ");
                        o.getData().setPrice(Double.parseDouble(input.nextLine()));
                    }
                }
                break;
            case 'R':
                try {
                    currBarista.removeCursor();
                } catch (EndOfListException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 'C':
                OrderListNode order = currBarista.getCursor();
                if(order != null) {
                    if (order.getData() != null) {
                        cutOrder = order.getData();
                        try {
                            currBarista.removeCursor();
                        } catch (EndOfListException e) {
                            System.out.println(e.getMessage());
                        }
                        System.out.println(cutOrder.getOrder() + " is in clipboard.");
                    }
                }
                break;
            case 'P':
                if(cutOrder == null) {
                    System.out.println("No item currently cut");
                } else {
                    currBarista.insertAfterCursor(cutOrder);
                    System.out.println(cutOrder.getOrder() + " paseted after list " + getBarista() + " cursor.");
                    cutOrder = null;
                }

                break;
        }

    }

    private static void setBarista(int b) {
        currBarista = barista1;
        if(b == 2)
            currBarista = barista2;
    }

    private static int getBarista() {
        return currBarista == barista1 ? 1 : 2;
    }

    private static char getInputOption() {
        System.out.print("Please select an option: ");
        String s = input.nextLine().toUpperCase();
        if(s == null || s.equals(""))
            return 'Z';
        return s.charAt(0);
    }

    private static void printMainMenu() {
        System.out.println("Welcome to Star Duck Coffee, the number one coffee shop for flannel enthusiasts.\n" +
                "Menu:\n" +
                "       O) Order\n" +
                "       P) Print Order Lists\n" +
                "       C) Cursor Options        \n" +
                "       Q) Quit");
    }
}
