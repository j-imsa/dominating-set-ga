# Dominating Set Problem - Genetic Algorithm Solution

A Java implementation of a Genetic Algorithm to solve the Minimum Dominating Set Problem in graph theory.

## 📋 Overview

This project implements a genetic algorithm to find minimum dominating sets in undirected graphs. A dominating set is a subset of vertices where every vertex not in the set has at least one neighbor in the set. Finding the minimum dominating set is an NP-complete problem with applications in wireless networks, social networks, and facility location problems.

- **Algorithm:** Genetic Algorithm (GA)  
- **Problem:** Minimum Dominating Set (MDS)  
- **Language:** Java 21  
- **Author:** Iman Salehi
- **Date:** November 2025

## 📚 Documentation

For detailed information about the problem, algorithm design, implementation details, and experimental results, please refer to the **comprehensive LaTeX report**:

📄 **[Report.pdf](Report.pdf)** - Complete technical documentation

The report includes:
- Problem definition and mathematical formulation
- Genetic algorithm approach and components
- Encoding scheme and fitness function design
- Selection, crossover, and mutation operators
- Experimental results and performance analysis
- References to academic literature

## 🎯 Problem Definition

Given an undirected graph `G = (V, E)`:
- **Input:** A graph with `n` vertices and `m` edges
- **Output:** A minimum subset `D ⊆ V` such that every vertex is either in `D` or adjacent to at least one vertex in `D`
- **Objective:** Minimize `|D|` (the size of the dominating set)

**Example:**

Graph:  
```
1 --- 2 --- 3
|     |     |
4 --- 5 --- 6
```
One possible dominating set: {2, 5} (size = 2)

## 🧬 Genetic Algorithm Parameters

| Parameter | Value | Description |
|-----------|-------|-------------|
| Population Size | 100 | Number of individuals in each generation |
| Crossover Rate | 0.4 | Fraction of population replaced by offspring |
| Mutation Rate | 0.1 | Probability of bit-flip mutation |
| Max Iterations | 50 | Number of generations |
| Elitism | Yes | Best 60% of population preserved |

## 🏗️ Project Structure
```
dominating-set-ga/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── genetic/
│                   └── dominatingset/
│                       ├── model/                      # Data structures
│                       │   ├── Graph.java
│                       │   ├── Individual.java
│                       │   └── Population.java
│                       ├── algorithm/                  # GA components
│                       │   ├── GeneticAlgorithm.java
│                       │   ├── FitnessEvaluator.java
│                       │   ├── SelectionOperator.java
│                       │   ├── CrossoverOperator.java
│                       │   └── MutationOperator.java
│                       ├── util/                       # Helper classes
│                       │   ├── GraphGenerator.java
│                       │   └── ResultLogger.java
│                       └── Main.java                   # Entry point
├── results/                                            # Output files
│   ├── output.txt
│   └── output.csv
├── Report.pdf                                          # LaTeX report (compiled)
├── references.bib                                      # Bibliography
└── README.md                                           # This file
```
## 🚀 Quick Start

### Prerequisites

- Java 21 or higher
- No external dependencies required (pure Java implementation)

### Compilation

```bash
    git clone https://github.com/imansalehi/dominating-set-ga.git
    cd dominating-set-ga
    
    # Compile all Java files
    javac -d bin src/main/java/com/genetic/dominatingset/**/*.java
    
    # Or use your IDE (IntelliJ IDEA, Eclipse, VS Code)
```

### Running

```shell script
    # Run the main program
    java -cp bin com.genetic.dominatingset.Main
    
    # Or run from your IDE
```


### Expected Output

```
============================================
DOMINATING SET PROBLEM - GENETIC ALGORITHM
============================================


Initializing graph with 10 vertices and 15 edges...
Graph structure:
  Vertex 0: [1, 2]
  Vertex 1: [0, 2, 3]
  ...

Initializing population of 100 individuals...
Starting genetic algorithm...

Generation 1  | Best Fitness: -7 | Best Size: 7
Generation 5  | Best Fitness: -6 | Best Size: 6
Generation 10 | Best Fitness: -5 | Best Size: 5
...
Generation 23 | Best Fitness: -4 | Best Size: 4 ✓ (converged)

============================================
RESULTS
============================================
Best dominating set found: [0, 1, 0, 1, 0, 1, 0, 1, 0, 0]
Vertices in set: {2, 4, 6, 8}
Size: 4
Valid: Yes
Iterations to converge: 23

All vertices are dominated ✓
```


## 🧪 Test Cases

The implementation includes test cases for various graph structures:

1. **Small Graph (5-10 vertices):** Quick testing and validation
2. **Medium Graph (20-30 vertices):** Performance evaluation
3. **Large Graph (50-100 vertices):** Scalability testing
4. **Special Cases:**
    - Complete graph (optimal solution = 1)
    - Star graph (optimal solution = 1)
    - Path graph (optimal solution ≈ n/3)
    - Cycle graph (optimal solution ≈ n/3)

## 📊 Features

- ✅ Binary encoding for solution representation
- ✅ Fitness function with constraint handling
- ✅ Elitist selection strategy
- ✅ Single-point crossover operator
- ✅ Bit-flip mutation operator
- ✅ Convergence tracking and visualization
- ✅ Result logging and statistics
- ✅ Support for custom graph input

## 🔬 Algorithm Components

### 1. Encoding
Each solution is represented as a binary chromosome:
```
[1, 0, 1, 0, 0] → vertices {1, 3} are in the dominating set
```


### 2. Fitness Function
```
fitness(x) = -|D| if valid, else -∞
where |D| is the size of the dominating set
```


### 3. Selection
Elitist selection: Keep the best 60% of individuals as parents.

### 4. Crossover
Single-point crossover at the middle of the chromosome.

### 5. Mutation
Bit-flip mutation with probability 0.1 per offspring.

## 📈 Performance

Typical results on a medium-sized graph (30 vertices):

- **Average convergence time:** 15-25 generations
- **Solution quality:** Within 10-20% of optimal (for known cases)
- **Execution time:** < 1 second
- **Success rate:** > 95% (finds valid dominating set)

## 🛠️ Customization

### Modify Algorithm Parameters

Edit `com.genetic.dominatingset.Main.java`:

```java
// Adjust these parameters
int populationSize = 100;
double crossoverRate = 0.4;
double mutationRate = 0.1;
int maxIterations = 50;
```


### Use Custom Graph

```java
// Create your own graph
Graph graph = new Graph(5);
graph.addEdge(0, 1);
graph.addEdge(1, 2);
graph.addEdge(2, 3);
graph.addEdge(3, 4);
```


### Change Fitness Function

Edit `FitnessEvaluator.java` to implement different constraint handling strategies.

## 📖 References

For academic references and detailed explanations, please see the **Bibliography** section in [Report.pdf](Report.pdf).

Key references include:
- Goldberg, D. E. (1989). *Genetic Algorithms in Search, Optimization and Machine Learning*
- Haynes, T. W. et al. (1998). *Fundamentals of Domination in Graphs*
- Garey & Johnson (1979). *Computers and Intractability*

## 🤝 Contributing

This is an academic project for educational purposes. However, improvements are welcome:

- Bug fixes
- Performance optimizations
- Additional test cases
- Documentation improvements

## 📝 License

This project is created for academic purposes as part of a university course assignment.

## 📧 Contact

- **Author:** Iman Salehi
- **Student ID:** 40421660007
- **Email:** iman.salehi@grad.kashanu.ac.ir
- **University:** Kashan University
- **Course:** Advanced Algorithms

---

## 🎓 Academic Integrity

This implementation is original work created for educational purposes. The algorithms and techniques are based on established literature in evolutionary computation and graph theory.

**Last Updated:** November 12, 2025

