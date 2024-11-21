/**
 * 816024135
 * Matthew Roodal
 * Assignment 1
 */
import java.util.Random;
//import java.util.Scanner;
public class ChatBotSimulation
{
    public static void main(String[] args){
        System.out.println("Hello World!");
        
        ChatBotPlatform botPlatform = new ChatBotPlatform();

        for(int i = 0; i < 6; i++){     //goes through the for loop 6 times since there are six bots
            botPlatform.addChatBot(i);  //adds all 6 bots to the object
        }
        
        System.out.println(botPlatform.getChatBotList());
        
        
        Random r = new Random();
        //Scanner scan = new Scanner(System.in);
        //String message = 
        int i = 6;
        for(int j = 0; j < 15; j++){
            String message = "suh";
            System.out.println(botPlatform.interactWithBot(r.nextInt(i + 1), message)); //gets random bot and replies to message
        }
        //scan.close();
        System.out.println(botPlatform.getChatBotList());
    }
}
