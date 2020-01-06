package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Riddhi Bagadiaa
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        /* fix it */
        super(name, perm);
        _notches = notches;
        for (int i = 0; i < _notches.length(); i++) {
            if (!alphabet().contains(_notches.charAt(i))) {
                throw new EnigmaException("Notch doesnt exist");
            }
        }
    }

    /* fix it */
    @Override
    boolean atNotch() {
        String currChar = Character.toString(alphabet().toChar(setting()));
        if (_notches.contains(currChar)) {
            return true;
        }
        return false;
    }

    @Override
    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int toContact = permutation().wrap(p + setting());
        int changeContact = permutation().permute(toContact);
        int fromContact = permutation().wrap(changeContact - setting());
        return fromContact;
    }

    @Override
    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int toContact = permutation().wrap(e + setting());
        int changeContact = permutation().invert(toContact);
        int fromContact = permutation().wrap(changeContact - setting());
        return fromContact;
    }

    @Override
    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return true;
    }

    @Override
    void advance() {
        /* fix it */
        set(permutation().wrap(setting() + 1));
    }

    /** Notches of this rotor. */
    private String _notches;

    /* fix it : ADDITIONAL FIELDS HERE, AS NEEDED */

}
