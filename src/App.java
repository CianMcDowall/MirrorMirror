import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class App {
    private static JFrame frame;
    private static Container container;
    private static Container temperatureContainer;
    
    private static Container timeContainer;
    private static JLabel hour;
    private static JLabel minute;
    private static JLabel second;

    private static Container dateContainer;
    private static JLabel date;

    private static int page = 0;

    //SM = 1 for testing
    //SM = 2 for operation
    private static int sizeMultiplier = 1;
    public static void main(String[] args)
    {
        openWindow();

        temperatureContainer = new Container();
        temperatureContainer.setBounds(300 * sizeMultiplier, 200 * sizeMultiplier, 225 * sizeMultiplier, 70 * sizeMultiplier);

        printHome();

        container.paintComponents(container.getGraphics());

        new Thread(() -> {
            update();
        }).start();
    }

    private static void openWindow()
    {
        frame = new JFrame("Smart Mirror");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(540 * sizeMultiplier, 960 * sizeMultiplier);
        frame.setLayout(null);
        container = frame.getContentPane();
        container.setBackground(Color.black);
    }

    private static void printHome()
    {
        page = 0;

        try {
            printWeather();
        } catch (Exception e)
        {
            temperatureContainer.setLayout(new GridLayout(1, 1));
            JLabel eLabel = new JLabel(e.getLocalizedMessage());
            eLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 24 * sizeMultiplier));
            eLabel.setForeground(Color.RED);
            temperatureContainer.add(eLabel);
            container.add(temperatureContainer);;
        }

        printTime();
        printDate();
    }

    private static void printWeather() throws Exception
    {      
        Weather weather = Interpreter.getWeather("Home");
        String maxTemp = weather.getMaxTemperature(0) + "\u00B0";
        String minTemp = weather.getMinTemperature(0) + "\u00B0";
        String curTemp = weather.getCurrentTemperature() + "\u00B0";
        String curRain = weather.getCurrentPrecipitation() + "";
        String nexRain = weather.getNextPrecipitation() + "";
        String totRain = weather.getPrecipitationSum(0) + "";

        Container rainContainer = new Container();
        rainContainer.setBounds(292 * sizeMultiplier, 245 * sizeMultiplier, 225 * sizeMultiplier, 50 * sizeMultiplier);
        
        temperatureContainer.setLayout(new GridBagLayout());
        rainContainer.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        JLabel minimumTemp = new JLabel(minTemp, JLabel.LEFT);
        minimumTemp.setFont(new Font("Bahnschrift", Font.PLAIN, 28 * sizeMultiplier));
        minimumTemp.setForeground(new Color(160,160,190));
        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 2;
        c.gridwidth = 1;
        temperatureContainer.add(minimumTemp, c);

        JLabel currentTemp = new JLabel(curTemp, JLabel.CENTER);
        currentTemp.setFont(new Font("Bahnschrift", Font.PLAIN, 46 * sizeMultiplier));
        currentTemp.setForeground(Color.WHITE);
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 3;
        c.gridwidth = 2;
        temperatureContainer.add(currentTemp, c);

        JLabel maximumTemp = new JLabel(maxTemp, JLabel.RIGHT);
        maximumTemp.setFont(new Font("Bahnschrift", Font.PLAIN, 28 * sizeMultiplier));
        maximumTemp.setForeground(new Color(190, 160, 160));
        c.gridx = 4;
        c.gridy = 1;
        c.gridheight = 2;
        c.gridwidth = 1;
        temperatureContainer.add(maximumTemp, c);

        JLabel rainNow;
        Font fontSun = new Font("Ariel", Font.PLAIN, 28 * sizeMultiplier);

        if(!totRain.equals("0.0"))
        {
            c.gridx = 0;
            c.gridy = 0;
            c.gridheight = 1;
            c.gridwidth = 1;

            Color rainBlue = new Color(160, 160, 250);
            Font fontCA = new Font("Ariel", Font.PLAIN, 22 * sizeMultiplier);
            Font fontNum = new Font("Bahnschrift", Font.PLAIN, 22 * sizeMultiplier);
            
            if(!curRain.equals("0.0"))
            {
                JLabel cloudNow = new JLabel("\u2601");
                cloudNow.setFont(fontCA);
                cloudNow.setForeground(rainBlue);
                rainContainer.add(cloudNow, c);

                rainNow = new JLabel(curRain);
                rainNow.setFont(fontNum);
                rainNow.setForeground(rainBlue);
                c.gridx = c.gridx + 1;
                rainContainer.add(rainNow, c);

                JLabel arrowNow = new JLabel("\u2192");
                arrowNow.setFont(fontCA);
                arrowNow.setForeground(rainBlue);
                c.gridx = c.gridx + 1;
                rainContainer.add(arrowNow, c);
            } else {
                rainNow = new JLabel("\u2600\u2192");
                rainNow.setFont(fontSun);
                rainNow.setForeground(Color.ORANGE);
                rainContainer.add(rainNow, c);
            }

            JLabel rainNext;
            
            if (!nexRain.equals("0.0"))
            {
                JLabel cloudNext = new JLabel("\u2601");
                cloudNext.setFont(fontCA);
                cloudNext.setForeground(rainBlue);
                c.gridx = c.gridx + 1;
                rainContainer.add(cloudNext, c);

                rainNext = new JLabel(nexRain);
                rainNext.setFont(fontNum);
                rainNext.setForeground(rainBlue);
                c.gridx = c.gridx + 1;
                rainContainer.add(rainNext, c);
            } else {
                rainNext = new JLabel("\u2600");
                rainNext.setFont(fontSun);
                rainNext.setForeground(Color.ORANGE);
                c.gridx = c.gridx + 1;
                rainContainer.add(rainNext, c);
            }

            JLabel cloudTotal = new JLabel("     \u2601");
            cloudTotal.setFont(fontCA);
            cloudTotal.setForeground(rainBlue);
            c.gridx = c.gridx + 1;
            rainContainer.add(cloudTotal, c);

            JLabel rainTotal = new JLabel(totRain);
            rainTotal.setFont(fontNum);
            rainTotal.setForeground(rainBlue);
            c.gridx = c.gridx + 1;
            rainContainer.add(rainTotal, c);
        } else {
            rainNow = new JLabel("\u2600");
            rainNow.setFont(fontSun);
            rainNow.setForeground(Color.ORANGE);
            rainContainer.add(rainNow);
        }

        Container windContainer = new Container();
        windContainer.setBounds(325 * sizeMultiplier, 170 * sizeMultiplier, 150 * sizeMultiplier, 50 * sizeMultiplier);
        windContainer.setLayout(new GridBagLayout());

        String windSpeed = weather.getCurrentWindSpeed() + "";
        JLabel windEmoji = new JLabel("\uD83C\uDF00", JLabel.RIGHT);
        windEmoji.setFont(new Font("Ariel", Font.PLAIN, 16 * sizeMultiplier));
        windEmoji.setForeground(new Color(0, 190, 255));
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        windContainer.add(windEmoji, c);

        JLabel wind = new JLabel(windSpeed, JLabel.LEFT);
        wind.setFont(new Font("Bahnschrift", Font.PLAIN, 25 * sizeMultiplier));
        wind.setForeground(new Color(0, 190, 255));
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 2;
        c.gridwidth = 1;
        windContainer.add(wind, c);        

        container.add(temperatureContainer);
        container.add(rainContainer);
        container.add(windContainer);
    }

    private static void printTime()
    {
        timeContainer = new Container();
        timeContainer.setBounds(150 * sizeMultiplier, 700 * sizeMultiplier, 240 * sizeMultiplier, 80 * sizeMultiplier);
        timeContainer.setLayout(new GridBagLayout());

        String hours = LocalDateTime.now().getHour() + "";
        String minutes = String.format("%02d", LocalDateTime.now().getMinute());
        String seconds = String.format("%02d", LocalDateTime.now().getSecond());
        Font fontTime = new Font("Bahnschrift", Font.PLAIN, 72 * sizeMultiplier);
        Font fontSec = new Font("Bahnschrift", Font.PLAIN, 56 * sizeMultiplier);

        hour = new JLabel(hours, JLabel.CENTER);
        hour.setFont(fontTime);
        hour.setForeground(Color.WHITE);
        timeContainer.add(hour);

        JLabel semi1 = new JLabel(":", JLabel.CENTER);
        semi1.setFont(fontTime);
        semi1.setForeground(Color.WHITE);
        timeContainer.add(semi1);

        minute = new JLabel(minutes, JLabel.CENTER);
        minute.setFont(fontTime);
        minute.setForeground(Color.WHITE);
        timeContainer.add(minute);

        JLabel semi2 = new JLabel(":", JLabel.CENTER);
        semi2.setFont(fontSec);
        semi2.setForeground(new Color(160, 160, 160));
        timeContainer.add(semi2);

        second = new JLabel(seconds, JLabel.CENTER);
        second.setFont(fontSec);
        second.setForeground(new Color(160, 160, 160));
        timeContainer.add(second);

        container.add(timeContainer);
    }

    private static void printDate()
    {
        dateContainer = new Container();
        dateContainer.setBounds(120 * sizeMultiplier, 765 * sizeMultiplier, 300 * sizeMultiplier, 40 * sizeMultiplier);
        dateContainer.setLayout(new GridLayout());

        String d = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, d LLLL uuuu GGG"));
        date = new JLabel(d, JLabel.CENTER);
        date.setFont(new Font("Bahnschrift", Font.PLAIN, 21 * sizeMultiplier));
        date.setForeground(Color.white);
        dateContainer.add(date);

        container.add(dateContainer);
    }

    private static void fullWeather()
    {
        try {
            Weather weather = new Weather();
            container.removeAll();
            page = 1;
        } catch (Exception e) {
            page = 0;
            
        }
    }

    private static void update()
    {
        if(page == 0)
        {
            updateHome();
        } else if (page == 1) {
            updateFullWeather();
        } else {
            page = 0;
            updateHome();
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        update();
    }

    private static void updateHome()
    {
        int h = LocalDateTime.now().getHour();
        if(h > 12)
        {
            h -= 12;
        }
        String hours = h + "";
        String minutes = String.format("%02d", LocalDateTime.now().getMinute());
        String seconds = String.format("%02d", LocalDateTime.now().getSecond());
        String d = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE d LLLL uuuu GGG"));

        hour.setText(hours);
        minute.setText(minutes);
        second.setText(seconds);
        date.setText(d);

        timeContainer.revalidate();
        timeContainer.repaint();

        dateContainer.revalidate();
        dateContainer.repaint();
    }

    private static void updateFullWeather()
    {

    }
}
