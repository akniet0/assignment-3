package graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int V;
    private final boolean directed;
    private final List<List<Edge>> adj;
    private final List<Edge> edges;

    public Graph(int V, boolean directed) {
        this.V = V;
        this.directed = directed;
        this.adj = new ArrayList<>(V);
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
        this.edges = new ArrayList<>();
    }

    public void addEdge(int u, int v, double weight) {
        Edge e = new Edge(u, v, weight);
        adj.get(u).add(e);
        edges.add(e);
        if (!directed && u != v) {
            adj.get(v).add(new Edge(v, u, weight));
        }
    }

    public int getV() { return V; }
    public int getE() { return edges.size(); }
    public boolean isDirected() { return directed; }
    public List<Edge> getAdj(int u) { return adj.get(u); }
    public List<Edge> getAllEdges() { return edges; }
}