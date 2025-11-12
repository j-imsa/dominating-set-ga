package com.genetic.dominatingset.algorithm;

import com.genetic.dominatingset.model.Individual;

import java.util.Random;

/**
 * Implements mutation operators for the genetic algorithm.
 *
 * Mutation introduces random changes to individuals, helping to maintain
 * genetic diversity and prevent premature convergence.
 *
 * @author Iman Salehi
 * @version 1.0
 */
public class MutationOperator {
    private final Random random;
    private final double mutationRate;

    /**
     * Creates a new mutation operator with default mutation rate.
     */
    public MutationOperator() {
        this(0.1);
    }

    /**
     * Creates a new mutation operator with specified mutation rate.
     *
     * @param mutationRate the probability of mutation (0.0 to 1.0)
     */
    public MutationOperator(double mutationRate) {
        if (mutationRate < 0.0 || mutationRate > 1.0) {
            throw new IllegalArgumentException("Mutation rate must be between 0.0 and 1.0");
        }

        this.random = new Random();
        this.mutationRate = mutationRate;
    }

    /**
     * Creates a new mutation operator with specified parameters.
     *
     * @param mutationRate the probability of mutation
     * @param seed the random seed for reproducibility
     */
    public MutationOperator(double mutationRate, long seed) {
        if (mutationRate < 0.0 || mutationRate > 1.0) {
            throw new IllegalArgumentException("Mutation rate must be between 0.0 and 1.0");
        }

        this.random = new Random(seed);
        this.mutationRate = mutationRate;
    }

    /**
     * Applies bit-flip mutation to an individual.
     *
     * With probability mutationRate, a random gene is flipped (0 -> 1 or 1 -> 0).
     * This is the mutation strategy used in the reference MATLAB code.
     *
     * @param individual the individual to mutate
     * @return true if mutation occurred, false otherwise
     */
    public boolean mutate(Individual individual) {
        if (random.nextDouble() < mutationRate) {
            // Select a random gene to flip
            int geneIndex = random.nextInt(individual.getSize());
            individual.flipGene(geneIndex);
            return true;
        }
        return false;
    }

    /**
     * Applies bit-flip mutation to all genes with independent probabilities.
     *
     * Each gene has an independent probability of being flipped.
     * This provides more exploration than single-gene mutation.
     *
     * @param individual the individual to mutate
     * @return the number of genes that were mutated
     */
    public int mutateAllGenes(Individual individual) {
        int mutationCount = 0;

        for (int i = 0; i < individual.getSize(); i++) {
            if (random.nextDouble() < mutationRate) {
                individual.flipGene(i);
                mutationCount++;
            }
        }

        return mutationCount;
    }

    /**
     * Applies uniform mutation: flips exactly one random gene.
     *
     * This ensures that every mutation changes the individual,
     * which can be useful for maintaining diversity.
     *
     * @param individual the individual to mutate
     */
    public void uniformMutation(Individual individual) {
        int geneIndex = random.nextInt(individual.getSize());
        individual.flipGene(geneIndex);
    }

    /**
     * Applies swap mutation: swaps the values of two random genes.
     *
     * This is less disruptive than bit-flip for binary encoding
     * but ensures change occurs.
     *
     * @param individual the individual to mutate
     */
    public void swapMutation(Individual individual) {
        if (individual.getSize() < 2) {
            return;
        }

        int gene1 = random.nextInt(individual.getSize());
        int gene2;

        do {
            gene2 = random.nextInt(individual.getSize());
        } while (gene2 == gene1);

        // Swap
        int temp = individual.getGene(gene1);
        individual.setGene(gene1, individual.getGene(gene2));
        individual.setGene(gene2, temp);
    }

    /**
     * Applies adaptive mutation based on individual fitness.
     *
     * Individuals with lower fitness have higher mutation rates
     * to encourage more exploration.
     *
     * @param individual the individual to mutate
     * @param populationBestFitness the best fitness in the population
     * @return true if mutation occurred
     */
    public boolean adaptiveMutate(Individual individual, double populationBestFitness) {
        // Calculate adaptive mutation rate
        double fitnessRatio = individual.getFitness() / populationBestFitness;
        double adaptiveRate = mutationRate * (1.0 - fitnessRatio);

        // Ensure rate is within bounds
        adaptiveRate = Math.max(0.01, Math.min(0.5, adaptiveRate));

        if (random.nextDouble() < adaptiveRate) {
            int geneIndex = random.nextInt(individual.getSize());
            individual.flipGene(geneIndex);
            return true;
        }

        return false;
    }

    /**
     * Returns the mutation rate.
     *
     * @return the mutation rate
     */
    public double getMutationRate() {
        return mutationRate;
    }
}