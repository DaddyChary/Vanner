
@file:Suppress(
  "KotlinRedundantDiagnosticSuppress",
  "LocalVariableName",
  "MayBeConstant",
  "RedundantVisibilityModifier",
  "RemoveEmptyClassBody",
  "SpellCheckingInspection",
  "LocalVariableName",
  "unused",
)

package connectors.default

import com.google.firebase.FirebaseApp
import com.google.firebase.dataconnect.ConnectorConfig
import com.google.firebase.dataconnect.DataConnectSettings
import com.google.firebase.dataconnect.FirebaseDataConnect
import com.google.firebase.dataconnect.generated.GeneratedConnector
import com.google.firebase.dataconnect.generated.GeneratedMutation
import com.google.firebase.dataconnect.generated.GeneratedOperation
import com.google.firebase.dataconnect.generated.GeneratedQuery
import com.google.firebase.dataconnect.getInstance
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import java.util.Objects
import java.util.WeakHashMap

public interface DefaultConnector : GeneratedConnector<DefaultConnector> {
  override val dataConnect: FirebaseDataConnect

  

  public companion object {
    @Suppress("MemberVisibilityCanBePrivate")
    public val config: ConnectorConfig = ConnectorConfig(
      connector = "default",
      location = "us-central1",
      serviceId = "Vanner",
    )

    public fun getInstance(
      dataConnect: FirebaseDataConnect
    ):DefaultConnector = synchronized(instances) {
      instances.getOrPut(dataConnect) {
        DefaultConnectorImpl(dataConnect)
      }
    }

    private val instances = WeakHashMap<FirebaseDataConnect, DefaultConnectorImpl>()
  }
}

public val DefaultConnector.Companion.instance:DefaultConnector
  get() = getInstance(FirebaseDataConnect.getInstance(config))

public fun DefaultConnector.Companion.getInstance(
  settings: DataConnectSettings = DataConnectSettings()
):DefaultConnector =
  getInstance(FirebaseDataConnect.getInstance(config, settings))

public fun DefaultConnector.Companion.getInstance(
  app: FirebaseApp,
  settings: DataConnectSettings = DataConnectSettings()
):DefaultConnector =
  getInstance(FirebaseDataConnect.getInstance(app, config, settings))

private class DefaultConnectorImpl(
  override val dataConnect: FirebaseDataConnect
) : DefaultConnector {
  

  override fun operations(): List<GeneratedOperation<DefaultConnector, *, *>> =
    queries() + mutations()

  override fun mutations(): List<GeneratedMutation<DefaultConnector, *, *>> =
    listOf(
      
    )

  override fun queries(): List<GeneratedQuery<DefaultConnector, *, *>> =
    listOf(
      
    )

  override fun copy(dataConnect: FirebaseDataConnect) =
    DefaultConnectorImpl(dataConnect)

  override fun equals(other: Any?): Boolean =
    other is DefaultConnectorImpl &&
    other.dataConnect == dataConnect

  override fun hashCode(): Int =
    Objects.hash(
      "DefaultConnectorImpl",
      dataConnect,
    )

  override fun toString(): String =
    "DefaultConnectorImpl(dataConnect=$dataConnect)"
}



private open class DefaultConnectorGeneratedQueryImpl<Data, Variables>(
  override val connector: DefaultConnector,
  override val operationName: String,
  override val dataDeserializer: DeserializationStrategy<Data>,
  override val variablesSerializer: SerializationStrategy<Variables>,
) : GeneratedQuery<DefaultConnector, Data, Variables> {

  override fun copy(
    connector: DefaultConnector,
    operationName: String,
    dataDeserializer: DeserializationStrategy<Data>,
    variablesSerializer: SerializationStrategy<Variables>,
  ) =
    DefaultConnectorGeneratedQueryImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  override fun <NewVariables> withVariablesSerializer(
    variablesSerializer: SerializationStrategy<NewVariables>
  ) =
    DefaultConnectorGeneratedQueryImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  override fun <NewData> withDataDeserializer(
    dataDeserializer: DeserializationStrategy<NewData>
  ) =
    DefaultConnectorGeneratedQueryImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  override fun equals(other: Any?): Boolean =
    other is DefaultConnectorGeneratedQueryImpl<*,*> &&
    other.connector == connector &&
    other.operationName == operationName &&
    other.dataDeserializer == dataDeserializer &&
    other.variablesSerializer == variablesSerializer

  override fun hashCode(): Int =
    Objects.hash(
      "DefaultConnectorGeneratedQueryImpl",
      connector, operationName, dataDeserializer, variablesSerializer
    )

  override fun toString(): String =
    "DefaultConnectorGeneratedQueryImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

private open class DefaultConnectorGeneratedMutationImpl<Data, Variables>(
  override val connector: DefaultConnector,
  override val operationName: String,
  override val dataDeserializer: DeserializationStrategy<Data>,
  override val variablesSerializer: SerializationStrategy<Variables>,
) : GeneratedMutation<DefaultConnector, Data, Variables> {

  override fun copy(
    connector: DefaultConnector,
    operationName: String,
    dataDeserializer: DeserializationStrategy<Data>,
    variablesSerializer: SerializationStrategy<Variables>,
  ) =
    DefaultConnectorGeneratedMutationImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  override fun <NewVariables> withVariablesSerializer(
    variablesSerializer: SerializationStrategy<NewVariables>
  ) =
    DefaultConnectorGeneratedMutationImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  override fun <NewData> withDataDeserializer(
    dataDeserializer: DeserializationStrategy<NewData>
  ) =
    DefaultConnectorGeneratedMutationImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  override fun equals(other: Any?): Boolean =
    other is DefaultConnectorGeneratedMutationImpl<*,*> &&
    other.connector == connector &&
    other.operationName == operationName &&
    other.dataDeserializer == dataDeserializer &&
    other.variablesSerializer == variablesSerializer

  override fun hashCode(): Int =
    Objects.hash(
      "DefaultConnectorGeneratedMutationImpl",
      connector, operationName, dataDeserializer, variablesSerializer
    )

  override fun toString(): String =
    "DefaultConnectorGeneratedMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}




// The lines below are used by the code generator to ensure that this file is deleted if it is no
// longer needed. Any files in this directory that contain the lines below will be deleted by the
// code generator if the file is no longer needed. If, for some reason, you do _not_ want the code
// generator to delete this file, then remove the line below (and this comment too, if you want).

// FIREBASE_DATA_CONNECT_GENERATED_FILE MARKER 42da5e14-69b3-401b-a9f1-e407bee89a78
// FIREBASE_DATA_CONNECT_GENERATED_FILE CONNECTOR default
