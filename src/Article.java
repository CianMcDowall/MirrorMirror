import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Article {
    private LocalDateTime publicationDate;
    private String title;
    
    public Article(int sectionNumber) throws Exception
    {
        try {
            String URL = KeyGet.buildNewsURL(sectionNumber);
            URI n = new URI(URL);
            URL weather = n.toURL();

            BufferedReader in = new BufferedReader(new InputStreamReader(weather.openStream()));

            Scanner reader = new Scanner(in);
            String data = reader.nextLine();
            reader.close();
            in.close();

            String[] newsData = data.split("\\{");
            
            String pd = newsData[3].substring(newsData[3].indexOf("webPublicationDate") + 21, newsData[3].indexOf("webTitle") - 4);
            publicationDate = LocalDateTime.parse(pd);
            title = newsData[3].substring(newsData[3].indexOf("webTitle") + 11, newsData[3].indexOf("webUrl") - 3);
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new Exception("News cooked it.");
        }
    }

    public LocalDateTime getPublicationDate()
    {
        return publicationDate;
    }

    public String getHeadline()
    {
        return title;
    }

    public String toString()
    {
        return publicationDate.format(DateTimeFormatter.ofPattern("k:m:s ")) + title;
    }
}