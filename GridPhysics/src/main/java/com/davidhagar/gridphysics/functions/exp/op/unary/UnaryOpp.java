package com.davidhagar.gridphysics.functions.exp.op.unary;

import com.davidhagar.gridphysics.State;
import com.davidhagar.gridphysics.functions.exp.op.Expression;

public abstract class UnaryOpp implements Expression{


    Expression e1;


    public UnaryOpp() {
    }

    public UnaryOpp(Expression e1, Expression e2) {
        this.e1 = e1;
     }


    public Expression getE1() {
        return e1;
    }

    public void setE1(Expression e1) {
        this.e1 = e1;
    }

    abstract public float eval(State[][] grid, int i, int j) ;


    @Override
    abstract public String toString();
}
