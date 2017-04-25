package balda;

/**
 * Created by dstepule on 17.04.2017.
 */
public class SwitchDemo {
    public static void main(String[] args) {
        String s = "one";
        // Начиная с Java 7
        switch (s) {
            case "one":
                System.out.println("Something...");
                break;
            case "two":
                System.out.println(";)");
                break;
        }
    }
}
