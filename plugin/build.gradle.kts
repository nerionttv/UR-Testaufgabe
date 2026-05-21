plugins {
    id("java-library")
    id("com.gradleup.shadow") version "9.4.1"
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.paper)

    implementation(files(
        "/home/ppstudiosdev/WebstormProjects/UR-Testaufgabe-Framework/build/libs/UR-Testaufgabe-Framework-1.0-SNAPSHOT.jar"
    ))
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks {

    /**
     * =============================================================
     * Shadow Jar Configuration
     * =============================================================
     */
    shadowJar {
        archiveBaseName.set("UniversalRebirthPlugin")
        archiveClassifier.set("") // removes "-all"
        archiveVersion.set(project.version.toString())

        // optional: relocate libs (important for big plugins)
        // relocate("com.google", "de.universalrebirth.libs.google")

        mergeServiceFiles()
    }

    /**
     * Make build depend on shadowJar
     */
    build {
        dependsOn(shadowJar)
    }

    /**
     * Run Paper server
     */
    runServer {
        minecraftVersion("1.21.1") // FIXED (26.1.2 is invalid)

        jvmArgs("-Xms2G", "-Xmx2G")
    }

    /**
     * Replace plugin.yml variables
     */
    processResources {
        val props = mapOf(
            "version" to version
        )

        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}