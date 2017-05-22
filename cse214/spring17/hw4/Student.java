
public class Student {

    private static int studentCounter = 1;
    private int studentId;
    private int timeArrived;
    private int timeRequired;
    private Course course;

    /**
     *
     * @param timeArrived
     * @param c
     * @param timeRequired
     */
    public Student(int timeArrived, Course c,int timeRequired) {
        this.studentId = studentCounter++;
        this.timeArrived = timeArrived;
        this.timeRequired = timeRequired;
        this.course = c;
    }


    /**
     *
     * @return
     */
    public static int getStudentCounter() {
        return studentCounter;
    }

    /**
     *
     * @param studentCounter
     */
    public static void setStudentCounter(int studentCounter) {
        Student.studentCounter = studentCounter;
    }

    /**
     *
     * @return
     */
    public int getStudentId() {
        return studentId;
    }

    /**
     *
     * @param studentId
     */
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    /**
     *
     * @return
     */
    public int getTimeArrived() {
        return timeArrived;
    }

    /**
     *
     * @param timeArrived
     */
    public void setTimeArrived(int timeArrived) {
        this.timeArrived = timeArrived;
    }

    /**
     *
     * @return
     */
    public int getTimeRequired() {
        return timeRequired;
    }

    /**
     *
     * @param timeRequired
     */
    public void setTimeRequired(int timeRequired) {
        this.timeRequired = timeRequired;
    }

    /**
     *
     * @param t
     */
    public void decrementTimeRequiered(int t) {
        this.timeRequired -= t;

    }

    /**
     *
     * @return
     */
    public Course getCourse() {
        return course;
    }


    /**
     *
     * @param course
     */
    public void setCourse(Course course) {
        this.course = course;
    }
}
