import spock.lang.Specification
import spock.lang.Unroll


/**
 * Created by SSI.
 */
class TransformationTest extends Specification {

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
        'lost' |     ["most","fist","lost", "cost","fish","fist","lost", "cost","fish","fist","lost", "cost","fish"]  as String[] | 'cost' | 2

    }



}