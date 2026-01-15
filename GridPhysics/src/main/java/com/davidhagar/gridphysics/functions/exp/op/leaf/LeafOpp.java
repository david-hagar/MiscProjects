package com.davidhagar.gridphysics.functions.exp.op.leaf;

import com.davidhagar.gridphysics.functions.exp.op.Expression;

public abstract class LeafOpp implements Expression {

    public void replaceChild(Expression toReplace, Expression replacementValue){
         throw new NullPointerException("no child expression");
    }
}
