# Ex2 – Map2D GUI Project

## Overview
This project implements a graphical user interface (GUI) for a 2D map.
The map is represented as a matrix of integers, where each cell is drawn as a square on the screen.
The GUI allows editing the map, placing obstacles, choosing start and target points, and finding the shortest path between them.

This project was implemented as part of the Intro2CS_2026A course.

---

## Main Classes

### Map
Represents a 2D grid of integers.
The class supports:
- Getting and setting pixel values
- Drawing lines, rectangles, and circles
- Flood fill using BFS
- Shortest path computation using BFS
- Distance map calculation

### Index2D
Represents a single pixel (x, y) in the map.
The class provides:
- Access to x and y coordinates
- Distance calculation between pixels
- Equality comparison

### Ex2_GUI
Implements the graphical user interface using StdDraw.
The GUI allows interaction with the map using the mouse and keyboard.

---

## GUI Controls

### Mouse
- Click on a cell to toggle an obstacle
- Click to select start and target points (according to the current mode)

### Keyboard
- 1 – Toggle obstacle mode  
- 2 – Erase mode  
- S – Set start point (blue)  
- T – Set target point (red)  
- P – Compute and draw shortest path  
- R – Reset start, target, and path  
- C – Toggle cyclic mode  
- W – Save map to file  
- Q – Exit the program  

---

## Map File Format

Maps are saved and loaded from a text file.

Format:
width height  
row0 values  
row1 values  
...  
rowH-1 values  

Each row contains width integers separated by spaces.

---
