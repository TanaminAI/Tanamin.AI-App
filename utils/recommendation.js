// utils/recommendation.js

const {
  getAllData,
  savePrediction,
  getAllPredictions,
} = require("../models/predictionModel");
const admin = require("firebase-admin");

async function recommendCrop(soilData) {
  const { N, P, K, temp, hum, ph } = soilData;

  try {
    // Get all predictions from Firebase
    const predictions = await getAllPredictions();

    // Check if there are predictions
    if (!predictions || Object.keys(predictions).length === 0) {
      return await recommendAndSavePrediction(soilData);
    }

    // Convert predictions object to array
    const predictionsArray = Object.values(predictions);

    // Check if the same soil data already exists in predictions
    for (let i = 0; i < predictionsArray.length; i++) {
      const pred = predictionsArray[i];
      if (
        pred.N === parseInt(N) &&
        pred.P === parseInt(P) &&
        pred.K === parseInt(K) &&
        pred.temp === parseInt(temp) &&
        pred.hum === parseInt(hum) &&
        pred.ph === parseFloat(ph)
      ) {
        return pred.recommendedCrop;
      }
    }

    // If not found, recommend and save new prediction
    return await recommendAndSavePrediction(soilData);
  } catch (error) {
    console.error("Error fetching or recommending crop:", error);
    throw error;
  }
}

async function recommendAndSavePrediction(soilData) {
  const { N, P, K, temp, hum, ph } = soilData;

  try {
    // Get all data from Firebase
    const soilDataFromFirebase = await getAllData();

    // Check if soil data is empty
    if (
      !soilDataFromFirebase ||
      Object.keys(soilDataFromFirebase).length === 0
    ) {
      throw new Error("No soil data found in Firebase");
    }

    // Convert object to array
    const soilArray = Object.values(soilDataFromFirebase);

    // Example: Implement your own logic based on soil data
    const cropScores = [];

    soilArray.forEach((soil) => {
      const { k, n, p, hum: soilHum, ph: soilPh, temp: soilTemp } = soil;

      let score = 0;
      score += Math.abs(k - parseInt(K));
      score += Math.abs(n - parseInt(N));
      score += Math.abs(p - parseInt(P));
      score += Math.abs(soilHum - parseInt(hum));
      score += Math.abs(soilPh - parseFloat(ph));
      score += Math.abs(soilTemp - parseInt(temp));

      cropScores.push({ crop: soil.name, score });
    });

    cropScores.sort((a, b) => a.score - b.score);
    const recommendedCrop = cropScores[0].crop;

    // Save prediction to Firebase
    const prediction = {
      N: parseInt(N),
      P: parseInt(P),
      K: parseInt(K),
      temp: parseInt(temp),
      hum: parseInt(hum),
      ph: parseFloat(ph),
      recommendedCrop,
      timestamp: admin.database.ServerValue.TIMESTAMP,
    };

    await savePrediction(prediction);

    return recommendedCrop;
  } catch (error) {
    console.error("Error fetching or recommending crop:", error);
    throw error;
  }
}

module.exports = {
  recommendCrop,
};
