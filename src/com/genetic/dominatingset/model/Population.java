package com.genetic.dominatingset.model;

import java.util.*;

/**
 * Represents a population of individuals in the genetic algorithm.
 *
 * The population maintains a collection of individuals and provides
 * methods for accessing, sorting, and manipulating them.
 *
 * @author Iman Salehi
 * @version 1.0
 */
public class Population {
    private final List<Individual> individuals;
    private final int size;
    private boolean sorted;

    /**
     * Creates a new population with the specified size.
     *
     * @param size the number of individuals in the population
     */
    public Population(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Population size must be positive");
        }

        this.size = size;
        this.individuals = new ArrayList<>(size);
        this.sorted = false;
    }

    /**
     * Creates a new population with random individuals.
     *
     * @param size the population size
     * @param chromosomeLength the length of each chromosome
     * @param random the random number generator
     */
    public Population(int size, int chromosomeLength, Random random) {
        this(size);

        for (int i = 0; i < size; i++) {
            individuals.add(new Individual(chromosomeLength, random));
        }
    }

    /**
     * Adds an individual to the population.
     *
     * @param individual the individual to add
     * @throws IllegalStateException if population is full
     */
    public void addIndividual(Individual individual) {
        if (individuals.size() >= size) {
            throw new IllegalStateException("Population is full");
        }

        individuals.add(individual);
        sorted = false;
    }

    /**
     * Returns an individual at the specified index.
     *
     * @param index the index
     * @return the individual
     */
    public Individual getIndividual(int index) {
        return individuals.get(index);
    }

    /**
     * Sets an individual at the specified index.
     *
     * @param index the index
     * @param individual the new individual
     */
    public void setIndividual(int index, Individual individual) {
        individuals.set(index, individual);
        sorted = false;
    }

    /**
     * Returns all individuals in the population.
     *
     * @return unmodifiable list of individuals
     */
    public List<Individual> getIndividuals() {
        return Collections.unmodifiableList(individuals);
    }

    /**
     * Returns the size of the population.
     *
     * @return the population size
     */
    public int getSize() {
        return individuals.size();
    }

    /**
     * Returns the maximum size of the population.
     *
     * @return the maximum size
     */
    public int getMaxSize() {
        return size;
    }

    /**
     * Sorts the population by fitness (descending order - best first).
     */
    public void sortByFitness() {
        Collections.sort(individuals);
        sorted = true;
    }

    /**
     * Checks if the population is sorted.
     *
     * @return true if sorted
     */
    public boolean isSorted() {
        return sorted;
    }

    /**
     * Returns the best individual in the population.
     *
     * @return the individual with highest fitness
     */
    public Individual getBest() {
        if (individuals.isEmpty()) {
            throw new IllegalStateException("Population is empty");
        }

        if (!sorted) {
            sortByFitness();
        }

        return individuals.get(0);
    }

    /**
     * Returns the worst individual in the population.
     *
     * @return the individual with lowest fitness
     */
    public Individual getWorst() {
        if (individuals.isEmpty()) {
            throw new IllegalStateException("Population is empty");
        }

        if (!sorted) {
            sortByFitness();
        }

        return individuals.get(individuals.size() - 1);
    }

    /**
     * Returns the average fitness of the population.
     *
     * @return the average fitness
     */
    public double getAverageFitness() {
        if (individuals.isEmpty()) {
            return 0.0;
        }

        double sum = 0.0;
        for (Individual individual : individuals) {
            sum += individual.getFitness();
        }

        return sum / individuals.size();
    }

    /**
     * Returns the best fitness in the population.
     *
     * @return the highest fitness value
     */
    public double getBestFitness() {
        return getBest().getFitness();
    }

    /**
     * Returns the worst fitness in the population.
     *
     * @return the lowest fitness value
     */
    public double getWorstFitness() {
        return getWorst().getFitness();
    }

    /**
     * Returns population statistics.
     *
     * @return a map containing statistics
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new LinkedHashMap<>();

        stats.put("size", individuals.size());
        stats.put("bestFitness", getBestFitness());
        stats.put("worstFitness", getWorstFitness());
        stats.put("averageFitness", getAverageFitness());

        // Count valid individuals
        long validCount = individuals.stream()
                .filter(Individual::isValid)
                .count();
        stats.put("validIndividuals", validCount);

        // Average dominating set size
        double avgSize = individuals.stream()
                .mapToInt(Individual::getDominatingSetSize)
                .average()
                .orElse(0.0);
        stats.put("averageSetSize", avgSize);

        return stats;
    }

    /**
     * Clears the population.
     */
    public void clear() {
        individuals.clear();
        sorted = false;
    }

    /**
     * Checks if the population is empty.
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return individuals.isEmpty();
    }

    /**
     * Checks if the population is full.
     *
     * @return true if full
     */
    public boolean isFull() {
        return individuals.size() >= size;
    }

    /**
     * Returns a string representation of the population.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return String.format("Population[size=%d/%d, bestFitness=%.2f, avgFitness=%.2f]",
                individuals.size(), size, getBestFitness(), getAverageFitness());
    }

    /**
     * Creates a deep copy of this population.
     *
     * @return a new population with copied individuals
     */
    public Population copy() {
        Population copy = new Population(this.size);

        for (Individual individual : individuals) {
            copy.addIndividual(individual.copy());
        }

        copy.sorted = this.sorted;
        return copy;
    }
}