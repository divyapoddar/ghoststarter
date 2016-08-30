package com.google.engedu.ghost;

import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;
import java.util.Random;


public class Trie {
    private static final String TAG = "Trie";
    public TrieNode root;

    private class TrieNode{
        private HashMap<Character, TrieNode> children;
        private boolean isWord;

        TrieNode(){
            children = new HashMap<>();
            isWord = false;
        }
    }

    public Trie() {
        root = new TrieNode();
    }

    public void add(String s) {
        TrieNode currentNode = root;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            TrieNode trieNode = currentNode.children.get(c);
            if (trieNode == null) {
                trieNode = new TrieNode();
                currentNode.children.put(c, trieNode);
            }
            currentNode = trieNode;
            if (i == s.length() - 1) trieNode.isWord = true;
        }
    }


    public boolean isWord(String s) {
        TrieNode node = getLastNode(s);
        return node != null && node.isWord;
    }

    @Nullable
    private TrieNode getLastNode(String s) {
        TrieNode currentNode = root;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (root.children.containsKey(c)) {
                currentNode = root.children.get(c);
            } else {
                return null;
            }
        }

        return currentNode;
    }

    public String getAnyWordStartingWith(String prefix) {
        Log.i(TAG, "getAnyWordStartingWith: ");
        TrieNode prefixNode = getLastNode(prefix);
        if (prefixNode != null) {
            String returnString = prefix;
            while (!prefixNode.isWord) {
                int length = prefixNode.children.keySet().toArray().length;
                Character c = (Character) prefixNode.children.keySet().toArray()[new Random().nextInt(length)];
                returnString += c;
                prefixNode = prefixNode.children.get(c);
                Log.i(TAG, "getAnyWordStartingWith: " + returnString);
            }
            return returnString;
        }
        return null;
    }

    public String getGoodWordStartingWith(String prefix) {
        Log.i(TAG, "getGoodWordStartingWith: ");
        TrieNode prefixNode = getLastNode(prefix);
        if (prefixNode != null) {

            int length = prefixNode.children.keySet().toArray().length;
            Character character = (Character) prefixNode.children.keySet().toArray()[new Random().nextInt(length)];
            TrieNode t = prefixNode.children.get(character);
            Log.i(TAG, "getGoodWordStartingWith: " + character);
            if (!t.isWord) return prefix + character;
            else return prefix;
        }
        return null;
    }
}
