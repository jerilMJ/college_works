package mini_programs.file_merge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

class FileMerge {
    private static FileReader reader;
    private static BufferedReader bReader;

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println(
                    "Please provide path to files as arguments in the format <file_1> <file_2> <merged_file>.");
            return;
        }

        String file1Path = args[0];
        String file2Path = args[1];
        String mergedFilePath = args[2];

        try {
            FileWriter writer = new FileWriter(mergedFilePath);
            BufferedWriter bWriter = new BufferedWriter(writer);

            Iterator<String> file1Lines = getLines(file1Path);
            Iterator<String> file2Lines = getLines(file2Path);

            ArrayList<Iterator<String>> fileLines = new ArrayList<Iterator<String>>(
                    Arrays.asList(file1Lines, file2Lines));

            for (Iterator<String> lines : fileLines) {
                while (lines.hasNext()) {
                    bWriter.write(lines.next());
                    bWriter.write('\n');
                }
            }

            bReader.close();
            bWriter.close();

            System.out.println("\n" + file1Path + " contents: \n");
            printFile(file1Path);
            System.out.println("\n" + file2Path + " contents: \n");
            printFile(file2Path);
            System.out.println("\n" + mergedFilePath + " contents: \n");
            printFile(mergedFilePath);
        } catch (FileNotFoundException fnfe) {
            System.out.println("Some files do not exist.");
        } catch (IOException ioe) {
            System.out.println("Unexpected error occurred.");
        }
    }

    private static Iterator<String> getLines(String filePath) throws FileNotFoundException {
        reader = new FileReader(filePath);
        bReader = new BufferedReader(reader);

        Iterator<String> lines = bReader.lines().iterator();

        return lines;
    }

    private static void printFile(String filePath) throws FileNotFoundException {
        Iterator<String> lines = getLines(filePath);

        while (lines.hasNext()) {
            System.out.println(lines.next());
        }
    }
}