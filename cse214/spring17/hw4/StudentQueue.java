import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;


public class StudentQueue extends TreeSet<Student> {

    /**
     *
     */
    public StudentQueue() {

        super(new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                Course c1 = o1.getCourse();
                Course c2 = o2.getCourse();
                int courseNumCompare = Integer.compare(c1.getCourseNumber(), c2.getCourseNumber());
                if(courseNumCompare != 0)
                    return courseNumCompare;
                int timeCompare = Integer.compare(o1.getTimeArrived(), o2.getTimeArrived());
                if (timeCompare == 0)
                    return 0;
                return timeCompare == 1 ? -1 : 1;
            }
        });
    }

    public void enqueue(Student s) {
        super.add(s);
    }

    public Student deque() {
        Student s = super.first();
        super.remove(s);
        return s;
    }

    /**
     *
     * @return
     */

    public int size() {
        return super.size();
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return super.isEmpty();
    }


    /**
     *
     */
    public void print() {
        if(super.isEmpty()) {
            System.out.println("---------------------------------------------\n" +
                    "[empty]");
            return;
        }

        System.out.println("---------------------------------------------");

        Iterator<Student> it = super.iterator();

        while (it.hasNext()) {
            Student s = it.next();
            System.out.println(s.getStudentId() + "\t" + s.getCourse().getCourseNumber()+ "\t" + s.getTimeRequired() + "\t" + s.getTimeArrived());
        }



    }

}
