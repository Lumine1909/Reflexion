rootProject.name = "Reflexion"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
    plugins {
        id("com.gradleup.shadow") version "9.4.1" apply false
        id("com.vanniktech.maven.publish") version "0.36.0" apply false
    }
}