# Flight Simulator Control

A java project we made during the semester.

## Introduction

In this repository we'll cover the semester final project that we did in the advanced software development course, which deals with java programming, with emphasis on design patterns and programming principles such as SOLID and GRASP.
As well as object-oriented architectures and the development of JavaFX desktop application.

A code where you can control an airplane simulator using a joystick or script, with a nice interface GUI.

## [Server](https://github.com/DanielPaz6)

As said in the main description, this project is about a general server, in which the programmer can decide how and what the server will do given a general problem and how he should solve it.
We want to write a general server, a server that can be used again and again in various projects. To do so, he must make a fundamental and important separation: separating what changes between a project and a project, and what is not.
We will define the functionality of the server through an interface, and each project can have another class that will implement the same functionality in a different way. In that way, we kept the Open / Close principle.

### Our Concerete Server

Given a graph, it could solve it,
Using BFS Algorithm.

[ServerClient Java UML](/uml/server_bridgepattern.png "Server Bridge Pattern UML")
