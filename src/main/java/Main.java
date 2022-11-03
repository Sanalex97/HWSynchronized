import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static int maxCount = 1;
    public static int maxFrequency = 0;

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                String route = generateRoute("RLRFR", 100);

                Integer frequency = characterCount('R', route);
                Integer count;

                synchronized (sizeToFreq) {
                    try {
                        count = sizeToFreq.get(frequency);
                        count += 1;
                        sizeToFreq.put(frequency, count);

                        if (count > maxCount) {
                            maxCount = count;
                            maxFrequency = frequency;
                        }
                    } catch (NullPointerException e) {
                        sizeToFreq.put(frequency, 1);
                    }
                }
            }).start();
        }

        System.out.println("Самое частое количество повторений " + maxFrequency + " (встретилось " + maxCount + " раз)");
        System.out.println("Другие размеры:");
        for (int key : sizeToFreq.keySet()) {
            if (key != maxFrequency) {
                System.out.println("- " + key + " (" + sizeToFreq.get(key) + " раз)");
            }
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int characterCount(char c, String route) {
        int count = 0;
        for (int i = 0; i < route.length(); i++) {
            if (route.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }
}