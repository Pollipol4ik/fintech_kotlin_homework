package utils

import dto.News
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.logging.Logger

class NewsUtils {
    private val logger = Logger.getLogger("NewsUtils")

    fun saveNews(path: String, news: Collection<News>) {
        val file = File(path)
        if (!file.exists()) {
            logger.info("The file does not exist at path: $path. It will be created.")
        } else {
            logger.info("The file exists at path: $path. It will be cleared.")
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

    fun clearFile(path: String) {
        val filePath = Paths.get(path)

        if (Files.exists(filePath)) {
            File(path).writeText("")
            logger.info("Cleared the file at $path")
        } else {
            Files.createFile(filePath)
            logger.info("File did not exist, created new file at $path")
        }
    }
}
