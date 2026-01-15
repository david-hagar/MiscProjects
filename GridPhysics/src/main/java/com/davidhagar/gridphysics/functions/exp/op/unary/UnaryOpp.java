package com.davidhagar.gridphysics.functions.exp.op.unary;

import com.davidhagar.gridphysics.State;
import com.davidhagar.gridphysics.functions.exp.ga.ExpressionMutator;
import com.davidhagar.gridphysics.functions.exp.op.Expression;
import com.davidhagar.gridphysics.functions.exp.op.ExpressionVisitor;

public abstract class UnaryOpp implements Expression{


    Expression e1;


    public UnaryOpp() {
    }

    public UnaryOpp(Expression e1) {
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


    public void walkVisitor(ExpressionVisitor visitor, Expression parent){
        visitor.visit(this, parent);
        e1.walkVisitor(visitor, this);
    }

    public void replaceChild(Expression toReplace, Expression replacementValue){
        if( e1 == toReplace)
            e1 = replacementValue;
        else throw new NullPointerException("child expression not found " + toReplace);
    }


    public boolean mutate(ExpressionMutator.EMSettings settings) {
        return false;
    }
}
