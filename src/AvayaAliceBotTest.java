
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import org.alicebot.ab.*;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

// Test class for testing bot interface class
public class AvayaAliceBotTest {


    static ArrayList GREETINGS = new ArrayList<String>(
            Arrays.asList("Hello!", "Hi There! How can I help you today?", "Hello! Are you having trouble with anything?", "How are you? What can I help you with today?", "Is there anything I can help you with?")
    );

    static ArrayList FAREWELLS = new ArrayList<String>(
            Arrays.asList("Goodbye.", "Thank you very much! Bye.", "Goodbye! Glad to have helped you.", "I hope we helped you as best we could.", "Have a wonderful day.")
    );

    static ArrayList HELP = new ArrayList<String>(
            Arrays.asList("It seems you're having trouble with something. Can you describe your issue.", "I'm sorry to hear that. Please describe your issue in greater detail.", "Let's see if we can help you. Please describe your issue in greater detail")
    );

    static ArrayList UNABLE = new ArrayList<String>(
            Arrays.asList("I can't seem to find a solution for your issue. Would you like to talk to a representative?", "There doesn't seem to be anything available for your issue. Do you want to connect to an Agent?")
    );

    // Creating new instances for each class type
    AvayaAliceBot testBot;
    Bot expectedBot;
    Chat expectedChat;

    @Before
    public void setUp(){
        // Create a new instance of the Bot class for each test
        testBot = new AvayaAliceBot();
        expectedBot = testBot.createBot("Avaya");
        expectedChat = testBot.createChatSession(expectedBot);
    }

    @After
    public void tearDown(){
        // Nullify the instance created for each test
        // This is to prevent the class remembering any data
        // These tests should all be passable within a vacuum
        testBot = null;
        expectedBot = null;
        expectedChat = null;
    }

    @Test
    public void createBot() {
        // Creating an Avaya bot instance and comparing it to the version created in the AvayaAliceBot instance
        Bot b = testBot.createBot("Avaya");
        // We want to test that the bot class is being instantiated correctly
        assertNotNull(b);
    }

    @Test
    public void createChatSession() {
        // Testing to see if chat sessions created are the same
        Bot b = testBot.createBot("Avaya");
        Chat c = testBot.createChatSession(b);
        // We want to see that both classes are being successfully created from the interfacing class
        assertNotNull(b);
        assertNotNull(c);
    }

    @Test
    public void generateIncorrectResponse() {
        // Make sure the bot is not creating a null value
        assertNotNull(testBot);

        // Testing for correct response to unknown input
        assertEquals("I have no answer for that.", testBot.generateResponse("Dummy text"));
        assertNotEquals("I have no answer for that.", testBot.generateResponse("Hello"));

    }

    @Test
    public void generateGreetingResponse() {
        // Make sure the bot is not creating a null value
        assertNotNull(testBot);
        assertNotNull(expectedBot);
        assertNotNull(expectedChat);

        // Generate responses for a list of strings, using the AvayaAliceBot interfacing class
        String resp1 = testBot.generateResponse(" HELLO ");
        String resp2 = testBot.generateResponse("HI");
        String resp3 = testBot.generateResponse("H ELLO");
        String resp4 = testBot.generateResponse("Hi, how are you?");
        String resp5 = testBot.generateResponse(" Hi, how are you?");

        // Print these responses out for manual verification
        System.out.println(resp1);
        System.out.println(resp2);
        System.out.println(resp3);
        System.out.println(resp4);
        System.out.println(resp5);

        // Testing for correct response to unknown input
        // We want to test that the response to a message given that we know SHOULD warrent a one of the responses works as intended
        assertTrue(GREETINGS.contains(resp1));
        assertTrue(GREETINGS.contains(resp2));
        assertFalse(GREETINGS.contains(resp3));
        assertTrue(GREETINGS.contains(resp4));
        assertTrue(GREETINGS.contains(resp5));
    }


    @Test
    public void generateFarewellResponse() {
        // Make sure the bot is not creating a null value
        assertNotNull(testBot);
        assertNotNull(expectedBot);
        assertNotNull(expectedChat);

        // Generate responses for a list of strings, using the AvayaAliceBot interfacing class
        String resp1 = testBot.generateResponse(" GOODBYE ");
        String resp2 = testBot.generateResponse("BYE");
        String resp3 = testBot.generateResponse("bye thanks");
        String resp4 = testBot.generateResponse("byeee now");
        String resp5 = testBot.generateResponse(" thank you goodbye");

        System.out.println(resp1);
        System.out.println(resp2);
        System.out.println(resp3);
        System.out.println(resp4);
        System.out.println(resp5);

        // We want to test that the response to a message given that we know SHOULD warrent a one of the responses works as intended

        assertTrue(FAREWELLS.contains(resp1));
        assertTrue(FAREWELLS.contains(resp2));
        assertTrue(FAREWELLS.contains(resp3));
        assertFalse(FAREWELLS.contains(resp4));
        assertTrue(FAREWELLS.contains(resp5));
    }


    @Test
    public void generateHelpResponse() {
        // Make sure the bot is not creating a null value
        assertNotNull(testBot);
        assertNotNull(expectedBot);
        assertNotNull(expectedChat);

        // Generate responses for a list of strings, using the AvayaAliceBot interfacing class
        String resp1 = testBot.generateResponse(" I need help with my computer ");
        String resp2 = testBot.generateResponse("Can you help me?");
        String resp3 = testBot.generateResponse("I had an issue with the Server?");
        String resp4 = testBot.generateResponse("I'm having trouble with Avaya's Contact Centre");
        String resp5 = testBot.generateResponse(" help me");

        System.out.println(resp1);
        System.out.println(resp2);
        System.out.println(resp3);
        System.out.println(resp4);
        System.out.println(resp5);

        // We want to test that the response to a message given that we know SHOULD warrent a one of the responses works as intended

        assertTrue(HELP.contains(resp1));
        assertTrue(HELP.contains(resp2));
        assertTrue(HELP.contains(resp3));
        assertTrue(HELP.contains(resp4));
        assertTrue(HELP.contains(resp5));
    }


    @Test
    public void generateUnableResponse() {
        // Make sure the bot is not creating a null value
        assertNotNull(testBot);
        assertNotNull(expectedBot);
        assertNotNull(expectedChat);

        // Generate responses for a list of strings, using the AvayaAliceBot interfacing class
        String resp1 = testBot.generateResponse(" CANNOT ");
        String resp2 = testBot.generateResponse("CAN'T");
        String resp3 = testBot.generateResponse("cant");
        String resp4 = testBot.generateResponse("I can't find any help");
        String resp5 = testBot.generateResponse(" can't help me");

        System.out.println(resp1);
        System.out.println(resp2);
        System.out.println(resp3);
        System.out.println(resp4);
        System.out.println(resp5);

        // We want to test that the response to a message given that we know SHOULD warrent a one of the responses works as intended

        assertTrue(UNABLE.contains(resp1));
        assertTrue(UNABLE.contains(resp2));
        assertTrue(UNABLE.contains(resp3));
        assertTrue(UNABLE.contains(resp4));
        assertTrue(UNABLE.contains(resp5));
    }
}
