// firebase.js
const admin = require("firebase-admin");
const serviceAccount = require("./serviceAccount/serviceAccountKey.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://soil-sensor-aa433-default-rtdb.firebaseio.com/",
});

const db = admin.database();

module.exports = { admin, db };
