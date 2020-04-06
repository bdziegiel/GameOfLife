import javax.swing.*;

public class Main extends JFrame {
    private Main(){
        Form form = new Form();
        form.setAndShowGUI();
    }

    public static void main(String[] args) {
        new Main();
    }
}