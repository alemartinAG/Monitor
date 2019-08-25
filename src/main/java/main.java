import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class main {

    public static void main(String[] args){

        File input = new File("/home/ale/Desktop/papa.html"); //get env $HOME
        Document doc = null;
        {
            try {
                doc = Jsoup.parse(input, "UTF-8", "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Element firstTable = doc.select("table").first();
        System.out.print(firstTable.text());

        System.out.println("\n\n\n---------\n\n\n");

        Pattern numRegex = Pattern.compile("\\s\\d+");
        Matcher match = numRegex.matcher(firstTable.text());

        while(match.find()) {
            System.out.println(match.group());
        }


    }

}
