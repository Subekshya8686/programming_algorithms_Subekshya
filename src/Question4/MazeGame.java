package src.Question4;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class MazeGame {
    static class State {
        int row;
        int col;
        int moves;
        Set<Character> keys;

        public State(int row, int col, int moves, Set<Character> keys) {
            this.row = row;
            this.col = col;
            this.moves = moves;
            this.keys = keys;
        }
    }

    public static int minMovesToCollectKeys(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        int numKeys = 0;
        for (char[] row : grid) {
            for (char cell : row) {
                if (Character.isLowerCase(cell)) {
                    numKeys++;
                }
            }
        }

        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        Queue<State> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();

        // Find starting position and initialize the BFS queue
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 'S') {
                    queue.offer(new State(i, j, 0, new HashSet<>()));
                    visited.add(i + "," + j);
                }
            }
        }

        while (!queue.isEmpty()) {
            State current = queue.poll();
            int row = current.row;
            int col = current.col;
            int moves = current.moves;
            Set<Character> keysCollected = current.keys;

            if (keysCollected.size() == numKeys) {
                return moves;
            }

            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && grid[newRow][newCol] != 'W') {
                    char cell = grid[newRow][newCol];
                    Set<Character> newKeysCollected = new HashSet<>(keysCollected);

                    if (Character.isLowerCase(cell)) {
                        newKeysCollected.add(cell);
                    } else if (Character.isUpperCase(cell) && keysCollected.contains(Character.toLowerCase(cell))) {
                        // Check if the key for this door is collected
                        newKeysCollected.remove(Character.toLowerCase(cell));
                    } else if (Character.isUpperCase(cell)) {
                        continue; // Skip if the key for this door is not collected
                    }

                    String newState = newRow + "," + newCol;
                    if (!visited.contains(newState)) {
                        visited.add(newState);
                        queue.offer(new State(newRow, newCol, moves + 1, newKeysCollected));
                    }
                }
            }
        }

        return -1; // If exit cannot be reached
    }

    public static void main(String[] args) {
        char[][] grid = {
                {'S', 'P', 'P', 'P'},
                {'W', 'P', 'P', 'E'},
                {'P', 'b', 'W', 'P'},
                {'P', 'P', 'P', 'P'}
        };
        System.out.println(minMovesToCollectKeys(grid));
    }
}
