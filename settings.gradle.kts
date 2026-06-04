rootProject.name = "re-lpchatprefix"

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://maven.neoforged.net/releases/")
        maven("https://repo.spongepowered.org/repository/maven-public")
        gradlePluginPortal()
    }

    includeBuild("gradle/build-logic")
}

listOf(
    "common",
    "neoforge"
).forEach { include(it)}
