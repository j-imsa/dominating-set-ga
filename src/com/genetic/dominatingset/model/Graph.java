
package com.genetic.dominatingset.model;

import java.util.*;

/**
 * Represents an undirected graph for the Dominating Set Problem.
 *
 * The graph is represented using an adjacency list for efficient
 * neighbor lookup during fitness evaluation.
 *
 * @author Iman Salehi
 * @version 1.0
 */
public class Graph {
    private final int numVertices;
    private final List<Set<Integer>> adjacencyList;
    private int numEdges;

    /**
     * Creates a new graph with the specified number of vertices.
     *
     * @param numVertices the number of vertices in the graph
     * @throws IllegalArgumentException if numVertices <= 0
     */
    public Graph(int numVertices) {
        if (numVertices <= 0) {
            throw new IllegalArgumentException("Number of vertices must be positive");
        }

        this.numVertices = numVertices;
        this.adjacencyList = new ArrayList<>(numVertices);
        this.numEdges = 0;

        // Initialize adjacency list
        for (int i = 0; i < numVertices; i++) {
            adjacencyList.add(new HashSet<>());
        }
    }

    /**
     * Adds an undirected edge between two vertices.
     *
     * @param u first vertex
     * @param v second vertex
     * @throws IllegalArgumentException if vertices are out of bounds or equal
     */
    public void addEdge(int u, int v) {
        validateVertex(u);
        validateVertex(v);

        if (u == v) {
            throw new IllegalArgumentException("Self-loops are not allowed");
        }

        // Add edge only if it doesn't exist (avoid duplicates)
        if (!adjacencyList.get(u).contains(v)) {
            adjacencyList.get(u).add(v);
            adjacencyList.get(v).add(u);
            numEdges++;
        }
    }

    /**
     * Returns the neighbors of a given vertex.
     *
     * @param vertex the vertex to query
     * @return an unmodifiable set of neighboring vertices
     * @throws IllegalArgumentException if vertex is out of bounds
     */
    public Set<Integer> getNeighbors(int vertex) {
        validateVertex(vertex);
        return Collections.unmodifiableSet(adjacencyList.get(vertex));
    }

    /**
     * Returns the degree (number of neighbors) of a vertex.
     *
     * @param vertex the vertex to query
     * @return the degree of the vertex
     */
    public int getDegree(int vertex) {
        validateVertex(vertex);
        return adjacencyList.get(vertex).size();
    }

    /**
     * Checks if two vertices are adjacent (connected by an edge).
     *
     * @param u first vertex
     * @param v second vertex
     * @return true if vertices are adjacent, false otherwise
     */
    public boolean hasEdge(int u, int v) {
        validateVertex(u);
        validateVertex(v);
        return adjacencyList.get(u).contains(v);
    }

    /**
     * Returns the number of vertices in the graph.
     *
     * @return the number of vertices
     */
    public int getNumVertices() {
        return numVertices;
    }

    /**
     * Returns the number of edges in the graph.
     *
     * @return the number of edges
     */
    public int getNumEdges() {
        return numEdges;
    }

    /**
     * Validates that a vertex index is within bounds.
     *
     * @param vertex the vertex to validate
     * @throws IllegalArgumentException if vertex is out of bounds
     */
    private void validateVertex(int vertex) {
        if (vertex < 0 || vertex >= numVertices) {
            throw new IllegalArgumentException(
                    String.format("Vertex %d is out of bounds [0, %d)", vertex, numVertices)
            );
        }
    }

    /**
     * Returns a string representation of the graph's adjacency list.
     *
     * @return string representation of the graph
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Graph with %d vertices and %d edges:\n", numVertices, numEdges));

        for (int i = 0; i < numVertices; i++) {
            sb.append(String.format("  Vertex %d: %s\n", i, adjacencyList.get(i)));
        }

        return sb.toString();
    }

    /**
     * Creates a copy of this graph.
     *
     * @return a new graph with the same structure
     */
    public Graph copy() {
        Graph copy = new Graph(this.numVertices);

        for (int u = 0; u < numVertices; u++) {
            for (int v : adjacencyList.get(u)) {
                if (u < v) { // Add each edge only once
                    copy.addEdge(u, v);
                }
            }
        }

        return copy;
    }

    /**
     * Returns basic statistics about the graph.
     *
     * @return a map containing graph statistics
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("vertices", numVertices);
        stats.put("edges", numEdges);

        // Calculate average degree
        double avgDegree = (2.0 * numEdges) / numVertices;
        stats.put("averageDegree", avgDegree);

        // Find min and max degree
        int minDegree = Integer.MAX_VALUE;
        int maxDegree = 0;

        for (int i = 0; i < numVertices; i++) {
            int degree = getDegree(i);
            minDegree = Math.min(minDegree, degree);
            maxDegree = Math.max(maxDegree, degree);
        }

        stats.put("minDegree", minDegree);
        stats.put("maxDegree", maxDegree);

        // Graph density: actual edges / possible edges
        double density = (2.0 * numEdges) / (numVertices * (numVertices - 1));
        stats.put("density", density);

        return stats;
    }
}