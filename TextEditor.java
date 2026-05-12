package texteditorassignment;
import java.util.Stack;
import java.util.Scanner;
import java.io.File;

/*

  This is the Text Editor class that reads a file and takes the commands from it,
  the insert, delete and replace commands create an Action object that gets pushed to
  the undo stack, the undo command undoes the action, pops it from the undo stack and pushes it to the
  redo stack, the redo stack redoes the action, pops it from the redo stack and pushes it to the undo stack again.
  
*/

public class TextEditor {
    
    //Instance variables
    private StringBuilder text; // This is the output text
    private Stack<Action> undoStack; // This is the stack that the actions get pushed to and popped from when undo is called
    private Stack<Action> redoStack; // This is the stack that gets pushed to when undo is called, and popped from when redo is called
    
    
    //Default constructor
    public TextEditor() {
        this.text = new StringBuilder();
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }
    
    // -This method processes the insert command, then pushes it to the undo stack
    // -@param insertedText the text to be inserted
    // -@param position the position that the text will be inserted in
    // -@param boolean needClear this is a flag that detrermines whether the redo stack should be cleared or not after the action
    // -@return the created Action object
    public Action insert(String insertedText, int position, boolean needClear) {
        this.text.insert(position, insertedText); // Edits the current output text by in inserting the specified text
        Action insert = new Action("insert", insertedText, position); // Creates a new Action object
        this.undoStack.push(insert); // Pushes the action object to the undo stack
        if(needClear) { // If the needClear flag is true, it resets the redo stack as it is no longer relevant
            clear(); // The method to reset the redo stack
        }
        return insert; // Returns the Action object
    }
    
    // -This method processes the delete command, then pushes it to the undo stack
    // -@param position the start position that the characters get deleted from
    // -@param length how many characters to be deleted
    // -@param boolean needClear this is a flag that detrermines whether the redo stack should be cleared or not after the action
    // -@return the created Action object
    public Action delete(int position, int length, boolean needClear) {
        String deletedText = text.substring(position, position+length);
        this.text.delete(position, position+length); // Edits the current output text by deleting the specified characters
        Action delete = new Action("delete", deletedText, position); // Create a new Action object
        this.undoStack.push(delete); //Pushes the action object to the undo stack
        if(needClear) { //If the needClear flag is true, it resets the redo stack as it is no longer relevant
            clear(); // The method to reset the redo stack
        }
        return delete; // Returns the Action object
    }
    
    // -This method processes the replace command, then pushes it to the undo stack
    // -@param newText the new text that will replace the old text
    // -@param position the position that the new text will go in
    // -@param length how many characters to be replaced
    // -@param boolean needClear this is a flag that detrermines whether the redo stack should be cleared or not after the action
    // -@return the created Action object
    public Action replace(String newText, int position, int length, boolean needClear) {
        String replacedText = text.substring(position, position+length);
        text.replace(position, position+length, newText); // Edits the current output text by replacing the text in the specified position
        Action replace = new Action("replace", newText, replacedText, position); // Creates a new Action object
        this.undoStack.push(replace); // Pushes the action object to the undo stack
        if(needClear) { //If the needClear flag is true, it resets the redo stack as it is no longer relevant
            clear(); // The method to reset the redo stack
        }
        return replace; // Returns the Action object
    }
    
    // -This method processes the undo command, then pushes it to the redo stack
    // -@return the created Action object
    public Action undo() {
        // Checks if the undo stack is empty or not
        if (this.undoStack.isEmpty()) {
            return null;
        }
        
        Action action = undoStack.peek(); // Gets the Action at the top of the stack
        String type = action.getType();
            //If the type of the action at the top is insert, it deletes then pushes to the redo stack
            if (type.endsWith("insert")) {
                delete(action.getPosition(), action.getText().length(), false); //needClear is false as it is called from undo
                this.undoStack.pop(); // Pop the top element becausue when delete is called it pushes an unncessary object to the undo stack
                redoStack.push(undoStack.pop()); // Pop from undo stack and push to redo stack
                action.setType("undo-insert"); // Change the type of the action so the toString method in Action class works properly
            }
            
            //If the type of the action at the top is delete, it inserts then pushes to the redo stack
            else if (type.endsWith("delete")) {
                insert(action.getText(), action.getPosition(), false); //needClear is false as it is called from undo
                this.undoStack.pop(); // Pop the top element becausue when delete is called it pushes an unncessary object to the undo stack
                redoStack.push(undoStack.pop()); // Pop from undo stack and push to redo stack
                action.setType("undo-delete"); // Change the type of the action so the toString method in Action class works properly
            }
            
            //If the type of the action at the top is replace, it undoes the replace and puts back the old text
            else if (type.endsWith("replace")) {
                replace(action.getOldText(), action.getPosition(), action.getText().length(), false); //needClear is false as it is called from undo
                this.undoStack.pop(); // Pop the top element becausue when delete is called it pushes an unncessary object to the undo stack
                redoStack.push(undoStack.pop()); // Pop from undo stack and push to redo stack
                action.setType("undo-replace"); // Change the type of the action so the toString method in Action class works properly
            }
        return action; // Returns the Action object
        
    }
    
    // -This method processes the undo command, then pushes it to the redo stack
    // -@return the created Action object
    public Action redo() {
        
        // Checks if the redo stack is empty or not
        if (this.redoStack.isEmpty()) {
            return null;
        }
        
        Action action = redoStack.peek(); // Gets the Action at the top of the stack
        String type = action.getType();
            
            //If the type of the action at the top is delete, it deletes then pushes back to the undo stack
            if (type.endsWith("delete")) {
                delete(action.getPosition(), action.getText().length(), false); //needClear is false as it is called from redo
                this.undoStack.pop();
                undoStack.push(redoStack.pop());
                action.setType("redo-delete");
            }
            
            //If the type of the action at the top is insert, it inserts then pushes back to the undo stack
            else if (type.endsWith("insert")) {
                insert(action.getText(), action.getPosition(), false); //needClear is false as it is called from redo
                this.undoStack.pop();
                undoStack.push(redoStack.pop());
                action.setType("redo-insert");
            }
            
            //If the type of the action at the top is replace, it replaces then pushes back to the undo stack
            else if (type.endsWith("replace")) {
                replace(action.getText(), action.getPosition(), action.getOldText().length(), false); //needClear is false as it is called from redo
                this.undoStack.pop();
                undoStack.push(redoStack.pop());
                action.setType("redo-replace");
            }
        return action;
    }
    
    //This method clears the redo stack when neccessary and when it's no longer relevant
    private void clear() {
        this.redoStack.clear();
    }
    
    //This is the main method in the class, it takes the actions from a file and proccesses them line by line
    public void readFile(File file) {
        int count = 1; // Counter for the lines
        String line = "";
        try (Scanner scanner = new Scanner(file)) { // Creates scanner object to read from file
            while (scanner.hasNextLine()) { // A loop that goes over every line in the file
                line = scanner.nextLine();
                if (line.isEmpty()) { // Ignores empty lines
                    continue;
                }
                try {
                    String[] tokenizer = line.split(" "); // Splits every line by spaces to use them as parameters
                    String command = tokenizer[0]; // First word is always the command
                    Action action = new Action();
                    switch (command) { // A switch statement to check the type of command
                        case "insert": // If command is insert
                            int position = Integer.parseInt(tokenizer[tokenizer.length-1]); // Position is always at the end
                            StringBuilder insertedText = new StringBuilder();
                            for(int i = 1; i < tokenizer.length-1; i++) {
                                insertedText.append(tokenizer[i] + " "); // Appened every word before the position to the insertedText
                            }
                            action = insert(insertedText.toString().trim(), position, true); // Trim to get rid of whitespace at the end, needClear is true as it is called independantly
                            break;

                        case "delete": // If command is delete
                            action = delete(Integer.parseInt(tokenizer[1]), Integer.parseInt(tokenizer[2]), true); // Token at position 1 is position, token at position 2 is length, needClear is true as it is called independantly
                            break;

                        case "replace": // If command is replace
                            int length = Integer.parseInt(tokenizer[tokenizer.length-1]); // Length is always at the end
                            int pos = Integer.parseInt(tokenizer[tokenizer.length-2]); // Position is alwaysb efore length
                            StringBuilder replacedText = new StringBuilder();
                            for(int i = 1; i < tokenizer.length-2; i++) {
                                replacedText.append(tokenizer[i] + " ");  // Appened every word before the position to the replacedText
                            }
                            action = replace(replacedText.toString().trim(), pos, length, true); // Trim to get rid of whitespace at the end, needClear is true as it is called independantly
                            break;

                        case "undo": // If command is undo
                            action = undo();
                            break;

                        case "redo": // If command is redo
                            action = redo();
                            break;
                        default:
                            break;
                    }
                    
                    // In case undo or redo is called to an empty stack
                    if(action == null) {
                        System.out.println(count + ") " + line);
                        System.out.println("There are no actions to " + command + ".\n");
                        count++;
                    }
                    // In case the command typed is invalid
                    else if(action.getType() == null) {
                        System.out.println(count + ") " + line);
                        System.out.println("Invalid command: " + command + "\n");
                        count++;
                    }
                    // In case everything is valid
                    else {
                        System.out.println(count + ") " + line);
                        System.out.println("Operation: " + action.toString() + "\nText: " + this.text+"\n");
                        count++;
                    }
                
                // Catch statements to catch potential errors
                } catch(IndexOutOfBoundsException e) {
                   System.out.println(count + ") " + line);
                   System.out.println("Invalid position entered or invalid parameters entered.\n");
                   count++;
                } catch(NumberFormatException e) {
                   System.out.println(count + ") " + line);
                   System.out.println("Invalid parameters entered.\n");
                   count++;
                }
                catch (Exception e) {
                   System.out.println(count + ") " + line);
                   System.out.println("An error happened: " + e.getMessage() + "\n");
                   count++;
                }
            }
        // Catch statement if the file is not found
        } catch (java.io.FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }
    
}
