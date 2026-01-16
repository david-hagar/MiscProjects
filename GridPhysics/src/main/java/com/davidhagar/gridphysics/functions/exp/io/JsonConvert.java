package com.davidhagar.gridphysics.functions.exp.io;


import com.davidhagar.gridphysics.functions.exp.ga.ExpressionGO;
import com.davidhagar.gridphysics.functions.exp.op.Expression;
import com.davidhagar.gridphysics.functions.exp.op.bin.Add;
import com.davidhagar.gridphysics.functions.exp.op.leaf.Constant;
import com.davidhagar.gridphysics.functions.exp.op.leaf.GridValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;


public class JsonConvert {

    ObjectMapper objectMapper = new ObjectMapper();

    public JsonConvert() {
    }


    static public void main(String[] args) {
        Expression exp = new Add(new Constant(1.5f), new GridValue(1, -1, 1, 0));

        JsonConvert jc = new JsonConvert();
        try {
            String jsonString = jc.toJsonString(exp);
            System.out.println(exp);
            System.out.println("Serialized JSON: " + jsonString);

            Expression exprR = jc.jsonToExpression(jsonString);
            System.out.println(exprR);

            Expression[] list = {exp, exp};
            String s = jc.objectMapper.writeValueAsString(list);
            System.out.println(s);

            //jc.objectMapper.readValue(s);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Expression jsonToExpression(String jsonString) throws JsonProcessingException {
        return objectMapper.readValue(jsonString, Expression.class);
    }

    private String toJsonString(Expression exp) throws JsonProcessingException {
        return objectMapper.writeValueAsString(exp);
    }
}