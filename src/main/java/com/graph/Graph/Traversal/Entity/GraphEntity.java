package com.graph.Graph.Traversal.Entity;

import lombok.Data;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@Entity
public class GraphEntity {

    @NotBlank(message = "can't be null")
    @Size(min = 2, max = 99, message = "Min - 2")
    public int vertex;

    @NotNull(message = "Edges can't be null")
    // @Pattern(regexp = "^\\d+-\\d+(,\\d+-\\d+)*$", message = "Invalid edge format. Use src-dest,src-dest,...")
    private String edge;

    public String weights;

    @NotNull(message = "src can't be null")
    private int src;

  



}
