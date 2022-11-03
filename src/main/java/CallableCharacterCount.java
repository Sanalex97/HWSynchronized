import java.util.concurrent.Callable;

public class CallableCharacterCount implements Callable {
    private String route;
    private char c;

    public CallableCharacterCount(String route, char c) {
        this.route = route;
        this.c = c;
    }


    @Override
    public Object call() throws Exception {
        int count = 0;
        for (int i = 0; i < route.length(); i++) {
            if (route.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }
}
