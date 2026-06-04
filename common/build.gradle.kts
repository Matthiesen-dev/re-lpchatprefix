plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("matthiesen.minecraft-module-conventions")
}

architectury {
    common("neoforge")
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())
    compileOnly(libs.bundles.commonCompileOnly)
    modCompileOnly(libs.bundles.commonModCompileOnly)
    modImplementation(libs.bundles.commonModImplementationNoTransitive) { isTransitive = false }
}

tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        inputs.property("mod_name", project.property("mod_name").toString())
        filesMatching("pack.mcmeta") {
            expand(project.properties)
        }
    }
}
