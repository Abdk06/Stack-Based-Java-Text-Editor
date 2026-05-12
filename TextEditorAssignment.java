package texteditorassignment;
import java.io.File;

/*
    This is a driver test class to test the TextEditor class
*/
public class TextEditorAssignment {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        File file = new File("actions.txt");
        editor.readFile(file);
    }
    
}
