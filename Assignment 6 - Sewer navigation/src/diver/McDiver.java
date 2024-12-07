package diver;

import datastructures.PQueue;
import datastructures.SlowPQueue;
import game.*;
import graph.ShortestPaths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.*;
import java.util.Comparator;
import java.util.Collections;


/**
 * This is the place for your implementation of the {@code SewerDiver}.
 */
public class McDiver implements SewerDiver {

    /**
     * See {@code SewerDriver} for specification.
     */
    @Override
    public void seek(SeekState state) {
        // DO NOT WRITE ALL THE CODE HERE. DO NOT MAKE THIS METHOD RECURSIVE.
        // Instead, write your method (it may be recursive) elsewhere, with a
        // good specification, and call it from this one.
        //
        // Working this way provides you with flexibility. For example, write
        // one basic method, which always works. Then, make a method that is a
        // copy of the first one and try to optimize in that second one.
        // If you don't succeed, you can always use the first one.
        //
        // Use this same process on the second method, scram.
        HashSet<Long> visited = new HashSet<>();
        dfsWalk(state, visited);
    }

    /**
     * This method performs a depth-first search (DFS) through the sewer system, checking all
     * neighbor tiles. The diver moves to a neighboring tile, recursively checks from there, and
     * backtracks if needed. The loop continues until the diver either finds the ring or exhausts
     * all neighbors.
     * <p>
     * Precondition: state and visitedLst must not be null.
     */
    public static void dfsWalk(SeekState state, HashSet<Long> visitedLst) {
        // Preconditions
        assert state != null;
        assert visitedLst != null;

        // Base case: If McDiver finds the ring, return.
        if (state.distanceToRing() == 0) {
            return;
        }

        long currentId = state.currentLocation();
        visitedLst.add(currentId);
        List<NodeStatus> neighbors = new ArrayList<>(state.neighbors());
        Collections.sort(neighbors); // sort the neighbors based on the distance to the ring

        // The diver explores neighbors with depth-first algorithm until it finds the ring or
        // explored all neighbors.
        for (NodeStatus neighbor : neighbors) {
            int amt = state.neighbors().size();
            long neighborId = neighbor.getId();

            if (!visitedLst.contains(neighborId)) {
                if (state.distanceToRing() == 0) {
                    return;
                }
                state.moveTo(neighborId);
                dfsWalk(state, visitedLst);
                if (state.distanceToRing() == 0) {
                    return;
                }
                state.moveTo(currentId);
            }
        }
    }

    /**
     * See {@code SewerDriver} for specification.
     */
    @Override
    public void scram(ScramState state) {
        // DO NOT WRITE ALL THE CODE HERE. Instead, write your method elsewhere,
        // with a good specification, and call it from this one.
        try {
            Maze maze = new Maze((Set<Node>) state.allNodes());
            exit(state, maze);
        } catch (Exception e) {
            // Handle the exception or log the error
            System.err.println("Exit. " + e.getMessage());
        }
    }

    /**
     * This method finds the optimal path for a diver to navigate through a maze to the exit,
     * collecting coins along the way. The diver starts at the current node of the given state and
     * needs to exit before steps run out. The algorithm prioritizes nodes with coins based on the
     * ratio of "coins to distance". It uses a recursive approach and dynamic adjustments to
     * maximize coin collection.
     * <p>
     * The base case is when the McDiver's current position is the exit node. If this condition is
     * met, the method returns.
     * <p>
     * For each node with coins, the method calculates the ratio of "coins to distance". It
     * prioritizes nodes with higher ratios, optimizing the collection of coins while ensuring
     * reaching the exit within the remaining steps. If there are no coin nodes left, it is heading
     * straight to the exit by following the best path to the exit. The algorithm continues
     * recursively, backtracking and re-evaluating feasible coin tiles until it runs out of coins to
     * retrieve or when no more feasible coins can be reached with the remaining steps.
     * <p>
     * Precondition: state and maze must not be null.
     */
    public static void exit(ScramState state, Maze maze) {
        // Preconditions
        assert state != null;
        assert maze != null;

        // Base case: Return if the diver is at the exit node.
        if (state.currentNode() == state.exit()) {
            return;
        }

        ShortestPaths<Node, Edge> shortest = new ShortestPaths<>(maze);
        shortest.singleSourceDistances(state.currentNode());
        Map<Node, Double> map = new HashMap<>();
        List<Node> next = new ArrayList<>();

        // For every node with conins, calculate "coins-to-distance" ratio.
        for (Node node : state.allNodes()) {
            double dist = shortest.getDistance(node); // distance between current node to tile
            // with coins
            if (node.getTile().coins() != 0) { // if Tile has coins
                double temp = node.getTile().coins() / dist; // ratio of coin/dist
                map.put(node, temp); // add node and ratio into map
            }
        }

        // If there are no nodes with coins, head straight to the exit.
        if (map.isEmpty()) {
            shortest.singleSourceDistances(state.currentNode());
            List<Edge> best = shortest.bestPath(state.exit());
            for (Edge go : best) {
                state.moveTo(go.destination());
            }
            return;
        }

        // Choose the next node with the highest "coins-to-distance" ratio.
        double value = 0.0;
        for (Node node : map.keySet()) {
            if (value < map.get(node)) {
                value = map.get(node);
                next.add(node);
            }
            if (next.size() > 1) {
                next.remove(0);
            }
        }

        // Check if the node is feasible for the diver to travel to
        ShortestPaths<Node, Edge> small = new ShortestPaths<>(maze);
        shortest.singleSourceDistances(state.currentNode());
        double dist = shortest.getDistance(next.get(0));
        small.singleSourceDistances(next.get(0));
        double feasible = dist + small.getDistance(state.exit());

        // If the largest ratio coin is not feasible, skip this coin.
        while (feasible > state.stepsToGo()) { // if largest ratio coins is not feasible
            map.remove(next.get(0)); // skip this coin
            if (map.isEmpty()) { // no coin tile is feasible for diver -> exit
                shortest.singleSourceDistances(state.currentNode());
                List<Edge> best = shortest.bestPath(state.exit());
                for (Edge go : best) {
                    state.moveTo(go.destination());
                }
                return;
            } else { // take the next highest ratio coin tile and check is its feasible
                value = 0.0;
                for (Node node : map.keySet()) {
                    if (value < map.get(node)) {
                        value = map.get(node);
                        next.add(node);
                    }
                    if (next.size() > 1) {
                        next.remove(0);
                    }
                }
                shortest.singleSourceDistances(state.currentNode());
                dist = shortest.getDistance(next.get(0));
                small.singleSourceDistances(next.get(0));
                feasible = dist + small.getDistance(state.exit());
            }
        }

        // Move to feasible coin tile with highest ratio.
        shortest.singleSourceDistances(state.currentNode());
        List<Edge> list = shortest.bestPath(next.get(0));
        for (Edge go : list) {
            state.moveTo(go.destination());
        }

        // Recursively continue the exiting and coin collection.
        exit(state, maze);
    }
}
