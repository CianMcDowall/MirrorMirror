public class KeyGet {
    private static String weatherBaseURL = "https://api.open-meteo.com/v1/forecast?";
    private static String newsBaseURL = "https://newsapi.org/v2/";

    public static String buildWeatherURL()
    {
        double[] home = {-37.75181, 144.96012};
        double[] university = {-37.72062, 145.04718};
        double[] work = {-37.69123, 145.02735};
        double[][] locations = {home, university, work};
        String toReturn = weatherBaseURL + "latitude=" + locations[0][0] + "," + locations[1][0] + "," + locations[2][0] + "&longitude=" + locations[0][1] + "," + locations[1][1] + "," + locations[2][1] + "&daily=sunrise,sunset,precipitation_sum&hourly=temperature_2m,precipitation,apparent_temperature,wind_speed_10m&forecast_days=3";
        return toReturn;
    }

    public static String buildNewsURL(int selection)
    {
        if(selection == 0)
        {
            return newsBaseURL += "top-headlines?country=au&apiKey=83672ae5290b44d7bef4125c7837a420";
        }
        return null;
    }
}