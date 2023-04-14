import java.util.Arrays;
import java.util.Random;

public class Main {

    private static final int NESTS = 25;
    private static final int MAX_ITERATIONS = 1000;
    private static final double PA = 0.25;

    private static Random random = new Random();

    public static void main(String[] args) {
     // Number of courses
        int numCourses = 5;

        // Maximum study hours per week
        double maxStudyHours = 20;

        // Preferred study hours and weights for each course
        double[] preferredStudyHours = {3, 4, 4, 5, 4};
        double[] courseWeights = {1, 1.2, 1, 1.5, 1};

        double[] lowerBounds = new double[numCourses];
        double[] upperBounds = new double[numCourses];
        Arrays.fill(lowerBounds, 0);
        Arrays.fill(upperBounds, maxStudyHours);

        double[] solution = optimize(lowerBounds, upperBounds,
            x -> objectiveFunction(x, maxStudyHours, preferredStudyHours, courseWeights));
        System.out.println("Optimal study schedule: " + Arrays.toString(solution));
    }

    public static double[] optimize(double[] lowerBounds, double[] upperBounds, FitnessFunction fitnessFunction) {
        double[][] nests = initializeNests(lowerBounds, upperBounds);
        double[] bestNest = findBestNest(nests, fitnessFunction);

        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            for (int i = 0; i < nests.length; i++) {
                double[] newNest = generateNewSolution(nests[i], lowerBounds, upperBounds);
                if (fitnessFunction.eval(newNest) < fitnessFunction.eval(nests[i])) {
                    nests[i] = newNest;
                }
            }

            for (int i = 0; i < nests.length; i++) {
                if (random.nextDouble() < PA) {
                    nests[i] = generateRandomSolution(lowerBounds, upperBounds);
                }
            }

            double[] newBestNest = findBestNest(nests, fitnessFunction);
            if (fitnessFunction.eval(newBestNest) < fitnessFunction.eval(bestNest)) {
                bestNest = newBestNest;
            }
        }

        return bestNest;
    }

    private static double[][] initializeNests(double[] lowerBounds, double[] upperBounds) {
        double[][] nests = new double[NESTS][lowerBounds.length];
        for (int i = 0; i < NESTS; i++) {
            nests[i] = generateRandomSolution(lowerBounds, upperBounds);
        }
        return nests;
    }

    private static double[] generateRandomSolution(double[] lowerBounds, double[] upperBounds) {
        double[] solution = new double[lowerBounds.length];
        for (int i = 0; i < lowerBounds.length; i++) {
            solution[i] = lowerBounds[i] + random.nextDouble() * (upperBounds[i] - lowerBounds[i]);
        }
        return solution;
    }

    private static double[] generateNewSolution(double[] currentSolution, double[] lowerBounds, double[] upperBounds) {
        double[] newSolution = new double[currentSolution.length];
        for (int i = 0; i < currentSolution.length; i++) {
            newSolution[i] = currentSolution[i] + random.nextGaussian() * (upperBounds[i] - lowerBounds[i]);
        }
        return newSolution;
    }

    private static double[] findBestNest(double[][] nests, FitnessFunction fitnessFunction) {
        double[] bestNest = nests[0];
        for (int i = 1; i < nests.length; i++) {
            if (fitnessFunction.eval(nests[i]) < fitnessFunction.eval(bestNest)) {
                bestNest = nests[i];
            }
        }
        return bestNest;

    }
       private static double objectiveFunction(double[] x, double maxStudyHours,
                                            double[] preferredStudyHours, double[] courseWeights) {
        double totalStudyHours = Arrays.stream(x).sum();
        if (totalStudyHours > maxStudyHours) {
            return Double.MAX_VALUE;
        }

        double discomfort = 0;
        for (int i = 0; i < x.length; i++) {
            discomfort += Math.abs(x[i] - preferredStudyHours[i]) * courseWeights[i];
        }
        return discomfort;
    }

    public interface FitnessFunction {
        double eval(double[] x);
    }
 
    
    }

 
    


