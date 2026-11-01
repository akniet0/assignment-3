package mst;

import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class PrimMST {
    public static class Result {
        public final List<Edge> mstEdges = new ArrayList<>();
        public double totalWeight = 0;
        public boolean isConnected = false;
        public long operations = 0;
    }

    public static Result compute(Graph g) {
        Result res = new Result();
        int V = g.getV();
        if (V == 0) return res;
        if (V == 1) {
            res.isConnected = true;
            return res;
        }

        boolean[] visited = new boolean[V];
        PriorityQueue<Edge> pq = new PriorityQueue<>((a, b) -> {
            res.operations++;
            return Double.compare(a.weight, b.weight);
        });

        int visitedCount = 1;
        visited[0] = true;
        for (Edge e : g.getAdj(0)) {
            pq.offer(e);
            res.operations++;
        }

        while (!pq.isEmpty() && visitedCount < V) {
            Edge e = pq.poll();
            res.operations++;
            if (visited[e.v]) continue;

            visited[e.v] = true;
            visitedCount++;
            res.mstEdges.add(e);
            res.totalWeight += e.weight;

            for (Edge next : g.getAdj(e.v)) {
                res.operations++;
                if (!visited[next.v]) {
                    pq.offer(next);
                }
            }
        }

        res.isConnected = (visitedCount == V);
        return res;
    }
}