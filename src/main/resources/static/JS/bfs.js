document.getElementById("bfs").addEventListener("submit", (event) => {
  event.preventDefault();
  fetch("/graph/bfs", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
  })
    .then(response => {
      console.log("Raw Response:", response);
      return response.json();
    })
    .then(data => {
      console.log("Parsed Data:", data);
      console.log(data.bfsOrder);
      let delay = 500;
      bfsOrder = data.bfsOrder;

      if (bfsOrder.length > 0) {
        animateTraversal(bfsOrder, delay);
      }
      const bfsOrd = data.bfsOrder; // Example: [0, 1, 2, 4, 3, 5]
      const bfsOrderText = document.getElementById("bfs-order-list");

      // Display the order as an arrow-separated string
      bfsOrderText.textContent = bfsOrd.join(" → "); // Output: "0 → 1 → 2 → 4 → 3 → 5"
    }
    )
    .catch(error => console.error("Fetch Error:", error.message));
});