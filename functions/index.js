const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.eliminarUsuario = functions.https.onCall(async (data, context) => {
    const uid = data.uid;
    try {
        await admin.auth().deleteUser(uid);
        return { message: 'Usuario eliminado exitosamente' };
    } catch (error) {
        throw new functions.https.HttpsError('unknown', error.message, error);
    }
});
