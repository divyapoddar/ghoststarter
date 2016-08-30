package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    Random random = new Random();

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
                words.add(word);
        }
        Collections.sort(words);
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        String word;
        if (prefix.isEmpty()) {
            word = words.get(random.nextInt(words.size()));
            return word;
        } else {
            try {
                int index = prefixSearch(prefix);
                word = words.get(index);
                Log.i(TAG, "getAnyWordStartingWith: " + word);
                return word;
            } catch (ArrayIndexOutOfBoundsException e) {
                return null;
            }

        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }

    private int prefixSearch(String prefix) {
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.substring(0,Math.min(o1.length(),o2.length())).compareTo(o2);
            }
        };

        return Collections.binarySearch(words, prefix, comparator);
    }
}
