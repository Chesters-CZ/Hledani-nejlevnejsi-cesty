import org.json.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

public class Delani {


    public HashMap<Character, Boolean> seznamMest = new HashMap<>();
    public ArrayList<Spoj> seznamSpoju = new ArrayList<>();

    public ArrayList<Spoj> zvoleneSpoje = new ArrayList<>();

    public void delej() {

        String json = "";
        try {
            File myObj = new File("spoje-a-mesta.json");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                json = json.concat(data);
            }
            myReader.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("mesta");
        for (int i = 0; i < jsonArray.length(); i++) {
            System.out.println(i+": "+jsonArray.get(i).toString().charAt(0));
            seznamMest.put(jsonArray.get(i).toString().charAt(0), false);
        }


        jsonArray = jsonObject.getJSONArray("spoje");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject currentObj = jsonArray.getJSONObject(i);
            seznamSpoju.add(new Spoj(currentObj.get("mesto1").toString().charAt(0), currentObj.get("mesto2").toString().charAt(0), Integer.parseInt(currentObj.get("cena").toString())));
            System.out.println(i+": "+currentObj.get("mesto1").toString().charAt(0)+", "+ currentObj.get("mesto2").toString().charAt(0)+", "+ Integer.parseInt(currentObj.get("cena").toString()));
        }

        findRequiredSpoje();
    }


    public void findRequiredSpoje() {
        seznamSpoju.sort(Comparator.comparingInt(a -> a.cena));
        zvoleneSpoje.add(seznamSpoju.get(0));
        seznamSpoju.remove(zvoleneSpoje.get(0));
        seznamMest.put(zvoleneSpoje.get(0).mesto1, true);
        seznamMest.put(zvoleneSpoje.get(0).mesto2, true);

        while (!isVsechnoPropojeno() && seznamSpoju.size() != 0) {
            ArrayList<Spoj> toBeRemoved = new ArrayList<>();
            for (Spoj s : seznamSpoju) {
                System.out.println(seznamSpoju.indexOf(s) +": Z "+s.mesto1+" ("+seznamMest.get(s.mesto1)+") do " + s.mesto2+" ("+seznamMest.get(s.mesto2)+") za "+s.cena);
                if (!seznamMest.get(s.mesto1) && !seznamMest.get(s.mesto2)) {
                    continue;
                } else if (seznamMest.get(s.mesto1) && seznamMest.get(s.mesto2)) {
                    toBeRemoved.add(s);
                    break;
                } else if (seznamMest.get(s.mesto1) && !seznamMest.get(s.mesto2)) {
                    seznamMest.put(s.mesto2, true);

                    zvoleneSpoje.add(s);
                    toBeRemoved.add(s);
                    break;
                } else if (!seznamMest.get(s.mesto1) && seznamMest.get(s.mesto2)) {
                    seznamMest.put(s.mesto1, true);

                    zvoleneSpoje.add(s);
                    toBeRemoved.add(s);
                    break;
                }
            }

            seznamSpoju.removeAll(toBeRemoved);
        }

        System.out.println("Hotovo.");

        if (isVsechnoPropojeno()) {
            System.out.println("Vsechno je propojeno:");
            int totalCost = 0;
            for (Spoj s : zvoleneSpoje) {
                System.out.println(s.mesto1 + " - " + s.mesto2 + " (" + s.cena + ")");
                totalCost += s.cena;
            }
            System.out.println("Ceková cena: " + totalCost);
        }
    }

    public boolean isVsechnoPropojeno() {
        for (boolean b : seznamMest.values()) {
            if (!b)
                return false;
        }
        return true;
    }
}
