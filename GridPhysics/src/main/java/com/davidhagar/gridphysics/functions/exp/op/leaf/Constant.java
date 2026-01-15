package com.davidhagar.gridphysics.functions.exp.op.leaf;

import com.davidhagar.gridphysics.State;
import com.davidhagar.gridphysics.functions.exp.ga.ExpressionMutator;
import com.davidhagar.gridphysics.functions.exp.op.Expression;
import com.davidhagar.gridphysics.functions.exp.op.ExpressionVisitor;

public class Constant extends LeafOpp {

    float value;


    public Constant() {
    }

    public Constant(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public float eval(State[][] grid, int i, int j) {
        return value;
    }


    @Override
    public String toString() {
        return Float.toString(value);
    }

    public Expression copy(){
        return new Constant(value);
    }

    public void walkVisitor(ExpressionVisitor visitor, Expression parent){
        visitor.visit(this, parent);
    }

    @Override
    public boolean mutate(ExpressionMutator.EMSettings settings) {
       value = settings.changeFloatAndSign(value);
       return true;
    }
}
