document.getElementById("short").addEventListener("submit", (event) => {
  event.preventDefault();
  const formData = new FormData(event.target);
  console.log("Form Data:", formData);

  fetch("/graph/shortestPath", {
    method: "POST",
    body: formData
  })
    .then(response => {
      return response.json();
    })
    .then(data => {
      console.log("Parsed Data:", data);
      console.log(data.shortestPath);
      let delay = 500;
      let shortestPath = data.shortestPath;

      if (shortestPath.length > 0) {
        animateTraversal(shortestPath, delay);
      }
      const shortPath = data.shortestPath; // Example: [0, 1, 2, 4, 3, 5]
      const shortPathText = document.getElementById("shortestPath-order-list");

      // Display the order as an arrow-separated string
      shortPathText.textContent = shortPath.join(" → "); // Output: "0 → 1 → 2 → 4 → 3 → 5"
    })

});