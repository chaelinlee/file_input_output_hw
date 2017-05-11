package com.example.leechaelin.file_io_hw;

/**
 * Created by leechaelin on 2017. 5. 11..
 */

public class titlename {
    String title;

    @Override
    public String toString() {
        return title;
    }

    public titlename(String title){
        this.title=title;
    }
    public String getTitlename(){
        return title;
    }

}
