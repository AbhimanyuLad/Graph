package com.graph.Graph.Traversal.Entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShortestPath {

    @NotNull(message = "Source vertex is required")
    public int src;

    @NotNull(message = "Destination vertex is required")
    public int dest;
}
