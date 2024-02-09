package Question3;

import java.util.*;

public class Kruskal {

    public static class Edge {
        int s;
        int d;
        int w;

        Edge(int s, int d, int w) {
            this.s = s;
            this.d = d;
            this.w = w;
        }
    }

    int v;
    int graph[][];
    ArrayList<Edge> edges;

    Kruskal(int v){
        this.v = v;
        graph = new int[v][v];
        edges = new ArrayList<>();
    }

    void addEdge(int source, int destination, int w){
        graph[source][destination] = w;
        graph[destination][source] = w;
        edges.add(new Edge(source, destination, w));
    }

    void mst() {
        int parent[] = new int[v];
        int size[] = new int[v];
        for (int i = 0; i < v; i++) {
            parent[i] = -1;
        }
        int mstgraph[][] = new int[v][v];

        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.w));
        pq.addAll(edges);

        int edgeTaken = 0;
        while (edgeTaken < v - 1 && !pq.isEmpty()) {
            Edge e = pq.poll();
            if (!isCycleDetected(e.s, e.d, parent)) {
                mstgraph[e.s][e.d] = e.w;
                mstgraph[e.d][e.s] = e.w;
                edgeTaken++;
                union(find(e.s, parent), find(e.d, parent), size, parent);
            }
        }

        // Print mst graph by traversing mstgraph
        System.out.println("Minimum Spanning Tree:");
        for (int i = 0; i < v; i++) {
            for (int j = i + 1; j < v; j++) {
                if (mstgraph[i][j] != 0) {
                    System.out.println(i + " - " + j + ": " + mstgraph[i][j]);
                }
            }
        }
    }

    boolean isCycleDetected(int u, int v, int[] parent){
        return find(u, parent) == find(v, parent);
    }

    int find(int vertex, int[] parent){
        if (parent[vertex] == -1) {
            return vertex;
        }
        return parent[vertex] = find(parent[vertex], parent);
    }

    void union(int u_abs, int v_abs, int size[], int parent[]){
        if (size[u_abs] > size[v_abs]) {
            parent[v_abs] = u_abs;
        } else if (size[u_abs] < size[v_abs]) {
            parent[u_abs] = v_abs;
        } else {
            parent[u_abs] = v_abs;
            size[v_abs]++;
        }
    }

    public static void main(String[] args) {
        Kruskal adj = new Kruskal(6);
        adj.addEdge(0, 1, 4);
        adj.addEdge(0, 3, 2);
        adj.addEdge(1, 2, 20);
        adj.addEdge(2, 5, 5);
        adj.addEdge(0, 5, 100);
        adj.addEdge(3, 1, 1);
        adj.addEdge(3, 4, 5);
        adj.addEdge(4, 5, 5);

        adj.mst(); // Call the MST method
    }
}
