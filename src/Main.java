import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        Scanner input = new Scanner(System.in);
        String API_KEY = System.getenv("API_KEY");
        String Url = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/USD";

        // Making Request
        URI NEW_URL = new URI(Url);
        URL url = NEW_URL.toURL();
        URLConnection connection = url.openConnection();
        connection.connect();

        Gson gson = new Gson();
        JsonObject jsonobj = gson.fromJson(new InputStreamReader((InputStream) connection.getContent()), JsonObject.class);
        ArrayList<String> arrayList = getData(jsonobj);
        System.out.println("Currencies Available:");
        System.out.println(arrayList);
        System.out.println("from currency:");
        String fromcurrency = input.nextLine().toUpperCase();
        if(!checkcorrectcurrency(fromcurrency, arrayList)){
            System.out.println("enter valid currency code , if needed refer to list:");
            fromcurrency = input.nextLine().toUpperCase();
        }
        System.out.println("Enter amount to convert:");
        int amount = Integer.valueOf(input.next());
        System.out.println("to currency:");
        String tocurrency = input.next().toUpperCase();
        if(!checkcorrectcurrency(tocurrency, arrayList)){
            System.out.println("enter valid currency code , if needed refer to list:");
            tocurrency = input.nextLine().toUpperCase();
        }
        Float result = calculator(jsonobj,fromcurrency,amount,tocurrency);
        System.out.println("Result:"+ result);

    }
    public static boolean checkcorrectcurrency(String currencycode, ArrayList<String> arr){
        if(!arr.contains(currencycode)){
            return false;
        }
        return true;
    }
    public static ArrayList<String> getData(JsonObject jsonobj){
        JsonObject conversionRates = jsonobj.get("conversion_rates").getAsJsonObject();
        Set<String> currencyCodes = conversionRates.keySet();
        ArrayList<String> arrayList = new ArrayList<>(currencyCodes);
        return arrayList;
    }
    public static Float calculator(JsonObject jsonobj ,String fromcurrency,int amount , String tocurrency){
        JsonObject conversionRates = jsonobj.get("conversion_rates").getAsJsonObject();
        Float fromcurrencybaseUSD = conversionRates.get(fromcurrency).getAsFloat();
        Float tocurrencybaseUSD = conversionRates.get(tocurrency).getAsFloat();
        Float base = tocurrencybaseUSD/fromcurrencybaseUSD;
        return base*amount;
    }
}