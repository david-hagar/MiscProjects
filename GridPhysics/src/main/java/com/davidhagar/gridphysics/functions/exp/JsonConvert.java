package com.davidhagar.gridphysics.functions.exp;


import com.davidhagar.gridphysics.functions.exp.op.bin.Add;
import com.davidhagar.gridphysics.functions.exp.op.leaf.Constant;
import com.davidhagar.gridphysics.functions.exp.op.Expression;
import com.davidhagar.gridphysics.functions.exp.op.leaf.GridValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonConvert {

    ObjectMapper objectMapper = new ObjectMapper();

    public JsonConvert() {
    }


    public void main(String[] args) {
        Expression exp = new Add(new Constant(1.5f), new GridValue(1, -1,0,1));


        try {
            String jsonString = toJsonString(exp);
            System.out.println(exp);
            System.out.println("Serialized JSON: " + jsonString);

            Expression exprR = jsonToExpression(jsonString);
            System.out.println(exprR);

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