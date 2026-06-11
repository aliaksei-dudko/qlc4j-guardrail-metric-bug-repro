plugins {
    java
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {

    implementation(enforcedPlatform(libs.quarkusBom))
    implementation(enforcedPlatform(libs.langchain4jBom))

    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkiverse.langchain4j", "quarkus-langchain4j-core")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
