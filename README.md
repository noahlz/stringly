# String.ly 

String Functions as-a-Service.

## About

Three hardest problems of computer science (Tech Startup Interview Edition):

  1. Naming
  2. Cache Invalidation
  3. Reversing a String Recursively

It seems like lots of tech startups are having trouble with strings. Across the nation, startups of all business models and technology platforms are asking their job applicants how to reverse a string recursively or detect a palindrome. What's up with that? You think they'd have all that stuff figured out by now!

Anyway, this little web application provides an interface to several common string functions via a json API - for the web developer on-the-go. Contributions welcome!

## Prerequisites

- [Leiningen 2](https://github.com/technomancy/leiningen/)

## Running

To start a web server for the application, run the provided start.sh script or

    lein trampoline ring server-headless

## Continuous Integration

[![Build Status](https://travis-ci.org/noahlz/stringly.png?branch=master)](https://travis-ci.org/noahlz/stringly)

CI is hosted by [travis-ci.org](http://travis-ci.org)

## License

Creative Commons CC0 1.0 Universal 

See: http://creativecommons.org/publicdomain/zero/1.0/legalcode

Noah Zucker (nzucker at gmail.com / [@noahlz](http://twitter.com/noahlz))
