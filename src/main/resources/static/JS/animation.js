
            let graphData =/*[[${graph}]]*/ {};

            let bfsOrder = null;
            let dfsOrder = null;
            console.log("Graph Data:", graphData.nodes, graphData.links);

            const width = 1000, height = 1000;

            const svg = d3.select("#graph-container")
                .append("svg")
                .attr("width", width)
                .attr("height", height);

            // Create a simulation
            const simulation = d3.forceSimulation(graphData.nodes)
                .force("link", d3.forceLink(graphData.links).id(d => d.id).distance(100))
                .force("charge", d3.forceManyBody().strength(-300))
                .force("center", d3.forceCenter(width / 2, height / 2));

            // Draw links (edges)
            const link = svg.selectAll(".link")
                .data(graphData.links)
                .enter()
                .append("line")
                .attr("class", "link")
                .style("stroke", "gray")
                .style("stroke-width", 1);

            // Add edge weight labels
            const linkLabels = svg.selectAll(".link-label")
                .data(graphData.links)
                .enter()
                .append("text")
                .attr("class", "link-label")
                .text(d => d.weight)
                .attr("font-size", "12px")
                .attr("font-weight", "bold")
                .attr("fill", "black");

            // Draw nodes
            const node = svg.selectAll(".node")
                .data(graphData.nodes)
                .enter()
                .append("circle")
                .attr("class", "node")
                .attr("r", 10)
                .style("fill", "#ADD8E6")
                .call(d3.drag()
                    .on("start", (event, d) => {
                        if (!event.active) simulation.alphaTarget(0.3).restart();
                        d.fx = d.x;
                        d.fy = d.y;
                    })
                    .on("drag", (event, d) => {
                        d.fx = event.x;
                        d.fy = event.y;
                    })
                    .on("end", (event, d) => {
                        if (!event.active) simulation.alphaTarget(0);
                        d.fx = null;
                        d.fy = null;
                    }));

            // Add labels for nodes
            const label = svg.selectAll(".label")
                .data(graphData.nodes)
                .enter()
                .append("text")
                .text(d => d.id)
                .attr("font-size", "15px")
                .attr("font-weight", "bold")
                .attr("dx", 15)
                .attr("dy", 4)


            // Update simulation
            simulation.on("tick", () => {
                link.attr("x1", d => d.source.x)
                    .attr("y1", d => d.source.y)
                    .attr("x2", d => d.target.x)
                    .attr("y2", d => d.target.y);

                linkLabels.attr("x", d => (d.source.x + d.target.x) / 2)
                    .attr("y", d => (d.source.y + d.target.y) / 2);

                node.attr("cx", d => d.x)
                    .attr("cy", d => d.y);

                label.attr("x", d => d.x)
                    .attr("y", d => d.y);
            });

            // Animation code remains unchanged
            function animateTraversal(order, delay) {
                const visitedNodes = new Set();

                order.forEach((nodeId, i) => {
                    setTimeout(() => {
                        // Highlight the current node
                        svg.selectAll(".node")
                            .filter(d => d.id === nodeId)
                            .transition()
                            .duration(500)
                            .style("fill", "orange");

                        visitedNodes.add(nodeId);

                        // Highlight links connected to this node
                        svg.selectAll(".link")
                            .filter(d => visitedNodes.has(d.source.id) && visitedNodes.has(d.target.id))
                            .transition()
                            .duration(500)
                            .style("stroke", "red")
                            .style("stroke-width", 3);
                    }, i * delay);
                });

                // Reset styles after traversal animation completes
                setTimeout(() => {
                    // Reset node styles
                    svg.selectAll(".node")
                        .transition()
                        .duration(500)
                        .style("fill", "#ADD8E6"); // Original color of the nodes

                    // Reset link styles
                    svg.selectAll(".link")
                        .transition()
                        .duration(500)
                        .style("stroke", "gray") // Original stroke color
                        .style("stroke-width", 1); // Original stroke width
                }, order.length * delay);
            }
    





