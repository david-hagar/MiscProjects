package com.davidhagar.gridphysics.functions.exp.ga;

import com.davidhagar.gridphysics.Sim;
import com.davidhagar.gridphysics.functions.exp.RandomExpression;
import com.davidhagar.gridphysics.functions.exp.op.Expression;
import com.davidhagar.gridphysics.functions.exp.op.ExpressionVisitor;
import com.davidhagar.gridphysics.functions.exp.op.bin.BinaryOpp;
import com.davidhagar.gridphysics.functions.exp.op.leaf.LeafOpp;
import com.davidhagar.gridphysics.functions.exp.op.unary.NoOpp;
import com.davidhagar.gridphysics.functions.exp.op.unary.UnaryOpp;
import com.davidhagar.gridphysics.util.RandomUtil;
import com.davidhagar.gridphysics.util.WeightedRandomSelector;

import java.util.ArrayList;

public class ExpressionMutator {

    private interface Mutator {
        public boolean mutate(Expression expression, Expression parent);
    }


    public static class EMSettings {
        float maxChangeFractionFloat = 0.1f;
        float changeSignProbability = 0.1f;

        public float changeFloat(float value) {
            return value + value * RandomUtil.rFloat(-maxChangeFractionFloat, maxChangeFractionFloat);
        }

        public float changeFloatAndSign(float value) {
            return changeSign(changeFloat(value));
        }

        public float changeSign(float value) {
            return RandomUtil.random.nextFloat() < changeSignProbability ? -value : value;
        }

    }

    WeightedRandomSelector<Mutator> unarySelector;
    WeightedRandomSelector<Mutator> leafSelector;
    WeightedRandomSelector<Mutator> binarySelector;

    RandomExpression randomExpression;


    public ExpressionMutator(Sim sim) {
        EMSettings ems = new EMSettings();

        randomExpression = new RandomExpression(sim.stateFunction.getStateSize(), sim.stateFunction.getHistorySize(), sim.randomSeed);

        unarySelector = new WeightedRandomSelector<Mutator>(sim.randomSeed);
        leafSelector = new WeightedRandomSelector<Mutator>(sim.randomSeed);
        binarySelector = new WeightedRandomSelector<Mutator>(sim.randomSeed);

        unarySelector.add(new Mutator() {
            @Override
            public boolean mutate(Expression expression, Expression parent) {      // remove
                UnaryOpp o = (UnaryOpp) expression;
                parent.replaceChild(o, o.getE1());
                return true;
            }
        }, 1);

        unarySelector.add(new Mutator() {
            @Override
            public boolean mutate(Expression expression, Expression parent) {  // add unary
                UnaryOpp o = (UnaryOpp) expression;

                UnaryOpp newOpp = randomExpression.newUnary();
                newOpp.setE1(o.getE1());
                parent.replaceChild(o, newOpp);

                return true;
            }
        }, 1);

        leafSelector.add(new Mutator() {
            @Override
            public boolean mutate(Expression expression, Expression parent) {  // change
                return expression.mutate(ems);
            }
        }, 1);

        leafSelector.add(new Mutator() {
            @Override
            public boolean mutate(Expression expression, Expression parent) {  // add binary
                LeafOpp o = (LeafOpp) expression;

                BinaryOpp newOpp = randomExpression.newBinary();
                newOpp.setE1(o);
                newOpp.setE2(randomExpression.newLeaf());
                parent.replaceChild(o, newOpp);

                return true;
            }
        }, 1);

        binarySelector.add(new Mutator() {
            @Override
            public boolean mutate(Expression expression, Expression parent) {
                BinaryOpp o = (BinaryOpp) expression;
                BinaryOpp nb = randomExpression.newBinary(o);
                nb.setE1(o.getE1());
                nb.setE2(o.getE2());
                parent.replaceChild(o, nb);
                return true;
            }
        }, 1);


    }


    public Expression mutate(Expression ex) {

        NoOpp eCopy = new NoOpp(ex.copy());

        ArrayList<Expression> list = new ArrayList<>(100);
        ArrayList<Expression> parentList = new ArrayList<>(100);

        eCopy.walkVisitor(new ExpressionVisitor() {
            @Override
            public void visit(Expression expression, Expression parent) {
                list.add(expression);
                parentList.add(parent);
            }
        }, null);

        boolean res;

        do {
            int index = RandomUtil.rInt(1, list.size() - 1); // skip NoOpp at 0
            Expression e = list.get(index);
            Expression parent = parentList.get(index);

            if (e instanceof UnaryOpp)
                res = unarySelector.selectRandomObject().mutate(e, parent);
            else if (e instanceof BinaryOpp)
                res = binarySelector.selectRandomObject().mutate(e, parent);
            else if (e instanceof LeafOpp)
                res = leafSelector.selectRandomObject().mutate(e, parent);
            else throw new NullPointerException("unexpected class " + e.getClass());
        } while (!res);

        return eCopy.getE1();
    }

}
