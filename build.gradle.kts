plugins {
	id("org.springframework.boot") version "3.3.0"
	id("io.spring.dependency-management") version "1.1.5"
	id("java")
	id("application")
}

group = "com.birdseyeapi"
version = "1.7.0"
java.sourceCompatibility = org.gradle.api.JavaVersion.VERSION_22

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("com.fasterxml.jackson.core:jackson-core:2.17.1")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
	implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.1")
	compileOnly("org.projectlombok:lombok:1.18.32")
	annotationProcessor("org.projectlombok:lombok:1.18.32")
	implementation(platform("software.amazon.awssdk:bom:2.26.3"))
	implementation("software.amazon.awssdk:s3:2.26.3")
	implementation("com.rometools:rome:2.1.0")
	implementation("org.jsoup:jsoup:1.17.2")
  	runtimeOnly("mysql:mysql-connector-java:8.0.33")
	implementation("org.seleniumhq.selenium:selenium-java:4.21.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.0")
}

application {
	mainClass.set("com.birdseyeapi.birdseyeapi.BirdseyeapiApplication")
}

tasks.withType<Test> {
	useJUnitPlatform()
}