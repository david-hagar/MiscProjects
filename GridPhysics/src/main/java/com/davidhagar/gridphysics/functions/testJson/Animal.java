package com.davidhagar.gridphysics.functions.testJson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

//@JsonTypeInfo(
//        use = JsonTypeInfo.Id.NAME, // Use a logical name for type identification
//        include = JsonTypeInfo.As.PROPERTY, // Include type info as a separate property
//        property = "type" // Name of the property containing type info
//)
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)

@JsonSubTypes({
        @JsonSubTypes.Type(value = Dog.class, name = "dog"),
        @JsonSubTypes.Type(value = Cat.class, name = "cat")
})
public abstract class Animal {
    public String name;
    // ... other common properties
}
