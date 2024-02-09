package src.Question5;

import java.util.*;

public class NetworkDevice {
    public static List<Integer> findImpactedDevices(int[][] edges, int targetDevice) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int[] edge : edges) {
            int from = edge[0];
            int to = edge[1];
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
        }

        Set<Integer> visited = new HashSet<>();
        List<Integer> impactedDevices = new ArrayList<>();
        dfs(graph, targetDevice, visited, impactedDevices);
        impactedDevices.remove(Integer.valueOf(targetDevice));
        return impactedDevices;
    }

    private static void dfs(Map<Integer, List<Integer>> graph, int current, Set<Integer> visited, List<Integer> impactedDevices) {
        visited.add(current);
        for (int neighbor : graph.getOrDefault(current, Collections.emptyList())) {
            if (!visited.contains(neighbor)) {
                impactedDevices.add(neighbor);
                dfs(graph, neighbor, visited, impactedDevices);
            }
        }
    }

    public static void main(String[] args) {
        int[][] edges = {{0,1},{0,2},{1,3},{1,6},{2,4},{4,6},{4,5},{5,7}};
        int targetDevice = 4;
        List<Integer> impactedDevices = findImpactedDevices(edges, targetDevice);
        System.out.println("Impacted Device List: " + impactedDevices);
    }
}
