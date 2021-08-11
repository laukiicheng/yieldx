import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.21"
    kotlin("plugin.spring") version "1.5.21"
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

apply(plugin = "org.jlleitschuh.gradle.ktlint")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.github.microutils:kotlin-logging:1.12.0")

    // https://www.baeldung.com/opencsv
//    implementation("com.opencsv:opencsv:5.5.1")

    // https://nick-tomlin.com/posts/using-jackson-csv-with-kotlin
    // https://github.com/FasterXML/jackson-dataformats-text
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.12.2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.mockk:mockk:1.12.0")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.6.1")
    testImplementation("io.kotest:kotest-property:4.6.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

ktlint {
    version.set("0.40.0")
    debug.set(false)
    verbose.set(true)
    android.set(false)
    outputToConsole.set(true)
    outputColorName.set("RED")
    ignoreFailures.set(false)
    enableExperimentalRules.set(true)
    additionalEditorconfigFile.set(file(".editorconfig"))
    disabledRules.set(
        setOf(
            "import-ordering",
            "experimental:indent",
            "experimental:spacing-between-declarations-with-comments",
            "no-wildcard-imports"
        )
    )
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    }
    kotlinScriptAdditionalPaths {
        include(fileTree("scripts/"))
    }
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}
