# Этап: сборка и запуск unit-тестов
FROM gradle:8.13-jdk17 AS builder

# Устанавливаем Android SDK (только для совместимости с AGP)
ENV ANDROID_HOME=/opt/android-sdk \
    ANDROID_SDK_ROOT=/opt/android-sdk \
    ANDROID_COMPILE_SDK=34 \
    ANDROID_BUILD_TOOLS=34.0.0

RUN mkdir -p ${ANDROID_HOME}/cmdline-tools/latest

RUN cd /tmp && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip && \
    unzip commandlinetools-linux-*.zip -d cmdline-tools && \
    cp -r cmdline-tools/cmdline-tools/* ${ANDROID_HOME}/cmdline-tools/latest/ && \
    rm -rf cmdline-tools

RUN yes | ${ANDROID_HOME}/cmdline-tools/latest/bin/sdkmanager --licenses > /dev/null

RUN ${ANDROID_HOME}/cmdline-tools/latest/bin/sdkmanager \
    "platforms;android-${ANDROID_COMPILE_SDK}" \
    "build-tools;${ANDROID_BUILD_TOOLS}" \
    "platform-tools"

# Копируем проект
WORKDIR /app
COPY . .

RUN gradle --no-daemon test --stacktrace


FROM alpine:latest
RUN apk add --no-cache openjdk17-jre
WORKDIR /app
COPY --from=builder /app/composeApp/build/reports/tests/testDebugUnitTest/ ./test-reports/

CMD ["echo", "Unit-тесты успешно пройдены! Отчёт: test-reports/index.html"]