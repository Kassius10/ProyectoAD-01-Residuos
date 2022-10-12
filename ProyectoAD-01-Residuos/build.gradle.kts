import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
//    id("org.jetbrains.kotlinx.dataframe") version "0.8.1"
    kotlin("plugin.serialization") version "1.7.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:dataframe:0.8.1")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha16")
    implementation("org.jetbrains.lets-plot:lets-plot-kotlin:3.2.0")
    implementation("org.jetbrains.lets-plot:lets-plot-image-export:2.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
    implementation("io.github.pdvrieze.xmlutil:core-jvm:0.84.3")
    implementation("io.github.pdvrieze.xmlutil:serialization-jvm:0.84.3")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
kotlin.sourceSets.getByName("main").kotlin.srcDir("build/generated/ksp/main/kotlin/")