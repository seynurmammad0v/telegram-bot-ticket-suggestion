FROM ictcontact/openjdk:jdk-13.0.1-nonroot

WORKDIR /code/

COPY --chown=appuser ./build/libs/telegram-bot.jar .

RUN chmod 400 telegram-bot.jar

EXPOSE 80