package com.davidhagar.gridphysics.functions.exp.op;

import com.davidhagar.gridphysics.State;


public class Add implements Expression {

    Expression e1;
    Expression e2;


    public Add() {
    }

    public Add(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }


    public Expression getE1() {
        return e1;
    }

    public void setE1(Expression e1) {
        this.e1 = e1;
    }

    public Expression getE2() {
        return e2;
    }

    public void setE2(Expression e2) {
        this.e2 = e2;
    }

    @Override
    public float[] eval(State[][] grid, int i, int j) {
        return new float[0];
    }


    @Override
    public String toString() {
        return "(" + e1 + " + " + e2 + ')';
    }
}
