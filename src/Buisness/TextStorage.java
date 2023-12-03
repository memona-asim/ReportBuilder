package Buisness;

import DataAccess.TextDBDAO;

import java.util.ArrayList;
import java.util.List;

public class TextStorage {
    List<Text> texts;
    public TextStorage(){
        texts=new ArrayList<>();
    }
    void loadTexts(){
        texts= TextDBDAO.loadText();
    }

    public List<Text> getTextList() {
        loadTexts();
        return texts;
    }
}
