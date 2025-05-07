
class TrafficLight
{
    private String color;
    public TrafficLight(String color)
    {
        this.color = color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public String getColor() {
        return color;
    }
}
public class Main {
    public static void main(String[] args)
    {
        TrafficLight t = new TrafficLight("red");
        TrafficLight t2 = new TrafficLight("green");
        TrafficLight t3 = new TrafficLight("blue");
        t.setColor("green");
        System.out.println(t.getColor());
    }
}