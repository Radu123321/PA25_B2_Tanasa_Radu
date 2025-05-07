// Excepție personalizată folosită atunci când nu se găsește o imagine în repository
//Exceptia mea
public class ImageNotFoundException extends Exception {
    public ImageNotFoundException(String message) {
        super(message); // Apelează constructorul clasei Exception cu mesajul primit
    }
}