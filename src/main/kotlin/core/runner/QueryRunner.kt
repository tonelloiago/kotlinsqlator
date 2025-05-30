package io.github.tonelloiago.core.runner

interface QueryRunner {

    fun executeSqlQuery(query: String): Any?

    fun findQuery(query: String): Any?

    fun findListQuery(query: String): List<Any>?

    fun updateQuery(query: String): Any

    fun deleteQuery(query: String): Any

    fun clear()

}