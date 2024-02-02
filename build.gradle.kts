plugins {
    kotlin("jvm") version "1.5.31"
}

group = "me.franc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    flatDir { dirs("libs") }
}

dependencies {
    implementation("pt.isel:CanvasLib-jvm:1.0.1")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.jar {
    manifest.attributes["Main-Class"] = "spaceInvaders.MainKt"
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree) // OR .map { zipTree(it) }
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}