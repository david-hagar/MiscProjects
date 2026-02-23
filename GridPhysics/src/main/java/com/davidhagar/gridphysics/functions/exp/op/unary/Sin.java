package com.davidhagar.gridphysics.functions.exp.op.unary;

import com.davidhagar.gridphysics.State;
import com.davidhagar.gridphysics.functions.exp.op.Expression;

public class Sin extends UnaryOpp {


    public Sin() {
    }

    public Sin(Expression e1){
        super(e1);
    }


    public float eval(State[][] grid, int i, int j) {
        float eval1 = e1.eval(grid, i, j);
        return (float) Math.sin(eval1);
    }

    @Override
    public Expression copy() {
        return new Sin(e1);
    }


    @Override
    public String toString() {
        return "Sin(" + e1 + " )";
    }
}
