package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import java.util.regex.Pattern;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Riddhi Bagadiaa
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        /* FIXED IT */
        readConfigFile();
        Machine myEnigmaMachine = readConfig();
        String settingLine = _input.nextLine();
        setUp(myEnigmaMachine, settingLine);
        while (_input.hasNextLine()) {
            String nextLine = _input.nextLine();
            if (nextLine.isEmpty()) {
                _output.println();
            } else if (nextLine.charAt(0) == '*') {
                myEnigmaMachine = readConfig();
                setUp(myEnigmaMachine, nextLine);
            } else {
                String encrypted = myEnigmaMachine.convert(nextLine);
                printMessageLine(encrypted);
                _output.println();
            }
        }
    }


    /** Read the ALPHABET, NUMROTORS and NUMPAWLS from _CONFIG. */
    private void readConfigFile() {
        String alphaString = _config.next();
        Pattern alphabetP = Pattern.compile("([!-'+-Za-z]+)");

        if (alphabetP.matcher(alphaString).matches()) {
            _alphabet = new Alphabet(alphaString);

        } else {
            throw new EnigmaException("Alphabet has wrong format");
        }

        String numRotors = _config.next();
        String numPawls = _config.next();
        Pattern numP = Pattern.compile("([1-9]+)");

        if (numP.matcher(numRotors).matches()) {
            _numRotors = Integer.valueOf(numRotors);

        } else {
            throw new EnigmaException("Number of Rotors is wrong");
        }
        if (numP.matcher(numPawls).matches()) {
            _numPawls = Integer.valueOf(numPawls);

        } else {
            throw new EnigmaException("Number of Pawls is wrong");
        }

        if (_numPawls >= _numRotors) {
            throw new EnigmaException("Number"
                    + " of pawls must be greater than number of Rotors");
        }

        getRotors();
    }

    /** Get the ROTORS from CONFIG. */
    private void getRotors() {
        _allRotors = new ArrayList<>();
        String rotorDetails = _config.next();
        _allRotorNames = "";


        while (_config.hasNext()) {
            String rotorName = "";
            char rotorType = ' ';
            String notches = "";
            String cycles = "";


            Pattern nameP = Pattern.compile("([!-'+-Za-z]+)");
            if (nameP.matcher(rotorDetails).matches()) {
                rotorName = rotorDetails;
                _allRotorNames += rotorName + " ";
            }


            rotorDetails = _config.next();
            Pattern settingP1 = Pattern.compile("([RNM][!-'+-Za-z]+)");
            if (settingP1.matcher(rotorDetails).matches()) {

                rotorType = rotorDetails.charAt(0);
                notches = rotorDetails.substring(1);
            }

            Pattern settingP2 = Pattern.compile("([RNM])");
            if (settingP2.matcher(rotorDetails).matches()) {
                rotorType = rotorDetails.charAt(0);
            }

            rotorDetails = _config.next();

            while (rotorDetails.charAt(0) == '('
                    && rotorDetails.charAt(rotorDetails.length() - 1) == ')') {
                cycles += rotorDetails;
                if (_config.hasNext()) {
                    rotorDetails = _config.next();
                } else {
                    break;
                }
            }

            Permutation perm = new Permutation(cycles, _alphabet);
            if (rotorType == 'R') {
                Rotor reflect = new Reflector(rotorName, perm);
                _allRotors.add(reflect);
            } else if (rotorType == 'N') {
                Rotor nonMoving = new FixedRotor(rotorName, perm);
                _allRotors.add(nonMoving);
            } else if (rotorType == 'M') {
                Rotor moving = new MovingRotor(rotorName, perm, notches);
                _allRotors.add(moving);
            }
        }

    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */

    private Machine readConfig() {
        /* FIXED IT */
        try {
            Machine enigma =
                    new Machine(_alphabet, _numRotors, _numPawls, _allRotors);
            return enigma;
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        /* fix it */
        try {
            return null;
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        /* FIXED IT */
        Scanner settingScanner = new Scanner(settings);
        if (settingScanner.hasNext()) {
            if (!settingScanner.next().equals("*")) {
                throw new EnigmaException("The setting line is wrong");
            }
            String[] useRotors = new String[_numRotors];
            int index = 0;
            String rotorName = settingScanner.next();
            while (_allRotorNames.contains(rotorName) && index < _numRotors) {
                useRotors[index] = rotorName;
                index += 1;
                rotorName = settingScanner.next();
            }
            M.insertRotors(useRotors);
            M.setRotors(rotorName);

            String cycles = "";

            while (settingScanner.hasNext()) {
                cycles += settingScanner.next();
            }

            M.setPlugboard(new Permutation(cycles, _alphabet));

        } else {
            throw new EnigmaException("Setting line is empty");
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        /* FIXED IT */
        int count = 1;
        int index = 0;
        int msgLength = msg.length();
        String finalMsg = "";
        while (index < msgLength) {
            while (count < 6 && index < msgLength) {
                finalMsg += msg.charAt(index);
                count += 1;
                index += 1;
            }
            finalMsg += " ";
            count = 1;
        }

        finalMsg = finalMsg.trim();
        _output.print(finalMsg);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** Number of rotors in the Enigma Machine. */
    private int _numRotors;

    /** Number of pawls in the Enigma Machine. */
    private int _numPawls;

    /** Collection of all rotors that are configured. */
    private ArrayList<Rotor> _allRotors;

    /** Name of all rotors in _ALLROTORS. */
    private String _allRotorNames;

}
