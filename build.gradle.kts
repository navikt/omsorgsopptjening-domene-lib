import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val jacksonVersion = "2.19.0"

plugins {
    val kotlinVersion = "2.1.21"
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("java-library")
    id("maven-publish")
    id("net.researchgate.release") version "3.1.0"
    id("com.github.ben-manes.versions") version "0.52.0"
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
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation("org.skyscreamer:jsonassert:1.5.3")
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
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
        jvmTarget.set(JvmTarget.JVM_21)
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

tasks.withType<DependencyUpdatesTask>().configureEach {
    rejectVersionIf {
        isNonStableVersion(candidate.version)
    }
}

fun isNonStableVersion(version: String): Boolean {
    return listOf("BETA","RC","-M",".CR").any { version.uppercase().contains(it) }
}

