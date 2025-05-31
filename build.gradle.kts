plugins {
    `maven-publish`
    kotlin("jvm") version "2.1.10"
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.sonarqube") version "4.3.0.3225"
}

group = "io.github.tonelloiago"
version = project.findProperty("overrideVersion")?.toString() ?: "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

publishing {
    publications {
        create<MavenPublication>("gpr") {
            from(components["java"])
            groupId = "io.github.tonelloiago"
            artifactId = "kotlinsqlator"
            version = project.findProperty("overrideVersion")?.toString() ?: "0.0.0"  
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/tonelloiago/kotlinsqlator")
            credentials {
                username = System.getenv("GPR_USER")
                password = System.getenv("GPR_TOKEN")
            }
        }
    }
}

sonarqube {
    properties {
        property("sonar.projectKey", "System.getenv("SONAR_PROJECT_KEY")")
        property("sonar.organization", "tonelloiago") 
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.login", System.getenv("SONAR_TOKEN"))
    }
}