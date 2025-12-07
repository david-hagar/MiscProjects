package com.david_hagar.bui;

import junit.framework.TestCase;
import org.junit.Assert;

import java.util.ArrayList;

public class ParserTest extends TestCase {


    public void test() {

        ArrayList<Symbol> out = new ArrayList<>();
        ArrayList<Symbol> newSymbol = new ArrayList<>();

        Parser p = new Parser(new Parser.SymbolOutput() {
            @Override
            public void outputSymbol(Symbol s) {
                out.add(s);
                System.out.println("out symbol: " + s);
            }
        }, new Parser.NewCharacter() {
            @Override
            public Symbol addNewCharacter(char c) {
                final Symbol s = new Symbol(Character.toString(c), 0);
                newSymbol.add(s);
                System.out.println("new symbol \'" + c + "\'");
                return s;
            }
        });

        Symbol s1 = new Symbol("123", 0);
        p.addSymbol(s1);

        Symbol s2 = new Symbol("12", 0);
        p.addSymbol(s2);

        p.addSymbol(new Symbol("4", 0));


//        p.removeSymbol(s2);
//        p.removeSymbol(s1);


        p.addChar('1');
        Assert.assertEquals(out.size(), 0);


        p.addChar('2');
        Assert.assertEquals(out.size(), 0);
        p.addChar('4');

        ArrayList<Symbol> expectedOut = new ArrayList<>();
        expectedOut.add(new Symbol("12", 0));
        Assert.assertEquals(expectedOut, out);

        p.add("1234");
        expectedOut.add(new Symbol("4", 0));
        expectedOut.add(new Symbol("123", 0));
        Assert.assertEquals(expectedOut, out);

        ArrayList<Symbol> expectedNewSymbol = new ArrayList<>();

        Assert.assertEquals(expectedNewSymbol, newSymbol);
        p.add("14");
        expectedOut.add(new Symbol("4", 0));
        expectedNewSymbol.add(new Symbol("1", 0));

        Assert.assertEquals(expectedOut, out);
        Assert.assertEquals(expectedNewSymbol, newSymbol);
    }


}