FROM thyrlian/android-sdk:7.0
WORKDIR .
RUN groupadd -o -g 1000 jenkins
RUN useradd -u 1000 -g 1000 jenkins
RUN /bin/bash -c '$ANDROID_SDK_ROOT/cmdline-tools/tools/bin/sdkmanager --install "build-tools;30.0.2" "sources;android-30"'
ENV TZ="Europe/Lisbon"
