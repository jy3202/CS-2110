package cs2110;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RpnParserTest {

    @Test
    @DisplayName("Parsing an expression consisting of a single number should yield a Constant " +
            "node with that value")
    void testParseConstant() throws IncompleteRpnException, UndefinedFunctionException {
        Expression expr = RpnParser.parse("1.5", Map.of());
        assertEquals(new Constant(1.5), expr);
    }

    @Test
    @DisplayName("Parsing an expression consisting of a single identifier should yield a " +
            "Variable node with that name")
    void testParseVariable() throws IncompleteRpnException, UndefinedFunctionException {
        Expression expr = RpnParser.parse("x", Map.of());
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
//        assertEquals(new Variable("x"), expr);
    }

    @Test
    @DisplayName("Parsing an expression ending with an operator should yield an Operation node " +
            "evaluating to the expected value")
    void testParseOperation()
            throws UnboundVariableException, IncompleteRpnException, UndefinedFunctionException {
        Expression expr = RpnParser.parse("1 1 +", Map.of());
        // TODO: Uncomment this test
//        assertInstanceOf(Operation.class, expr);
        assertEquals(2.0, expr.eval(MapVarTable.empty()));

        // TODO: This is not a very thorough test!  Both operands are the same, and the operator is
        // commutative.  Write additional test cases that don't have these properties.
        // You should also write a test case that requires recursive evaluation of the operands.

        // Test Case 1: two operands are different
        Expression expr1 = RpnParser.parse("1 10 +", Map.of());
        assertInstanceOf(Operation.class, expr1);
        assertEquals(11.0, expr1.eval(MapVarTable.empty()));

        // Test Case 2: division 1
        Expression expr2 = RpnParser.parse("8 4 /", Map.of());
        assertInstanceOf(Operation.class, expr2);
        assertEquals(2.0, expr2.eval(MapVarTable.empty()));

        // Test Case 3: division 2
        Expression expr3 = RpnParser.parse("4 8 /", Map.of());
        assertInstanceOf(Operation.class, expr3);
        assertEquals(0.5, expr3.eval(MapVarTable.empty()));

        // Test Case 4: minus
        Expression expr4 = RpnParser.parse("100 50 -", Map.of());
        assertInstanceOf(Operation.class, expr4);
        assertEquals(50.0, expr4.eval(MapVarTable.empty()));

        // Test Case 5: recursive
        Expression expr5 = RpnParser.parse("8 4 - -20 abs() * 8 /", Map.of());
        assertInstanceOf(Operation.class, expr5);
        assertEquals(10.0, expr5.eval(MapVarTable.empty()));
    }

    @Test
    @DisplayName("Parsing an expression ending with a function should yield an Application node " +
            "evaluating to the expected value")
    void testParseApplication()
            throws UnboundVariableException, IncompleteRpnException, UndefinedFunctionException {
        Expression expr = RpnParser.parse("4 sqrt()", UnaryFunction.mathDefs());
        // TODO: Uncomment this test
//        assertInstanceOf(Application.class, expr);
        assertEquals(2.0, expr.eval(MapVarTable.empty()));

    }

    @Test
    @DisplayName("Parsing an empty expression should throw an IncompleteRpnException")
    void testParseEmpty() {
        assertThrows(IncompleteRpnException.class, () -> RpnParser.parse("", Map.of()));
    }

    @Test
    @DisplayName("Parsing an expression that leave more than one term on the stack should throw " +
            "an IncompleteRpnException")
    void testParseIncomplete() {
        assertThrows(IncompleteRpnException.class, () -> RpnParser.parse("1 1 1 +", Map.of()));
    }

    @Test
    @DisplayName("Parsing an expression that consumes more terms than are on the stack should " +
            "throw an IncompleteRpnException")
    void testParseUnderflow() {
        assertThrows(IncompleteRpnException.class, () -> RpnParser.parse("1 1 + +", Map.of()));
    }

    @Test
    @DisplayName("Parsing an expression that applies an unknown function should throw an " +
            "UnknownFunctionException")
    void testParseUndefined() {
        assertThrows(UndefinedFunctionException.class, () -> RpnParser.parse("1 foo()", Map.of()));
    }
}
