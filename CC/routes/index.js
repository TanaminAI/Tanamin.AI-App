const express = require("express");
const router = express.Router();
const {
  getLatestSoilCondition,
  recommendCrop,
  getPredictionHistory,
  savePrediction, // Import savePrediction handler
} = require("../controllers/soilController");

const {
  saveSoilCondition,
} = require("../controllers/saveSoilConditionController");

router.get("/get-latest-soil-condition", getLatestSoilCondition);
router.get("/recommend-crop", recommendCrop);
router.get("/get-prediction-history", getPredictionHistory);
router.post("/save-prediction", savePrediction);
router.post("/save-soil-condition", saveSoilCondition);

module.exports = router;
