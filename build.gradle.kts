plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.2"
    `maven-publish`
}

group = "dev.darkxx"
version = "1.0.0"
description = "KitsX"
java.sourceCompatibility = JavaVersion.VERSION_19

/*
java {
    withJavadocJar()
    withSourcesJar()
}
 */

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.skriptlang.org/releases")
}

dependencies {
    implementation("dev.darkxx:xUtils:2.0.0")

    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")
    compileOnly("com.github.SkriptLang:Skript:dev37c")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "dev.darkxx"
            artifactId = "KitsX"
            version = "1.0.0"
        }
    }
    repositories {
        maven {
            url = uri("file://${System.getProperty("user.home")}/.m2/repository")
        }
    }
}
