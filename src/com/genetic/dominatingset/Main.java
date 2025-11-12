package com.genetic.dominatingset;

import com.genetic.dominatingset.algorithm.GeneticAlgorithm;
import com.genetic.dominatingset.model.Graph;
import com.genetic.dominatingset.model.Individual;
import com.genetic.dominatingset.util.GraphGenerator;
import com.genetic.dominatingset.util.ResultLogger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Main entry point for the Dominating Set Genetic Algorithm.
 *
 * This class provides a menu-driven interface for running the algorithm
 * on different graph types and configurations.
 *
 * @author Iman Salehi
 * @version 1.0
 */
public class Main {

    // Default algorithm parameters
    private static final int DEFAULT_POPULATION_SIZE = 100;
    private static final double DEFAULT_CROSSOVER_RATE = 0.4;
    private static final double DEFAULT_MUTATION_RATE = 0.1;
    private static final int DEFAULT_MAX_ITERATIONS = 50;

    public static void main(String[] args) {
        printHeader();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    runSampleTest();
                    break;
                case 2:
                    runRandomGraph(scanner);
                    break;
                case 3:
                    runPathGraph(scanner);
                    break;
                case 4:
                    runCycleGraph(scanner);
                    break;
                case 5:
                    runStarGraph(scanner);
                    break;
                case 6:
                    runGridGraph(scanner);
                    break;
                case 7:
                    runCompleteGraph(scanner);
                    break;
                case 8:
                    runCustomParameters(scanner);
                    break;
                case 9:
                    runBatchTests();
                    break;
                case 0:
                    System.out.println("\nThank you for using the Dominating Set GA!");
                    System.out.println("Goodbye!\n");
                    scanner.close();
                    return;
                default:
                    System.out.println("\nInvalid choice. Please try again.\n");
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    /**
     * Prints the application header.
     */
    private static void printHeader() {
        System.out.println("============================================");
        System.out.println("  DOMINATING SET PROBLEM");
        System.out.println("  Genetic Algorithm Solution");
        System.out.println("============================================");
        System.out.println();
    }

    /**
     * Prints the main menu.
     */
    private static void printMenu() {
        System.out.println("\n============ MAIN MENU ============");
        System.out.println("1. Run sample test (10 vertices)");
        System.out.println("2. Run on random graph");
        System.out.println("3. Run on path graph");
        System.out.println("4. Run on cycle graph");
        System.out.println("5. Run on star graph");
        System.out.println("6. Run on grid graph");
        System.out.println("7. Run on complete graph");
        System.out.println("8. Run with custom parameters");
        System.out.println("9. Run batch tests");
        System.out.println("0. Exit");
        System.out.println("===================================");
    }

    /**
     * Runs the algorithm on a predefined sample graph.
     */
    private static void runSampleTest() {
        System.out.println("\n=== Running Sample Test ===\n");

        GraphGenerator generator = new GraphGenerator();
        Graph graph = generator.generateSampleGraph10();

        System.out.println("Graph created:");
        System.out.println(graph);
        printGraphStatistics(graph);

        runAlgorithm(graph, "Sample_10_vertices");
    }

    /**
     * Runs the algorithm on a random graph.
     */
    private static void runRandomGraph(Scanner scanner) {
        System.out.println("\n=== Random Graph ===");
        System.out.print("Enter number of vertices: ");
        int numVertices = scanner.nextInt();
        System.out.print("Enter number of edges: ");
        int numEdges = scanner.nextInt();
        scanner.nextLine();

        GraphGenerator generator = new GraphGenerator();
        Graph graph = generator.generateRandomGraph(numVertices, numEdges);

        System.out.println("\nGraph created:");
        System.out.println(graph);
        printGraphStatistics(graph);

        runAlgorithm(graph, "Random_" + numVertices + "v_" + numEdges + "e");
    }

    /**
     * Runs the algorithm on a path graph.
     */
    private static void runPathGraph(Scanner scanner) {
        System.out.println("\n=== Path Graph ===");
        System.out.print("Enter number of vertices: ");
        int numVertices = scanner.nextInt();
        scanner.nextLine();

        GraphGenerator generator = new GraphGenerator();
        Graph graph = generator.generatePathGraph(numVertices);

        System.out.println("\nGraph created:");
        System.out.println(graph);
        printGraphStatistics(graph);

        System.out.println("Note: Optimal dominating set size is approximately " +
                           Math.ceil(numVertices / 3.0));

        runAlgorithm(graph, "Path_" + numVertices);
    }

    /**
     * Runs the algorithm on a cycle graph.
     */
    private static void runCycleGraph(Scanner scanner) {
        System.out.println("\n=== Cycle Graph ===");
        System.out.print("Enter number of vertices (minimum 3): ");
        int numVertices = scanner.nextInt();
        scanner.nextLine();

        GraphGenerator generator = new GraphGenerator();
        Graph graph = generator.generateCycleGraph(numVertices);

        System.out.println("\nGraph created:");
        System.out.println(graph);
        printGraphStatistics(graph);

        System.out.println("Note: Optimal dominating set size is approximately " +
                           Math.ceil(numVertices / 3.0));

        runAlgorithm(graph, "Cycle_" + numVertices);
    }

    /**
     * Runs the algorithm on a star graph.
     */
    private static void runStarGraph(Scanner scanner) {
        System.out.println("\n=== Star Graph ===");
        System.out.print("Enter number of vertices (minimum 2): ");
        int numVertices = scanner.nextInt();
        scanner.nextLine();

        GraphGenerator generator = new GraphGenerator();
        Graph graph = generator.generateStarGraph(numVertices);

        System.out.println("\nGraph created:");
        System.out.println(graph);
        printGraphStatistics(graph);

        System.out.println("Note: Optimal dominating set size is 1 (the center vertex)");

        runAlgorithm(graph, "Star_" + numVertices);
    }

    /**
     * Runs the algorithm on a grid graph.
     */
    private static void runGridGraph(Scanner scanner) {
        System.out.println("\n=== Grid Graph ===");
        System.out.print("Enter number of rows: ");
        int rows = scanner.nextInt();
        System.out.print("Enter number of columns: ");
        int cols = scanner.nextInt();
        scanner.nextLine();

        GraphGenerator generator = new GraphGenerator();
        Graph graph = generator.generateGridGraph(rows, cols);

        System.out.println("\nGraph created:");
        System.out.println(graph);
        printGraphStatistics(graph);

        runAlgorithm(graph, "Grid_" + rows + "x" + cols);
    }

    /**
     * Runs the algorithm on a complete graph.
     */
    private static void runCompleteGraph(Scanner scanner) {
        System.out.println("\n=== Complete Graph ===");
        System.out.print("Enter number of vertices: ");
        int numVertices = scanner.nextInt();
        scanner.nextLine();

        GraphGenerator generator = new GraphGenerator();
        Graph graph = generator.generateCompleteGraph(numVertices);

        System.out.println("\nGraph created:");
        System.out.println(graph);
        printGraphStatistics(graph);

        System.out.println("Note: Optimal dominating set size is 1 (any single vertex)");

        runAlgorithm(graph, "Complete_" + numVertices);
    }

    /**
     * Runs the algorithm with custom parameters.
     */
    private static void runCustomParameters(Scanner scanner) {
        System.out.println("\n=== Custom Parameters ===");

        // Get graph parameters
        System.out.print("Enter number of vertices: ");
        int numVertices = scanner.nextInt();
        System.out.print("Enter number of edges: ");
        int numEdges = scanner.nextInt();

        // Get algorithm parameters
        System.out.print("Enter population size (default 100): ");
        int popSize = scanner.nextInt();
        System.out.print("Enter crossover rate (default 0.4): ");
        double crossoverRate = scanner.nextDouble();
        System.out.print("Enter mutation rate (default 0.1): ");
        double mutationRate = scanner.nextDouble();
        System.out.print("Enter max iterations (default 50): ");
        int maxIterations = scanner.nextInt();
        scanner.nextLine();

        GraphGenerator generator = new GraphGenerator();
        Graph graph = generator.generateRandomGraph(numVertices, numEdges);

        System.out.println("\nGraph created:");
        System.out.println(graph);
        printGraphStatistics(graph);

        runAlgorithm(graph, "Custom", popSize, crossoverRate, mutationRate, maxIterations);
    }

    /**
     * Runs multiple batch tests for comprehensive evaluation.
     */
    private static void runBatchTests() {
        System.out.println("\n=== Running Batch Tests ===\n");

        GraphGenerator generator = new GraphGenerator();
        ResultLogger logger = new ResultLogger();

        // Test 1: Small graphs
        System.out.println("Test 1: Small random graph (10 vertices)");
        Graph g1 = generator.generateRandomGraph(10, 15);
        runAlgorithm(g1, "Batch_Small_10v");

        // Test 2: Medium graphs
        System.out.println("\nTest 2: Medium random graph (30 vertices)");
        Graph g2 = generator.generateRandomGraph(30, 60);
        runAlgorithm(g2, "Batch_Medium_30v");

        // Test 3: Path graph
        System.out.println("\nTest 3: Path graph (20 vertices)");
        Graph g3 = generator.generatePathGraph(20);
        runAlgorithm(g3, "Batch_Path_20v");

        // Test 4: Grid graph
        System.out.println("\nTest 4: Grid graph (5x5)");
        Graph g4 = generator.generateGridGraph(5, 5);
        runAlgorithm(g4, "Batch_Grid_5x5");

        System.out.println("\n=== Batch Tests Completed ===");
        System.out.println("Check the 'results' directory for detailed output.");
    }

    /**
     * Runs the genetic algorithm on a graph with default parameters.
     */
    private static void runAlgorithm(Graph graph, String testName) {
        runAlgorithm(graph, testName, DEFAULT_POPULATION_SIZE, DEFAULT_CROSSOVER_RATE,
                DEFAULT_MUTATION_RATE, DEFAULT_MAX_ITERATIONS);
    }

    /**
     * Runs the genetic algorithm on a graph with specified parameters.
     */
    private static void runAlgorithm(Graph graph, String testName, int populationSize,
                                     double crossoverRate, double mutationRate, int maxIterations) {
        System.out.println();

        // Create and run the genetic algorithm
        GeneticAlgorithm ga = new GeneticAlgorithm(graph, populationSize, crossoverRate,
                mutationRate, maxIterations);

        Individual bestSolution = ga.solve();

        // Print results
        ga.printResults();

        // Save results
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("populationSize", populationSize);
        parameters.put("crossoverRate", crossoverRate);
        parameters.put("mutationRate", mutationRate);
        parameters.put("maxIterations", maxIterations);

        ResultLogger logger = new ResultLogger();
        logger.logResults(graph, bestSolution, ga.getFitnessHistory(),
                ga.getConvergenceIteration(), ga.getExecutionTime(), parameters);

        logger.logFitnessHistoryCSV(ga.getFitnessHistory(),
                "fitness_" + testName + ".csv");

        logger.appendSummary(testName, graph.getNumVertices(),
                bestSolution.getDominatingSetSize(), ga.getConvergenceIteration(),
                ga.getExecutionTime());
    }

    /**
     * Prints graph statistics.
     */
    private static void printGraphStatistics(Graph graph) {
        Map<String, Object> stats = graph.getStatistics();
        System.out.println("\nGraph Statistics:");
        System.out.println("  Vertices: " + stats.get("vertices"));
        System.out.println("  Edges: " + stats.get("edges"));
        System.out.println("  Average Degree: " + String.format("%.2f", stats.get("averageDegree")));
        System.out.println("  Min Degree: " + stats.get("minDegree"));
        System.out.println("  Max Degree: " + stats.get("maxDegree"));
        System.out.println("  Density: " + String.format("%.4f", stats.get("density")));
        System.out.println();
    }
}