package BE;

import java.io.*;
import java.util.*;


public class EditPlaylistData {

	public static Playlist AllPlaylists[] = new Playlist[6000];
	//The index of a Playlist in the Array of All Playlists, this index for this Playlist will never be changed
	public static int numOfPlaylistsProcessedCumulatively = 0;
	public static int numOfExistingPlaylistsInTheArray = 0;

	
	
	//fileName_PlaylistDayX: The file name and path of the text file storing the Playlists of dayX, ex: day01
	public static void ParsePlayListsOfOneDay(String fileName_PlaylistDayX){
				
		File txtPlayLists = new File(fileName_PlaylistDayX);   //Import the txt file
		BufferedReader txtLineReader = null;   //Read the txt by line
		String curLine = null;		

		int indexOfAPlaylistInTheTxtFileDayX = 0; 

		
		try {
			txtLineReader = new BufferedReader(new FileReader(txtPlayLists));
			/* The method *.readLine() will go on reading by lines by itself! 
			   It does not need a lineCounter++ to move to the next line! */
			while((curLine = txtLineReader.readLine())!= null){				
				
				/* split the string by "spaces", repeatedly! And place the elements in an Array of Strings
				    (1) The 1st to (length-1)th elements are the indexes of songs included in this Playlist
				    (2) The last element was the parameter "Popularity" of the Playlist, with a prefix "Tab" */				
				String infoOfAPlaylist[] = curLine.split(" ");
				
				//Define an Array of int to store the indexes of the songs
				int IndexesOfSongs[] = new int[infoOfAPlaylist.length-1];
				for(int i=0; i<=infoOfAPlaylist.length-2; i++) {
					IndexesOfSongs[i] = Integer.parseInt(infoOfAPlaylist[i]);	
					//Println for testing
					//System.out.println(IndexesOfSongs[i]);
				}
		
				//Get the "Popularity" of this Playlist
				int popOfPlaylist = Integer.parseInt
					(infoOfAPlaylist[infoOfAPlaylist.length - 1].replaceAll("\t", ""));				
				//Println for testing
				//System.out.println(popOfPlaylist);

				//=============== Log the data parsed above into the Arrays of Playlists and Songs ===============
				
				/* Apart from defining the Array: "Playlist AllPlaylists[]",
				 we still need to redefine each of the elements in this Array! */
				AllPlaylists[numOfExistingPlaylistsInTheArray + indexOfAPlaylistInTheTxtFileDayX] 
					= new Playlist(); //Attention!!!
				
				AllPlaylists[numOfExistingPlaylistsInTheArray + indexOfAPlaylistInTheTxtFileDayX].indexOfPlaylist 
					= numOfPlaylistsProcessedCumulatively;
				//System.out.println(AllPlaylists[indexOfAPlaylistInTheTxtFileDayX].indexOfPlaylist);
				
				AllPlaylists[numOfExistingPlaylistsInTheArray + indexOfAPlaylistInTheTxtFileDayX].popularityOfPlaylist
					= popOfPlaylist;
				
				AllPlaylists[numOfExistingPlaylistsInTheArray + indexOfAPlaylistInTheTxtFileDayX].includedSong
					= IndexesOfSongs;
				//for (int j=0; j<IndexesOfSongs.length; j++) {
					//System.out.println(AllPlaylists[numOfExistingPlaylitsInTheArray + indexOfAPlaylistInTheTxtFileDayX].includedSong[j]);
				

				for (int j=0; j<IndexesOfSongs.length; j++){
					
					/* Increase the popularities of the Songs included
					 in the Playlist which has just been added into the Array of all Playlists */
					EditSongData.AllSongs[IndexesOfSongs[j]].popularityOfSong += popOfPlaylist;
					
					/* Update the value of the "Most Popular Playlist" of the Songs included
					 in this Playlist */
					if (EditSongData.AllSongs[IndexesOfSongs[j]].mostPopularPlaylist==null) {
						EditSongData.AllSongs[IndexesOfSongs[j]].mostPopularPlaylist = 
								AllPlaylists[numOfExistingPlaylistsInTheArray + indexOfAPlaylistInTheTxtFileDayX];	
					} else {
						int formerPlaylistPopValue = EditSongData.AllSongs[IndexesOfSongs[j]].
								mostPopularPlaylist.popularityOfPlaylist;
						if (formerPlaylistPopValue<=popOfPlaylist)
								EditSongData.AllSongs[IndexesOfSongs[j]].mostPopularPlaylist = 
									AllPlaylists[numOfExistingPlaylistsInTheArray + indexOfAPlaylistInTheTxtFileDayX];				
					}
				}				
				
				//Initialize the name of a Playlist, i.e. make the heading of its name as: "Playlist #: "
				AllPlaylists[numOfExistingPlaylistsInTheArray + indexOfAPlaylistInTheTxtFileDayX].nameOfPlaylist = 
					"Playlist " + numOfPlaylistsProcessedCumulatively + ": ";
				for (int j=0; j<IndexesOfSongs.length; j++) {
					AllPlaylists[numOfExistingPlaylistsInTheArray + indexOfAPlaylistInTheTxtFileDayX].nameOfPlaylist
						+= EditSongData.AllSongs[IndexesOfSongs[j]].songName + "; "; }
				//System.out.println(AllPlaylists[indexOfAPlaylistInTheTxtFileDayX].nameOfPlaylist);
				
				indexOfAPlaylistInTheTxtFileDayX ++;		
				
				numOfPlaylistsProcessedCumulatively ++;
				
			} //The End of "While curLine is not NULL"
			
		}catch (IOException e) {
	        e.printStackTrace();
	    }		
		
		numOfExistingPlaylistsInTheArray += indexOfAPlaylistInTheTxtFileDayX;
		
		//Printlns for testing
		//System.out.println("indexes (newly added) of this day: " + indexOfAPlaylistInTheTxtFileDayX);
		//System.out.println("existing in the array: " + numOfExistingPlaylistsInTheArray);
		//System.out.println("cumulative in all times: " + numOfPlaylistsProcessedCumulatively);
		
		
		/* ========================= Sort the Playlists =========================
		 We need to sort all the Playlists according to their Popularities
		 everytime after we added a bunch of Playlists into the Array of all Playlists */
		
		AllPlaylists=SortPlaylistsInTheArray();
		
		
		/* ==================== Arrange the Exceeded Playlists ==================== 
		 If the number of existing Playlists in the Array of all the Playlists exceeds 1024
		 after we input the series of Playlists contained in the txt file dayX.txt and sorted them,
		 we will have to remove the Playlists ranking after 1024, whose Popularities are smaller */
		
		RemoveExceedingPlaylists();
			
	}	
	

	public static void ParseOnePlayList(String inputIndexesOfSongs, int popOfPlaylist){
		
		String infoOfAPlaylist[] = inputIndexesOfSongs.split(" ");
		
		//Define an Array of int to store the indexes of the songs
		int IndexesOfSongs[] = new int[infoOfAPlaylist.length-1];
		for(int i=0; i<=infoOfAPlaylist.length-2; i++)
			IndexesOfSongs[i] = Integer.parseInt(infoOfAPlaylist[i]);
		

		//=============== Log the data parsed above into the Arrays of Playlists and Songs ===============

		AllPlaylists[numOfExistingPlaylistsInTheArray] 
			= new Playlist(); //Attention!!!
		
		AllPlaylists[numOfExistingPlaylistsInTheArray].indexOfPlaylist 
			= numOfPlaylistsProcessedCumulatively;
		
		AllPlaylists[numOfExistingPlaylistsInTheArray].popularityOfPlaylist
			= popOfPlaylist;
		
		AllPlaylists[numOfExistingPlaylistsInTheArray].includedSong
			= IndexesOfSongs;


		for (int j=0; j<IndexesOfSongs.length; j++){
			
			/* Increase the popularities of the Songs included
			 in the Playlist which has just been added into the Array of all Playlists */
			EditSongData.AllSongs[IndexesOfSongs[j]].popularityOfSong += popOfPlaylist;
			
			/* Update the value of the "Most Popular Playlist" of the Songs included
			 in this Playlist */
			if (EditSongData.AllSongs[IndexesOfSongs[j]].mostPopularPlaylist==null) {
				EditSongData.AllSongs[IndexesOfSongs[j]].mostPopularPlaylist = 
						AllPlaylists[numOfExistingPlaylistsInTheArray];	
			} else {
				int formerPlaylistPopValue = EditSongData.AllSongs[IndexesOfSongs[j]].
						mostPopularPlaylist.popularityOfPlaylist;
				if (formerPlaylistPopValue<=popOfPlaylist)
						EditSongData.AllSongs[IndexesOfSongs[j]].mostPopularPlaylist = 
							AllPlaylists[numOfExistingPlaylistsInTheArray];				
			}
		}
		

		//Initialize the name of a Playlist, i.e. make the heading of its name as: "Playlist #: "
		AllPlaylists[numOfExistingPlaylistsInTheArray].nameOfPlaylist = 
			"Playlist " + numOfPlaylistsProcessedCumulatively + ": ";
		for (int j=0; j<IndexesOfSongs.length; j++) {
			AllPlaylists[numOfExistingPlaylistsInTheArray].nameOfPlaylist
				+= EditSongData.AllSongs[IndexesOfSongs[j]].songName + "; "; }
		
		
		numOfPlaylistsProcessedCumulatively ++;		
		
		numOfExistingPlaylistsInTheArray ++;
		
		
		// ========================= Sort the Playlists =========================
		AllPlaylists=SortPlaylistsInTheArray();
		
		
		// ==================== Arrange the Exceeded Playlists ====================
		RemoveExceedingPlaylists();
			
	}	
	
	
	
	//Sort the Playlists in the Array of all the Playlists, according to the Playlists' Popularities
	public static Playlist[] SortPlaylistsInTheArray(){
		Playlist[] existingPlaylist=Arrays.copyOf(AllPlaylists, numOfExistingPlaylistsInTheArray);
		Arrays.sort(existingPlaylist,new MyComprator());
		return Arrays.copyOf(existingPlaylist,6000);		
	}
	
	
	
	// Arrange the Exceeded Playlists
	public static void RemoveExceedingPlaylists(){
		
		if (numOfExistingPlaylistsInTheArray > 1024){
			for (int i=1024; i<=numOfExistingPlaylistsInTheArray-2; i++){
				
				/* Decrease the popularities of the Songs included
				   in the Playlist which would be removed from the Array of all Playlists */
				for (int j=0; j<AllPlaylists[i].includedSong.length; j++){
					EditSongData.AllSongs[AllPlaylists[i].includedSong[j]].popularityOfSong 
						-= AllPlaylists[i].popularityOfPlaylist; 
					if(EditSongData.AllSongs[AllPlaylists[i].includedSong[j]].mostPopularPlaylist==AllPlaylists[i])
						EditSongData.AllSongs[AllPlaylists[i].includedSong[j]].mostPopularPlaylist=null;
					}
				
				// Remove the Playlists whose indexes in the Array are >= 1024
				AllPlaylists[i]	= new Playlist(); 			
			}
			
			//At last, reset the number of existing Playlists in the Array to be 1024
			numOfExistingPlaylistsInTheArray = 1024;
		}
	}

}

