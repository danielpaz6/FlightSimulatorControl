# Flight Simulator Control


[![Presentation Project](/uml/demo3.png)](https://www.youtube.com/watch?v=GJdyEY4cXHk)


A java project we made during the semester.

## Introduction

In this repository we'll cover the semester final project that we did in the advanced software development course, which deals with java programming, with emphasis on design patterns and programming principles such as SOLID and GRASP.
As well as object-oriented architectures and the development of JavaFX desktop application.

A code where you can control an airplane simulator using a joystick or script, with a nice interface GUI.

## [Server](https://github.com/danielpaz6/ServerClient_Java)
<img src="/uml/graph.png" align="right" width="148">
As said in the main description, this project is about a general server, in which the programmer can decide how and what the server will do given a general problem and how he should solve it.
We want to write a general server, a server that can be used again and again in various projects. To do so, he must make a fundamental and important separation: separating what changes between a project and a project, and what is not.
We will define the functionality of the server through an interface, and each project can have another class that will implement the same functionality in a different way. In that way, we kept the Open / Close principle.

---

### Our Concerete Server

Given a graph, it could solve it,
Using [Best-First Search](https://en.wikipedia.org/wiki/Best-first_search) algorithm ( which is already implemented in this project ) or any other search algorithm.

<p align="center">
  <img src="/uml/server_bridgepattern.png" width="600">
</p>

So in our concrete server, given a weighted graph, it will run the search algorithm, and as a output will return the cheapest route of the target.

In addition, we can see that we used the Bridge Design Pattern, we created a separation between the problem, and what solves the problem and so we can solve various problems through different solutions.

The specific problem and solution in the project is that given a matrix is able to solve it and say the cheapest path from point A to point B using **BestFirstSearch** algorithm as said before.

For example: lets assume we have this matrix:

|  |   |  |  |
| :---: | :---: | :---: | :---: |
| **114** | **93**  | 164 | 123 |
| 109 | **27**  | **40**  | **15**  |
| 156 | 175 | 189 | **5**   |
| 160 | 186 | 153 | **38**  |

If we'll set the start point to be 114 (0,0) and the end point to be 38 (3,3) then the path ( the output ) will be:

Right, Down, Right, Right, Down, Down.

*(Marked in bold)*

---

## Script Reader ( Interpreter )

As stated at the beginning of the repository, the project is a GUI of a flight simulator by which you can control the plane and get information from it.

And one of its features is running a script, basically a kind of programming language that can run and fly the plane.

As in the following example:

```scala
openDataServer 5400 10
connect 127.0.0.1 5402
var breaks = bind "/controls/flight/speedbrake"
var throttle = bind "/controls/engines/current-engine/throttle"
var heading = bind "/instrumentation/heading-indicator/indicated-heading-deg"
var roll = bind "/instrumentation/attitude-indicator/indicated-roll-deg"
var pitch = bind "/instrumentation/attitude-indicator/internal-pitch-deg"
var rudder = bind "/controls/flight/rudder"
var aileron = bind "/controls/flight/aileron"
var elevator = bind "/controls/flight/elevator"
var alt = bind "/instrumentation/altimeter/indicated-altitude-ft"
breaks = 0
throttle = 1
var h0 = heading
sleep 5000
while alt < 1000 {
	rudder = (h0 - heading)/180
	aileron = - roll / 70
	elevator = pitch / 50
	sleep 150
}

```
For this purpose, we wrote a code reader, an interpreter, which allows you to connect to the simulator, open a server, and run various commands that control the plane and sample its data.
For example:

We see a while loop that will actually take place as long as the plane’s altitude is less than 1000 meters, and the loop content is the acceleration of the plane.
And in this part:
```scala
rudder = (h0 - heading)/180
```
We can see that arithmetic expressions are supported as well, and to interpret them we use [Dijkstra's Shunting Yard algorithm](https://en.wikipedia.org/wiki/Shunting-yard_algorithm).

---

### Command Pattern

<p align="center">
  <img src="/uml/CommandPattern.png" width="600">
</p>

There is also extensive use of different commands, which use different design patterns:
For example, the design pattern we use to implement the parser is Command Pattern - which means that each command in the program will receive a Command object.

It is important that all commands will implement the same interface, because we want them to have a common polymorphic denominator.

Another reason to use the Pattern Pattern template and not simply to use functions, is so we can do the assemblies, for example, a command that holds a lot of commandos, and we actually combined **Command Pattern** with **Composite Pattern**.

So if, for example, we take a look at the loop command or if command, then we can see that each contains a list of commands which in turn can be either a standard command or another list of commands.




### Our interpreter stages

<p align="center">
  <img src="/uml/interpreter.png" width="600">
</p>

So this script-reader works in a very similar way to the interpreter of a real programming language.

And the first stage that happens in the interpretation process is Lexer

That actually takes the string as it is, and converts it to logical distribution according to commands and parameters that can run later on with a Scanner.

The next stage that is supposed to happen to is the parser stage, which actually begins to convert the "array" created by the Lexer into commands and to execute them.

However, since this script is supposed to run an airplane, we don't want the interpreter to run the simulator, connect to the server and start running the code and in the middle of the script we will find that there are syntactic errors or incorrect entries.

So, before we start running the commands, we will make sure that a pre-parser will pass the initial scan on the script and check for Syntax errors, such as incorrect parameters or irrational values.

Since we are already running an initial scan to check the integrity of the code, we won't run again the same code and do exactly the same operation in the parser stage, so in the Pre-Parser phase, in addition to testing the program we will also maintain a list of commands.

So in the parser phase, we can run on this list instead of running over each cell in the array and reinterpreting the different commands.

---

### GUI & JavaFx Technology

In this milestone, we built the MVVM desktop application using JavaFX technology, which we will be explained later on.
Here's how the app looks:

<p align="center">
  <img src="/uml/gui.png" width="600">
</p>

* **Dashboard** – This is actually the application's home window, which you can access our Github
pages.
* **Run a script** – Here you can actually write a script that can be interpreted and change
the plane values so that he can do different things like taking off.
* **Projects** - In this window we have a list of ready scripts - this list is in an XML file that the application can load into the page and prints it as a table.
And once we click on a particular line in the table it will open the appropriate script for us. for example:
By clicking on the first line, we have moved to the Run a script page with the desired code.
* **Manual Control** – Once connected to the simulator, we can control the plane with a joystick and
two sliders to control the plane's stability and throttle.
* **Surface Map** – This is basically a pane from which you can select a map csv file. When the file
is selected, the map will be loaded into the application, and will display by color when red is low
area and green is a high area.

Also, we can mark a destination point on the map, and connect to the server that will give the
cheapest route to that destination.

In addition, you can see the plane's progress on the map and recalculate route when another
target is selected.

## MVVM Architecture

<p align="center">
  <img src="/uml/mvvm.png" width="600">
</p>

So as we said, we chose to use the **MVVM architecture**.

We have the View layer that is responsible for the presentation, for example the 
input from the user, and he is also responsible for producing the graphic and also has the code-
behind - for example, functions that are activated when we press a button. Which actually called
event-oriented programming.

* **Model** – Responsible for our business logic, such as algorithms and data access
* **View Model** – It passes commands from the View to the Model, and its purpose is to
separate the View from the Model.
* **Data Binding** – We can wrap variables such as those in the View, and then when we change
something in the text, it will automatically changed in the ViewModel.

Actually, for all the MVVM architecture to work, we'll have to wrap the different components
together. And this is done through the Observer Pattern, actually bind
the different components together.

```java
Model m = new Model(server);
			
ViewModel vm = new ViewModel(m);
m.addObserver(vm);

MainWindowController mwc= fxl.getController();
mwc.setViewModel(vm);
vm.addObserver(mwc);
```

So this was our application with emphasis on many programming principles.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them.

imports / technologies:

* javafx
* fontawesomefx-8.9
* jfoenix-8.0.8
* updated Java Development Kit ( JDK )
* [Flight Gear](https://www.flightgear.org/) - open-source flight simulator

## Built With

* [Eclipse](https://www.eclipse.org/) - Java IDE
* [Scene Builder](https://gluonhq.com/products/scene-builder/) - Scene Builder 8.5.0


## Authors

* **Daniel Paz** - *Part of the project program* - [Profile](https://github.com/DanielPaz6)
* **Omer Nahum** - *Part of the project program* - [Profile](https://github.com/OmerNahum/)

