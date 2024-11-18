package autojudge;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {
        String zipFiles = "/mnt/c/Users/Keith/documents/oop2_project/Zips";
        String outputFile = "/mnt/c/Users/Keith/documents/oop2_project/assignments";
       


        List<MethodSpec> methodSpecs = new ArrayList<>();

        // 1. getNumResponsesGenerated()
        methodSpecs.add(new MethodSpec(
                "getNumResponsesGenerated",
                "int",
                List.of(),
                2,
                "Failed to get correct number of responses generated.",
                new TestMethod() {
                    @Override
                    public boolean test(Object instance, Object... params) {
                        try {
                            Method method = instance.getClass().getMethod("getNumResponsesGenerated");
                            int actual = (int) method.invoke(instance);
                            return true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }));

        // 2. getTotalNumResponsesRemaining()
        methodSpecs.add(new MethodSpec(
                "getTotalNumResponsesRemaining",
                "int", // Correct return type
                List.of(), // No parameters
                3,
                "Failed to get correct number of remaining responses.",
                new TestMethod() {
                    @Override
                    public boolean test(Object instance, Object... params) {
                        try {
                            Method method = instance.getClass().getMethod("getTotalNumResponsesRemaining");
                            int actual = (int) method.invoke(instance);
                            return true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }));

        // 3. limitReached()
        methodSpecs.add(new MethodSpec(
                "limitReached",
                "boolean", // Correct return type
                List.of(), // No parameters
                3,
                "Failed to correctly identify if the limit is reached.",
                new TestMethod() {
                    @Override
                    public boolean test(Object instance, Object... params) {
                        try {
                            Method method = instance.getClass().getMethod("limitReached");
                            method.invoke(instance);
                            return true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }));

        // 4. generateResponse()
        methodSpecs.add(new MethodSpec(
                "generateResponse",
                "String", // Correct return type
                List.of(), // No parameters
                5,
                "Failed to generate correct response.",
                new TestMethod() {
                    @Override
                    public boolean test(Object instance, Object... params) {
                        try {
                            Method method = instance.getClass().getMethod("generateResponse");
                            String actual = (String) method.invoke(instance);
                            return actual != null;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }));

        // 5. prompt(String requestMessage)
        methodSpecs.add(new MethodSpec(
                "prompt",
                "String", // Correct return type
                List.of("String"), // Single parameter: String
                5,
                "Failed to process request message correctly.",
                new TestMethod() {
                    @Override
                    public boolean test(Object instance, Object... params) {
                        try {
                            Method method = instance.getClass().getMethod("prompt", String.class);
                            String actual = (String) method.invoke(instance, "test");
                            return actual != null;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }));

        // 6. toString()
        methodSpecs.add(new MethodSpec(
                "toString",
                "String", // Correct return type
                List.of(), // No parameters
                4,
                "Failed to produce correct string representation.",
                new TestMethod() {
                    @Override
                    public boolean test(Object instance, Object... params) {
                        try {
                            Method method = instance.getClass().getMethod("toString");
                            String actual = (String) method.invoke(instance);
                            return actual != null;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }));

        List<VariableSpec> variableSpecs = new ArrayList<>();

        variableSpecs.add(
                new VariableSpec("chatBotName", "String", " Stores the name of LLM that powers the chatbot", 1, false));
        variableSpecs
                .add(new VariableSpec("numResponsesGenerated", "int", "Keeps track of the number of responses \r\n" + //
                        "generated by the chatbot.", 1, false));
        variableSpecs.add(new VariableSpec(
                "messageLimit",
                "int",
                "Stores the fixed total number of messages that \r\n" + //
                        "can be generated overall by all chatbots. \r\n" + //
                        "Initialised to 10.",
                3, true));
        variableSpecs.add(new VariableSpec("",
                "messageNumber",
                "Keeps track of the number of messages \r\n" + //
                        "generated overall by all chatbots. Initialised to 0.",
                2,
                false));

        ConstructorSpec constructorSpec = new ConstructorSpec("ChatBot",
                List.of("int"), 3);
        ConstructorSpec constructorSpec1 = new ConstructorSpec("ChatBot", null, 3);

        ClassSpec chatBot = new ClassSpec("ChatBot",methodSpecs, variableSpecs, List.of(constructorSpec, constructorSpec1), 36);

        // ChatBotPlatform Class Specification
        List<MethodSpec> methodSpecsPlatform = new ArrayList<>();

        // Constructor: ChatBotPlatform()
        methodSpecsPlatform.add(new MethodSpec(
                "ChatBotPlatform",
                null, // Constructor (no return type)
                List.of(), // No parameters
                2, // Marks
                "Constructor initializes the bots collection.",
                null // No test implementation for constructors
        ));

        // addChatBot(int LLMCode)
        methodSpecsPlatform.add(new MethodSpec(
                "addChatBot",
                "boolean", // Return type
                List.of("int"), // Single parameter: int (LLMCode)
                5, // Marks
                "Creates and adds a new ChatBot object to the bots collection based on the supplied LLM code. Returns true if successful, false otherwise.",
                new TestMethod() {
                    @Override
                    public boolean test(Object instance, Object... params) {
                        try {
                            Method method = instance.getClass().getMethod("addChatBot", int.class);
                            boolean result = (boolean) method.invoke(instance, params[0]);
                            return result; // Check if returned true
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }));

        // getChatBotList()
        methodSpecsPlatform.add(new MethodSpec(
                "getChatBotList",
                "String", // Return type
                List.of(), // No parameters
                6, // Marks
                "Returns a formatted string containing information about all chatbots managed by the ChatBotPlatform.",
                new TestMethod() {
                    @Override
                    public boolean test(Object instance, Object... params) {
                        try {
                            Method method = instance.getClass().getMethod("getChatBotList");
                            String result = (String) method.invoke(instance);
                            return result != null && !result.isEmpty(); // Check if result is valid
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }));

        // interactWithBot(int botNumber, String message)
        methodSpecsPlatform.add(new MethodSpec(
                "interactWithBot",
                "String", // Return type
                List.of("int", "String"), // Parameters: int (botNumber), String (message)
                5, // Marks
                "Passes a message to a given chatbot and returns its response. Handles incorrect bot numbers gracefully.",
                new TestMethod() {
                    @Override
                    public boolean test(Object instance, Object... params) {
                        try {
                            Method method = instance.getClass().getMethod("interactWithBot", int.class, String.class);
                            String result = (String) method.invoke(instance, params[0], params[1]);
                            return result != null; // Check if result is not null
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }));

        List<VariableSpec> variableSpecsPlatform = new ArrayList<>();
        variableSpecsPlatform.add(new VariableSpec(
                "bots",
                "ArrayList<ChatBot>",
                "Stores multiple ChatBot objects.",
                2, // Marks
                false // Not a constant
        ));

        ClassSpec chatBotPlatform = new ClassSpec("ChatBotPlatform",methodSpecsPlatform, variableSpecsPlatform, List.of(), 20);

        // ChatBotGenerator Class Specification
        List<MethodSpec> methodSpecsGenerator = new ArrayList<>();

        // generateChatBotLLM(int LLMCodeNumber)
        methodSpecsGenerator.add(new MethodSpec(
                "generateChatBotLLM",
                "String", // Return type
                List.of("int"), // Single parameter: int (LLMCodeNumber)
                7, // Marks
                "A class method that returns the name of an LLM based on a supplied integer code. Default: 'ChatGPT-3.5'.",
                new TestMethod() {
                    @Override
                    public boolean test(Object instance, Object... params) {
                        try {
                            Method method = instance.getClass().getMethod("generateChatBotLLM", int.class);
                            String result = (String) method.invoke(null, params[0]); // Static method
                            return result != null && !result.isEmpty(); // Check if result is valid
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }));

        List<VariableSpec> variableSpecsGenerator = new ArrayList<>();
        // No attributes specified for ChatBotGenerator

        ClassSpec chatBotGenerator = new ClassSpec("ChatBotGenerator",methodSpecsGenerator, variableSpecsGenerator, List.of(), 7);

        // ChatBotSimulation Class Specification
        List<MethodSpec> methodSpecsSimulation = new ArrayList<>();

        // main(String[] args)
        methodSpecsSimulation.add(new MethodSpec(
                "main",
                "void", // Return type
                List.of("String[]"), // Parameter: String[] args
                12, // Marks
                "Executes the main simulation workflow. The method includes the following steps: \n" +
                        "1. Prints 'Hello World!' to the screen. (1 mark)\n" +
                        "2. Declares and initializes a ChatBotPlatform object. (1 mark)\n" +
                        "3. Adds several ChatBot objects (at least one of each kind) to the ChatBotPlatform. (2 marks)\n"
                        +
                        "4. Prints a list of all ChatBots managed by the ChatBotPlatform with summary statistics. (2 marks)\n"
                        +
                        "5. Interacts up to 15 times with random ChatBots by sending messages and prints responses. (4 marks)\n"
                        +
                        "6. Prints a final list of all ChatBots managed by the ChatBotPlatform with summary statistics. (2 marks)",
                new TestMethod() {
                    @Override
                    public boolean test(Object instance, Object... params) {
                        try {
                            Method method = instance.getClass().getMethod("main", String[].class);

                            // Simulate invocation of the main method
                            method.invoke(null, (Object) new String[] {});

                            // Since it's a simulation, returning true to assume proper execution
                            return true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }));

        List<VariableSpec> variableSpecsSimulation = new ArrayList<>();
        // No attributes needed for ChatBotSimulation as per the specifications.

        ClassSpec chatBotSimulation = new ClassSpec("ChatBotSimulation",
                methodSpecsSimulation, // Methods
                variableSpecsSimulation, // Variables
                List.of(), // Constructors
                12 // Total marks
        );

        List<ClassSpec> classSpecs  = List.of(chatBot,chatBotGenerator,chatBotPlatform,chatBotSimulation);
        
        ArrayList<String> files = FileExtractor.extractAllZipFiles(zipFiles, outputFile);
        for(String file : files){
            PDFCreator pdfCreator = new PDFCreator(file + "/result.pdf");
            pdfCreator.CreatePDF();
            ArrayList<File> javaFiles = ExtensionFilesGatherer.GrabFiles(file, "java");
            List<File> classFiles = JavaCompilerClass.compileJavaFiles(javaFiles.toArray(new File[javaFiles.size()]));
            for(File classFile : classFiles){
                
               String className = classFile.getName().substring(0, classFile.getName().lastIndexOf('.'));
               pdfCreator.AddTitle(className);
               boolean found = false;
               for(ClassSpec classSpec : classSpecs){
                    if(classSpec.getClassName().equals(className)){
                        CriteriaChecker criteriaChecker = new CriteriaChecker(classSpec);
                        EvaluationResult result = criteriaChecker.evaluateClass(classFile);
                        
                        for(String string: result.getComments()){
                            pdfCreator.WriteParagraph(string);
                        }
                        pdfCreator.WriteParagraph(result.GetOverallResult());
                        found = true;
                        break;
                    }
               }
               if(!found)
                    pdfCreator.WriteParagraph(className + " Not found");
               
            }
            pdfCreator.Compile();
        }
       
    }
}
