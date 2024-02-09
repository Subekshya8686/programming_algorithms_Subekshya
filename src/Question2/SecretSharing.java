package Question2;

import java.util.ArrayList;
import java.util.List;

class SecretSharing {
    public List<Integer> findSecretHolders(int n, int[][] intervals, int firstPerson) {
        boolean[] knowsSecret = new boolean[n];
        knowsSecret[firstPerson] = true; // Initially, first person has the secret

        // Iterate through each interval
        for (int[] interval : intervals) {
            int start = interval[0];
            int end = interval[1];

            // Share the secret with individuals within the current interval
            for (int i = start; i <= end; i++) {
                if (!knowsSecret[i]) {
                    knowsSecret[i] = true;
                }
            }
        }

        // Collect individuals who know the secret
        List<Integer> secretHolders = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (knowsSecret[i]) {
                secretHolders.add(i);
            }
        }

        return secretHolders;
    }

    public static void main(String[] args) {
        int n = 5;
        int[][] intervals = {{0, 2}, {1, 3}, {2, 4}};
        int firstPerson = 0;

        SecretSharing secretSharing = new SecretSharing();
        List<Integer> result = secretSharing.findSecretHolders(n, intervals, firstPerson);

        System.out.println("Individuals who eventually know the secret: " + result);
    }
}
