import connectivity.TarjanSCC;
import connectivity.TopologicalSort;
import graph.Graph;
import graph.GraphGenerator;
import mst.KruskalMST;
import mst.PrimMST;
import shortestpath.DijkstraSP;

public class MainBenchmark {
    public static void main(String[] args) {
        runTests();
        runExperiments();
    }

    private static void runTests() {
        Graph single = new Graph(1, false);
        if (!KruskalMST.compute(single).isConnected || !PrimMST.compute(single).isConnected) {
            throw new RuntimeException("Single vertex test failed");
        }

        Graph disconnected = new Graph(4, false);
        disconnected.addEdge(0, 1, 5);
        disconnected.addEdge(2, 3, 10);
        if (KruskalMST.compute(disconnected).isConnected || PrimMST.compute(disconnected).isConnected) {
            throw new RuntimeException("Disconnected graph test failed");
        }

        Graph duplicateWeights = new Graph(4, false);
        duplicateWeights.addEdge(0, 1, 10);
        duplicateWeights.addEdge(1, 2, 10);
        duplicateWeights.addEdge(2, 3, 10);
        duplicateWeights.addEdge(3, 0, 10);
        if (KruskalMST.compute(duplicateWeights).totalWeight != PrimMST.compute(duplicateWeights).totalWeight) {
            throw new RuntimeException("Duplicate weight MST test failed");
        }

        Graph cyclic = new Graph(3, true);
        cyclic.addEdge(0, 1, 1);
        cyclic.addEdge(1, 2, 1);
        cyclic.addEdge(2, 0, 1);
        if (!TopologicalSort.compute(cyclic).hasCycle) {
            throw new RuntimeException("Cycle detection test failed");
        }

        Graph dag = GraphGenerator.generateDirected(10, 20, true, 42L);
        if (TopologicalSort.compute(dag).hasCycle) {
            throw new RuntimeException("DAG topological sort test failed");
        }

        Graph unreachable = new Graph(4, true);
        unreachable.addEdge(0, 1, 5);
        unreachable.addEdge(1, 2, 5);
        DijkstraSP.Result dRes = DijkstraSP.compute(unreachable, 0);
        if (dRes.dist[3] != Double.POSITIVE_INFINITY || !dRes.getPath(3).isEmpty()) {
            throw new RuntimeException("Unreachable vertex test failed");
        }

        System.out.println("All tests passed.");
    }

    private static void runExperiments() {
        int[][] sizes = {{10, 30}, {100, 500}, {1000, 5000}};
        String[] names = {"Small", "Medium", "Large"};
        int trials = 5;

        System.out.printf("%-8s | %-6s | %-6s | %-12s | %-14s | %-12s%n",
                "Graph", "V", "E", "Algorithm", "Avg Time (ns)", "Avg Ops");
        System.out.println("-------------------------------------------------------------------------");

        for (int i = 0; i < sizes.length; i++) {
            int V = sizes[i][0];
            int E = sizes[i][1];
            Graph undir = GraphGenerator.generateConnectedUndirected(V, E, 42L);
            Graph dirGeneral = GraphGenerator.generateDirected(V, E, false, 42L);
            Graph dirDag = GraphGenerator.generateDirected(V, E, true, 42L);

            long kTime = 0, kOps = 0;
            long pTime = 0, pOps = 0;
            long sccTime = 0, sccOps = 0;
            long topoTime = 0, topoOps = 0;
            long dijTime = 0, dijOps = 0;

            for (int t = 0; t < trials; t++) {
                long start = System.nanoTime();
                KruskalMST.Result kr = KruskalMST.compute(undir);
                kTime += (System.nanoTime() - start);
                kOps += kr.operations;

                start = System.nanoTime();
                PrimMST.Result pr = PrimMST.compute(undir);
                pTime += (System.nanoTime() - start);
                pOps += pr.operations;

                start = System.nanoTime();
                TarjanSCC.Result sr = new TarjanSCC().compute(dirGeneral);
                sccTime += (System.nanoTime() - start);
                sccOps += sr.operations;

                start = System.nanoTime();
                TopologicalSort.Result tr = TopologicalSort.compute(dirDag);
                topoTime += (System.nanoTime() - start);
                topoOps += tr.operations;

                start = System.nanoTime();
                DijkstraSP.Result dr = DijkstraSP.compute(dirGeneral, 0);
                dijTime += (System.nanoTime() - start);
                dijOps += dr.operations;
            }

            System.out.printf("%-8s | %-6d | %-6d | %-12s | %-14d | %-12d%n", names[i], V, E, "Kruskal", kTime / trials, kOps / trials);
            System.out.printf("%-8s | %-6d | %-6d | %-12s | %-14d | %-12d%n", names[i], V, E, "Prim", pTime / trials, pOps / trials);
            System.out.printf("%-8s | %-6d | %-6d | %-12s | %-14d | %-12d%n", names[i], V, E, "Tarjan SCC", sccTime / trials, sccOps / trials);
            System.out.printf("%-8s | %-6d | %-6d | %-12s | %-14d | %-12d%n", names[i], V, E, "TopoSort", topoTime / trials, topoOps / trials);
            System.out.printf("%-8s | %-6d | %-6d | %-12s | %-14d | %-12d%n", names[i], V, E, "Dijkstra", dijTime / trials, dijOps / trials);
            System.out.println("-------------------------------------------------------------------------");
        }
    }
}