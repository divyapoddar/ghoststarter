package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;


public class FastDictionary implements GhostDictionary {

    private Trie root;
    private ArrayList<String> words;

    public FastDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        root = new Trie();
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH) {
                words.add(word);
                root.add(word);
            }
        }
    }
    @Override
    public boolean isWord(String word) {
        return root.isWord(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if(prefix.isEmpty()){
            return words.get(new Random().nextInt(words.size()));
        }
        return root.getAnyWordStartingWith(prefix);
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        if(prefix.isEmpty()){
            return words.get(new Random().nextInt(words.size()));
        }
        return root.getGoodWordStartingWith(prefix);
    }
}
