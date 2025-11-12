package com.genetic.dominatingset.model;

import java.util.*;

/**
 * Represents an individual (chromosome) in the genetic algorithm.
 *
 * Each individual represents a potential solution to the dominating set problem,
 * encoded as a binary array where genes[i] = 1 means vertex i is in the dominating set.
 *
 * @author Iman Salehi
 * @version 1.0
 */
public class Individual implements Comparable<Individual> {
    private final int[] genes;
    private double fitness;
    private boolean fitnessCalculated;
    private boolean isValid;

    /**
     * Creates a new individual with the specified genes.
     *
     * @param genes binary array representing the solution
     */
    public Individual(int[] genes) {
        this.genes = genes.clone();
        this.fitness = Double.NEGATIVE_INFINITY;
        this.fitnessCalculated = false;
        this.isValid = false;
    }

    /**
     * Creates a new individual with random genes.
     *
     * @param size the number of genes (vertices)
     * @param random the random number generator
     */
    public Individual(int size, Random random) {
        this.genes = new int[size];

        for (int i = 0; i < size; i++) {
            this.genes[i] = random.nextInt(2); // 0 or 1
        }

        this.fitness = Double.NEGATIVE_INFINITY;
        this.fitnessCalculated = false;
        this.isValid = false;
    }

    /**
     * Returns the genes of this individual.
     *
     * @return a copy of the genes array
     */
    public int[] getGenes() {
        return genes.clone();
    }

    /**
     * Returns the value of a specific gene.
     *
     * @param index the gene index
     * @return 0 or 1
     */
    public int getGene(int index) {
        return genes[index];
    }

    /**
     * Sets the value of a specific gene.
     *
     * @param index the gene index
     * @param value the new value (0 or 1)
     */
    public void setGene(int index, int value) {
        if (value != 0 && value != 1) {
            throw new IllegalArgumentException("Gene value must be 0 or 1");
        }

        if (genes[index] != value) {
            genes[index] = value;
            fitnessCalculated = false; // Fitness needs recalculation
        }
    }

    /**
     * Flips a gene (0 -> 1 or 1 -> 0).
     *
     * @param index the gene index to flip
     */
    public void flipGene(int index) {
        genes[index] = 1 - genes[index];
        fitnessCalculated = false;
    }

    /**
     * Returns the number of genes.
     *
     * @return the chromosome length
     */
    public int getSize() {
        return genes.length;
    }

    /**
     * Returns the fitness value of this individual.
     *
     * @return the fitness value
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * Sets the fitness value of this individual.
     *
     * @param fitness the new fitness value
     */
    public void setFitness(double fitness) {
        this.fitness = fitness;
        this.fitnessCalculated = true;
    }

    /**
     * Checks if the fitness has been calculated.
     *
     * @return true if fitness is up-to-date
     */
    public boolean isFitnessCalculated() {
        return fitnessCalculated;
    }

    /**
     * Returns whether this individual represents a valid dominating set.
     *
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        return isValid;
    }

    /**
     * Sets the validity of this individual.
     *
     * @param valid the validity status
     */
    public void setValid(boolean valid) {
        this.isValid = valid;
    }

    /**
     * Returns the size of the dominating set (number of vertices selected).
     *
     * @return the number of 1s in the genes
     */
    public int getDominatingSetSize() {
        int count = 0;
        for (int gene : genes) {
            count += gene;
        }
        return count;
    }

    /**
     * Returns the vertices that are in the dominating set.
     *
     * @return a set of vertex indices
     */
    public Set<Integer> getDominatingSet() {
        Set<Integer> dominatingSet = new HashSet<>();

        for (int i = 0; i < genes.length; i++) {
            if (genes[i] == 1) {
                dominatingSet.add(i);
            }
        }

        return dominatingSet;
    }

    /**
     * Creates a copy of this individual.
     *
     * @return a new individual with the same genes
     */
    public Individual copy() {
        Individual copy = new Individual(this.genes);
        copy.fitness = this.fitness;
        copy.fitnessCalculated = this.fitnessCalculated;
        copy.isValid = this.isValid;
        return copy;
    }

    /**
     * Compares this individual with another based on fitness (for sorting).
     * Higher fitness comes first (descending order).
     *
     * @param other the other individual
     * @return negative if this is better, positive if other is better
     */
    @Override
    public int compareTo(Individual other) {
        // Descending order (higher fitness first)
        return Double.compare(other.fitness, this.fitness);
    }

    /**
     * Returns a string representation of this individual.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return String.format("Individual[genes=%s, fitness=%.2f, size=%d, valid=%b]",
                Arrays.toString(genes), fitness, getDominatingSetSize(), isValid);
    }

    /**
     * Returns a compact string representation (just the genes).
     *
     * @return compact representation
     */
    public String toCompactString() {
        return Arrays.toString(genes);
    }

    /**
     * Checks equality based on genes only.
     *
     * @param obj the object to compare
     * @return true if genes are identical
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Individual other = (Individual) obj;
        return Arrays.equals(genes, other.genes);
    }

    /**
     * Returns hash code based on genes.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(genes);
    }
}