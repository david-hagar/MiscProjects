package com.davidhagar.gridphysics.functions.exp.io;

import com.davidhagar.gridphysics.functions.exp.ga.ExpressionGO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class ExpressionContainer {

    ArrayList<ExpressionGO> expressions = new ArrayList<>();

    public ExpressionContainer() {
    }

    public ExpressionContainer(ArrayList<ExpressionGO> expressions) {
        this.expressions = expressions;
    }

    public ArrayList<ExpressionGO> getExpressions() {
        return expressions;
    }

    public void setExpressions(ArrayList<ExpressionGO> expressions) {
        this.expressions = expressions;
    }


    public void save(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(file, this);
    }

    static public ExpressionContainer load(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        ExpressionContainer ec = objectMapper.readValue(file, ExpressionContainer.class);
        return ec;
    }
}
