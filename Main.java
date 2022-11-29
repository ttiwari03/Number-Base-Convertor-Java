package converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Scanner;
/*
 *  This program convert number between two base.
 */
public class Main {
    public static final Scanner readIp = new Scanner(System.in);
    public static final int decimalBase = 10;
    public static final String characterRange = "0123456789abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) {

        while (true) {
            System.out.println("Enter two numbers in format: {source base} {target base} (To quit type /exit)");
            String input = readIp.nextLine();
            if (input.equals("/exit")) {
                break;
            }
            int sourceBase = Integer.parseInt(input.split(" ")[0]);
            int targetBase = Integer.parseInt(input.split(" ")[1]);
            convertor(sourceBase, targetBase);
            System.out.println();
        }
    }

    private static void convertor(int sourceBase, int targetBase) {

        while (true) {
            System.out.printf("Enter number in base %d to convert to base %d (To go back type /back)%n", sourceBase, targetBase);

            String response = readIp.nextLine();
            if (response.equals("/back")) {
                break;
            }

            BigInteger integerPartInDecimal;
            String fractionalPart;
            try {
                integerPartInDecimal = new BigInteger(response.split("\\.")[0], sourceBase);

            } catch (Exception e) {
                integerPartInDecimal = BigInteger.ZERO;
            }

            try {
                fractionalPart = response.split("\\.")[1];
            } catch (Exception e) {
                fractionalPart = null;
            }

            String convertedNumber;
            String convertedFraction = "";
            if (fractionalPart != null) {
                if (sourceBase == decimalBase) {
                    convertedFraction = fractionalPart;
                } else {
                    convertedFraction = convertFractionToDecimal(fractionalPart, sourceBase);
                }
                if (targetBase != decimalBase) {
                    convertedFraction = convertDecimalFractionToBase(convertedFraction, targetBase);
                }

                convertedFraction = convertedFraction.contains(".") ? convertedFraction.split("\\.")[1] : convertedFraction;
                int len = convertedFraction.length();

                if (len > 5) {
                    convertedFraction = convertedFraction.substring(0, 5);
                }

                if (len < 5) {
                    convertedFraction = convertedFraction.concat("0".repeat(5 - len));
                }

                convertedFraction = ".".concat(convertedFraction);
            }

            if (targetBase == decimalBase) {
                convertedNumber = integerPartInDecimal + convertedFraction;
            } else {
                convertedNumber = convertDecimalToBase(integerPartInDecimal, BigInteger.valueOf(targetBase)) + convertedFraction;
            }

            System.out.println("Conversion result: " + convertedNumber);
            System.out.println();
        }
    }

    private static String convertFractionToDecimal(String fraction, int sourceBase) {

        BigDecimal tempResult = BigDecimal.ZERO;

        for (int i = 0; i < fraction.length(); i++) {
            int digit = characterRange.indexOf(fraction.charAt(i));
            int power = i + 1;
            tempResult = tempResult.add(BigDecimal.valueOf(digit * Math.pow(sourceBase, -power)));
        }

        MathContext precision = new MathContext(5);
        tempResult = tempResult.round(precision);
        return String.valueOf(tempResult);
    }

    private static String convertDecimalFractionToBase(String fraction, int targetBase) {


        fraction = fraction.contains(".") ? fraction : ".".concat(fraction);

        BigDecimal num = new BigDecimal(fraction);
        int digitCount = 0;
        BigDecimal tempResult;
        int integerPart;
        StringBuilder result = new StringBuilder();

        while (num.compareTo(BigDecimal.ZERO) > 0 && digitCount < 5) {
            tempResult = num.multiply(BigDecimal.valueOf(targetBase));

            integerPart = tempResult.intValue();
            char ch = characterRange.charAt(integerPart);
            result.append(ch);
            num = tempResult.subtract(BigDecimal.valueOf(integerPart));
            digitCount++;
        }

        return result.toString();
    }

    private static String convertDecimalToBase(BigInteger number, BigInteger targetBase) {
        StringBuilder result = new StringBuilder();
        int remainder;

        while (!number.equals(BigInteger.ZERO)) {
            BigInteger[] quotientAndRemainder = number.divideAndRemainder(targetBase);
            number = quotientAndRemainder[0];
            remainder = quotientAndRemainder[1].intValue();
            result.insert(0, characterRange.charAt(remainder));
        }
        return result.toString();
    }
}