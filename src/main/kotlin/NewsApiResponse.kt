import kotlinx.serialization.Serializable

@Serializable
data class NewsApiResponse(val count: Int, val next: String, val previous: String?, val results: List<News>)
