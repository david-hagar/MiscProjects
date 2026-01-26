package com.davidhagar.gridphysics.functions.exp.op.leaf;

import com.davidhagar.gridphysics.Sim;
import com.davidhagar.gridphysics.State;
import com.davidhagar.gridphysics.functions.exp.ga.ExpressionMutator;
import com.davidhagar.gridphysics.functions.exp.op.Expression;
import com.davidhagar.gridphysics.functions.exp.op.ExpressionVisitor;
import com.davidhagar.gridphysics.util.RandomSelector;
import com.davidhagar.gridphysics.util.RandomUtil;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.DEDUCTION;

@JsonTypeInfo(use = DEDUCTION)
public class GridValue extends LeafOpp {

    private static interface Change {
        void change(GridValue gv);
    }

    static RandomSelector<Change> rs = new RandomSelector<Change>(999);

    static {
        rs.add(new Change() {
            @Override
            public void change(GridValue gv) {
                gv.iOffset = RandomUtil.rChangeLimitExclude(gv.iOffset, -3, 3);
            }
        });
        rs.add(new Change() {
            @Override
            public void change(GridValue gv) {
                gv.jOffset = RandomUtil.rChangeLimitExclude(gv.jOffset, -3, 3);
            }
        });
        rs.add(new Change() {
            @Override
            public void change(GridValue gv) {
                int stateSize = Sim.getInstance().stateFunction.getStateSize();
                gv.stateIndex = RandomUtil.rIntExclude( gv.stateIndex, 0, stateSize - 1);
            }
        });
        rs.add(new Change() {
            @Override
            public void change(GridValue gv) {
                int historySize = Sim.getInstance().stateFunction.getHistorySize();
                gv.historyIndex = RandomUtil.rChangeLimitExclude(gv.historyIndex, 1, historySize - 1);
            }
        });

    }

    int iOffset;
    int jOffset;
    int stateIndex;
    int historyIndex;

    public GridValue() {
    }

    public GridValue(int iOffset, int jOffset, int historyIndex, int stateIndex) {
        this.iOffset = iOffset;
        this.jOffset = jOffset;
        this.stateIndex = stateIndex;
        this.historyIndex = historyIndex;
    }

    public int getStateIndex() {
        return stateIndex;
    }

    public void setStateIndex(int stateIndex) {
        this.stateIndex = stateIndex;
    }

    public int getHistoryIndex() {
        return historyIndex;
    }

    public void setHistoryIndex(int historyIndex) {
        this.historyIndex = historyIndex;
    }

    public int getiOffset() {
        return iOffset;
    }

    public void setiOffset(int iOffset) {
        this.iOffset = iOffset;
    }

    public int getjOffset() {
        return jOffset;
    }

    public void setjOffset(int jOffset) {
        this.jOffset = jOffset;
    }

    @Override
    public float eval(State[][] grid, int i, int j) {
        int iIndex = wrap(grid.length, i + iOffset);
        int jIndex = wrap(grid[0].length, j + jOffset);
        return grid[iIndex][jIndex].values[historyIndex][stateIndex];
    }

    private static int wrap(int limit, int index) {
        if (index >= limit)
            index -= limit;
        if (index < 0)
            index += limit;
        return index;
    }

    @Override
    public String toString() {
        return "g(" +
                "i+" + iOffset +
                ", j+" + jOffset +
                ") [" + historyIndex + "][" + stateIndex + "]";
    }

    public void walkVisitor(ExpressionVisitor visitor, Expression parent) {
        visitor.visit(this, parent);
    }

    public Expression copy() {
        return new GridValue(iOffset, jOffset, historyIndex, stateIndex);
    }

    public boolean mutate(ExpressionMutator.EMSettings settings) {
        rs.selectRandomObject().change(this);
        return true;
    }
}
