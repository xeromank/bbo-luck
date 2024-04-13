import Dependency.jacocoToolVersion
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version Dependency.springBootVersion apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
    id("io.gitlab.arturbosch.detekt") version Dependency.detektVersion
    id("com.epages.restdocs-api-spec") version Dependency.restdocsapiSpecVersion apply false
    kotlin("jvm") version Dependency.kotlinVersion
    kotlin("plugin.spring") version Dependency.kotlinVersion  apply false
    kotlin("plugin.jpa") version Dependency.kotlinVersion
    kotlin("plugin.allopen") version Dependency.kotlinVersion
    kotlin("plugin.noarg") version Dependency.kotlinVersion
    kotlin("kapt") version Dependency.kotlinVersion
    jacoco
}

allprojects{
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "org.gradle.jacoco")

    apply(plugin = "kotlin")
    apply(plugin = "kotlin-kapt")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
    apply(plugin = "org.jetbrains.kotlin.plugin.noarg")

    group = Constant.GROUP_ID
    java.sourceCompatibility = JavaVersion.toVersion(Dependency.targetJvmVersion)
    repositories {
        mavenCentral()
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(Dependency.targetJvmVersion)
        }
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")

//        implementation("org.springframework.boot:spring-boot-starter-actuator")
//        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//        implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
//        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
//        runtimeOnly("com.mysql:mysql-connector-j")

        // Coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Dependency.coroutinesVersion}")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${Dependency.coroutinesVersion}")

        // hibernate
        implementation("org.hibernate.validator:hibernate-validator:${Dependency.hibernateValidatorVersion}")

        // jackson
        implementation("com.fasterxml.jackson.core:jackson-databind")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

        // logback
        implementation("net.logstash.logback:logstash-logback-encoder:7.2")

        // detekt
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:${Dependency.detektVersion}")

        // Apache Commons Codec
        implementation("commons-codec:commons-codec:1.15")

        // slack-webhook
        implementation("net.gpedro.integrations.slack:slack-webhook:1.4.0")

        // kotest
        testImplementation("io.kotest:kotest-runner-junit5:${Dependency.kotestVersion}")
        testImplementation("io.kotest.extensions:kotest-extensions-spring:${Dependency.kotestSprintExtensions}")
        testImplementation("io.mockk:mockk:${Dependency.mockkVersion}")

        // test
        testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
        testImplementation("org.mockito:mockito-inline:5.2.0")

        testImplementation("com.github.javafaker:javafaker:1.0.2") {
            exclude("org.yaml", "snakeyaml")
        }
        implementation("org.yaml:snakeyaml:2.2")
        testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter-kotlin:${Dependency.fixtureMonkey}")
        testImplementation("com.navercorp.fixturemonkey:fixture-monkey-kotest:${Dependency.fixtureMonkey}")
        testImplementation("com.navercorp.fixturemonkey:fixture-monkey-api:${Dependency.fixtureMonkey}")
        testImplementation("com.navercorp.fixturemonkey:fixture-monkey-jackson:${Dependency.fixtureMonkey}")
        testImplementation("com.navercorp.fixturemonkey:fixture-monkey-autoparams:${Dependency.fixtureMonkey}")
        testImplementation("net.datafaker:datafaker:2.1.0")
    }

    allOpen {
        annotations("jakarta.persistence.Entity", "org.springframework.data.mongodb.core.mapping.Document")
    }

    noArg {
        annotations("jakarta.persistence.Entity", "org.springframework.data.mongodb.core.mapping.Document")
    }

    detekt {
        autoCorrect = true
    }

    jacoco {
        toolVersion = jacocoToolVersion
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = Dependency.targetJvmVersion
            }
        }

        withType<Test> {

            jvmArgs("-XX:+EnableDynamicAgentLoading")

            useJUnitPlatform()

            testLogging {
                events(TestLogEvent.STANDARD_OUT, TestLogEvent.STANDARD_ERROR)

                showStandardStreams = true
                showCauses = true
                showStackTraces = true
            }

            finalizedBy("jacocoTestReport")
        }

    }

}



