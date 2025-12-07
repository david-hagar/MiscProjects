package com.davidhagar.gridphysics.functions.exp;


import com.davidhagar.gridphysics.functions.exp.op.Add;
import com.davidhagar.gridphysics.functions.exp.op.Constant;
import com.davidhagar.gridphysics.functions.exp.op.Expression;
import com.davidhagar.gridphysics.functions.exp.op.GridValue;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JSONRW {

    public static void main(String[] args) {
        Expression exp = new Add(new Constant(new float[]{1, 2.0f}), new GridValue(1,-1));

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String jsonString = objectMapper.writeValueAsString(exp);
            System.out.println(exp);
            System.out.println("Serialized JSON: " + jsonString);

            Expression exprR = objectMapper.readValue(jsonString, Expression.class);
            System.out.println(exprR);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}