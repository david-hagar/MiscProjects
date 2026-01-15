package com.davidhagar.gridphysics.functions.exp.op.unary;

import com.davidhagar.gridphysics.State;
import com.davidhagar.gridphysics.functions.exp.op.Expression;

public class NoOpp extends UnaryOpp {


    public NoOpp() {
    }

    public NoOpp(Expression e1) {
        this.e1 = e1;
    }


    public float eval(State[][] grid, int i, int j) {
        return e1.eval(grid, i, j);
    }

    @Override
    public Expression copy() {
        return new NoOpp(e1);
    }


    public String toString() {
        return "NoOpp(" + e1 + " )";
    }
}
