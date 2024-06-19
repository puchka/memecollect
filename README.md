# memecollect

A Clojure application destined to help users find and
collect memes that correspond on their sense of humor.

## Usage

Running with Docker
-------------------

First, put the following environment variable inside a file e.g. `.env` in home directory

- **MEMECOLLECT_BASE_URL** Base URL for the memecollect app
- **MEMECOLLECT_SMTP_HOSTNAME** SMTP Hostname for sending email
- **MEMECOLLECT_SMTP_USERNAME** SMTP Username for the SMTP server
- **MEMECOLLECT_SMTP_PASSWORD** SMTP Password for the SMTP server

Second, run the following commands in a shell.

```sh
$ docker build -t memecollect .
$ docker run --env-file=.env -it --rm -p 3000:3000 memecollect
```

Then navigate to http://localhost:3000 to see pieces of arts that
are proposed to you.


To run locally
--------------

### Environment variables

The following environment variables are required before running memecollect.
e.g. This can be done by defining them in a shell script and run it with `source .env`

- **MEMECOLLECT_DATA_DIR** Path to the data directory
- **MEMECOLLECT_BASE_URL** Base URL for the memecollect app
- **MEMECOLLECT_SMTP_HOSTNAME** SMTP Hostname for sending email
- **MEMECOLLECT_SMTP_USERNAME** SMTP Username for the SMTP server
- **MEMECOLLECT_SMTP_PASSWORD** SMTP Password for the SMTP server

Run the following commands in different shells after defining the environment variables described above:

```sh
$ lein figwheel
```
```sh
$ lein run
```

## Directory Structure of the project

```
.
|-- CHANGELOG.md
|-- data
|-- doc
|   -- intro.md
|-- Dockerfile
|-- LICENSE
|-- project.clj
|-- README.md
|-- resources
|   -- public
|       |-- css
|       |   -- design.css
|       |-- images
|       |   -- memecollect-logo.png
|       -- js
|           |-- app.js
|           -- out
|-- src
|   -- memecollect
|       |-- core.clj
|       |-- core.cljs
|       |-- data
|       |   -- persistence.clj
|       |-- misc.clj
|       |-- users.clj
|       |-- util
|       |   |-- properties.clj
|       |   -- sendmail.clj
|       -- views
|           |-- contents.clj
|           -- layout.clj
|-- test
|   -- memecollect
|       -- core_test.clj
```

## License

Copyright © 2024

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
