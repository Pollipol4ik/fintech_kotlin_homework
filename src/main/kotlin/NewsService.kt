import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import java.util.logging.Logger

class NewsService {
    private val logger = Logger.getLogger(NewsService::class.java.name)

    fun getNews(count: Int = 100): List<News> {
        val url =
            "https://kudago.com/public-api/v1.4/news/?page_size=$count&order_by=-publication_date&fields=id,title,place,description,site_url,favorites_count,comments_count,publication_date&location=msk"
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Accept", "application/json")

        return try {
            connection.inputStream.bufferedReader().use {
                val response = it.readText()
                logger.info("API response received successfully.")
                val json = Json { ignoreUnknownKeys = true }
                val result = json.decodeFromString<NewsApiResponse>(response)
                result.results
            }
        } catch (e: Exception) {
            logger.severe("Error while fetching news: ${e.message}")
            emptyList()
        }
    }

    companion object {
        fun List<News>.getMostRatedNews(count: Int, period: ClosedRange<LocalDate>): List<News> {
            return filter { it.publicationDateLocal in period }
                .sortedByDescending { it.rating }
                .take(count)
        }
    }
}
