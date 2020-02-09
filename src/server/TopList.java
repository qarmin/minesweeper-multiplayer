package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TopList {

	static File f = new File("toplist/toplist.txt");

    static public String readFile() {
    	String allString = "";
        String tmpStr = "";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(f));
            while ((tmpStr = br.readLine()) != null) {
                allString += tmpStr + "="; //That means new line
            }
            System.out.println(allString);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Wczytano dane z pliku");
        return allString;
    }
	
	
    static public void writeToFile(String Text) {
    	
        BufferedWriter bw;
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            bw = new BufferedWriter(new FileWriter(f));
            String[] qq = Text.split("=");
            for (int i=0;i<qq.length;i++) {
            	bw.write(qq[i]);
            	bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Zapisano dane do pliku");
    }
}
