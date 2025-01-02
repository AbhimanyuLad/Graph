package com.graph.Graph.Traversal.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.graph.Graph.Traversal.Entity.GraphEntity;
import com.graph.Graph.Traversal.Entity.ShortestPath;
import com.graph.Graph.Traversal.Model.Edge;
import com.graph.Graph.Traversal.Model.GraphCredentials;
import com.graph.Graph.Traversal.Services.GraphService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequestMapping("/graph")
public class GraphController {

    @Autowired
    private GraphService graphService;

    public GraphCredentials credentials;

    @GetMapping("/getGraph")
    public String createGraph(Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("Graph", new GraphEntity());
        return "home";
    }

    @PostMapping("/createGraph")
    public String postMethodName(@Valid @ModelAttribute("Graph") GraphEntity graph, BindingResult result,
            RedirectAttributes redirectAttributes) {
        boolean isCreated = false;
        try {
            if (result.hasErrors()) {
                redirectAttributes.addFlashAttribute("alertType", "danger");
                redirectAttributes.addFlashAttribute("alertMessage",
                        "Error! Some error in arguments, please check the input and try again.");
                log.info(result.toString());
                return "redirect:getGraph";
            }

            redirectAttributes.addFlashAttribute("Graph", graph);

            ArrayList<Edge>[] edges = graphService.convertEdgesAndCreateGraph(graph.getEdge(), graph.vertex + 1,
                    graph.getWeights());
            credentials = new GraphCredentials(graph.vertex, edges, graph.getSrc());

            for (ArrayList<Edge> edgeList : edges) {
                for (Edge edge : edgeList) {
                    log.info("Source: " + edge.getSrc() + ", Destination: " + edge.getDest() + ", Weight: "
                            + edge.getWgt());
                }
            }

            // Create D3.js data
            List<Map<String, Object>> links = new ArrayList<>();
            Set<Map<String, Object>> nodesData = new HashSet<>();

            for (ArrayList<Edge> edgeList : edges) {
                for (Edge edge : edgeList) {
                    links.add(Map.of("source", edge.getSrc(), "target", edge.getDest(), "weight", edge.getWgt()));
                    nodesData.add(Map.of("id", edge.getSrc()));
                    nodesData.add(Map.of("id", edge.getDest()));
                }
            }

            Map<String, Object> graphData = new HashMap<>();
            graphData.put("nodes", nodesData);
            graphData.put("links", links);

            isCreated = true;
            if (isCreated) {
                log.info("Edges passed to view: {}", Arrays.toString(edges));
                System.out.println(graphData);
                redirectAttributes.addFlashAttribute("links", links);
                redirectAttributes.addFlashAttribute("graph", graphData);
                redirectAttributes.addFlashAttribute("alertType", "success");
                redirectAttributes.addFlashAttribute("alertMessage", "Graph is successfully formed.");
            }

            log.info("OK");
            return "redirect:getGraph";
        } catch (Exception e) {
            log.error("Wrong credentials typed", e);
            redirectAttributes.addFlashAttribute("alertType", "danger");
            redirectAttributes.addFlashAttribute("alertMessage",
                    "Error! Some error in input, please check the input and try again.");
            return "redirect:getGraph";
        }
    }

    @PostMapping("/bfs")
    public ResponseEntity bfsTraversal() {
        Map<String, Object> response = new HashMap<>();
        try {
            if (credentials == null) {
                response.put("alertType", "danger");
                response.put("alertMessage", "BFS Traversal didn't initialize. Please create a graph first.");
                return ResponseEntity.ok(response);
            }

            List<Integer> bfsOrder = graphService.bfs(credentials.getEdg(), credentials.getSrc(),
                    credentials.getVer() + 1);
            response.put("bfsOrder", bfsOrder);
            response.put("alertType", "success");
            response.put("alertMessage", "See BFS traversal at the bottom of the buttons.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error during BFS traversal", e);
            response.put("alertType", "danger");
            response.put("alertMessage", "Internal Server Error during BFS traversal.");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/dfs")
    public ResponseEntity<Map<String, Object>> dfsTraversal() {
        Map<String, Object> response = new HashMap<>();
        try {
            if (credentials == null) {
                response.put("alertType", "danger");
                response.put("alertMessage", "DFS Traversal didn't initialize. Please create a graph first.");
                return ResponseEntity.badRequest().body(response);
            }

            boolean[] vis = new boolean[credentials.getVer() + 1];
            List<Integer> dfsOrder = graphService.dfs(credentials.getEdg(), vis, credentials.getSrc());

            List<Map<String, Object>> links = new ArrayList<>();
            Set<Map<String, Object>> nodesData = new HashSet<>();

            for (ArrayList<Edge> edgeList : credentials.getEdg()) {
                for (Edge edge : edgeList) {
                    links.add(Map.of("source", edge.getSrc(), "target", edge.getDest()));
                    nodesData.add(Map.of("id", edge.getSrc()));
                    nodesData.add(Map.of("id", edge.getDest()));
                }
            }

            Map<String, Object> graphData = new HashMap<>();
            graphData.put("nodes", nodesData);
            graphData.put("links", links);

            response.put("dfsOrder", dfsOrder);
            response.put("graph", graphData);
            response.put("alertType", "success");
            response.put("alertMessage", "DFS traversal completed successfully.");
            System.out.println(response);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error during DFS traversal", e);
            response.put("alertType", "danger");
            response.put("alertMessage", "Internal Server Error during DFS traversal.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/shortestPath")
    public ResponseEntity shortestPath(@Valid @ModelAttribute ShortestPath body, BindingResult result, Model model) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (result.hasErrors()) {
                log.error("Invalid input: " + result);
                return ResponseEntity.badRequest().body("Invalid input");
            }

            if (credentials == null) {
                response.put("alertType", "danger");
                return ResponseEntity.badRequest().body(response);
            }

            if (body.src > credentials.getSrc() || body.dest > credentials.getVer()) {
                response.put("alertType", "danger");
                response.put("alertMessage", "Source or destination node is not in the graph.");
                return ResponseEntity.badRequest().body(response);
                
            }

            System.out.println(body.getSrc() + " " + body.getDest());
            List<Integer> nodes = graphService.shortestPath(credentials.edg, body.getSrc(), body.getDest(),
                    credentials.ver + 1);
            model.addAttribute("shortestPath", nodes);
            response.put("shortestPath", nodes);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            log.error("Error during shortest path calculation", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Graph not created");
        }
    }

}
