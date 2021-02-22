package fr.impact.launcherlearn;

import java.io.IOException;
import java.text.DecimalFormat;

import fr.impact.launcherlearn.settings.LauncherSettings;
import fr.trxyy.alternative.alternative_api.GameEngine;
import fr.trxyy.alternative.alternative_api.GameForge;
import fr.trxyy.alternative.alternative_api.GameStyle;
import fr.trxyy.alternative.alternative_api.auth.GameAuth;
import fr.trxyy.alternative.alternative_api.updater.GameUpdater;
import fr.trxyy.alternative.alternative_api.utils.FontLoader;
import fr.trxyy.alternative.alternative_api.utils.config.LauncherConfig;
import fr.trxyy.alternative.alternative_api_ui.LauncherPane;
import fr.trxyy.alternative.alternative_api_ui.base.IScreen;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherButton;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherImage;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherLabel;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherProgressBar;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherRectangle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import re.alwyn974.minecraftserverping.MinecraftServerPing;
import re.alwyn974.minecraftserverping.MinecraftServerPingInfos;
import re.alwyn974.minecraftserverping.MinecraftServerPingOptions;
public class LaunchPanel extends IScreen {
	/** LOGIN */
	private LauncherButton loginButton;
	private LauncherButton settingsButton;
	
	LauncherImage loginButtonImage;
	LauncherImage settingsButtonImage;

	/** UPDATE */
	public Timeline timeline;
	private DecimalFormat decimalFormat = new DecimalFormat(".#");
	private Thread updateThread;
	private GameUpdater updater = new GameUpdater();
	private LauncherRectangle updateRectangle;
	private LauncherLabel updateLabel;
	private LauncherLabel currentFileLabel;
	private LauncherLabel percentageLabel;
	private LauncherLabel currentStep;
	private LauncherLabel hypixelStats;
	/** USERNAME SAVER, CONFIG SAVER */
	public LauncherConfig config;
	/** PROGRESS BAR */
	public LauncherProgressBar bar;
	/** GAMEENGINE REQUIRED */
	private GameEngine theGameEngine;

	/** MUSIC 
	private LauncherButton muteMusic;
	private LauncherButton resumeMusic;
	*/
	
	// Label
	private LauncherLabel welcomeLabel;

	public LaunchPanel(Pane root, GameEngine engine, GameAuth auth) throws IOException {
		this.theGameEngine = engine;
		/** ===================== CONFIGURATION UTILISATEUR ===================== */
		this.config = new LauncherConfig(engine);
		this.config.loadConfiguration();
		
		MinecraftServerPingInfos data = new MinecraftServerPing().getPing(new MinecraftServerPingOptions().setHostname("play.hypixel.net").setPort(25565));
		this.hypixelStats = new LauncherLabel(root);
		this.hypixelStats.setText("Hypixel est en version " + data.getVersion().getName() + " il y a une latence de " + data.getLatency() + " ms avec la France, il y a " + data.getPlayers().getOnline() + " de connectés, son motd est : " + data.getDescription());
		this.hypixelStats.setPosition(20, 50);
		this.hypixelStats.setStyle("-fx-text-fill: red;");
		this.hypixelStats.setVisible(true);
		
	
		
		/** ===================== BOUTON DE CONNEXION ===================== */
		this.loginButton = new LauncherButton(root);
		loginButtonImage = new LauncherImage(root);
		loginButtonImage.setImage(getResourceLocation().loadImage(this.theGameEngine, "play.png"));
		loginButtonImage.setSize(200, 200);
		this.loginButton.setGraphic(loginButtonImage);
		this.loginButton.setPosition(theGameEngine.getWidth() / 2 - 50, theGameEngine.getHeight() / 2);
		this.loginButton.setStyle("-fx-border-color: transparent; -fx-border-width: 0; -fx-background-radius: 0;-fx-background-color: transparent;");		
		this.loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				if((boolean) config.getValue("useforge"))
				{
					LauncherMain.getGameLinks().JSON_NAME = config.getValue("version") + ".json";
					
					switch(engine.getGameLinks().JSON_NAME)
					{
						case "1.7.10.json":
							engine.setGameStyle(GameStyle.FORGE_1_7_10_OLD);
							break;
						case "1.8.9.json":
						case "1.12.2.json":
							engine.setGameStyle(GameStyle.FORGE_1_8_TO_1_12_2);
							break;
						case "1.15.2.json":
							engine.setGameStyle(GameStyle.OPTIFINE);
							LauncherMain.gameForge = new GameForge("fmlclient", "31.2.45", "1.15.2", "net.minecraft.launchwrapper.Launch", "20200515.085601");
							break;
						case "1.16.2.json":
							engine.setGameStyle(GameStyle.OPTIFINE);
							LauncherMain.gameForge = new GameForge("fmlclient", "33.0.61", "1.16.2", "net.minecraft.launchwrapper.Launch", "20200812.004259");
							break;
						case "1.16.3.json":
							System.out.println("Test");
							engine.setGameStyle(GameStyle.OPTIFINE);
							LauncherMain.gameForge = new GameForge("fmlclient", "34.1.42", "1.16.3", "net.minecraft.launchwrapper.Launch", "20201025.185957");
							break;
					}
				} else {
					engine.setGameStyle(GameStyle.VANILLA);
				}
				
				
				update(auth);		
			}
		});
				
		/** ===================== BOUTON PARAMETRES ===================== */
		this.settingsButton = new LauncherButton(root);
		settingsButtonImage = new LauncherImage(root,
				getResourceLocation().loadImage(theGameEngine, "settings.png"));
		settingsButtonImage.setSize(85, 85);
		this.settingsButton.setStyle("-fx-border-color: transparent; -fx-border-width: 0; -fx-background-radius: 0;-fx-background-color: transparent;");		
		this.settingsButton.setGraphic(settingsButtonImage);
		this.settingsButton.setPosition(theGameEngine.getWidth() / 2 - 135, theGameEngine.getHeight() / 2 + 56);
		this.settingsButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					createSettingsPanel();
					changePanel(root, engine);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		
		/** MUSIC
			this.muteMusic = new LauncherButton(root);
			this.muteMusic.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4); -fx-text-fill: white;");
			LauncherImage muteMusicImage = new LauncherImage(root,
					getResourceLocation().loadImage(theGameEngine, "mute.png"));
			imageButton.setSize(27, 27);
			this.muteMusic.setGraphic(muteMusicImage);
			this.muteMusic.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 14F));
			this.muteMusic.setPosition(theGameEngine.getWidth() / 2 + 180, theGameEngine.getHeight() / 2 + 60);
			this.muteMusic.setSize(30, 22);
			this.muteMusic.setOnAction(new EventHandler<ActionEvent>() {
	
				@Override
				public void handle(ActionEvent event) {
					muteMusic.setVisible(false);
					resumeMusic.setVisible(true);
					LauncherMain.muteMusic();
				}
			});
			
			this.resumeMusic = new LauncherButton(root);
			this.resumeMusic.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4); -fx-text-fill: white;");
			LauncherImage resumeMusicImage = new LauncherImage(root,
					getResourceLocation().loadImage(theGameEngine, "resume.png"));
			imageButton.setSize(27, 27);
			this.resumeMusic.setGraphic(resumeMusicImage);
			this.resumeMusic.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 14F));
			this.resumeMusic.setPosition(theGameEngine.getWidth() / 2 + 180, theGameEngine.getHeight() / 2 + 60);
			this.resumeMusic.setSize(30, 22);
			this.resumeMusic.setVisible(false);
			this.resumeMusic.setOnAction(new EventHandler<ActionEvent>() {
	
				@Override
				public void handle(ActionEvent event) {
					resumeMusic.setVisible(false);
					muteMusic.setVisible(true);
					LauncherMain.resumeMusic();
				}
			});
			
			*/
			
			/**
			 * ============================== WELCOME LABEL ==============================
			 **/
			this.welcomeLabel = new LauncherLabel(root);
			this.welcomeLabel.setText("Bienvenue " + auth.getSession().getUsername() + " !");
			this.welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10.0f));
			this.welcomeLabel.setAlignment(Pos.CENTER);
			this.welcomeLabel.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 22F));
			this.welcomeLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
			this.welcomeLabel.setPosition(theGameEngine.getWidth() / 2 - 95, theGameEngine.getHeight() / 2 - 100);
			this.welcomeLabel.setOpacity(1);
		
			
		/**
		 * ============================== MISE A JOUR ==============================
		 **/
		this.updateRectangle = new LauncherRectangle(root, theGameEngine.getWidth() / 2 - 175,
				theGameEngine.getHeight() / 2 - 60, 350, 180);
		this.updateRectangle.setArcWidth(10.0);
		this.updateRectangle.setArcHeight(10.0);
		this.updateRectangle.setFill(Color.rgb(0, 0, 0, 0.60));
		this.updateRectangle.setVisible(false);
		/** =============== LABEL TITRE MISE A JOUR =============== **/
		this.updateLabel = new LauncherLabel(root);
		this.updateLabel.setText("- MISE A JOUR -");
		this.updateLabel.setFont(Font.font("FontName", FontWeight.BOLD, 10.0f));
		this.updateLabel.setAlignment(Pos.CENTER);
		this.updateLabel.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 22F));
		this.updateLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
		this.updateLabel.setPosition(theGameEngine.getWidth() / 2 - 95, theGameEngine.getHeight() / 2 - 55);
		this.updateLabel.setOpacity(1);
		this.updateLabel.setSize(190, 40);
		this.updateLabel.setVisible(false);
		/** =============== ETAPE DE MISE A JOUR =============== **/
		this.currentStep = new LauncherLabel(root);
		this.currentStep.setText("Preparation de la mise a jour.");
		this.currentStep.setFont(Font.font("Verdana", FontPosture.ITALIC, 18F)); // FontPosture.ITALIC
		this.currentStep.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
		this.currentStep.setAlignment(Pos.CENTER);
		this.currentStep.setPosition(theGameEngine.getWidth() / 2 - 160, theGameEngine.getHeight() / 2 + 83);
		this.currentStep.setOpacity(0.4);
		this.currentStep.setSize(320, 40);
		this.currentStep.setVisible(false);
		/** =============== FICHIER ACTUEL EN TELECHARGEMENT =============== **/
		this.currentFileLabel = new LauncherLabel(root);
		this.currentFileLabel.setText("launchwrapper-12.0.jar");
		this.currentFileLabel.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 18F));
		this.currentFileLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
		this.currentFileLabel.setAlignment(Pos.CENTER);
		this.currentFileLabel.setPosition(theGameEngine.getWidth() / 2 - 160, theGameEngine.getHeight() / 2 + 25);
		this.currentFileLabel.setOpacity(0.8);
		this.currentFileLabel.setSize(320, 40);
		this.currentFileLabel.setVisible(false);
		/** =============== POURCENTAGE =============== **/
		this.percentageLabel = new LauncherLabel(root);
		this.percentageLabel.setText("0%");
		this.percentageLabel.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 30F));
		this.percentageLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
		this.percentageLabel.setAlignment(Pos.CENTER);
		this.percentageLabel.setPosition(theGameEngine.getWidth() / 2 - 50, theGameEngine.getHeight() / 2 - 5);
		this.percentageLabel.setOpacity(0.8);
		this.percentageLabel.setSize(100, 40);
		this.percentageLabel.setVisible(false);

		this.bar = new LauncherProgressBar(root);
		this.bar.setPosition(theGameEngine.getWidth() / 2 - 125, theGameEngine.getHeight() / 2 + 60);
		this.bar.setSize(250, 20);
		this.bar.setVisible(false);

	}
	
	private void update(GameAuth auth) {
		this.loginButton.setDisable(true);
		this.settingsButton.setDisable(true);
		this.loginButton.setVisible(false);
		this.settingsButton.setVisible(false);
		this.updateRectangle.setVisible(true);
		this.updateLabel.setVisible(true);
		this.currentStep.setVisible(true);
		this.currentFileLabel.setVisible(true);
		this.percentageLabel.setVisible(true);
		this.bar.setVisible(true);
		theGameEngine.getGameLinks().JSON_URL = theGameEngine.getGameLinks().BASE_URL + this.config.getValue("version") + ".json";
		updater.reg(theGameEngine);
		updater.reg(auth.getSession());
		theGameEngine.reg(this.updater);

		this.updateThread = new Thread() {
			public void run() {
				theGameEngine.getGameUpdater().run();
			}
		};
		this.updateThread.start();
		/**
		 * ===================== REFAICHIR LE NOM DU FICHIER, PROGRESSBAR, POURCENTAGE
		 * =====================
		 **/
		this.timeline = new Timeline(
				new KeyFrame[] { new KeyFrame(javafx.util.Duration.seconds(0.0D), new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						timelineUpdate(theGameEngine);
					}
				}, new javafx.animation.KeyValue[0]),
						new KeyFrame(javafx.util.Duration.seconds(0.1D), new javafx.animation.KeyValue[0]) });
		this.timeline.setCycleCount(Animation.INDEFINITE);
		this.timeline.play();
	}

	protected Parent createSettingsPanel() {
		LauncherPane contentPaneSettings = new LauncherPane(theGameEngine);
		return contentPaneSettings;
	}

	public void timelineUpdate(GameEngine engine) {
		if (engine.getGameUpdater().downloadedFiles > 0) {
			this.percentageLabel.setText(decimalFormat.format(
					engine.getGameUpdater().downloadedFiles * 100.0D / engine.getGameUpdater().filesToDownload) + "%");
		}
		this.currentFileLabel.setText(engine.getGameUpdater().getCurrentFile());
		this.currentStep.setText(engine.getGameUpdater().getCurrentInfo());
		double percent = (engine.getGameUpdater().downloadedFiles * 100.0D / engine.getGameUpdater().filesToDownload
				/ 100.0D);
		this.bar.setProgress(percent);
	}
	
	
	private void changePanel(Pane root, GameEngine engine) throws IOException 
	{
		this.loginButton.setVisible(false);
		this.loginButton.setDisable(true);
		this.welcomeLabel.setVisible(false);
		this.welcomeLabel.setDisable(true);
		this.loginButtonImage.setVisible(false);
		this.loginButtonImage.setDisable(true);
		this.settingsButton.setVisible(false);
		this.settingsButton.setDisable(true);
		LoginPanel.avatar.setVisible(false);
		LoginPanel.avatar.setDisable(true);
		
		new LauncherSettings(root, engine, this);
	}
}