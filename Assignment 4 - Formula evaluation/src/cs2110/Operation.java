package cs2110;

import java.util.HashSet;
import java.util.Set;

/**
 * An expression tree node representing a mathematical operation.
 */
public class Operation implements Expression {

    private Operator operator;
    private Expression leftOperand;
    private Expression rightOperand;

    public Operation(Operator operator, Expression leftNode, Expression rightNode) {
        this.operator = operator;
        this.leftOperand = leftNode;
        this.rightOperand = rightNode;
    }

    @Override
    public double eval(VarTable vars) throws UnboundVariableException {
        double leftValue = leftOperand.eval(vars);
        double rightValue = rightOperand.eval(vars);

        if (Operator.isOperator(operator.symbol())) {
            return operator.operate(leftValue, rightValue);
        } else {
            throw new IllegalArgumentException("Invalid operator: " + operator);
        }

    }

    @Override
    public int opCount() {
        return leftOperand.opCount() + rightOperand.opCount() + 1;
    }

    @Override
    public String infixString() {
        return "(" + leftOperand.infixString() + " " + operator.symbol() + " " +
                rightOperand.infixString() + ")";
    }

    @Override
    public String postfixString() {
        return leftOperand.postfixString() + " " + rightOperand.postfixString() + " " +
                operator.symbol();
    }

    @Override
    public Expression optimize(VarTable vars) {
//        throw new UnsupportedOperationException();
        Operation x = new Operation(this.operator, leftOperand.optimize(vars), rightOperand.optimize(vars));
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
        Set<String> ans = new HashSet<>();
        try
        {
            ans.addAll(leftOperand.dependencies());
            ans.addAll(rightOperand.dependencies());
            System.out.println(ans);
            return ans;
        }
        catch(UnsupportedOperationException e)
        {
            return ans;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        Operation o = (Operation) other;
        return o.operator.equals(this.operator) && o.leftOperand.equals(this.leftOperand) &&
                o.rightOperand.equals(this.rightOperand);
    }
}
