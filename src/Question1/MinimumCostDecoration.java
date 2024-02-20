//You are a planner working on organizing a series of events in a row of n venues. Each venue can be decorated with
//one of the k available themes. However, adjacent venues should not have the same theme. The cost of decorating
//each venue with a certain theme varies.
//The costs of decorating each venue with a specific theme are represented by an n x k cost matrix. For example,
//costs [0][0] represents the cost of decorating venue 0 with theme 0, and costs[1][2] represents the cost of
//decorating venue 1 with theme 2. Your task is to find the minimum cost to decorate all the venues while adhering
//to the adjacency constraint.
//For example, given the input costs = [[1, 5, 3], [2, 9, 4]], the minimum cost to decorate all the venues is 5. One
//possible arrangement is decorating venue 0 with theme 0 and venue 1 with theme 2, resulting in a minimum cost of
//1 + 4 = 5. Alternatively, decorating venue 0 with theme 2 and venue 1 with theme 0 also yields a minimum cost of
//3 + 2 = 5.
//Write a function that takes the cost matrix as input and returns the minimum cost to decorate all the venues while
//satisfying the adjacency constraint.
//Please note that the costs are positive integers.
//Example: Input: [[1, 3, 2], [4, 6, 8], [3, 1, 5]] Output: 7
//Explanation: Decorate venue 0 with theme 0, venue 1 with theme 1, and venue 2 with theme 0. Minimum cost: 1 +
//6 + 1 = 7.

package src.Question1;

public class MinimumCostDecoration {
//     check if the input matrix is valid
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
                int minCost = Integer.MAX_VALUE;

                for (int l =0; l<k;l++){
                    if (l!=j){
                        minCost= Math.min(minCost, dp[i-1][l]);
                    }
                }
                // Find the minimum cost by considering the costs of adjacent venues
                dp[i][j] = costs[i][j] + minCost;
            }
        }

//         find the minimum total cost of decorating the last venue
        int minTotalCost = Integer.MAX_VALUE;
        for (int j = 0; j<k; j++){
            minTotalCost = Math.min(minTotalCost,dp[n-1][j]);
        }

        // Return the minimum cost of decorating the last venue
        return minTotalCost;
    }


    public static void main(String[] args) {
        int[][] costs = {{1, 3, 2}, {4, 6, 8}, {3, 1, 5}};
        System.out.println("Minimum cost: " + minCostToDecorateVenues(costs));
    }
}
