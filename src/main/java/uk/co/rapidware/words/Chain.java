package uk.co.rapidware.words;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * Created by Jivraj on 30/07/2017.
 */
public class Chain {

    static int longestChain(String[] words) {
        Arrays.sort(words, (o1, o2) -> o2.length() - o1.length());
        final Set<String> library = new HashSet<>(Arrays.asList(words));

        int longestChain = 0;
        for (final String word : words) {
            if (word.length() <= longestChain) {
                break;
            } else {
                final int maxChainOfThisWord = computeMaxChainUsingStack(word, library);
                longestChain = Math.max(longestChain, maxChainOfThisWord);
            }
        }
        return longestChain;
    }

    private static int computeMaxChain(final String word, final Set<String> library) {
        final int wordLength = word.length();
        int maxChain = 1;
        final Queue<String> wordsInChain = new ArrayDeque<>(word.length()); // default capacity
        wordsInChain.offer(word);
        while (!wordsInChain.isEmpty()) {
            final String wordToChain = wordsInChain.poll();
            for (int index = 0; index < wordToChain.length(); index++) {
                final StringBuilder wordChainBuilder = new StringBuilder(wordToChain);
                final String nextPossibleWordInChain = wordChainBuilder.deleteCharAt(index)
                                                                       .toString();
                if (library.contains(nextPossibleWordInChain)) {
                    wordsInChain.offer(nextPossibleWordInChain);
                    maxChain = wordLength - nextPossibleWordInChain.length() + 1;
                }
            }
        }
        return maxChain;
    }

    private static int computeMaxChainUsingStack(final String word, final Set<String> library) {
        final int wordLength = word.length();
        int maxChain = 1;

        // Use a stack so that you get effectively a depth-first chain traversal
        // As this has the opportunity to be more optimal due to possibility of short
        // circuit if the max chain for the word has already been computed
        final Stack<String> wordsInChain = new Stack<>(); // default capacity
        wordsInChain.push(word);

        // See comment above to understand the short circuit in this clause
        while (!wordsInChain.isEmpty() && maxChain < wordLength) {
            final String wordToChain = wordsInChain.pop();
            for (int index = 0; index < wordToChain.length(); index++) {
                final StringBuilder wordChainBuilder = new StringBuilder(wordToChain);
                final String nextPossibleWordInChain = wordChainBuilder.deleteCharAt(index)
                                                                       .toString();
                if (library.contains(nextPossibleWordInChain)) {
                    wordsInChain.push(nextPossibleWordInChain);
                    maxChain = wordLength - nextPossibleWordInChain.length() + 1;
                }
            }
        }
        return maxChain;
    }}
