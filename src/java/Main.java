import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static int size = 0;
    public static double precision = -1;
    public static ArrayList<ArrayList<Double>> list_two_dim_init;
    public static ArrayList<ArrayList<Double>> sanitized_two_dim_list_c;
    public static ArrayList<Double> sanitized_d;

    /*мэйн задается через конструктор, где должны быть проинициализированны три поля точность размер и начальный массив
    * причем инициализирует мэйн ридер - а какой - не важно либо с клавы либо с файла интерфейс*/

    //TODO  интерактивное предложение о том, файл: (рандом или прочитать файл) || клавиатура


    public static void main(String[] args) throws IOException{
        //new RandomMatrix().generateMatrix();
        //readFromConsole();
        //readFromFile();
        doSwitch();
        list_two_dim_init = reorder();
        sanitizeList();
        //System.out.println(list_two_dim_init);
        //System.out.println(sanitized_two_dim_list_c);
        //System.out.println(sanitized_d);
        iterate();

    }

    public static double measureBiggestError(List<Double> first, List<Double> second, List<Double> list_of_errors){
        list_of_errors.clear();
        double current_error;
        double biggest_error = 0;
        for (int i=0;i<size; i++){
            current_error = Math.abs(first.get(i) - second.get(i));
            list_of_errors.add(current_error);
            if (biggest_error < current_error){
                biggest_error = current_error;
            }
        }
        return biggest_error;
    }

    public static void calculate(List<Double> current, List<Double> previous){
        current.clear();
        for (int i=0; i<size;i++){
            double sum = 0;
            for (int j=0;j<size; j++) {
                sum += sanitized_two_dim_list_c.get(i).get(j) * previous.get(j);
            }
            sum+= sanitized_d.get(i);
            current.add(sum);
        }
    }

    public static void iterate(){
        List<Double> list_of_errors = new ArrayList<>(size);

        List<Double> previous_answer = new ArrayList<>(size);
        List<Double> current_answer = new ArrayList<>(size);
        fillZeros(previous_answer);
        fillZeros(current_answer);
        Collections.copy(current_answer,sanitized_d);

        System.out.println(0 + "|               x:     "+current_answer);
        int counter = 1;
        double BiggestError  =0;
        while((BiggestError = measureBiggestError(current_answer, previous_answer, list_of_errors))>=precision){
            Collections.copy(previous_answer, current_answer);
            calculate(current_answer, previous_answer);

            System.out.println("вектор погрешности:     "+ list_of_errors);
            //System.out.println("погрешность:    " + BiggestError);

            System.out.println();
            System.out.println("_______________________________________________________");
            System.out.println(counter+ "|               x:     "+current_answer);

            counter++;
            /*
            if (counter > 30) {
                System.exit(-1);
            }
               */

        }
        System.out.println("вектор погрешности:     "+ list_of_errors);
    }

    private static void fillZeros(List<Double> list){
        for (int i =0; i< size; i++){
            list.add(0d);
        }
    }

    public static void sanitizeList() {
        sanitized_two_dim_list_c = new ArrayList<>(size);
        sanitized_d = new ArrayList<>(size);
        ArrayList<Double> list_one_dim = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            double divider = list_two_dim_init.get(i).get(i);
            sanitized_d.add(list_two_dim_init.get(i).get(size) / divider);
            for (int j = 0; j < size; j++) {
                if (i==j){
                    list_one_dim.add(0d);
                } else{
                    list_one_dim.add(-list_two_dim_init.get(i).get(j)/ divider);
                }
            }
            sanitized_two_dim_list_c.add((ArrayList<Double>) list_one_dim.clone());
            list_one_dim.clear();
        }
        checkListC();
    }

    public static void checkListC(){

        double max = 0;
        for (int i =0; i< size; i++){
            double sum = 0;
            for (int j=0; j< size; j++){
                sum+=Math.abs(sanitized_two_dim_list_c.get(i).get(j));
            }
            if (sum > max){
                max = sum;
            }
        }
        if (max >=1) {
            System.out.println("Норма преобразованной матрицы >= 1, а значит условие сходимости не выполнено");
            System.exit(-1);
        }
    }


    public static void readFromFile() {
        try (FileReader fr = new FileReader("C:\\Users\\User\\IdeaProjects\\Math1\\src\\resourses\\input.txt");
             Scanner scan = new Scanner(fr)) {
            scan.useLocale(Locale.US);
            int n = scan.nextInt();
            list_two_dim_init = new ArrayList<>(n+1);
            ArrayList<Double> list_one_dim = new ArrayList<>(n);

            //scan.hasNextDouble()
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n + 1; j++) {
                    list_one_dim.add(scan.nextDouble());
                }
                list_two_dim_init.add((ArrayList<Double>) list_one_dim.clone());
                list_one_dim.clear();
            }
            precision = scan.nextDouble();
            size = n;

        } catch (Exception e) {
            System.out.println("Не корректный файл: либо недостает элементов (особенно точности в конце), либо присутствуют нечисловые символы");
            System.exit(-1);
        }
    }

    public static void readFromConsole(){
        try (Scanner scan = new Scanner(System.in)) {
            scan.useLocale(Locale.US);
            System.out.println("Введите размерность матрицы");
            while (size == 0) {
                try {
                    //size = scan.nextInt();
                    System.out.println(size);
                    size = Integer.parseInt(scan.nextLine());
                    if (size > 20) {
                        size = 0;
                        throw new Exception();
                    }
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage()+ e.getMessage());
                    System.out.println("Введите корректное число, не большее 20");
                }
            }

            list_two_dim_init = new ArrayList<>(size+1);
            ArrayList<Double> list_one_dim = new ArrayList<>(size);

            System.out.println("Введите массив");
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size + 1; j++) {
                    list_one_dim.add(scan.nextDouble());
                }
                list_two_dim_init.add((ArrayList<Double>) list_one_dim.clone());
                list_one_dim.clear();
            }

            System.out.println("Введите желаемую точность");
            while (precision == -1) {
                try {
                    precision = new Scanner(System.in).nextDouble();
                } catch (Exception e) {
                    System.out.println("Введите корректное число");
                }
            }

        } catch (Exception e) {
            //System.out.println("");
            System.out.println("Вводите данные корретно");
            System.exit(-1);
        }
    }



    static class Order implements Comparable<Order>{
        public int number_of_a_row;
        public int index_of_max_value;

        public Order(int number_of_a_row, int index_of_max_value){
            this.index_of_max_value = index_of_max_value;
            this.number_of_a_row = number_of_a_row;
        }

        @Override
        public int compareTo(Order order) {
            return this.index_of_max_value - order.index_of_max_value;
        }
    }

    private static int max(ArrayList<Double> list){
        double previous_max = 0;
        double max = list.get(0);
        int position = 0;
        for (int i=1; i< size; i++){
            if (max <= list.get(i)){
                previous_max = max;
                max = list.get(i);
                position = i;
            }
        }
        if (max == previous_max) {
            System.out.println("Невозможность дстижения диагонального пребладания:  В строке матрицы нет абсолютно наибольшего числа");
            System.exit(-1);
        }

        return position;
    }

    public static ArrayList<ArrayList<Double>> reorder(){
        ArrayList<Order> list_of_order = new ArrayList<>(size);
        for (int i=0; i<size; i++){
            Order order = new Order(i,max(list_two_dim_init.get(i)));
            list_of_order.add(order);
        }

        ArrayList<ArrayList<Double>> new_two_dim_list = new ArrayList<>(size);

        Collections.sort(list_of_order);
        for (int i=0; i<size; i++){
            if (i!=size-1 &&  list_of_order.get(i).index_of_max_value == list_of_order.get(i+1).index_of_max_value ){
                System.out.println("Невозможность дстижения диагонального пребладания:  Матрицу никак не переставить, нужно ввести другую. (В матрице позиции столбцов наибольших членов повторяются)");
                System.exit(-1);
            }
            new_two_dim_list.add(list_two_dim_init.get(list_of_order.get(i).number_of_a_row));
        }
        return new_two_dim_list;
    }


    public static void doSwitch() throws IOException {
        System.out.println("Введите цифру, чтоб определить что вы собираетесь сделать");
        System.out.println("Если вы хотите ввести запрос с клавиатуры, введите 1");
        System.out.println("Если вы хотите решить матрицу в файле, введите 2");
        System.out.println("Если вы хотите сгенировать рандомную матрицу в файл и посчитать ее введите 3");
        int query = 0;

        while(query == 0) {
            /*
            try (Scanner scanner = new Scanner(System.in);) {
                query = Integer.parseInt(scanner.nextLine());
            } catch (Exception e){
                System.out.println("Введите один из вариантов выше");
            }

             */

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            query  = Integer.parseInt(reader.readLine());
            reader.close();
        }

        if (query ==1){
            readFromConsole();
        } else if (query == 2){
            readFromFile();
        } else if (query ==3){
            new RandomMatrix().generateMatrix();
            readFromFile();
        }
    }
}