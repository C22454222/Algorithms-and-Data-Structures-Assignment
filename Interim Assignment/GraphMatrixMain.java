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

class GraphMatrix {
   private int V, E;
   private int[][] adjMatrix;
   private int[] visited;
   private int id;
   
   public GraphMatrix(String graphFile) throws IOException {
       try (FileReader fr = new FileReader(graphFile);
            BufferedReader reader = new BufferedReader(fr)) {
   
           String splits = " +";  // multiple whitespace as delimiter
           String line = reader.readLine();
           String[] parts = line.split(splits);
           System.out.println("Parts[] = " + parts[0] + " " + parts[1]);
   
           V = Integer.parseInt(parts[0]);
           E = Integer.parseInt(parts[1]);
   
           adjMatrix = new int[V + 1][V + 1];
   
           System.out.println("Reading edges from text file");
           for (int e = 1; e <= E; ++e) {
               line = reader.readLine();
               parts = line.split(splits);
               int u = Integer.parseInt(parts[0]);
               int v = Integer.parseInt(parts[1]);
               int wgt = Integer.parseInt(parts[2]);
   
               System.out.println("Edge " + toChar(u) + "--(" + wgt + ")--" + toChar(v));
   
               adjMatrix[u][v] = wgt;
               adjMatrix[v][u] = wgt;
           }
       }
   }
  
   private char toChar(int u) {  
       return (char)(u + 64);
   }
   
   public void display() {
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
   
   public void DF(int prev, int s) {
       id = 0;
       visited = new int[V + 1];
       System.out.println("");
   
       for (int j = 1; j <= V; j++) 
       {
           visited[j] = 0;
       }
       dfVisit(prev, s);
   }
   
   private void dfVisit(int prev, int v) {
       visited[v] = ++id;
       System.out.println("Visiting node [" + toChar(v) + "] from node [" + toChar(prev) + "]");
       for (int i = 1; i <= V; i++) {
           if (adjMatrix[v][i] != 0 && visited[i] == 0) {
               dfVisit(v, i);
           }
       }
   }

   public void BF(int s) {
       int id = 0;
       Queue<Integer> q = new LinkedList<Integer>();
       System.out.println();
       System.out.println("Breadth First Traversal:");

       visited = new int[V + 1];
       for (int i = 1; i <= V; i++) {
           visited[i] = 0;
       }

       q.add(s);
       visited[s] = ++id;

       while (!q.isEmpty()) {
           int v = q.poll();
           System.out.println("Currently visiting [" + toChar(v) + "]");
           for (int i = 1; i <= V; i++) {
               if (adjMatrix[v][i] != 0 && visited[i] == 0) {
                   q.add(i);
                   visited[i] = ++id;
               }
           }
       }
       System.out.print("\n\n");
   }
}

public class GraphMatrixMain {
   public static void main(String[] args) throws IOException {
       try (Scanner sc = new Scanner(System.in)) {
           String fname;
           System.out.print("\nInput name of file with graph definition: ");
           fname = sc.nextLine();   
           
           System.out.print("\nInput the number of the vertex you want to start at: ");
           int s = sc.nextInt();

           GraphMatrix g = new GraphMatrix(fname);
          
           g.display();
           
           System.out.println();             
           
           System.out.print("Depth first using recursion:");

           g.DF(0, s);
           
           System.out.print("Breadth first:");
           
           g.BF(s);
       }
   }
}