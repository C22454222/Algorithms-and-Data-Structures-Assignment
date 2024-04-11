// Simple weighted graph representation 
// Uses an Adjacency Linked Lists, suitable for sparse graphs
import java.io.*;
import java.util.Queue;
import java.util.Scanner;
import java.util.LinkedList;

// Heap Code for efficient implementation of Prim's Algorithm
 class Heap 
 {
    private int[] a; // heap array
    private int[] hPos; // hPos[h[k]] == k
    private int[] dist; // dist[v] = priority of v
    private int N; // heap size

    // The heap constructor gets passed from the Graph:
    // 1. maximum heap size
    // 2. reference to the dist[] array
    // 3. reference to the hPos[] array
    public Heap(int maxSize, int[] _dist, int[] _hPos) 
    {
        N = 0;
        a = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
    }

    public boolean isEmpty() 
    {
        return N == 0;
    }

    public void siftUp(int k) 
    {
        // k = last element in list
        int v = a[k];

        // code yourself
        // must use hPos[] and dist[] arrays

        while (dist[v] < dist[a[k / 2]]) 
        {
            hPos[a[k]] = k / 2;
            a[k] = a[k / 2];
            k = k / 2;
        }
        a[k] = v;
        hPos[v] = k;
    }

    public void siftDown(int k) 
    {
        int v, j;
        v = a[k];

        // code yourself
        // must use hPos[] and dist[] arrays
        while (k * 2 < N) 
        {
            j = k * 2;
            if (j < N && dist[a[j]] > dist[a[j + 1]]) 
            {
                ++j;
            }
            if (dist[v] <= dist[a[j]]) 
            {
                break;
            }

            hPos[a[k]] = j;
            a[k] = a[j];
            k = j;
        }
        hPos[v] = k;
        a[k] = v;
    }

    public void insert(int x) 
    {
        System.out.println("Inserting: " + x);
        a[++N] = x;
        siftUp(N);
    }

    public int remove() 
    {
        int v = a[1];
        System.out.println("Removing: " + v);
        hPos[v] = 0;

        a[1] = a[N--];
        siftDown(1);

        a[N + 1] = 0;

        return v;
    }
}

class Graph 
{
    class Node 
    {
        public int vert; // Vertices
        public int wgt; // Weight
        public Node next; // Next node
    }

    // V = number of vertices
    // E = number of edges
    // adj[] is the adjacency lists array
    private int V, E;
    private Node[] adj;
    private Node z;

    // used for traversing graph
    private int[] visited;
    private int id;

    // default constructor
    public Graph(String graphFile) throws IOException 
    {
        int u, v;
        int e, wgt;
        Node t, w;

        FileReader fr = new FileReader(graphFile);
        BufferedReader reader = new BufferedReader(fr);

        try 
        {
            String splits = " +"; // multiple whitespace as delimiter
            String line = reader.readLine();
            String[] parts = line.split(splits);
            System.out.println("Parts[] = " + parts[0] + " " + parts[1]);

            V = Integer.parseInt(parts[0]);
            E = Integer.parseInt(parts[1]);

            // create sentinel node
            z = new Node();
            z.next = z;

            // create adjacency lists, initialised to sentinel node z
            adj = new Node[V + 1];
            for (v = 1; v <= V; ++v)
                adj[v] = z;

            // read the edges
            System.out.println("Reading edges from text file");
            for (e = 1; e <= E; ++e) 
            {
                line = reader.readLine();
                parts = line.split(splits);
                u = Integer.parseInt(parts[0]);
                v = Integer.parseInt(parts[1]);
                wgt = Integer.parseInt(parts[2]);

                System.out.println("Edge " + toChar(u) + "--(" + wgt + ")--" + toChar(v));

                // write code to put edge into adjacency matrix
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
        } 
        finally 
        {
            // Close the reader in the finally block to ensure it gets closed
            if (reader != null) 
            {
                reader.close();
            }
        }
    }

    // convert vertex into char for pretty printing
    private char toChar(int u) 
    {
        return (char) (u + 64);
    }

    // method to display the graph representation
    public void display() 
    {
        int v;
        Node n;

        for (v = 1; v <= V; ++v) 
        {
            System.out.print("\nadj[" + toChar(v) + "] ->");
            for (n = adj[v]; n != z; n = n.next)
                System.out.print(" |" + toChar(n.vert) + " | " + n.wgt + "| ->");
        }
        System.out.println("");
    }

    // Depth First Search
    public void DF(int s) 
    {
        id = 0;
        visited = new int[V + 1];
        System.out.println("");

        for (int j = 1; j <= V; j++) 
        {
            visited[j] = 0;
        }
        dfVisit(0, s);
    }

    private void dfVisit(int prev, int v) 
    {
        Node n = new Node();
        n = adj[v];
        visited[v] = ++id;
        System.out.println("Visiting node [" + toChar(v) + "] from node [" + toChar(prev) + "]");
        
        while (n.next != n) 
        {
            if (visited[n.vert] == 0) 
            {
                dfVisit(v, n.vert); // recursively call the next vertex
            }
            n = n.next;
        }
    }

    // Breadth First Search
    public void BF(int s) 
    {
        int id = 0;
        Node n;
        Queue<Integer> q = new LinkedList<Integer>();
        System.out.println();
        System.out.println("Breadth First Traversal:");

        // initialise visited
        for (int i = 0; i <= V; i++) 
        {
            visited[i] = 0;
        }
        q.add(s);

        while (!(q.isEmpty())) 
        {
            int v = q.poll();
            if (visited[v] == 0) 
            {
                n = adj[v];
                visited[v] = ++id;
                System.out.println("Currently visiting [" + toChar(v) + "]");
                while (n.next != n) 
                {
                    if (visited[n.vert] == 0) 
                    {
                        q.add(n.vert);
                    }
                    n = n.next;
                }
            }
        }
    }
}

public class GraphLists 
{
    public static void main(String[] args) throws IOException 
    {
        Scanner sc = new Scanner(System.in);
        String fname;
        System.out.print("\nInput name of file with graph definition: ");
        fname = sc.nextLine();

        System.out.print("\nInput the number of the vertex you want to start at: ");
        int s = sc.nextInt();

        Graph g = new Graph(fname);

        g.display();

        System.out.println();

        System.out.print("Depth first search using recursion:");
       
        g.DF(s);

        System.out.println();

        System.out.print("Breadth first search using a queue:");

        g.BF(s); 
        
        sc.close();
    }
}
