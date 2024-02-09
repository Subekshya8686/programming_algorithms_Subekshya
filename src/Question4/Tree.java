package src.Question4;

import java.util.ArrayList;
import java.util.List;

public class Tree {

    public static class Node {
        Node left;
        Node right;
        int data;

        Node(int data) {
            this.data = data;
            this.left = this.right = null;
        }
    }

    void inorder(Node root, double k, int x, List<Integer> closest) {
        if (root == null)
            return;

        inorder(root.left, k, x, closest);

        if (closest.size() < x) {
            closest.add(root.data);
        } else {
            if (Math.abs(root.data - k) < Math.abs(closest.get(0) - k)) {
                closest.remove(0);
                closest.add(root.data);
            } else {
                return; // No need to explore further
            }
        }

        inorder(root.right, k, x, closest);
    }

    public static void main(String[] args) {
        Tree tree = new Tree();

        // Construct the binary tree
        Node root = new Node(4);
        root.left = new Node(2);
        root.right = new Node(5);
        root.left.left = new Node(1);
        root.left.right = new Node(3);

        // Given target value k and x
        double k = 3.8;
        int x = 2;
        List<Integer> closest = new ArrayList<>();

        // Find x close values to k
        tree.inorder(root, k, x, closest);
        if (!closest.isEmpty()) {
            System.out.println("The one set of values closest to the target are: "+ closest);
        } else {
            System.out.println("No values found close to the target.");
        }
    }
}

