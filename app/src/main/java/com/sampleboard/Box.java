package com.sampleboard;

import java.util.List;

/**
 * Created by Anuj Sharma on 3/15/2017.
 */

public class Box<T> {
    protected T t;

    protected void setBox(T t){
        this.t = t;
    }
    void getBox(){
        System.out.println("T is " + t);
    }

    class test{
        void testing(){

        }

    }
    public List<T> list(List<T> list){
        return list;
    }
}
