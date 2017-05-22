package hw3;

import java.io.*;
import java.util.Scanner;


public class JavascriptFormatterRunner {
    private static Scanner s = new Scanner(System.in);
    public static void main(String[] args) {
        try {
            System.out.println("Welcome to the Javascript Formatter.");
            System.out.print("Please Enter a filename: ");
            String inputFile = s.nextLine();
            System.out.print("Please Enter an output filename: ");
            String outputFile = s.nextLine();

            StringBuffer in = new StringBuffer();
            String curr = null;
            FileReader file = new FileReader(inputFile);
            BufferedReader inReader = new BufferedReader(file);
            while((curr = inReader.readLine()) != null) {
                curr = curr.replace("\n","");
                curr = curr.replace("\t","");
                in.append(curr);
            }
            inReader.close();
            JavascriptFormatter jsFormatter = new JavascriptFormatter();
            String out = jsFormatter.format(in.toString());
            if(jsFormatter.getErrorMsg() != null) {
                System.out.println("ERROR: " + jsFormatter.getErrorMsg().replace("\n",""));
            }
            // write output to output file
            FileWriter outputWriter = new FileWriter(outputFile);
            BufferedWriter outReader = new BufferedWriter(outputWriter);
            outReader.append(out);
            outReader.close();
            System.out.println("Wrote output to " + outputFile);
            System.out.print("Ending program.");

        } catch(Exception ex) {
            ex.printStackTrace();
            System.out.println("Unable to open file.");
        }
    }
}

