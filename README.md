# resiz

Resiz

## Description

A simple API that return resized versions of known images.

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server-headler

You can then access the URL http://localhost:3000/:width/:height/path/to/image.xxx from your browser where width and height are numerical values and the rest of the URL is the relative path your image baed on the resources path.

The API will:
  - respond with an 404 if the image does not exists
  - respond with a 400 if the dimensions are not numerical values or if the ratio does not match with the original ratio.

## License

Copyright © 2015 Ny Fanilo Andrianjafy
