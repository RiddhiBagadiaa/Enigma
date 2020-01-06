package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Riddhi Bagadiaa
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        /* FIXED IT */
        _characters = chars;
        _size = _characters.length();
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        /* FIXED IT */
        return _size;
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        /* FIXED IT */
        CharSequence chSeq = Character.toString(ch);
        return _characters.contains(chSeq);
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        /* FIXED IT */
        return (_characters.charAt(index));
    }

    /** Returns the index of character CH, which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        /* FIXED IT */
        return _characters.indexOf(ch);
    }

    /** Size of the ALPHABET. */
    private int _size;

    /** All the CHARACTERS in ALPHABET. */
    private String _characters;

}
