import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.*;

public class OfficeHourSimulator {
    private static Scanner input = new Scanner(System.in);


    public static void main(String[] args) {
        try {
            //System.out.print("Please enter a filename: ");
            String f = "in.txt";//input.nextLine();
            BufferedReader buff = new BufferedReader(new FileReader(f));
            String line = ridUgly(buff.readLine());
            int numCourses = Integer.parseInt(line);
            line = ridUgly(buff.readLine());
            String[] arr = line.split(" ");
            int[] courseNumbers = new int[arr.length];
            int i;
            for(i = 0; i < arr.length; ++i)
                courseNumbers[i] = Integer.parseInt(arr[i]);
            Arrays.sort(courseNumbers);
            line = ridUgly(buff.readLine());
            arr = line.split(" ");
            double[] arrivalProbabilities = new double[arr.length];
            for(i = 0; i < arr.length; ++i)
                arrivalProbabilities[i] = Double.parseDouble(arr[i]);
            int minTime = Integer.parseInt(ridUgly(buff.readLine()));
            int maxTime = Integer.parseInt(ridUgly(buff.readLine()));
            int numCups = Integer.parseInt(ridUgly(buff.readLine()));
            int simulationTime = Integer.parseInt(ridUgly(buff.readLine()));
            int numTas = Integer.parseInt(ridUgly(buff.readLine()));
            // create courses list
            Course[] courses = new Course[courseNumbers.length];
            for(i = 0; i < courses.length; ++i) {
                courses[i] = new Course(courseNumbers[i], arrivalProbabilities[i]);
                courses[i].setCourseDifficulty(i + 1);
            }
            simulate(simulationTime, arrivalProbabilities, courses, minTime, maxTime, numCups, numTas);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("==> Error log: " + e.getMessage());
        }
    }

    /**
     *
     * @param officeHrTime
     * @param arrivalProbability
     * @param courses
     * @param minTime
     * @param maxTime
     * @param numCups
     * @param numTas
     */
    public static void simulate(int officeHrTime, double[] arrivalProbability, Course[] courses, int minTime, int maxTime, int numCups, int numTas) {
        int i;
        boolean run = true;
        Helper professor = new Helper(true);
        ArrayList<Helper> tas = new ArrayList<>();
        for(i = 0; i < numTas; ++i)
            tas.add(new Helper(false));
        StudentQueue studentQueue= new StudentQueue();
        boolean occurs = false;
        Student newStudent = null;
        int totalStudents = 0;


        for(int timeStamp = 0; timeStamp < officeHrTime; ++timeStamp) {

            System.out.println("\nTime Step " + timeStamp + 1);
            for(int courseIndex = courses.length - 1; courseIndex >= 0; --courseIndex) {
                Course currentCourse= courses[courseIndex];
                occurs = new BooleanSource(arrivalProbability[courseIndex]).occurs();
                if(occurs) {
                    Random rand = new Random();
                    int randomTimeRequired =  rand.nextInt((maxTime - minTime) + 1) + minTime;
                    newStudent = new Student(timeStamp, currentCourse, randomTimeRequired);
                    System.out.println(
                            "Student " + newStudent.getStudentId() +
                            " has arrived for " + currentCourse.getCourseNumber() +
                            " requiring " + randomTimeRequired + " minutes."
                    );

                    studentQueue.enqueue(newStudent);
                } else {
                    System.out.println("No students have arrived for " + currentCourse.getCourseNumber());
                }


                if(professor.isFree() && !studentQueue.isEmpty()) {
                    Student s = studentQueue.deque();
                    professor.setTimeLeftToFree(s.getTimeRequired() - numCups);
                    professor.assignStudent(s);
                }
                for(Helper ta : tas) {
                    if(ta.isFree() && !studentQueue.isEmpty()) {
                        Student s = studentQueue.deque();
                        ta.setTimeLeftToFree(s.getTimeRequired() * 2);
                        ta.assignStudent(s);
                    }
                }
            }
            System.out.println();

            if(professor.isFree()) {
                System.out.println("Professor is waiting for the next student to arrive.");
            } else {
                Student s = professor.student;
                System.out.println("Professor is helping " + s.getStudentId() + ", " + professor.getTimeLeftToFree() + " minutes remaining.");
                professor.setTimeLeftToFree(professor.getTimeLeftToFree() - 1);
            }

            for(Helper ta : tas) {
                if(ta.isFree()) {
                    System.out.println("TA  is waiting for the next student to arrive.\n");
                } else {
                    Student s = ta.student;
                    System.out.println("Professor is helping " + s.getStudentId() + ", " + ta.getTimeLeftToFree() + " minutes remaining.");
                    ta.setTimeLeftToFree(ta.getTimeLeftToFree() - 1);
                }
            }

            System.out.println();
            System.out.println("Student Queue:");

            System.out.print("Student Queue:");
            System.out.println("ID     Course     Required Time Arrival Time");
            studentQueue.print();

        }

        System.out.println("__________________________________________________\n" +
                "End simulation.");
        System.out.println("Statistics:\n" +
                "Course    #StudentsHelped   Avg.Time\n" +
                "________________________________________");


        System.out.println("We hope you have a pleasant visit in your next actual office hours, good bye!");
    }
    private static String ridUgly(String s) {
        return s.substring(s.indexOf(':') + 1, s.length());
    }
}
