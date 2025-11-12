package com.genetic.dominatingset.algorithm;

import com.genetic.dominatingset.model.Individual;
import com.genetic.dominatingset.model.Population;

import java.util.Random;

/**
 * Implements selection operators for the genetic algorithm.
 *
 * This class uses elitist selection strategy where the best individuals
 * from the current generation are preserved for the next generation.
 *
 * @author Iman Salehi
 * @version 1.0
 */
public class SelectionOperator {
    private final Random random;

    /**
     * Creates a new selection operator with default random seed.
     */
    public SelectionOperator() {
        this.random = new Random();
    }

    /**
     * Creates a new selection operator with specified random seed.
     *
     * @param seed the random seed for reproducibility
     */
    public SelectionOperator(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Performs elitist selection.
     *
     * Keeps the best 'eliteSize' individuals and uses them as parents
     * for generating offspring.
     *
     * @param population the current population
     * @param eliteSize the number of best individuals to keep
     * @return a new population containing only elite individuals
     */
    public Population selectElite(Population population, int eliteSize) {
        if (eliteSize > population.getSize()) {
            throw new IllegalArgumentException("Elite size cannot exceed population size");
        }

        // Sort population by fitness (best first)
        if (!population.isSorted()) {
            population.sortByFitness();
        }

        // Create new population with elite individuals
        Population elite = new Population(eliteSize);

        for (int i = 0; i < eliteSize; i++) {
            elite.addIndividual(population.getIndividual(i).copy());
        }

        return elite;
    }

    /**
     * Selects a random parent from the elite population.
     *
     * @param elite the elite population
     * @return a randomly selected individual
     */
    public Individual selectRandomParent(Population elite) {
        int index = random.nextInt(elite.getSize());
        return elite.getIndividual(index);
    }

    /**
     * Selects two different random parents from the elite population.
     *
     * @param elite the elite population
     * @return an array containing two parent individuals
     */
    public Individual[] selectTwoParents(Population elite) {
        if (elite.getSize() < 2) {
            throw new IllegalStateException("Need at least 2 individuals for parent selection");
        }

        int parent1Index = random.nextInt(elite.getSize());
        int parent2Index;

        // Ensure parents are different
        do {
            parent2Index = random.nextInt(elite.getSize());
        } while (parent2Index == parent1Index);

        return new Individual[]{
                elite.getIndividual(parent1Index),
                elite.getIndividual(parent2Index)
        };
    }

    /**
     * Tournament selection: selects the best individual from a random subset.
     *
     * @param population the population to select from
     * @param tournamentSize the size of the tournament
     * @return the winner of the tournament
     */
    public Individual tournamentSelection(Population population, int tournamentSize) {
        if (tournamentSize > population.getSize()) {
            tournamentSize = population.getSize();
        }

        Individual best = null;

        for (int i = 0; i < tournamentSize; i++) {
            int randomIndex = random.nextInt(population.getSize());
            Individual candidate = population.getIndividual(randomIndex);

            if (best == null || candidate.getFitness() > best.getFitness()) {
                best = candidate;
            }
        }

        return best;
    }

    /**
     * Roulette wheel selection based on fitness proportions.
     *
     * Note: This requires all fitness values to be non-negative.
     * For dominating set, we need to shift fitness values first.
     *
     * @param population the population to select from
     * @return the selected individual
     */
    public Individual rouletteWheelSelection(Population population) {
        // Calculate total fitness (shift to make all values positive)
        double minFitness = population.getWorstFitness();
        double shift = minFitness < 0 ? Math.abs(minFitness) + 1 : 0;

        double totalFitness = 0.0;
        for (int i = 0; i < population.getSize(); i++) {
            totalFitness += population.getIndividual(i).getFitness() + shift;
        }

        // Generate random value
        double randomValue = random.nextDouble() * totalFitness;

        // Select individual
        double cumulativeFitness = 0.0;
        for (int i = 0; i < population.getSize(); i++) {
            Individual individual = population.getIndividual(i);
            cumulativeFitness += individual.getFitness() + shift;

            if (cumulativeFitness >= randomValue) {
                return individual;
            }
        }

        // Fallback (should not reach here)
        return population.getIndividual(population.getSize() - 1);
    }
}