
public class Actor {
    private String name;    // name of actor
    private int count;  // number of movies this actor is in
    public Actor(String n) {
        this.name = n;
    }
    public Actor(String n, int c) {
        this.name = n;
        this.count = c;
    }
    public void setName(String n) { this.name = n; }
    public String getName() { return this.name; }
    public void setCount(int c) { this.count = c; }
    public int getCount() { return this.count; }
    public void increaseCount() { ++count; }
    public void decrementCount() { --count; }

}
