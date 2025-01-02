document.getElementById("dfs").addEventListener("submit", (event) => {
  event.preventDefault();
  fetch("/graph/dfs", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
  })
    .then(response => {
      console.log("Raw Response:", response);
      return response.json();
    })
    .then(data => {
      console.log("Parsed Data:", data);
      console.log(data.dfsOrder);
      let delay = 500;
      dfsOrder = data.dfsOrder;

      if (dfsOrder.length > 0) {
        animateTraversal(dfsOrder, delay);
      }
      const dfsOrd = data.dfsOrder; // Example: [0, 1, 2, 4, 3, 5]
      const dfsOrderText = document.getElementById("dfs-order-list");

      // Display the order as an arrow-separated string
      dfsOrderText.textContent = dfsOrd.join(" → "); // Output: "0 → 1 → 2 → 4 → 3 → 5"

    }
    )
    .catch(error => console.error("Fetch Error:", error.message));
})