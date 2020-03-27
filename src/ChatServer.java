import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatServer extends Thread implements ActionListener {
    // New instance of the bot interface
    AvayaAliceBot bot;

    JFrame serverWindow;
    JPanel chatPanel, textBoxPanel;

    JTextArea chatTextArea;
    JScrollPane scrollBar;

    JLabel textBoxLabel;
    JScrollPane textScrollBar;
    JButton sendButton;
    JTextArea textBox;

    ServerSocket serverSocket;
    Socket clientSocket;

    BufferedReader userIn;
    PrintWriter serverOut;

    String clientQuery;
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public void createWindow() {
        // Create a new JFrame
        serverWindow = new JFrame("Server Program");

        // Define its size
        serverWindow.setSize(520,510);

        // Let it be seen by the user
        serverWindow.setLayout(null);
        serverWindow.setVisible(true);

        // Exit program on window close
        serverWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a chat panel to view chat history
        chatPanel = new JPanel();
        chatPanel.setBounds(0, 0, 500, 400);
        chatPanel.setLayout(new FlowLayout());

        // TextBox panel may be defunct for server side
        // We want to generate responses from bot
        textBoxPanel = new JPanel();
        textBoxPanel.setBounds(0, 400, 500, 100);
        textBoxPanel.setLayout(new FlowLayout());

        // Add panels to JFrame
        serverWindow.add(chatPanel);
        serverWindow.add(textBoxPanel);

        // Create text box to hold chat history
        chatTextArea = new JTextArea(24, 40);
        chatTextArea.setEditable(false);
        chatTextArea.setLineWrap(true);
        chatPanel.add(chatTextArea);

        // Scroll bar to let users move up and down the chat history
        scrollBar = new JScrollPane(chatTextArea);
        scrollBar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        chatPanel.add(scrollBar);
    }

    public static void main(String[] args) {
        // Create a new instance of the chat server
        ChatServer server = new ChatServer();
        // Create the GUI
        server.createWindow();
        // Start the thread
        server.start();
    }

    public void run() {
        try {

            // Initialise Bot
            bot = new AvayaAliceBot();
            bot.createBot("Avaya");
            bot.createChatSession();

            // Create a new server on port 8888
            serverSocket = new ServerSocket(8888);
            System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");

            clientSocket = serverSocket.accept();
            System.out.println("Connected to " + clientSocket.getRemoteSocketAddress());
            userIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            serverOut = new PrintWriter(clientSocket.getOutputStream(), true);

            while (true) {
                // Read user query from client instance
                clientQuery = userIn.readLine();
                // Write the text out to the UI
                chatTextArea.setText(chatTextArea.getText() + dateFormat.format(new Date()) + " Client: " + clientQuery + "\n");
                String botResp = bot.generateResponse(clientQuery);
                serverOut.println(botResp);
                chatTextArea.setText(chatTextArea.getText() + dateFormat.format(new Date()) + " AvayaBot: " + botResp + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            clientQuery = textBox.getText();
            serverOut.println(clientQuery);
            chatTextArea.setText(chatTextArea.getText() + dateFormat.format(new Date()) + " AvayaBot: " + clientQuery + "\n");

        }
        textBox.setText("");
    }
}