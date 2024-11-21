/**
 * 816024135
 * Matthew Roodal
 * Assignment 1
 */
import java.util.ArrayList;
public class ChatBotPlatform
{
    private ArrayList<ChatBot> bots = new ArrayList<ChatBot>();
    
    public ChatBotPlatform(){
        this.bots = new ArrayList<ChatBot>();
    }
    
    public boolean addChatBot(int LLMCode){
        if(!bots.isEmpty()){                    //checks if list is not empty before doing operations
            ChatBot defaultBot = bots.get(0);   
            if(defaultBot.limitReached()){
                return false;                   //if the limit is reached then cannot create new bot
            }
        }//else create the bot and return true
        ChatBot newBot = new ChatBot(LLMCode);
        bots.add(newBot);
        return true;
    }
    
    public String getChatBotList(){
        String format ="----------------------\n"+"Your ChatBots\n";
        
        if(!bots.isEmpty()){
            for(int i = 0; i < bots.size(); i++){
                ChatBot bot = bots.get(i);
                format += "Bot Number: "+i+" Chatbot Name: "+bot.getChatBotName()
                +"\tNumber Messages Used: "+bot.getNumResponsesGenerated()+"\n";
            }
            ChatBot bot = bots.get(0);
            format +="\nTotal Messages Used: "+bot.getTotalNumResponsesGenerated()
                   + " \nTotal Messages Remaining: "+bot.getTotalNumResponsesRemaining();
        }
        return format;
    }
    
    public String interactWithBot(int botNumber, String message){
        if(botNumber >= 0 && botNumber < bots.size()){
            ChatBot currBot = bots.get(botNumber);
            return currBot.prompt(message);
        }else{
            return "Incorrect Bot Number ("+botNumber+") Selected. Try Again.";
        }
    }
    
    
}
