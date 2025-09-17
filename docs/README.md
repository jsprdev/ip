# Pichu - Your Personal Task Manager

**SUIII!** Meet Pichu, your personal task management chatbot with the legendary Cristiano Ronaldo's personality! 

![Pichu Screenshot](Ui.png)

---

## Features Overview

Pichu helps you manage three types of tasks:
- **📝 Todo**: Simple tasks without deadlines
- **⏰ Deadline**: Tasks with specific due dates
- **📅 Event**: Tasks with start and end times

**Plus special features:**
- 🏷️ **Tag System**: Organize tasks with hashtags
- 🔍 **Smart Search**: Find tasks by keywords or tags
- 💾 **Auto-save**: Your tasks are automatically saved
- ⚽ **Ronaldo Personality**: Get motivated with "SUIII!" and "SEWY..." responses!

---

## Quick Start

1. Launch the application
2. Type commands in the text field
3. Press "Send" or hit Enter
4. **SUIII!** Start managing your tasks like a champion!

---

## Features

> :information_source: **Notes about the command format:**
>
> * Words in `UPPER_CASE` are the parameters to be supplied by the user.  
>   e.g. in `todo DESCRIPTION`, `DESCRIPTION` is a parameter which can be used as `todo finish homework`.
>
> * Items in square brackets are optional.  
>   e.g `todo DESCRIPTION [#TAG]` can be used as `todo finish homework #urgent` or as `todo finish homework`.
>
> * Items with `…​` after them can be used multiple times including zero times.  
>   e.g. `[#TAG]…​` can be used as ` ` (i.e. 0 times), `#work`, `#work #urgent` etc.
>
> * Parameters can be in any order for deadline and event commands.  
>   e.g. if the command specifies `deadline DESCRIPTION /by DATETIME`, `/by DATETIME deadline DESCRIPTION` is also acceptable.
>
> * Extraneous parameters for commands that do not take in parameters (such as `bye`, `list`) will be ignored.  
>   e.g. if the command specifies `list 123`, it will be interpreted as `list`.
>
> * If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.

### Adding a todo task: `todo`

Adds a simple task without any date/time attached to it.

**Format:** `todo DESCRIPTION [#TAG]…​`

> :bulb: **Tip:** A task can have any number of tags (including 0)

**Examples:**
* `todo finish homework`
* `todo buy groceries #shopping #urgent`
* `todo call mom #personal #family`

**Expected response:**
```
GOLAZO!!!! I've added this task:
  [T][ ] finish homework
Now you have 1 task(s) in the list.
```

### Adding a deadline task: `deadline`

Adds a task that needs to be done before a specific date/time.

**Format:** `deadline DESCRIPTION /by DATETIME [#TAG]…​`

**Examples:**
* `deadline submit report /by 2024-12-15 1800`
* `deadline finish assignment /by 15/12/2024 #homework #urgent`
* `deadline pay bills /by 2024-12-20`

**Supported date formats:**
* `yyyy-MM-dd HHmm` (e.g., 2024-12-15 1800)
* `yyyy-MM-dd` (e.g., 2024-12-15)
* `dd/MM/yyyy HHmm` (e.g., 15/12/2024 1800)
* `dd/MM/yyyy` (e.g., 15/12/2024)
* `d/M/yyyy` (e.g., 5/3/2024)

**Expected response:**
```
GOLAZO!!!! I've added this task:
  [D][ ] submit report (by: Dec 15 2024, 6:00 PM)
Now you have 2 task(s) in the list.
```

### Adding an event task: `event`

Adds a task that starts at a specific time and ends at a specific time.

**Format:** `event DESCRIPTION /from START_DATETIME /to END_DATETIME [#TAG]…​`

**Examples:**
* `event team meeting /from 2024-12-20 1400 /to 2024-12-20 1600`
* `event birthday party /from 15/12/2024 1900 /to 15/12/2024 2300 #personal #fun`

**Expected response:**
```
GOLAZO!!!! I've added this task:
  [E][ ] team meeting (from: Dec 20 2024, 2:00 PM to: Dec 20 2024, 4:00 PM)
Now you have 3 task(s) in the list.
```

### Listing all tasks: `list`

Shows all tasks in your task list.

**Format:** `list`

**Expected response:**
```
SUIII! Here are the tasks in your list:
1.[T][ ] finish homework #urgent #school
2.[D][ ] submit report (by: Dec 15 2024, 6:00 PM) #work
3.[E][ ] team meeting (from: Dec 20 2024, 2:00 PM to: Dec 20 2024, 4:00 PM) #meeting
```

### Marking a task as done: `mark`

Marks the specified task as completed.

**Format:** `mark INDEX`

* Marks the task at the specified `INDEX` as done.
* The index refers to the index number shown in the displayed task list.
* The index **must be a positive integer** 1, 2, 3, …​

**Examples:**
* `mark 2` marks the 2nd task in the task list as done.

**Expected response:**
```
SUIIIII! I've marked this task as done:
[X] submit report
```

### Marking a task as not done: `unmark`

Marks the specified task as not completed.

**Format:** `unmark INDEX`

* The index refers to the index number shown in the displayed task list.
* The index **must be a positive integer** 1, 2, 3, …​

**Examples:**
* `unmark 1` marks the 1st task in the task list as not done yet.

**Expected response:**
```
SEWY..., I've marked this task as not done yet:
[ ] finish homework
```

### Deleting a task: `delete`

Deletes the specified task from the task list.

**Format:** `delete INDEX`

* Deletes the task at the specified `INDEX`.
* The index refers to the index number shown in the displayed task list.
* The index **must be a positive integer** 1, 2, 3, …​

**Examples:**
* `list` followed by `delete 2` deletes the 2nd task in the task list.
* `find homework` followed by `delete 1` deletes the 1st task in the results of the find command.

**Expected response:**
```
SUIIII. I've removed this task:
  [D][ ] submit report
Now you have 2 task(s) in the list.
```

### Finding tasks by keyword: `find`

Finds tasks whose descriptions contain any of the given keywords.

**Format:** `find KEYWORD`

* The search is case-insensitive. e.g `homework` will match `Homework`
* Only the task description is searched.
* Only full words will be matched e.g. `home` will not match `homework`
* Tasks matching the keyword will be returned.

**Examples:**
* `find homework` returns tasks containing "homework"
* `find meeting` returns tasks containing "meeting"

**Expected response:**
```
SUIII! Here are the matching tasks in your list:
1.[T][X] finish homework #urgent #school
```

### Finding tasks by tag: `tag`

Finds tasks that have the specified tag.

**Format:** `tag TAGNAME`

* Searches for tasks with the exact tag name (without the # symbol).
* The search is case-sensitive.

**Examples:**
* `tag urgent` finds all tasks tagged with #urgent
* `tag work` finds all tasks tagged with #work

**Expected response:**
```
Here are the tasks with tag #urgent:
1.[T][X] finish homework #urgent #school
```

### Exiting the program: `bye`

Exits the program.

**Format:** `bye`

**Expected response:**
```
SUIII! Bye. Hope to see you again soon!
```

---

## Saving the data

Pichu data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

---

## Editing the data file

Pichu data are saved automatically as a text file `[JAR file location]/data/tasks.txt`. Advanced users are welcome to update data directly by editing that data file.

> :exclamation: **Caution:** If your changes to the data file makes its format invalid, Pichu will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.  
> Furthermore, certain edits can cause Pichu to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
