package enigma;

import static enigma.EnigmaException.*;

import java.util.ArrayList;
import java.util.HashMap;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Riddhi Bagadiaa
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cyclePerm = cycles.trim();
        addCycles();
        checkFormatAddCycles();
        _size = alphabet.size();
    }

    /**Add the cycle c0->c1->...->cm->c0 to CYCLES, where CYCLE is
     *  c0c1...cm. */
    private void addCycles() {
        for (int i = 0; i < alphabet().size(); i++) {
            if (!_cyclePerm.contains(Character.toString(_alphabet.toChar(i)))) {
                _cyclePerm += "(" + alphabet().toChar(i) + ")";
            }
        }
    }

    /** CHeck the structure of the input and add the cycle
     * c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void checkFormatAddCycles() {
        _cyclePerm = _cyclePerm.replaceAll(" ", "");
        allCycles = new HashMap<>();
        boolean hasOpenedParenthesis = false;
        int key = 0;

        for (int i = 0; i < _cyclePerm.length(); i++) {
            if (_cyclePerm.charAt(i) == '(' && !hasOpenedParenthesis) {
                hasOpenedParenthesis = true;
                key += 1;
                ArrayList<Character> val = new ArrayList<>();
                allCycles.put(key, val);
            } else if (_cyclePerm.charAt(i)
                    == '(' && hasOpenedParenthesis) {
                throw new EnigmaException("Malformed "
                        + "cycle, extra open parenthesis");
            } else if (_cyclePerm.charAt(i) == ')' && hasOpenedParenthesis) {
                hasOpenedParenthesis = false;
            } else if (_cyclePerm.charAt(i)
                    == ')' && !hasOpenedParenthesis) {
                throw new EnigmaException("Malformed cycle,"
                        + " extra closed parenthesis");
            } else if (((_cyclePerm.charAt(i)
                    < '(' && _cyclePerm.charAt(i) >= '!')
                    || (_cyclePerm.charAt(i)
                    <= 'z' && _cyclePerm.charAt(i) >= 'a')
                    || (_cyclePerm.charAt(i)
                    <= 'Z' && _cyclePerm.charAt(i) >= '+'))
                    && _alphabet.contains(_cyclePerm.charAt(i))) {
                allCycles.get(key).add(_cyclePerm.charAt(i));
            } else {
                throw new EnigmaException("Unidentified character "
                        + _cyclePerm.charAt(i));
            }
        }
        mapLetters();
    }

    /** Adds the letters and their mapping to the Hashmap. */
    private void mapLetters() {
        charMapping = new HashMap<>();
        for (Integer key : allCycles.keySet()) {
            ArrayList<Character> oneCycle = allCycles.get(key);
            for (char character : oneCycle) {
                char[] invforw = new char[2];
                int len = oneCycle.size();
                int nextInd  = myWrap(oneCycle.indexOf(character) + 1, len);
                int prevInd  = myWrap(oneCycle.indexOf(character) - 1, len);
                invforw[0] = oneCycle.get(prevInd);
                invforw[1] = oneCycle.get(nextInd);
                charMapping.put(character, invforw);
            }
        }
    }

    /** Return the value of INDEX modulo LEN. */
    private int myWrap(int index, int len) {
        int r = index % len;
        if (r < 0) {
            r += len;
        }
        return r;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        /* fixed it */
        return _size;
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        /* fixed it */
        p = wrap(p);
        char pChar = alphabet().toChar(p);
        char mapTo = charMapping.get(pChar)[1];
        return alphabet().toInt(mapTo);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        /* fixed it */
        c = wrap(c);
        char cChar = alphabet().toChar(c);
        char mapTo = charMapping.get(cChar)[0];
        return alphabet().toInt(mapTo);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        /* fixed it */
        char mapTo = charMapping.get(p)[1];
        return mapTo;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        /* fixed it */
        char mapTo = charMapping.get(c)[0];
        return mapTo;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        /* fixed it */
        for (Integer key : allCycles.keySet()) {
            if (allCycles.get(key).size() < 2) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** String of cycles of this permutation. */
    private String _cyclePerm;

    /** Size of it ALPHABET. */
    private int _size;

    /** Mapping each character to its forward and inverse. */
    private HashMap<Character, char[]> charMapping;

    /** Cycles in the permutation. */
    private HashMap<Integer, ArrayList<Character>> allCycles;
    /* fixed it */

}
