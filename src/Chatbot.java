import java.io.File;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.History;
// MagicBooleans contains
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.utils.IOUtils;

public class  Chatbot {
    // TRACE_MODE set to true for debugging
    // false for production
    private static final boolean TRACE_MODE = true;
    // Give the bot a name
    static String botName = "Avaya";

    public static void main(String[] args) {
        try {
            // Get the location of the bot details - the AIML files
            String resourcesPath = getResourcesPath();
            System.out.println("Resource path: " + resourcesPath);
            // TRACE_MODE is a toggle for a debug mode, which prints more info to the user
            MagicBooleans.trace_mode = TRACE_MODE;
            // Create the bot from the Alice Bot code base
            Bot bot = new Bot(botName, resourcesPath);
            // Create a new ChatSession with the bot
            Chat chatSession = new Chat(bot);
            // Write out note stats to console
            bot.brain.nodeStats();
            String textLine = "";

            while(true) {
                // Read the text in from the human
                System.out.print("Human : ");
                // An IO function that reads in the input from a user
                textLine = IOUtils.readInputTextLine();
                // This line defines a specific null input for the chatbot
                if ((textLine == null) || (textLine.length() < 1))
                    textLine = MagicStrings.null_input;
                // Quits bot
                if (textLine.equals("q")) {
                    System.exit(0);
                }
                // Quits input with writing info
                else if (textLine.equals("wq")) {
                    bot.writeQuit();
                    System.exit(0);
                }
                else {
                    String request = textLine;
                    // Prints out debugging data if trace_mode is true
                    if (MagicBooleans.trace_mode) {
                        System.out.println("State: " + request + " That: " + ((History) chatSession.thatHistory.get(0)).get(0) + " Topic: " + chatSession.predicates.get("topic"));
                    }
                    // Uses Alice Bot to find an appropriate response from either the AIML files, or the default bot files
                    String response = chatSession.multisentenceRespond(request);
                    // Tidying up responses from bot due to storing sensitive character in XML
                    while (response.contains("&lt;")) {
                        response = response.replace("&lt;", "<");
                    }
                    while (response.contains("&gt;")) {
                        response = response.replace("&gt;", ">");
                    }
                    // Prints out response to console
                    System.out.println(botName + " : " + response);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // This function just returns the resources folder for the bot
    // It contextually determines it based on the operating system.
    private static String getResourcesPath() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        //System.out.println(path);
        //String resourcesPath = path + File.separator + "src" + File.separator + "main" + File.separator + "resources";
        String resourcesPath = path + File.separator + "src";
        return resourcesPath;
    }
}