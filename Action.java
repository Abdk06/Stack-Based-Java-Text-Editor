package texteditorassignment;
/* This is the Action class that creates an object for each action taken from the user,
   and records the type of the action (insert, delete, replace etc), the specified text,
   the position the action was taken at and if the action is replace it also records
   the old text.
   Each action object later gets put in either an undo stack or a redo stack.
*/

public class Action {
    // Instance variables
    private String type;
    private String text;
    private int position;
    private String oldText;
    
    // No-Argument constructor
    public Action () {
        this.type = null;
        this.text = null;
        this.position = -1;
        this.oldText = null;
    }
    
    //Argumented constructor
    public Action(String type, String text, int position) {
        this.type = type;
        this.text = text;
        this.position = position;
    }
    
    //Argumented constructor for replace, as it has the oldText variable
    public Action(String type, String text, String oldText, int position) {
        this.type = type;
        this.text = text;
        this.oldText = oldText;
        this.position = position;
    }
    
    // - Getter method
    // - @return the type of the action
    public String getType() {
        return this.type;
    }
    
    // - Getter method
    // - @return the text of the action
    public String getText() {
        return text;
    }
    
    // - Getter method
    // - @return the position of the action
    public int getPosition() {
        return position;
    }

    // - Getter method
    // - @return the type of the old text of the replace action
    public String getOldText() {
        return oldText;
    }
    
    // Setter method, sets the tpye for the action
    public void setType(String type) {
        this.type = type;
    }
    
    @Override
    //- toString method used to get info for each type of action
    //- @return information about action
    public String toString() {
        switch(this.type) {
            //Returns info for insert action
            case "insert":
                return("\"" + this.text +"\" was inserted at position " + this.position +".");
            //Returns info for delete action
            case "delete":
                return(this.text.length() + " characters starting from position " + this.position + " (\"" + this.text + "\") were deleted.");
            //Returns info for replace action
            case "replace":
                return(this.oldText.length() + " characters starting from position " + this.position + " (\"" + this.oldText + "\") were replaced with \"" + this.text + "\".");
            //Returns info for undone insert action
            case "undo-insert":
                return("The last insert operation was undone. The inserted text \"" + this.text + "\" was deleted.");
            //Returns info for undone delete action
            case "undo-delete":
                return("The last delete operation was undone. The deleted text \"" + this.text + "\" was restored.");
            //Returns info for undone replace action
            case "undo-replace":
                return("The last replace operation was undone. \"" + this.text +"\" was removed and \"" + this.oldText + "\" was restored.");
            //Returns info for redone insert action
            case "redo-insert":
                return("The last insert operation was redone. The deleted text \"" + this.text + "\" was restored again.");
            //Returns info for redone delete action
            case "redo-delete":
                return("The last delete operation was redone. The inserted text \"" + this.text + "\" was deleted again.");
            //Returns info for redone replace action
            case "redo-replace":
                return("The last replace operation was redone. \"" + this.oldText +"\" was removed again and replaced with \"" + this.text + "\".");
            default:
                return "Invalid command.";
        }
    }
    
}
