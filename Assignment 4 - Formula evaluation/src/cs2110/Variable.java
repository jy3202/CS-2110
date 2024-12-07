package cs2110;

import java.util.HashSet;
import java.util.Set;

/**
 * An expression tree node representing a variable within the calculation.
 */
public class Variable implements Expression{

    /**
     * The value of this variable in this expression.
     */
    final String name;
    /**
     * Create a node representing the value `value`.
     */
    public Variable(String name)
    {
        assert name!=null;
        this.name = name;
    }

    /**
     * Return this node's value. (Which is the value of the variable with the name 'name'.)
     */
    @Override
    public double eval(VarTable vars) throws UnboundVariableException {
//        throw new UnsupportedOperationException();
        return vars.get(name);
    }

    /**
     * No operations are required to evaluate a Variable's value.
     */
    @Override
    public int opCount() {
//        throw new UnsupportedOperationException();
        return 0;
    }

    @Override
    public String infixString() {
        return name;
    }

    @Override
    public String postfixString() {
        return name;
    }

    @Override
    public Expression optimize(VarTable vars) {
        try
        {
            Constant x = new Constant(vars.get(name));
            return x.optimize(vars);
        }
        catch (UnboundVariableException e)
        {

            return this;
        }
    }


    @Override
    public Set<String> dependencies() {
//        throw new UnsupportedOperationException();
        // Depends on itself
        Set<String> s = new HashSet<String>();
        s.add(this.name);
        return s;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        Variable v = (Variable) other;
        return v.name.equals(this.name);
    }
}
