package analyzer;

import java.awt.Color;
import java.util.ArrayList;

public class Theme {
	private ArrayList<Keyword> keywords;
	private String name;
	private Color color;
        
	public Theme(){
            name="";
            keywords=new ArrayList<Keyword>();
            color=Color.RED;
	}
	public Theme(String themeName){
		name=themeName;
		keywords = new ArrayList<Keyword>();
	}
        public Theme(String themeName, Color col){
		name=themeName;
		keywords = new ArrayList<Keyword>();
                color=col;
	}
	public Theme(String themeName, ArrayList<Keyword> themeWords){
		name=themeName;
		keywords = themeWords;
	}
	public Theme(String themeName, ArrayList<Keyword> themeWords, Color col){
		name=themeName;
		keywords = themeWords;
                color=col;
	}
	public void setName(String newName){
		name=newName;
	}
	public void setColor(int rgb){
            color = new Color(rgb);
        }
        public void setKeywords(ArrayList<Keyword> keys){
            this.keywords=keys;
        }
	public String getName(){
		return name;
	}
        public int getTotalOccurs(){
            int toReturn=0;
            
            for(Keyword k: keywords)
                toReturn+=k.getNumOccurs();
            
            return toReturn;
        }
        public String getStringOfKeywords(){
            String toReturn="";
            for(int i=0;i<keywords.size();i++){
                //This if-else combination is here to avaoid an unnecessary comma
                //after the last keyword
                if(i==keywords.size()-1)
                    toReturn+=keywords.get(i).getName();
                else
                    toReturn+=keywords.get(i).getName()+",";
            }
            return toReturn;
        }
        public Color getColor(){
            return color;
        }
        public String getHexColor(){
            String toReturn= Integer.toHexString(color.getRGB()).substring(2);
            return toReturn;
        }
	public ArrayList<Keyword> getKeywords(){
		return keywords;
	}
	public void addKeyword(Keyword k){
		keywords.add(k);
	}
        @Override
        public String toString(){
            return this.name;
        }
        public String getStringForFile(){
            return name+";"+getStringOfKeywords()+";"+color.getRGB();
        }
}
