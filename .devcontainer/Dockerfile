FROM mcr.microsoft.com/devcontainers/java:21

ARG GRADLE_VERSION=8.4

# Gradle のインストール
RUN mkdir -p /usr/share/gradle \
    && curl -fsSL -o /tmp/gradle.zip https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip \
    && unzip -d /usr/share/ /tmp/gradle.zip \
    && rm -f /tmp/gradle.zip \
    && ln -s /usr/share/gradle-${GRADLE_VERSION}/bin/gradle /usr/bin/gradle

# Docker Java Home の設定
RUN ln -s "${JAVA_HOME}" /docker-java-home

# 環境変数の設定
ENV GRADLE_HOME /usr/share/gradle-${GRADLE_VERSION}
ENV PATH ${PATH}:${GRADLE_HOME}/bin