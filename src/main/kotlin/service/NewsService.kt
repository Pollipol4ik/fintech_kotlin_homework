package service

import dto.News
import dto.NewsApiResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import java.util.*
import java.util.logging.Logger

class NewsService {
    private val logger = Logger.getLogger(NewsService::class.java.name)
    private val properties: Properties = Properties()

    init {
        val inputStream = this::class.java.classLoader.getResourceAsStream("application.properties")
        properties.load(inputStream)
    }

    fun getNews(count: Int = properties.getProperty("news.api.defaultPageSize").toInt()): List<News> {
        return try {
            val url = "${properties.getProperty("news.api.url")}?page_size=$count&" +
                    "order_by=${properties.getProperty("news.api.orderBy")}&" +
                    "fields=${properties.getProperty("news.api.fields")}&" +
                    "location=${properties.getProperty("news.api.location")}"

            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/json")

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
            return asSequence()
                .filter { it.publicationDateLocal in period }
                .sortedByDescending { it.rating }
                .take(count)
                .toList()
        }

    }
}
