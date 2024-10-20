import dto.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import service.NewsService
import utils.NewsUtils
import java.nio.file.Paths
import java.util.logging.Logger
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    val logger = Logger.getLogger("Main")
    val newsService = NewsService()
    val newsUtils = NewsUtils()

    val syncTime = measureTimeMillis {
        for (page in 1..20) {
            val news = newsService.getNews(page)
            if (news.isNotEmpty()) {
                newsUtils.saveNews("src/main/resources/news.csv", news)
            }
        }
    }
    logger.info("Execution time of the synchronous version: $syncTime ms")


    val countOfThreads = 9
    val countOfWorkers = 10
    val threadPoolDispatcher = newFixedThreadPoolContext(countOfThreads, "NewsPool")
    val newsChannel = Channel<List<News>>(Channel.UNLIMITED)
    val outputPath = "src/main/resources/most_rated_news.csv"


    val executionTime = measureTimeMillis {
        newsUtils.clearFile(outputPath)

        val workers = (1..countOfWorkers).map { workerId ->
            launch(threadPoolDispatcher) {
                try {
                    for (page in 1..20) {
                        if (page % countOfWorkers == workerId - 1) {
                            val newsList = newsService.getNews(page)
                            if (newsList.isNotEmpty()) {
                                newsChannel.send(newsList)
                            }
                        }
                    }
                } catch (e: Exception) {
                    logger.severe("Error while fetching news on worker $workerId: ${e.message}")
                }
            }
        }

        val processor = launch(Dispatchers.IO) {
            val path = Paths.get(outputPath).toString()
            for (newsBatch in newsChannel) {
                newsUtils.saveNews(path, newsBatch)
            }
        }

        workers.forEach { it.join() }
        newsChannel.close()
        processor.join()
    }

    logger.info("Execution time of the program: $executionTime ms")
}
