package cs2110;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstantExpressionTest {

    @Test
    @DisplayName("A Constant node should evaluate to its value (regardless of var table)")
    void testEval() throws UnboundVariableException {
        Expression expr = new Constant(1.5);
        assertEquals(1.5, expr.eval(MapVarTable.empty()));
    }


    @Test
    @DisplayName("A Constant node should report that 0 operations are required to evaluate it")
    void testOpCount() {
        Expression expr = new Constant(1.5);
        assertEquals(0, expr.opCount());
    }


    @Test
    @DisplayName("A Constant node should produce an infix representation with just its value (as " +
            "formatted by String.valueOf(double))")
    void testInfix() {
        Expression expr = new Constant(1.5);
        assertEquals("1.5", expr.infixString());

        expr = new Constant(Math.PI);
        assertEquals("3.141592653589793", expr.infixString());
    }

    @Test
    @DisplayName("A Constant node should produce an postfix representation with just its value " +
            "(as formatted by String.valueOf(double))")
    void testPostfix() {
        Expression expr = new Constant(1.5);
        assertEquals("1.5", expr.postfixString());

        expr = new Constant(Math.PI);
        assertEquals("3.141592653589793", expr.postfixString());
    }


    @Test
    @DisplayName("A Constant node should equal itself")
    void testEqualsSelf() {
        Expression expr = new Constant(1.5);
        // Normally `assertEquals()` is preferred, but since we are specifically testing the
        // `equals()` method, we use the more awkward `assertTrue()` to make that call explicit.
        assertTrue(expr.equals(expr));
    }

    @Test
    @DisplayName("A Constant node should equal another Constant node with the same value")
    void testEqualsTrue() {
        Expression expr1 = new Constant(1.5);
        Expression expr2 = new Constant(1.5);
        assertTrue(expr1.equals(expr2));
    }

    @Test
    @DisplayName("A Constant node should not equal another Constant node with a different value")
    void testEqualsFalse() {
        Expression expr1 = new Constant(1.5);
        Expression expr2 = new Constant(2.0);
        assertFalse(expr1.equals(expr2));
    }


    @Test
    @DisplayName("A Constant node does not depend on any variables")
    void testDependencies() {
        Expression expr = new Constant(1.5);
        Set<String> deps = expr.dependencies();
        assertTrue(deps.isEmpty());
    }


    @Test
    @DisplayName("A Constant node should optimize to itself (regardless of var table)")
    void testOptimize() {
        Expression expr = new Constant(1.5);
        Expression opt = expr.optimize(MapVarTable.empty());
        assertEquals(expr, opt);
    }
}

class VariableExpressionTest {

    @Test
    @DisplayName("A Variable node should evaluate to its variable's value when that variable is " +
            "in the var map")
    void testEvalBound() throws UnboundVariableException {
//        fail();  // TODO
        MapVarTable varTbl = new MapVarTable();
        varTbl.set("x", 10.0);
        varTbl.set("y", 15.0);
        varTbl.set("X", 10.0);

        Expression expr = new Variable("x");
        assertEquals(10.0, expr.eval(varTbl));

        Expression expr1 = new Variable("y");// different value, different variable
        assertEquals(15.0, expr1.eval(varTbl));

        Expression expr2 = new Variable("X"); // same value, different variable
        assertEquals(10.0, expr2.eval(varTbl));
    }

    @Test
    @DisplayName("A Variable node should throw an UnboundVariableException when evaluated if its " +
            "variable is not in the var map")
    void testEvalUnbound() {
        // TODO: Uncomment these lines when you have read about testing exceptions in the handout.
        // They assume that your `Variable` constructor takes its name as an argument.
        Expression expr = new Variable("x");
        assertThrows(UnboundVariableException.class, () -> expr.eval(MapVarTable.empty()));
    }


    @Test
    @DisplayName("A Variable node should report that 0 operations are required to evaluate it")
    void testOpCount() {
//        fail();  // TODO
        MapVarTable varTbl = new MapVarTable();
        varTbl.set("x", 10.0);
        varTbl.set("y", 15.0);
        varTbl.set("X", 10.0);

        Expression expr = new Variable("x");
        assertEquals(0, expr.opCount());

        Expression expr1 = new Variable("y");// different value, different variable
        assertEquals(0, expr1.opCount());

        Expression expr2 = new Variable("X"); // same value, different variable
        assertEquals(0, expr2.opCount());
    }


    @Test
    @DisplayName("A Variable node should produce an infix representation with just its name")
    void testInfix() {
//        fail();  // TODO
        MapVarTable varTbl = new MapVarTable();
        varTbl.set("x", 10.0);
        varTbl.set("y", 15.0);
        varTbl.set("X", 10.0);

        Expression expr = new Variable("x");
        assertEquals("x", expr.infixString());

        Expression expr1 = new Variable("y");// different value, different variable
        assertEquals("y", expr1.infixString());

        Expression expr2 = new Variable("X"); // same value, different variable
        assertEquals("X", expr2.infixString());
    }

    @Test
    @DisplayName("A Variable node should produce an postfix representation with just its name")
    void testPostfix() {
//        fail();  // TODO
        MapVarTable varTbl = new MapVarTable();
        varTbl.set("x", 10.0);
        varTbl.set("y", 15.0);
        varTbl.set("X", 10.0);

        Expression expr = new Variable("x");
        assertEquals("x", expr.postfixString());

        Expression expr1 = new Variable("y");// different value, different variable
        assertEquals("y", expr1.postfixString());

        Expression expr2 = new Variable("X"); // same value, different variable
        assertEquals("X", expr2.postfixString());
    }


    @Test
    @DisplayName("A Variable node should equal itself")
    void testEqualsSelf() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Variable("x");
        assertTrue(expr.equals(expr));
    }

    @Test
    @DisplayName("A Variable node should equal another Variable node with the same name")
    void testEqualsTrue() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        // Force construction of new String objects to detect inadvertent use of `==`
        Expression expr1 = new Variable(new String("x"));
        Expression expr2 = new Variable(new String("x"));
        assertTrue(expr1.equals(expr2));
    }

    @Test
    @DisplayName("A Variable node should not equal another Variable node with a different name")
    void testEqualsFalse() {
//        fail();  // TODO
        Expression expr1 = new Variable(new String("x"));
        Expression expr2 = new Variable(new String("X"));
        assertFalse(expr1.equals(expr2));
    }


    @Test
    @DisplayName("A Variable node only depends on its name")
    void testDependencies() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Variable("x");
        Set<String> deps = expr.dependencies();
        assertTrue(deps.contains("x"));
        assertEquals(1, deps.size());

        Expression expr2 = new Variable("y");
        Set<String> deps2 = expr2.dependencies();
        assertTrue(deps2.contains("y"));
        assertEquals(1, deps2.size());
    }


    @Test
    @DisplayName("A Variable node should optimize to a Constant if its variable is in the var map")
    void testOptimizeBound() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Variable("x");
        Expression opt = expr.optimize(MapVarTable.of("x", 1.5));
        assertEquals(new Constant(1.5), opt);
    }

    @Test
    @DisplayName("A Variable node should optimize to itself if its variable is not in the var map")
    void testOptimizeUnbound() {
//        fail();  // TODO
        Expression expr = new Variable("x");
        Expression opt = expr.optimize(MapVarTable.of("y", 1.5));
        assertEquals(expr, opt);
    }
}

class OperationExpressionTest {

    @Test
    @DisplayName("An Operation node for ADD with two Constant operands should evaluate to their " +
            "sum")
    void testEvalAdd() throws UnboundVariableException {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Operation(Operator.ADD, new Constant(1.5), new Constant(2));
        assertEquals(3.5, expr.eval(MapVarTable.empty()));
    }

    @Test
    @DisplayName("An Operation node for ADD with a Variable for an operand should evaluate " +
            "to its operands' sum when the variable is in the var map")
    void testEvalAddBound() throws UnboundVariableException {
        MapVarTable varTbl = new MapVarTable();
        varTbl.set("x", 10.0);
        varTbl.set("y", 15.0);

        Expression expr = new Operation(Operator.ADD, new Variable("x"), new Variable("y"));
        assertEquals(25.0, expr.eval(varTbl));
    }

    @Test
    @DisplayName("An Operation node for ADD with a Variable for an operand should throw an " +
            "UnboundVariableException when evaluated if the variable is not in the var map")
    void testEvalAddUnbound() {
        Expression expr = new Operation(Operator.ADD, new Variable("x"), new Variable("y"));
        assertThrows(UnboundVariableException.class, () -> expr.eval(MapVarTable.empty()));
    }


    @Test
    @DisplayName("An Operation node with leaf operands should report that 1 operation is " +
            "required to evaluate it")
    void testOpCountLeaves() {
//        fail();  // TODO
        Expression expr = new Operation(Operator.ADD, new Constant(1.5), new Constant(1.5));
        assertEquals(1, expr.opCount());

        Expression expr1 = new Operation(Operator.SUBTRACT, new Constant(1.5), new Constant(0));
        assertEquals(1, expr1.opCount());

        Expression expr2 = new Operation(Operator.MULTIPLY, new Constant(1.5), new Constant(0));
        assertEquals(1, expr2.opCount());

        Expression expr3 = new Operation(Operator.DIVIDE, new Constant(1.5), new Constant(1.5));
        assertEquals(1, expr3.opCount());

        Expression expr4 = new Operation(Operator.POW, new Constant(1.5), new Constant(0));
        assertEquals(1, expr4.opCount());
    }


    @Test
    @DisplayName("An Operation node with an Operation for either or both operands should report " +
            "the correct number of operations to evaluate it")
    void testOpCountRecursive() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Operation(Operator.ADD,
                new Operation(Operator.MULTIPLY, new Constant(1.5), new Variable("x")),
                new Constant(2.0));
        assertEquals(2, expr.opCount());

        expr = new Operation(Operator.SUBTRACT,
                new Operation(Operator.MULTIPLY, new Constant(1.5), new Variable("x")),
                new Operation(Operator.DIVIDE, new Constant(1.5), new Variable("x")));
        assertEquals(3, expr.opCount());
    }


    @Test
    @DisplayName("An Operation node with leaf operands should produce an infix representation " +
            "consisting of its first operand, its operator symbol surrounded by spaces, and " +
            "its second operand, all enclosed in parentheses")
    void testInfixLeaves() {
        Expression expr = new Operation(Operator.ADD, new Constant(1.5), new Constant(2.0));
        assertEquals("(1.5 + 2.0)", expr.infixString());

        Expression express = new Operation(Operator.ADD, new Variable("x"), new Constant(1.0));
        assertEquals("(x + 1.0)", express.infixString());

        Expression expr1 = new Operation(Operator.SUBTRACT, new Constant(1.5), new Constant(2.0));
        assertEquals("(1.5 - 2.0)", expr1.infixString());

        Expression express1 = new Operation(Operator.SUBTRACT, new Constant(1), new Variable("x"));
        assertEquals("(1.0 - x)", express1.infixString());

        Expression expr2 = new Operation(Operator.MULTIPLY, new Constant(1.5), new Constant(2.0));
        assertEquals("(1.5 * 2.0)", expr2.infixString());

        Expression express2 = new Operation(Operator.MULTIPLY, new Constant(1), new Variable("x"));
        assertEquals("(1.0 * x)", express2.infixString());

        Expression expr3 = new Operation(Operator.DIVIDE, new Constant(1.5), new Constant(2.0));
        assertEquals("(1.5 / 2.0)", expr3.infixString());

        Expression express3 = new Operation(Operator.DIVIDE, new Constant(1), new Variable("x"));
        assertEquals("(1.0 / x)", express3.infixString());

        Expression expr4 = new Operation(Operator.POW, new Constant(1.5), new Constant(2.0));
        assertEquals("(1.5 ^ 2.0)", expr4.infixString());

        Expression express4 = new Operation(Operator.POW, new Constant(1), new Variable("x"));
        assertEquals("(1.0 ^ x)", express4.infixString());
    }

    @Test
    @DisplayName("An Operation node with an Operation for either operand should produce the " +
            "expected infix representation with parentheses around each operation")
    void testInfixRecursive() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Operation(Operator.ADD,
                new Operation(Operator.MULTIPLY, new Constant(1.5), new Variable("x")),
                new Constant(2.0));
        assertEquals("((1.5 * x) + 2.0)", expr.infixString());

        expr = new Operation(Operator.SUBTRACT,
                new Constant(2.0),
                new Operation(Operator.DIVIDE, new Constant(1.5), new Variable("x")));
        assertEquals("(2.0 - (1.5 / x))", expr.infixString());
    }


    @Test
    @DisplayName("An Operation node with leaf operands should produce a postfix representation " +
            "consisting of its first operand, its second operand, and its operator symbol " +
            "separated by spaces")
    void testPostfixLeaves() {
        Expression expr = new Operation(Operator.ADD, new Constant(1.5), new Constant(2.0));
        assertEquals("1.5 2.0 +", expr.postfixString());

        Expression express = new Operation(Operator.ADD, new Variable("x"), new Constant(1.0));
        assertEquals("x 1.0 +", express.postfixString());
    }

    @Test
    @DisplayName("An Operation node with an Operation for either operand should produce the " +
            "expected postfix representation")
    void testPostfixRecursive() {
        // TODO (at least two cases; you may crib from testInfixRecursive above)
        Expression expr = new Operation(Operator.ADD,
                new Operation(Operator.MULTIPLY, new Constant(1.5), new Variable("x")),
                new Constant(2.0));
        assertEquals("1.5 x * 2.0 +", expr.postfixString());

        expr = new Operation(Operator.SUBTRACT,
                new Constant(2.0),
                new Operation(Operator.DIVIDE, new Constant(1.5), new Variable("x")));
        assertEquals("2.0 1.5 x / -", expr.postfixString());
    }


    @Test
    @DisplayName("An Operation node should equal itself")
    void testEqualsSelf() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Operation(Operator.ADD, new Constant(1.5), new Variable("x"));
        assertTrue(expr.equals(expr));
    }

    @Test
    @DisplayName("An Operation node should equal another Operation node with the same " +
            "operator and operands")
    void testEqualsTrue() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr1 = new Operation(Operator.ADD, new Constant(1.5), new Variable("x"));
        Expression expr2 = new Operation(Operator.ADD, new Constant(1.5), new Variable("x"));
        assertTrue(expr1.equals(expr2));
    }

    @Test
    @DisplayName("An Operation node should not equal another Operation node with a different " +
            "operator")
    void testEqualsFalse() {
        Expression expr1 = new Operation(Operator.ADD, new Constant(1.5), new Variable("x"));
        Expression expr2 = new Operation(Operator.SUBTRACT, new Constant(1.5), new Variable("x"));
        assertFalse(expr1.equals(expr2));
    }


    @Test
    @DisplayName("An Operation node depends on the dependencies of both of its operands")
    void testDependencies() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Operation(Operator.ADD, new Variable("x"), new Variable("y"));
        Set<String> deps = expr.dependencies();
        assertTrue(deps.contains("x"));
        assertTrue(deps.contains("y"));
        assertEquals(2, deps.size());

        Expression expr1 = new Operation(Operator.ADD, new Variable("x"), new Constant(2));
        Set<String> deps1 = expr1.dependencies();
        assertTrue(deps1.contains("x"));
        assertEquals(1, deps1.size());

        Expression expr2 = new Operation(Operator.ADD, new Constant(1), new Variable("x"));
        Set<String> deps2 = expr2.dependencies();
        assertTrue(deps2.contains("x"));
        assertEquals(1, deps2.size());

        Expression expr3 = new Operation(Operator.ADD, new Constant(1), new Constant(2));
        Set<String> deps3 = expr3.dependencies();
        assertEquals(0, deps3.size());
    }


    @Test
    @DisplayName("An Operation node for ADD with two Constant operands should optimize to a " +
            "Constant containing their sum")
    void testOptimizeAdd() throws UnboundVariableException {
//        fail();  // TODO
        MapVarTable varTbl = new MapVarTable();
        varTbl.set("x", 10.0);
        varTbl.set("y", 15.0);

        Expression expr = new Operation(Operator.ADD, new Constant(1), new Constant(2));
        Expression opt = expr.optimize(varTbl);
        assertInstanceOf(Constant.class, opt);
        double ans = opt.eval(varTbl);
        assertEquals(3.0, ans);
    }

    @Test
    @DisplayName("An Operation node for ADD with one Constant operand and one Variable operand should optimize to a " +
            "Constant containing their sum")
    void testOptimizeAddTakeOne() throws UnboundVariableException {
//        fail();  // TODO
        MapVarTable varTbl = new MapVarTable();
        varTbl.set("x", 10.0);
        varTbl.set("y", 15.0);

        Expression expr = new Operation(Operator.ADD, new Constant(1), new Variable("x"));
        Expression opt = expr.optimize(varTbl);
        assertInstanceOf(Constant.class, opt);
        double ans = opt.eval(varTbl);
        assertEquals(11.0, ans);
    }

    @Test
    @DisplayName("An Operation node for ADD with one Constant operand and one Variable operand should optimize to a " +
            "Operation containing their formula")
    void testOptimizeAddTakeTwo() throws UnboundVariableException {
//        fail();  // TODO
        MapVarTable varTbl = new MapVarTable();
        varTbl.set("x", 10.0);
        varTbl.set("y", 15.0);

        Expression expr = new Operation(Operator.ADD, new Constant(1), new Variable("z"));
        Expression opt = expr.optimize(varTbl);
        assertInstanceOf(Operation.class, opt);
        Expression optOne = new Operation(Operator.ADD, new Constant(1), new Variable("z"));
        assertTrue(optOne.equals(opt));
    }
}

class ApplicationExpressionTest {

    @Test
    @DisplayName("An Application node for SQRT with a Constant argument should evaluate to its " +
            "square root")
    void testEvalSqrt() throws UnboundVariableException {
        Expression expr = new Application(UnaryFunction.SQRT, new Constant(16));
        assertEquals(4.0, expr.eval(MapVarTable.empty()));
    }

    @Test
    @DisplayName("An Application node with a Variable for its argument should throw an " +
            "UnboundVariableException when evaluated if the variable is not in the var map")
    void testEvalAbsUnbound() {
        Expression expr = new Application(UnaryFunction.SQRT, new Variable("x"));
        assertThrows(UnboundVariableException.class, ()-> expr.eval(MapVarTable.empty()));
    }


    @Test
    @DisplayName("An Application node with a leaf argument should report that 1 operation is " +
            "required to evaluate it")
    void testOpCountLeaf() {
//        fail();  // TODO
        MapVarTable varTbl = new MapVarTable();
        varTbl.set("x", 100.0);
        varTbl.set("y", 15.0);

        Expression expr = new Application(UnaryFunction.SQRT, new Constant(4.0));
        assertEquals(1, expr.opCount());

        Expression expr1 = new Application(UnaryFunction.SQRT, new Variable("x"));
        assertEquals(1, expr1.opCount());
    }


    @Test
    @DisplayName("An Application node with non-leaf expressions for its argument should report " +
            "the correct number of operations to evaluate it")
    void testOpCountRecursive() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Application(UnaryFunction.SQRT,
                new Operation(Operator.MULTIPLY, new Constant(1.5), new Variable("x")));
        assertEquals(2, expr.opCount());
    }


    @Test
    @DisplayName(
            "An Application node with a leaf argument should produce an infix representation " +
                    "consisting of its name, followed by the argument enclosed in parentheses.")
    void testInfixLeaves() {
        Expression expr = new Application(UnaryFunction.ABS, new Variable("x"));
        assertEquals("abs(x)", expr.infixString());
    }

    @Test
    @DisplayName("An Application node with an Operation for its argument should produce the " +
            "expected infix representation with redundant parentheses around the argument")
    void testInfixRecursive() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Application(UnaryFunction.ABS,
                new Operation(Operator.MULTIPLY, new Constant(1.5), new Variable("x")));
        assertEquals("abs((1.5 * x))", expr.infixString());
    }


    @Test
    @DisplayName("An Application node with a leaf argument should produce a postfix " +
            "representation consisting of its argument, followed by a space, followed by its " +
            "function's name appended with parentheses")
    void testPostfixLeaves() {
        Expression expr = new Application(UnaryFunction.ABS, new Variable("x"));
        assertEquals("x abs()", expr.postfixString());
    }

    @Test
    @DisplayName("An Application node with an Operation for its argument should produce the " +
            "expected postfix representation")
    void testPostfixRecursive() {
        Expression expr = new Application(UnaryFunction.ABS,
                new Operation(Operator.MULTIPLY, new Constant(1.5), new Variable("x")));
        assertEquals("1.5 x * abs()", expr.postfixString());
    }

    @Test
    @DisplayName("An Application node should equal itself")
    void testEqualsSelf() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Application(UnaryFunction.SQRT, new Constant(4.0));
        assertTrue(expr.equals(expr));
    }

    @Test
    @DisplayName("An Application node should equal another Application node with the same " +
            "function and argument")
    void testEqualsTrue() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr1 = new Application(UnaryFunction.SQRT, new Constant(4.0));
        Expression expr2 = new Application(UnaryFunction.SQRT, new Constant(4.0));
    }

    @Test
    @DisplayName("An Application node should not equal another Application node with a different " +
            "argument")
    void testEqualsFalseArg() {
        Expression expr1 = new Application(UnaryFunction.ABS, new Constant(1.0));
        Expression expr2 = new Application(UnaryFunction.ABS, new Constant(4.0));
        assertFalse(expr1.equals(expr2));
    }

    @Test
    @DisplayName("An Application node should not equal another Application node with a different " +
            "function")
    void testEqualsFalseFunc() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr1 = new Application(UnaryFunction.SQRT, new Constant(4.0));
        Expression expr2 = new Application(UnaryFunction.ABS, new Constant(4.0));
        assertFalse(expr1.equals(expr2));
    }


    @Test
    @DisplayName("An Application node for SQRT with a Constant/Variable argument should optimize to a " +
            "Constant containing its square root.")
    void testOptimizeConstantOrVariable() throws UnboundVariableException {
//        fail();  // TODO
        MapVarTable varTbl = new MapVarTable();
        varTbl.set("x", 100.0);
        varTbl.set("y", 15.0);

        Expression expr1 = new Application(UnaryFunction.SQRT, new Constant(4.0));
        Expression opt = expr1.optimize(varTbl);
        assertInstanceOf(Constant.class, opt);
        double ans = opt.eval(varTbl);
        assertEquals(2.0, ans);

        Expression expr2 = new Application(UnaryFunction.SQRT, new Variable("x"));
        Expression opt1 = expr2.optimize(varTbl);
        assertInstanceOf(Constant.class, opt1);
        double ans2 = opt1.eval(varTbl);
        assertEquals(10.0, ans2);
    }


    @Test
    @DisplayName("An Application node has the same dependencies as its argument")
    void testDependencies() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression arg = new Variable("x");
        Expression expr = new Application(UnaryFunction.SQRT, arg);
        Set<String> argDeps = arg.dependencies();
        Set<String> exprDeps = expr.dependencies();
        assertEquals(argDeps, exprDeps);
    }


    @Test
    @DisplayName("An Application node with an argument depending on a variable should optimize " +
            "to an Application node if the variable is unbound")
    void testOptimizeUnbound() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Application(UnaryFunction.SQRT, new Variable("x"));
        Expression opt = expr.optimize(MapVarTable.empty());
        assertInstanceOf(Application.class, opt);
    }

    @Test
    @DisplayName("An Application node with an argument depending on an variable should optimize " +
            "to an Application node if the variable is unbound with its arguments optimized fully")
    void testOptimizeUnboundArgument() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Application(UnaryFunction.SQRT, new Operation(Operator.MULTIPLY,
                new Operation(Operator.ADD, new Constant(1), new Constant(1)), new Variable("x")));
        Expression opt = expr.optimize(MapVarTable.empty());
        assertInstanceOf(Application.class, opt);
        Expression expr1 = new Application(UnaryFunction.SQRT, new Operation(Operator.MULTIPLY, new Constant(2), new Variable("x")));
        assertTrue(expr1.equals(opt));
    }
    // Known missing coverage:
    // * When optimizing an Application that depends on an unbound variable, is the result's
    //   argument node optimized?
}
