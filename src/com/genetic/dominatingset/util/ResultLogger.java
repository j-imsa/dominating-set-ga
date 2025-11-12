
package com.genetic.dominatingset.util;

import com.genetic.dominatingset.model.Graph;
import com.genetic.dominatingset.model.Individual;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Utility class for logging results to files.
 *
 * Provides methods to save experimental results, fitness history,
 * and detailed analysis to output files.
 *
 * @author Iman Salehi
 * @version 1.0
 */
public class ResultLogger {
    private final String outputDirectory;

    /**
     * Creates a new result logger with default output directory.
     */
    public ResultLogger() {
        this("results");
    }

    /**
     * Creates a new result logger with specified output directory.
     *
     * @param outputDirectory the directory to save results
     */
    public ResultLogger(String outputDirectory) {
        this.outputDirectory = outputDirectory;

        // Create directory if it doesn't exist
        java.io.File dir = new java.io.File(outputDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Logs the complete results to a file.
     *
     * @param graph the graph instance
     * @param bestSolution the best solution found
     * @param fitnessHistory the fitness history across generations
     * @param convergenceIteration the convergence iteration
     * @param executionTime the execution time in milliseconds
     * @param parameters the algorithm parameters
     */
    public void logResults(Graph graph, Individual bestSolution,
                           List<Double> fitnessHistory, int convergenceIteration,
                           long executionTime, Map<String, Object> parameters) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = outputDirectory + "/results_" + timestamp + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Header
            writer.println("============================================");
            writer.println("DOMINATING SET PROBLEM - GENETIC ALGORITHM");
            writer.println("============================================");
            writer.println("Timestamp: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            writer.println();

            // Graph information
            writer.println("=== GRAPH INFORMATION ===");
            writer.println("Number of vertices: " + graph.getNumVertices());
            writer.println("Number of edges: " + graph.getNumEdges());
            Map<String, Object> graphStats = graph.getStatistics();
            writer.println("Average degree: " + String.format("%.2f", graphStats.get("averageDegree")));
            writer.println("Min degree: " + graphStats.get("minDegree"));
            writer.println("Max degree: " + graphStats.get("maxDegree"));
            writer.println("Density: " + String.format("%.4f", graphStats.get("density")));
            writer.println();

            // Algorithm parameters
            writer.println("=== ALGORITHM PARAMETERS ===");
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                writer.println(entry.getKey() + ": " + entry.getValue());
            }
            writer.println();

            // Results
            writer.println("=== RESULTS ===");
            writer.println("Best solution: " + bestSolution.toCompactString());
            writer.println("Dominating set: " + bestSolution.getDominatingSet());
            writer.println("Set size: " + bestSolution.getDominatingSetSize());
            writer.println("Fitness: " + bestSolution.getFitness());
            writer.println("Valid: " + (bestSolution.isValid() ? "Yes" : "No"));
            writer.println("Convergence iteration: " +
                           (convergenceIteration != -1 ? convergenceIteration : "Did not converge"));
            writer.println("Execution time: " + executionTime + " ms");
            writer.println();

            // Fitness history
            writer.println("=== FITNESS HISTORY ===");
            writer.println("Generation\tBest Fitness\tSet Size");
            for (int i = 0; i < fitnessHistory.size(); i++) {
                writer.println(i + "\t\t" + fitnessHistory.get(i) + "\t\t" +
                               Math.abs(fitnessHistory.get(i).intValue()));
            }
            writer.println();

            // Graph structure
            writer.println("=== GRAPH STRUCTURE ===");
            writer.println(graph.toString());

            writer.println("============================================");

            System.out.println("\nResults saved to: " + filename);

        } catch (IOException e) {
            System.err.println("Error writing results to file: " + e.getMessage());
        }
    }

    /**
     * Logs only the fitness history to a CSV file.
     *
     * @param fitnessHistory the fitness history
     * @param filename the output filename
     */
    public void logFitnessHistoryCSV(List<Double> fitnessHistory, String filename) {
        String fullPath = outputDirectory + "/" + filename;

        try (PrintWriter writer = new PrintWriter(new FileWriter(fullPath))) {
            writer.println("Generation,BestFitness,SetSize");

            for (int i = 0; i < fitnessHistory.size(); i++) {
                double fitness = fitnessHistory.get(i);
                int setSize = Math.abs((int) fitness);
                writer.println(i + "," + fitness + "," + setSize);
            }

            System.out.println("Fitness history saved to: " + fullPath);

        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }
    }

    /**
     * Appends a summary line to a cumulative results file.
     *
     * @param testName the name of the test
     * @param graphSize the graph size
     * @param bestSize the best dominating set size found
     * @param convergence the convergence iteration
     * @param executionTime the execution time
     */
    public void appendSummary(String testName, int graphSize, int bestSize,
                              int convergence, long executionTime) {
        String filename = outputDirectory + "/summary.txt";
        boolean fileExists = new java.io.File(filename).exists();

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {
            // Write header if file is new
            if (!fileExists) {
                writer.println("TestName\tGraphSize\tBestSize\tConvergence\tTime(ms)");
            }

            writer.println(testName + "\t" + graphSize + "\t" + bestSize + "\t" +
                           convergence + "\t" + executionTime);

        } catch (IOException e) {
            System.err.println("Error writing summary: " + e.getMessage());
        }
    }

    /**
     * Logs the graph structure to a file for visualization.
     *
     * @param graph the graph to log
     * @param filename the output filename
     */
    public void logGraphStructure(Graph graph, String filename) {
        String fullPath = outputDirectory + "/" + filename;

        try (PrintWriter writer = new PrintWriter(new FileWriter(fullPath))) {
            writer.println("# Graph structure");
            writer.println("# Format: vertex1 vertex2");
            writer.println("# Number of vertices: " + graph.getNumVertices());
            writer.println("# Number of edges: " + graph.getNumEdges());
            writer.println();

            for (int u = 0; u < graph.getNumVertices(); u++) {
                for (int v : graph.getNeighbors(u)) {
                    if (u < v) { // Print each edge only once
                        writer.println(u + " " + v);
                    }
                }
            }

            System.out.println("Graph structure saved to: " + fullPath);

        } catch (IOException e) {
            System.err.println("Error writing graph structure: " + e.getMessage());
        }
    }
}