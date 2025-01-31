package cs2110;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;

public class CsvEvaluator {

    /**
     * Copy the spreadsheet data (represented as CSV records) from `parser` to `printer`, replacing
     * any formula cells with their evaluated value.
     * <p>
     * A formula cell is any cell whose first character is '='.  The remainder of the cell is
     * interpreted as a formula in Reverse Polish Notation with each token separated by whitespace.
     * The formula may refer to values of other cells by the cell's coordinates (column letter + row
     * number; e.g., "B4").  Only cells above or to the left (on the same line) of the formula cell
     * can be evaluated.  If the formula cannot be evaluated for any reason, its contents are
     * replaced by "#N/A" in the output.  Otherwise, its contents are replaced by Java's String
     * representation of the formula's value.
     * <p>
     * For a cell to be used in a later formula, its contents must represent a floating-point number
     * (as understood by Java's `Double.parseDouble()`), or else it must be a formula itself.
     */
    public static void evaluateCsv(CSVParser parser, CSVPrinter printer) throws IOException {
        // Support the most common math functions when parsing expressions.
        Map<String, UnaryFunction> defs = UnaryFunction.mathDefs();

        // A mapping of the coordinates of cells we have seen so far to their numerical values (if
        // they are a number or a successfully evaluated formula).
        VarTable vars = new MapVarTable();

        // TODO: Implement this method according to its specification.
//        throw new UnsupportedOperationException();

        // Note that `CSVParser` implements `Iterable<CSVRecord>` and that `CSVRecord` implements
        // `Iterable<String>`.  This may suggest a solution using "enhanced for-loops" (though this
        // is not strictly required).
        int rowCounter = 0;
        for(CSVRecord cell : parser) // goes through each cell in the csv row
        {
            rowCounter += 1; // increase row counter by 1
            int columnCounter = 0;
            for(String value : cell) // goes through each string in the current cell of the csv file
            {
                columnCounter += 1; // increase column counter by 1
                char sign = '=';
                if(value.length()>0 && sign == value.charAt(0)) // if a formula exists in this cell
                {
                    String currentLocation = colToLetters(columnCounter) + rowCounter; // current location of cell
                    try
                    {
                        Expression ans = RpnParser.parse(value.substring(1), defs); // creates an expression from the formula given
                        double solvedAns = ans.eval(vars); // evaluates the formula within the cell
                        vars.set(currentLocation, solvedAns); // sets new output into varsTable for current location for future reference
                        printer.print(vars.get(currentLocation)); // prints out evaluated formula answer with the variable name

                    }
                    catch(IncompleteRpnException | UndefinedFunctionException e) // occurs from RpnParser.parse()
                    {
                        printer.print("#N/A");
                    }
                    catch (UnboundVariableException e) // occurs from eval()
                    {
                        printer.print("#N/A");
                    }
                }
                else// if a formula doesn't exist in this cell
                {
                    String currentLocation = colToLetters(columnCounter) + rowCounter; // current location of cell
                    try
                    {
                        Expression ans = RpnParser.parse(value, defs); // creates an expression from the formula given
                        double solvedAns = ans.eval(vars); // evaluates the formula within the cell
                        vars.set(currentLocation, solvedAns); // sets new output into varsTable for current location for future reference
                        printer.print(value); // prints out evaluated formula answer with the variable name
                    }
                    catch(IncompleteRpnException | UndefinedFunctionException e) // occurs from RpnParser.parse()
                    {
                        printer.print(value); // since cell is not solvable(not a formula), print out entire cell
                    }
                    catch (UnboundVariableException e) // occurs from eval()
                    {
                        printer.print(value); // since cell is not solvable(not a formula), print out entire cell
                    }
                }
            }
            printer.println(); // out prints the '\n' in output, which indicates the next row
        }
    }

    /**
     * Return the base-26 bijective numeration of `n` using the digits 'A'-'Z'.  Requires `n` is
     * non-negative.  0 is represented as the empty string.
     */
    public static String colToLetters(int n) {
        // Implementation constraint: this method must be implemented recursively.

        // Bijective hexavigesimal (base-26) numeration using the digits 'A'-'Z'.
        // Let x be the largest multiple of the base strictly less than n.
        // The rightmost digit of the representation is the difference between x and n (the
        // "remainder" - may be equal to the base).
        // The left digits are the representation of x divided by the base (the "quotient").

        // TODO: Implement this method according to its specification.
//        throw new UnsupportedOperationException();
        if(n==0)
        {
            return "";
        }
        else
        {
            int quotient = n/26;
            int remainder = n%26;
            if(remainder >= 1 && remainder <= 25)// remainder != 0
            {
                return colToLetters(quotient) + (char)('A' + remainder - 1); // + colToLetters(quotient);
            }
            else // remainder = 0
            {
                return colToLetters(quotient-1) + (char)('Z' + remainder); // + colToLetters(quotient-1);
            }

        }

    }

    /**
     * A custom CSV format that resembles our "Simplified CSV" format from A3.  Values are never
     * quoted (because Commons CSV quotes too many things, like empty strings and '#' characters,
     * even in MINIMAL mode, which could confuse testers); instead, delimiters and record separators
     * are escaped with a backslash if necessary.
     */
    static final CSVFormat SIMPLIFIED_CSV = CSVFormat.RFC4180.builder()
            .setEscape('\\')
            .setQuoteMode(QuoteMode.NONE)
            .setRecordSeparator('\n')
            .build();

    /**
     * Parse a CSV file whose name is provided as the sole program argument, then print its
     * contents, evaluating any cells containing formulas, to the standard output stream (also in
     * CSV format).
     */
    public static void main(String[] args) throws IOException {
        // Ensure that the user provided the expected number of program arguments, then extract
        // those arguments.
        if (args.length != 1) {
            System.err.println("Usage: java CsvEvaluator <infile>");
            System.exit(1);
        }
        String filename = args[0];

        // Open the specified CSV file, then copy its contents, with formulas evaluated, to
        // `System.out`.
        try (FileReader reader = new FileReader(filename);
                CSVParser parser = SIMPLIFIED_CSV.parse(reader)) {
            // We don't open the Printer as a "resource" because we don't want to automatically
            // close `System.out`.  Instead, we flush it manually when we are done writing.
            CSVPrinter printer = SIMPLIFIED_CSV.printer();
            evaluateCsv(parser, printer);
            printer.flush();
        }
    }
}
