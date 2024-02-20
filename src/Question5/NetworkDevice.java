// Assume you were hired to create an application for an ISP, and there are n network devices, such as routers,
//that are linked together to provide internet access to users. You are given a 2D array that represents network
//connections between these network devices. write an algorithm to return impacted network devices, If there is
//a power outage on a certain device, these impacted device list assist you notify linked consumers that there is a
//power outage and it will take some time to rectify an issue.
//Input: edges= {{0,1},{0,2},{1,3},{1,6},{2,4},{4,6},{4,5},{5,7}}
//Target Device (On which power Failure occurred): 4
//Output (Impacted Device List) = {5,7}

package src.Question5;

import java.util.*;

public class NetworkDevice {
    public static List<Integer> findImpactedDevices(int[][] edges, int targetDevice) {
        Map<Integer, List<Integer>> graph = buildGraph(edges);
        Set<Integer> visited = new HashSet<>();
        List<Integer> impactedDevices = new ArrayList<>();

        dfs(graph, targetDevice, visited, impactedDevices);
        impactedDevices.remove((Integer) targetDevice);

        return impactedDevices;
    }

    private static void dfs(Map<Integer, List<Integer>> graph, int device, Set<Integer> visited, List<Integer> impactedDevices) {
        visited.add(device);
        impactedDevices.add(device);
        List<Integer> connectedDevices = graph.getOrDefault(device, new ArrayList<>());

        for (int connectedDevice : connectedDevices) {
            if (!visited.contains(connectedDevice)) {
                dfs(graph, connectedDevice, visited, impactedDevices);
            }
        }
    }

    private static Map<Integer, List<Integer>> buildGraph(int[][] edges) {
        Map<Integer, List<Integer>> graph = new HashMap<>();

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph.putIfAbsent(u, new ArrayList<>());
            graph.putIfAbsent(v, new ArrayList<>());
            graph.get(u).add(v);
        }

        return graph;
    }

    public static void main(String[] args) {
        int[][] edges = {{0,1},{0,2},{1,3},{1,6},{2,4},{4,6},{4,5},{5,7}};
        int targetDevice = 4;
        List<Integer> impactedDevices = findImpactedDevices(edges, targetDevice);
        System.out.println("Impacted Device List: " + impactedDevices);
    }
}
