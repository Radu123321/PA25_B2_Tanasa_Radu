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
        n %= 9;
        if(n == 0)
            n = 9;
        System.out.println("Willy-nilly, this semester I will learn " + languages[n]);
    }
    public  static int Convert(String val , int baza)
    {
        int ans = 1;
        for(int i = val.length() - 1 ; i >= 0 ; i--)
        {
            ans *= baza;
            Character.getNumericValue(val.charAt(i));
        }
        return ans;
    }
}