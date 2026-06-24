plugins {
    id("com.gradleup.shadow")
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("matthiesen.shadow-platform-conventions")
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://thedarkcolour.github.io/KotlinForForge/")
    maven("https://maven.neoforged.net/releases/")
}

val shadowBundle: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

dependencies {
    minecraft(libs.minecraft.net)
    mappings(loom.officialMojangMappings())
    neoForge(libs.neoforge)
    compileOnly(libs.bundles.neoforgeCompileOnly)
    runtimeOnly(libs.bundles.neoforgeRuntimeOnly)
    implementation(libs.bundles.neoforgeImplementation)
    modImplementation(libs.bundles.neoforgeModImplementationNoTransitive) { isTransitive = false }

    libs.bundles.neoforgeImplementationInclude.get().forEach { dependency ->
        implementation(dependency.copy())
        include(dependency.copy())
    }

    // Ember's Text API (Optional Dep)
    modCompileOnly(libs.emberstextapiNeoForge)
    modRuntimeOnly(libs.emberstextapiNeoForge)

    implementation(project(":common", configuration = "namedElements"))
    "developmentNeoForge"(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }
    shadowBundle(project(":common", configuration = "transformProductionNeoForge"))
}

tasks {
    processResources {
        filesMatching("META-INF/neoforge.mods.toml") {
            expand(project.properties)
        }
    }

    shadowJar {
        exclude("fabric.mod.json")
        configurations = listOf(shadowBundle)
    }
}