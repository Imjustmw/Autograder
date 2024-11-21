/**
 * 816024135
 * Matthew Roodal
 * Assignment 1
 */
import java.util.ArrayList;
import java.util.Random;
public class ChatBot
{
    private String chatBotName;
    private int numResponsesGenerated;
    private static int messageLimit = 10;
    private static int messageNumber = 0;
    
    
    //Accessors 
        public String getChatBotName(){
        return chatBotName;
    }
    
    public int getNumResponsesGenerated(){
        return numResponsesGenerated;
    }
    //the limit for how many responses can be generated
    public int getMessageLimit(){
        return messageLimit;
    }
    //is the total responses generated
    public int getTotalNumResponsesGenerated(){
        return this.messageNumber;
    }
    
    //Mutators
    public void setChatBotName(String botName){
        this.chatBotName = botName;
    }

    public void setNumResponsesGenerated(int numResponse){
        this.numResponsesGenerated = numResponse;
    }

        public void setMessageNumber(int numMessage){
        this.messageNumber = numMessage;
    }
    
    //Constructor
    public ChatBot(){
        setChatBotName("ChatGPT-3.5");
        setNumResponsesGenerated(0);
    }
    
    public ChatBot(int LLMCode){
        if(LLMCode >= 0){
            ChatBotGenerator LLMNames = new ChatBotGenerator();
            setChatBotName(LLMNames.generateChatBotLLM(LLMCode));
            setNumResponsesGenerated(0);
        }
    }
    
    public int getTotalNumResponsesRemaining(){
        return (getMessageLimit() - getTotalNumResponsesGenerated());
    }
    
    public boolean limitReached(){
        return(getTotalNumResponsesGenerated() == getMessageLimit());
    }
    
    public String generateResponse(){
        ArrayList<String> responses = new ArrayList<>();
        responses.add(0, "Hey nice to meet you!");
        responses.add(1, "Did you pray today???");
        responses.add(2, "You seem very not chill");
        responses.add(3, "That was not very cash money of you");
        responses.add(4, "You should be ashamed of yourself for typing that to me");
        responses.add(5, "THE AUDACITY!!!!");
        
        Random r = new Random();
        int rand = r.nextInt(responses.size());
        
        return("(Message#"+getTotalNumResponsesGenerated()+")"+"Response from"
                +getChatBotName()+"\t>>"+responses.get(rand));
    }
    
    public String prompt(String requestMessage){
        if(!limitReached()){
            setMessageNumber(this.messageNumber + 1);
            setNumResponsesGenerated(this.numResponsesGenerated + 1);
            return generateResponse();
        }
        //just trying this out
        String dailyLimit = "Daily Limit Reached.";
        String waitTime = "Wait 24 hours to resume chatbot usage";
        return(dailyLimit.concat(waitTime));
    }
    
    public String toString(){
        return("ChatBot Name: "+getChatBotName()+"\tNumber of Messages Used: "+getNumResponsesGenerated());
    }
    

}
