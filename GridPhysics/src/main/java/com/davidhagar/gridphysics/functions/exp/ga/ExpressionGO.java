package com.davidhagar.gridphysics.functions.exp.ga;

import com.davidhagar.gridphysics.functions.exp.op.Expression;
import com.davidhagar.gridphysics.functions.exp.op.bin.Add;
import com.davidhagar.gridphysics.functions.exp.op.bin.Div;
import com.davidhagar.gridphysics.functions.exp.op.bin.Mult;
import com.davidhagar.gridphysics.functions.exp.op.bin.Sub;
import com.davidhagar.gridphysics.functions.exp.op.leaf.Constant;
import com.davidhagar.gridphysics.functions.exp.op.leaf.GridValue;
import com.davidhagar.gridphysics.functions.exp.op.unary.Square;
import com.davidhagar.gridphysics.functions.exp.op.unary.SquareRoot;
import com.davidhagar.gridphysics.genetic.GeneticObject;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Arrays;
import java.util.Objects;



public class ExpressionGO extends GeneticObject {
    public Expression[] expressions;

    public ExpressionGO() {
    }

    public ExpressionGO(Expression[] expressions) {
        this.expressions = expressions;
    }

    public ExpressionGO(Expression[] expressions, float fitness) {
        super(fitness);
        this.expressions = expressions;
    }

    public Expression[] getExpressions() {
        return expressions;
    }

    public void setExpressions(Expression[] expressions) {
        this.expressions = expressions;
    }

//    @Override
//    public boolean equals(Object o) { // need to implement ?
//        return this == o;
//    }
//
//    @Override
//    public int hashCode() { // need to implement ?
//        return Objects.hash(this);
//    }


    @Override
    public String toString() {
        return "ExpressionGO{" +
                "expressions=" + Arrays.toString(expressions) +
                '}';
    }
}
