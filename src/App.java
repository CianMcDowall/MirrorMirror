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

    private static Container newsContainer;
    private static JLabel ausNews;

    private static int page = 0;
    private static int lastPage = 0;
    private static int lastHour;

    private static Container gTimeContainer;
    private static JLabel gTime;

    private static String APIKey;

    //SM = 1 for testing
    //SM = 2 for operation
    private static int sizeMultiplier = 1;
    public static void main(String[] args)
    {
        try {
            APIKey = args[0];
        } catch (Exception e)
        {
            APIKey = "";
        }

        openWindow();

        temperatureContainer = new Container();
        temperatureContainer.setBounds(300 * sizeMultiplier, 200 * sizeMultiplier, 225 * sizeMultiplier, 70 * sizeMultiplier);

        printHome();

        container.paintComponents(container.getGraphics());

        lastHour = LocalDateTime.now().getHour();

        new Thread(() -> {
            update();
        }).start();
    }

    private static void openWindow()
    {
        frame = new JFrame("Smart Mirror");
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(540 * sizeMultiplier, 960 * sizeMultiplier);
        frame.setLayout(null);
        container = frame.getContentPane();
        container.setBackground(Color.black);
    }

    private static void printHome()
    {
        //page = 0;
        container.removeAll();
        container.print(container.getGraphics());

        try {
            printWeather();
        } catch (Exception e)
        {
            temperatureContainer.setLayout(new GridLayout(1, 1));
            JLabel eLabel = new JLabel("Weather cooked it.");
            eLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 24 * sizeMultiplier));
            eLabel.setForeground(Color.RED);
            temperatureContainer.add(eLabel);
            container.add(temperatureContainer);;
        }

        try {
            printNews();
        } catch (Exception e)
        {
            e.printStackTrace();
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

    private static void printNews() throws Exception
    {
        Article aus = Interpreter.getNews("Australia", APIKey);
        Article world = Interpreter.getNews("World", APIKey);

        String ausHeadline = aus.getHeadline();
        String worldHeadline = world.getHeadline();

        newsContainer = new Container();
        newsContainer.setBounds(-20 * sizeMultiplier, 25 * sizeMultiplier, 520 * sizeMultiplier, 80 * sizeMultiplier);
        newsContainer.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;

        Font newsFont = new Font("Bahnschrift", Font.PLAIN, 9 * sizeMultiplier);
        
        ausNews = new JLabel(ausHeadline);
        ausNews.setFont(newsFont);
        ausNews.setForeground(new Color(70, 156, 0));
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        newsContainer.add(ausNews, c);

        JLabel worldNews = new JLabel(worldHeadline);
        worldNews.setFont(newsFont);
        worldNews.setForeground(new Color(0, 190, 255));
        worldNews.setHorizontalAlignment(JLabel.LEFT);
        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        newsContainer.add(worldNews, c);

        container.add(newsContainer);
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
            Weather weather = Interpreter.getWeather("all");
            container.removeAll();
            container.repaint();
            page = 1;
            lastPage = 1;

            gTimeContainer = new Container();
            gTimeContainer.setBounds(460 * sizeMultiplier, 30 * sizeMultiplier, 40 * sizeMultiplier, 20 * sizeMultiplier);
            gTimeContainer.setLayout(new GridLayout());

            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("h:mm"));

            gTime = new JLabel(time, JLabel.LEFT);
            gTime.setFont(new Font("Bahnschrift", Font.PLAIN, 18 * sizeMultiplier));
            gTime.setForeground(Color.WHITE);
            gTimeContainer.add(gTime);

            for(int location = 0; location < 3; location++)
            {
                Weather weatherLoc;
                String labelString;
                switch (location) {
                    case 0: weatherLoc = weather.home;
                            labelString = "Home.";
                            break;
                    case 1: weatherLoc = weather.uni;
                            labelString = "University.";
                            break;
                    case 2: weatherLoc = weather.work;
                            labelString = "Work.";
                            break;
                    default: throw new Exception("For statement in fullWeather(), incorrect number");
                }

                int pushDown = location * 300 * sizeMultiplier;
                JPanel homeGraphPanel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g1)
                    {
                        super.paintComponent(g1);
                        Graphics2D g = (Graphics2D) g1;
                        g.setColor(Color.WHITE);
                        BasicStroke tStroke = new BasicStroke(4 * sizeMultiplier);
                        BasicStroke pStroke = new BasicStroke(2 * sizeMultiplier);
                        int frameHeight = 130 * sizeMultiplier;

                        double maxTemp = weatherLoc.getMaxTemperature(0);
                        double minTemp = weatherLoc.getMinTemperature(0);
                        double maxPrec = weatherLoc.getMaxPrecipitation(0);
                        double minPrec = weatherLoc.getMinPrecipitation(0); 

                        int tempLastY = (int) (frameHeight - (frameHeight * 0.95 * ((weatherLoc.getTemperature(0, 0) - minTemp) / (maxTemp - minTemp))));
                        int precLastY = 0;
                        if(maxPrec >= 5)
                        {
                            precLastY = (int) (frameHeight - (frameHeight * 0.95 * ((weatherLoc.getPrecipitation(0, 0) - minPrec) / (maxPrec - minPrec))) + 5);
                        } else {
                            precLastY = (int) (frameHeight - (frameHeight * 0.95 * ((weatherLoc.getPrecipitation(0, 0) - minPrec) / (6 - minPrec))) + 5);
                        }
                        int lastx = 0;
                        double[] temps = weatherLoc.getTemperature(0);
                        double[] precs = weatherLoc.getPrecipitation(0);

                        LocalDateTime sunrise = weatherLoc.getSunrise(0);
                        int sunriseHour = sunrise.getHour();

                        LocalDateTime sunset = weatherLoc.getSunset(0);
                        int sunsetHour = sunset.getHour();

                        int nowHour = LocalDateTime.now().getHour();

                        for(int i = 1; i < temps.length; i++)
                        {
                            double normalisedTemp = ((temps[i] - minTemp) / (maxTemp - minTemp));
                            double normalisedPrec = 0.0;
                            if(maxPrec >= 5)
                            {
                                normalisedPrec = ((precs[i] - minPrec) / (maxPrec - minPrec));
                            } else {
                                normalisedPrec = ((precs[i] - minPrec) / (5 - minPrec));
                            }

                            int tempY = (int) (frameHeight - (frameHeight * 0.95 * normalisedTemp));
                            int precY = (int) (frameHeight - (frameHeight * 0.95 * normalisedPrec) + 5);
                            
                            Color tempColor = new Color(255, (int) (frameHeight * (1 - normalisedTemp) + 50), (int) (frameHeight * (1 - normalisedTemp)));
                            g.setColor(tempColor);
                            g.setStroke(tStroke);
                            g.drawLine(lastx, tempLastY, lastx + 20, tempY);
                            
                            g.setColor(Color.BLUE);
                            g.setStroke(pStroke);
                            g.drawLine(lastx, precLastY, lastx + 20, precY);

                            if(i == sunriseHour || i == sunsetHour)
                            {
                                g.setColor(Color.YELLOW);
                                g.setStroke(tStroke);
                                g.drawLine(lastx, tempLastY - 15, lastx, frameHeight);
                            }

                            if(i == nowHour)
                            {
                                g.setColor(tempColor);
                                g.setStroke(new BasicStroke(10));
                                g.drawOval((lastx + 10) * sizeMultiplier, (tempY - 5) * sizeMultiplier, 10 * sizeMultiplier, 10 * sizeMultiplier);
                            }

                            lastx += 20 * sizeMultiplier;
                            tempLastY = tempY;
                            precLastY = precY;
                        }
                    }
                };

                homeGraphPanel.setBounds(35 * sizeMultiplier, 100 * sizeMultiplier + pushDown, 460 * sizeMultiplier, 160 * sizeMultiplier);
                homeGraphPanel.setOpaque(false);

                JLabel title = new JLabel(labelString, JLabel.LEFT);
                title.setFont(new Font("Bahnschrift", Font.PLAIN, 36 * sizeMultiplier));
                title.setForeground(Color.WHITE);
                title.setBounds(10 * sizeMultiplier, 20 * sizeMultiplier + pushDown, 180 * sizeMultiplier, 45 * sizeMultiplier);
                container.add(title);

                LocalDateTime sunrise = weatherLoc.getSunrise(0);
                int sunriseHour = sunrise.getHour();
                String sunriseTime = sunrise.format(DateTimeFormatter.ofPattern("h:mm"));

                LocalDateTime sunset = weatherLoc.getSunset(0);
                int sunsetHour = sunset.getHour();
                String sunsetTime = sunset.format(DateTimeFormatter.ofPattern("h:mm"));

                String timeText = sunriseTime;

                for(int i = 0; i < 23; i++)
                {
                    if(i == sunriseHour || i == sunsetHour)
                    {
                        JLabel timeLabel = new JLabel(timeText, JLabel.CENTER);
                        timeLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 18 * sizeMultiplier));
                        timeLabel.setForeground(Color.YELLOW);
                        timeLabel.setBounds((i * 20) - 5, 240 * sizeMultiplier, 40, 25);
                        container.add(timeLabel);
                        timeText = sunsetTime;
                    }
                }

                Container homeContainer = new Container();
                homeContainer.setBounds(20 * sizeMultiplier, 230 * sizeMultiplier + pushDown, 490 * sizeMultiplier, 100 * sizeMultiplier);
                homeContainer.setLayout(new GridBagLayout());

                double[] temps = weatherLoc.getTemperature(0);
                double maxTemp = weatherLoc.getMaxTemperature(0);
                double minTemp = weatherLoc.getMinTemperature(0);
                int currHour = LocalDateTime.now().getHour();
                for(int i = 0; i < temps.length; i++)
                {
                    JLabel temp = new JLabel(temps[i] + " ", JLabel.CENTER);
                    int fontSize = 0;
                    if(i >= currHour + 3 || i <= currHour - 3)
                    {
                        fontSize = 9 * sizeMultiplier;
                    } else if(i == currHour + 2 || i == currHour - 2) {
                        fontSize = 15 * sizeMultiplier;
                    } else if(i == currHour + 1 || i == currHour - 1) {
                        fontSize = 21 * sizeMultiplier;
                    } else {
                        fontSize = 28 * sizeMultiplier;
                    }
                    temp.setFont(new Font("Bahnschrift", Font.PLAIN, fontSize));
                    Color fontColor = Color.WHITE;
                    if(temps[i] == maxTemp)
                    {
                        fontColor = new Color(255, 160, 160);
                    } else if(temps[i] == minTemp)
                    {
                        fontColor = new Color(160, 160, 255);
                    }
                    temp.setForeground(fontColor);
                    homeContainer.add(temp);
                }
        
                container.add(homeGraphPanel);
                container.add(homeContainer);
            }
            
            container.add(gTimeContainer);
            container.revalidate();
            container.repaint();
        } catch (Exception e) {
            page = 0;
            System.out.println(e.getLocalizedMessage());
        }
    }

    private static void update()
    {
        if(page == 0)
        {
            if(lastPage != 0)
            {
                printHome();
            }
            updateHome();
        } else if (page == 1) {
            if(lastPage != 1)
            {
                fullWeather();
            } 
            updateFullWeather();
        } else {
            page = 0;
            updateHome();
        }
        lastPage = page;

        int currentMinute = LocalDateTime.now().getMinute();

        if (currentMinute == 0 || currentMinute == 15 || currentMinute == 30 || currentMinute == 45)
        {
            page = 1;
        } else {
            page = 0;
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
        int h = LocalDateTime.now().getHour();
        if(lastHour != h)
        {
            fullWeather();
            lastHour = h;
        }

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("h:mm"));
        gTime.setText(time);

        gTimeContainer.revalidate();
        gTimeContainer.repaint();
    }
}
