package com.davidhagar.gridphysics.functions.exp.op.bin;

import com.davidhagar.gridphysics.State;
import com.davidhagar.gridphysics.functions.exp.op.Expression;

public abstract class BinaryOpp implements Expression{


    Expression e1;
    Expression e2;


    public BinaryOpp() {
    }

    public BinaryOpp(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }


    public Expression getE1() {
        return e1;
    }

    public void setE1(Expression e1) {
        this.e1 = e1;
    }

    public Expression getE2() {
        return e2;
    }

    public void setE2(Expression e2) {
        this.e2 = e2;
    }

    abstract public float eval(State[][] grid, int i, int j) ;


    @Override
    abstract public String toString();
}
