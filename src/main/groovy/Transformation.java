import java.util.*;
import java.util.stream.*;
/**
 * Created by SSI.
 */
public class Transformation {

    int wordLadder(String beginWord, String endWord, String[] wordList) {
        Set<String> wordSet = new LinkedHashSet<>();
        for (int i = 0; i < wordList.length; i++) {
            wordSet.add(wordList[i]);
        }

        int count =  wordLadderInternal(beginWord, endWord, wordSet, new LinkedHashSet<>(), 1);
        return count;
    }

    static int wordLadderInternal(String beginWord,
                                  String endWord,
                                  Set<String> wordSet,
                                  Set<String> wordsUsed,
                                  int transformationCount) {
        Set<String> wordsNotUsed = new HashSet<>(wordSet);
        wordsNotUsed.removeAll(wordsUsed);
        Set<String> filteredWords = filterWords(beginWord, wordsNotUsed);

        if (filteredWords.contains(endWord)) {
            return transformationCount + 1;
        }
        else {
            List<Integer> transformations = filteredWords.stream()
                .map(w -> {
                    Set<String> copySet = new LinkedHashSet<>(wordsUsed);
                    copySet.add(w);
                    return wordLadderInternal(w, endWord, wordSet, copySet, transformationCount + 1);

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

    static char[] convertCharactersToList(String str) {
        return str.toCharArray();
    }
}
