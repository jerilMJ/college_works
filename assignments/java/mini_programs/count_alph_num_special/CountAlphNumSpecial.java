package mini_programs.count_alph_num_special;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class CountAlphNumSpecial {
    private static Reader reader;
    private static BufferedReader bReader;

    public static void main(String[] args) {
        String filePath = "./sample.txt";
        String content = "";

        Pattern alphPattern = Pattern.compile("[a-z]|[A-Z]");
        Pattern numPattern = Pattern.compile("\\d+");
        Pattern splPattern = Pattern.compile("[^A-Za-z0-9 ]");

        int alphCount = 0;
        int numCount = 0;
        int splCount = 0;

        try {
            Iterator<String> lines = getLines(filePath);
            while (lines.hasNext()) {
                String line = lines.next();

                content += line;
                content += "\n";

                Matcher alphMatcher = alphPattern.matcher(line);
                Matcher numMatcher = numPattern.matcher(line);
                Matcher splMatcher = splPattern.matcher(line);

                while (alphMatcher.find())
                    alphCount++;

                while (numMatcher.find())
                    numCount++;

                while (splMatcher.find())
                    splCount++;

            }

            bReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Make sure it exists. Error: " + e);
        } catch (IOException e) {
            System.out.println("Error reading file. Error: " + e);
        }

        System.out.println("Contents of " + filePath + "\n");
        System.out.print(content);
        System.out.println("----------------------------------");

        System.out.println("Total alphabets         : " + alphCount);
        System.out.println("Total numbers           : " + numCount);
        System.out.println("Total special characters: " + splCount);

    }

    private static Iterator<String> getLines(String filePath) throws FileNotFoundException, IOException {
        reader = new FileReader(filePath);
        bReader = new BufferedReader(reader);

        Iterator<String> lines = bReader.lines().iterator();

        return lines;
    }
}