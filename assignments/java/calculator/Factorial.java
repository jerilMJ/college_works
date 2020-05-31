package calculator;

public class Factorial {
    long[] memo = new long[21];

    public Long fact(int num) {
        if (num > 20)
            throw new UnsupportedOperationException();

        if (num <= 1)
            return (long) num;

        if (memo[num] == 0)
            memo[num] = fact(num - 1) * num;

        return memo[num];
    }
}