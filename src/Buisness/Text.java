package Buisness;

import java.awt.*;

public class Text {
    int id;
    String text;
    private Font font;

    public void setId(int id){
        this.id=id;
    }
    public Text(int i,String s){
        text=s;
        id=i;
    }
    public Text(){

    }
    public int getId(){
        return id;
    }
    public void setText(String t){
        text=t;
    }
    public String getText(){
        return text;
    }
    public void setFont(Font f){
        font=f;
    }
    public Font getFont(){
        return font;
    }
}
