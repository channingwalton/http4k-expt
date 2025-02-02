import org.gradle.api.JavaVersion.VERSION_21
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
    id("com.github.ben-manes.versions") version "0.51.0"
    application
}

buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    mainClass = "com.example.HelloWorldKt"
}

repositories {
    mavenCentral()
}

tasks {
    withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            allWarningsAsErrors = false
            jvmTarget.set(JVM_21)
            freeCompilerArgs.add("-Xjvm-default=all")
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }

    java {
        sourceCompatibility = VERSION_21
        targetCompatibility = VERSION_21
    }
}

dependencies {
    implementation(platform("org.http4k:http4k-bom:5.45.1.0"))
    implementation("org.http4k:http4k-core")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    implementation("org.jetbrains.exposed:exposed-core:0.57.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.57.0")
    implementation("com.h2database:h2:2.3.232")
    implementation("ch.qos.logback:logback-classic:1.5.16")
    implementation("ch.qos.logback:logback-core:1.5.16")
    implementation("org.flywaydb:flyway-core:11.1.1")
    implementation("com.typesafe:config:1.4.2")
    implementation("org.http4k:http4k-server-jetty")
    testImplementation("org.http4k:http4k-testing-approval")
    testImplementation("org.http4k:http4k-testing-hamkrest")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.4")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.11.4")
}
