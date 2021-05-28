package sample;

public class Card {
    private String[] attributes = new String[4];
    private String path;
    public Card(String att){
        attributes[0] = att.substring(0,1);
        attributes[1] = att.substring(1,2);
        attributes[2] = att.substring(2,3);
        attributes[3] = att.substring(3);
        path = att + ".jpg";
    }
    public String[] getAttributes(){
        return attributes;
    }
    public String getPath(){
        return path;
    }
}
