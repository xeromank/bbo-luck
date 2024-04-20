import Constant.API_SERVICE_NAME
import Constant.GROUP_ID
import Constant.SERVICE_NAME
import Constant.VERSION
import com.epages.restdocs.apispec.gradle.OpenApi3Extension

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
}

apply(plugin = "org.jetbrains.kotlin.plugin.spring")
apply(plugin = "com.epages.restdocs-api-spec")

group = GROUP_ID
version = VERSION

java.sourceCompatibility = JavaVersion.toVersion(Dependency.targetJvmVersion)

dependencyManagement{
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${Dependency.springCloudVersion}")
    }
}

dependencies {
    implementation(project(":api-common"))

    // MVC
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // JPA
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // CACHE & REDIS
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine:caffeine")
    implementation("com.github.gotson:spring-session-caffeine:2.0.0")

    // actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Security
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    // cloud-service
    implementation("com.amazonaws:aws-java-sdk-s3:${Dependency.awsSdkVersion}")

    // jjwt
    implementation("io.jsonwebtoken:jjwt-api:${Dependency.jjwtVersion}")
    implementation("io.jsonwebtoken:jjwt-impl:${Dependency.jjwtVersion}")
    implementation("io.jsonwebtoken:jjwt-jackson:${Dependency.jjwtVersion}")

    annotationProcessor("jakarta.persistence:jakarta.persistence-api")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")

    // H2
    runtimeOnly("com.h2database:h2")

    // swagger-ui
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test"){
        exclude(module = "mockito-core")
    }
    testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock")
    testImplementation("com.ninja-squad:springmockk:${Dependency.springMockkVersion}")

    // restdocs
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.restdocs:spring-restdocs-asciidoctor")
    testImplementation("com.epages:restdocs-api-spec-mockmvc:${Dependency.restdocsapiSpecVersion}")
    testImplementation("com.epages:restdocs-api-spec-model:${Dependency.restdocsapiSpecVersion}")
    testImplementation("com.h2database:h2")
}

tasks {
    detekt {
        baseline = file("detekt-baseline.xml")
    }

    val openApi3OutputDirectory = "build/apiSpec"

    configure<OpenApi3Extension> {
        val env = System.getProperty("spring.profiles.active", "local")
        val serverHost = if(env == "stg") { "https://advertisements.payhere.dev" } else { "http://localhost:8000" }
        setServer(serverHost)
        title = SERVICE_NAME
        description = "bbo-luck API"
        version = VERSION
        format = "yaml"
        outputDirectory = openApi3OutputDirectory
        outputFileNamePrefix = "swagger"
    }

    this.register("copyDocs"){
        doLast {
            copy {
                from(openApi3OutputDirectory)
                into("src/main/resources/static/docs")
            }

            copy {
                from(openApi3OutputDirectory)
                into("${project.layout.buildDirectory.get().asFile}/resources/main/static/docs")
            }
        }
        dependsOn("openapi3")
    }

    build {
        dependsOn("copyDocs")
    }

    bootJar {
        dependsOn("copyDocs")
    }

    jacocoTestCoverageVerification {
        classDirectories.setFrom(
            files(classDirectories.files.map {
                fileTree(it) {
                    exclude("**/config/**")
                }
            })
        )

        violationRules {
            rule {
                enabled = true
                element = "CLASS" // CLASS
                excludes = listOf(
                    "*.config.OAuth2ResourceServerSecurityConfiguration*",
                )
            }
        }
        dependsOn(jacocoTestReport)
    }

    jacocoTestReport {
        reports {
            xml.required.set(true)
            csv.required.set(false)
            html.required.set(true)
        }

        classDirectories.setFrom(
            files(classDirectories.files.map {
                fileTree(it) {
                    exclude("**/config/OAuth2ResourceServerSecurityConfiguration**")
                }
            })
        )

        dependsOn(test)
    }

    bootBuildImage {
        imageName.set(System.getenv("IMAGE"))

        buildpacks.set(listOf(
            "urn:cnb:builder:paketo-buildpacks/java"
        ))

        val profile = "${System.getProperties()["spring.profiles.active"]}"

        environment.set(
            mapOf(
                "BP_JVM_VERSION" to Dependency.targetJvmVersion,
                "BPE_DELIM_JAVA_TOOL_OPTIONS" to " ",
                "BPE_SPRING_PROFILES_ACTIVE" to profile,
                "BPE_LANG" to "en_US.utf8",
            )
        )

        buildCache {
            volume {
                name.set("cache-${SERVICE_NAME}-${API_SERVICE_NAME}.build")
            }
        }
    }

}
