package graph;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class ShortestPathsTest {

    /**
     * The graph example from Prof. Myers's notes. There are 7 vertices labeled a-g, as described by
     * vertices1. Edges are specified by edges1 as triples of the form {src, dest, weight} where src
     * and dest are the indices of the source and destination vertices in vertices1. For example,
     * there is an edge from a to d with weight 15.
     */
    static final String[] vertices1 = {"a", "b", "c", "d", "e", "f", "g"};

    static final int[][] edges1 = {
            {0, 1, 9}, {0, 2, 14}, {0, 3, 15},
            {1, 4, 23},
            {2, 4, 17}, {2, 3, 5}, {2, 5, 30},
            {3, 5, 20}, {3, 6, 37},
            {4, 5, 3}, {4, 6, 20},
            {5, 6, 16}
    };


    static final String[] vertices2 = {"Ithaca", "New York", "Boston", "Chicago", "Seattle"};
    static final int[][] edges2 = {
            {0, 1, 222}, {0, 3, 670},
            {1, 2, 190}, {1, 3, 790},
            {2, 3, 850}, {2, 4, 2485},
            {3, 4, 2064}
    };
    static final String[] vertices3 = {"Task0", "Task1", "Task2", "Task3", "Task4", "Task5",
            "Task6", "Task7"};
    static final int[][] edges3 = {
            {0, 1, 1}, {0, 2, 2}, {0, 3, 5},
            {1, 4, 4}, {1, 5, 11},
            {2, 4, 9}, {2, 5, 5}, {2, 6, 16},
            {3, 6, 2},
            {4, 7, 18},
            {5, 7, 13},
            {6, 7, 2}
    };

    static class TestGraph implements WeightedDigraph<String, int[]> {

        int[][] edges;
        String[] vertices;
        Map<String, Set<int[]>> outgoing;

        TestGraph(String[] vertices, int[][] edges) {
            this.vertices = vertices;
            this.edges = edges;
            this.outgoing = new HashMap<>();
            for (String v : vertices) {
                outgoing.put(v, new HashSet<>());
            }
            for (int[] edge : edges) {
                outgoing.get(vertices[edge[0]]).add(edge);
            }
        }

        public Iterable<int[]> outgoingEdges(String vertex) {
            return outgoing.get(vertex);
        }

        public String source(int[] edge) {
            return vertices[edge[0]];
        }

        public String dest(int[] edge) {
            return vertices[edge[1]];
        }

        public double weight(int[] edge) {
            return edge[2];
        }
    }

    static TestGraph testGraph1() {
        return new TestGraph(vertices1, edges1);
    }

    static TestGraph testGraph2() {
        return new TestGraph(vertices2, edges2);
    }

    static TestGraph testGraph3() {
        return new TestGraph(vertices3, edges3);
    }


    @Test
    void lectureNotesTest() {
        TestGraph graph = testGraph1();
        ShortestPaths<String, int[]> ssp = new ShortestPaths<>(graph);
        ssp.singleSourceDistances("a");
        assertEquals(50, ssp.getDistance("g"));
        StringBuilder sb = new StringBuilder();
        sb.append("best path:");
        for (int[] e : ssp.bestPath("g")) {
            sb.append(" " + vertices1[e[0]]);
        }
        sb.append(" g");
        assertEquals("best path: a c e f g", sb.toString());
    }

    // TODO: Add 2 more tests
    @Test
    void TestOne() {
        TestGraph graph = testGraph2();
        ShortestPaths<String, int[]> ssp = new ShortestPaths<>(graph);
        ssp.singleSourceDistances("Ithaca");
        assertEquals(2734, ssp.getDistance("Seattle"));
        StringBuilder sb = new StringBuilder();
        sb.append("best path:");
        for (int[] e : ssp.bestPath("Seattle")) {
            sb.append(" " + vertices2[e[0]]);
        }
        sb.append(" Seattle");
        assertEquals("best path: Ithaca Chicago Seattle", sb.toString());
    }

    // directed edges affect the calculation of distances
    @Test
    void TestTwo() {
        TestGraph graph = testGraph3();
        ShortestPaths<String, int[]> ssp = new ShortestPaths<>(graph);
        ssp.singleSourceDistances("Task0");
        assertEquals(9, ssp.getDistance("Task7"));
        StringBuilder sb = new StringBuilder();
        sb.append("best path:");
        for (int[] e : ssp.bestPath("Task7")) {
            sb.append(" " + vertices3[e[0]]);
        }
        sb.append(" Task7");
        assertEquals("best path: Task0 Task3 Task6 Task7", sb.toString());
    }

}
