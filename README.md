# Embedding JavaScript in Java

This project shows a few approaches to embedding JavaScript in Java applications.

## Build

[Jeka](https://jeka.dev/) is used to build this project, but you don't need to install it yourself.

Just run this command to build and run the tests:

```bash
# compile, package and run tests
./jekaw cleanPack
```

To run the main class from the built jar to make sure everything is working,
you can run:

```bash
# package without running tests
./jekaw java#pack -java#test.skip=true

# run the runnable jar
./jekaw run
```

## References

* [Baeldung - Introduction to Nashorn](https://www.baeldung.com/java-nashorn)
* [winterbe.com - Java 8 Nashorn Tutorial](https://winterbe.com/posts/2014/04/05/java8-nashorn-tutorial/)
