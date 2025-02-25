//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World");
        String[] languages = {"C", "C++", "C#", "Python", "Go", "Rust", "JavaScript", "PHP", "Swift", "Java"};
        int n = (int)(Math.random() * 1_000_000);
        System.out.println(n);
        n *= 3;
        n += Convert("10101" , 2);
        n += Convert("FF" , 16);
        System.out.println(n);
        n *= 6;
        /*
          Suma cifrelor reprezinta un ciclu de lungime 9
          De ex:15->6, 15%9 = 6, Cand modulo este 0, inseamna ca suma cifrelor este 9:27->9, 27 % 9 = 0
         */
        n %= 9;
        if(n == 0)
            n = 9;
        System.out.println("Willy-nilly, this semester I will learn " + languages[n]);
    }
    /*
     Functie care face convert in baza 10 de la orice baza.
     */
    public  static int Convert(String val , int baza)
    {
        int ans = 1;
        /*
          Plecam de la bitul non- semnificativ(ultimul), si il transformam transformam numarul in baza 10:
          Ex:10101 -> 1 * 2^0 + 0 * 2 ^ 1 + 1 * 2 ^ 2 + 0 * 2 ^ 3 + 1 * 2 ^ 4
         */
        for(int i = val.length() - 1 ; i >= 0 ; i--)
        {
            ans *= baza;
            Character.getNumericValue(val.charAt(i));
        }
        return ans;
    }
}