/**
 * 816024135
 * Matthew Roodal
 * Assignment 1
 */
public class ChatBotGenerator
{
    //https://www.codecademy.com/forum_questions/51461bd65e90f4eda1007271
    //https://www.w3schools.com/java/java_switch.asp
    public String generateChatBotLLM(int LLMCodeNumber){
        switch(LLMCodeNumber){
            case 1:
                return "LLaMa";
            case 2:
                return "Mistral7B";
            case 3:
                return "Bard";
            case 4:
                return "Claude";
            case 5:
                return "Solar";
            default:
                return "ChatGPT-3.5";
        }
    }
    //public String generateChatBotLLM(String botName){
    //    return botName;
    //}
}
