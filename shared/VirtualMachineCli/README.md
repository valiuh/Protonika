# VirtualMachineCli (`neutronika`)

`neutronika` is a terminal CLI for running MK-61 scripts with Protonika's shared virtual machine.

It reads a script file, executes it, prints stack/register state, and can optionally save the same output to a `.cnl` file.

## Arguments

- `-s <script_path>` (required): path to the input script file.
- `-r <y|yes>` (optional): save output to `<script_name>.cnl` in the current terminal directory.

If `-s` is missing, the CLI prints:

```text
Error: Missing required argument -s <script_path>
```

## Compile from terminal

From repository root:

```bash
./gradlew :shared:VirtualMachineCli:build
```

Build executable fat JAR:

```bash
./gradlew :shared:VirtualMachineCli:fatJar
```

Output JAR:

```text
shared/VirtualMachineCli/build/libs/neutronika-cli-all.jar
```

## Run from terminal

Run with Gradle:

```bash
./gradlew :shared:VirtualMachineCli:run --args="-s /absolute/path/program.mk61"
```

Run fat JAR directly:

```bash
java -jar shared/VirtualMachineCli/build/libs/neutronika-cli-all.jar -s /absolute/path/program.mk61
```

Run and save output to `.cnl`:

```bash
java -jar shared/VirtualMachineCli/build/libs/neutronika-cli-all.jar -s /absolute/path/program.mk61 -r yes
```

## Example (input file and output)

### Input file: `neutronika_input.mk61`

```text
15
С/П
```

### Command

```bash
java -jar shared/VirtualMachineCli/build/libs/neutronika-cli-all.jar -s /tmp/neutronika_input.mk61 -r yes
```

### Terminal output

```text
▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
Stack:
X: 15.0
Y: 0.0
Z: 0.0
Z: 0.0
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
Register of the Previous Result:
X1: 0.0
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
Registers:
0: 0.0 		 7: 0.0
1: 0.0 		 8: 0.0
2: 0.0 		 9: 0.0
3: 0.0 		 a: 0.0
		 b: 0.0
4: 0.0 		 c: 0.0
5: 0.0 		 d: 0.0
6: 0.0 		 e: 0.0
▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓

Result was saved to neutronika_input.cnl
```
