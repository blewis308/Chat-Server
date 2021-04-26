import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.lang.*;

public class Gui extends JFrame{
    //Stores the text the user inputs into the textbox
    private String fromUser = "";
    private boolean hasText = false;
    private JTextArea textArea;
    private boolean disconnect = false;

    //Creates a new Gui
    public Gui() {
        super("Java Chat Client");

        // Create a TextArea object
        textArea = new JTextArea(50, 30);
        // Put the TextArea object in a Scrollable Pane
        JScrollPane scrollPane = new JScrollPane(this.textArea);

        // In order to ensure the scroll Pane object appears in user's window,
        // set a preferred size to it.
        scrollPane.setPreferredSize(new Dimension(560, 600));

        // Lines will be wrapped if they are too long to fit within the
        // allocated width
        textArea.setLineWrap(true);

        // Lines will be wrapped at word boundaries (whitespace) if they are
        // too long to fit within the allocated width
        textArea.setWrapStyleWord(true);

        // Text area is not editable
        textArea.setEditable(false);


        // A vertical scroll bar on our pane, as text is added to it
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // add a Text Field for user input, make sure the
        // text area stays on the last line as subsequent lines are
        // added and auto-scrolls
        final JTextField userInputField = new JTextField(30);

        //add a menu bar to the top of the gui
        final JMenuBar mb;
        final JMenu x;
        final JMenuItem m1, m2, m3;
        mb = new JMenuBar();

        //name the menu bar and the three options it will have
        x = new JMenu("Options");
        m1 = new JMenuItem("Help");
        m2 = new JMenuItem("Disconnect");
        m3 = new JMenuItem("Exit");

        //add listener to menu items
        m1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                textArea.append("Welcome to the Java Chat Client." + "\n");
                textArea.append("Enter a message in the text box and press enter to send." + "\n");
                textArea.append("Selecting disconnect from the menu will disconnect you from the server and close the client." + "\n");
                textArea.append("Selecting Exit will exit the Java Chat Client " + "\n");
            }
        });
        //if disconnect is pressed on the menu
        m2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                disconnect = true;
                //textArea.append("Disconnect pressed" + "\n");
            }
        });
        //if Exit is pressed on the menu
        m3.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                System.exit(0);
            }

        });

        //add the items create to the menu bar
        x.add(m1);
        x.add(m2);
        x.add(m3);

        //add the menu bar to the entire gui and set the size
        mb.add(x);
        this.setJMenuBar(mb);
        this.setSize(500,500);
        this.setVisible(true);

        userInputField.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                //get the text from the textfield
                fromUser = userInputField.getText();
                hasText = true;

                //Reset the text field to "" each time the user presses Enter
                userInputField.setText("");
            }
        });

        this.setLayout(new FlowLayout());
        //adds and centers the text field to the frame
        this.add(userInputField, SwingConstants.CENTER);
        //adds and centers the scroll pane to the frame
        this.add(scrollPane, SwingConstants.CENTER);

        // Set size, default close operation, resizability boolean and overall contents visibility
        this.setSize(600, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setVisible(true);
    }

    public String inputReceived(){
        try {
            while (!this.hasText){
                Thread.sleep(5);
            }
            this.hasText = false;
            return this.fromUser;
        } catch (Exception e){
            System.err.println(e);
        }
        return "";
    }

    public boolean leave(){
        try {
            while (!disconnect){
                Thread.sleep(2);
            }
            System.err.printf("Received disconnect signal.\n");
            return disconnect; //Should always return true if it gets here
        } catch (Exception e){
            System.err.println(e);
        }

        return false;
    }

    public boolean printToGui(String output){
        System.out.printf("Value: %s\n", output);
        if (output != null) {
            textArea.append(output);
            textArea.append("\n");
            //The pane auto-scrolls with each new response added
            textArea.setCaretPosition(textArea.getDocument().getLength());
            return true;
        }
        else {
            return false;
        }
    }

    //public static void main(String[] args) {
    //    new gui();
    //}
}
