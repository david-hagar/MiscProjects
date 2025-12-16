package com.davidhagar.gridphysics.functions.exp;

import com.davidhagar.gridphysics.functions.exp.op.Expression;
import com.davidhagar.gridphysics.functions.exp.op.bin.*;
import com.davidhagar.gridphysics.functions.exp.op.leaf.Constant;
import com.davidhagar.gridphysics.functions.exp.op.leaf.GridValue;
import com.davidhagar.gridphysics.util.RandomUtil;
import com.davidhagar.gridphysics.util.WeightedRandomSelector;

import java.util.ArrayList;
import java.util.Arrays;

public class RandomExpression {

    int maxLeafCount = 7;
    int minLeafCount = 2;
    int stateMax = 2;
    int historyMax = 2;
    float constMin = -4;
    float constMax = +4;
    WeightedRandomSelector<ExpGen> leafSelector = new WeightedRandomSelector<>();
    WeightedRandomSelector<OppGen> oppSelector = new WeightedRandomSelector<>();

    private interface ExpGen {
        Expression getInstance();
    }

    private interface OppGen {
        BinaryOpp getInstance();
    }


    public RandomExpression() {

        leafSelector.add(new ExpGen() {
            @Override
            public Expression getInstance() {
                return new Constant(RandomUtil.rDouble(constMin, constMax));
            }
        }, 0.4);


        leafSelector.add(new ExpGen() {
            @Override
            public Expression getInstance() {
                return new GridValue(RandomUtil.rInt(-2, 2), RandomUtil.rInt(-2, 2), RandomUtil.rInt(0, stateMax), RandomUtil.rInt(1, historyMax));
            }
        }, 2.0);


        oppSelector.add(new OppGen() {
            @Override
            public BinaryOpp getInstance() {
                return new Add();
            }
        }, 3.0);

        oppSelector.add(new OppGen() {
            @Override
            public BinaryOpp getInstance() {
                return new Sub();
            }
        }, 2.0);

        oppSelector.add(new OppGen() {
            @Override
            public BinaryOpp getInstance() {
                return new Mult();
            }
        }, 1.0);

        oppSelector.add(new OppGen() {
            @Override
            public BinaryOpp getInstance() {
                return new Div();
            }
        }, 1.0);

    }
    
    
    
    
    public Expression randomExpression(){
        
        int leafCount = RandomUtil.rInt(minLeafCount, maxLeafCount);
        Expression[] leafs = new Expression[leafCount];

        for (int i = 0; i < leafCount; i++) {
            leafs[i] = leafSelector.selectRandomObject().getInstance();
        }



        int oppCount = leafCount -1;
        ArrayList<Expression> exList = new ArrayList<>(Arrays.asList(leafs));

        while(exList.size() >1){
            Expression e1 = exList.remove(RandomUtil.rInt(0, exList.size()-1));
            Expression e2 = exList.remove(RandomUtil.rInt(0, exList.size()-1));
            BinaryOpp opExpression = oppSelector.selectRandomObject().getInstance();
            opExpression.setE1(e1);
            opExpression.setE2(e2);
            exList.add(opExpression);
        }

        return exList.getFirst();
    }
}
