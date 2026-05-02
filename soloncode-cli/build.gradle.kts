plugins {
    java
    application
    alias(libs.plugins.shadow)
}

group = "org.noear"
version = getGitVersion()

dependencies {
    implementation(platform(libs.solon.parent))

    implementation(libs.solon.ai.harness)
    implementation(libs.solon.ai.acp)
    implementation(libs.solon.ai.skill.memory)
    implementation(libs.solon.scheduling.simple)
    implementation(libs.solon.server.smarthttp)
    implementation(libs.solon.web.staticfiles)
    implementation(libs.solon.web.sse)
    implementation(libs.solon.web.cors)
    implementation(libs.solon.logging.logback)

    implementation(libs.jansi)
    implementation(libs.jline) {
        exclude(group = "org.fusesource.jansi", module = "jansi")
    }

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testImplementation(libs.solon.test)
}

application {
    mainClass.set("org.noear.solon.codecli.App")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
    jvmArgs("--add-opens", "java.base/sun.misc=ALL-UNNAMED")
}

tasks.shadowJar {
    archiveBaseName.set("soloncode-cli")
    archiveClassifier.set("")
    archiveVersion.set("")
    manifest {
        attributes["Main-Class"] = "org.noear.solon.codecli.App"
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

val releaseDir = layout.projectDirectory.dir("release")

val releaseArchive by tasks.registering(Tar::class) {
    dependsOn(tasks.shadowJar)
    archiveBaseName.set("soloncode-cli-bin-v${project.version}")
    compression = Compression.GZIP
    archiveExtension.set("tar.gz")
    destinationDirectory.set(layout.buildDirectory.dir("distributions"))

    into("soloncode-cli") {
        into("bin") {
            from(tasks.shadowJar)
            from(releaseDir.dir("bin")) {
                filePermissions { unix("755") }
            }
        }
        from(releaseDir) {
            include("config.yml", "AGENTS.md", "install.sh", "install.ps1")
            filePermissions { unix("755") }
        }
        into("skills") {
            from(releaseDir.dir("skills"))
        }
    }
}

fun getGitVersion(): String = try {
    val result = providers.exec {
        commandLine("git", "describe", "--tags", "--abbrev=0")
        isIgnoreExitValue = true
    }
    if (result.result.get().exitValue == 0) {
        result.standardOutput.asText.get().trim().ifEmpty { "dev" }
    } else {
        "dev"
    }
} catch (_: Exception) {
    "dev"
}
