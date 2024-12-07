package cs2110;

import java.util.Set;

/**
 * An expression tree node representing a mathematical application.
 */
public class Application implements Expression {

    /**
     * The mathematical application to an argument
     */
    private UnaryFunction func;

    /**
     * Numeric argument used within the function
     */
    private Expression argument;

    private static int count = 0;

    /**
     * Create a node representing the unary function.
     */
    public Application(UnaryFunction func, Expression argument)
    {
        //maybe need to check if argument is nonempty :)
        this.func = func;
        this.argument = argument;
    }


    /**
     * Return this node's value. (Which is the value of the unary function and its argument.)
     */
    @Override
    public double eval(VarTable vars) throws UnboundVariableException {
        double arg = argument.eval(vars);
        return func.apply(arg);
    }

    @Override
    public int opCount() {
        return argument.opCount() + 1;
    }

    @Override
    public String infixString() {
        return func.name() + "(" + argument.infixString() + ")";
    }

    @Override
    public String postfixString() {
        return argument.postfixString() + " " + func.name() + "()";
    }

    @Override
    public Expression optimize(VarTable vars) {
//        throw new UnsupportedOperationException();
        Application x = new Application(this.func, argument.optimize(vars));

        try{
            Constant y = new Constant(x.eval(vars));
            return y;
        }
        catch(UnboundVariableException e)
        {
            return x;
        }
    }

    @Override
    public Set<String> dependencies() {
//        throw new UnsupportedOperationException();
        Set<String> ans = argument.dependencies();
        return ans;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        Application a = (Application) other;
        return a.func.equals(this.func) && a.argument.equals(this.argument);
    }
}
