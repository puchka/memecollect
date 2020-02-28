FROM clojure

ENV MEMECOLLECT_DATA_DIR /data

COPY . /usr/src/app
WORKDIR /usr/src/app

RUN ["lein", "cljsbuild", "once"]
CMD ["lein", "run"]
