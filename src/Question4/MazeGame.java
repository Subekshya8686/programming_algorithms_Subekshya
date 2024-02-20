//You are given a 2D grid representing a maze in a virtual game world. The grid is of size m x n and consists of
//different types of cells:
//'P' represents an empty path where you can move freely. 'W' represents a wall that you cannot pass through. 'S'
//represents the starting point. Lowercase letters represent hidden keys. Uppercase letters represent locked doors.
//You start at the starting point 'S' and can move in any of the four cardinal directions (up, down, left, right) to
//adjacent cells. However, you cannot walk through walls ('W').
//As you explore the maze, you may come across hidden keys represented by lowercase letters. To unlock a door
//represented by an uppercase letter, you need to collect the corresponding key first. Once you have a key, you can
//pass through the corresponding locked door.
//For some 1 <= k <= 6, there is exactly one lowercase and one uppercase letter of the first k letters of the English
//alphabet in the maze. This means that there is exactly one key for each door, and one door for each key. The letters
//used to represent the keys and doors follow the English alphabet order.
//Your task is to find the minimum number of moves required to collect all the keys. If it is impossible to collect all
//the keys and reach the exit, return -1.
//Example:
//Input: grid = [ ["S","P","q","P","P"], ["W","W","W","P","W"], ["r","P","Q","P","R"]]
//Output: 8
//The goal is to Collect all key

package src.Question4;

import java.util.*;

public class MazeGame {
    private static final int[] dx = {0, 0, 1, -1}; // Directions: right, left, down, up
    private static final int[] dy = {1, -1, 0, 0};

    public static int minStepsToCollectAllKeys(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        int totalKeys = 0;
        int startX = -1, startY = -1;
        Map<Character, int[]> keyPositions = new HashMap<>();
        Set<Character> collectedKeys = new HashSet<>();

        // Initialize the keyPositions map and count the total number of keys
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                char cell = grid[i][j];
                if (cell >= 'a' && cell <= 'f') {
                    keyPositions.put(cell, new int[]{i, j});
                    totalKeys++;
                } else if (cell == 'S') {
                    startX = i;
                    startY = j;
                }
            }
        }

        // BFS to find the shortest path to collect all keys
        Queue<int[]> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.offer(new int[]{startX, startY, 0}); // {x, y, steps}
        visited.add(startX + "," + startY);

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];
            int steps = current[2];

            if (collectedKeys.size() == totalKeys) {
                return steps;
            }

            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];

                if (nx >= 0 && nx < m && ny >= 0 && ny < n && grid[nx][ny] != 'W') {
                    char nextCell = grid[nx][ny];

                    if (nextCell == 'P' || nextCell == 'S' || (nextCell >= 'a' && nextCell <= 'f') || collectedKeys.contains(Character.toLowerCase(nextCell))) {
                        String key = nx + "," + ny;
                        if (!visited.contains(key)) {
                            queue.offer(new int[]{nx, ny, steps + 1});
                            visited.add(key);
                        }
                    } else if (nextCell >= 'A' && nextCell <= 'F') {
                        char requiredKey = Character.toLowerCase(nextCell);
                        if (collectedKeys.contains(requiredKey)) {
                            String key = nx + "," + ny;
                            if (!visited.contains(key)) {
                                queue.offer(new int[]{nx, ny, steps + 1});
                                visited.add(key);
                            }
                        }
                    } else {
                        // Collecting the key
                        if (nextCell >= 'a' && nextCell <= 'f' && !collectedKeys.contains(nextCell)) {
                            collectedKeys.add(nextCell);
                            grid[nx][ny] = 'P'; // Update the grid
                            String key = nx + "," + ny;
                            if (!visited.contains(key)) {
                                queue.offer(new int[]{nx, ny, steps + 1});
                                visited.add(key);
                            }
                        }
                    }
                }
            }
        }

        return -1; // Cannot collect all keys
    }

    public static void main(String[] args) {
        char[][] grid = {
                {'S', 'P', 'q', 'P', 'P'},
                {'W', 'W', 'W', 'P', 'W'},
                {'r', 'P', 'Q', 'P', 'R'}
        };
        int minSteps = minStepsToCollectAllKeys(grid);
        System.out.println("Minimum steps to collect all keys: " + minSteps); // Output: 8
    }
}
