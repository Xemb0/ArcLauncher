package com.launcher.myapplication;



import android.graphics.drawable.Drawable;

public class AppObject {
    private String  name,
            packageName;
    private Drawable image;
//    private Boolean isAppInDrawer = null;

    public AppObject(String packageName, String name, Drawable image, boolean b){
        this.name = name;
        this.packageName = packageName;
        this.image = image;
    }

    public String getPackageName(){return packageName;}
    public String getName(){return name;}
    public Drawable getImage(){return image;}
//    public Boolean getIsAppInDrawer(){return isAppInDrawer;}

    public void setPackageName(String packageName){
        this.packageName = packageName;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setImage(Drawable image){
        this.image = image;
    }


}
