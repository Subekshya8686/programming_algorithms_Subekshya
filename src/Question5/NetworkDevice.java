package src.Question5;

import java.util.*;

public class NetworkDevice {
    // Method to find impacted devices given network connections and target device
    public static List<Integer> findImpactedDevices(int[][] edges, int targetDevice) {
        // Build the graph from the given edges
        Map<Integer, List<Integer>> graph = buildGraph(edges);
        // Set to keep track of visited devices during DFS traversal
        Set<Integer> visited = new HashSet<>();
        // List to store impacted devices
        List<Integer> impactedDevices = new ArrayList<>();

        // Perform DFS traversal to find impacted devices
        dfs(graph, targetDevice, visited, impactedDevices);
        // Remove the target device from the list of impacted devices
        impactedDevices.remove((Integer) targetDevice);

        return impactedDevices;
    }

    // Depth-first search (DFS) traversal to find impacted devices
    private static void dfs(Map<Integer, List<Integer>> graph, int device, Set<Integer> visited, List<Integer> impactedDevices) {
        // Mark the current device as visited and add it to the list of impacted devices
        visited.add(device);
        impactedDevices.add(device);
        // Get the list of connected devices for the current device
        List<Integer> connectedDevices = graph.getOrDefault(device, new ArrayList<>());

        // Explore each connected device recursively
        for (int connectedDevice : connectedDevices) {
            // If the connected device has not been visited yet, perform DFS on it
            if (!visited.contains(connectedDevice)) {
                dfs(graph, connectedDevice, visited, impactedDevices);
            }
        }
    }

    // Method to build a graph from the given edges
    private static Map<Integer, List<Integer>> buildGraph(int[][] edges) {
        Map<Integer, List<Integer>> graph = new HashMap<>();

        // Iterate through each edge and add it to the graph
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            // Add edge (u, v)
            graph.putIfAbsent(u, new ArrayList<>());
            graph.get(u).add(v);
            // Add edge (v, u) as well assuming undirected graph
            graph.putIfAbsent(v, new ArrayList<>());
            graph.get(v).add(u);
        }

        return graph;
    }

    // Main method to test the NetworkDevice class
    public static void main(String[] args) {
        int[][] edges = {{0,1},{0,2},{1,3},{1,6},{2,4},{4,6},{4,5},{5,7}};
        int targetDevice = 4;
        List<Integer> impactedDevices = findImpactedDevices(edges, targetDevice);
        System.out.println("Impacted Device List: " + impactedDevices);
    }
}
