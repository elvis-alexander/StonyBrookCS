
import java.io.Serializable;
import java.util.HashMap;

// maps roomNumber to classrooms
public class Building extends HashMap<Integer, Classroom> implements Serializable {

    public void addClassroom(int roomNumber, Classroom classroom) {
        if(super.containsKey(roomNumber)) {
            throw new IllegalArgumentException("Classroom is a part of this Building");
        }
        super.put(roomNumber, classroom);
    }

    public Classroom getClassroom(int roomNumber) {
        if (!super.containsKey(roomNumber)) {
            return null;
        }
        return super.get(roomNumber);
    }

    public void removeClassroom(int roomNumber) {
        if(!super.containsKey(roomNumber)) {
            throw new IllegalArgumentException("Not a valid room number");
        }
        super.remove(roomNumber);
    }

}
