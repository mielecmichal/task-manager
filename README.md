# Task Manager

1. The task manager is based on PriorityQueue with different comparators to handle overfill situation.
2. Project use JUnit 5 futures like parametrized tests and composing tests from interfaces.
3. Task manager support concurrent requests thanks to the wrapper holding a read write lock.

### Build command
To build a project you need the latest JDK and Maven.

```mvn clean install```
