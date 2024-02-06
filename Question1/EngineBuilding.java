package Question1;

import java.util.Arrays;

public class EngineBuilding {

    public static int minTimeToBuildEngines(int[] engines, int k) {
        Arrays.sort(engines);
        int totalTime = 0;
        int numEngineers = 1;

        for (int i = 0; i < engines.length; i++) {
            if (numEngineers * 2 < engines.length && engines[i] > k) {
                totalTime += k;
                numEngineers *= 2;
            } else {
                totalTime += engines[i];
            }
        }
        return totalTime;
    }

    public static void main(String[] args) {
        int[] engines = {3, 4, 5, 2};
        int k = 2;
        int minTime = minTimeToBuildEngines(engines, k);
        System.out.println("Minimum time to build all engines: " + minTime);
    }
}

