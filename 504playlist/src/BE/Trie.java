package BE;

public class Trie {
	private Vertex root;//һ��Trie����һ�����ڵ�

    //�ڲ���
    protected class Vertex{//�ڵ���
	    
    	protected int words;
		protected int[] songs;
		protected int prefixes;
		protected Vertex[] edges;//ÿ���ڵ����27���ӽڵ�(����Ϊ����)
	        
		Vertex(){
			words = 0;
	        songs = new int[3200];
	        prefixes = 0;
	        edges = new Vertex[128];
	        for (int i = 0; i < edges.length; i++) 
	        	edges[i] = null;
		}
    }
    
    public Trie() {
        root = new Vertex();
    }
    
    public void AddWord(String word,int indexOfSong) {
        AddWord(root, word,indexOfSong);
    }
    
	private void AddWord(Vertex vertex, String word,int indexOfSong) {
		int index=0;
	    if (word.length() == 0)  //if all characters of the word has been added
	    {vertex.words ++;
    	vertex.songs[vertex.prefixes]=vertex.songs[0];
    	vertex.songs[0]=indexOfSong;
    	vertex.prefixes ++;} 
	    else {	    	 
	         char c = word.charAt(0);
	         c = Character.toLowerCase(c);
	         index = c - ' ';	             
	          if (vertex.edges[index] == null){ //if the edge does NOT exist
	              vertex.edges[index] = new Vertex();
	          }
	       	vertex.songs[vertex.prefixes]=indexOfSong;  
	       	vertex.prefixes ++;
			AddWord(vertex.edges[index], word.substring(1),indexOfSong); //go the the next character
	    }
	}
    
	public int[] SearchSong(String wordSegment) {
		return SearchSong(root, wordSegment);
	}
	
	private int[] SearchSong(Vertex vertex, String wordSegment){
		int index=0;
		int[] noResult=new int[1];
		if (wordSegment.length() == 0) {
			return vertex.songs;
		}
		else{
			char c = wordSegment.charAt(0);
			c = Character.toLowerCase(c);
	        index = c - ' ';
	        if (vertex.edges[index] == null)
	        	 return noResult;
	        else
	        	 return SearchSong(vertex.edges[index], wordSegment.substring(1));
		}
}
    
}