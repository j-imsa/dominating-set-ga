package com.genetic.dominatingset.util;

import com.genetic.dominatingset.model.Graph;

import java.util.Random;

/**
 * Utility class for generating various types of graphs for testing.
 *
 * Provides methods to create random graphs, special graph structures,
 * and predefined test cases.
 *
 * @author Iman Salehi
 * @version 1.0
 */
public class GraphGenerator {
    private final Random random;

    /**
     * Creates a new graph generator with default random seed.
     */
    public GraphGenerator() {
        this.random = new Random();
    }

    /**
     * Creates a new graph generator with specified seed.
     *
     * @param seed the random seed for reproducibility
     */
    public GraphGenerator(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Generates a random graph with specified number of vertices and edges.
     *
     * @param numVertices the number of vertices
     * @param numEdges the number of edges to add
     * @return a random graph
     */
    public Graph generateRandomGraph(int numVertices, int numEdges) {
        if (numVertices <= 0) {
            throw new IllegalArgumentException("Number of vertices must be positive");
        }

        int maxEdges = numVertices * (numVertices - 1) / 2;
        if (numEdges > maxEdges) {
            throw new IllegalArgumentException(
                    String.format("Too many edges. Maximum for %d vertices is %d",
                            numVertices, maxEdges));
        }

        Graph graph = new Graph(numVertices);
        int edgesAdded = 0;
        int attempts = 0;
        int maxAttempts = numEdges * 10;

        while (edgesAdded < numEdges && attempts < maxAttempts) {
            int u = random.nextInt(numVertices);
            int v = random.nextInt(numVertices);

            if (u != v && !graph.hasEdge(u, v)) {
                graph.addEdge(u, v);
                edgesAdded++;
            }

            attempts++;
        }

        return graph;
    }

    /**
     * Generates a random graph with specified density.
     *
     * @param numVertices the number of vertices
     * @param density the edge density (0.0 to 1.0)
     * @return a random graph
     */
    public Graph generateRandomGraphByDensity(int numVertices, double density) {
        if (density < 0.0 || density > 1.0) {
            throw new IllegalArgumentException("Density must be between 0.0 and 1.0");
        }

        int maxEdges = numVertices * (numVertices - 1) / 2;
        int numEdges = (int) (maxEdges * density);

        return generateRandomGraph(numVertices, numEdges);
    }

    /**
     * Generates a complete graph (every vertex connected to every other).
     *
     * Minimum dominating set size = 1
     *
     * @param numVertices the number of vertices
     * @return a complete graph
     */
    public Graph generateCompleteGraph(int numVertices) {
        Graph graph = new Graph(numVertices);

        for (int u = 0; u < numVertices; u++) {
            for (int v = u + 1; v < numVertices; v++) {
                graph.addEdge(u, v);
            }
        }

        return graph;
    }

    /**
     * Generates a path graph (vertices connected in a line).
     *
     * Example: 0 -- 1 -- 2 -- 3 -- 4
     * Minimum dominating set size ≈ n/3
     *
     * @param numVertices the number of vertices
     * @return a path graph
     */
    public Graph generatePathGraph(int numVertices) {
        Graph graph = new Graph(numVertices);

        for (int i = 0; i < numVertices - 1; i++) {
            graph.addEdge(i, i + 1);
        }

        return graph;
    }

    /**
     * Generates a cycle graph (vertices connected in a ring).
     *
     * Example: 0 -- 1 -- 2 -- 3 -- 4 -- 0
     * Minimum dominating set size ≈ n/3
     *
     * @param numVertices the number of vertices
     * @return a cycle graph
     */
    public Graph generateCycleGraph(int numVertices) {
        if (numVertices < 3) {
            throw new IllegalArgumentException("Cycle graph needs at least 3 vertices");
        }

        Graph graph = new Graph(numVertices);

        for (int i = 0; i < numVertices - 1; i++) {
            graph.addEdge(i, i + 1);
        }

        // Close the cycle
        graph.addEdge(numVertices - 1, 0);

        return graph;
    }

    /**
     * Generates a star graph (one center connected to all others).
     *
     * Example: Center 0 connected to 1, 2, 3, 4, ...
     * Minimum dominating set size = 1 (the center)
     *
     * @param numVertices the number of vertices
     * @return a star graph
     */
    public Graph generateStarGraph(int numVertices) {
        if (numVertices < 2) {
            throw new IllegalArgumentException("Star graph needs at least 2 vertices");
        }

        Graph graph = new Graph(numVertices);

        // Connect vertex 0 (center) to all others
        for (int i = 1; i < numVertices; i++) {
            graph.addEdge(0, i);
        }

        return graph;
    }

    /**
     * Generates a grid graph (2D lattice).
     *
     * Example (3x3):
     * 0 -- 1 -- 2
     * |    |    |
     * 3 -- 4 -- 5
     * |    |    |
     * 6 -- 7 -- 8
     *
     * @param rows the number of rows
     * @param cols the number of columns
     * @return a grid graph
     */
    public Graph generateGridGraph(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Rows and columns must be positive");
        }

        int numVertices = rows * cols;
        Graph graph = new Graph(numVertices);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int current = r * cols + c;

                // Connect to right neighbor
                if (c < cols - 1) {
                    graph.addEdge(current, current + 1);
                }

                // Connect to bottom neighbor
                if (r < rows - 1) {
                    graph.addEdge(current, current + cols);
                }
            }
        }

        return graph;
    }

    /**
     * Generates a connected random graph using a spanning tree approach.
     *
     * This ensures the graph is connected by first creating a spanning tree,
     * then adding random edges.
     *
     * @param numVertices the number of vertices
     * @param numEdges the total number of edges (must be >= n-1)
     * @return a connected random graph
     */
    public Graph generateConnectedRandomGraph(int numVertices, int numEdges) {
        if (numEdges < numVertices - 1) {
            throw new IllegalArgumentException(
                    "Need at least n-1 edges for a connected graph");
        }

        Graph graph = new Graph(numVertices);

        // Create spanning tree to ensure connectivity
        for (int i = 1; i < numVertices; i++) {
            int parent = random.nextInt(i);
            graph.addEdge(parent, i);
        }

        // Add remaining edges randomly
        int edgesToAdd = numEdges - (numVertices - 1);
        int edgesAdded = 0;
        int attempts = 0;
        int maxAttempts = edgesToAdd * 10;

        while (edgesAdded < edgesToAdd && attempts < maxAttempts) {
            int u = random.nextInt(numVertices);
            int v = random.nextInt(numVertices);

            if (u != v && !graph.hasEdge(u, v)) {
                graph.addEdge(u, v);
                edgesAdded++;
            }

            attempts++;
        }

        return graph;
    }

    /**
     * Generates a sample test graph with 10 vertices.
     *
     * This is a predefined graph for reproducible testing.
     *
     * @return a sample graph
     */
    public Graph generateSampleGraph10() {
        Graph graph = new Graph(10);

        // Create a graph structure similar to the report example
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 4);
        graph.addEdge(3, 5);
        graph.addEdge(4, 5);
        graph.addEdge(4, 6);
        graph.addEdge(5, 6);
        graph.addEdge(5, 7);
        graph.addEdge(6, 7);
        graph.addEdge(6, 8);
        graph.addEdge(7, 8);
        graph.addEdge(7, 9);
        graph.addEdge(8, 9);

        return graph;
    }

    /**
     * Generates a bipartite graph with two sets of vertices.
     *
     * @param set1Size size of first vertex set
     * @param set2Size size of second vertex set
     * @param edgeProbability probability of edge between sets
     * @return a bipartite graph
     */
    public Graph generateBipartiteGraph(int set1Size, int set2Size, double edgeProbability) {
        int numVertices = set1Size + set2Size;
        Graph graph = new Graph(numVertices);

        // Add edges between the two sets
        for (int u = 0; u < set1Size; u++) {
            for (int v = set1Size; v < numVertices; v++) {
                if (random.nextDouble() < edgeProbability) {
                    graph.addEdge(u, v);
                }
            }
        }

        return graph;
    }

    /**
     * Generates a custom graph from adjacency matrix.
     *
     * @param adjacencyMatrix the adjacency matrix (symmetric)
     * @return the graph
     */
    public Graph fromAdjacencyMatrix(int[][] adjacencyMatrix) {
        int n = adjacencyMatrix.length;
        Graph graph = new Graph(n);

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (adjacencyMatrix[i][j] == 1) {
                    graph.addEdge(i, j);
                }
            }
        }

        return graph;
    }
}