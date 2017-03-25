import java.util.*;
import java.util.stream.*;
/**
 * Created by SSI.
 */
public class Transformation {
    public static class Pair {
        private final String word1;
        private final String word2;

        public Pair(String word1, String word2) {
            this.word1 = word1;
            this.word2 = word2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pair pair = (Pair) o;

            return (word1.equals(pair.word1) && word2.equals(pair.word2)) ||
                   (word1.equals(pair.word2) && word2.equals(pair.word1));
        }

        @Override
        public int hashCode() {
           return word1.hashCode() + word2.hashCode();
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "word1='" + word1 + '\'' +
                    ", word2='" + word2 + '\'' +
                    '}';
        }
    }

    int wordLadder(String beginWord, String endWord, String[] wordList) {
        Set<String> wordSet = new LinkedHashSet<>();
        String[] distances = new String[wordList.length + 1];
        distances[0] = beginWord;
        System.arraycopy(wordList, 0, distances, 1, wordList.length);
        for (int i = 0; i < wordList.length; i++) {
            wordSet.add(wordList[i]);
        }

        Map<Pair, Integer> distanceMap = hammingDistances(distances);

        int count =  wordLadderInternal(beginWord, endWord, wordSet, new LinkedHashSet<>(), distanceMap, 1);
        return count;
    }

    static int wordLadderInternal(String beginWord,
                                  String endWord,
                                  Set<String> wordSet,
                                  Set<String> wordsUsed,
                                  Map<Pair, Integer> distances,
                                  int transformationCount) {
        Set<String> wordsNotUsed = new HashSet<>(wordSet);
        wordsNotUsed.removeAll(wordsUsed);
        Set<String> filteredWords = filterWords(beginWord, wordsNotUsed, distances);

        if (filteredWords.contains(endWord)) {
            return transformationCount + 1;
        }
        else {
            List<Integer> transformations = filteredWords.stream()
                .map(w -> {
                    Set<String> copySet = new LinkedHashSet<>(wordsUsed);
                    copySet.add(w);
                    return wordLadderInternal(w, endWord, wordSet, copySet, distances, transformationCount + 1);

                })
                .filter(i -> i > 0)
                .collect(Collectors.toList());


            if (transformations.isEmpty()) {
                return 0;
            }

            int minIndex = IntStream.range(0,transformations.size()).boxed()
                    .min(Comparator.comparingInt(transformations::get))
                    .get();  // or throw if empty list


            return transformations.get(minIndex);
        }
    }

    static Set<String> filterWords(String beginWord, Set<String> wordList) {
        char[] characters = convertCharactersToList(beginWord);
        return wordList.stream()
                .filter(w ->  {
                    char[]  wCharacters = convertCharactersToList(w);
                    int sum = 0;
                    for (int i = 0, size = wCharacters.length; i < size && sum <= 1; i++) {
                        if (wCharacters[i] != characters[i]) {
                            sum++;
                        }
                    }

                    return sum < 2;
                })
                .collect(Collectors.toSet());
    }

    static Set<String> filterWords(String beginWord,
                                   Set<String> wordList,
                                   Map<Pair, Integer> distances) {
       return wordList.stream()
                .filter(w ->  distances.get(new Pair(beginWord, w)) < 2)
                .collect(Collectors.toSet());
    }

    public static Map<Pair, Integer> hammingDistances(String[] words) {
        Map<Pair, Integer> map = new HashMap<>();

        for (int j = 0; j < words.length -1; j++) {
            for (int i = j + 1; i < words.length; i++) {
                Pair pair = new Pair(words[j], words[i]);
                int distance = hammingDistance(words[j], words[i]);
                map.put(pair, distance);
            }
        }

        return map;
    }

    static int hammingDistance(String beginWord, String endWord) {
        char[] characters = convertCharactersToList(beginWord);
        char[] wCharacters = convertCharactersToList(endWord);
        int sum = 0;
        for (int i = 0, size = wCharacters.length; i < size; i++) {
            if (wCharacters[i] != characters[i]) {
                sum++;
            }
        }


        return sum;
    }

    static char[] convertCharactersToList(String str) {
        return str.toCharArray();
    }
}
