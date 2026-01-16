package com.davidhagar.gridphysics.functions.exp.op;

import com.davidhagar.gridphysics.State;
import com.davidhagar.gridphysics.functions.exp.ga.ExpressionGO;
import com.davidhagar.gridphysics.functions.exp.ga.ExpressionMutator;
import com.davidhagar.gridphysics.functions.exp.op.bin.*;
import com.davidhagar.gridphysics.functions.exp.op.leaf.Constant;
import com.davidhagar.gridphysics.functions.exp.op.leaf.GridValue;
import com.davidhagar.gridphysics.functions.exp.op.unary.Square;
import com.davidhagar.gridphysics.functions.exp.op.unary.SquareRoot;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, // Use a logical name for type identification
        include = JsonTypeInfo.As.PROPERTY, // Include type info as a separate property
        property = "type" // Name of the property containing type info
)

//@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)

@JsonSubTypes({
        @JsonSubTypes.Type(value = Add.class, name = "Add"),
        @JsonSubTypes.Type(value = Div.class, name = "Div"),
        @JsonSubTypes.Type(value = Mult.class, name = "Mult"),
        @JsonSubTypes.Type(value = Sub.class, name = "Sub"),

        @JsonSubTypes.Type(value = Constant.class, name = "Constant"),
        @JsonSubTypes.Type(value = GridValue.class, name = "GridValue"),

        @JsonSubTypes.Type(value = Square.class, name = "Square"),
        @JsonSubTypes.Type(value = SquareRoot.class, name = "SquareRoot")
})
public interface Expression {
    float eval(State[][] grid, int i, int j);
    void walkVisitor(ExpressionVisitor visitor, Expression parent);
    Expression copy();
    void replaceChild(Expression toReplace, Expression replacementValue);
    boolean mutate(ExpressionMutator.EMSettings settings);
}
