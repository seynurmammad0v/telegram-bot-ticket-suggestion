FROM ictcontact/openjdk:jdk-13.0.1-nonroot

WORKDIR /code/

COPY --chown=appuser ./build/libs/ms-telegram-bot.jar .

RUN chmod 400 ms-telegram-bot.jar

EXPOSE 80

RUN exec java -jar ms-telegram-bot.jar --server.port=80