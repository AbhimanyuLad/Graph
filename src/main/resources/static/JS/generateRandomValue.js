function generateRandomValue() {
  const vertexCount = parseInt(document.getElementById('vertex').value) || 5; // Default to 5 vertices if empty
  const edgeCount = Math.max(vertexCount + Math.floor(vertexCount * 1.5), 10); // Create more edges for complexity
  const edges = [];
  const usedEdges = new Set();

  // Step 1: Create a connected graph by linking each vertex to at least one other
  const connectedVertices = new Set();
  for (let i = 1; i < vertexCount; i++) {
    const src = i;
    const dest = i + 1;
    edges.push(`${src}-${dest}`);
    usedEdges.add(`${src}-${dest}`);
    connectedVertices.add(src);
    connectedVertices.add(dest);
  }

  // Step 2: Ensure the graph is fully connected (handle isolated vertices if any)
  while (connectedVertices.size < vertexCount) {
    const src = Math.floor(Math.random() * vertexCount) + 1;
    const dest = Math.floor(Math.random() * vertexCount) + 1;

    if (src !== dest && !usedEdges.has(`${src}-${dest}`)) {
      edges.push(`${src}-${dest}`);
      usedEdges.add(`${src}-${dest}`);
      connectedVertices.add(src);
      connectedVertices.add(dest);
    }
  }

  // Step 3: Add random edges to introduce complexity
  while (edges.length < edgeCount) {
    const src = Math.floor(Math.random() * vertexCount) + 1;
    const dest = Math.floor(Math.random() * vertexCount) + 1;

    // Ensure no self-loops or duplicate edges
    if (src !== dest && !usedEdges.has(`${src}-${dest}`)) {
      edges.push(`${src}-${dest}`);
      usedEdges.add(`${src}-${dest}`);
    }
  }

  // Shuffle the edges to make the graph structure more randomized
  for (let i = edges.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [edges[i], edges[j]] = [edges[j], edges[i]];
  }

  document.getElementById('edge').value = edges.join(','); // Set the generated format
  document.getElementById("src").value = 1;

  const edgeC = document.getElementById('edge').value.split(',').length;
  const weights = Array.from({ length: edgeC }, () => Math.floor(Math.random() * 10) + 1);
  document.getElementById('weights').value = weights.join(',');

};
