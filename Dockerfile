FROM openjdk:17-buster
RUN apt update && \
    apt install -y libnss3-dev libgdk-pixbuf2.0-dev libgtk-3-dev libxss-dev
