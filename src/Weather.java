import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.time.*;
import java.util.Scanner;

public class Weather {
    private double[][] temperature = new double[3][24];
    private double[][] precipitation = new double[3][24];
    private double[][] apparentTemperature = new double[3][24];
    private double[][] windSpeed = new double[3][24];
    private LocalDateTime[] sunrise = new LocalDateTime[3];
    private LocalDateTime[] sunset = new LocalDateTime[3];
    private double[] precipitationSum = new double[3];

    public Weather home;
    public Weather uni;
    public Weather work;

    public Weather(int location) throws Exception 
    {
        try {
            String URL = KeyGet.buildWeatherURL();
            URI w = new URI(URL);
            URL weather = w.toURL();

            BufferedReader in = new BufferedReader(new InputStreamReader(weather.openStream()));

            Scanner reader = new Scanner(in);
            String data = reader.nextLine();
            reader.close();
            in.close();

            String[] weatherData = data.split("\\{");

            if(location != -1)
            {
                int s = 5 * location;

                String tempData = weatherData[s + 3].substring(weatherData[s + 3].indexOf("temperature_2m") + 17, weatherData[s + 3].indexOf("precipitation") - 3);
                String[] temps = tempData.split(",");
                String precData = weatherData[s + 3].substring(weatherData[s + 3].indexOf("precipitation") + 16, weatherData[s + 3].indexOf("apparent_temperature") - 3);
                String[] precs = precData.split(",");
                String appTData = weatherData[s + 3].substring(weatherData[s + 3].indexOf("apparent_temperature") + 23, weatherData[s + 3].indexOf("wind_speed_10m") - 3);
                String[] apTems = appTData.split(",");
                String windData = weatherData[s + 3].substring(weatherData[s + 3].indexOf("wind_speed_10m") + 17, weatherData[s + 3].indexOf("daily_units") - 4);
                String[] winds = windData.split(",");

                for(int i = 0; i < temperature.length; i++)
                {
                    for(int j = 0; j < temperature[i].length; j++)
                    {
                        temperature[i][j] = Double.parseDouble(temps[i * 24 + j]);
                        precipitation[i][j] = Double.parseDouble(precs[i * 24 + j]);
                        apparentTemperature[i][j] = Double.parseDouble(apTems[i * 24 + j]);
                        windSpeed[i][j] = Double.parseDouble(winds[i * 24 + j]);

                        //System.out.println(i + ", " + j + " >> " + temperature[i][j]);
                    }
                }

                String setData = weatherData[s + 5].substring(weatherData[s + 5].indexOf("sunrise") + 11, weatherData[s + 5].indexOf("sunset") - 4);
                String[] sets = setData.split(",");
                String riseData = weatherData[s + 5].substring(weatherData[s + 5].indexOf("sunset") + 10, weatherData[s + 5].indexOf("precipitation_sum") - 4);
                String[] rises = riseData.split(",");
                String toPData = weatherData[s + 5].substring(weatherData[s + 5].indexOf("precipitation_sum") + 20, weatherData[s + 5].length() - 4);
                String[] tPres = toPData.split(",");

                for (int i = 0; i < sunset.length; i++)
                {
                    sets[i] = sets[i].replace("\"", " ").trim();
                    rises[i] = rises[i].replace("\"", " ").trim();

                    sunset[i] = LocalDateTime.parse(sets[i]);
                    sunrise[i] = LocalDateTime.parse(rises[i]);
                    precipitationSum[i] = Double.parseDouble(tPres[i]);
                }
            } else {
                throw new Exception("Invalid Location Given.");
            }
        } catch (Exception e)
        {
            //throw e;
            e.printStackTrace();
            throw new Exception("Weather cooked it.");
        }
    }

    public Weather() throws Exception
    {
        home = new Weather(0);
        uni = new Weather(1);
        work = new Weather(2);
    }

    public String toString()
    {
        String toReturn = "Weather [";
        for (int i = 0; i < temperature.length; i++)
        {
            toReturn += "Day " + i + " [";
            for (int j = 0; j < temperature[i].length; j++)
            {
                toReturn += j + " [T: " + temperature[i][j] + " P: " + precipitation[i][j] + " AT: " + apparentTemperature[i][j] + " W: " + windSpeed[i][j] + "]\n";
            }
            toReturn += "SR: " + sunrise[i] + " SS: " + sunset[i] + " PS: " + precipitationSum[i] + "]\n\n";
        }

        return toReturn;
    }

    public double[][] getTemperature()
    {
        double[][] r = new double[temperature.length][temperature[0].length];
        for (int i = 0; i < temperature.length; i++)
        {
            for (int j = 0; j < temperature[i].length; j++)
            {
                r[i][j] = temperature[i][j];
            }
        }
        return r;
    }

    public double[] getTemperature(int day)
    {
        double[] r = new double[temperature[day].length];
        for (int i = 0; i < r.length; i++)
        {
            r[i] = temperature[day][i];
        }
        return r;
    }

    public double getTemperature(int day, int hour)
    {
        return temperature[day][hour];
    }

    public double[][] getPrecipitation()
    {
        double[][] r = new double[precipitation.length][precipitation[0].length];
        for (int i = 0; i < precipitation.length; i++)
        {
            for (int j = 0; j < precipitation[i].length; j++)
            {
                r[i][j] = precipitation[i][j];
            }
        }
        return r;
    }

    public double[] getPrecipitation(int day)
    {
        double[] r = new double[precipitation[day].length];
        for (int i = 0; i < r.length; i++)
        {
            r[i] = precipitation[day][i];
        }
        return r;
    }

    public double getPrecipitation(int day, int hour)
    {
        return precipitation[day][hour];
    }

    public double[][] getApparentTemperature()
    {
        double[][] r = new double[apparentTemperature.length][apparentTemperature[0].length];
        for (int i = 0; i < apparentTemperature.length; i++)
        {
            for (int j = 0; j < apparentTemperature[i].length; j++)
            {
                r[i][j] = apparentTemperature[i][j];
            }
        }
        return r;
    }

    public double[] getApparentTemperature(int day)
    {
        double[] r = new double[apparentTemperature[day].length];
        for (int i = 0; i < r.length; i++)
        {
            r[i] = apparentTemperature[day][i];
        }
        return r;
    }

    public double getApparentTemperature(int day, int hour)
    {
        return apparentTemperature[day][hour];
    }

    public double[][] getWindSpeed()
    {
        double[][] r = new double[windSpeed.length][windSpeed[0].length];
        for (int i = 0; i < windSpeed.length; i++)
        {
            for (int j = 0; j < windSpeed[i].length; j++)
            {
                r[i][j] = windSpeed[i][j];
            }
        }
        return r;
    }

    public double[] getWindSpeed(int day)
    {
        double[] r = new double[windSpeed[day].length];
        for (int i = 0; i < r.length; i++)
        {
            r[i] = windSpeed[day][i];
        }
        return r;
    }

    public double getWindSpeed(int day, int hour)
    {
        return windSpeed[day][hour];
    }

    public LocalDateTime[] getSunrise()
    {
        LocalDateTime[] r = new LocalDateTime[sunrise.length];
        for (int i = 0; i < sunrise.length; i++)
        {
            r[i] = LocalDateTime.parse((sunrise[i].toString()));
        }

        return r;
    }

    public LocalDateTime getSunrise(int day)
    {
        return LocalDateTime.parse(sunrise[day].toString());
    }

    public LocalDateTime[] getSunset()
    {
        LocalDateTime[] r = new LocalDateTime[sunset.length];
        for (int i = 0; i < sunset.length; i++)
        {
            r[i] = LocalDateTime.parse((sunset[i].toString()));
        }

        return r;
    }

    public LocalDateTime getSunset(int day)
    {
        return LocalDateTime.parse(sunset[day].toString());
    }

    public double[] getPrecipitationSum()
    {
        double[] r = new double[precipitationSum.length];
        for (int i = 0; i < precipitationSum.length; i++)
        {
            r[i] = precipitationSum[i];
        }

        return r;
    }

    public double getPrecipitationSum(int day)
    {
        return precipitationSum[day];
    }

    public double[] getMaxTemperature()
    {
        double[] max = new double[temperature.length];

        for(int i = 0; i < temperature.length; i++)
        {
            for (int j = 0; j < temperature[i].length; j++)
            {
                if (temperature[i][j] >= max[i])
                {
                    max[i] = temperature[i][j];
                }
            }
        }

        return max;
    }

    public double getMaxTemperature(int day)
    {
        double max = 0;

        for(int i = 0; i < temperature[day].length; i++)
        {
            if(temperature[day][i] >= max)
            {
                max = temperature[day][i];
            }
        }

        return max;
    }

    public double[] getMinTemperature()
    {
        double[] min = new double[temperature.length];

        for(int i = 0; i < temperature.length; i++)
        {
            min[i] = temperature[i][0];
            for (int j = 0; j < temperature[i].length; j++)
            {
                if (temperature[i][j] <= min[i])
                {
                    min[i] = temperature[i][j];
                }
            }
        }

        return min;
    }

    public double getMinTemperature(int day)
    {
        double min = 0;
        min = temperature[day][0];

        for(int i = 0; i < temperature[day].length; i++)
        {
            
            if(temperature[day][i] <= min)
            {
                min = temperature[day][i];
            }
        }

        return min;
    }

    public double getCurrentTemperature()
    {
        return getTemperature(0, LocalDateTime.now().getHour());
    }

    public double getCurrentPrecipitation()
    {
        return getPrecipitation(0, LocalDateTime.now().getHour());
    }

    public double getNextPrecipitation()
    {
        return getPrecipitation(0, LocalDateTime.now().getHour());
    }

    public double getCurrentWindSpeed()
    {
        return getWindSpeed(0, LocalDateTime.now().getHour());
    }
}