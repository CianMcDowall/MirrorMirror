public class Interpreter {
    public static Weather getWeather(String location) throws Exception
    {
        int locationNumber = -1;

        if(location.trim().equalsIgnoreCase("Home"))
        {
            locationNumber = 0;
        } else if(location.trim().equalsIgnoreCase("University") || location.trim().equalsIgnoreCase("Uni"))
        {
            locationNumber = 1;
        } else if(location.trim().equalsIgnoreCase("Work"))
        {
            locationNumber = 2;
        } else if(location.trim().equalsIgnoreCase("All"))
        {
            return new Weather();
        }

        return new Weather(locationNumber);
    }

    public static Article getNews(String section, String APIKey) throws Exception
    {
        int sectionNumber = -1;
        if(section.trim().equalsIgnoreCase("Aus") || section.trim().equalsIgnoreCase("Australia"))
        {
            sectionNumber = 0;
        } else if(section.trim().equalsIgnoreCase("World"))
        {
            sectionNumber = 1;
        }

        return new Article(sectionNumber, APIKey);
    }
}