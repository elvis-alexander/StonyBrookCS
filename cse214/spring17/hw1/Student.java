
public class Student {
    final int MAX_WRITEUPS = 3;
    private String name;
    private int idNumber;
    private int numWriteups;


    /**
     * Costructor
     * @param n
     * @param idNum
     * @param writeUps
     */
    public Student(String n, int idNum, int writeUps) {
        this.name = n;
        this.idNumber = idNum;
        this.numWriteups = writeUps;
    }

    /**
     * Costructor
     * @param name
     * @param idNumber
     */
    public Student(String name, int idNumber) {
        this.name = name;
        this.idNumber = idNumber;
        this.numWriteups = 0;
    }

    /**
     * gets name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * set name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * gets id number
     * @return
     */
    public int getIdNumber() {
        return idNumber;
    }

    /**
     * sets the id number
     * @param idNumber
     */
    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    /**
     * gets number of write up
     * @return
     */
    public int getNumWriteups() {
        return numWriteups;
    }

    /**
     * sets number of write ups
     * @param numWriteups
     */
    public void setNumWriteups(int numWriteups) {
        this.numWriteups = numWriteups;
    }

    /**
     * Clones object
     * @return cloned object
     */
    public Student clone() {
        String nameCpy = new String(name);
        Student studentCpy = new Student(nameCpy, this.idNumber, this.numWriteups);
        return studentCpy;
    }

    /**
     *
     * @return toString representation
     */
    @Override
    public String toString() {
        return "Student{" +
                ", name='" + name + '\'' +
                ", idNumber=" + idNumber +
                ", numWriteups=" + numWriteups +
                '}';
    }

}
