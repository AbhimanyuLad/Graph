package com.graph.Graph.Traversal.Model;

import java.util.ArrayList;

import lombok.Data;

@Data
public class GraphCredentials {
    
    public int ver;
    public ArrayList<Edge> []edg;
    public int src;

    public GraphCredentials(int ver, ArrayList<Edge> []graph, int src){
        this.ver = ver;
        this.edg = graph;
        this.src = src;
    }
    
}
