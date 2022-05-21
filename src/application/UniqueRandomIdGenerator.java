package application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class UniqueRandomIdGenerator {

    /**
     * Letter 'O' and number 0 didn't take into account to avoid misleading interpretation
     */
    private static char LETTER[] = {
            'A', '1', 'B', '2', 'C', '3', 'D', '4', 'E', '5', 'F', '6', 'G', '7', 'H', '8', 'I', '9', 'J', 'K', 'L',
            'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    /**
     * prime numbers are for mod(%) operation
     * prime number must not be greater than (LETTER.length + 1)
     */
    private static List<Integer> PRIME_LIST = makePrimList(11, LETTER.length);
    private static int START_POS = 0;

    public static String getUniqueRandomId() {
        StringBuilder uniqueRandomId = new StringBuilder();

        LocalDateTime localDateTime = LocalDateTime.now();

        int year = localDateTime.getYear();
        int month = localDateTime.getMonthValue();
        int day = localDateTime.getDayOfMonth();
        int hour = localDateTime.getHour();
        int minute = localDateTime.getMinute();
        int sec = localDateTime.getSecond();
        int nanoSec = localDateTime.getNano();

        uniqueRandomId.append(LETTER[year % getRandomPrime()])
                .append(LETTER[month])
                .append(LETTER[day % getRandomPrime()])
                .append(LETTER[hour % getRandomPrime()])
                .append(LETTER[minute < LETTER.length ? minute : minute % getRandomPrime()])
                .append(LETTER[sec < LETTER.length ? sec : sec % getRandomPrime()])
                .append(LETTER[nanoSec % getRandomPrime()]);

        return uniqueRandomId.toString();
    }

  
    private static int getPrimeNumber(int origin, int bound) {
        int position = new Random().ints(1, origin, bound).findFirst().getAsInt();

        return PRIME_LIST.get(position);
    }

    /**
     * To mod(%) with different prime number
     * This will add more randomness
     *
     * @return a random prime number from PRIME_LIST
     */
    private static int getRandomPrime() {
        return getPrimeNumber(START_POS, PRIME_LIST.size());
    }

    private static List<Integer> makePrimList(int fromNum, int toNum) {
        List<Integer> primeNumList = new ArrayList<Integer>();

        IntStream.rangeClosed(fromNum, toNum)
                .filter(i -> IntStream.rangeClosed(2, (int) Math.sqrt(i))
                        .allMatch(j -> i % j != 0))
                .forEach(n -> {
                    primeNumList.add(n);
                });

        return primeNumList;
    }

    public enum Type {
        PREFIX,
        SUFFIX,
    }
}
