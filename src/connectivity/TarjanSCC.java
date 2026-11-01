package connectivity;

import graph.Edge;
import graph.Graph;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class TarjanSCC {
    public static class Result {
        public final List<List<Integer>> components = new ArrayList<>();
        public long operations = 0;
    }

    private int time = 0;
    private int[] disc;
    private int[] low;
    private boolean[] onStack;
    private Deque<Integer> stack;

    public Result compute(Graph g) {
        Result res = new Result();
        int V = g.getV();
        disc = new int[V];
        low = new int[V];
        onStack = new boolean[V];
        stack = new ArrayDeque<>();
        Arrays.fill(disc, -1);

        for (int i = 0; i < V; i++) {
            res.operations++;
            if (disc[i] == -1) {
                dfs(i, g, res);
            }
        }
        return res;
    }

    private void dfs(int u, Graph g, Result res) {
        disc[u] = low[u] = time++;
        stack.push(u);
        onStack[u] = true;
        res.operations++;

        for (Edge e : g.getAdj(u)) {
            res.operations++;
            int v = e.v;
            if (disc[v] == -1) {
                dfs(v, g, res);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        if (low[u] == disc[u]) {
            List<Integer> scc = new ArrayList<>();
            while (true) {
                int v = stack.pop();
                onStack[v] = false;
                scc.add(v);
                res.operations++;
                if (u == v) break;
            }
            res.components.add(scc);
        }
    }
}