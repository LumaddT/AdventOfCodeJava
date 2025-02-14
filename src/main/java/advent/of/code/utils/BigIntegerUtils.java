package advent.of.code.utils;

import java.math.BigInteger;

public class BigIntegerUtils {
    public static BigInteger modularInverse(BigInteger value, BigInteger modulo) {
        return value.modPow(modulo.subtract(BigInteger.TWO), modulo);
    }

    /**
     * It trusts `value` and `modulo` are co-primes. It does not check.
     */
    public static BigInteger modularDivision(BigInteger numerator, BigInteger denominator, BigInteger modulo) {
        return numerator.multiply(modularInverse(denominator, modulo)).mod(modulo);
    }
}
