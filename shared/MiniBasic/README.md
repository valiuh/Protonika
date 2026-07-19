# MiniBasic Language Specification

MiniBasic is a lightweight, educational programming language modeled after early BASIC dialects. It features clear syntax, essential control structures, and support for mathematical operations.

This document describes the complete specification of the MiniBasic language, including keywords, operators, brackets, and example code.

---

## Keywords

### Program Structure
- `REM` – Comment. Used to include remarks.
  ```basic
  REM This is a comment
  ```
- `LET` – Variable assignment.
  ```basic
  LET a = 10
  ```
- `END` – Terminates the program.
  ```basic
  END
  ```
- `STOP` – Halts execution without ending the program.
  ```basic
  STOP
  ```

### I/O
- `PRINT` – Outputs values to the console.
  ```basic
  PRINT a
  ```
- `INPUT` – Reads a value from user input.
  ```basic
  INPUT a
  ```

### Control Flow
- `IF` / `THEN` / `ELSE` – Conditional branching.
  ```basic
  IF a > 10 THEN PRINT 1 ELSE PRINT -1
  ```
- `GOTO` – Jumps to a specified line.
  ```basic
  GOTO 100
  ```
- `GOSUB` / `RETURN` – Subroutine call and return.
  ```basic
  GOSUB 200
  RETURN
  ```

### Looping
- `FOR` / `TO` / `STEP` / `NEXT` – Loops with initialization and incrementation.
  ```basic
  FOR i = 1 TO 10 STEP 2
    PRINT i
  NEXT
  ```

### Subroutines
- `SUB` – Defines a subroutine (may be used with `RETURN`).
  ```basic
  SUB Square
    LET S = A * A
    RETURN
  ```

---

## Operators

### Unary Operators (Function style)
- `ABS(x)` – Absolute value
- `COS(x)` – Cosine
- `SIN(x)` – Sine
- `TAN(x)` – Tangent
- `ACOS(x)` – Arc cosine
- `ASIN(x)` – Arc sine
- `ATN(x)` – Arc tangent
- `LOG(x)` – Log base 10
- `LN(x)` – Natural log
- `EXP(x)` – Exponential function
- `SQR(x)` – Square root

```basic
LET a = ABS(-5)
LET b = SQR(16)
```

### Binary Operators (Infix style)
- Arithmetic:
    - `+` (Addition)
    - `-` (Subtraction)
    - `*` (Multiplication)
    - `/` (Division)
    - `^` (Exponentiation)
- Logical:
    - `AND`, `OR`, `NOT`, `XOR`
- Relational:
    - `>`, `<`, `>=`, `<=`, `=`

```basic
LET a = 3 + 5 * 2
IF a >= 10 THEN PRINT -1
```

---

## Brackets
- `(` – Left Parenthesis
- `)` – Right Parenthesis

Used for grouping expressions and function arguments:
```basic
LET a = (3 + 2) * 5
LET b = SIN(3.14159 / 2)
```

---

## Sample Program
```basic
REM MiniBasic sample
INPUT a
IF a < 0 THEN
  PRINT -1
ELSE
  PRINT 1
END
```

---

## Example Algorithms

### Factorial Calculation
```basic
REM Calculate factorial of a number
INPUT n
LET f = 1
FOR i = 1 TO N
  LET f = f * i
NEXT
PRINT f
```

### Quadratic Equation Solver
```basic
REM Solve ax^2 + bx + c = 0
INPUT a
INPUT b
INPUT c
LET d = b * b - 4 * a * c
IF d < 0 THEN
  PRINT -1
ELSE
  LET r1 = (-b + SQR(d)) / (2 * a)
  LET r2 = (-b - SQR(D)) / (2 * a)
  PRINT r1
  PRINT r2
END
```

---

## Notes
- Statements are typically written one per line.
- Line numbers may be used optionally to control flow.
- All keywords are **case-insensitive**.

---

Happy coding in MiniBasic!

