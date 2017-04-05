package com.androidpay;

import com.androidpay.bean.PhotosBean;

import java.util.List;

/**
 * Created by Anuj Sharma on 3/17/2017.
 */

public class OptimizedPrograms {
    private OptimizedPrograms(){

    }
    private static OptimizedPrograms instance;
    public static synchronized OptimizedPrograms getInstance(){
        return (instance==null)?instance = new OptimizedPrograms():instance;
    }

    /**
     * Sample program of Hackers Earth
     * @param input
     */
    protected void evaluateProgram(int input){
        int totalValuesArr[] = new int[input];
        for(int i=0;i<input;i++){
            totalValuesArr[i] = i+1;
        }
        for(int i=0;i<totalValuesArr.length;i++){
            if(totalValuesArr[i]%15==0){
                System.out.println("FizzBuzz");
            }else if(totalValuesArr[i]%3==0){
                System.out.println("Fizz");
            }else if(totalValuesArr[i]%5==0){
                System.out.println("Buzz");
            }else{
                System.out.println(totalValuesArr[i]);
            }
        }
    }

    /**
     * Fibonnaci series program
     * @param input
     */
    protected void fibonnaciSeries(int input){
        int temp =0, x = 0, y = 1; ;
        System.out.println("Checking value-> " + input);
        if(input<0){
            System.out.println("Need positive value always");
            return;
        }
        else if(input==0){
            System.out.println("0");
        }
        else{
            System.out.print(x + "" + y);
            for(int i=2;i<=input;i++){
                temp = x + y;
                System.out.print(temp);
                x = y;
                y = temp;
            }
        }
    }

    /**
     * Check Prime Number
     * @param input
     */
    protected void checkPrimeNumber(int input){
        int flag = 0;
        for(int i=2;i<input;i++){
            if(input%i == 0){
                flag = 1;
                break;
            }
        }
        if(flag == 1){
            System.out.println("Its not a prime number");
        }else{
            System.out.println("It is a prime number");
        }
    }

    /**
     *
     * @param list list to be sorted
     * @param sortType //sortType = 0 for available count
        //sortType = 1 for price
        //sortType = 2 for title
     */
    protected void sortUsingBubbleSort(List<PhotosBean> list, int sortType){
        //sortType = 0 for available count
        //sortType = 1 for price
        //sortType = 2 for title
        PhotosBean temp;
        for (PhotosBean obj:list
                ) {
            if(sortType==0)
                System.out.println("Item Count before sorting-> " + obj.availableCount);
            else if(sortType ==1)
                System.out.println("Item Count before sorting-> " + obj.price);
            else if(sortType ==2)
                System.out.println("Item Count before sorting-> " + obj.title);
        }
        for(int i=0;i<list.size();i++){
            for(int j=(list.size()-1);j>i;j--){
                if(sortType == 0 && list.get(j).availableCount < list.get(j-1).availableCount){
                    temp = list.get(j);
                    list.set(j,list.get(j-1));
                    list.set(j-1,temp);
                }else if(sortType == 1 && Integer.parseInt(list.get(j).price) < Integer.parseInt(list.get(j-1).price)){
                    temp = list.get(j);
                    list.set(j,list.get(j-1));
                    list.set(j-1,temp);
                }else if(sortType == 2 && list.get(j).title.compareTo(list.get(j-1).title) == -1){
                    temp = list.get(j);
                    list.set(j,list.get(j-1));
                    list.set(j-1,temp);
                }
            }

        }
        for (PhotosBean obj:list
                ) {
            if(sortType==0)
                System.out.println("Item Count after sorting-> " + obj.availableCount);
            else if(sortType ==1)
                System.out.println("Item Count after sorting-> " + obj.price);
            else if(sortType ==2)
                System.out.println("Item Count after sorting-> " + obj.title);
        }
    }
}
