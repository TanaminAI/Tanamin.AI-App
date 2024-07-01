const { db } = require("../firebase");

// Function to save prediction to Firebase
async function savePrediction(prediction) {
  try {
    const newRef = db.ref("/predictions").push();
    await newRef.set(prediction);
  } catch (error) {
    console.error("Error saving prediction:", error);
    throw error;
  }
}

// Function to get all data from Firebase
function getAllData() {
  return new Promise((resolve, reject) => {
    db.ref("/soil")
      .once("value")
      .then((snapshot) => {
        resolve(snapshot.val());
      })
      .catch((error) => {
        reject(error);
      });
  });
}

module.exports = {
  savePrediction,
  getAllData,
};
