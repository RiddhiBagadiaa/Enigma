package enigma;

import java.util.Collection;
import java.util.HashMap;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Riddhi Bagadiaa
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        /* fix it */
        _alphabet = alpha;
        _numRotors = numRotors;
        _numPawls = pawls;
        _allRotors = allRotors;
        _rotorsByPosition = new HashMap<>();
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        /* fix it */
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        /* fix it */
        return _numPawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        /* fix it */
        for (int i = 0; i < rotors.length; i++) {
            for (Rotor r : _allRotors) {
                if (rotors[i].equals(r.name())) {
                    if (_rotorsByPosition.containsValue(r)) {
                        throw new EnigmaException(
                                "Rotor already exists in the machine");
                    }
                    _rotorsByPosition.put(i, r);
                    if (!_rotorsByPosition.get(0).reflecting()) {
                        throw new
                                EnigmaException(
                                        "The first rotor is not a reflector");
                    }
                    int numFixedRotors = numRotors() - numPawls();
                    if (i >= numFixedRotors
                            && !_rotorsByPosition.get(i).rotates()) {
                        throw new EnigmaException("Fixed rotor"
                                + " instead of a moving rotor was added.");
                    } else if (i < numFixedRotors
                            && _rotorsByPosition.get(i).rotates()) {
                        throw new
                                EnigmaException(
                                "Moving rotor instead of "
                                        + "a fixed rotor was added.");
                    }
                    break;
                }
            }
            if (!_rotorsByPosition.containsKey(i)) {
                throw new EnigmaException("Rotor has been misnamed");
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 0; i < setting.length(); i++) {
            _rotorsByPosition.get(i + 1).set(setting.charAt(i));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        /* fix it */
        plugBoard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        /* fix it */
        advanceRotors();
        c = plugBoard.permute(c);
        int checkRotors = numRotors() - 1;
        while (checkRotors >= 0) {
            c = _rotorsByPosition.get(checkRotors).convertForward(c);
            checkRotors -= 1;
        }
        checkRotors = 1;
        while (checkRotors < numRotors()) {
            c = _rotorsByPosition.get(checkRotors).convertBackward(c);
            checkRotors += 1;
        }

        c = plugBoard.permute(c);
        return c;
    }

    /** Advance the appropriate rotors at the
     * beginning of each letter conversion.
     * Moves the rotor whose pawl is in the notch
     * and the rotor who the notch(alphabet ring) belongs to*/
    private void advanceRotors() {
        assert numRotors() > 1;
        int checkRotor = 1;
        while (checkRotor < numRotors()) {
            if (checkRotor == numRotors() - 1) {
                _rotorsByPosition.get(checkRotor).advance();
                break;
            }
            if (_rotorsByPosition.get(checkRotor).rotates()
                    && _rotorsByPosition.get(checkRotor + 1).atNotch()) {
                _rotorsByPosition.get(checkRotor).advance();
                _rotorsByPosition.get(checkRotor + 1).advance();

                checkRotor = checkRotor + 2;
                while (checkRotor < numRotors() - 1
                        && _rotorsByPosition.get(checkRotor).atNotch()) {
                    _rotorsByPosition.get(checkRotor).advance();
                }
            }

            checkRotor += 1;
        }
    }

    /** Returns the encoding/decoding of MSG, updating the
     * state of the rotors accordingly. */
    String convert(String msg) {
        /* fix it */
        msg = msg.replaceAll(" ", "");
        String encryptedMsg = "";
        for (int i = 0; i < msg.length(); i++) {
            char encryptedLetter = _alphabet.toChar
                    (convert(_alphabet.toInt(msg.charAt(i))));

            encryptedMsg += encryptedLetter;
        }
        return encryptedMsg;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Total number of rotors in the Enigma Machine. */
    private int _numRotors;

    /** Number of moving rotors. */
    private int _numPawls;

    /** Collection of all available rotors. */
    private Collection<Rotor> _allRotors;

    /** Position and Rotors in the Enigma Machine. */
    private HashMap<Integer, Rotor> _rotorsByPosition;

    /** Plugboard. */
    private Permutation plugBoard;

}
