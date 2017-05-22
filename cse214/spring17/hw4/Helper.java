
public class Helper {
    private int timeLeftToFree;
    private boolean isProfessor;
    Student student;

    /**
     *
     * @param s
     */
    public void assignStudent(Student s) {
        this.student = s;
    }


    /**
     *
     * @param isProfessor
     */
    public Helper(boolean isProfessor) {
        this.isProfessor = isProfessor;
        this.timeLeftToFree = 0;
    }

    /**
     *
     * @return
     */
    public int getTimeLeftToFree() {
        return timeLeftToFree;
    }

    /**
     *
     * @param timeLeftToFree
     */
    public void setTimeLeftToFree(int timeLeftToFree) {
        this.timeLeftToFree = timeLeftToFree;
        if(this.timeLeftToFree < 0)
            this.timeLeftToFree = 0;
    }

    /**
     *
     * @return
     */
    public boolean isProfessor() {
        return isProfessor;
    }

    /**
     *
     * @param professor
     */
    public void setProfessor(boolean professor) {
        isProfessor = professor;
    }

    /**
     *
     * @return
     */
    public boolean isFree() {
        return timeLeftToFree == 0;
    }

}