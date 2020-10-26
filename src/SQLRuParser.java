import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
public class SQLRuParser implements Parser{

    private Set<Vacancy> vacancySet;

    private Set<String> getTopics(String keyWord, int period) throws IOException {
        Set<String> topicList = new LinkedHashSet<>();
        Document doc = Jsoup.connect("https://www.sql.ru/forum/actualsearch.aspx?search="+keyWord+
                "&sin=1&st=t&bid=66&a=&ma=0&dt="+period+"&s=1&so=1").get();
        Element count_finder = doc.getElementsByClass("forumtable_results").first();
        Element counter = count_finder.getElementsByTag("font").first();
        int pageCount = Integer.parseInt(counter.ownText()) /50;
        if (pageCount < 1) pageCount = 1;
        for (int i = 1; i <= pageCount; i++) {
            doc = Jsoup.connect("https://www.sql.ru/forum/actualsearch.aspx?search="+keyWord+
                    "&sin=1&st=t&bid=66&a=&ma=0&dt="+period+"&s=1&so=1&pg=" + i).get();
           Elements topics = doc.getElementsByClass("postslisttopic");
            for (Element topic : topics) {
                topicList.add(topic.getElementsByTag("a").first().attr("href"));
            }
        }
        return topicList;
    }

    private Vacancy getDetails(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        Element msgContent = doc.getElementsByClass("messageHeader").first();
        String header = msgContent.ownText();
        Elements msgText = doc.getElementsByClass("msgBody").next();
        String vacancyText = msgText.text();
        msgText = doc.getElementsByClass("msgFooter");
        String date = msgText.text();
        date = date.substring(0, date.indexOf(" ["));
        return new Vacancy(link, header, vacancyText, date);
    }

    @Override
    public Set<Vacancy> parse(String keyWord, int period) throws IOException {
        Set<String> topics = getTopics(keyWord, period);
        Vacancy currentVacancy;
        Set<Vacancy> vacancies = new LinkedHashSet<>();
        int i = 1;
        for (String topic : topics) {
            try {
                currentVacancy = getDetails(topic);
                vacancies.add(currentVacancy);
            } catch (IOException e){e.printStackTrace();}
        }
        return vacancies;
    }
}
