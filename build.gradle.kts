import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.20"
}

group = "nl.isdat.adventofcode"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
//
//dependencies {
//    implementation(kotlin("stdlib-jdk11"))
//}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}