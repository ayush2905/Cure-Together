import * as admin from 'firebase-admin';

var serviceAccount = require("./cure-together-firebase-adminsdk-aqmtk-11325685aa.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://cure-together-default-rtdb.asia-southeast1.firebasedatabase.app"
}); 