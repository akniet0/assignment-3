package shortestpath;

import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class DijkstraSP {
    public static class Result {
        public final double[] dist;
        public final int[] parent;
        public long operations = 0;

        public Result(int V) {
            dist = new double[V];
            parent = new int[V];
            Arrays.fill(dist, Double.POSITIVE_INFINITY);
            Arrays.fill(parent, -1);
        }

        public List<Integer> getPath(int target) {
            if (dist[target] == Double.POSITIVE_INFINITY) {
                return Collections.emptyList();
            }
            List<Integer> path = new ArrayList<>();
            for (int at = target; at != -1; at = parent[at]) {
                path.add(at);
            }
            Collections.reverse(path);
            return path;
        }
    }

    public static Result compute(Graph g, int source) {
        int V = g.getV();
        Result res = new Result(V);
        if (source < 0 || source >= V) return res;

        res.dist[source] = 0;
        PriorityQueue<double[]> pq = new PriorityQueue<>((a, b) -> {
            res.operations++;
            return Double.compare(a[1], b[1]);
        });
        pq.offer(new double[]{source, 0.0});

        while (!pq.isEmpty()) {
            double[] curr = pq.poll();
            int u = (int) curr[0];
            double d = curr[1];
            res.operations++;

            if (d > res.dist[u]) continue;

            for (Edge e : g.getAdj(u)) {
                res.operations++;
                if (e.weight < 0) {
                    throw new IllegalArgumentException("Negative edge weights are not allowed.");
                }
                if (res.dist[u] + e.weight < res.dist[e.v]) {
                    res.dist[e.v] = res.dist[u] + e.weight;
                    res.parent[e.v] = u;
                    pq.offer(new double[]{e.v, res.dist[e.v]});
                }
            }
        }
        return res;
    }
}