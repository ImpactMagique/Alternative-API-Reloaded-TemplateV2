package fr.impact.launcherlearn;

import java.io.IOException;

import fr.trxyy.alternative.alternative_api.GameEngine;
import fr.trxyy.alternative.alternative_api.GameFolder;
import fr.trxyy.alternative.alternative_api.GameForge;
import fr.trxyy.alternative.alternative_api.GameLinks;
import fr.trxyy.alternative.alternative_api.GameStyle;
import fr.trxyy.alternative.alternative_api.LauncherPreferences;
import fr.trxyy.alternative.alternative_api_ui.LauncherBackground;
import fr.trxyy.alternative.alternative_api_ui.LauncherPane;
import fr.trxyy.alternative.alternative_api_ui.base.AlternativeBase;
import fr.trxyy.alternative.alternative_api_ui.base.LauncherBase;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;



public class LauncherMain extends AlternativeBase{

	private GameFolder gameFolder = new GameFolder("launcherlearn");
	private LauncherPreferences launcherPreferences = new LauncherPreferences("LauncherLearn", 950, 600, true);
	private static GameLinks gameLinks = new GameLinks("https://www.launcherlearn.nhx.fr/", "1.16.9.json");
	private GameEngine gameEngine = new GameEngine(gameFolder, gameLinks, launcherPreferences, GameStyle.OPTIFINE);
	public static GameForge gameForge;
	private static Media media;
	private static MediaPlayer mediaPlayer;
	private static Stage primaryStage;
	private static LauncherMain instance;
	
	
	private void playMusic(Media media, String path) 
	{
		
		media = getResourceLocation().getMedia(this.gameEngine, path);
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
	}

	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		instance = this;
		LauncherMain.primaryStage = primaryStage;
	 //   this.playMusic(media, "arouf.wav");
		Scene scene = new Scene(createContent());
		this.gameEngine.reg(primaryStage);
		this.gameEngine.reg(gameForge);
		LauncherBase launcherBase = new LauncherBase(primaryStage, scene, StageStyle.UNDECORATED, gameEngine);
		launcherBase.setIconImage(primaryStage, getResourceLocation().loadImage(gameEngine, "favicon.png"));
	
	}

	private Parent createContent() throws IOException 
	{
		LauncherPane contentPane = new LauncherPane(gameEngine);	
		new LauncherBackground(gameEngine, getResourceLocation().getMedia(gameEngine, "background.mp4"), contentPane);
		LoginPanel panel = new LoginPanel(contentPane, gameEngine);
		readVersion(panel);
		return contentPane;		
	}
	
	public static void main(String[] args) 
	{
		Application.launch(args);
	}
	

	
	private void readVersion(LoginPanel panel)
	{
		switch((String) panel.config.getValue("version")) 
		{
			case "1.7.10":
				gameLinks.JSON_URL = gameLinks.BASE_URL + "1.7.10.json";
				break;
			case "1.8.8":
				gameLinks.JSON_URL = gameLinks.BASE_URL + "1.8.8.json";				
				break;
			case "1.8.9":
				gameLinks.JSON_URL = gameLinks.BASE_URL + "1.8.9.json";				
				break;
			case "1.12.2":
				gameLinks.JSON_URL = gameLinks.BASE_URL + "1.12.2.json";				
				break;
			case "1.15.2":
				gameLinks.JSON_URL = gameLinks.BASE_URL + "1.15.2.json";
				break;
			case "1.16.1":
				gameLinks.JSON_URL = gameLinks.BASE_URL + "1.16.1.json";	
			case "1.16.2":
				gameLinks.JSON_URL = gameLinks.BASE_URL + "1.16.2.json";	
			case "1.16.3":
				gameLinks.JSON_URL = gameLinks.BASE_URL + "1.16.3.json";				
				break;
			case "1.16.4":
				gameLinks.JSON_URL = gameLinks.BASE_URL + "1.16.4.json";				
				break;
			default: 
				panel.config.updateValue("version", gameLinks.getJsonName().replace(".json", ""));
				break;
		}
	}
	
	public static LauncherMain getInstance() {return LauncherMain.instance;}
	public static Stage getPrimaryStage() {return LauncherMain.primaryStage;}
	public static void setPrimaryStage(Stage stage) {LauncherMain.primaryStage = stage;}
	public static void muteMusic() {LauncherMain.getMediaPlayer().setMute(true);}
	public static void resumeMusic() {LauncherMain.getMediaPlayer().setMute(false);}
	public static Media getMedia() {return media;}
	public static MediaPlayer getMediaPlayer() {return mediaPlayer;}
	public static GameLinks getGameLinks() {return gameLinks;}
	

}
