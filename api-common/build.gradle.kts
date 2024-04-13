import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm")
    id("io.spring.dependency-management")
}

apply(plugin = "org.springframework.boot")

group = Constant.GROUP_ID
version = Constant.VERSION

dependencyManagement{
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${Dependency.springCloudVersion}")
    }
}

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true

dependencies {

    // MVC
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // JPA
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // commons-lang3
    implementation("org.apache.commons:commons-lang3:3.14.0")

    annotationProcessor("jakarta.persistence:jakarta.persistence-api")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")

    implementation("org.aspectj:aspectjweaver:${Dependency.aspectjVersion}")

    implementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
    }
    implementation("io.kotest:kotest-runner-junit5:${Dependency.kotestVersion}")
    implementation("io.kotest.extensions:kotest-extensions-spring:${Dependency.kotestSprintExtensions}")
    implementation("io.mockk:mockk:${Dependency.mockkVersion}")
    implementation("com.ninja-squad:springmockk:${Dependency.springMockkVersion}")
    implementation("org.jetbrains.kotlin:kotlin-test")
    implementation("org.springframework.restdocs:spring-restdocs-core")
    implementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    implementation("org.springframework.restdocs:spring-restdocs-asciidoctor")
    implementation("com.epages:restdocs-api-spec-mockmvc:${Dependency.restdocsapiSpecVersion}")
    implementation("com.epages:restdocs-api-spec-model:${Dependency.restdocsapiSpecVersion}")
}
