package com.genetic.dominatingset.algorithm;

import com.genetic.dominatingset.model.Individual;

import java.util.Random;

/**
 * Implements crossover operators for the genetic algorithm.
 *
 * Crossover combines genetic information from two parents to create offspring,
 * allowing the algorithm to explore new areas of the solution space.
 *
 * @author Iman Salehi
 * @version 1.0
 */
public class CrossoverOperator {
    private final Random random;

    /**
     * Creates a new crossover operator with default random seed.
     */
    public CrossoverOperator() {
        this.random = new Random();
    }

    /**
     * Creates a new crossover operator with specified random seed.
     *
     * @param seed the random seed for reproducibility
     */
    public CrossoverOperator(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Performs single-point crossover on two parents.
     *
     * A random crossover point is chosen, and genetic material is swapped
     * after that point to create two offspring.
     *
     * @param parent1 the first parent
     * @param parent2 the second parent
     * @return an array containing two offspring
     */
    public Individual[] singlePointCrossover(Individual parent1, Individual parent2) {
        int size = parent1.getSize();

        if (size != parent2.getSize()) {
            throw new IllegalArgumentException("Parents must have the same chromosome length");
        }

        // Choose crossover point (typically at middle, but can be random)
        int crossoverPoint = size / 2;

        // Create offspring
        int[] offspring1Genes = new int[size];
        int[] offspring2Genes = new int[size];

        // Copy genes before crossover point
        for (int i = 0; i < crossoverPoint; i++) {
            offspring1Genes[i] = parent1.getGene(i);
            offspring2Genes[i] = parent2.getGene(i);
        }

        // Copy genes after crossover point (swapped)
        for (int i = crossoverPoint; i < size; i++) {
            offspring1Genes[i] = parent2.getGene(i);
            offspring2Genes[i] = parent1.getGene(i);
        }

        return new Individual[]{
                new Individual(offspring1Genes),
                new Individual(offspring2Genes)
        };
    }

    /**
     * Performs single-point crossover with random crossover point.
     *
     * @param parent1 the first parent
     * @param parent2 the second parent
     * @return an array containing two offspring
     */
    public Individual[] singlePointCrossoverRandom(Individual parent1, Individual parent2) {
        int size = parent1.getSize();

        if (size != parent2.getSize()) {
            throw new IllegalArgumentException("Parents must have the same chromosome length");
        }

        // Random crossover point (avoid 0 and size to ensure mixing)
        int crossoverPoint = random.nextInt(size - 1) + 1;

        // Create offspring
        int[] offspring1Genes = new int[size];
        int[] offspring2Genes = new int[size];

        // Copy genes
        for (int i = 0; i < size; i++) {
            if (i < crossoverPoint) {
                offspring1Genes[i] = parent1.getGene(i);
                offspring2Genes[i] = parent2.getGene(i);
            } else {
                offspring1Genes[i] = parent2.getGene(i);
                offspring2Genes[i] = parent1.getGene(i);
            }
        }

        return new Individual[]{
                new Individual(offspring1Genes),
                new Individual(offspring2Genes)
        };
    }

    /**
     * Performs two-point crossover on two parents.
     *
     * Two crossover points are chosen, and genetic material between
     * these points is swapped to create offspring.
     *
     * @param parent1 the first parent
     * @param parent2 the second parent
     * @return an array containing two offspring
     */
    public Individual[] twoPointCrossover(Individual parent1, Individual parent2) {
        int size = parent1.getSize();

        if (size != parent2.getSize()) {
            throw new IllegalArgumentException("Parents must have the same chromosome length");
        }

        // Choose two random crossover points
        int point1 = random.nextInt(size - 1) + 1;
        int point2 = random.nextInt(size - 1) + 1;

        // Ensure point1 < point2
        if (point1 > point2) {
            int temp = point1;
            point1 = point2;
            point2 = temp;
        }

        // Create offspring
        int[] offspring1Genes = new int[size];
        int[] offspring2Genes = new int[size];

        // Copy genes
        for (int i = 0; i < size; i++) {
            if (i < point1 || i >= point2) {
                // Outside the crossover region
                offspring1Genes[i] = parent1.getGene(i);
                offspring2Genes[i] = parent2.getGene(i);
            } else {
                // Inside the crossover region (swap)
                offspring1Genes[i] = parent2.getGene(i);
                offspring2Genes[i] = parent1.getGene(i);
            }
        }

        return new Individual[]{
                new Individual(offspring1Genes),
                new Individual(offspring2Genes)
        };
    }

    /**
     * Performs uniform crossover on two parents.
     *
     * Each gene is independently chosen from either parent with equal probability.
     *
     * @param parent1 the first parent
     * @param parent2 the second parent
     * @return an array containing two offspring
     */
    public Individual[] uniformCrossover(Individual parent1, Individual parent2) {
        int size = parent1.getSize();

        if (size != parent2.getSize()) {
            throw new IllegalArgumentException("Parents must have the same chromosome length");
        }

        // Create offspring
        int[] offspring1Genes = new int[size];
        int[] offspring2Genes = new int[size];

        // For each gene, randomly choose which parent to inherit from
        for (int i = 0; i < size; i++) {
            if (random.nextBoolean()) {
                offspring1Genes[i] = parent1.getGene(i);
                offspring2Genes[i] = parent2.getGene(i);
            } else {
                offspring1Genes[i] = parent2.getGene(i);
                offspring2Genes[i] = parent1.getGene(i);
            }
        }

        return new Individual[]{
                new Individual(offspring1Genes),
                new Individual(offspring2Genes)
        };
    }

    /**
     * Performs crossover with the configured strategy.
     * Default is single-point crossover.
     *
     * @param parent1 the first parent
     * @param parent2 the second parent
     * @return an array containing two offspring
     */
    public Individual[] crossover(Individual parent1, Individual parent2) {
        return singlePointCrossover(parent1, parent2);
    }
}