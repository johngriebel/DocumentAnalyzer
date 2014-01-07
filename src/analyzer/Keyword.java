package analyzer;

import java.util.ArrayList;

public class Keyword {
	private String name;
	private ArrayList<Occurrence> occurrs;
	
	public Keyword(){
		name="";
		occurrs=new ArrayList<Occurrence>();
	}
	public Keyword(String newName){
		name=newName;
		occurrs=new ArrayList<Occurrence>();
	}
	public String getName(){
		return name;
	}
        public int getNumOccurs(){
            return occurrs.size();
        }
	public ArrayList<Occurrence> getOccurrs(){
		return occurrs;
	}
	public void addOccurrence(String file, String sentence, int location){
		occurrs.add(new Occurrence(file,name,sentence,location,occurrs.size()));
	}
        @Override
        public String toString(){
            return this.name;
        }
}
