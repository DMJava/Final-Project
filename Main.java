package Final_project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {

        Controller controller = new Controller();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        first:
        while (true) {
            System.out.println("(1) - search rooms, (2) - search hotels by name, 3 - search hotel by city, (0) - cancel operation");
            String step1 = bufferedReader.readLine();
            if (step1.equals("0")) break;

            //search hotel by city
            if (step1.equals("3")) {
                while (true) {
                    System.out.println("Enter name of city, or part of name; " +
                            "get all list  - just press Enter; " +
                            "9- previous menu; 0- finish work");
                    String step2 = bufferedReader.readLine();
                    if (step2.equals("0")) break first;
                    if (step2.equals("9")) continue first;
                    controller.findHotelByCity(step2).forEach(System.out::println);
                    System.out.println("Do you want to continue the search?");
                }
            }
            //search hotels by name
            if (step1.equals("2")) {
                while (true) {
                    System.out.println("Enter the name of hotel, or part of name;" +
                            " get all list - just press Enter; " +
                            "9- previous menu; 0- finish work");
                    String step3 = bufferedReader.readLine();
                    if (step3.equals("0")) break first;
                    if (step3.equals("9")) continue first;
                    controller.findHotelByName(step3.trim()).forEach(System.out::println);
                    System.out.println("Do you want to continue the search?");
                }
            }
        }
    }
}
