public class HomeWork {
    public static void main(String[] args){
        int x = 10;
        int y = 5;

        //Сложение
        int addition = x + y;
        System.out.println("Result addition " + addition);

        //Вычитание
        int subtraction = x - y;
        System.out.println("Result subtraction " + subtraction);

        //Умножение
        int multiplication = x * y;
        System.out.println("Result multiplication " + multiplication);

        //Деление целое
        int division = x / y;
        System.out.println("Result division " + division);

        //Деление с остатком (Остаток будет равен 0)
        int divisionWitchRemaider = x % y;
        System.out.println("Result divisionWitchRemaider " + divisionWitchRemaider);

        //X больше Y?
        boolean checkX = x > y;
        System.out.println("X > Y ? " + checkX);

        //X меньше Y?
        boolean checkY = x < y;
        System.out.println("X < Y ? " + checkY);

        //X равен Y?
        boolean checkEqual = x == y;
        System.out.println("X = Y ? " + checkEqual);

        //добиться переполнения при вычисления
         byte a = 10;
         byte b = 20;
         byte s = a * b; //Результат ошибка: possible lossy conversion from int to byte.
         System.out.print("Result overflow " + s);

        //Комбинация разных типов данных. Выше приведены уже string + integer. Здесь будет комбинация integer and double.
        double d = 1.5;
        int i = 2;
        System.out.println("Result combination double and integer " + (d + i));
        System.out.println("Result combination double and integer " + d + i); //Результат не будет равен примеру выше. Из-за отсутствия скобок



    }
}
