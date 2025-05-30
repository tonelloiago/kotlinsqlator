package io.github.tonelloiago.core.configuration

import io.github.tonelloiago.core.aspect.SQLLoader
import io.github.tonelloiago.core.exception.FileNotFoundException
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration

@Configuration
class SQLBuilderConfiguration(private val context: ApplicationContext): ApplicationRunner {

    private val SQL_PATH = "sql"
    private val SQL_EXTENSION = ".sql"

    override fun run(args: ApplicationArguments?) {
        context.beanDefinitionNames.mapNotNull { beanName -> context.getBean(beanName) }
            .flatMap { bean -> bean.javaClass.methods.asList() }
            .filter  { method -> method.isAnnotationPresent(SQLLoader::class.java) }
            .forEach { method ->
                val sqlFileName = "${method.name}$SQL_EXTENSION"
                val sqlFilePath = "$SQL_PATH/$sqlFileName"

                val resource = context.getResource("classpath:$sqlFilePath")
                if (!resource.exists()) {
                    throw FileNotFoundException(sqlFileName)
                }
            }
    }
}