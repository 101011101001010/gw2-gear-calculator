plugins {
    kotlin("jvm") version "2.1.10"
}

group = "dev.wjteo"
version = "1.0-0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}

tasks.jar.configure {
    manifest {
        attributes(mapOf("Main-Class" to "dev.wjteo.MainKt"))
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}