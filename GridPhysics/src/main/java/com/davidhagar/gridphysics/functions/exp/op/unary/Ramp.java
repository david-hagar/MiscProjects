package com.davidhagar.gridphysics.functions.exp.op.unary;

import com.davidhagar.gridphysics.State;
import com.davidhagar.gridphysics.functions.exp.op.Expression;

public class Ramp extends UnaryOpp {


    public Ramp() {
    }

    public Ramp(Expression e1) {
        super(e1);
    }


    public float eval(State[][] grid, int i, int j) {
        float eval1 = e1.eval(grid, i, j);
        return eval1 > 0 ? eval1 : 0;
    }

    @Override
    public Expression copy() {
        return new Ramp(e1);
    }


    @Override
    public String toString() {
        return "Ramp(" + e1 + " )";
    }
}
