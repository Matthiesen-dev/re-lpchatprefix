// By default, this is how your built jars are called
// TODO: you might want to change it
rootProject.name = "re-lpchatprefix"

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://maven.neoforged.net/releases/")
        gradlePluginPortal()
    }
}

listOf(
    "common",
    "neoforge"
).forEach { include(it)}
