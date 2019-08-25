import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

        //Element firstTable = doc.select("table").first();
        //System.out.print(firstTable.text());
        //System.out.println("\n\n\n---------\n\n");

        ArrayList<String> tableList = new ArrayList<>();
        Elements tables = null;

        try{
            tables = doc.getElementsByTag("table");
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }

        int nTable = 0;
        for(Element table : tables){

            if(nTable%2 == 0){
                tableList.add(table.text());
            }

            nTable++;
        }

        for (int i=0; i<tableList.size(); i++){
            parseMatrix(tableList.get(i));
        }

    }

    static private void parseMatrix(String plainText){

        System.out.println("\n\n\n---------\n\n");

        Pattern pattern = Pattern.compile("P\\d");
        Matcher matcher = pattern.matcher(plainText);

        int nPlaces = 0;
        int nTrans = 0;

        while (matcher.find()) {
            nPlaces++;
        }

        pattern = Pattern.compile("T\\d");
        matcher = pattern.matcher(plainText);

        while (matcher.find()){
            nTrans++;
        }

        pattern = Pattern.compile("\\s\\d+");
        matcher = pattern.matcher(plainText);

        for(int j=0; j<nPlaces; j++){

            for(int i=0; i<nTrans; i++){
                matcher.find();
                System.out.print(matcher.group());
            }

            System.out.println("");
        }

        System.out.println("");


        System.out.println("Cantidad de Plazas = "+ String.valueOf(nPlaces));
        System.out.println("Cantidad de Transiciones = "+ String.valueOf(nTrans));

    }

}
