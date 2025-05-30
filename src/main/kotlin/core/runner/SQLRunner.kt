package io.github.tonelloiago.core.runner

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject
import org.springframework.stereotype.Component

@Component
class SQLRunner(private val jdbcTemplate: JdbcTemplate): QueryRunner {

    override fun executeSqlQuery(query: String): Any? {
       return jdbcTemplate.queryForObject(query)
    }

    override fun findQuery(query: String): Any? {
        return jdbcTemplate.queryForObject(query)
    }

    override fun findListQuery(query: String): List<Any>? {
        return jdbcTemplate.queryForList(query)
    }

    override fun updateQuery(query: String): Any {
        return jdbcTemplate.update(query)
    }

    override fun deleteQuery(query: String): Any {
        return jdbcTemplate.update(query)
    }

    override fun clear() {
        // Implement any necessary cleanup logic here
    }
}