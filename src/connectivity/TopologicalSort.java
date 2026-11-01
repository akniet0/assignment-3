package connectivity;

import graph.Edge;
import graph.Graph;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class TopologicalSort {
    public static class Result {
        public final List<Integer> order = new ArrayList<>();
        public boolean hasCycle = false;
        public long operations = 0;
    }

    public static Result compute(Graph g) {
        Result res = new Result();
        int V = g.getV();
        int[] inDegree = new int[V];

        for (int u = 0; u < V; u++) {
            for (Edge e : g.getAdj(u)) {
                inDegree[e.v]++;
                res.operations++;
            }
        }

        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < V; i++) {
            res.operations++;
            if (inDegree[i] == 0) {
                q.offer(i);
            }
        }

        while (!q.isEmpty()) {
            int u = q.poll();
            res.order.add(u);
            res.operations++;

            for (Edge e : g.getAdj(u)) {
                res.operations++;
                if (--inDegree[e.v] == 0) {
                    q.offer(e.v);
                }
            }
        }

        if (res.order.size() != V) {
            res.hasCycle = true;
            res.order.clear();
        }
        return res;
    }
}