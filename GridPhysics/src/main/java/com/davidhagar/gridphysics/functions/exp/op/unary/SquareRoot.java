package com.davidhagar.gridphysics.functions.exp.op.unary;

import com.davidhagar.gridphysics.State;
import com.davidhagar.gridphysics.functions.exp.op.Expression;

public class SquareRoot extends UnaryOpp {


    public SquareRoot() {
    }

    public SquareRoot(Expression e1){
        super(e1);
    }


    public float eval(State[][] grid, int i, int j) {
        float eval1 = e1.eval(grid, i, j);
        return (float) Math.sqrt(e1.eval(grid, i, j));
    }

    @Override
    public Expression copy() {
        return new SquareRoot(e1);
    }


    @Override
    public String toString() {
        return "Sqrt(" + e1 + " )";
    }
}
