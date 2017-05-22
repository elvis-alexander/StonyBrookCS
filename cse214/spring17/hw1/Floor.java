
public class Floor {
    private Student[] students;
    private int numberOfStudents;
    final int CAPACITY = 50;

    /**
     * Empty Constructor
     */
    public Floor() {
        this.students = new Student[CAPACITY];
        this.numberOfStudents = 0;
    }

    /**
     * Adds student to collection
     * @param student
     * @param position
     * @throws FullFloorException
     * @throws IllegalArgumentException
     */
    public void addStudent(Student student, int position) throws FullFloorException, IllegalArgumentException {
        if(numberOfStudents == CAPACITY)
            throw new FullFloorException("Maximum Capacity Reached");
        if(!(position >= 1 && position <= numberOfStudents + 1))
            throw new IllegalArgumentException("Illegal Position");

        // shift right
        for(int i = numberOfStudents; i >= position; --i)
            this.students[i] = this.students[i - 1];
        this.students[position - 1] = student;
        ++this.numberOfStudents;
    }

    /**
     * Removes student from collection
     * @param position
     * @return
     * @throws EmptyFloorException
     * @throws IllegalArgumentException
     */
    public Student removeStudent(int position) throws EmptyFloorException, IllegalArgumentException {
        if(numberOfStudents == 0)
            throw new EmptyFloorException("Floor is Empty");
        if((!(position >= 1 && position <= numberOfStudents + 1)) || (position >= 50) )
            throw new IllegalArgumentException("Illegal Position");

        // shift left and retrieve object
        Student toRemove = this.students[position - 1];
        for(int i = position - 1; i < numberOfStudents; ++i)
            this.students[i] = this.students[i+1];
        --this.numberOfStudents;
        return toRemove;
    }

    /**
     * Retrieves student from collection
     * @param position
     * @return
     * @throws IllegalArgumentException
     */
    public Student getStudent(int position) throws IllegalArgumentException {
        if((!(position >= 1 && position <= numberOfStudents + 1)) || (position >= 50) )
            throw new IllegalArgumentException("Illegal Position");
        return this.students[position - 1];
    }

    /**
     * Sets student to locations
     * @param position
     * @param student
     * @throws IllegalArgumentException
     */
    public void setStudent(int position, Student student) throws IllegalArgumentException {
        if ((!(position >= 1 && position <= numberOfStudents + 1)) || (position >= 50))
            throw new IllegalArgumentException("Illegal Position");
        this.students[position - 1] = student;
    }

    /**
     * returns number of students
     * @return
     */
    public int count() {
        return this.numberOfStudents;
    }

    /**
     * Deep copy
     * @return
     */
    public Floor clone() {
        Floor floorCpy = new Floor();
        Student[] studentCpy = floorCpy.students;
        for(int i = 0; i < this.numberOfStudents; ++i) {
            studentCpy[i] = this.students[i].clone();
        }
        floorCpy.numberOfStudents = this.numberOfStudents;
        return floorCpy;
    }

    /**
     * Retrieve index by name
     * @param name
     * @return
     */
    public int getByName(String name) {
        int i;
        boolean found = false;
        for(i = 0; i < numberOfStudents; ++i) {
            Student currStudent = this.students[i];
            if(currStudent.getName().equalsIgnoreCase(name)) {
                found = true;
                break;
            }
        }
        if(!found)
            return -1;
        return i;
    }

    /**
     * toString representation of floor
     * @return
     */
    @Override
    public String toString() {
        StringBuffer output = new StringBuffer();
        output.append(String.format("%-30s%-40s%-40s%-40s\n", "Room", "Name", "ID", "Writeups"));
        output.append("-----------------------------------------------------------------------------------------------------------------------\n");
        for (int i = 0; i < numberOfStudents; ++i) {
            Student currStudent = this.students[i];
            output.append(String.format("%-30d%-40s%-40d%-40d\n",
                    i + 1, currStudent.getName(), currStudent.getIdNumber(), currStudent.getNumWriteups()
            ));
        }
        return output.toString();

    }
}
