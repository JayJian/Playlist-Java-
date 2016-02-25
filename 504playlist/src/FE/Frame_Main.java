package FE;

import java.io.*;
import java.awt.Event;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.List;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;



public class Frame_Main {

	protected Shell shell;
	private Text textSongName;
	private Text textPlaylists;
	private Button btnPopLists;
	private TabFolder tabFolder;
	private TabItem tabViewPlaylist;
	private Composite composite_ViewPlaylist;
	private TabItem tabUploadPlaylist;
	private Composite composite_UploadPlaylist;
	private Text textUploadPath;
	private Label lblAddAGroupPlaylists;
	private Label lblAddASinglePlaylist;
	private Text textSongsInPlaylist;
	private Button btnUploadPlaylists;
	private Button btnAddAPlaylist;
	private Text textPopOfPlaylist;
	private Label lblIndexesOfSongs;
	private Label lblPopOfPlaylist;
	private Button btnBrowse;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Frame_Main window = new Frame_Main();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		
		// ===================================== Initialize the BE Data ========================================
		
		// Data of all the Songs
		String txtFileAllSongs = "/Users/jianzhou/Desktop/PlaylistApp-Datasets/song_list.txt";
		BE.Song songlist[]= BE.EditSongData.ParseAllSongs(txtFileAllSongs);
		
		// Data of default pre-loaded Playlists
		String txtFilePlaylistsDayX = "/Users/jianzhou/Desktop/PlaylistApp-Datasets/day01.txt";
		BE.EditPlaylistData.ParsePlayListsOfOneDay(txtFilePlaylistsDayX);
		
		
		BE.EditPlaylistData.AllPlaylists = BE.EditPlaylistData.SortPlaylistsInTheArray();
		
		// The Trie used to store (the letters in) the names of all the Songs
  		BE.Trie trie = new BE.Trie();
  		// Generate the Trie
		for(int i=0;i<songlist.length;i++){
			trie.AddWord(songlist[i].songName, songlist[i].indexOfSong);
		}
		
		
		// ===================================== Frame Configuration ========================================
		
		shell = new Shell();
		shell.setToolTipText("");
		shell.setSize(628, 430);
		shell.setText("Playlist");
		
		tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(0, 10, 620, 393);
		
		
		// ======================================= 1st Tab: View Playlists =========================================
		
		tabViewPlaylist = new TabItem(tabFolder, SWT.NONE);
		tabViewPlaylist.setText("View Playlists");
		
		// ---------------------- The Overall Composite in this Tab ----------------------
		
		composite_ViewPlaylist = new Composite(tabFolder, SWT.NONE);
		tabViewPlaylist.setControl(composite_ViewPlaylist);
				
		Label lblFillInSongName = new Label(composite_ViewPlaylist, SWT.NONE);
		lblFillInSongName.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD));
		lblFillInSongName.setBounds(46, 26, 358, 20);
		lblFillInSongName.setText("Fill in the name of the Song you want to search");
		
		Label lblMostPopPlaylists = new Label(composite_ViewPlaylist, SWT.NONE);
		lblMostPopPlaylists.setText("The 8 most popular Playlists in all records");
		lblMostPopPlaylists.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD));
		lblMostPopPlaylists.setBounds(46, 100, 358, 20);

		textPlaylists = new Text(composite_ViewPlaylist, SWT.BORDER | SWT.MULTI);
		textPlaylists.setLocation(46, 123);
		textPlaylists.setSize(381, 219);
		textPlaylists.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));		
		//When this frame is loaded, we initialize the text of this component
		List8PopPlaylists();
		
		Button btnSearch = new Button(composite_ViewPlaylist, SWT.NONE);
		btnSearch.setLocation(445, 50);
		btnSearch.setSize(121, 29);
		btnSearch.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD));
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				String inputSongName = textSongName.getText();
				int[] songprefix = trie.SearchSong(inputSongName);
	    		int inputSongIndex = songprefix[0]-1;
	    		String inputSongsMostPopPlaylist="";
	    		
	    		if(inputSongIndex<0) {
	    			inputSongsMostPopPlaylist="";
	    		} else {
		    		inputSongsMostPopPlaylist = 
		    				songlist[inputSongIndex].mostPopularPlaylist.nameOfPlaylist;	    			
	    		}
				
				textPlaylists.setText(inputSongsMostPopPlaylist);
				
				lblMostPopPlaylists.setText("Playlists containing this Song");
			}
		});
		btnSearch.setToolTipText("");
		btnSearch.setText("Search");
		
		btnPopLists = new Button(composite_ViewPlaylist, SWT.NONE);
		btnPopLists.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				List8PopPlaylists();				
				
			}
		});
		btnPopLists.setLocation(445, 306);
		btnPopLists.setSize(121, 29);
		btnPopLists.setToolTipText("");
		btnPopLists.setText("Pop Lists");
		btnPopLists.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD));
		
		textSongName = new Text(composite_ViewPlaylist, SWT.BORDER);
		textSongName.setToolTipText("Hints will be displayed below after you input some letters");
		textSongName.setLocation(46, 50);
		textSongName.setSize(381, 29);
		textSongName.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
		// !!! Listening to the change of the text
		textSongName.addModifyListener(new ModifyListener(){
	        public void modifyText(ModifyEvent event) {
	      		
	        	int[] songprefix;
	        	BE.Song[] topSongs=null;
	        	String hintSongNames="";
	        	
	    		songprefix = trie.SearchSong(textSongName.getText());
	    		if(songprefix.length==1 && songprefix[0]==0)
	    			hintSongNames = "";
	    		else {
	    			topSongs = BE.EditSongData.mostPopularSongs(songprefix, songlist);
		    		for (int i=0; i<4; i++)
		    			if(topSongs[i]!=null)
		    				hintSongNames += topSongs[i].songName + "\n";
	    		}

	        	textPlaylists.setText(hintSongNames);
	
	        }
	      });
		
		
		
		// ======================================= 2nd Tab: Upload Playlists =========================================
		
		tabUploadPlaylist = new TabItem(tabFolder, SWT.NONE);
		tabUploadPlaylist.setText("Upload Playlists");
		
		// ---------------------- The Overall Composite in this Tab ----------------------
		
		composite_UploadPlaylist = new Composite(tabFolder, SWT.NONE);
		composite_UploadPlaylist.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		composite_UploadPlaylist.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.ITALIC));
		tabUploadPlaylist.setControl(composite_UploadPlaylist);
		
		textUploadPath = new Text(composite_UploadPlaylist, SWT.BORDER);
		textUploadPath.setToolTipText("The path and file name of the txt file");
		textUploadPath.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.NORMAL));
		textUploadPath.setBounds(45, 53, 268, 29);
		
		lblAddAGroupPlaylists = new Label(composite_UploadPlaylist, SWT.NONE);
		lblAddAGroupPlaylists.setText("Add a group of Playlists by uploading a txt file that contains them");
		lblAddAGroupPlaylists.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD));
		lblAddAGroupPlaylists.setBounds(45, 27, 493, 20);
		
		lblAddASinglePlaylist = new Label(composite_UploadPlaylist, SWT.NONE);
		lblAddASinglePlaylist.setText("Add a single Playlist by inputting its content");
		lblAddASinglePlaylist.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD));
		lblAddASinglePlaylist.setBounds(45, 143, 358, 20);
		
		textSongsInPlaylist = new Text(composite_UploadPlaylist, SWT.BORDER | SWT.MULTI);
		textSongsInPlaylist.setToolTipText("Input a series of Integers divided by spaces \" \"");
		textSongsInPlaylist.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		textSongsInPlaylist.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.NORMAL));
		textSongsInPlaylist.setBounds(45, 188, 518, 91);
		
		textPopOfPlaylist = new Text(composite_UploadPlaylist, SWT.BORDER);
		textPopOfPlaylist.setToolTipText("Fill in an Integer");
		textPopOfPlaylist.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.NORMAL));
		textPopOfPlaylist.setBounds(45, 307, 181, 29);
		
		btnUploadPlaylists = new Button(composite_UploadPlaylist, SWT.NONE);
		btnUploadPlaylists.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				String fileNameAndPathOfPlaylistsDayX = textUploadPath.getText();
				
				BE.EditPlaylistData.ParsePlayListsOfOneDay(fileNameAndPathOfPlaylistsDayX);
			}
		});
		btnUploadPlaylists.setToolTipText("");
		btnUploadPlaylists.setText("Upload these Playlists");
		btnUploadPlaylists.setFont(SWTResourceManager.getFont("Tahoma", 8, SWT.BOLD));
		btnUploadPlaylists.setBounds(427, 53, 136, 29);
		
		btnAddAPlaylist = new Button(composite_UploadPlaylist, SWT.NONE);
		btnAddAPlaylist.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				String inputIndexesOfSongs = textSongsInPlaylist.getText();
				int popOfPlaylist = Integer.parseInt(textPopOfPlaylist.getText());
				
				BE.EditPlaylistData.ParseOnePlayList(inputIndexesOfSongs, popOfPlaylist);
			}
		});
		btnAddAPlaylist.setToolTipText("");
		btnAddAPlaylist.setText("Add this Playlist");
		btnAddAPlaylist.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD));
		btnAddAPlaylist.setBounds(441, 306, 122, 29);
		
		lblIndexesOfSongs = new Label(composite_UploadPlaylist, SWT.NONE);
		lblIndexesOfSongs.setText("Indexes of the Songs contained in this Playlist");
		lblIndexesOfSongs.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
		lblIndexesOfSongs.setBounds(45, 170, 358, 16);
		
		lblPopOfPlaylist = new Label(composite_UploadPlaylist, SWT.NONE);
		lblPopOfPlaylist.setText("Popularity of this Playlist");
		lblPopOfPlaylist.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
		lblPopOfPlaylist.setBounds(45, 289, 358, 16);
		
		btnBrowse = new Button(composite_UploadPlaylist, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
				    "TXT files", "txt");
				chooser.setFileFilter(filter);
				chooser.setCurrentDirectory(null);
				int returnVal = chooser.showOpenDialog(chooser);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
				   textUploadPath.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		btnBrowse.setToolTipText("");
		btnBrowse.setText("Browse");
		btnBrowse.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD));
		btnBrowse.setBounds(329, 53, 85, 29);

	}
	
	public void List8PopPlaylists(){
		String mostPop8Lists = "";
		for (int i=0; i<8; i++){
			mostPop8Lists += 
				"Top"+(i+1)+" - " + BE.EditPlaylistData.AllPlaylists[i].nameOfPlaylist + "\n";
		}
		textPlaylists.setText(mostPop8Lists);
	}
	
}
