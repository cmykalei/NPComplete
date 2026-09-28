# NP Complete Stack
Heuristic algorithms for the NP-complete box stacking problem in Java.

## Project 🌳
```
.
├── Box.java
├── DP.java
├── Greedy.java
├── NPCStack.java
├── rand1000.boxes
├── README.md
└── SA.java
```

### Usage
`NPCStack.java` and `SA.java` (simulated annealing) have the main algorithms used here, and `Greedy.java` is used by `SA.java` to create the initial solution.

The `removeBox()` method in `NPCStack.java` is commented out — results appear to be better (taller stacks) without it.

`DP.java` gives the best solution but it's not a heuristic algorithm and only works for up to around 20 boxes.

## Commands
1. To run the simulated annealing stack use `java NPCStack rand1000.boxes`.
2. To run the dynamic programming solution use `java DP rand1000.boxes`.
