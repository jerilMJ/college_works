package mini_programs.file_read_reverse;

import java.io.*;
import java.util.*;
import java.util.stream.*;

public class FileReadReverse {
    private static Reader reader;
    private static BufferedReader bReader;

    public static void main(String[] args) {
        String filePath = "./sample.txt";

        try {
            Iterator<String> lines = getLinesInReverse(filePath);
            while (lines.hasNext()) {
                System.out.println(lines.next());
            }
            bReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Make sure it exists. Error: " + e);
        } catch (IOException e) {
            System.out.println("Error reading file. Error: " + e);
        }
    }

    private static Iterator<String> getLinesInReverse(String filePath) throws FileNotFoundException, IOException {
        reader = new FileReader(filePath);
        bReader = new BufferedReader(reader);

        Stream<String> lineStream = bReader.lines();
        Iterator<String> lines = lineStream.collect(Collectors.toCollection(LinkedList::new)).descendingIterator();

        return lines;
    }
}