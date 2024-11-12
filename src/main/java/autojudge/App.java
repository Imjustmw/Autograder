package autojudge;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {

    public static void main(String[] args) {

        // Step 1: Extract the ZIP file
        String zipFilePath = "/mnt/c/Users/Keith/documents/oop2_project/816032626.zip"; // Replace with the actual path
        String outputDir = "/mnt/c/Users/Keith/documents/oop2_project/assignments"; // Directory to store extracted files
        String extractedDir = FileExtractor.extractZipFile(zipFilePath, outputDir);
        if (extractedDir.isEmpty()) {
            System.out.println("Failed to extract the zip file.");
            return;
        }

        // Step 2: Gather .java files in the extracted directory
        ArrayList<File> javaFiles = ExtensionFilesGatherer.GrabFiles(extractedDir, ".java");

        // Step 3: Define ClassSpecs according to the criteria
        List<ClassSpec> classSpecs = new ArrayList<>();

        // Example setup for ChatBot class - Add specific method tests using TestMethod subclasses
        ClassSpec chatBotSpec = new ClassSpec(
            Arrays.asList(
                new MethodSpec("getChatBotNames", String[].class, List.of(), 1, "getChatBotName failed.", new TestMethod() {
                    @Override
                    public boolean test(Object instance, Object... params) {
                        try {
                            String[] name = (String[]) instance.getClass().getMethod("getChatBotNames").invoke(instance);
                            return name != null ;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }),
                new MethodSpec("generateResponse", String.class, List.of(), 5, "generateResponse failed.", new TestMethod() {
                    @Override
                    public boolean test(Object instance, Object... params) {
                        try {
                            String response = (String) instance.getClass().getMethod("generateResponse").invoke(instance);
                            return response != null && response.contains("ChatBot");
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                })
            ),
            Arrays.asList(
                new VariableSpec("chatBotName", "String", "Chatbot name variable", 1),
                new VariableSpec("numResponsesGenerated", "int", "Tracks responses generated", 1),
                new VariableSpec("messageLimit", "int", "Limits the messages", 3),
                new VariableSpec("messageNumber", "int", "Tracks message count", 2)
            ),
            Arrays.asList(new ConstructorSpec("ChatBot()",null,0)),
            36
        );

        // Example setup for ChatBotPlatform class
        ClassSpec chatBotPlatformSpec = new ClassSpec(
            Arrays.asList(
                new MethodSpec("getNumResponsesGenerated", int.class, List.of(), 1, "getNumResponsesGenerated failed.", new TestMethod() {
                    @Override
                    public boolean test(Object instance, Object... params) {
                        try {
                            int numResponses = (int) instance.getClass().getMethod("getNumResponsesGenerated").invoke(instance);
                            return numResponses >= 0;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }),
                new MethodSpec("limitReached", boolean.class, List.of(), 3, "limitReached failed.", new TestMethod() {
                    @Override
                    public boolean test(Object instance, Object... params) {
                        try {
                            boolean limit = (boolean) instance.getClass().getMethod("limitReached").invoke(instance);
                            return limit == true || limit == false; // Ensures boolean return
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                })
            ),
            Arrays.asList(
                new VariableSpec("bots", "ArrayList<ChatBot>", "Collection of ChatBots", 2)
            ),
            Arrays.asList(new ConstructorSpec("ChatBotPlatform()",null,0)),
            20
        );

        // Add ClassSpecs to the list
        classSpecs.add(chatBotSpec);
        classSpecs.add(chatBotPlatformSpec);

        // Step 4: Initialize CriteriaChecker for each ClassSpec
        List<CriteriaChecker> criteriaCheckers = new ArrayList<>();
        for (ClassSpec classSpec : classSpecs) {
            criteriaCheckers.add(new CriteriaChecker(classSpec));
        }

        // Step 5: Evaluate each .java file and collect results
        List<EvaluationResult> evaluationResults = new ArrayList<>();
        for (File javaFile : javaFiles) {
            for (CriteriaChecker checker : criteriaCheckers) {
                EvaluationResult result = checker.evaluateClass(javaFile);
                evaluationResults.add(result);
            }
        }

        // Step 6: Generate PDF report
        PDFCreator pdfCreator = new PDFCreator("evaluation_report.pdf");
        if (pdfCreator.CreatePDF()) {
            pdfCreator.AddTitle("Evaluation Report");

            for (EvaluationResult result : evaluationResults) {
                pdfCreator.WriteParagraph("Class: " + result.getClassName());
                pdfCreator.WriteParagraph("Score: " + result.GetOverallResult());
                for (String comment : result.getComments()) {
                    pdfCreator.WriteParagraph(comment);
                }
                pdfCreator.WriteParagraph("\n"); // Add spacing between classes
            }

            pdfCreator.Compile();
            System.out.println("Evaluation report generated as evaluation_report.pdf");
        } else {
            System.out.println("Failed to create PDF report.");
        }
    }
}

