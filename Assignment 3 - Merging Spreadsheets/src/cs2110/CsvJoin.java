package cs2110;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class CsvJoin {
    public static void main(String[] args) {

        // throw error message when reading a csv
        try {
            // When the Run Configuration does not have two arguments, throw RuntimeException.
            if (args.length !=2 )
                throw new RuntimeException("Two arguments are required. You are missing at least one argument.");
            Seq<Seq<String>> left = csvToList(args[0]);
            Seq<Seq<String>> right = csvToList(args[1]);

            // When either one of the two files does not contain rectangle list, throw RuntimeException.
            if (!isRectangle(left) || !isRectangle(right))
                throw new RuntimeException("Input tables are not rectangular.");

            // Create an empty linked list.
            Seq<Seq<String>> table;

            // Join two CSV files.
            table = join(left, right);

            // Print the output in simplified CSV format.
            printSimplifiedCSV(table);

        } catch (FileNotFoundException e) {
            System.err.println("IOException Error: File not found." + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("IOException Error: An IOException occurred when reading the file(s): " + e.getMessage());
            System.exit(1);
        } catch (RuntimeException e) {
            System.err.println("Runtime Error: " + e.getMessage());
            System.exit(1);
        } catch (AssertionError e) {
            System.err.println("Assertion Error: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Load a table from a Simplified CSV file and return a row-major list-of-lists representation.
     * The CSV file is assumed to be in the platform's default encoding. Throws an IOException if
     * there is a problem reading the file.
     */
    public static Seq<Seq<String>> csvToList(String file) throws IOException {
        // Create empty linked list for the table.
        Seq<Seq<String>> rows = new LinkedSeq<>();

        // Create FileReader to read the CSV file.
        FileReader fileReader = new FileReader(file);

        // Create Scanner to process the data.
        Scanner scanner = new Scanner(fileReader);

        while (scanner.hasNextLine()) {
            // Read each line
            String line = scanner.nextLine();

            // Use comma as the delimiter, split the line into strings and store them in the String[]
            String[] values = line.split(",", -1);

            // Create an empty linked for the row.
            Seq<String> row = new LinkedSeq<>();

            // Loop through the array of String
            for (String element : values) {
                // Append each String to the linked list row
                row.append(element);
            }
            // Append the row to the linked list table
            rows.append(row);
        }
        return rows;
    }

    /**
     * Return the left outer join of tables `left` and `right`, joined on their first column. Result
     * will represent a rectangular table, with empty strings filling in any columns from `right`
     * when there is no match. Requires that `left` and `right` represent rectangular tables with at
     * least 1 column.
     */
    public static Seq<Seq<String>> join(Seq<Seq<String>> left, Seq<Seq<String>> right) {
        // Assert left and right are rectangles
        assert isRectangle(left) : "Left table is not a rectangle during join.";
        assert left.get(0).size() >= 1 : "Left table has less than 1 column.";
        assert isRectangle(right) : "Right table is not a rectangle during join.";
        assert right.get(0).size() >= 1 : "Right table has less than 1 column.";

        Seq<Seq<String>> result = new LinkedSeq<Seq<String>>();

        for (Seq<String> leftRow : left) {
            String leftKey = leftRow.get(0).toString();
            Seq<Seq<String>> matchingRows = new LinkedSeq<Seq<String>>();
            Seq<Seq<String>> tempL = new LinkedSeq<Seq<String>>();
            Seq<Seq<String>> tempLI = new LinkedSeq<Seq<String>>();
            matchingRows = findMatchingRows(right, leftKey);

            if (matchingRows.size() > 1) {
                for (Seq<String> matchRow : matchingRows) {
                    Seq<String> newRow = new LinkedSeq<String>();
                    //first append left
                    for (String elem : leftRow)
                        newRow.append(elem);
                    //then append right
                    for (String elem : matchRow)
                        newRow.append(elem);
                    tempL.append(newRow);
                }
            }
            else {
                tempL.append(leftRow);
                for (int i = 0; i < matchingRows.size(); i++) {
                    tempLI.append(concatenate(tempL.get(i), matchingRows.get(i)));
                }
            }
            result = concatenateSeq(result, tempL);
        }
        return result;
    }

    /**
     * Given a linked list table, print the content in simplified CSV format.
     */
    private static void printSimplifiedCSV(Seq<Seq<String>> table) {
        assert isRectangle(table) : "Table is not a rectangles during printing.";
        
        for (Seq<String> row : table) {
            for (int i=0; i<row.size(); i++) {
                System.out.print(row.get(i));
                if (i!=row.size()-1)
                    System.out.print(",");
            }
            System.out.println();
        }
    }

    /**
     * Check if a linked list is a rectangle. A linked list is a rectangle when the linked list is an object
     * of the type of 'LinkedSeq' and when the size is not 0 and all the rows have the same size.
     * Return true if it is a rectangle; return false otherwise.
     */
    private static boolean isRectangle(Seq<Seq<String>> list) {
        // If not object of Seq, not a rectangle
        if (!(list instanceof LinkedSeq)) {
            return false;
        }
        // If empty list, not a rectangle
        if (list.size() == 0) {
            return false;
        }
        // Check if all the rows have the same size
        for (Seq<String> row : list) {
            if (row.size() != list.get(0).size())
                return false;
        }
        return true;
    }

    /**
     * Given a linked list table "right" and a string key "leftKey", find all the rows in which the first element
     * equals to the "leftKey".
     * If matching rows are found, return a list of all the matching rows.
     * Else if no matching row is found, return a row of empty strings with the size of the row equals to the
     * number of the columns in "right" minus 1.
     */
    private static Seq<Seq<String>> findMatchingRows(Seq<Seq<String>> right, String leftKey) {
        assert isRectangle(right) : "Second input file is not a rectangle.";
        assert leftKey != null : "Key is null value.";

        Seq<Seq<String>> ans = new LinkedSeq<>();

        for (Seq<String> rightRow : right) {
            String rightKey = rightRow.get(0);
            if (rightKey.equals(leftKey)) {
                Seq<String> row = new LinkedSeq<>();
                for (int i = 1; i < rightRow.size(); i++) {
                    row.append(rightRow.get(i));
                }
                ans.append(row);
            }
        }

        //if leftKey not found in right table, return empty row, which is the same size as a right row - 1
        if (ans.size() == 0) {
            Seq<String> row = new LinkedSeq<>();
            for (int i = 1; i < right.get(0).size(); i++) {
                row.append("");
            }
            ans.append(row);
        }
        return ans;
    }

    /**
     * Given two linked lists of String, "leftRow" and "rightRow".
     * Append all the String elements from the 'rightRow' to the "rightRow".
     * Return the "leftRow".
     */
    private static Seq<String> concatenate(Seq<String> leftRow, Seq<String> rightRow) {
        assert (leftRow instanceof LinkedSeq) : "Invalid linded link detected during concatenation.";
        assert (rightRow instanceof LinkedSeq) : "Invalid linded link detected during concatenation.";

        for (int i = 0; i < rightRow.size(); i++) {
            leftRow.append(rightRow.get(i));
        }
        return leftRow;
    }

    /**
     * Given two linked lists of lined list of Strings, "leftTable" and "rightTable".
     * Append all the elements from the 'rightTable' to the "rightTable".
     * Return the "leftTable".
     */
    private static Seq<Seq<String>> concatenateSeq(Seq<Seq<String>> leftTable, Seq<Seq<String>> rightTable) {
        assert isRectangle(leftTable) : "Table is not a rectangles during concatenation.";
        assert isRectangle(rightTable) : "Table is not a rectangles during concatenation.";

        for (int i = 0; i < rightTable.size(); i++) {
            leftTable.append(rightTable.get(i));
        }
        return leftTable;
    }


}
