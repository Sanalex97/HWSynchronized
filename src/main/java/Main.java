import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static int maxCount = 1;
    public static int maxFrequency = 0;

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        Runnable runnable2 = () -> {
            while (!Thread.interrupted()) {
                synchronized (sizeToFreq) {
                    if (sizeToFreq.isEmpty()) {
                        try {
                            sizeToFreq.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    for (Integer key : sizeToFreq.keySet()) {

                        int val = sizeToFreq.get(key);

                        if (val > maxCount) {
                            maxCount = val;
                            maxFrequency = key;
                        }
                    }
                    System.out.println("Самое частое количество повторений " + maxFrequency + " (встретилось " + maxCount + " раз)");
                }
            }
        };
        Thread thread2 = new Thread(runnable2);
        thread2.start();

            Runnable runnable = () -> {
                String route = generateRoute("RLRFR", 100);

                Integer frequency = characterCount('R', route);
                Integer count;

                synchronized (sizeToFreq) {
                    try {
                        count = sizeToFreq.get(frequency);
                        count += 1;
                        sizeToFreq.put(frequency, count);
                    } catch (NullPointerException e) {
                        sizeToFreq.put(frequency, 1);
                    }
                    sizeToFreq.notify();
                }
            };

        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(runnable);
            thread.start();
            threads.add(thread);
        }

        for (Thread thread1 : threads) {
            thread1.join();
        }

        thread2.interrupt();
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