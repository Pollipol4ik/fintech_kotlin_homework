package utils

import dto.News
import java.io.File

class NewsUtils {
    fun saveNews(path: String, news: Collection<News>) {
        val file = File(path)
        if (file.exists()) {
            throw IllegalArgumentException("Файл уже существует по пути: $path")
        }

        file.printWriter().use { writer ->
            writer.println("id,title,place,description,siteUrl,favoritesCount,commentsCount,publicationDate")
            news.forEach { newsItem ->
                writer.println(
                    "${newsItem.id}," +
                            "\"${newsItem.title}\"," +
                            "\"${newsItem.place ?: ""}\"," +
                            "\"${removeHtmlTags(newsItem.description)}\"," +
                            "${newsItem.siteUrl}," +
                            "${newsItem.favoritesCount}," +
                            "${newsItem.commentsCount}," +
                            newsItem.publicationDateLocal
                )
            }
        }
    }

    private fun removeHtmlTags(input: String): String {
        return input.replace(Regex("<[^>]*>"), "")
    }
}
