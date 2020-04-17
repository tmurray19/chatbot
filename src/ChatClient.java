import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatClient extends Thread implements ActionListener {
    JFrame clientWindow;
    JPanel chatPanel, textBoxPanel;

    JTextArea chatTextArea;
    JScrollPane chatScrollBar;

    JScrollPane textScrollBar;
    JLabel textBoxLabel;
    JTextArea textBox;
    JButton sendButton;

    Socket clientSocket;
    
    BufferedReader serverIn;
    PrintWriter userOut;

    String serverQuery;
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public void createWindow() {
        // Create a new client window
        clientWindow = new JFrame("Avaya CCCS Chatbot Prototype");

        // Set window size
        clientWindow.setSize(500, 500);

        clientWindow.setLayout(null);
        clientWindow.setVisible(true);

        // Tell JFrame to quit when the window is closed
        clientWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a new panel to hold the chat text between the bot and the user
        chatPanel = new JPanel();
        chatPanel.setBounds(0, 0, 500, 400);
        chatPanel.setLayout(new FlowLayout());

        // Create a new panel to hold the input text box for the user
        textBoxPanel = new JPanel();
        textBoxPanel.setBounds(0, 400, 500, 100);
        textBoxPanel.setLayout(new FlowLayout());

        // Add both panels to the window
        clientWindow.add(chatPanel);
        clientWindow.add(textBoxPanel);

        // Define a text area to show the chat history
        chatTextArea = new JTextArea(24, 40);
        // Make sure it is un editable
        chatTextArea.setEditable(false);
        // Turning on line wrapo
        chatTextArea.setLineWrap(true);
        // Add it to the chat panel 
        chatPanel.add(chatTextArea);

        // Giving the text history a scroll bar
        chatScrollBar = new JScrollPane(chatTextArea);
        // Scroll bar will always be shown
        chatScrollBar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        // Add it to the chat panel
        chatPanel.add(chatScrollBar);

        // Give the input box a label
        textBoxLabel = new JLabel("User:");
        textBoxPanel.add(textBoxLabel);

        // Define input as a text area
        textBox = new JTextArea(3, 24);
        // Turning line wrap on for the box
        textBox.setLineWrap(true);
        textBox.setWrapStyleWord(true);
        textBoxPanel.add(textBox);

        // Give text input a scroll bar
        textScrollBar = new JScrollPane(textBox);
        textBoxPanel.add(textScrollBar);

        sendButton = new JButton("Submit");
        sendButton.addActionListener(this);
        textBoxPanel.add(sendButton);


    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.createWindow();
        client.start();
    }

    public void run() {
        try {
            System.out.println("Connecting to localhost on port 8888");
            clientSocket = new Socket("localhost", 8888);
            System.out.println("Connected to " + clientSocket.getRemoteSocketAddress());

            // Create reader for
            serverIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            userOut = new PrintWriter(clientSocket.getOutputStream(), true);

            while (true) {
                // Read bot response from the server running bot instance
                serverQuery = serverIn.readLine();
                // Append the text from the server to whatever currently exists in the chat history,
                chatTextArea.setText(chatTextArea.getText() + dateFormat.format(new Date()) + " AvayaBot: " + serverQuery + "\n");
            }
        }
        // Standard error handling
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            // Get text from input text area
            serverQuery = textBox.getText();
            // Remove any delimiters such as tabs or new lines
            serverQuery = serverQuery.replaceAll("[\\t\\n\\r]+"," ");

            // Send to the server
            userOut.println(serverQuery);
            // Update the client chat box with the client message
            chatTextArea.setText(chatTextArea.getText() + dateFormat.format(new Date()) + " Client: " + serverQuery + "\n");

        }
        // Resets text box
        textBox.setText("");
    }

}