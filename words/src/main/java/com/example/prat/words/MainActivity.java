package com.example.prat.words;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    LinkedHashMap<String, Integer> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get result ahead of time for performance sake
        result = getWordOccurrences();
        result = sortByValue(result);

        Button countButton =  (Button) findViewById(R.id.open_file);

        countButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                LinearLayout container = (LinearLayout) findViewById(R.id.container);
                container.removeAllViews();

                TextView tmp = new TextView(MainActivity.this);
                int start = 1;
                int end = 10;
                Iterator iterator = result.keySet().iterator();

                // Print result in groups in a text view
                tmp.append(start+"-"+end+"\n");
                while(iterator.hasNext()){
                    String key = (String) iterator.next();
                    int value = result.get(key);

                    if(value>=start && value<=end){
                        tmp.append(key + " ," + value + "\n");
                    } else if(value>end) {
                        start+=10;
                        end+=10;
                        tmp.append("\n"+start+"-"+end+"\n");
                        tmp.append(key + " ," + value + "\n");
                        continue;
                    }
                }

                container.addView(tmp);
            }
        });
    }

    // Count occurrences of all words in the source string
    protected LinkedHashMap<String, Integer> getWordOccurrences(){
        LinkedHashMap<String, Integer> count = new LinkedHashMap<>();

        try {
            // Read file in the package
            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("words.txt")));

            String source = new String();
            String line;

            while((line = br.readLine())!=null){
                source+=line;
            }

            // Count word occurrences
            source = source.replaceAll(","," ");
            source = source.replaceAll("\\."," ");
            String[] words = source.split("\\s+");

            for(String w: words){
                Integer c;
                if( (c = count.get(w)) != null) c++;
                else c=1;
                count.put(w,c);
            }

            // Close streams
            br.close();
        } catch(Exception e) { e.getCause(); }
        return count;
    }

    static LinkedHashMap sortByValue(Map<String, Integer> map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        LinkedHashMap result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
