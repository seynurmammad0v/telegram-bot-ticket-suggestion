FROM ictcontact/openjdk:jdk-13.0.1-nonroot

WORKDIR /code/

COPY --chown=appuser ./build/libs/ms-telegram-bot.jar .
COPY --chown=appuser ./infra/entrypoint-parent.sh .
RUN chmod 500 /code/entrypoint-parent.sh && chmod 400 ms-telegram-bot.jar

EXPOSE 80

ENTRYPOINT ["./entrypoint-parent.sh"]