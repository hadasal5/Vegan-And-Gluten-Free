
package com.Project.veganandglutenfree;

public class Recipe {
    public String user_name,name,type,image;


    public Recipe() {

    }

    public Recipe(String name,String type,String user_name) {
        this.name = name;
        this.type = type;
        this.image = image;
        this.user_name = user_name;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}