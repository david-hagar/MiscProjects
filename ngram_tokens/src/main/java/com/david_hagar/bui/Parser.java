package com.david_hagar.bui;

import java.util.HashMap;
import java.util.LinkedList;

public class Parser {

    private final SymbolOutput so;
    private CharacterStage root = new CharacterStage(null, null, null);
    private LinkedList<Character> pushbackBuffer = new LinkedList<>();
    private CharacterStage currentStage = root;
    private final NewCharacter newCharacter;

    private class CharacterStage {
        private Symbol symbol;
        private final Character character;
        private final CharacterStage parent;
        private final HashMap<Character, CharacterStage> map = new HashMap<>(64);

        public CharacterStage(Symbol symbol, Character character, CharacterStage parent) {
            this.symbol = symbol;
            this.character = character;
            this.parent = parent;
        }
    }


    interface SymbolOutput {
        public void outputSymbol(Symbol s);
    }

    interface NewCharacter {
        public Symbol addNewCharacter(char c);
    }


    public Parser(SymbolOutput so, NewCharacter newCharacter) {
        this.so = so;
        this.newCharacter = newCharacter;
    }

    public void addSymbol(Symbol s) {

        CharacterStage stage = root;
        for (int i = 0; i < s.value.length(); i++) {
            char c = s.value.charAt(i);
            CharacterStage nextStage = stage.map.get(c);
            if (nextStage == null) {
                nextStage = new CharacterStage(null, c, stage);
                stage.map.put(c, nextStage);
            }
            stage = nextStage;
        }

        if (stage.symbol == null)
            stage.symbol = s;
        else
            throw new RuntimeException("duplicate symbol added: " + s + " prev:" + stage.symbol);
    }

    private boolean removeRecurse(int index, Symbol s, CharacterStage cs) {

        boolean rValue = false;

        char c = s.value.charAt(index);
        CharacterStage ncs = cs.map.get(c);
        if (ncs.symbol == s) {
            ncs.symbol = null;
            rValue = true;
        } else if (index + 1 < s.value.length())
            rValue = removeRecurse(index + 1, s, ncs);
        else
            rValue = false;

        if (ncs.map.isEmpty())
            cs.map.remove(c);

        return rValue;
    }

    public void removeSymbol(Symbol s) {
        removeRecurse(0, s, root);
    }

    public void add(String s) {
        for (int i = 0; i < s.length(); i++)
            addChar(s.charAt(i));
    }


    public void addChar(char c) {
        System.out.println("add " + c);
        if(pushbackBuffer.isEmpty())
            addInternalChar(c);
        else
            pushbackBuffer.addLast(c);

        while(! pushbackBuffer.isEmpty() )
            addInternalChar(pushbackBuffer.pollFirst());

    }



    private void addInternalChar(char c) {
        System.out.println("addi " + c);
        CharacterStage next = currentStage.map.get(c);
        if (next == null) {
            popSymbol(c);
        } else {
            currentStage = next;
        }
    }

    private void popSymbol(char c) {

        if (currentStage == root) {
            Symbol s = newCharacter.addNewCharacter(c);
            root.map.put(c, new CharacterStage(s, c, root));
            so.outputSymbol(s);
            return;
        }

        pushbackBuffer.addFirst(c);

        while (currentStage.symbol == null && currentStage != root) {
            if (currentStage.character != null)
                pushbackBuffer.addFirst(currentStage.character);
            currentStage = currentStage.parent;
        }

        if (currentStage.symbol != null) {
            so.outputSymbol(currentStage.symbol);
            currentStage = root;
        }
        //else throw new RuntimeException("Unexpected parser state.");
    }


}
