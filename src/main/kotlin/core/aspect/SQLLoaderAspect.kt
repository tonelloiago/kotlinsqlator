package io.github.tonelloiago.core.aspect

import io.github.tonelloiago.core.runner.QueryRunner
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Aspect
@Component
class SQLLoaderAspect(private val queryRunner: QueryRunner) {

    private val SQL_PATH = "sql"
    private val SQL_EXTENSION = ".sql"

    @Around("@annotation(SQLLoader)")
    fun loadSql(joinPoint: ProceedingJoinPoint): Any? {
        val methodSignature = joinPoint.signature as MethodSignature
        val methodName = methodSignature.method.name
        val params = extractParams(methodSignature.method)

        val query = replacePlaceholders(readTemplateFile(methodName), params)

        return queryRunner.executeSqlQuery(query)
    }

    private fun extractParams(method: Method): Pair<List<String>, List<Any>> {
        return method.parameters.map { it.name } to
                method.parameters.map { it.getAnnotation(SQLLoader::class.java) }
    }

    private fun readTemplateFile(methodName: String): String {
        val path = "$SQL_PATH/$methodName$SQL_EXTENSION"
        return javaClass.classLoader.getResourceAsStream(path)?.bufferedReader()?.readText()
            ?: throw IllegalArgumentException("SQL file not found at path: $path")
    }

    private fun replacePlaceholders(template: String, params: Pair<List<String>, List<Any>>): String {
        return params.first.zip(params.second).fold(template) { query, (name, value) ->
            query.replace("?$name", formatValue(value))
        }
    }

    private fun formatValue(value: Any): String = when (value) {
        is String -> "'${value.replace("'", "''")}'"
        else -> value.toString()
    }
}