package src.Question3;

import java.util.Arrays;

public class ScoreTracker {
    private double[] scores;
    private int size;

    public ScoreTracker() {
        scores = new double[0];
        size = 0;
    }

    public void addScore(double score) {
        scores = Arrays.copyOf(scores, size + 1);
        scores[size++] = score;
        Arrays.sort(scores);
    }

    public double getMedianScore() {
        if (size == 0) {
            throw new IllegalStateException("No scores available.");
        }

        if (size % 2 == 0) {
            int mid = size / 2;
            return (scores[mid - 1] + scores[mid]) / 2.0;
        } else {
            return scores[size / 2];
        }
    }

    public static void main(String[] args) {
        ScoreTracker scoreTracker = new ScoreTracker();
        scoreTracker.addScore(85.5);
        scoreTracker.addScore(92.3);
        scoreTracker.addScore(77.8);
        scoreTracker.addScore(90.1);
        double median1 = scoreTracker.getMedianScore();
        System.out.println("Median 1: " + median1);

        scoreTracker.addScore(81.2);
        scoreTracker.addScore(88.7);
        double median2 = scoreTracker.getMedianScore();
        System.out.println("Median 2: " + median2);
    }
}

