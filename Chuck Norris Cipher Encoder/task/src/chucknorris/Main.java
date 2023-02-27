package chucknorris;

import java.util.Scanner;

class EncodedInputHasInvalidCharacters extends Exception {}
class EncodedInputHasInvalidBlockSequenceStart extends Exception {}

class EncodedInputHasOddBlockCount extends Exception {}

class DecodedBinaryStringNotMultipleOfSeven extends Exception {}

public class Main {
    public static void main(String[] args) throws EncodedInputHasOddBlockCount, EncodedInputHasInvalidCharacters, EncodedInputHasInvalidBlockSequenceStart, DecodedBinaryStringNotMultipleOfSeven {
        Scanner s = new Scanner(System.in);

        boolean isRunning = true;

        while (isRunning) {
            System.out.println("Please input operation (encode/decode/exit): ");
            String option = s.nextLine();
            switch (option) {
                case "exit" -> {
                    isRunning = false;
                    System.out.println("Bye!");
                }
                case "encode" -> {
                    System.out.println("Input string:");
                    String encoded = s.nextLine();
                    System.out.println("Encoded string:");
                    System.out.println(encrypt(encoded));
                    System.out.println();
                }
                case "decode" -> {
                    System.out.println("Input encoded string:");
                    String decoded = s.nextLine();
                    String result = "";

                    try {
                        result = decrypt(decoded);
                    } catch (Exception e) {
                        System.out.println("Encoded string is not valid.");
                        // System.out.println(e.getClass().getSimpleName()); // show errors
                        System.out.println();
                        continue;
                    }

                    System.out.println("Decoded string:");
                    System.out.println(result);
                    System.out.println();
                }
                default -> {
                    System.out.println("There is no '" + option + "'" + " operation");
                    System.out.println();
                }
            }
        }
    }

    /* Takes the input and decrypts it from Chuck Norris Cipher */
    public static String decrypt(String input)
            throws EncodedInputHasInvalidBlockSequenceStart,
            EncodedInputHasInvalidCharacters,
            EncodedInputHasOddBlockCount,
            DecodedBinaryStringNotMultipleOfSeven
    {
        StringBuilder result = new StringBuilder();
        StringBuilder work = new StringBuilder();
        int bitCounter = 0;

        String [] temp = input.split(" "); // split to tokens

        if (temp.length % 2 != 0) {
            // not enough blocks to be right
            throw new EncodedInputHasOddBlockCount();
        }

        for (int i = 0; i < temp.length; i += 2) {
            // we read them two at a time, and append them to bits.
            // when bitCounter = 7 we actually change it into a value with parseInt

            if (temp[i + 1].matches("^0+$")) {
                bitCounter += temp[i + 1].length();
            } else {
                throw new EncodedInputHasInvalidCharacters();
            }

            work.append(switch (temp[i]) {
                case "00" -> "0".repeat(temp[i + 1].length());
                case "0" -> "1".repeat(temp[i + 1].length());
                default -> throw new EncodedInputHasInvalidBlockSequenceStart();
            });

            if (bitCounter >= 7) {
                bitCounter -= 7;
                result.append((char) Integer.parseInt(work.substring(0, 7), 2));
                work = new StringBuilder(work.substring(7)); // cut off the part we already used.
            }
        }

        if (bitCounter != 0) {
            // there were leftover bits, obviously something
            // did not work
            throw new DecodedBinaryStringNotMultipleOfSeven();
        }

        return result.toString();
    }

    public static String encrypt(String input) {
        String output = "";
        String bitmap = "0".repeat(7); // this lets us pad the output with 0's
        StringBuilder work = new StringBuilder();

        for (char c : input.toCharArray()) {
            // create zero padded null string
            work.append(
                    bitmap.substring(
                            Integer.toBinaryString(c).length()
                    )
            ).append(
                    Integer.toBinaryString(c)
            );
        }
        char previousBit = 'Z'; // just something impossible
        String firstTime = "";  // first run we don't want
                                // spaces

        // takes the binary representation of the string input
        // and encodes it to Chuck Norris Cipher
        for (char bit : work.toString().toCharArray()) {
            switch (bit) {
                case '0' -> {
                    if (previousBit != bit) {
                        output = output.concat(firstTime).concat("00 0");
                    } else {
                        output = output.concat("0");
                    }
                    previousBit = bit;
                }
                case '1' -> {
                    if (previousBit != bit) {
                        output = output.concat(firstTime).concat("0 0");
                    } else {
                        output = output.concat("0");
                    }
                    previousBit = bit;
                }
            }
            firstTime = " ";
        }

        return output;
    }
}