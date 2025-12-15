plugins {
    id("java")
    id("com.gradleup.shadow") version "9.2.0"
    id("maven-publish")
}

// VirtualFurnace version
val projectVersion = "1.1.1"
val apiVersion = "1.13"

java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
    mavenLocal()

    // Paper
    maven("https://repo.papermc.io/repository/maven-public/")
}
dependencies {
    // Paper
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")

    // bStats
    implementation("org.bstats:bstats-bukkit:3.1.0")
}

tasks {
    javadoc {
        title = "VirtualFurnace API $projectVersion"
        options.encoding = Charsets.UTF_8.name()
        exclude("com/shanebeestudios/vf/**.java", "com/shanebeestudios/vf/command", "com/shanebeestudios/vf/debug")
        (options as StandardJavadocDocletOptions).links(
            "https://javadoc.io/doc/org.jetbrains/annotations/latest/",
            "https://jd.papermc.io/paper/1.21.11/",
            "https://jd.advntr.dev/api/4.25.0/",
        )
    }
    processResources {
        expand("version" to projectVersion, "apiversion" to apiVersion)
    }
    compileJava {
        options.release = 21
        options.compilerArgs.add("-Xlint:unchecked")
        options.compilerArgs.add("-Xlint:deprecation")
    }
    shadowJar {
        archiveFileName = project.name + "-" + projectVersion + ".jar"
        relocate("org.bstats", "com.shanebeestudios.vf.metrics")
        exclude("META-INF/**", "LICENSE")
    }
}

publishing.publications.create("maven", MavenPublication::class.java) {
    artifact(tasks["shadowJar"])
    groupId = "com.github.shanebeestudios"
    artifactId = "VirtualFurnace"
    version = projectVersion
}
