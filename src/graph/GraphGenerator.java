package graph;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GraphGenerator {
    public static Graph generateConnectedUndirected(int V, int E, long seed) {
        Random rand = new Random(seed);
        Graph g = new Graph(V, false);
        Set<String> used = new HashSet<>();

        for (int i = 1; i < V; i++) {
            int u = rand.nextInt(i);
            double w = 1 + rand.nextInt(100);
            g.addEdge(u, i, w);
            used.add(Math.min(u, i) + ":" + Math.max(u, i));
        }

        while (g.getE() < E) {
            int u = rand.nextInt(V);
            int v = rand.nextInt(V);
            if (u == v) continue;
            String key = Math.min(u, v) + ":" + Math.max(u, v);
            if (!used.contains(key)) {
                used.add(key);
                g.addEdge(u, v, 1 + rand.nextInt(100));
            }
        }
        return g;
    }

    public static Graph generateDirected(int V, int E, boolean dagOnly, long seed) {
        Random rand = new Random(seed);
        Graph g = new Graph(V, true);
        Set<String> used = new HashSet<>();

        while (g.getE() < E) {
            int u = rand.nextInt(V);
            int v = rand.nextInt(V);
            if (u == v) continue;
            if (dagOnly && u > v) {
                int tmp = u;
                u = v;
                v = tmp;
            }
            String key = u + ":" + v;
            if (!used.contains(key)) {
                used.add(key);
                g.addEdge(u, v, 1 + rand.nextInt(100));
            }
        }
        return g;
    }
}