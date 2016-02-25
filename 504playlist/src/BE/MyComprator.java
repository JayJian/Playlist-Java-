package BE;

import java.util.Comparator;

public class MyComprator implements Comparator<Object>{
	public int compare(Object arg0, Object arg1) {
    	Playlist t1=(Playlist)arg0;
    	Playlist t2=(Playlist)arg1;
    	if(t1.popularityOfPlaylist>t2.popularityOfPlaylist)
    		return -1;
    	else if(t1.popularityOfPlaylist==t2.popularityOfPlaylist)
    		return 0;
    	else
    		return 1;
	}

}
