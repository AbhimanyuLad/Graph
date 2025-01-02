package com.graph.Graph.Traversal.Model;

import lombok.Data;

@Data
public class Edge {
    public int src;
    public int dest;
    public int wgt;

    public Edge(int src, int dest, int wgt) {
        this.src = src;
        this.dest = dest;
        this.wgt = wgt;
    }

    // Constructors
    public Edge(int src, int dest) {
        this.src = src;
        this.dest = dest;
    }
}
