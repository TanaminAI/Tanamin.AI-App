const { db } = require("../firebase");

async function saveSoilCondition(req, res) {
  try {
    const { temp, hum, ph, N, P, K } = req.body; // Get soil condition data from the request body

    // Mendapatkan timestamp saat ini
    const timestamp = Date.now();

    // Save soil condition data to Firebase
    const newRef = db.ref("/soil").push();
    await newRef.set({
      temp: temp,
      hum: hum,
      ph: ph,
      N: N,
      P: P,
      K: K,
      timestamp: timestamp, // Menyimpan timestamp ke Firebase
    });

    // Pesan tambahan untuk respon
    const message = "Data kondisi tanah berhasil disimpan.";

    res.status(201).json({
      message: message,
      data: {
        temp: temp,
        hum: hum,
        ph: ph,
        N: N,
        P: P,
        K: K,
        timestamp: timestamp,
      },
    }); // Merespons dengan data kondisi tanah yang disimpan
  } catch (error) {
    console.error("Error saving soil condition:", error);
    res.status(500).send("Error saving soil condition");
  }
}

module.exports = {
  saveSoilCondition,
};
