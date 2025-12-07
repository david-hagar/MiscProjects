package com.davidhagar.gridphysics.functions.exp.op;

import com.davidhagar.gridphysics.State;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, // Use a logical name for type identification
        include = JsonTypeInfo.As.PROPERTY, // Include type info as a separate property
        property = "type" // Name of the property containing type info
)

//@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)

@JsonSubTypes({
        @JsonSubTypes.Type(value = Add.class, name = "add"),
        @JsonSubTypes.Type(value = Constant.class, name = "const"),
        @JsonSubTypes.Type(value = GridValue.class, name = "grid")
})
public interface Expression {
    float[] eval(State[][] grid, int i, int j);
}
