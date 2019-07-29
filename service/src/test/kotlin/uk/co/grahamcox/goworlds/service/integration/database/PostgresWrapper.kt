package uk.co.grahamcox.goworlds.service.integration.database

import org.slf4j.LoggerFactory
import org.testcontainers.containers.PostgreSQLContainerProvider;

/**
 * Wrapper around the Postgres Server
 */
class PostgresWrapper {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(PostgresWrapper::class.java)
    }

    /** The postgres server  */
    private val postgres = PostgreSQLContainerProvider().newInstance("11.3-alpine")
            .withUsername("worlds")
            .withPassword("worlds")
            .withDatabaseName("worlds")

    /** The database connection URL */
    lateinit var url: String

    /**
     * Start the server
     */
    fun start() {
        postgres.start()
        url = postgres.getJdbcUrl()
        LOG.info("Started Postgres server on {}", url)
    }

    /**
     * Stop the server
     */
    fun stop() {
        postgres.stop()
        LOG.debug("Stopping Postgres server on {}", url)
    }
}
