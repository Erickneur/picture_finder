package com.uhnux.picturefinder.util;

import com.uhnux.picturefinder.MainActivity;

public class GeneralControl {

    private static GeneralControl instance;

    public static GeneralControl getInstance() {
        if (instance == null)
            instance = new GeneralControl();
        return instance;
    }

    private MainActivity activity;

    private GeneralControl(){
        setActivity(null);
    }

    public void reset(){
        setActivity(null);
    }

    public void showProgress(boolean isVisible){
        getActivity().showProgress(isVisible);
    }

    public MainActivity getActivity() {
        return activity;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }
}
