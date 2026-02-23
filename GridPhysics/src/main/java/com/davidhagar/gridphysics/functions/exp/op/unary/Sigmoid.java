package com.davidhagar.gridphysics.functions.exp.op.unary;

import com.davidhagar.gridphysics.State;
import com.davidhagar.gridphysics.functions.exp.op.Expression;

public class Sigmoid extends UnaryOpp {


    public Sigmoid() {
    }

    public Sigmoid(Expression e1){
        super(e1);
    }

    public float eval(State[][] grid, int i, int j) {
        float eval1 = e1.eval(grid, i, j);
        return (float)( 1.0 / (1.0 + Math.exp(-eval1)));
    }

    @Override
    public Expression copy() {
        return new Sigmoid(e1);
    }


    @Override
    public String toString() {
        return "Sin(" + e1 + " )";
    }
}
