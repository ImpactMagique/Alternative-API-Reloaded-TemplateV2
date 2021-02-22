package fr.impact.launcherlearn;

import java.io.IOException;
import java.util.HashMap;

import fr.trxyy.alternative.alternative_api.GameEngine;
import fr.trxyy.alternative.alternative_api.GameForge;
import fr.trxyy.alternative.alternative_api.GameStyle;
import fr.trxyy.alternative.alternative_api.account.AccountType;
import fr.trxyy.alternative.alternative_api.auth.GameAuth;
import fr.trxyy.alternative.alternative_api.utils.FontLoader;
import fr.trxyy.alternative.alternative_api.utils.config.LauncherConfig;
import fr.trxyy.alternative.alternative_api_ui.LauncherAlert;
import fr.trxyy.alternative.alternative_api_ui.LauncherBackground;
import fr.trxyy.alternative.alternative_api_ui.LauncherPane;
import fr.trxyy.alternative.alternative_api_ui.base.IScreen;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherButton;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherImage;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherLabel;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherPasswordField;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherRectangle;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherTextField;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginPanel extends IScreen {
	/** LOGIN */
	private LauncherTextField usernameField;
	private LauncherPasswordField passwordField;
	private LauncherButton loginButton;
	/** UPDATE */
	public Timeline timeline;

	/** USERNAME SAVER, CONFIG SAVER */
	public LauncherConfig config;
	/** GAMEENGINE REQUIRED */
	private static GameEngine theGameEngine;
	
	// Se souvenir de moi
	private LauncherButton rememberMe;
	private LauncherImage rememberMeImage;
	private LauncherImage rememberMeImageOff;
	
	// Top Rectangle
	private LauncherRectangle topRectangle;
	private LauncherLabel titleLabel;
	
	// Boutons
	private LauncherButton closeButton;
	private LauncherButton minimizeButton;
	
	// GameAuth pour recupérer la session
	public static GameAuth auth;
	private boolean canPass = false;
	
	// Image
	public static LauncherImage avatar;

	public LoginPanel(Pane root, GameEngine engine) throws IOException {
	
		
		// Déselectionne la textfield par défaut
	    Platform.runLater( () -> root.requestFocus());
	   
	    
		LoginPanel.theGameEngine = engine;
		
		
		/** ===================== CONFIGURATION UTILISATEUR ===================== */
		this.config = new LauncherConfig(engine);
		this.config.loadConfiguration();
		/** ===================== CASE PSEUDONYME ===================== */
		this.usernameField = new LauncherTextField(root);
		this.usernameField.setText((String) this.config.getValue("username"));
		this.usernameField.setPosition(theGameEngine.getWidth() / 2 - 135, theGameEngine.getHeight() / 2 - 57);
		this.usernameField.setSize(270, 50);
		this.usernameField.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 14F));
		this.usernameField.setStyle("-fx-background-color: rgba(0, 0, 0, 0); -fx-text-fill: white; -fx-border-width: 4; -fx-border-color: #D3D3D3; -fx-border-style: hidden hidden solid hidden;");
		this.usernameField.setVoidText("Pseudo/Mail");
		/** ===================== CASE MOT DE PASSE ===================== */
		this.passwordField = new LauncherPasswordField(root);
		this.passwordField.setPosition(theGameEngine.getWidth() / 2 - 135, theGameEngine.getHeight() / 2);
		this.passwordField.setSize(270, 50);
		this.passwordField.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 14F));
		this.passwordField.setStyle("-fx-background-color: rgba(0, 0, 0, 0); -fx-text-fill: white; -fx-border-width: 4; -fx-border-color: #D3D3D3; -fx-border-style: hidden hidden solid hidden;");
		this.passwordField.setVoidText("Mot de passe (vide = crack)");
		if((boolean) config.getValue("rememberme")) 
		{
			passwordField.setText((String) config.getValue("password"));
		} 
		else 
		{
			passwordField.setText("");
		} 
		
		/*// Test Lib Alwyn
		MinecraftServerPingInfos data = new MinecraftServerPing().getPing(new MinecraftServerPingOptions().setHostname("").setPort(25565));
		this.hypixelStats = new LauncherLabel(root);
		this.hypixelStats.setText("Hypixel est en version " + data.getVersion().getName() + " il y a une latence de " + data.getLatency() + " ms avec la France, il y a " + data.getPlayers().getOnline() + " de connectés, son motd est : " + data.getDescription());
		this.hypixelStats.setPosition(20, 50);
		this.hypixelStats.setVisible(true);*/
		
		//
		
		/** ===================== RECTANGLE NOIR EN HAUT ===================== */
		this.topRectangle = new LauncherRectangle(root, 0, 0, theGameEngine.getWidth(), 31);
		this.topRectangle.setFill(Color.rgb(5, 5, 13, 0.25));
		this.topRectangle.setOpacity(1.0);
		/** ===================== TEXTE EN HAUT MILIEU ===================== */
		this.titleLabel = new LauncherLabel(root);
		this.titleLabel.setText("LauncherLearn | V2");
		this.titleLabel.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 18F));
		this.titleLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
		this.titleLabel.setPosition(theGameEngine.getWidth() / 2 - 80, -4);
		this.titleLabel.setOpacity(0.7);
		this.titleLabel.setSize(500, 40);
		
		/** ===================== BOUTON CLOSE / MINIMIZE ===================== */
		this.closeButton = new LauncherButton(root);
		this.closeButton.setPosition(engine.getWidth() - 40, engine.getHeight() - 602);
		LauncherImage closeButtonImage = new LauncherImage(root);
		closeButtonImage.setImage(getResourceLocation().loadImage(engine, "close.png"));
		closeButtonImage.setSize(20, 20);
		this.closeButton.setSize(30, 30);
		this.closeButton.setStyle("-fx-border-color: transparent; -fx-border-width: 0; -fx-background-radius: 0;-fx-background-color: transparent;");		
		this.closeButton.setGraphic(closeButtonImage);
		
		this.closeButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});
		
		this.minimizeButton = new LauncherButton(root);
		this.minimizeButton.setPosition(engine.getWidth() - 80, engine.getHeight() - 602);
		LauncherImage minimizeButtonImage = new LauncherImage(root);
		minimizeButtonImage.setImage(getResourceLocation().loadImage(engine, "minimize.png"));
		minimizeButtonImage.setSize(20, 20);
		this.minimizeButton.setSize(30, 30);
		this.minimizeButton.setStyle("-fx-border-color: transparent; -fx-border-width: 0; -fx-background-radius: 0;-fx-background-color: transparent;");		
		this.minimizeButton.setGraphic(minimizeButtonImage);
		this.minimizeButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Stage stage = (Stage) ((LauncherButton) event.getSource()).getScene().getWindow();
				stage.setIconified(true);
			}
		});
		

		
		/** ===================== BOUTON DE CONNEXION ===================== */
		this.loginButton = new LauncherButton(root);
		this.loginButton.setText("Connexion");
		this.loginButton.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 22F));
		this.loginButton.setPosition(theGameEngine.getWidth() / 2 - 100, theGameEngine.getHeight() / 2 + 70);
		this.loginButton.setSize(200, 45);
		this.loginButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4); -fx-text-fill: white;; -fx-border-width: 4; -fx-border-color: white;");
		this.loginButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
	
				config.updateValue("username", usernameField.getText());
				if((boolean) config.getValue("rememberme")) 
				{
					config.updateValue("password", passwordField.getText());
				} 
				else 
				{
					config.updateValue("password", "");
				} 
				
				/**
				 * ===================== AUTHENTIFICATION OFFLINE (CRACK) =====================
				 */
				if (usernameField.getText().length() < 3) {
					new LauncherAlert("Authentification echouee",
							"Il y a un probleme lors de la tentative de connexion: Le pseudonyme doit comprendre au moins 3 caracteres.");
				} else if (usernameField.getText().length() > 3 && passwordField.getText().isEmpty()) {
					LoginPanel.auth = new GameAuth(usernameField.getText(), passwordField.getText(), AccountType.OFFLINE);
					if (LoginPanel.auth.isLogged()) {
						canPass = true;
						connectAccountCrack(root);
					}
				}
				/** ===================== AUTHENTIFICATION OFFICIELLE ===================== */
				else if (usernameField.getText().length() > 3 && !passwordField.getText().isEmpty()) {
					LoginPanel.auth = new GameAuth(usernameField.getText(), passwordField.getText(), AccountType.MOJANG);
					if (LoginPanel.auth.isLogged()) {
						// Connexion au compte
								canPass = true;
								connectAccountPremium(LoginPanel.auth.getSession().getUsername(), root);
					} else {
						canPass = false;
						new LauncherAlert("Authentification echouee!",
								"Impossible de se connecter, l'authentification semble etre une authentification 'en-ligne'"
										+ " \nIl y a un probleme lors de la tentative de connexion. \n\n-Verifiez que le pseudonyme comprenne au minimum 3 caracteres. (compte non migrer)"
										+ "\n-Faites bien attention aux majuscules et minuscules. \nAssurez-vous d'utiliser un compte Mojang.");
					}
				} else {
					canPass = false;
					new LauncherAlert("Authentification echouee!",
							"Impossible de se connecter, l'authentification semble etre une authentification 'hors-ligne'"
									+ " \nIl y a un probleme lors de la tentative de connexion. \n\n-Verifiez que le pseudonyme comprenne au minimum 3 caracteres.");
				}
				
				try {
					createContentLaunch(engine);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				// Passage au LaunchPanel
				if(canPass) 
				{
					try {
						changePanel(root, engine);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
		
		
		
		
		/** ===================== BOUTON SE SOUVENIR ===================== */
		this.rememberMe = new LauncherButton(root);
		this.rememberMe.setOpacity(1.0);
		rememberMeImage = new LauncherImage(root, getResourceLocation().loadImage(engine, "lockpad.png"));
		rememberMeImageOff = new LauncherImage(root, getResourceLocation().loadImage(engine, "lockpadoff.png"));
		rememberMeImage.setSize(70, 60);
		rememberMeImage.setStyle("-fx-background-radius: 30;\r\n" + "-fx-border-radius: 30;\r\n" + "-fx-border-width:5;");
		rememberMeImageOff.setSize(70, 60);
		rememberMeImageOff.setStyle("-fx-background-radius: 30;\r\n" + "-fx-border-radius: 30;\r\n" + "-fx-border-width:5;");
		if( (boolean) config.getValue("rememberme")) 
		{
			this.rememberMe.setGraphic(rememberMeImage);
			this.rememberMeImageOff.setVisible(false);
		} else 
		{
			this.rememberMe.setGraphic(rememberMeImageOff);
			this.rememberMeImage.setVisible(false);
		} 
		this.rememberMe.setStyle("-fx-border-color: transparent; -fx-border-width: 0; -fx-background-radius: 0;-fx-background-color: transparent;");		
		this.rememberMe.setLayoutX(engine.getWidth() / 2 - 30);
		this.rememberMe.setLayoutY(engine.getHeight() / 2 + 150);
		this.rememberMe.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				HashMap<String, String> configMap = new HashMap<String, String>();
				if(rememberMe.getGraphic().equals(rememberMeImage)) 
				{
					rememberMe.setGraphic(rememberMeImageOff); 
					rememberMeImage.setVisible(false);
					rememberMeImageOff.setVisible(true);
					rememberMeImageOff.setPosition((int) rememberMe.getLayoutX(), (int) rememberMe.getLayoutY());
					configMap.put("rememberme", "" + false);
				} else if(rememberMe.getGraphic().equals(rememberMeImageOff)) 
				{
					rememberMeImageOff.setVisible(false);
					rememberMeImage.setVisible(true);
					rememberMe.setGraphic(rememberMeImage); 
					configMap.put("rememberme", "" + true);
				}
				config.updateValues(configMap);
			}
		});
		
		}
	
	private void changePanel(Pane root, GameEngine engine) throws IOException 
	{
		loginButton.setVisible(false);
		loginButton.setDisable(true);
		usernameField.setVisible(false);
		usernameField.setDisable(true);
		rememberMe.setVisible(false);
		rememberMe.setDisable(true);
		passwordField.setVisible(false);
		passwordField.setDisable(true);
		
		new LaunchPanel(root, engine, LoginPanel.auth);
	}
	
	public static void connectAccountCrack(Pane root)
	{
		LoginPanel.avatar = new LauncherImage(root, new Image("https://minotar.net/avatar/MHF_Steve.png"));
		avatar.setSize(60, 60);
		avatar.setPosition(LoginPanel.theGameEngine.getWidth() / 2 - 30, LoginPanel.theGameEngine.getHeight() / 2 - 50);
	}
	
	public static void connectAccountPremium(String username, Pane root) 
	{
		LoginPanel.avatar = new LauncherImage(root, new Image("https://minotar.net/avatar/" + username + ".png"));
		LoginPanel.avatar.setSize(60, 60);
		LoginPanel.avatar.setPosition(LoginPanel.theGameEngine.getWidth() / 2 - 30, LoginPanel.theGameEngine.getHeight() / 2 - 50);
	}
	
	private Parent createContentLaunch(GameEngine engine) throws IOException 
	{
		LauncherPane contentPaneLogin = new LauncherPane(engine);
		new LauncherBackground(engine, getResourceLocation().getMedia(engine, "background.mp4"), contentPaneLogin);
		return contentPaneLogin;
	}
	

	public LauncherTextField getUsernameField() {
		return usernameField;
	}

	public LauncherPasswordField getPasswordField() {
		return passwordField;
	}

}