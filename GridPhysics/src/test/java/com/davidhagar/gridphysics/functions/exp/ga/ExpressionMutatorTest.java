package com.davidhagar.gridphysics.functions.exp.ga;

import com.davidhagar.gridphysics.Sim;
import com.davidhagar.gridphysics.functions.exp.op.Expression;
import com.davidhagar.gridphysics.functions.exp.op.bin.Add;
import com.davidhagar.gridphysics.functions.exp.op.leaf.Constant;
import com.davidhagar.gridphysics.functions.exp.op.leaf.GridValue;
import junit.framework.TestCase;

public class ExpressionMutatorTest extends TestCase {

    public void testMutate() {
        Sim sim = Sim.makeExpression();
        ExpressionMutator em = new ExpressionMutator(sim);

        Expression e = new Add(new Constant(2), new GridValue(1, 2, 1, 2));

        for (int i = 0; i < 100; i++) {
            Expression me = em.mutate(e);

            System.out.println();
            System.out.println(e);
            System.out.println(me);

            if (me.toString().equals(e.toString())) {
                System.out.println("no change");
                fail("no change");
                break;
            }
        }
    }
}