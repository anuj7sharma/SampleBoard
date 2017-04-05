package com.androidpay;

/**
 * Created by Anuj Sharma on 3/16/2017.
 */

public class SingletonTest {
    private static SingletonTest instance;
    private SingletonTest(){

    }
    //double checked thread safe singleton class
    /*public static  SingletonTest getInstance(){
        if(instance==null){
            synchronized (SingletonTest.class){
                if(instance==null)instance = new SingletonTest();
            }
        }
        return instance;
    }*/

    //Thread safe singleton method
    public static synchronized SingletonTest getInstance(){
        if(instance==null){
            instance = new SingletonTest();
        }
        return instance;
    }

    // lazy initialization singleton method
    /*public static SingletonTest getInstance(){
        if(instance==null){
            instance = new SingletonTest();
        }
        return instance;
    }*/


}
