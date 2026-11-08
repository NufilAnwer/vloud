import java.util.Random;

public class RLAgent {
    private final double[][] weightProfiles = {
            {0.7, 0.1, 0.1, 0.1},
            {0.1, 0.7, 0.1, 0.1},
            {0.1, 0.1, 0.7, 0.1},
            {0.1, 0.1, 0.1, 0.7},
            {0.25, 0.25, 0.25, 0.25}
    };
    private final double epsilon = 0.1;
    private final Random random = new Random();

    public double[] getWeightProfile(int queueSize, double utilization,
                                     double fairnessDrop, double terminationRate) {
        if (random.nextDouble() < epsilon) {
            return weightProfiles[random.nextInt(weightProfiles.length)].clone();
        }

        if (terminationRate > 0.1) {
            return weightProfiles[3].clone();
        }
        if (queueSize > 100) {
            return weightProfiles[0].clone();
        }
        if (fairnessDrop > 0.3) {
            return weightProfiles[2].clone();
        }
        return weightProfiles[4].clone();
    }
}