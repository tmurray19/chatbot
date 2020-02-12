import java.io.File;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.History;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.utils.IOUtils;

public class Chatbot {
    // TRACE_MODE set to true for debugging
    // false for production
    private static final boolean TRACE_MODE = true;
    static String botName = "Super";

    public static void main(String[] args) {
        try {

            String resourcesPath = getResourcesPath();
            System.out.println("Reource path: " + resourcesPath);
            // TRACE_MODE toggles a debugging mode
            MagicBooleans.trace_mode = TRACE_MODE;
            // Create the bot from the AB.jar
            Bot bot = new Bot(botName, resourcesPath);
            // Create a new ChatSession with the bot
            Chat chatSession = new Chat(bot);
            bot.brain.nodeStats();
            String textLine = "";

            while(true) {
                // Read the text in from the human
                System.out.print("Human : ");
                // An IO function that reads in the input from a user
                textLine = IOUtils.readInputTextLine();
                // Null input
                if ((textLine == null) || (textLine.length() < 1))
                    textLine = MagicStrings.null_input;
                // Quits bot
                if (textLine.equals("q")) {
                    System.exit(0);
                }
                // Quits input with writing
                else if (textLine.equals("wq")) {
                    bot.writeQuit();
                    System.exit(0);
                }
                else {
                    String request = textLine;
                    // For debugging
                    if (MagicBooleans.trace_mode)
                        System.out.println("STATE=" + request + ":THAT=" + ((History) chatSession.thatHistory.get(0)).get(0) + ":TOPIC=" + chatSession.predicates.get("topic"));
                    // Generates a response from the user
                    String response = chatSession.multisentenceRespond(request);
                    while (response.contains("&lt;"))
                        response = response.replace("&lt;", "<");
                    while (response.contains("&gt;"))
                        response = response.replace("&gt;", ">");
                    System.out.println(botName + " : " + response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // This function just returns the resources folder for the bot
    // It contextually determines it based on the operating system.
    private static String getResourcesPath() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        System.out.println(path);
        String resourcesPath = path + File.separator + "src" + File.separator + "main" + File.separator + "resources";
        return resourcesPath;
    }
}