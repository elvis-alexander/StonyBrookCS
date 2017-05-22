
import java.io.*;
import java.util.*;


public class Driver {
    private static Scanner scanner = new Scanner(System.in);
    private static Campus campus = new Campus();

    public static void main(String[] a) {
            System.out.println("Welcome to SBGetARoom, Stony Brook's premium room lookup system.\n");
            boolean r = true;
            try {
                campus = loadCampus();
                System.out.println("Saved file loaded...\n");
            } catch (Exception e) {
                System.out.println("No save file found. Creating an empty campus.\n");
                campus = new Campus();
            }
            while (r) {
                try {
                    menu();
                    switch (scanner.nextLine().toUpperCase().charAt(0)) {
                        case 'A':
                            addBuil();
                            break;
                        case 'D':
                            deleBuil();
                            break;
                        case 'E':
                            editBuil();
                            break;
                        case 'C':
                            allBuildings();
                            break;
                        case 'L':
                            listBuildingDetails();
                            break;
                        case 'F':
                            findRoom();
                            break;
                        case 'S':
                            searchForRooms();
                            break;
                        case 'Q':
                            System.out.println("\tS - Save");
                            System.out.println("\tD - Don't Save");
                            System.out.print("Select an option: ");
                            switch (scanner.nextLine().toUpperCase().charAt(0)) {
                                case 'S':
                                    saveCampus();
                                    System.out.println("Saving and closing ...");
                                    break;
                                case 'D':
                                    System.out.println("Closing without saving ...");
                                    break;
                                default:
                                    System.out.println("Option not available, terminating, no saving");
                                    break;
                            }
                            r = false;
                            break;
                        default:
                            System.out.println("Option not available");
                            break;
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage() + "\n");
            }
        }
    }

    private static Campus loadCampus() throws Exception {
        FileInputStream file = new FileInputStream("campus.obj");
        ObjectInputStream inStream = new ObjectInputStream(file);
        campus =  (Campus) inStream.readObject();
        inStream.close();
        return campus;
    }

    /* saves program state */
    private static void saveCampus() throws Exception {
        FileOutputStream file = new FileOutputStream("campus.obj");
        ObjectOutputStream outStream = new ObjectOutputStream(file);
        outStream.writeObject(campus);
        outStream.close();
    }

    /* attempts to find and print info of a classroom based on a building name and room number */
    private static void findRoom() {
        System.out.print("Please enter a room name: ");
        String[] input = scanner.nextLine().split(" ");
        String buildingName = input[0];
        int roomNumber = Integer.parseInt(input[1]);
        Building building = campus.getBuilding(buildingName);
        Classroom classroom = building.getClassroom(roomNumber);
        System.out.println("Room Details:");
        System.out.println("\tSeats: " + classroom.getNumSeats());
        if(classroom.isHasWhiteboard())
            System.out.println("\tHas Whiteboard");
        else
            System.out.println("\tDoesn't have Whiteboard");
        if(classroom.isHasChalkBoard())
            System.out.println("\tHas Blackboard");
        else
            System.out.println("\tDoesn't have Blackboard");
        System.out.print("\tAV Amenities: ");
        for(String s : classroom.getAVEquipmentList())
            System.out.print(s + ", ");
        System.out.println();
    }


    /* searches for rooms  */
    private static void searchForRooms() {
        System.out.println("\tOptions:\n" +
                "\tB) Blackboard\n" +
                "\tW) Whiteboard\n" +
                "\tA) AV Equipment");
        System.out.print("Please select options: ");
        switch (scanner.nextLine().toUpperCase().charAt(0)) {
            case 'B':
                System.out.println("Rooms with blackboard:");
                for(String buildingName : campus.keySet()) {
                    Building building = campus.getBuilding(buildingName);
                    boolean atleastOne = false;
                    for(Integer roomNumber : building.keySet()) {
                        Classroom classroom = building.getClassroom(roomNumber);
                        if(classroom.isHasChalkBoard()) {
                            if(atleastOne == false){
                                System.out.print("\t" + buildingName);
                            }
                            System.out.print(" " + roomNumber + ", ");
                            atleastOne = true;
                        }
                    }
                }
                break;
            case 'W':
                System.out.println("Rooms with whiteboards:");
                for(String buildingName : campus.keySet()) {
                    Building building = campus.getBuilding(buildingName);
                    boolean atleastOne = false;
                    for(Integer roomNumber : building.keySet()) {
                        Classroom classroom = building.getClassroom(roomNumber);
                        if(classroom.isHasWhiteboard()) {
                            if(atleastOne == false){
                                System.out.print("\t" + buildingName);
                            }
                            System.out.print(" " + roomNumber + ", ");
                            atleastOne = true;
                        }
                    }
                    if(atleastOne == true)
                        System.out.println();
                }
                break;
            case 'A':
                System.out.print("Please enter a keyword: ");
                String keyword = scanner.nextLine();
                for(String buildingName : campus.keySet()) {
                    Building building = campus.getBuilding(buildingName);
                    boolean atleastOne = false;
                    for(Integer roomNumber : building.keySet()) {
                        Classroom classroom = building.getClassroom(roomNumber);
                        for(String s : classroom.getAVEquipmentList()) {
                            if(s.equals(keyword)) {
                                if(atleastOne == false){
                                    System.out.print("\t" + buildingName);
                                }
                                System.out.print(" " + roomNumber + ",");
                                atleastOne = true;
                            }
                        }
                    }
                }
                break;
        }
        System.out.println();
    }


    /* main menu print out */
    static void menu() {
        System.out.print("\nMain Menu:\n" +
                "A) Add a building\n" +
                "D) Delete a building\n" +
                "E) Edit a building\n" +
                "F) Find a room\n" +
                "S) Search for rooms\n" +
                "C) List all buildings on Campus\n" +
                "L) List building details\n" +
                "Q) Quit\n" +
                "Please select an option: ");
    }

    /* edit menu print out */
    static void editMenu() {
        System.out.println("Options:\n" +
                "        A) Add room\n" +
                "        D) Delete room\n" +
                "        E) Edit room");
    }

    /* list all building names as well as class room numbers for each individual one  */
    private static void allBuildings() {
        for(String buildingName : campus.keySet()) {
            Building building = campus.getBuilding(buildingName);
            System.out.print(buildingName + ": ");
            for(Integer roomNumber : building.keySet()) {
                System.out.print(roomNumber + ", ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /* list the details of one building */
    private static void listBuildingDetails() {
        System.out.print("Please enter a building name: ");
        String buildName = scanner.nextLine();
        Building building = campus.getBuilding(buildName);
        System.out.println("Details:");
        System.out.print("\tRooms: ");
        int totalRooms = 0;
        int totalSeats = 0;
        int numWhiteBoards = 0;
        int numChalkBoards = 0;
        List<String> avlList = new ArrayList<>();
        for(Integer roomNumber : building.keySet()) {
            Classroom classroom = building.getClassroom(roomNumber);
            ++totalRooms;
            totalSeats += classroom.getNumSeats();
            if(classroom.isHasWhiteboard()) ++numWhiteBoards;
            if(classroom.isHasChalkBoard()) ++numChalkBoards;
            for(String s : classroom.getAVEquipmentList()) {
                if(!avlList.contains(s)) avlList.add(s);
            }
            System.out.print(roomNumber + ", ");
        }
        System.out.println();
        System.out.println("\tTotal seats: " + totalSeats);
        if(numWhiteBoards == 0) {
            System.out.println("\t0% of rooms has whiteboards");
        } else {
            System.out.println("\t" + ((double)numWhiteBoards/totalRooms)*100 + "% of rooms has whiteboards");
        }
        if(numChalkBoards == 0) {
            System.out.println("\t0% of rooms has blackboards");
        } else {
            System.out.println("\t" + ((double)numChalkBoards/totalRooms)*100 + "% of rooms has blackboards");
        }
        System.out.print("\tAV Equipment present: ");
        for(String s : avlList) {
            System.out.print(s + ", ");
        }
        System.out.println();
    }

    /* adds a new empty building to campus */
    private static void addBuil() {
        System.out.print("Please enter a building name: ");
        String buildName = scanner.nextLine();
        campus.addBuilding(buildName, new Building());
        System.out.println("Building " + buildName + " added.");
    }

    /* deletes a building from this campus */
    private static void deleBuil() {
        System.out.print("Please enter a building name: ");
        String buildingName = scanner.nextLine();
        campus.removeBuilding(buildingName);
        System.out.println("Building " + buildingName + " deleted");
    }

    /* edit an existing building */
    private static void editBuil() {
        System.out.print("Please enter a building name: ");
        String buildName = scanner.nextLine();
        Building building = campus.getBuilding(buildName);
        System.out.println("Building " + buildName + " selected:");
        editMenu();
        System.out.print("Please select an option: ");
        switch (scanner.nextLine().toUpperCase().charAt(0)) {
            case 'A':
                addRoom(building, buildName);
                break;
            case 'D':
                deleteRoom(building, buildName);
                break;
            case 'E':
                editRoom(building, buildName);
            default:
                break;
        }
    }

    /* methods for editing buildings */
    public static void addRoom(Building building, String buildingName) {
        System.out.print("Please enter room number: ");
        int roomNumber = Integer.parseInt(scanner.nextLine());
        System.out.print("Please enter number of seats: ");
        int numSeats = Integer.parseInt(scanner.nextLine());
        System.out.print("Please enter AV Equipment (separated by commas): ");
        String av = scanner.nextLine();
        System.out.print("Does it have a whiteboard?(Y/n): ");
        boolean hasWhite = scanner.nextLine().toUpperCase().equals("Y") ? true : false;
        System.out.print("Does it have a chalkboard?(Y/n): ");
        boolean hasChalk = scanner.nextLine().toUpperCase().equals("Y") ? true : false;
        Classroom classroom = new Classroom(hasWhite, hasChalk, numSeats, av);
        building.addClassroom(roomNumber, classroom);
        System.out.println("Room " + buildingName + " " + roomNumber + " Added.");
    }
    public static void deleteRoom(Building building, String buildingName) {
        System.out.print("Please enter room number: ");
        int roomNumber = Integer.parseInt(scanner.nextLine());
        building.removeClassroom(roomNumber);
        System.out.println("removed " + roomNumber + " from " + buildingName);
    }

    private static void editRoom(Building building, String buildName) {
        System.out.print("Please enter room number: ");
        int roomNumber = Integer.parseInt(scanner.nextLine());
        Classroom classroom = building.getClassroom(roomNumber);
        System.out.println("Old number of seats: " + classroom.getNumSeats());
        System.out.print("Please enter number of seats or press enter to skip: ");
        String str = scanner.nextLine();
        if(!str.equals("")) {
            classroom.setNumSeats(Integer.parseInt(str));
            System.out.println("Num seats Updated");
        }
        System.out.print("Old AV Equipment: ");
        for(String s : classroom.getAVEquipmentList())
            System.out.print(s + ", ");
        System.out.println();
        System.out.print("Please enter new AV Equipment (separated by commas) or press enter to skip: ");
        str = scanner.nextLine();
        if(!str.equals("")) {
            classroom.setAVEquipmentList(Arrays.asList(str.split(",")));
            System.out.println("AV Equipment Updated");
        }
        System.out.print("Does it have a whiteboard?(Y/n) or press enter to skip:");
        str = scanner.nextLine();
        if(!str.equals("")) {
            classroom.setHasWhiteboard(str.toUpperCase().charAt(0) == 'Y' ? true : false);
            System.out.println("Chalkboard updated");
        }
        System.out.print("Does it have a chalkboard?(Y/n) or press enter to skip:");
        str = scanner.nextLine();
        if(!str.equals("")) {
            classroom.setHasChalkBoard(str.toUpperCase().charAt(0) == 'Y' ? true : false);
            System.out.println("Chalkboard updated");
        }
        System.out.println(buildName + " " + roomNumber + " updated");
    }
    /* methods for editing buildings */
}
