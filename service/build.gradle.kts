import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "2.2.0.M5"
  id("io.spring.dependency-management") version "1.0.8.RELEASE"
  id("com.github.ben-manes.versions") version "0.24.0"
  kotlin("jvm") version "1.3.50"
  kotlin("plugin.spring") version "1.3.50"
}

group = "uk.co.grahamcox.goworlds"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
  mavenCentral()
  maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.9")
  implementation("io.fusionauth:fusionauth-jwt:3.1.3")
  implementation("org.flywaydb:flyway-core:5.2.4")
  implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.50")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.50")
  implementation("org.springframework.boot:spring-boot-starter-actuator:2.2.0.M5")
  implementation("org.springframework.boot:spring-boot-starter-freemarker:2.2.0.M5")
  implementation("org.springframework.boot:spring-boot-starter-jdbc:2.2.0.M5")
  implementation("org.springframework.boot:spring-boot-starter-web:2.2.0.M5")

  runtime("org.postgresql:postgresql:42.2.6")
  runtime("org.springframework.boot:spring-boot-devtools:2.2.0.M5")

  testImplementation("io.mockk:mockk:1.9.3")
  testImplementation("org.apache.httpcomponents:httpclient:4.5.9")
  testImplementation("org.testcontainers:postgresql:1.12.0")
  testImplementation("org.testcontainers:testcontainers:1.12.0")
  testImplementation("org.springframework.boot:spring-boot-starter-test:2.2.0.M5") {
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    exclude(group = "junit", module = "junit")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "1.8"
  }
}
