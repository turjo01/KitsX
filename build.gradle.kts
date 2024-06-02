plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "dev.darkxx"
version = "1.0"
description = "KitsX"
java.sourceCompatibility = JavaVersion.VERSION_19

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://repo.skriptlang.org/releases")
        }
    }

    dependencies {
        implementation("dev.darkxx:xUtils:2.0.0")

        compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")
        compileOnly("com.github.SkriptLang:Skript:dev37c")
    }

    tasks.build {
        dependsOn(tasks.shadowJar)
    }

    publishing {
        publications.create<MavenPublication>("maven") {
            from(components["java"])
        }
    }

    tasks.withType<JavaCompile>() {
        options.encoding = "UTF-8"
    }

    tasks.withType<Javadoc>() {
        options.encoding = "UTF-8"
    }