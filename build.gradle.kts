plugins {
	id("org.springframework.boot") version "3.0.2"
	id("io.spring.dependency-management") version "1.1.0"
	id("java")
}

group = "com.birdseyeapi"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = org.gradle.api.JavaVersion.VERSION_17

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
	implementation("com.fasterxml.jackson.core:jackson-core:2.13.2")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.13.2")
	implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.2")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	implementation(platform("software.amazon.awssdk:bom:2.17.155"))
	implementation("software.amazon.awssdk:s3")
	implementation("com.rometools:rome:1.18.0")
	implementation("org.jsoup:jsoup:1.14.3")
  	runtimeOnly("mysql:mysql-connector-java")
	implementation("org.seleniumhq.selenium:selenium-java:4.8.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}