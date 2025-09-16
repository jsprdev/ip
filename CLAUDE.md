# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is Pichu, a JavaFX-based chatbot for task management. The project has evolved from a command-line interface to a graphical user interface while maintaining backward compatibility with the text-based UI.

## Build and Development Commands

### Building and Running
- `./gradlew build` - Build the project
- `./gradlew run` - Run the application (GUI version via Launcher class)
- `./gradlew test` - Run JUnit tests
- `./gradlew check` - Run checkstyle and other quality checks

### Memory Configuration
The project includes specific JVM memory settings in build.gradle to prevent OOM errors:
- Build tasks: `-Xmx2g -Xms512m`
- Test tasks: `-Xmx1g -Xms512m`
- Run task: `-Xms512m -Xmx1g` with heap dump on OOM

### Code Quality
- Checkstyle is configured with version 11.0.0
- Checkstyle failures are set to not fail the build (`ignoreFailures = true`)
- Use `./gradlew checkstyleMain` and `./gradlew checkstyleTest` for specific checks

## Architecture

### Main Components

**Entry Points:**
- `Launcher.java` - Main entry point that launches the JavaFX application
- `Main.java` - JavaFX Application class that sets up the GUI
- `pichu/Pichu.java` - Core chatbot logic and command processing

**Core Architecture:**
- **Parser** (`pichu.parser.Parser`) - Command parsing and validation
- **TaskList** (`pichu.core.TaskList`) - Task collection management
- **Storage** (`pichu.storage.Storage`) - File I/O operations for task persistence
- **TextUi** (`pichu.ui.TextUi`) - Legacy text-based UI (now unused)
- **DialogBox** (`pichu.DialogBox`) - JavaFX dialog components for GUI

**Task Types:**
- `pichu.task.Task` - Abstract base class
- `pichu.task.Todo` - Simple tasks
- `pichu.task.Deadline` - Tasks with due dates
- `pichu.task.Event` - Tasks with start and end times

### Key Patterns

**Command Processing:** The `Pichu.getResponse()` method serves as the main controller, parsing commands via `Parser.getCommandType()` and delegating to specific handler methods.

**Data Persistence:** Tasks are automatically saved to `data/tasks.txt` after each modification. The `Storage` class handles both individual task saves and bulk operations.

**GUI Architecture:** JavaFX-based with `Main` class handling UI setup and event handling. User input flows through `handleUserInput()` to `pichu.getResponse()` and back to dialog boxes.

## Development Notes

### JavaFX Dependencies
The project includes platform-specific JavaFX dependencies for Windows, macOS, and Linux. Version 17.0.7 is used across all platforms.

### Legacy Code
The `Pichu.java` file contains extensive commented-out code for the original text-based interface. This demonstrates the evolution from CLI to GUI while maintaining the same core logic structure.

### File Paths
- Task data storage: `data/tasks.txt`
- User images: `/images/Speed.png`
- Bot images: `/images/Ronaldo.png`

### Testing
- JUnit 5 (Jupiter) is configured for testing
- Test files should be placed in `src/test/java/`
- Current test: `src/test/java/pichu/PichuTest.java`