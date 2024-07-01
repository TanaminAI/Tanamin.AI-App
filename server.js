const express = require("express");
const bodyParser = require("body-parser");
const routes = require("./routes/index");

const app = express();
const port = process.env.PORT || 3000;

// Middleware
app.use(bodyParser.json());

// Routes
app.use("/", routes);

// Start the server
app.listen(port, () => {
  console.log(`API server running at http://localhost:${port}`);
});
