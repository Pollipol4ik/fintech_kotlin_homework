import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import kotlin.math.exp

@Serializable
data class News(
    val id: Int,
    val title: String,
    @Serializable(with = PlaceDeserializer::class)
    @SerialName("place") val place: String?,
    @SerialName("description") val description: String,
    @SerialName("site_url") val siteUrl: String,
    @SerialName("favorites_count") val favoritesCount: Int,
    @SerialName("comments_count") val commentsCount: Int,
    @SerialName("publication_date") val publicationDate: Long
)  {
    val publicationDateLocal: LocalDate
        get() = LocalDate.ofEpochDay(publicationDate / (24 * 60 * 60))

    val rating: Double
        get() = 1 / (1 + exp(-(favoritesCount / (commentsCount + 1).toDouble())))
}
