package SPT; // When reviewing code you may have to change this line when downloading on your own machine

import java.io.*;
import java.util.*;

// Definition of the Heap class for efficient implementation of Dijkstra's Algorithm
class Heap {
    // Instance variables for the heap
    private int[] a; // Heap array
    public int[] hPos; // Positions of elements in the heap
    private int[] dist; // Distances of vertices from the source
    private int N; // Current size of the heap

    // Constructor for the Heap class
    public Heap(int maxSize, int[] _dist, int[] _hPos) {
        N = 0;
        a = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
    }

    // Method to check if the heap is empty
    public boolean isEmpty() {
        return N == 0;
    }

    // Method to perform sift-up operation in the heap
    public void siftUp(int k) {
        int v = a[k];
        while (k > 1 && dist[v] < dist[a[k / 2]]) {
            hPos[a[k / 2]] = k;
            a[k] = a[k / 2];
            k = k / 2;
        }
        a[k] = v;
        hPos[v] = k;
    }

    // Method to perform sift-down operation in the heap
    public void siftDown(int k) {
        int v, j;
        v = a[k];
        while (k * 2 <= N) {
            j = k * 2;
            if (j < N && dist[a[j]] > dist[a[j + 1]]) {
                ++j;
            }
            if (dist[v] <= dist[a[j]]) {
                break;
            }
            hPos[a[j]] = k;
            a[k] = a[j];
            k = j;
        }
        hPos[v] = k;
        a[k] = v;
    }

    // Method to insert a vertex into the heap
    public void insert(int x) {
        a[++N] = x;
        siftUp(N);
        System.out.println("Inserted " + toChar(x));
    }

    // Method to remove the minimum vertex from the heap
    public int remove() {
        int v = a[1];
        hPos[v] = 0;
        a[1] = a[N--];
        siftDown(1);
        a[N + 1] = 0;
        return v;
    }

    // Method to convert vertex number to corresponding character
    private char toChar(int u) {
        return (char) (u + 64);
    }
}

// Definition of the GraphMatrix class for representing a graph using an
// adjacency matrix
class GraphMatrix {
    // Instance variables for the graph
    private int V, E; // Number of vertices and edges
    private int[][] adjMatrix; // Adjacency matrix
    private ArrayList<ArrayList<Integer>> adjList; // Adjacency list

    // Constructor for the GraphMatrix class
    public GraphMatrix(String graphFile) throws IOException {
        try (FileReader fr = new FileReader(graphFile);
                BufferedReader reader = new BufferedReader(fr)) {

            String splits = " +";
            String line = reader.readLine();
            String[] parts = line.split(splits);

            V = Integer.parseInt(parts[0]); // Extracting number of vertices
            E = Integer.parseInt(parts[1]); // Extracting number of edges

            adjMatrix = new int[V + 1][V + 1]; // Initializing adjacency matrix
            adjList = new ArrayList<>(V + 1); // Initializing adjacency list

            for (int i = 0; i <= V; i++) {
                adjList.add(new ArrayList<>()); // Adding empty lists to adjacency list
            }

            // Reading edges from the file and populating adjacency matrix and list
            for (int e = 1; e <= E; ++e) {
                line = reader.readLine();
                parts = line.split(splits);
                int u = Integer.parseInt(parts[0]);
                int v = Integer.parseInt(parts[1]);
                int wgt = Integer.parseInt(parts[2]);

                adjMatrix[u][v] = wgt;
                adjMatrix[v][u] = wgt;

                // Adding edges to adjacency list
                adjList.get(u).add(v);
                adjList.get(v).add(u);
            }
        }
    }

    // Method to convert vertex number to corresponding character
    private char toChar(int u) {
        return (char) (u + 64);
    }

    // Method to display the adjacency matrix
    public void displayAdjMatrix() {
        System.out.println("Adjacency Matrix:");
        System.out.print("  ");
        for (int i = 1; i <= V; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 1; i <= V; i++) {
            System.out.print(i + " ");
            for (int j = 1; j <= V; j++) {
                System.out.print(adjMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Method to perform Dijkstra's algorithm to find shortest paths from a source
    // vertex
    public void Dijkstra(int s) {
        boolean[] inTree = new boolean[V + 1];
        int[] parent = new int[V + 1];
        int[] dist = new int[V + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);

        // Creating heap for efficient selection of minimum distance vertex
        Heap h = new Heap(V, dist, new int[V + 1]);

        dist[s] = 0; // Distance to source vertex is 0
        h.insert(s); // Inserting source vertex into the heap

        while (!h.isEmpty()) {
            int v = h.remove(); // Removing minimum distance vertex from the heap
            inTree[v] = true; // Marking vertex as in tree

            // Iterating through neighbors of the current vertex
            for (int neighbor : adjList.get(v)) {
                // If neighbor is not in tree and the distance through current vertex is shorter
                if (!inTree[neighbor] && dist[v] + adjMatrix[v][neighbor] < dist[neighbor]) {
                    dist[neighbor] = dist[v] + adjMatrix[v][neighbor]; // Updating distance
                    parent[neighbor] = v; // Updating parent
                    h.insert(neighbor); // Inserting or updating neighbor in the heap

                    // Printing heap contents, distance array, and parent array for visualization
                    System.out.println("Heap Contents:");
                    for (int i = 1; i <= V; i++) {
                        System.out.println("Node: " + i + ", Distance: " + dist[i]);
                    }

                    System.out.println("Dist Array:");
                    for (int i = 1; i <= V; i++) {
                        System.out.println(toChar(i) + " -> " + dist[i]);
                    }

                    System.out.println("Parent Array:");
                    for (int i = 1; i <= V; i++) {
                        System.out.println(toChar(i) + " -> " + toChar(parent[i]));
                    }

                    System.out.println("\n");
                }
            }
        }

        // Displaying the shortest paths from the source vertex
        System.out.println("\nDijkstra's SPT:");
        for (int i = 1; i <= V; i++) {
            if (i != s) {
                System.out.println("Shortest path from " + toChar(s) + " to " + toChar(i) + " is " + dist[i]);
            }
        }
    }
}

// Main class for executing the program
public class SPT {
    // Main method for execution
    public static void main(String[] args) throws IOException {
        try (Scanner sc = new Scanner(System.in)) {
            String fname; // Filename for graph definition
            System.out.print("\nInput name of file you want to use : ");
            fname = sc.nextLine(); // Taking filename input from user

            System.out.print("\nEnter the number you want to start at: ");
            int s = sc.nextInt(); // Taking starting vertex input from user

            GraphMatrix g = new GraphMatrix(fname); // Creating graph object from the input file

            g.displayAdjMatrix(); // Displaying the adjacency matrix
            System.out.println();

            g.Dijkstra(s); // Performing Dijkstra's algorithm
        } catch (IOException e) {
            System.err.println("Error: couldn't read file: " + e.getMessage());
        }
    }
}
