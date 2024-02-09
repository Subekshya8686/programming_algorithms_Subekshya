package src.Question1;

public class MinimumCostDecoration {

    public static int minCostToDecorateVenues(int[][] costs) {
        if (costs == null || costs.length == 0 || costs[0].length == 0) {
            return 0;
        }

        int n = costs.length;
        int k = costs[0].length;

        // Initialize a DP array to store minimum costs
        int[][] dp = new int[n][k];

        // Copy the cost of decorating the first venue to the DP array
        for (int i = 0; i < k; i++) {
            dp[0][i] = costs[0][i];
        }

        // Iterate through the venues and calculate the minimum cost
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < k; j++) {
                // Find the minimum cost by considering the costs of adjacent venues
                dp[i][j] = costs[i][j] + getMin(dp[i - 1]);
            }
        }

        // Return the minimum cost of decorating the last venue
        return getMin(dp[n - 1]);
    }

    private static int getMin(int[] array) {
        int min = Integer.MAX_VALUE;
        for (int num : array) {
            min = Math.min(min, num);
        }
        return min;
    }

    public static void main(String[] args) {
        int[][] costMatrix = {{1, 3, 2}, {4, 6, 8}, {3, 1, 5}};
        int result = minCostToDecorateVenues(costMatrix);
        System.out.println("Minimum cost: " + result);
    }
}
