package com.graph.Graph.Traversal;

import org.springframework.boot.SpringApplication;

public class TestGraphTraversalApplication {

	public static void main(String[] args) {
		SpringApplication.from(GraphTraversalApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
