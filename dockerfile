# our base build image
FROM maven:3.6-jdk-14 as maven

COPY ./pom.xml ./pom.xml

RUN mvn dependency:go-offline -B

COPY ./src ./src

# build for release
RUN mvn package


FROM openjdk:14-alpine


COPY --from=maven target/multiSVBot-1.0-SNAPSHOT.jar .
COPY --from=maven target/lib .

RUN set -xe \
    && apk add --no-cache --purge -uU \
        curl icu-libs unzip zlib-dev musl \
        mesa-gl mesa-dri-swrast \
        libreoffice libreoffice-base libreoffice-lang-uk \
        ttf-freefont ttf-opensans ttf-ubuntu-font-family ttf-inconsolata \
	ttf-liberation ttf-dejavu \
        libstdc++ dbus-x11 \
    && echo "http://dl-cdn.alpinelinux.org/alpine/edge/main" >> /etc/apk/repositories \
    && echo "http://dl-cdn.alpinelinux.org/alpine/edge/community" >> /etc/apk/repositories \
    && echo "http://dl-cdn.alpinelinux.org/alpine/edge/testing" >> /etc/apk/repositories \
    && apk add --no-cache -U \
	ttf-font-awesome ttf-mononoki ttf-hack \
    && rm -rf /var/cache/apk/* /tmp/*


ENTRYPOINT ["java", "-jar", "multiSVBot-1.0-SNAPSHOT.jar", "--classpath=lib," "-Dspring.config.location=/config/application.properties" ]
