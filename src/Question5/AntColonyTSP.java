package src.Question5;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AntColonyTSP {
    private int[][] distances; // Distance matrix
    private int numAnts; // Number of ants
    private double[][] pheromones; // Pheromone matrix
    private double[][] probabilities; // Transition probabilities matrix
    private int numIterations; // Number of iterations
    private int numCities; // Number of cities
    private Random random;

    public AntColonyTSP(int[][] distances, int numAnts, int numIterations) {
        this.distances = distances;
        this.numAnts = numAnts;
        this.numIterations = numIterations;
        this.numCities = distances.length;
        this.pheromones = new double[numCities][numCities];
        this.probabilities = new double[numCities][numCities];
        this.random = new Random();
    }

    public void solveTSP() {
        // Initialize pheromones
        double initialPheromone = 1.0 / numCities;
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                pheromones[i][j] = initialPheromone;
            }
        }

        // Perform iterations
        for (int iteration = 0; iteration < numIterations; iteration++) {
            // Move ants
            for (int ant = 0; ant < numAnts; ant++) {
                boolean[] visited = new boolean[numCities];
                int[] path = new int[numCities];
                int currentCity = random.nextInt(numCities);
                path[0] = currentCity;
                visited[currentCity] = true;

                for (int i = 1; i < numCities; i++) {
                    int nextCity = selectNextCity(currentCity, visited);
                    path[i] = nextCity;
                    visited[nextCity] = true;
                    currentCity = nextCity;
                }

                // Update pheromones
                double pheromoneDelta = 1.0 / calculateTourLength(path);
                for (int i = 0; i < numCities - 1; i++) {
                    pheromones[path[i]][path[i + 1]] += pheromoneDelta;
                    pheromones[path[i + 1]][path[i]] += pheromoneDelta;
                }
                pheromones[path[numCities - 1]][path[0]] += pheromoneDelta;
                pheromones[path[0]][path[numCities - 1]] += pheromoneDelta;
            }

            // Update pheromone evaporation
            for (int i = 0; i < numCities; i++) {
                for (int j = 0; j < numCities; j++) {
                    pheromones[i][j] *= 0.9; // Evaporation factor
                }
            }
        }

        // Find best tour
        int[] bestTour = findBestTour();
        System.out.println("Best tour length: " + calculateTourLength(bestTour));
        System.out.print("Best tour path: ");
        for (int city : bestTour) {
            System.out.print(city + " ");
        }
    }

    private int selectNextCity(int currentCity, boolean[] visited) {
        double[] probabilities = calculateProbabilities(currentCity, visited);
        double randomValue = random.nextDouble();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < numCities; i++) {
            if (!visited[i]) {
                cumulativeProbability += probabilities[i];
                if (randomValue <= cumulativeProbability) {
                    return i;
                }
            }
        }
        throw new RuntimeException("No city selected");
    }

    private double[] calculateProbabilities(int currentCity, boolean[] visited) {
        double total = 0.0;
        for (int i = 0; i < numCities; i++) {
            if (!visited[i]) {
                total += Math.pow(pheromones[currentCity][i], 1.0) * Math.pow(1.0 / distances[currentCity][i], 5.0);
            }
        }
        double[] probabilities = new double[numCities];
        for (int i = 0; i < numCities; i++) {
            if (!visited[i]) {
                probabilities[i] = (Math.pow(pheromones[currentCity][i], 1.0) * Math.pow(1.0 / distances[currentCity][i], 5.0)) / total;
            }
        }
        return probabilities;
    }

    private int calculateTourLength(int[] tour) {
        int length = 0;
        for (int i = 0; i < tour.length - 1; i++) {
            length += distances[tour[i]][tour[i + 1]];
        }
        length += distances[tour[tour.length - 1]][tour[0]];
        return length;
    }

    private int[] findBestTour() {
        int[] bestTour = null;
        int shortestLength = Integer.MAX_VALUE;
        for (int ant = 0; ant < numAnts; ant++) {
            int[] tour = new int[numCities];
            boolean[] visited = new boolean[numCities];
            int currentCity = random.nextInt(numCities);
            tour[0] = currentCity;
            visited[currentCity] = true;

            for (int i = 1; i < numCities; i++) {
                int nextCity = selectNextCity(currentCity, visited);
                tour[i] = nextCity;
                visited[nextCity] = true;
                currentCity = nextCity;
            }

            int tourLength = calculateTourLength(tour);
            if (tourLength < shortestLength) {
                shortestLength = tourLength;
                bestTour = tour;
            }
        }
        return bestTour;
    }

    public static void main(String[] args) {
        int[][] distances = { { 0, 10, 15, 20 },
                { 10, 0, 35, 25 },
                { 15, 35, 0, 30 },
                { 20, 25, 30, 0 } };
        int numAnts = 5;
        int numIterations = 100;
        AntColonyTSP antColonyTSP = new AntColonyTSP(distances, numAnts, numIterations);
        antColonyTSP.solveTSP();
    }
}
