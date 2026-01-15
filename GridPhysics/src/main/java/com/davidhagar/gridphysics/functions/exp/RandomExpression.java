package com.davidhagar.gridphysics.functions.exp;

import com.davidhagar.gridphysics.functions.exp.op.Expression;
import com.davidhagar.gridphysics.functions.exp.op.bin.*;
import com.davidhagar.gridphysics.functions.exp.op.leaf.Constant;
import com.davidhagar.gridphysics.functions.exp.op.leaf.GridValue;
import com.davidhagar.gridphysics.functions.exp.op.unary.Square;
import com.davidhagar.gridphysics.functions.exp.op.unary.SquareRoot;
import com.davidhagar.gridphysics.functions.exp.op.unary.UnaryOpp;
import com.davidhagar.gridphysics.util.RandomUtil;
import com.davidhagar.gridphysics.util.WeightedRandomSelector;

import java.util.ArrayList;
import java.util.Arrays;

import static com.davidhagar.gridphysics.util.RandomUtil.rInt;

public class RandomExpression {

    int maxLeafCount = 7;
    int minLeafCount = 2;
    int maxUnary = 2;
    int stateMax;
    int historyMax;
    float constMin = -4;
    float constMax = +4;
    WeightedRandomSelector<ExpGen> leafSelector;
    WeightedRandomSelector<BinaryOppGen> binaryOppSelector;
    WeightedRandomSelector<UnaryOppGen> unaryOppSelector;

    private interface ExpGen {
        Expression getInstance();
    }

    private interface BinaryOppGen {
        BinaryOpp getInstance();
    }

    private interface UnaryOppGen {
        UnaryOpp getInstance();
    }


    public RandomExpression(int stateMax, int historyMax, long seed) {
        this.stateMax = stateMax;
        this.historyMax = historyMax;

        leafSelector = new WeightedRandomSelector<>(seed);
        leafSelector.add(new ExpGen() {
            @Override
            public Expression getInstance() {
                return new Constant(RandomUtil.rFloat(constMin, constMax));
            }
        }, 0.4);


        leafSelector.add(new ExpGen() {
            @Override
            public Expression getInstance() {
                return new GridValue(rInt(-2, 2), rInt(-2, 2), rInt(0, stateMax - 1), rInt(1, historyMax - 1));
            }
        }, 2.0);


        binaryOppSelector = new WeightedRandomSelector<>(seed);
        binaryOppSelector.add(new BinaryOppGen() {
            @Override
            public BinaryOpp getInstance() {
                return new Add();
            }
        }, 3.0);

        binaryOppSelector.add(new BinaryOppGen() {
            @Override
            public BinaryOpp getInstance() {
                return new Sub();
            }
        }, 2.0);

        binaryOppSelector.add(new BinaryOppGen() {
            @Override
            public BinaryOpp getInstance() {
                return new Mult();
            }
        }, 1.0);

        binaryOppSelector.add(new BinaryOppGen() {
            @Override
            public BinaryOpp getInstance() {
                return new Div();
            }
        }, 1.0);


        unaryOppSelector = new WeightedRandomSelector<>(seed);
        unaryOppSelector.add(new UnaryOppGen() {
            @Override
            public UnaryOpp getInstance() {
                return new SquareRoot();
            }
        }, 1);

        unaryOppSelector.add(new UnaryOppGen() {
            @Override
            public UnaryOpp getInstance() {
                return new Square();
            }
        }, 1);
    }


    public Expression randomExpression() {

        int leafCount = rInt(minLeafCount, maxLeafCount);
        Expression[] leafs = new Expression[leafCount];

        for (int i = 0; i < leafCount; i++) {
            leafs[i] = newLeaf();
        }

        ArrayList<Expression> exList = new ArrayList<>(Arrays.asList(leafs));

        int unaryCount = rInt(0, maxUnary);
        for (int i = 0; i < unaryCount; i++) {
            int index = rInt(0, exList.size() - 1);
            Expression e1 = exList.get(index);
            UnaryOpp unaryOpp = newUnary();
            unaryOpp.setE1(e1);
            exList.set(index,unaryOpp);
        }

        while (exList.size() > 1) {
            Expression e1 = exList.remove(rInt(0, exList.size() - 1));
            Expression e2 = exList.remove(rInt(0, exList.size() - 1));
            BinaryOpp opExpression = newBinary();
            opExpression.setE1(e1);
            opExpression.setE2(e2);
            exList.add(opExpression);
        }

        return exList.getFirst();
    }

    public UnaryOpp newUnary() {
        return unaryOppSelector.selectRandomObject().getInstance();
    }

    public BinaryOpp newBinary() {
        return binaryOppSelector.selectRandomObject().getInstance();
    }

    public BinaryOpp newBinary(BinaryOpp excludeOpp) {

        while(true){
            BinaryOpp op = newBinary();
            if(op.getClass() != excludeOpp.getClass())
                return op;
        }
    }

    public Expression newLeaf() {
        return leafSelector.selectRandomObject().getInstance();
    }
}
