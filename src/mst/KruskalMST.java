package mst;

import dsu.DisjointSetUnion;
import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.List;

public class KruskalMST {
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

        List<Edge> sortedEdges = new ArrayList<>(g.getAllEdges());
        sortedEdges.sort((a, b) -> {
            res.operations++;
            return Double.compare(a.weight, b.weight);
        });

        DisjointSetUnion dsu = new DisjointSetUnion(V);

        for (Edge e : sortedEdges) {
            res.operations++;
            if (dsu.union(e.u, e.v)) {
                res.mstEdges.add(e);
                res.totalWeight += e.weight;
                if (res.mstEdges.size() == V - 1) {
                    break;
                }
            }
        }

        res.operations += dsu.findOps + dsu.unionOps;
        res.isConnected = (dsu.getComponents() == 1);
        return res;
    }
}