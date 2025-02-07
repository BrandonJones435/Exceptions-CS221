import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * FormatChecker
 * 
 * This program reads one or more files given as command-line arguments
 * and checks if the contents of each file are in the expected format.
 * 
 * Expected file format:
 *  - The first line contains two positive integers: number of rows and columns.
 *  - Each subsequent line contains exactly one double per column for each row.
 *  
 * If the file format is correct, it prints "VALID". Otherwise, it prints an error message
 * followed by "INVALID".
 * 
 * Usage: java FormatChecker file1 [file2 ... fileN]
 * 
 * @author Brandon Jones 
 */
public class FormatChecker {

    /**
     * Main method that processes command-line arguments and validates each file.
     *
     * @param args the list of file names to check
     */
    public static void main(String[] args) {
        // Check that at least one argument is provided
        if (args.length == 0) {
            System.out.println("Usage: $ java FormatChecker file1 [file2 ... fileN]");
            return;
        } 

        // Process each file provided in the command line
        for (int i = 0; i < args.length; i++) {
            // Puts each of the file names into the array args 
            String fileName = args[i];
            // Print the file name
            System.out.println(fileName);
            
            // Validate the file and output the result
            if (validateFile(fileName)) {
                System.out.println("VALID");
            }
            
            // Print an empty line between file outputs
            System.out.println();
        }
    }
    
    /**
     * Validates the format of the given file.
     *
     * @param filename the name of the file to validate
     * @return true if the file is in the valid format; false otherwise
     */
    private static boolean validateFile(String filename) {
        // Use a try-catch block to handle any exceptions that occur during file processing
        try (Scanner scanner = new Scanner(new File(filename))) {
            // 1. Read the first two tokens as integers: row count and column count
            if (!scanner.hasNext()) {
                throw new Exception("Missing or invalid row count");
            }
            int numRows = scanner.nextInt();
            
            if (!scanner.hasNextInt()) {
                throw new Exception("Missing or invalid column count");
            }
            int numCols = scanner.nextInt();

            // Check to see if there is another element on the same line 
            String remainingLine = scanner.nextLine();

            if (!remainingLine.isEmpty()) {
                throw new Exception("File should not have a third parameter");
            }
            // Check that the numbers are positive
            if (numRows <= 0 || numCols <= 0) {
                throw new Exception("Row and column counts must be positive");
            }
            
            // Process the grid rows
            int rowsRead = 0;
            
            // Read exactly numRows lines (ignoring completely empty lines, if any)
            while (scanner.hasNextLine() && rowsRead < numRows) {
                String line = scanner.nextLine().trim(); // Reads the line and takes out whitespace, tabs, or newlines from the line read. 

                // Split the line into tokens based on whitespace
                String[] tokens = line.split(" ");
                if (tokens.length != numCols) {
                    // If the number of tokens does not match the expected number of columns,
                    // throw an exception with a custom message.
                    throw new Exception("Row " + (rowsRead + 1) + " does not have " + numCols + " columns");
                }
                
                // Attempt to parse each token as a double
                for (String token : tokens) {
                    try {
                        Double.parseDouble(token);
                    } catch (NumberFormatException e) {
                        // Rethrow the exception so that it can be caught in the outer catch block.
                        throw e;
                    }
                }
                rowsRead++;
            }
            
            // Check that we have read exactly numRows rows
            if (rowsRead != numRows) {
                throw new Exception("Expected " + numRows + " rows but found " + rowsRead);
            }
            
            // Check for any extra data beyond the expected grid.
            // If there is any additional token after the expected rows, that is an error.
            if (scanner.hasNext()) {
                throw new Exception("Extra data after expected grid");
            }
            
            // If everything is read correctly, return true.
            return true;
            
        } catch (FileNotFoundException e) {
            // File not found error
            System.out.println(e.toString());
        } catch (InputMismatchException e) {
            // Data type did not match what we expected
            System.out.println(e.toString());
        } catch (NumberFormatException e) {
            // When parsing a double fails
            System.out.println(e.toString());
        } catch (NoSuchElementException e) {
            // Not enough tokens were found
            System.out.println(e.toString());
        } catch (Exception e) {
            // Any other exceptions or custom condition checks
            System.out.println(e.toString());
        }
        
        // If an exception was caught, the file is invalid.
        System.out.println("INVALID");
        return false;
    }
}

// Command to run all of the tests once FormatChecker.java is compiled 
// java FormatChecker invalid1.dat invalid2.dat invalid3.dat invalid4.dat invalid5.dat invalid6.dat invalid7.dat valid1.dat valid2.dat valid3.dat
