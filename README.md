# 1351 Path Editor

_Follow as [changes](https://trello.com/b/EOfeD5tU/path-planner-gui) are made_

## Description

This path editor is a custom Java application made with the LibGDX framework that edits/generates robot trajectories for use during autonomous mode in FRC matches. It is scaled to the field in inches, can add/edit spline paths, and contains a variety of tools for working with waypoints that teams designate on the field. 

### Features

- Can edit paths with commonly used splines (Cubic Hermite + Quintic Hermite)
- Can export paths directly to code, so all that needs to be done is a bit of copy/pasting
- Performance (direct access to OpenGL)
