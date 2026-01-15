package com.davidhagar.gridphysics.functions.exp.op;

public interface ExpressionVisitor {
    public void visit(Expression expression, Expression parent);
}
