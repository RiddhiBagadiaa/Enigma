package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Riddhi Bagadiaa
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        /* fix it */
        super(name, perm);
    }

    /* fix it */
    @Override
    /** Return true iff I reflect. */
    boolean reflecting() {
        return true;
    }

    @Override
    /** Cant be inverted */
    int convertBackward(int e) {
        /* FIXED IT */
        throw new EnigmaException("Reflector doesnt have an inverse");
    }

    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("reflector has only one position");
        }
    }
}
