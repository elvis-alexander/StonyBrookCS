
import java.util.Scanner;

public class ResidenceHallManager {
    static Floor floor1 = new Floor();
    static Floor floor2 = new Floor();
    static Floor floor3 = new Floor();
    static Floor currentFloor = floor1;

    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("Welcome to RockStar Rez, the second worst housing management System at SBU.\n");
        System.out.println(
                "Menu:\n" +
                "        A) Add a student\n" +
                "        R) Remove a student\n" +
                "        S) Swap Students\n" +
                "        M) Move Student\n" +
                "        F) Select Floor\n" +
                "        C) Copy Floor\n" +
                "        P) Print Current Floor\n" +
                "        W) Write Up Student\n" +
                "        Q) Quit");

        boolean run = true;
        while (run) {
            System.out.print("\n\nPlease select an option: ");
            String strinput = getInputStr().toUpperCase();
            if(strinput == null || strinput.equals(""))
                continue;
            char input = strinput.charAt(0);
            try {
                switch (input) {

                    case 'A':
                        printHelper("Add A Student:\n");
                        printHelper("Please enter a name: ");
                        String name = getInputStr();
                        printHelper("Please enter an id number: ");
                        int id = getInputInt();
                        printHelper("Please enter a spot number: ");
                        int position = getInputInt();
                        currentFloor.addStudent(new Student(name, id), position);
                        printHelper(String.format("\n%s added to Floor %d Room %d.", name, getFloorInt(), position));
                        break;

                    case 'R':
                        printHelper("Please select a student number: ");
                        int studentIndex = getInputInt();
                        Student studentToRemove = currentFloor.getStudent(studentIndex);
                        currentFloor.removeStudent(studentIndex);
                        printHelper(String.format("%s removed.", studentToRemove.getName()));
                        break;

                    case 'S':
                        // swap students
                        printHelper("Please enter the Student 1 floor: ");
                        Floor sourceFloorOne = getFloor(getInputInt());
                        printHelper("Please enter the Student 1 room: ");
                        int sourceRoomOne = getInputInt();
                        /* dest floor */
                        printHelper("Please enter the Student 2 floor: ");
                        Floor destFloorOne = getFloor(getInputInt());
                        printHelper("Please enter the Student 2 room: ");
                        int destRoomOne = getInputInt();
                        Student studentOne = sourceFloorOne.getStudent(sourceRoomOne);
                        Student studentTwo = destFloorOne.getStudent(destRoomOne);
                        sourceFloorOne.setStudent(sourceRoomOne, studentTwo);
                        destFloorOne.setStudent(destRoomOne, studentOne);
                        printHelper(String.format("%s and %s swapped",
                                studentTwo.getName(), studentOne.getName()));
                        break;

                    case 'M':
                        /* src floor */
                        printHelper("Please enter the source floor: ");
                        int sourceFloorInt = getInputInt();
                        Floor sourceFloor = getFloor(sourceFloorInt);
                        printHelper("Please enter the source room: ");
                        int sourceRoom = getInputInt();
                        /* dest floor */
                        printHelper("Please enter the destination floor: ");
                        int destFloorInteger = getInputInt();
                        Floor destFloor = getFloor(destFloorInteger);
                        printHelper("Please enter the destination room: ");
                        int destRoom = getInputInt();
                        Student student = sourceFloor.getStudent(sourceRoom);
                        sourceFloor.removeStudent(sourceRoom);
                        destFloor.addStudent(student, destRoom);
                        printHelper(String.format("%s move to Floor %d room %d.", student.getName(), destFloorInteger, destRoom));
                        break;
                    case 'F':
                        printHelper("Please select a floor: ");
                        int floorInt = getInputInt();
                        Floor selectedFloor = getFloor(floorInt);
                        ResidenceHallManager.currentFloor = selectedFloor;
                        printHelper(String.format("Floor %d selected", getFloorInt()));
                        break;

                    case 'C':
                        printHelper("Please enter the source floor: ");
                        int srcFloorInt = getInputInt();
                        Floor src = getFloor(srcFloorInt);

                        printHelper("Please enter the destination floor: ");
                        int destFloorInt = getInputInt();

                        if (destFloorInt == 1) {
                            boolean make_this_current = false;
                            if(currentFloor == floor1)
                                make_this_current = true;

                            floor1 = src.clone();

                            if(make_this_current) {
                                currentFloor = floor1;
                            }
                        } else if (destFloorInt == 2) {
                            boolean make_this_current = false;
                            if(currentFloor == floor2)
                                make_this_current = true;

                            floor2 = src.clone();
                            if(make_this_current) {
                                currentFloor = floor2;
                            }
                        } else if (destFloorInt == 3) {
                            boolean make_this_current = false;
                            if(currentFloor == floor3)
                                make_this_current = true;

                            floor3 = src.clone();

                            if(make_this_current) {
                                currentFloor = floor3;
                            }
                        }
                        printHelper(String.format("Floor %d Copied to Floor %d.", srcFloorInt, destFloorInt));
                        break;

                    case 'P':
                        printHelper(String.format("Floor %d\n", getFloorInt()));
                        printHelper(currentFloor.toString());
                        break;

                    case 'W':
                        printHelper("Please enter student name: ");
                        String studentName = getInputStr();
                        int sIndex = currentFloor.getByName(studentName);
                        if(sIndex == -1)
                            throw new IllegalArgumentException("Student not present");

                        Student stu = currentFloor.getStudent(sIndex + 1);
                        if(stu.getNumWriteups() == 2) {
                            printHelper(String.format(
                                    "%s has 3 writeups and has been removed from the building.\n", stu.getName()));
                            currentFloor.removeStudent(sIndex + 1);
                        } else {
                            stu.setNumWriteups(stu.getNumWriteups() + 1);
                            printHelper(String.format("%s has %d writeup.",
                                    stu.getName(), stu.getNumWriteups()));
                        }
                        break;
                    case 'Q':
                        printHelper("See you later, alligator!\n");
                        run = false;
                        break;

                    default:
                        printHelper("Not a valid entry, please try again");
                        break;
                }
            } catch (Exception e) {
                printHelper(e.getMessage());
            }
        }
    }

    /**
     * print helper
     * @param output
     */
    static void printHelper(String output) {
        System.out.print(output);
    }

    /**
     * helper for next int
     * @return
     */
    public static String getInputStr() {
        return scanner.nextLine();
    }

    /**
     * helper for next int
     * @return
     */
    public static int getInputInt() {
        return Integer.parseInt(scanner.nextLine());
    }

    /**
     * helper for int rep.
     * @return
     */
    public static int getFloorInt() {
        return currentFloor == floor1 ? 1 : currentFloor == floor2 ? 2 : 3;
    }

    /**
     * helper for Floor reference
     * @param level
     * @return
     * @throws IllegalArgumentException
     */
    public static Floor getFloor(int level) throws IllegalArgumentException {
        if(!(level >= 1 && level <= 3))
            throw new IllegalArgumentException("Invalid Floor");
        return level == 1 ? floor1 : level == 2 ? floor2 : floor3;
    }

}
