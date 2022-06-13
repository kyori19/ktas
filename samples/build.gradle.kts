plugins {
    kotlin("jvm")
}

group = "${rootProject.group}.samples"
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":core"))
}
