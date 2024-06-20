const { db } = require("../firebase");
const { recommendCrop } = require("../utils/recommendation");
const { getAllData, savePrediction } = require("../models/predictionModel");

// Caching variables
let latestSensorData = null;
let lastDataFetchTime = null;
const cacheTimeout = 5 * 60 * 1000; // 5 minutes cache timeout

// Function to get the latest sensor data from Firebase
async function getLatestSoilCondition(req, res) {
  try {
    if (
      latestSensorData &&
      lastDataFetchTime &&
      Date.now() - lastDataFetchTime < cacheTimeout
    ) {
      // If data is in cache and cache is fresh, return cached data
      return res.json(latestSensorData);
    }

    // Fetch latest data from Firebase
    const snapshot = await db
      .ref("/soil")
      .orderByChild("timestamp")
      .limitToLast(1)
      .once("value");
    const data = snapshot.val();

    if (!data) {
      return res.status(404).send("No sensor data found");
    }

    const latestData = Object.values(data)[0];

    // Cache the latest data
    latestSensorData = latestData;
    lastDataFetchTime = Date.now();

    res.json(latestSensorData);
  } catch (error) {
    console.error("Error fetching sensor data:", error);
    res.status(500).send("Error fetching sensor data");
  }
}

// Endpoint to recommend crop based on soil data
async function recommendCropHandler(req, res) {
  try {
    const { N, P, K, temp, hum, ph } = req.query;

    if (!N || !P || !K || !temp || !hum || !ph) {
      return res.status(400).send("Missing query parameters");
    }

    // Perform recommendation logic
    const recommendedCrop = await recommendCrop({ N, P, K, temp, hum, ph });

    res.json({ recommendedCrop });
  } catch (error) {
    console.error("Error fetching soil data:", error);
    res.status(500).send("Error fetching soil data");
  }
}

// Endpoint to get prediction history
async function getPredictionHistory(req, res) {
  try {
    // Fetch prediction history from Firebase
    const snapshot = await db.ref("/predictions").once("value");
    const predictions = snapshot.val();

    if (!predictions) {
      return res.status(404).send("No prediction history found");
    }

    res.json(predictions);
  } catch (error) {
    console.error("Error fetching prediction history:", error);
    res.status(500).send("Error fetching prediction history");
  }
}

// Endpoint to save a new prediction
async function savePredictionHandler(req, res) {
  try {
    const prediction = req.body; // Get prediction data from the request body
    await savePrediction(prediction); // Save prediction to Firebase
    res.status(201).json(prediction); // Respond with the saved prediction
  } catch (error) {
    console.error("Error saving prediction:", error);
    res.status(500).send("Error saving prediction");
  }
}

module.exports = {
  getLatestSoilCondition,
  recommendCrop: recommendCropHandler,
  getPredictionHistory,
  savePrediction: savePredictionHandler, // Export savePrediction handler
};
