package ml.oscarmorton.pedometro;

public class Walk {
    private String name;
    private String location;
    private int steps;

    public Walk(String name, String location, int steps) {
        this.name = name;
        this.location = location;
        this.steps = steps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
}
