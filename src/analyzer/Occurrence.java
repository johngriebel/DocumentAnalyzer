package analyzer;

public class Occurrence {
	private String file, keyword, sentence;
	private int location;
	private int number;
	
	public Occurrence(){
		file="";
                keyword="";
		sentence="";
		location=0;
		number=0;
	}
	public Occurrence(String filename, String key, String newSent, int loc, int num){
		file=filename;
                keyword=key;
		sentence=newSent;
		location=loc;
		number=num;
	}
	public String getFile(){
		return file;
	}
	public String getSentece(){
		return sentence;
	}
	public int getLocation(){
		return location;
	}
	public int getNumber(){
		return number;
	}
        public String getKeyword(){
            return keyword;
        }
	@Override
	public String toString(){
		return "Keyword: "+keyword+"\t File: "+file+"\t Location: "+location+"\t"+sentence;
	}
        public String getOccurInfo(){
            return "Keyword: "+keyword+"\t File: "+file+"\t Location: "+location;
        }
	public String csvString(){
		return (number+1)+","+file+","+(location+1)+","+sentence;
	}
	
}
