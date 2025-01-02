package com.graph.Graph.Traversal.Services;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.graph.Graph.Traversal.Entity.GraphEntity;
import com.graph.Graph.Traversal.Model.Edge;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GraphService {

    static class Pair implements Comparable<Pair> {
        public int n;
        public int path;

        public Pair(int n, int p) {
            this.n = n;
            this.path = p;
        }

        @Override
        public int compareTo(Pair p2) {
            return this.path - p2.path;
        }
    }

    public ArrayList<Edge>[] convertEdgesAndCreateGraph(String edgeString, int v, String weights) {
        ArrayList<Edge>[] graph = new ArrayList[v];
        for (int i = 0; i < graph.length; i++) {
            graph[i] = new ArrayList<>();
        }

        String result = edgeString.replace(", ", ",");
        // Split the input string by commas to get individual edges
        String[] edgePairs = result.split(",");
        String[] wgt = new String[v];
        if (!weights.isEmpty()) {
            String res = weights.replace(", ", ",");
            wgt = res.split(",");
        }

        int i = 0;

        for (String pair : edgePairs) {
            // Split each edge pair by the hyphen to get the two vertices
            String[] vertices = pair.split("-");
            if (vertices.length == 2) {
                try {
                    int src = Integer.parseInt(vertices[0]);
                    int dest = Integer.parseInt(vertices[1]);
                    int wgh = 1;
                    if (!weights.equals("") && wgt.length > i) {
                        wgh = Integer.parseInt(wgt[i]);
                    }
                    graph[src].add(new Edge(src, dest, wgh));
                    i++;
                } catch (NumberFormatException e) {
                    log.error("Invalid edge format: " + pair, e);
                    return null;
                }
            } else {
                log.error("Invalid edge format: " + pair);
                return null;
            }
        }

        print(graph);
        
        return graph;
    }

    public void print(ArrayList<Edge>[] graph) {
        for (int i = 0; i < graph.length; i++) {
            System.out.print(i + " -> ");
            for (int j = 0; j < graph[i].size(); j++) {
                Edge e = graph[i].get(j);
                System.out.print(e.src + " " + e.dest + " " + e.wgt + " | ");
            }
            System.out.println();
        }
    }

    public List bfs(ArrayList<Edge>[] graph, int src, int v) {
        boolean[] vis = new boolean[v];
        Queue<Integer> q = new ArrayDeque<>();
        List<Integer> nodes = new ArrayList<>();
        // Mark the source node as visited and enqueue it
        vis[src] = true;
        q.add(src);

        while (!q.isEmpty()) {
            int curr = q.remove();
            System.out.print(curr + " "); // Process the current node
            nodes.add(curr);

            for (int i = 0; i < graph[curr].size(); i++) {
                Edge e = graph[curr].get(i);
                if (!vis[e.dest]) {
                    vis[e.dest] = true; // Mark as visited when adding to queue
                    q.add(e.dest);
                }
            }
        }
        System.out.println();
        return nodes;
    }

    public List<Integer> dfs(ArrayList<Edge>[] edges, boolean[] visited, int src) {
        List<Integer> dfsOrder = new ArrayList<>();
        dfsRecursive(edges, visited, src, dfsOrder);
        System.out.println();
        return dfsOrder;
    }

    private void dfsRecursive(ArrayList<Edge>[] edges, boolean[] visited, int node, List<Integer> dfsOrder) {
        if (visited[node]) {
            return;
        }
        visited[node] = true;
        dfsOrder.add(node);

        for (int i = 0; i < edges[node].size(); i++) {
            Edge e = edges[node].get(i);
            if (!visited[e.dest]) {
                dfsRecursive(edges, visited, e.dest, dfsOrder);
            }
        }
    }

    public List<Integer> shortestPath(ArrayList<Edge>[] graph, int src, int dest, int v) {
        boolean[] vis = new boolean[v];
        boolean flag = hasPath(graph, src, dest, vis);
        if (!flag) {
            return null;
        }
        Arrays.fill(vis, false);
        return dijkshtras(graph, src, dest, vis, v);

    }

    private boolean hasPath(ArrayList<Edge>[] graph, int src, int dest, boolean[] vis) {
        if (src == dest) {
            return true;
        }

        vis[src] = true;
        for (int i = 0; i < graph[src].size(); i++) {
            Edge e = graph[src].get(i);
            if (!vis[e.dest] && hasPath(graph, e.dest, dest, vis)) {
                return true;
            }
        }
        return false;
    }

    private List<Integer> dijkshtras(ArrayList<Edge>[] graph, int src, int dest, boolean[] vis, int vertex) {
        List<Integer> pathNodes = new ArrayList<>();
        PriorityQueue<Pair> pq = new PriorityQueue<>();
        int[] dist = new int[vertex];
        for (int i = 0; i < dist.length; i++) {
            if (i != src) {
                dist[i] = Integer.MAX_VALUE;
            }
        }

        pq.add(new Pair(src, 0));
        while (!pq.isEmpty()) {
            Pair curr = pq.remove();
            if (curr.n == dest) {
                pathNodes.add(curr.n);
                break;
            }
            
            if (!vis[curr.n]) {
                pathNodes.add(curr.n);
                vis[curr.n] = true;
                for (int i = 0; i < graph[curr.n].size(); i++) {
                    Edge e = graph[curr.n].get(i);
                    int u = e.src;
                    int v = e.dest;
                    int w = e.wgt;
                    if (dist[u] + w < dist[v]) {
                        dist[v] = dist[u] + w;
                        pq.add(new Pair(v, dist[v]));
                    }
                }
            }
        }
        System.out.println(pathNodes);
        return pathNodes;
    }

}
