package studiplayer.ui;
import java.io.File;
import java.lang.reflect.Array;
import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import studiplayer.audio.AudioFile;
import studiplayer.audio.NotPlayableException;
import studiplayer.audio.PlayList;
import studiplayer.audio.SortCriterion;
import studiplayer.audio.TaggedFile;


public class Player extends Application {
	
	public static final String DEFAULT_PLAYLIST = "playlists/DefaultPlayList.m3u";
	private static final String PLAYLIST_DIRECTORY = "playlists";
	private static final String INITIAL_PLAY_TIME_LABEL = "00:00";
	private static final String NO_CURRENT_SONG = "-";
	
	private PlayList playList = new PlayList();
	private boolean useCertPlayList = false;
	private SongTable table;
	private PlayerThread playerThread;
	private TimerThread timerThread;
	private AudioFile currentSong;
	
	private Button playButton;
	private Button pauseButton;
	private Button stopButton; 
	private Button nextButton;
	private Button filterButton;
	private Button titledPaneButton;
	
	private Label playListLabel = new Label();
	private Label currentSongLabel = new Label();
	private Label playTimeLabel = new Label();
	
	private ChoiceBox<SortCriterion> sortChoiceBox = new ChoiceBox<>();
	private TextField searchTextField = new TextField();
	
    public Player() {
    	playList = new PlayList();
    	currentSong = playList.currentAudioFile();
    }
	
	public void setUseCertPlayList(boolean value) {
		this.useCertPlayList = value;
	}
	
	public void setPlayList(String pathname) {
		try {
			playList.loadFromM3U(pathname);
			currentSong = playList.currentAudioFile();
		} catch (NotPlayableException e) {
			// Auto-generated catch block
			e.printStackTrace();
		} 
		table.refreshSongs();
	}
	
	public void loadPlayList(String pathname) {
		if(pathname == null || pathname.isEmpty()) {
			setPlayList(DEFAULT_PLAYLIST);
			table.refreshSongs();
		} else {
			setPlayList(pathname);
			table.refreshSongs();
		}
	}
	
	private Node topBorderPane() {
		GridPane searchNsort = new GridPane();
		Label search = new Label("Search");
		Label sort = new Label("Sort");
		
		// Choice Box -> add sortCriterion 
		sortChoiceBox.getItems().addAll(SortCriterion.values());
		sortChoiceBox.setValue(SortCriterion.DEFAULT);
		sortChoiceBox.setPrefWidth(200);		// length
		// in the beginning nothing selected
		sortChoiceBox.getSelectionModel().clearSelection();		
		
		// add to GridPane
		searchNsort.add(search, 0, 0);
		searchNsort.add(sort, 0, 1);
		searchNsort.add(sortChoiceBox, 1, 1);
		searchNsort.add(searchTextField, 1, 0);
		filterButton = new Button("Show");
		// add action to showButton
		filterButton.setOnAction(e -> {
			playList.setSearch(searchTextField.getText());
			playList.setSortCriterion(sortChoiceBox.getValue());
			table.refreshSongs();
		});
		searchNsort.add(filterButton, 2, 1);
		
		// space between cells
		searchNsort.setHgap(30);
		searchNsort.setVgap(5);
		searchNsort.setPadding(new Insets(10)); // space to frame
		
		//TitledPane
		titledPaneButton = new Button();
		TitledPane filter = new TitledPane("Filter", titledPaneButton);
		
		// add searchNsort and TitlePane to VBox 
		filter.setContent(searchNsort);
		
		return filter;
	}
	
	// predefined
	private Button createButton(String iconfile) {
		Button button = null;
		try {
			URL url = getClass().getResource("/icons/" + iconfile);
			Image icon = new Image(url.toString());
			ImageView imageView = new ImageView(icon);
			imageView.setFitHeight(20);
			imageView.setFitWidth(20);
			button = new Button("", imageView);
			button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			button.setStyle("-fx-background-color: #fff;");
		} catch (Exception e) {
			System.out.println("Image " + "icons/"
			+ iconfile + " not found!");
			System.exit(-1);
		}
		return button;
	}
	
	// Buttons in HBox
	public Node buttonBox() {
		HBox box = new HBox();
		
		// create Buttons mit Methode
		playButton = createButton("play.jpg");
		nextButton = createButton("next.jpg");
		pauseButton = createButton("pause.jpg");
		stopButton = createButton("stop.jpg");
		
		// add buttons to box
		box.getChildren().addAll(playButton, pauseButton, stopButton, nextButton);
		box.setAlignment(Pos.CENTER); // zentriert buttons
		box.setPadding(new Insets(10)); // abstand zum rand
		return box;
	}
	
	// Labels 
	public Node currentSongInfoBox() {
		GridPane currentSongInfo = new GridPane();
		
		// create Labels
		Label currentPlayList = new Label("Playlist");
		playListLabel.setText(PLAYLIST_DIRECTORY);
		Label currentSong = new Label("Song");
		currentSongLabel.setText(NO_CURRENT_SONG);
		Label playTime = new Label("Playtime");
		playTimeLabel.setText(INITIAL_PLAY_TIME_LABEL);
		
		// add to GridPane
		currentSongInfo.add(currentPlayList, 0, 0);
		currentSongInfo.add(playListLabel, 1, 0);
		currentSongInfo.add(currentSong, 0, 1);
		currentSongInfo.add(currentSongLabel, 1, 1);
		currentSongInfo.add(playTime, 0, 2);
		currentSongInfo.add(playTimeLabel, 1, 2);
		
		// space between cells
		currentSongInfo.setHgap(30);
		currentSongInfo.setVgap(5);
		
		currentSongInfo.setPadding(new Insets(5)); // abstand zum rand
		
		return currentSongInfo;
	}
	
	public Node bottomBorderPane() {
		VBox bottomBox = new VBox();
		
		// add Labels und Buttons
		bottomBox.getChildren().addAll(currentSongInfoBox(), buttonBox());		
		return bottomBox;
	}
	
    public void start(Stage stage) throws Exception {
        // "Bühnentitel"
    	stage.setTitle("APA Player");
    	table = new SongTable(playList);

        BorderPane pane = new BorderPane();

        // choose File in Directory
        if(!useCertPlayList) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Playlist auswählen");
			// set initial directory -> methode
			String directoryString = "C:\\Users\\laura\\git\\AudioPlayer\\va10\\playlists";
			File directory = new File(directoryString);
			fileChooser.setInitialDirectory(directory);
			
			// Extension filtered -> m3u
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("m3u", "*.m3u"));
			File selectedFile = fileChooser.showOpenDialog(stage);
			loadPlayList(selectedFile.getAbsolutePath());

        }
        
        // Top
        // TitlePane
        // Search and Sort
        pane.setTop(topBorderPane());
        
        // Center 
        // Song Table 
        pane.setCenter(table);
        
        // Bottom - Labels und Buttons
        pane.setBottom(bottomBorderPane());
		// defaulte
        // Dis/Able Buttons
		playButton.setDisable(false);
		pauseButton.setDisable(true);
		stopButton.setDisable(true);
		nextButton.setDisable(false);  
        
		table.setRowSelectionHandler(event -> {
			if (!table.getSelectionModel().isEmpty()) {
				AudioFile temp = table.getSelectionModel().getSelectedItem().getAudioFile();
				if (currentSong != null) {
					currentSong.stop();								
					playList.jumpToAudioFile(temp);
					table.selectSong(temp);
				}
			}
		});
        
        // Buttons Events
        playButton.setOnAction(e -> {
        	table.selectSong(currentSong);
        	playCurrentSong();
        });

        pauseButton.setOnAction(e -> {
        	pauseCurrentSong();
        });
        
        stopButton.setOnAction(e -> {
        	stopCurrentSong();
        });
        
        nextButton.setOnAction(e -> {
        	nextSong();
        });
        
        // Fenster mit Struktur "pane" und Größe 600 * 400
        Scene scene = new Scene(pane, 600, 400);
        stage.setScene(scene);

        // zeigt "stage" (Bühne)
        stage.show();
    }

	private void updateSongInfo(AudioFile file) {
    	Platform.runLater(() -> {
    		if (file == null) {
    			playListLabel.setText(DEFAULT_PLAYLIST);
    			playTimeLabel.setText(INITIAL_PLAY_TIME_LABEL);
    			currentSongLabel.setText(NO_CURRENT_SONG);
    		} else {
    			playListLabel.setText(file.getPathname());
    			playTimeLabel.setText(file.formatPosition());
    			currentSongLabel.setText(file.toString());			
    		}
    	});    	
    }

    // Methods for Button Events
    // play Button
    private void playCurrentSong() {
    	currentSong = playList.currentAudioFile();
    	System.out.println(playList.currentAudioFile());
    	// Song info
    	updateSongInfo(currentSong);	
    	if (currentSong != null) {
    		// start Threads
    		playerThread = new PlayerThread();
    		playerThread.start();
    		timerThread = new TimerThread();
    		timerThread.start();
    		
    	}
    	// Dis/Able Buttons
    	playButton.setDisable(true);
    	pauseButton.setDisable(false);
    	stopButton.setDisable(false);
    	nextButton.setDisable(false);    
	}
    
    private void pauseCurrentSong() {
    	// play -> pause
    	// check if timerThread is alive
    	if(timerThread.isAlive()) {
    		timerThread.terminate();
    	// pause -> play
    	} else {
    		timerThread = new TimerThread();
    		timerThread.start();
    	}
    	
    	currentSong.togglePause();
    		
    	// Dis/Able Buttons
    	playButton.setDisable(true);
    	pauseButton.setDisable(false);
    	stopButton.setDisable(false);
    	nextButton.setDisable(false);
    }
    
    private void stopCurrentSong() {
    	// Dis/Able Buttons
    	playButton.setDisable(false);
    	pauseButton.setDisable(true);
    	stopButton.setDisable(true);
    	nextButton.setDisable(false);    
    	
    	// stop Threads
    	playerThread.terminate();
    	timerThread.terminate();
    	
    	playList.currentAudioFile().stop();
    	
		// reset Playtime
		updateSongInfo(null);
    }
    
    private void nextSong() {
    	playerThread.terminate();
    	timerThread.terminate();
    	playList.currentAudioFile().stop();
    	playList.nextSong();
		playCurrentSong();
    }

	public static void main(String[] args) {
        launch();
    }
	
	public class PlayerThread extends Thread {
		private boolean stopped = false;
		
		public void run() {			
			while(!this.stopped) {
				try {
					currentSong = playList.currentAudioFile();
					currentSong.play();
	        	} catch(NotPlayableException e) {
	        		e.printStackTrace();
	        	}
				
				if(!this.stopped) {
					playList.nextSong();
					updateSongInfo(playList.currentAudioFile());
				}
			}
		}
		
		public void terminate() {
			this.stopped = true;
		}
	}
	
	public class TimerThread extends Thread {
		private boolean stopped = false;
		int counter = 0;
		
		public void run() {
			while(!this.stopped) {
				Platform.runLater(() -> {
					currentSong = playList.currentAudioFile();
					updateSongInfo(currentSong);
				});
				
				try {
					TimerThread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void terminate() {
			this.stopped = true;
		}
	}

}