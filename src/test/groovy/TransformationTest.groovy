import spock.lang.Specification
import spock.lang.Unroll


/**
 * Created by SSI.
 */
class TransformationTest extends Specification {

    @Unroll
    def "should find differences between words"() {
        when:
        def distances  = Transformation.hammingDistances(words)

        then:
        distances.size() == words.size()

        distances[(new Transformation.Pair("hat", "cog"))] == 3
        distances[(new Transformation.Pair("cog", "hat"))] == 3
        distances[(new Transformation.Pair("cog", "cot"))] == 1

        where:
        words << [["hat", "cog", "cot"] as String[]]

    }


    def "should build graph"() {
        when:
        Transformation.Graph graph = Transformation
                .buildGraph(["bot", "hat", "cot", "cog"] as String[])


        then:
        graph.nodes.size() == 4;
        graph.nodes['bot'].word == 'bot'
        graph.nodes['bot'].adjacentWords[0].word == 'cot'
        graph.nodes['bot'].adjacentWords[0].adjacentWords[0].word == 'cog'
        graph.nodes['hat'].adjacentWords.isEmpty()

    }

    @Unroll
    def "should skip words that would remove more than one character"() {
        when:
        def filteredWords = Transformation.filterWords(beginWord, words)

        then:
        filteredWords == expectedWord

        where:
        beginWord   | words                            |  expectedWord
        'cot'       | ["hat", "cog"]  as Set           | ['cog'] as Set
        'hot'       | ['cat', 'bot', 'bat'] as Set     | ['bot'] as Set
        'hot'       | ['coo', 'bot', 'bat'] as Set     | ['bot'] as Set
    }

    @Unroll
    def "should return 0 if word cannot be transformed for #words"() {
        when:
        int count = new Transformation().wordLadder(beginWord, "cog", words)


        then:
        count == 0

        where:
        beginWord   | words
        'cot'       | ["tat", "zoo", "boo", "gee"] as String[]
        'cot'       | ["cod", "cob"] as String[]
        'bat'       | ["hat", "cat", "cod"] as String[]
    }

    @Unroll
    def "transformation count should be #expectedCount for #words"() {
        when:
        int count = new Transformation().wordLadder(beginWord, end, words)

        then:
        count == expectedCount

        where:
        beginWord   | words                       |  end                           |  expectedCount
        'cot'       | ["hat", "bag"] as String[]  | 'cog' | 0
        'cot'       | ["hat", "cog"] as String[]  | 'cog' | 2
        'bot'       | ["hat", "cot", "cog"] as String[]  | 'cog' |  3
        'hit'       | ["hot", "dot", "dog", "lot", "log", "cog"] as String[]  | 'cog' | 5
        'lost' |      ["most","fist", "cost","fish"]  as String[] | 'cost' | 2
         "hot" | ["hot",
                   "cog",
                   "dog",
                   "tot",
                   "hog",
                   "hop",
                   "pot",
                   "dot"] as String[] | "dog"  | 3

    }



}