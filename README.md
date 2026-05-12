# Stack-Based Java Text Editor

This is a project that implements a simple text editor using the stack data structure.
It supports the core rediting operations like insert, delete, replace, undo and redo.
It proccesses a file named actions.txt and executes the instructions from that file one by one, and displays the result.

It contains an Action class that contains the information for each action, such as the type of the action, the content of the action and additional information that the action might need, and contains the Text Editor class, that handles each Action object depending on it's type, and proccesses the actions.txt file to execute its commands.

It also handles any errors regarding the format, either incorrectly formating the actions or typing actions that do not exist/ are not supported.

A driver class is included too that runs the program, initiates the Text Editor object and allows the user to give the path to the file they want to read the commands from.

Here is a sample for the actions.txt file:

```
insert Hello 0
insert,World 5
delete 5 7
undo
replace Java 75
undo
redo
