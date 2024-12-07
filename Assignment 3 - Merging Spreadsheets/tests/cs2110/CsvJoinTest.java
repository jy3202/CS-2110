package cs2110;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CsvJoinTest {

    @Test
    void testCsvToList() throws IOException {
        Seq<Seq<String>> table = CsvJoin.csvToList("input-tests/example/input2.csv");
        String expectedString = "[[netid, grade], [def456, junior], [ghi789, first-year], [abc123, senior]]";
        assertEquals(expectedString, table.toString());

        table = CsvJoin.csvToList("tests/testCsvToList/non-rectangular.csv");
        expectedString = "[[1], [1, 2], [1, 2, 3], [1, , , 4], [1, , 3], [1, , ], [1]]";
        assertEquals(expectedString, table.toString());

        table = CsvJoin.csvToList("tests/testCsvToList/empty.csv");
        expectedString = "[]";
        assertEquals(expectedString, table.toString());

        table = CsvJoin.csvToList("tests/testCsvToList/empty-col.csv");
        expectedString = "[[], [], []]";
        assertEquals(expectedString, table.toString());
        // Distinguish between empty array and empty string
        assertEquals(1, table.get(0).size());
    }

    /**
     * Assert that joining "input-tests/dir/input1.csv" and "input-tests/dir/input2.csv" yields the
     * table in "input-tests/dir/output.csv".  Requires that tables in "input1.csv" and "input2.csv"
     * be rectangular with at least one column.
     */
    static void testJoinHelper(String dir) throws IOException {
        Seq<Seq<String>> left = CsvJoin.csvToList("input-tests/" + dir + "/input1.csv");
        Seq<Seq<String>> right = CsvJoin.csvToList("input-tests/" + dir + "/input2.csv");
        Seq<Seq<String>> expected = CsvJoin.csvToList("input-tests/" + dir + "/output.csv");
        Seq<Seq<String>> join = CsvJoin.join(left, right);
        assertEquals(expected, join);
    }

    @Test
    void testJoin() throws IOException {
        testJoinHelper("example");

        testJoinHelper("states");

        // Two of our own input-tests
        testJoinHelper("Apartment");

        testJoinHelper("Boba");
    }


}
