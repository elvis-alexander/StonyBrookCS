import java.io.FileOutputStream;
import java.util.Scanner;

/**
 * Simple Java utility for creating text files in various UTF and ASCII encodings.
 */
public class Encode {

    private static Encoding encoding;
    private static Boolean hasBom = false;
    private static ByteOrderMarking bom;
    private static String outputPath = "out.txt";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Parse the command line arguments
        Encode.parseArgs(args);
        try(FileOutputStream fos = new FileOutputStream(outputPath)) {
            // Add the BOM if the user wants it
            if(hasBom) {
                fos.write(bom.value());
            }
            // Ask the user for input
            System.out.println("Enter the text you want to encode. When done press ctrl-d");
            Scanner keyboard = new Scanner(System.in);
            while(keyboard.hasNextLine()) {
                String next = keyboard.nextLine();
                if(!next.isEmpty()) {
                    // Get the bytes in using the encodings value.
                    fos.write(next.getBytes(Encode.encoding.value));
                }
                // Write out the new line character for the system
                if(keyboard.hasNextLine()) {
                    fos.write(((String)System.getProperty("line.separator")).getBytes(Encode.encoding.value));
                }
            }
            // We are done close the keyboard
            keyboard.close();
            System.out.println(String.format("The file %s has been successfully created.", outputPath));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Attempts to parse command line arguments.
     * @param args Arguments passed to the main function.
     */
    public static void parseArgs(String[] args) {
        // Flags for required parameters
        Boolean encoding = false;
        // Start Searching args
        for(int i = 0; i < args.length; i++) {
            switch(args[i]) {
                case "-h":
                    Encode.usage();
                    System.exit(0);
                    break;
                case "-b":
                    Encode.hasBom = true;
                    break;
                case "-e":
                    encoding = true;
                    Encode.encoding = Encoding.fromValue(args[i+1]);
                    i++; // Move i by 1 since we used the next argument
                    break;
                case "-o":
                    Encode.outputPath = args[i+1];
                    i++;
                    break;
                default:
                    /* Alert the user that the argument doesnt match then quit */
                    System.out.println(String.format("The command line argument `%s` is not reconized", args[i]));
                    Encode.usage();
                    System.exit(1);
                    break;
            }
        }

        if(Encode.hasBom && Encode.encoding != null) {
            switch(Encode.encoding) {
                case ASCII:
                    Encode.bom = ByteOrderMarking.ASCII;
                    break;
                case UTF_8:
                    Encode.bom = ByteOrderMarking.UTF_8;
                    break;
                case UTF_16LE:
                    Encode.bom = ByteOrderMarking.UTF_16LE;
                    break;
                case UTF_16BE:
                    Encode.bom = ByteOrderMarking.UTF_16BE;
                    break;
                default:
                    throw new RuntimeException();
            }
        }

        // Check to make sure the required encoding was provided
        if(!encoding) {
            System.out.println("You did not provide an encoding for the output file.");
            Encode.usage();
            System.exit(1);
        }
    }

    /**
     * Simple method that prints out the usage.
     */
    private static void usage() {
        System.out.println("usage: java Encode [-h] [-b] [-o output_file] [-e encoding]\n"
                + "-h       Displays this usage Menu\n"
                + "-b       Adds the Byte Order Marking to the output file\n"
                + "-o       The output file to write to\n"
                + "-e       The encoding to encode the output file with\n"
                + "             US-ASCII, ASCII, UTF-8, UTF-16LE, UTF-16BE");
    }

    private enum Encoding {
        ASCII("US-ASCII"),
        UTF_8("UTF-8"),
        UTF_16LE("UTF-16LE"),
        UTF_16BE("UTF-16BE");

        private final String value;

        private Encoding(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }

        public static Encoding fromValue(String value) {
            Encoding encoding = null;
            if(value != null) {
                switch(value.toUpperCase()) {
                    case "ASCII":
                    case "US-ASCII":
                        encoding = Encoding.ASCII;
                        break;
                    case "UTF-8":
                        encoding = Encoding.UTF_8;
                        break;
                    case "UTF-16BE":
                        encoding = Encoding.UTF_16BE;
                        break;
                    case "UTF-16LE":
                        encoding = Encoding.UTF_16LE;
                        break;
                    default:
                        throw new RuntimeException(String.format("Invalid encoding %s", value));
                }
            } else {
                throw new RuntimeException(String.format("Invalid encoding %s", value));
            }
            return encoding;
        }
    }

    private enum ByteOrderMarking {
        UTF_8(new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF}),
        UTF_16LE(new byte[]{(byte)0xFF, (byte)0xFE}),
        UTF_16BE(new byte[]{(byte)0xFE, (byte)0xFF}),
        ASCII(new byte[]{});

        private final byte[] bom;

        private ByteOrderMarking(byte[] bom) {
            this.bom = bom;
        }

        public byte[] value() {
            return this.bom;
        }
    }
}
