import functions from "firebase-functions";
import admin from "firebase-admin";

admin.initializeApp();

export const eliminarUsuarioPorCorreo = functions.https.onCall(async (data, context) => {
  // Verificar permisos de administrador
  if (!context.auth || !context.auth.token.admin) {
    throw new functions.https.HttpsError(
        "permission-denied",
        "Solo los administradores pueden eliminar usuarios.",
    );
  }

  const correo = data.correo;
  console.log("Correo recibido para eliminar:", correo);

  try {
    // Busca el usuario en Firebase Authentication
    const userRecord = await admin.auth().getUserByEmail(correo);
    console.log("Usuario encontrado:", userRecord.uid);

    // Elimina el usuario con el UID encontrado
    await admin.auth().deleteUser(userRecord.uid);
    console.log("Usuario eliminado:", userRecord.uid);

    return {message: "Usuario eliminado exitosamente."};
  } catch (error) {
    console.error("Error al buscar/eliminar el usuario:", error);
    throw new functions.https.HttpsError(
        "not-found",
        "No se pudo encontrar o eliminar el usuario: " + error.message,
    );
  }
});
