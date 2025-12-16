package com.davidhagar.gridphysics.functions.exp.op.unary;

import com.davidhagar.gridphysics.State;
import com.davidhagar.gridphysics.functions.exp.op.Expression;

public abstract class Square extends UnaryOpp {


    Expression e1;


    public Square() {
    }

    public Square(Expression e1, Expression e2) {
        this.e1 = e1;
    }

    public Expression getE1() {
        return e1;
    }

    public void setE1(Expression e1) {
        this.e1 = e1;
    }

    public float eval(State[][] grid, int i, int j) {
        float eval1 = e1.eval(grid, i, j);
        return eval1 * eval1;
    }


    @Override
    abstract public String toString();
}
