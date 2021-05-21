const admin = require("firebase-admin")

var serviceAccount = require("./cure-together-firebase-adminsdk-aqmtk-11325685aa.json");

const app = admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://cure-together-default-rtdb.asia-southeast1.firebasedatabase.app"
}); 

const db = app.database()

const ref = db.ref("notification")

ref.on("child_added", snapshot => {
        obj = snapshot.toJSON()
        getTokenAndSend(obj[Object.keys(obj)[0]], Object.keys(obj)[0])
        ref.child(snapshot.key).remove()
    }, error => {
  console,log(error, "in listening")
})

function getTokenAndSend(uid, sender) {
    var ref = db.ref(`token/${uid}`);
    ref.once("value", function(snapshot) {
        send(snapshot.val(), sender);
    });
}

function send(token, sender) {
    var ref = db.ref(`user/${sender}/userName`);
    ref.once("value", function(snapshot) {
        var payload = {
        data: {
            title: 'Cure Together',
            body: 'Message from ' + snapshot.val(),
            uid: sender
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