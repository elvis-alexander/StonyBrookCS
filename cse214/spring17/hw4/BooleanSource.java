/**
 *
 */
public class BooleanSource {
    private double probability;

    public BooleanSource(double initProb) {
        this.probability = initProb;
    }

    public boolean occurs() {
        return Math.random() < probability;
    }

}
