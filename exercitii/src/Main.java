class LearningLanguages {
    public static void main(String[] args) {
        /// 1
        System.out.println("Hello World!");

        /// 2
        String[] languages = {"C", "C++", "C#", "Python", "Go", "Rust", "JavaScript", "PHP", "Swift", "Java"};

        /// 3
        int n = (int) (Math.random() * 1_000_000);
        n *= 3;
        n += 0b10101;
        n += 0xFF;

        int result = sumDigits(n);
        while (result >= 10) {
            result = sumDigits(result);
        }
        System.out.println(result);

        /// 4
        System.out.println("Willy-nilly, this semester I will learn " + languages[result]);
    }

    // Method to compute the sum of digits of a number
    private static int sumDigits(int num) {
        int sum = 0;
        while (num > 0) {
            sum += num % 10;
            num /= 10;
        }
        return sum;
    }
}
