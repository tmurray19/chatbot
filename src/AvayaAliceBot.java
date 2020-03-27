// Import our bot of choice for testing, in this case its alicebot
import com.avaya.ccs.api.enums.Profile;
import com.avaya.ccs.core.Client;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.MagicStrings;
import java.io.File;

// Import the Avaya CCS API
import com.avaya.ccs.api.*;

// BotInterface.java
//
public class AvayaAliceBot implements BotInterface {
    // This is for use with Program AB and AIML

    Bot bot;
    Chat chatSession;

    Client avayaClient;

    // Give the bot a name
    static String botName = "Avaya";



    // Create a new bot instance
    public Bot createBot(String botName) {
        // Get the location of the bot details - the AIML files
        String resourcesPath = getResourcesPath();
        System.out.println("Resource path: " + resourcesPath);

        // Create the bot from the Alice Bot code base
        Bot bot = new Bot(botName, resourcesPath);

        // Create a new instance of an Avaya CCS Client
        //
        Profile.AgentDesktop();
        Client avayaClient = Client.create("testServer", );

        this.bot = bot;
        return bot;
    }

    // Create a chat session for the bot
    public Chat createChatSession() {
        Chat chatSession = new Chat(this.bot);
        // Write out note stats to console
        this.bot.brain.nodeStats();
        this.chatSession = chatSession;
        return chatSession;
    }

    // Create a chat session for the bot
    public Chat createChatSession(Bot b) {
        Chat chatSession = new Chat(b);
        // Write out note stats to console
        b.brain.nodeStats();
        this.chatSession = chatSession;
        return chatSession;
    }

    public String generateResponse(String query) {
        String response = "";
        if ((query == null) || (query.length() < 1)) {
            query = MagicStrings.null_input;
        }
        // Quits bot
        if (query.equals("q")) {
            System.exit(0);
        }
        else {
            String request = query;
            // Uses Alice Bot to find an appropriate response from either the AIML files, or the default bot files
            response = this.chatSession.multisentenceRespond(request);
            // Tidying up responses from bot due to storing sensitive character in XML
            while (response.contains("&lt;")) {
                response = response.replace("&lt;", "<");
            }
            while (response.contains("&gt;")) {
                response = response.replace("&gt;", ">");
            }
        }
        return response;
    }

    // This function just returns the resources folder for the bot
    // It contextually determines it based on the operating system.
    public static String getResourcesPath() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        //System.out.println(path);
        //String resourcesPath = path + File.separator + "src" + File.separator + "main" + File.separator + "resources";
        String resourcesPath = path + File.separator + "src";
        return resourcesPath;
    }

}