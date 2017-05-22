
import java.io.Serializable;
import java.util.HashMap;

// name of building to building
public class Campus extends HashMap<String, Building> implements Serializable {


    public void addBuilding(String buildingName, Building building) throws IllegalArgumentException {
        if(containsKey(buildingName)) {
            throw new IllegalArgumentException("Building already exists.");
        }
        super.put(buildingName, building);
    }

    public Building getBuilding(String buildingName) throws IllegalArgumentException {
        if(!super.containsKey(buildingName))
            throw new IllegalArgumentException("Building not found.");
        return super.get(buildingName);
    }

    public void removeBuilding(String buildingName) {
        if(!super.containsKey(buildingName)) {
            throw new IllegalArgumentException(buildingName + " is not a part of this campus");
        }
        super.remove(buildingName);
    }



}