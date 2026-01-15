package com.davidhagar.gridphysics.functions.exp.op.bin;

import com.davidhagar.gridphysics.State;
import com.davidhagar.gridphysics.functions.exp.op.Expression;


public class Div extends BinaryOpp {


    public Div() {
    }

    public Div(Expression e1, Expression e2) {
       super(e1,e2);
    }


    @Override
    public float eval(State[][] grid, int i, int j) {
        float eval1 = e1.eval(grid,i,j);
        float eval2 = e2.eval(grid,i,j);
        return eval1 / eval2;
    }

    @Override
    public String toString() {
        return "(" + e1 + " / " + e2 + ')';
    }

    public Expression copy(){
        return new Div(e1.copy(), e2.copy());
    }
}
