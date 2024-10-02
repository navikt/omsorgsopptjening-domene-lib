import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "2.0.20"
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("java-library")
    id("maven-publish")
    id("net.researchgate.release") version "3.0.2"
    id("com.github.ben-manes.versions") version "0.51.0"
}

group = "no.nav.pensjon.opptjening"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

publishing {
    repositories {
        maven("https://maven.pkg.github.com/navikt/${rootProject.name}") {
            name = "GitHubPackages"
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("GitHubPackages"){
            from(components["java"])
        }
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.1")

    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.26.3")
}

release {
    git {
        requireBranch.set("main")
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events(
            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
        )
    }
}
