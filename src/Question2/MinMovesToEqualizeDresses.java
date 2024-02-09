package src.Question2;
public class MinMovesToEqualizeDresses {

    public static int minMovesToEqualize(int[] dresses) {
        int n = dresses.length;
        int sum = 0;
        for (int dress : dresses) {
            sum += dress;
        }

        // If the total number of dresses is not divisible by the number
        // of sewing machines, return -1
        if (sum % n != 0) {
            return -1;
        }

        // Calculate the target value
        int target = sum / n;

        int moves = 0;
        int currentSum = 0;

        // Iterate through the array and calculate the moves
        // needed for each sewing machine
        for (int dress : dresses) {
            int diff = dress - target;
            currentSum += diff;
            moves += Math.abs(currentSum); // Accumulate the absolute differences
        }
        return moves;
    }

    public static void main(String[] args) {
        int[] dresses = {2, 1, 3, 0, 2};
        int result = minMovesToEqualize(dresses);
        System.out.println("Minimum moves to equalize dresses: " + result);
    }
}
