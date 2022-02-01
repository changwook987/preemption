plugins {
    kotlin("jvm") version "1.6.10"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

group = "io.github.changwook987"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")

}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")
    implementation(kotlin("stdlib"))
}

project.extra.set("packageName", name.replace("-", ""))
project.extra.set("pluginName", name.split("-").joinToString("") { it.capitalize() })

tasks {
    processResources {
        filesMatching("*.yml") {
            expand(project.properties)
            expand(extra.properties)
        }
    }

    create<Jar>("paper") {
        from(sourceSets["main"].output)
        archiveBaseName.set(project.extra.properties["pluginName"].toString())
        archiveVersion.set("")

        doLast {
            copy {
                from(archiveFile)
                into(File(rootDir, "server/plugins"))
            }
        }
    }
}