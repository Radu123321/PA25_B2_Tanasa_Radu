// Interfață pentru toate comenzile din shell
// Fiecare comandă (add, remove, update etc.) trebuie să implementeze această interfață
public interface Command
{
    void execute(String[] args, ImageRepository repository) throws Exception;
}