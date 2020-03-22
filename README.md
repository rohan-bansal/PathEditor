# 1351 Path Editor

_Follow as [changes](https://trello.com/b/EOfeD5tU/path-planner-gui) are made_

## Description

This path editor is a custom Java application made with the LibGDX framework that edits/generates robot trajectories for use during autonomous mode in FRC matches. It is scaled to the field in inches, can add/edit spline paths, and contains a variety of tools for working with waypoints that teams designate on the field. 

### Features

- Can edit paths with commonly used splines (Cubic Hermite + Quintic Hermite)
- Can export paths directly to code, so all that needs to be done is a bit of copy/pasting
- Performance (direct access to OpenGL)

### Build Instructions

Java version recommended: `1.8`

#### Windows

1. Clone repository
2. Run `gradlew build` then `gradlew run`

#### Mac/Linux

1. Clone repository
2. Run `./gradlew build` then `./gradlew run`

There is a universal jar file located in `build/libs` that can be run with `java -jar jarfile.jar` as well.
