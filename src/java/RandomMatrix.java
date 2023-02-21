import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class RandomMatrix {

    private ArrayList<Integer> order;
    private double precision = -1;
    private int size = 0;
    private ArrayList<ArrayList<Double>> two_dim_list = new ArrayList<>();

    public void orderInit() {
        order = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            order.add(i);
        }
        Collections.shuffle(order);
    }

    public void generateMatrix() {
        readConsoleData();
        orderInit();
        //System.out.println("order: " + order);
        for (int i = 0; i < size; i++) {
            two_dim_list.add(generateRow(i));
        }
        //System.out.println(two_dim_list);
        fileWrite();
    }

    private void fileWrite() {
        try (FileWriter fw =
                     new FileWriter("C:\\Users\\User\\IdeaProjects\\Math1\\src\\resourses\\input.txt")) {
            fw.write(size + "\n");
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size + 1; j++) {
                    fw.write(two_dim_list.get(i).get(j) + " ");
                }
                fw.write("\n");
            }
            fw.write(precision + "");
        } catch (IOException e) {
            System.out.println("Не могу записать в файл");
            System.exit(-1);
        }
    }

    public void readConsoleData() {
        //System.out.println();
        while (size == 0) {
            try {

                size = new Scanner(System.in).nextInt();

                if (size > 20) {
                    size = 0;
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("Введите корректное число, не большее 20");
            }
        }

        System.out.println("Введите желаемую точность");
        while (precision == -1) {
            try {
                precision = new Scanner(System.in).nextDouble();
            } catch (Exception e) {
                System.out.println("Введите корректное число");
            }
        }
    }

    private ArrayList<Double> generateRow(int index) {
        ArrayList<Double> list = new ArrayList<>(size);
        Double sum = 0d;
        for (int i = 0; i < size + 1; i++) {

            Double random_number = (double) (int) (Math.random() * 10);
            sum += random_number;
            list.add(random_number);
        }
        list.set(order.get(index), (double) (int) (Math.random()*5 + sum + 1) );
        //System.out.println(list);
        return list;
    }

}
