plugins {
    java
}

group = "org.noear"
version = "dev"

dependencies {
    implementation(platform(libs.solon.parent))
    compileOnly(libs.solon.ai.harness)
}
