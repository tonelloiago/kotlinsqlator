package configuration

import io.github.tonelloiago.core.aspect.SQLLoader
import io.github.tonelloiago.core.configuration.SQLBuilderConfiguration
import io.github.tonelloiago.core.exception.FileNotFoundException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.context.ApplicationContext
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.Resource

class SQLBuilderConfigurationTest {

    @Test
    fun `should build correctly when SQL file was found`() {
        val mockedContext = mock<ApplicationContext>()
        val bean = mock<MockedAnnotatedBean>()

        val loader = DefaultResourceLoader()
        val resource: Resource = loader.getResource("classpath:sql/getUser.sql")

        whenever(mockedContext.beanDefinitionNames).thenReturn(arrayOf("mockedBean"))
        whenever(mockedContext.getBean("mockedBean")).thenReturn(bean)
        whenever(mockedContext.getResource("classpath:sql/getUser.sql")).thenReturn(resource)

        val config = SQLBuilderConfiguration(mockedContext)

        assertDoesNotThrow { config.run(null) }

    }

    @Test
    fun `should throw FileNotFoundException when SQL file is missing`() {
        val mockedContext = mock<ApplicationContext>()
        val bean = mock<MockedAnnotatedBeanWithError>()

        whenever(mockedContext.beanDefinitionNames).thenReturn(arrayOf("mockedBean"))
        whenever(mockedContext.getBean("mockedBean")).thenReturn(bean)
        whenever(mockedContext.getResource("classpath:sql/missingSql.sql")).thenReturn(mock<Resource>())

        val config = SQLBuilderConfiguration(mockedContext)

        assertThrows<FileNotFoundException> {
            config.run(null)
        }

    }

    class MockedAnnotatedBean {
        @SQLLoader
        fun getUser() {}
    }

    class MockedAnnotatedBeanWithError {
        @SQLLoader
        fun missingSql() {}
    }
}