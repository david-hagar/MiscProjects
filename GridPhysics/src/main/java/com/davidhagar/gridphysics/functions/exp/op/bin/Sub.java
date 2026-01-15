package com.davidhagar.gridphysics.functions.exp.op.bin;

import com.davidhagar.gridphysics.State;
import com.davidhagar.gridphysics.functions.exp.op.Expression;


public class Sub extends BinaryOpp {


    public Sub() {
    }

    public Sub(Expression e1, Expression e2) {
       super(e1,e2);
    }

    @Override
    public float eval(State[][] grid, int i, int j) {
        float eval1 = e1.eval(grid,i,j);
        float eval2 = e2.eval(grid,i,j);
        return eval1 - eval2;
    }

    @Override
    public String toString() {
        return "(" + e1 + " - " + e2 + ')';
    }

    public Expression copy(){
        return new Sub(e1.copy(), e2.copy());
    }
}
