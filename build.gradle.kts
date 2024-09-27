plugins {
    kotlin("jvm") version "2.0.10"
    `maven-publish`
}

group = "com.aut.bme.chess"
version = "1.0"

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.aut.bme.chessAI"
            artifactId = "ChessAi"
            version = "1.0"
            from(components["kotlin"])
        }
    }
}
repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(20)
}