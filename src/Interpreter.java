public class Interpreter {
    public static Weather getWeather(String location) throws Exception
    {
        int locationNumber = -1;

        if(location.trim().equalsIgnoreCase("Home"))
        {
            locationNumber = 0;
        } else if(location.equalsIgnoreCase("University") || location.equalsIgnoreCase("Uni"))
        {
            locationNumber = 1;
        } else if(location.equalsIgnoreCase("Work"))
        {
            locationNumber = 2;
        }

        return new Weather(locationNumber);
    }
}