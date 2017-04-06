package com.sampleboard;

import com.sampleboard.bean.PhotosBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by Anuj Sharma on 3/15/2017.
 */

public class PrimitiveLesson extends Box {

    List<PhotosBean> list = new ArrayList<>();

    public static void main(String[] args) {
        PrimitiveLesson obj = new PrimitiveLesson();
        obj.addDataToList();
//        OptimizedPrograms.getInstance().sortUsingBubbleSort(obj.list,2);
        /*Iterator iterator = obj.list.iterator();
        if (iterator.hasNext()) {
            System.out.println("Has value");
        } else {
            System.out.println("No more value");
        }*/
        obj.akshatProblem("zbcc abc xab");
        obj.akshatProblem1("zbcc abc xab");
        //get output of following programs
//        OptimizedPrograms.getInstance().evaluateProgram(15);
//        OptimizedPrograms.getInstance().fibonnaciSeries(5);
//        OptimizedPrograms.getInstance().checkPrimeNumber(18);
//        obj.start();
    }
    private void addDataToList() {
        if (list == null) list = new ArrayList<>();
        PhotosBean obj = new PhotosBean();
        obj.price = "7000";
        obj.photoUrl = "";
        obj.availableCount = 54;
        obj.title = "Anuj Info";
        list.add(obj);

        obj = new PhotosBean();
        obj.price = "6000";
        obj.photoUrl = "";
        obj.availableCount = 45;
        obj.title = "Amit Info";
        list.add(obj);
    }

    /**
     *
     * @param inputLine It would be like abc abc xab
     *                  Output should be aaa bbb ccx
     */
    private void akshatProblem(String inputLine){
        String wordsArray[] = inputLine.split("\\s+"); // split words with space
        String totalChars[] = new String[inputLine.length()]; // total characters of line
        for(int i=0;i<inputLine.length();i++){
            if(!String.valueOf(inputLine.charAt(i)).equals("\\s+"))
                totalChars[i] = String.valueOf(inputLine.charAt(i));
        }
        Map<String,Integer> values = new HashMap<>();
        int count = 0;
        for(int i=0;i<totalChars.length;i++){
            //check if unique array empty put zero index value of totalArray
            for(int j=0;j<totalChars.length;j++){
                if(totalChars[i].equals(totalChars[j])){
                    count++;
                }
            }
            //add character to map
            values.put(totalChars[i],count);
            count = 0;
        }
        //retrieve valued of map
        if(values.size()>0){
            Iterator iterator = values.entrySet().iterator();
            //fetch all string from stored hashmap
            StringBuilder outputLine = new StringBuilder();
            while (iterator.hasNext()){
                Map.Entry pair = (Map.Entry)iterator.next();
                    for(int i=0;i<(int)pair.getValue();i++){
                        if(!pair.getKey().equals(" "))
                            outputLine.append(pair.getKey().toString());
                    }


            }
            //sort input line first and then print
            char[] chars = outputLine.toString().toCharArray();
            Arrays.sort(chars);
            String sorted = new String(chars);
            //add space after each word
            StringBuilder stringBuilder = new StringBuilder(sorted);

            int counter = 0;
            for (int i=0;i<wordsArray.length;i++){
                counter = counter + wordsArray[i].length();
                stringBuilder.insert(counter+i, " ");
            }

            System.out.println("Before Computation");
            System.out.println(inputLine);
            //sort Map keys in alphabetical order
            System.out.println("\nAfter Computation");
            System.out.println("Final String-> " + stringBuilder.toString());
        }

    }

    void akshatProblem1(String inputLine){
        String[] words = inputLine.split("//s+");
        String[] totalChars = new String[inputLine.length()];
        for(int i=0;i<inputLine.length();i++){
                if(!String.valueOf(inputLine.charAt(i)).equals(" "))
                    totalChars[i] = String.valueOf(inputLine.charAt(i));
        }
        Character a = 'A';
        Character b = 'a';
        if((int)(a)>(int)b){
            System.out.println("a is greater than b");
        }else{
            System.out.println("b is greater than a");
        }
        //remove white space from array
    }

    void start() {
        boolean b1 = false;
        boolean b2 = changeInfo(b1);
        System.out.println(" b1 " + b1 + "  b2 " + b2);
    }

    boolean changeInfo(boolean b2) {
        b2 = true;
        return b2;
    }


}


