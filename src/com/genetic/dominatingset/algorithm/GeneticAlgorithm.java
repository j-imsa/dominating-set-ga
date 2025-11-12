package com.genetic.dominatingset.algorithm;

import com.genetic.dominatingset.model.Graph;
import com.genetic.dominatingset.model.Individual;
import com.genetic.dominatingset.model.Population;

import java.util.*;

/**
 * Main genetic algorithm implementation for the Dominating Set Problem.
 *
 * This class orchestrates the genetic algorithm components (selection,
 * crossover, mutation) to evolve solutions over multiple generations.
 *
 * @author Iman Salehi
 * @version 1.0
 */
public class GeneticAlgorithm {
    // Algorithm parameters
    private final int populationSize;
    private final double crossoverRate;
    private final double mutationRate;
    private final int maxIterations;
    private final int eliteSize;
    private final int numCrossover;

    // Graph and operators
    private final Graph graph;
    private final FitnessEvaluator fitnessEvaluator;
    private final SelectionOperator selectionOperator;
    private final CrossoverOperator crossoverOperator;
    private final MutationOperator mutationOperator;

    // Tracking
    private Population currentPopulation;
    private Individual bestSolution;
    private List<Double> fitnessHistory;
    private int convergenceIteration;
    private long startTime;
    private long endTime;

    /**
     * Creates a new genetic algorithm with specified parameters.
     *
     * @param graph the graph to solve
     * @param populationSize the number of individuals in the population
     * @param crossoverRate the fraction of population for crossover
     * @param mutationRate the probability of mutation
     * @param maxIterations the maximum number of generations
     */
    public GeneticAlgorithm(Graph graph, int populationSize, double crossoverRate,
                            double mutationRate, int maxIterations) {
        this.graph = graph;
        this.populationSize = populationSize;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.maxIterations = maxIterations;

        // Calculate derived parameters
        this.numCrossover = (int) (populationSize * crossoverRate);
        this.eliteSize = populationSize - numCrossover;

        // Initialize operators
        this.fitnessEvaluator = new FitnessEvaluator(graph);
        this.selectionOperator = new SelectionOperator();
        this.crossoverOperator = new CrossoverOperator();
        this.mutationOperator = new MutationOperator(mutationRate);

        // Initialize tracking
        this.fitnessHistory = new ArrayList<>();
        this.convergenceIteration = -1;
    }

    /**
     * Initializes the population with random individuals.
     */
    private void initializePopulation() {
        Random random = new Random();
        currentPopulation = new Population(populationSize, graph.getNumVertices(), random);

        // Evaluate initial population
        for (int i = 0; i < currentPopulation.getSize(); i++) {
            fitnessEvaluator.evaluate(currentPopulation.getIndividual(i));
        }

        // Sort and track best
        currentPopulation.sortByFitness();
        bestSolution = currentPopulation.getBest().copy();
    }

    /**
     * Runs the genetic algorithm.
     *
     * @return the best solution found
     */
    public Individual solve() {
        startTime = System.currentTimeMillis();

        System.out.println("============================================");
        System.out.println("Starting Genetic Algorithm");
        System.out.println("============================================");
        System.out.println("Population Size: " + populationSize);
        System.out.println("Crossover Rate: " + crossoverRate);
        System.out.println("Mutation Rate: " + mutationRate);
        System.out.println("Max Iterations: " + maxIterations);
        System.out.println("Elite Size: " + eliteSize);
        System.out.println("Num Crossover: " + numCrossover);
        System.out.println();

        // Initialize
        initializePopulation();

        // Track initial state
        fitnessHistory.add(bestSolution.getFitness());
        double previousBestFitness = bestSolution.getFitness();

        // Main evolution loop
        for (int iteration = 1; iteration <= maxIterations; iteration++) {
            // Selection: Keep elite individuals
            Population elite = selectionOperator.selectElite(currentPopulation, eliteSize);

            // Create new population starting with elite
            Population newPopulation = new Population(populationSize);
            for (int i = 0; i < elite.getSize(); i++) {
                newPopulation.addIndividual(elite.getIndividual(i).copy());
            }

            // Crossover: Generate offspring
            int offspringCreated = 0;
            while (offspringCreated < numCrossover) {
                // Select two parents
                Individual[] parents = selectionOperator.selectTwoParents(elite);

                // Perform crossover
                Individual[] offspring = crossoverOperator.crossover(parents[0], parents[1]);

                // Apply mutation
                mutationOperator.mutate(offspring[0]);
                mutationOperator.mutate(offspring[1]);

                // Evaluate offspring
                fitnessEvaluator.evaluate(offspring[0]);
                fitnessEvaluator.evaluate(offspring[1]);

                // Add to new population
                if (offspringCreated < numCrossover) {
                    newPopulation.addIndividual(offspring[0]);
                    offspringCreated++;
                }
                if (offspringCreated < numCrossover) {
                    newPopulation.addIndividual(offspring[1]);
                    offspringCreated++;
                }
            }

            // Replace current population
            currentPopulation = newPopulation;
            currentPopulation.sortByFitness();

            // Update best solution
            Individual currentBest = currentPopulation.getBest();
            if (currentBest.getFitness() > bestSolution.getFitness()) {
                bestSolution = currentBest.copy();
            }

            // Track fitness history
            fitnessHistory.add(bestSolution.getFitness());

            // Check for convergence
            if (convergenceIteration == -1 &&
                Math.abs(bestSolution.getFitness() - previousBestFitness) < 1e-6) {
                convergenceIteration = iteration;
            }
            previousBestFitness = bestSolution.getFitness();

            // Print progress
            if (iteration == 1 || iteration % 5 == 0 || iteration == maxIterations) {
                printProgress(iteration);
            }
        }

        endTime = System.currentTimeMillis();

        System.out.println("\n============================================");
        System.out.println("Algorithm Completed");
        System.out.println("============================================");

        return bestSolution;
    }

    /**
     * Prints progress information for the current iteration.
     *
     * @param iteration the current iteration number
     */
    private void printProgress(int iteration) {
        System.out.printf("Generation %3d | Best Fitness: %6.0f | Set Size: %d | Valid: %s%s\n",
                iteration,
                bestSolution.getFitness(),
                bestSolution.getDominatingSetSize(),
                bestSolution.isValid() ? "Yes" : "No",
                (convergenceIteration == iteration) ? " ✓ (converged)" : "");
    }

    /**
     * Prints detailed results.
     */
    public void printResults() {
        System.out.println("\n============================================");
        System.out.println("FINAL RESULTS");
        System.out.println("============================================");
        System.out.println("Best solution: " + bestSolution.toCompactString());
        System.out.println("Dominating set: " + bestSolution.getDominatingSet());
        System.out.println("Set size: " + bestSolution.getDominatingSetSize());
        System.out.println("Fitness: " + bestSolution.getFitness());
        System.out.println("Valid: " + (bestSolution.isValid() ? "Yes ✓" : "No ✗"));
        System.out.println("Convergence iteration: " +
                           (convergenceIteration != -1 ? convergenceIteration : "Did not converge"));
        System.out.println("Execution time: " + (endTime - startTime) + " ms");
        System.out.println();

        // Detailed domination check
        if (bestSolution.isValid()) {
            System.out.println("All vertices are properly dominated ✓");
        } else {
            System.out.println("Solution has violations ✗");
            fitnessEvaluator.printDominationInfo(bestSolution);
        }
    }

    /**
     * Returns the fitness history across generations.
     *
     * @return list of best fitness values per generation
     */
    public List<Double> getFitnessHistory() {
        return Collections.unmodifiableList(fitnessHistory);
    }

    /**
     * Returns the best solution found.
     *
     * @return the best individual
     */
    public Individual getBestSolution() {
        return bestSolution;
    }

    /**
     * Returns the convergence iteration.
     *
     * @return the iteration where convergence occurred, or -1 if not converged
     */
    public int getConvergenceIteration() {
        return convergenceIteration;
    }

    /**
     * Returns the execution time in milliseconds.
     *
     * @return execution time
     */
    public long getExecutionTime() {
        return endTime - startTime;
    }
}