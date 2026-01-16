package com.davidhagar.gridphysics.functions.exp.io;

import com.davidhagar.gridphysics.functions.exp.RandomExpression;
import com.davidhagar.gridphysics.functions.exp.ga.ExpressionGO;
import com.davidhagar.gridphysics.functions.exp.op.Expression;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ExpressionContainerTest {

    @Test
    @DisplayName("testSave")
    public void testSave(@TempDir Path tempDir) throws IOException {

        RandomExpression re = new RandomExpression(1, 1, 1234);

        ExpressionContainer ec = new ExpressionContainer();
        int n = 20;
        for (int i = 0; i < n; i++) {
            Expression[] expressions = {re.getRandomExpression(), re.getRandomExpression()};
            ec.expressions.add(new ExpressionGO(expressions, i));
        }

        //File file = tempDir.resolve("ExpressionContainerTest.txt").toFile();
        File file = new File("./test.txt");
        System.out.println(file);

        ec.save(file);

        ExpressionContainer ec2 = ExpressionContainer.load(file);
        for (int i = 0; i < n; i++) {
            String s1 = ec.expressions.get(i).toString();
            String s2 = ec2.expressions.get(i).toString();
            assertEquals(s1, s2);

            System.out.println(s1);
        }

        assertEquals(n, ec.expressions.size());
        assertEquals(n, ec2.expressions.size());
    }
}