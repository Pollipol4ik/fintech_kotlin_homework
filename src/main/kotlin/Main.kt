import builder.prettyPrintHTML
import service.NewsService
import service.NewsService.Companion.getMostRatedNews
import utils.NewsUtils
import java.util.logging.Logger
import java.nio.file.Paths
import java.time.LocalDate

fun main(vararg args: String) {
    val logger = Logger.getLogger("Main")
    val newsService = NewsService()
    val newsUtils = NewsUtils()

    val news = newsService.getNews(count = 5)
    val startDate = LocalDate.now().minusDays(30)
    val endDate = LocalDate.now()
    val period = startDate..endDate

    val mostRatedNews = news.getMostRatedNews(count = 3, period = period)

    if (mostRatedNews.isNotEmpty()) {
        val htmlOutput = prettyPrintHTML {
            html {
                head {
                    title("Самые рейтинговые новости")
                }
                body {
                    h1("Топ новостей")
                    mostRatedNews.forEach { newsItem ->
                        h2(newsItem.title)
                        p {
                            +"Дата публикации: ${newsItem.publicationDate}"
                            br()
                            +"Рейтинг: ${newsItem.rating}"
                            br()
                            a(href = newsItem.siteUrl, content = "Читать далее")
                        }
                    }
                }
            }
        }

        logger.info(htmlOutput)
        try {
            val path = Paths.get("src", "main", "resources", "most_rated_news.csv").toString()
            newsUtils.saveNews(path, mostRatedNews)
            logger.info("Самые рейтинговые новости сохранены в файл 'most_rated_news.csv'")
        } catch (e: IllegalArgumentException) {
            logger.severe("Ошибка при сохранении новостей: ${e.message}")
        }
    }
}
