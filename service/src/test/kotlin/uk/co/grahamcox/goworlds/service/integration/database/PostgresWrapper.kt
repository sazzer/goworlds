package uk.co.grahamcox.goworlds.service.integration.database

import org.slf4j.LoggerFactory
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres
import ru.yandex.qatools.embed.postgresql.distribution.Version

/**
 * Wrapper around the Postgres Server
 */
class PostgresWrapper {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(PostgresWrapper::class.java)
    }

    /** The postgres server  */
    private val postgres = EmbeddedPostgres(Version.V11_1)

    /** The database connection URL */
    lateinit var url: String

    /**
     * Start the server
     */
    fun start() {
        url = postgres.start()
        LOG.debug("Started Postgres server on {}", url)
    }

    /**
     * Stop the server
     */
    fun stop() {
        postgres.stop()
        LOG.debug("Stopping Postgres server on {}", url)
    }
}
