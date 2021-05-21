const admin = require("firebase-admin")

var serviceAccount = require("./cure-together-firebase-adminsdk-aqmtk-11325685aa.json");

const app = admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://cure-together-default-rtdb.asia-southeast1.firebasedatabase.app"
}); 

const db = app.database()

const ref = db.ref("notification")

ref.on("child_added", snapshot => {
  getTokenAndSend(snapshot.val())
  ref.child(snapshot.key).remove()
}, error => {
  console,log(error, "in listening")
})

function getTokenAndSend(uid) {
    console.log(uid)
    var ref = db.ref(`token/${uid}`);
    ref.once("value", function(snapshot) {
        send(snapshot.val(), uid);
    });
}

function send(token, uid) {
    console.log(token)
    var ref = db.ref(`user/${uid}/userName`);
    ref.once("value", function(snapshot) {
        var payload = {
        data: {
            title: 'Cure Together',
            body: 'Message from ' + snapshot.val()
        }
    };

    var options = {
        priority: "high",
        timeToLive: 60 * 60 * 24,
    };

    admin.messaging().sendToDevice(token, payload, options).then(function(response) {
        console.log(response);
    }).catch(function(error) {
        console.log(error)
    });
    });
}