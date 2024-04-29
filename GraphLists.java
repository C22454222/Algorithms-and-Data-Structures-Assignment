
// Importing necessary libraries for file reading, data structures, and input/output
import java.io.*;
import java.util.Queue;
import java.util.Scanner;
import java.util.LinkedList;

// Definition of the Heap class for efficient implementation of Prim's Algorithm
class Heap {
    // Declaring necessary instance variables for the heap
    private int[] a; // heap array
    private int[] hPos; // hPos[h[k]] == k
    private int[] dist; // dist[v] = priority of v
    private int N; // heap size

    // Constructor for the Heap class
    // Takes maximum heap size, reference to the dist[] array, and reference to the
    // hPos[] array
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
        // Get the vertex at index k
        int v = a[k];

        // Perform sift-up operation until the vertex's priority is higher than its
        // parent's priority
        while (dist[v] < dist[a[k / 2]]) {
            hPos[a[k]] = k / 2;
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

        // Perform sift-down operation until the vertex's priority is lower than its
        // children's priorities
        while (k * 2 < N) {
            j = k * 2;
            if (j < N && dist[a[j]] > dist[a[j + 1]]) {
                ++j;
            }
            if (dist[v] <= dist[a[j]]) {
                break;
            }

            hPos[a[k]] = j;
            a[k] = a[j];
            k = j;
        }
        hPos[v] = k;
        a[k] = v;
    }

    // Method to insert a vertex into the heap
    public void insert(int x) {
        System.out.println("Inserting: " + toChar(x));
        a[++N] = x;
        siftUp(N);
    }

    // Method to remove the minimum vertex from the heap
    public int remove() {
        int v = a[1];
        System.out.println("Removing: " + toChar(v));
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

// Definition of the Graph class
class Graph {
    // Definition of the Node class for adjacency lists
    class Node {
        public int vert; // Vertex
        public int wgt; // Weight
        public Node next; // Next node
    }

    // Instance variables for the graph
    private int V, E; // Number of vertices and edges
    private Node[] adj; // Adjacency lists array
    private Node z; // Sentinel node
    private int[] mst; // Minimum Spanning Tree array

    // Variables for traversing the graph
    private int[] visited; // Array to track visited vertices
    private int id; // Identifier for vertices

    // Constructor for the Graph class
    public Graph(String graphFile) throws IOException {
        int u, v; // Vertices
        int e, wgt; // Edges and weights
        Node t, w; // Temporary nodes for edge creation

        // Reading the graph file
        FileReader fr = new FileReader(graphFile);
        BufferedReader reader = new BufferedReader(fr);

        try {
            String splits = " +"; // Delimiter for splitting lines
            String line = reader.readLine();
            String[] parts = line.split(splits);
            System.out.println("Parts[] = " + parts[0] + " " + parts[1]);

            V = Integer.parseInt(parts[0]); // Extracting number of vertices
            E = Integer.parseInt(parts[1]); // Extracting number of edges

            // Creating sentinel node
            z = new Node();
            z.next = z;

            // Creating adjacency lists, initialized to sentinel node z
            adj = new Node[V + 1];
            for (v = 1; v <= V; ++v)
                adj[v] = z;

            // Reading the edges from the file
            System.out.println("Reading edges from text file");
            for (e = 1; e <= E; ++e) {
                line = reader.readLine();
                parts = line.split(splits);
                u = Integer.parseInt(parts[0]);
                v = Integer.parseInt(parts[1]);
                wgt = Integer.parseInt(parts[2]);

                System.out.println("Edge " + toChar(u) + "--(" + wgt + ")--" + toChar(v));

                // Creating nodes for the edge and adding them to the adjacency lists
                t = new Node();
                t.vert = v;
                t.wgt = wgt;
                t.next = adj[u];
                adj[u] = t;

                w = new Node();
                w.vert = u;
                w.wgt = wgt;
                w.next = adj[v];
                adj[v] = w;

            }
        } finally {
            // Closing the reader in the finally block to ensure it gets closed
            if (reader != null) {
                reader.close();
            }
        }
    }

    // Method to convert vertex number to corresponding character
    private char toChar(int u) {
        return (char) (u + 64);
    }

    // Method to display the graph representation
    public void display() {
        int v;
        Node n;
        System.out.println("Vertex        Edge Weight");
        for (v = 1; v <= V; ++v) {
            System.out.print("\nadj[" + toChar(v) + "] ->");
            for (n = adj[v]; n != z; n = n.next)
                System.out.print(" |" + toChar(n.vert) + " | " + n.wgt + "| ->");
        }
        System.out.println("");
    }

    // Prim's Minimum Spanning Tree Algorithm
    public void MST_Prim(int s) {
        int v;
        int wgt_sum = 0; // Total weight of MST
        int[] dist, parent, hPos; // Arrays for distances, parent vertices, and heap positions
        Node t;

        // Initializing arrays
        dist = new int[V + 1];
        parent = new int[V + 1];
        hPos = new int[V + 1];

        for (int i = 1; i <= V; i++) {
            dist[i] = Integer.MAX_VALUE;
            parent[i] = 0;
            hPos[i] = 0;
        }

        parent[s] = s;
        dist[s] = 0;
        dist[0] = 0;

        // Creating and initializing heap
        Heap pq = new Heap(V, dist, hPos);
        pq.insert(s);

        // Main loop of Prim's algorithm
        while (!(pq.isEmpty())) {
            v = pq.remove();

            // Marking vertex as visited by negating the distance
            dist[v] = -dist[v];
            System.out.println("Visited: " + toChar(v));
            System.out.println("\n");

            t = adj[v];

            // Iterating through adjacent vertices
            while (t.next != t) {
                // If the vertex hasn't been visited and its distance is less than the current
                // distance
                if (t.wgt < dist[t.vert] && dist[t.vert] > 0) {
                    dist[t.vert] = t.wgt;
                    parent[t.vert] = v;

                    // Inserting or updating the vertex in the heap
                    if (hPos[t.vert] == 0) {
                        pq.insert(t.vert);
                    } else {
                        pq.siftUp(hPos[t.vert]);
                    }
                }

                // Moving to the next adjacent vertex
                t = t.next;
            }
        }

        // Calculating total weight of MST
        for (int i = 0; i <= V; i++) {
            wgt_sum += dist[i];
        }

        // Ensuring result is positive integer for printing the final MST weight
        wgt_sum *= -1;

        // Printing final MST Weight
        System.out.print("\n\n\nTOTAL MST WEIGHT ->> " + wgt_sum + "\n\n");

        mst = parent;
    }

    // Method to display the MST
    public void showMST() {
        // Displaying MST Parent Array
        System.out.print("\n\n\nMinimum Spanning tree parent array ->>\n");

        // Traversing the MST and converting the integer values to alphabetical
        // characters
        for (int v = 1; v <= V; ++v)

            if (v == mst[v]) {
                // Demarking the starting node with an @ symbol
                System.out.println(toChar(v) + " -> @");
            } else {
                System.out.println(toChar(v) + " -> " + toChar(mst[v]));
            }

        // Newline for formatting
        System.out.print("\n\n");
    }

    // Depth First Traversal
    public void DF(int s) {
        id = 0;
        visited = new int[V + 1];
        System.out.println("");

        for (int j = 1; j <= V; j++) {
            visited[j] = 0;
        }
        dfVisit(0, s);
    }

    // Helper method for Depth First Traversal
    private void dfVisit(int prev, int v) {
        Node n = new Node();
        n = adj[v];
        visited[v] = ++id;
        System.out.println("Visiting node [" + toChar(v) + "] from node [" + toChar(prev) + "]");
        while (n.next != n) {
            if (visited[n.vert] == 0) {
                dfVisit(v, n.vert); // Recursively call the next vertex
            }
            n = n.next;
        }
    }

    // Breadth First Traversal
    public void BF(int s) {
        int id = 0;
        Node n;
        Queue<Integer> q = new LinkedList<Integer>();
        System.out.println();
        System.out.println("Breadth First Traversal:");

        // Initializing visited array
        for (int i = 0; i <= V; i++) {
            visited[i] = 0;
        }
        q.add(s);
        while (!(q.isEmpty())) {
            int v = q.poll();
            if (visited[v] == 0) {
                n = adj[v];
                visited[v] = ++id;
                System.out.println("Currently visiting [" + toChar(v) + "]");
                while (n.next != n) {
                    if (visited[n.vert] == 0) {
                        q.add(n.vert);
                    }
                    n = n.next;
                }
            }
        }

        // Newlines for formatting
        System.out.print("\n\n");
    }
}

// Main class for executing the program
public class GraphLists {
    // Main method for execution
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String fname; // Filename for graph definition
        System.out.print("\nInput name of file with graph definition: ");
        fname = sc.nextLine(); // Taking filename input from user

        System.out.print("\nInput the number of the vertex you want to start at: ");
        int s = sc.nextInt(); // Taking starting vertex input from user

        Graph g = new Graph(fname); // Creating graph object from the input file

        g.display(); // Displaying the graph

        System.out.println();

        System.out.print("Depth first using recursion:");

        g.DF(s); // Performing Depth First Traversal

        System.out.println("MST using Prim's Algorithm:\n");

        g.MST_Prim(s); // Finding Minimum Spanning Tree using Prim's Algorithm

        g.showMST(); // Displaying the MST

        System.out.print("Breadth first:");

        g.BF(s); // Performing Breadth First Traversal

        sc.close(); // Closing scanner object
    }
}
