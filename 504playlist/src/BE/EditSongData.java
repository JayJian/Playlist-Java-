package BE;

import java.io.*;
import java.util.*;


public class EditSongData {

	static Song AllSongs[] = new Song[3168];
	//The index of a Song in the Array of All Songs, this index for this Song will never be changed		
	static int indexOfASongInTheArray = 0;
	
	
	//fileName_SongList: The file name and path of the text file storing the info of all the songs
	public static Song[] ParseAllSongs(String fileName_SongList){
		
		File txtAllSongs = new File(fileName_SongList);   //Import the txt file
		BufferedReader txtLineReader = null;   //Read the txt by line
		String curLine = null;
		
		try {
			txtLineReader = new BufferedReader(new FileReader(txtAllSongs));
			/* The method *.readLine() will go on reading by lines by itself! 
			   It does not need a lineCounter++ to move to the next line! */
			while((curLine = txtLineReader.readLine())!= null){
				
				//split the string by "Tabs" ("/t"), repeatedly! And place the elements in an Array of Strings
				String InfoOfOneSong[] = curLine.split("\t");
				
				/* Apart from defining the Array: "Song AllSongs[]",
				 we still need to redefine each of the elements in this Array! */ 
				AllSongs[indexOfASongInTheArray] = new Song(); //Attention!!!
				AllSongs[indexOfASongInTheArray].indexOfSong = indexOfASongInTheArray + 1;
				AllSongs[indexOfASongInTheArray].songName = InfoOfOneSong[1];	
				indexOfASongInTheArray++;
			}
		}catch (IOException e) {
	        e.printStackTrace();
	    }		

		return AllSongs;
	}
	
	
	public static Song[] mostPopularSongs(int[] songindex, Song[] songpre){
		int i,j;
		int[] index=new int[4];
		Song[] top4Songs=new Song[4];
		for(j=0;j<4;j++){
			top4Songs[j]=new Song();
			top4Songs[j]=songpre[songindex[0]-1];
			for(i=1;songindex[i]!=0;i++){
				if(top4Songs[j].popularityOfSong<=songpre[songindex[i]-1].popularityOfSong){
					top4Songs[j]=songpre[songindex[i]-1];
					index[j]=songindex[i]-1;
				}				
			}
			songpre[index[j]].popularityOfSong=-songpre[index[j]].popularityOfSong;
		}
		for(j=0;j<4;j++)
			songpre[index[j]].popularityOfSong=-songpre[index[j]].popularityOfSong;
		for(j=1;j<4;j++){
			if(top4Songs[j].indexOfSong==top4Songs[j-1].indexOfSong){
				for(i=j;i<4;i++)
					top4Songs[i]=null;
				break;
			}
		}
		return top4Songs;
	}
	
	
	
}

