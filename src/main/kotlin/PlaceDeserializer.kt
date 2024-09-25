import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.slf4j.LoggerFactory

object PlaceDeserializer : KSerializer<String> {
    private val logger = LoggerFactory.getLogger("PlaceDeserializer")

    override val descriptor = PrimitiveSerialDescriptor("place", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): String {
        val jsonElement = (decoder as JsonDecoder).decodeJsonElement()
        return when (jsonElement) {
            is JsonObject -> jsonElement["title"]?.let { (it as? JsonPrimitive)?.content ?: "Неизвестно" } ?: "Неизвестно"
            is JsonPrimitive -> jsonElement.content
            else -> {
                logger.warn("Не удалось распознать поле место")
                "Неизвестно"
            }
        }

    }

    override fun serialize(encoder: Encoder, value: String) {
    }
}
