package com.davidhagar.gridphysics.functions.exp.ga;

import com.davidhagar.gridphysics.functions.exp.op.Expression;
import com.davidhagar.gridphysics.genetic.GeneticObject;

public class ExpressionGO extends GeneticObject {
    private final Expression expression;

    public ExpressionGO(Expression expression) {
        this.expression = expression;
    }

    @Override
    public boolean equals(Object o) { // need to implement ?
        return false;
    }

    @Override
    public int hashCode() { // need to implement ?
        return this.hashCode();
    }
}
