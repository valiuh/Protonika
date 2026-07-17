# Elektronika MK-61 Virtual Machine Specification

The Elektronika MK-61 Virtual Machine (VM) is a faithful software simulation of the third-generation Soviet RPN calculator, *Elektronika MK-61*. This VM is based on stack architecture and emulates its behavior using a command set described below.

---

## Table of Contents
- [Architecture](#architecture)
- [Command Set](#command-set)
- [Script Execution](#script-execution)
- [Sample Algorithms](#sample-algorithms)
    - [Factorial Calculation](#factorial-calculation)
    - [Quadratic Equation Solver](#quadratic-equation-solver)

---

## Architecture

The MK-61 VM uses a **Reverse Polish Notation (RPN)** model and operates on a four-register stack:

```
T
Z
Y
X ← top of the stack
```

Additionally, it includes general-purpose registers:
```
[0..9, a..e]
```

Some commands also operate using subroutine stacks, flags, and condition registers.

---

## Command Set

All supported commands are defined in the `Command` enum. Here are categorized highlights:

### Stack and Register Operations
- `B↑ (PUSH)`: Push value to stack
- `↻ (SHIFT)`: Rotate T → Z → Y → X → T
- `↔ (SWAP)`: Swap X and Y
- `СX (CLR)`: Clear X

### Arithmetic
- `+ (ADD)` / `- (SUB)` / `× (MUL)` / `÷ (DIV)`
- `x^2 (SQR)` / `√ (SQRT)` / `x^y (POW)` / `1/x (FRAC)`

### Trigonometric / Logarithmic
- `sin`, `cos`, `tg` / `sin^-1`, `cos^-1`, `tg^-1`
- `lg`, `ln`, `10^x`, `e^x`

### Constants
- `π (PI)` / `e (E)`

### Logic
- `∧ (AND)`, `∨ (OR)`, `⊕ (XOR)`, `¬ (NOT)`

### Program Control
- `С/П (STOP)`, `НОП (NOP)`
- `БП (GOTO)`, `ПП (GOSUB)`, `В/O (RTN)`
- Conditional jumps: `X<0 (NEG)`, `X≥0 (NNG)`, `X=0 (ZRO)`, `X≠0 (NZR)`
- Loop instructions: `L0`, `L1`, `L2`, `L3`

### Register Access
- `X→П (MOVT)`: Store X to register
- `П→X (MOVF)`: Load X from register

### Indirect Control (advanced)
- Indirect versions of jumps, moves, and calls like `К БП`, `К ПП`, etc.

---

## Script Execution

To build a stand alone virtual machine, use the following command:
```sh
kotlinc Mk61.kt tools/neutronika.kt -include-runtime -d tools/neutronika.jar
```

Scripts for this virtual machine are saved in `*.mk61` files.

To run a script, use the following command:
```sh
java -jar neutronika.jar -s script.mk61 -r y
```
Where:
- `-s` specifies the script path
- `-r y` (or `yes`) instructs the VM to produce an output file

### Output
The output is saved as `*.cnl` and displays:
```
▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
Stack:
X: 0.0
Y: 0.0
Z: 0.0
T: 0.0
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
Register of the Previous Result:
X1: 0.0
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
Registers:
0: 0.0       7: 0.0
1: 0.0       8: 0.0
2: 0.0       9: 0.0
3: 0.0       a: 0.0
             b: 0.0
4: 0.0       c: 0.0
5: 0.0       d: 0.0
6: 0.0       e: 0.0
▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
```

---

## Sample Algorithms

### Factorial Calculation
Assume the number is stored in register `0`, and result will be in `X`.
```mk61
П→X 0     ; Load N into X
X=0 12    ; If X == 0, jump to label 12
X→П 1     ; Store N in reg 1 (counter)
1         ; Push 1
X→П 2     ; Store result in reg 2
LBL 05
П→X 2     ; Load result
П→X 1     ; Load counter
×         ; Multiply
X→П 2     ; Store updated result
П→X 1     ; Load counter
1         ; Push 1
-         ; Decrease counter
X→П 1     ; Update counter
X≠0 05    ; Loop while counter ≠ 0
П→X 2     ; Load final result into X
С/П       ; End
```

### Quadratic Equation Solver
Assume coefficients a, b, c are in registers 0, 1, 2
```mk61
П→X 1     ; X = b
x^2       ; X = b^2
X→П 3     ; Save in reg 3
П→X 0     ; X = a
П→X 2     ; X = c
×         ; X = a * c
4         ; X = 4
×         ; X = 4ac
П→X 4     ; Store in reg 4
П→X 3     ; Load b^2
П→X 4     ; Load 4ac
-         ; X = D = b^2 - 4ac
X<0 99    ; If D < 0, jump to label 99 (no roots)
√         ; √D
X→П 5     ; Store √D
П→X 1     ; X = b
-         ; -b
X→П 6     ; Store -b
П→X 6     ; -b
П→X 5     ; √D
+         ; -b + √D
П→X 0     ; a
2         ; 2
×         ; 2a
÷         ; Root 1
X→П 7     ; Store in reg 7
П→X 6     ; -b
П→X 5     ; √D
-         ; -b - √D
П→X 0     ; a
2         ; 2
×         ; 2a
÷         ; Root 2
X→П 8     ; Store in reg 8
С/П       ; End
LBL 99
; Handle no real roots case (optional)
С/П
```