
public class Course {

    private int courseNumber;
    private int courseDifficulty;
    private double arrivalProbability;

    /**
     *
     * @param courseNumber
     * @param arrivalProbability
     */
    public Course(int courseNumber, double arrivalProbability) {
        this.courseNumber  = courseNumber;
        this.arrivalProbability = arrivalProbability;
    }

    /**
     *
     * @return
     */
    public int getCourseNumber() {
        return courseNumber;
    }

    /**
     *
     * @param courseNumber
     */
    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }

    /**
     *
     * @return
     */
    public int getCourseDifficulty() {
        return courseDifficulty;
    }

    /**
     *
     * @param courseDifficulty
     */
    public void setCourseDifficulty(int courseDifficulty) {
        this.courseDifficulty = courseDifficulty;
    }

    /**
     *
     * @return
     */
    public double getArrivalProbability() {
        return arrivalProbability;
    }

    /**
     *
     * @param arrivalProbability
     */
    public void setArrivalProbability(double arrivalProbability) {
        this.arrivalProbability = arrivalProbability;
    }
}
