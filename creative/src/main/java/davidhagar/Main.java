package davidhagar;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.io.FileUtils;


public class Main {


    public static void main(String[] args) {

        System.out.println("Start");

        boolean lower = true;


        try (FileOutputStream fos = new FileOutputStream("./out.txt");
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(osw)
        ) {

            String[] w1 = {"the ancient", "the beautiful", "the deep", "the embodied", "the emergent", "the healing", "the hidden", "the infinite", "the inspiring", "the liberating", "the life-changing", "the living", "the luminous", "the mystical", "the mysterious", "the natural", "the rhythmic", "the sacred", "the silent", "the spontaneous"};
            String[] w2 = {"alchemy", "art", "awareness", "essence", "evolution", "experience", "flow", "impact", "language", "magic", "origin", "pathways", "patterns", "playground", "power", "practices", "purpose", "revelations", "structures", "wisdom"};
            String[] w3 = {"of being", "of being human", "of being together", "of compassion", "of consciousness", "of creativity", "of curiosity", "of generosity", "of gratitude", "of imagination", "of kindness", "of love", "of nature", "of presence", "of reality", "of relationship", "of surrender", "of the unknown", "of this moment", "of wonder"};

            ArrayList<String> list = new ArrayList<>(w1.length*w2.length*w3.length);
            for (String s1 : w1)
                for (String s2 : w2)
                    for (String s3 : w3) {
                        list.add(s1 + ' ' + s2 + ' ' +s3 +", ");

                    }

            Collections.shuffle(list);
            for (String s:list) {
                writer.append(s);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Done");

    }

}
