package com.genetic.dominatingset.algorithm;

import com.genetic.dominatingset.model.Graph;
import com.genetic.dominatingset.model.Individual;

import java.util.Set;

/**
 * Evaluates the fitness of individuals for the dominating set problem.
 *
 * The fitness function balances two objectives:
 * 1. Ensure the solution is a valid dominating set
 * 2. Minimize the size of the dominating set
 *
 * @author Iman Salehi
 * @version 1.0
 */
public class FitnessEvaluator {
    private final Graph graph;
    private final double penaltyCoefficient;

    /**
     * Creates a new fitness evaluator for the given graph.
     *
     * @param graph the graph to evaluate solutions against
     */
    public FitnessEvaluator(Graph graph) {
        this(graph, 1000.0);
    }

    /**
     * Creates a new fitness evaluator with custom penalty coefficient.
     *
     * @param graph the graph to evaluate solutions against
     * @param penaltyCoefficient penalty for constraint violations
     */
    public FitnessEvaluator(Graph graph, double penaltyCoefficient) {
        this.graph = graph;
        this.penaltyCoefficient = penaltyCoefficient;
    }

    /**
     * Evaluates the fitness of an individual.
     *
     * Fitness function:
     * - If valid: fitness = -|D| (negative size, to maximize means minimize size)
     * - If invalid: fitness = -|D| - penalty * violations
     *
     * @param individual the individual to evaluate
     */
    public void evaluate(Individual individual) {
        int[] genes = individual.getGenes();
        int n = genes.length;

        // Count the size of the dominating set
        int dominatingSetSize = 0;
        for (int gene : genes) {
            dominatingSetSize += gene;
        }

        // Check validity and count violations
        int violations = countViolations(genes);
        boolean isValid = (violations == 0);

        // Calculate fitness
        double fitness;
        if (isValid) {
            // Valid solution: fitness = -size (we want to minimize size)
            fitness = -dominatingSetSize;
        } else {
            // Invalid solution: apply penalty
            fitness = -dominatingSetSize - penaltyCoefficient * violations;
        }

        // Update individual
        individual.setFitness(fitness);
        individual.setValid(isValid);
    }

    /**
     * Counts the number of constraint violations in a solution.
     *
     * A vertex violates the constraint if:
     * - It is not in the dominating set (gene = 0)
     * - AND none of its neighbors are in the dominating set
     *
     * @param genes the chromosome to check
     * @return the number of vertices that are not dominated
     */
    private int countViolations(int[] genes) {
        int violations = 0;

        for (int v = 0; v < genes.length; v++) {
            // If vertex v is in the dominating set, it's automatically dominated
            if (genes[v] == 1) {
                continue;
            }

            // Check if at least one neighbor is in the dominating set
            boolean isDominated = false;
            Set<Integer> neighbors = graph.getNeighbors(v);

            for (int neighbor : neighbors) {
                if (genes[neighbor] == 1) {
                    isDominated = true;
                    break;
                }
            }

            if (!isDominated) {
                violations++;
            }
        }

        return violations;
    }

    /**
     * Checks if an individual represents a valid dominating set.
     *
     * @param individual the individual to check
     * @return true if valid, false otherwise
     */
    public boolean isValidDominatingSet(Individual individual) {
        return countViolations(individual.getGenes()) == 0;
    }

    /**
     * Returns the number of violations for an individual.
     *
     * @param individual the individual to check
     * @return the number of constraint violations
     */
    public int getViolations(Individual individual) {
        return countViolations(individual.getGenes());
    }

    /**
     * Prints detailed domination information for debugging.
     *
     * @param individual the individual to analyze
     */
    public void printDominationInfo(Individual individual) {
        int[] genes = individual.getGenes();
        System.out.println("\n=== Domination Analysis ===");
        System.out.println("Dominating Set: " + individual.getDominatingSet());
        System.out.println("Size: " + individual.getDominatingSetSize());

        for (int v = 0; v < genes.length; v++) {
            if (genes[v] == 1) {
                System.out.println("Vertex " + v + ": IN SET");
            } else {
                Set<Integer> neighbors = graph.getNeighbors(v);
                boolean dominated = false;

                for (int neighbor : neighbors) {
                    if (genes[neighbor] == 1) {
                        dominated = true;
                        System.out.println("Vertex " + v + ": dominated by " + neighbor);
                        break;
                    }
                }

                if (!dominated) {
                    System.out.println("Vertex " + v + ": NOT DOMINATED ✗");
                }
            }
        }
    }
}