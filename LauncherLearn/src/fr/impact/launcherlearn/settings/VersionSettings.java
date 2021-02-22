package fr.impact.launcherlearn.settings;

import java.util.HashMap;

import fr.impact.launcherlearn.LaunchPanel;
import fr.impact.launcherlearn.LauncherMain;
import fr.trxyy.alternative.alternative_api.GameEngine;
import fr.trxyy.alternative.alternative_api.GameForge;
import fr.trxyy.alternative.alternative_api.GameStyle;
import fr.trxyy.alternative.alternative_api.utils.FontLoader;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherButton;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherLabel;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherRectangle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class VersionSettings {
	
	private LauncherLabel titleLabel;	
	private LauncherLabel subTitleLabel;
	private LauncherLabel subTitleLabel2;

	private LauncherRectangle rect;

	private LauncherButton saveButton;
	private ComboBox<String> versionList;
	private CheckBox useForge;
	
	public VersionSettings(final Pane root, final GameEngine engine, final LaunchPanel pane) {
		//this.drawBackgroundImage(engine, root, "background.png");
		pane.config.loadConfiguration();
		
		/** ===================== RECT 1.7.10 ===================== */
		this.rect = new LauncherRectangle(engine.getWidth() - 40, 220);
		this.rect.setLayoutX(20);
		this.rect.setLayoutY(200);
		this.rect.setArcHeight(10.0d);
		this.rect.setArcWidth(10.0d);
		this.rect.setColor(Color.PINK);
		this.rect.setOpacity(0.6d);
		root.getChildren().add(rect);
				
		/** ===================== LABEL VERSION ===================== */
		this.titleLabel = new LauncherLabel(root);
		this.titleLabel.setText("Version");
		this.titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
		this.titleLabel.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 20F));
		this.titleLabel.setPosition(engine.getWidth() / 2 - 350, (engine.getHeight() / 2) - 100);
		
		this.subTitleLabel = new LauncherLabel(root);
		this.subTitleLabel.setText("Modifier la version lancée par le launcher.");
		this.subTitleLabel.setStyle("-fx-text-fill: white;");
		this.subTitleLabel.setFont(FontLoader.loadFont("Arial.ttf", "Comfortaa", 17F));
		this.subTitleLabel.setPosition(engine.getWidth() / 2 - 350, (engine.getHeight() / 2) - 70);
		
		this.subTitleLabel2 = new LauncherLabel(root);
		this.subTitleLabel2.setText("Choisissez une version dans la liste.\nVous pouvez aussi faire démarrer Minecraft en version Forge.");
		this.subTitleLabel2.setStyle("-fx-text-fill: white;");
		this.subTitleLabel2.setFont(FontLoader.loadFont("Arial.ttf", "Comfortaa", 17F));
		this.subTitleLabel2.setPosition(engine.getWidth() / 2 - 420, (engine.getHeight() / 2) - 30);
		/** ===================== MC VERSION LIST ===================== */
		this.versionList = new ComboBox<String>();
		this.populateVersionList();
		if (pane.config.getValue("version") != null) {
			this.versionList.setValue((String) pane.config.getValue("version"));
		}
		this.versionList.setPrefSize(100, 20);
		this.versionList.setLayoutX(engine.getWidth() / 2 - 400);
		this.versionList.setLayoutY((engine.getHeight() / 2) + 30);
		this.versionList.setVisibleRowCount(10);
		this.versionList.setStyle("-fx-background-color: #B04008; -fx-text-box-border: transparent; -fx-text-alignment: center; -fx-background-radius: 18 18 18 18; -fx-border-radius: 18 18 18 18;");
		this.versionList.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				pane.config.updateValue("version", versionList.getValue());
			}
		});
		root.getChildren().add(this.versionList);
	
		/** ===================== CHECKBOX USE Forge ===================== */
		this.useForge = new CheckBox();
		this.useForge.setText("Lancer avec Forge");
		this.useForge.setSelected((boolean) pane.config.getValue("useforge"));
		this.useForge.setOpacity(1.0);
		this.useForge.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 14F));
		this.useForge.setStyle("-fx-text-fill: white;");
		this.useForge.setLayoutX(engine.getWidth() / 2 - 410);
		this.useForge.setLayoutY((engine.getHeight() / 2) + 70);
		
		this.useForge.setOnMouseEntered(new EventHandler<Event>() {@Override public void handle(Event event) {root.getScene().setCursor(Cursor.CLOSED_HAND);}});
		this.useForge.setOnMouseExited(new EventHandler<Event>() {@Override public void handle(Event event) {root.getScene().setCursor(Cursor.DEFAULT);}});
		
		root.getChildren().add(useForge);
		
		/** ===================== BOUTON DE VALIDATION ===================== */
		this.saveButton = new LauncherButton(root);
		this.saveButton.setText("Valider");
		this.saveButton.setStyle("-fx-background-color: rgba(53, 89, 119, 0.4); -fx-text-fill: white;");
		this.saveButton.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 16F));
		this.saveButton.setPosition(engine.getHeight() / 2 + 350 , (engine.getWidth() / 2) - 180);
		this.saveButton.setSize(130, 35);
		this.saveButton.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				HashMap<String, String> configMap = new HashMap<String, String>();
				configMap.put("version", "" + versionList.getValue());
				configMap.put("useforge", "" + useForge.isSelected());
				pane.config.updateValues(configMap);
				engine.getGameLinks().JSON_NAME = (String) pane.config.getValue("version") + ".json";
				engine.getGameLinks().JSON_URL = engine.getGameLinks().BASE_URL + engine.getGameLinks().JSON_NAME;		
				engine.reg(engine.getGameLinks());
				engine.reg(engine.getGameStyle());
				
				if(useForge.isSelected())
				{
					switch(engine.getGameLinks().JSON_NAME)
					{
						case "1.7.10.json":
							engine.setGameStyle(GameStyle.FORGE_1_7_10_OLD);
							LauncherMain.gameForge = null;
							break;
						case "1.8.9.json":
							LauncherMain.gameForge = null;
						case "1.12.2.json":
							engine.setGameStyle(GameStyle.FORGE_1_8_TO_1_12_2);
							break;
						case "1.15.2.json":
							engine.setGameStyle(GameStyle.FORGE_1_13_HIGHER);
							LauncherMain.gameForge = new GameForge("fmlclient", "31.1.0", "1.15.2", "net.minecraftforge", "20200122.131323");
							break;
						case "1.16.2.json":
							engine.setGameStyle(GameStyle.FORGE_1_13_HIGHER);
							LauncherMain.gameForge = new GameForge("fmlclient", "33.0.61", "1.16.2", "net.minecraftforge", "20200812.004259");
							break;
						case "1.16.3.json":
							engine.setGameStyle(GameStyle.FORGE_1_13_HIGHER);
							LauncherMain.gameForge = new GameForge("fmlclient", "34.1.42", "1.16.3", "net.minecraftforge", "20201025.185957");
							break;
					}
						engine.reg(LauncherMain.gameForge);
				} else {
					engine.setGameStyle(GameStyle.VANILLA);
				}
				
				hideElements();
				new LauncherSettings(root, engine, pane);
			}
		});
	}
	
	private void hideElements()
	{
		this.titleLabel.setVisible(false);
		this.saveButton.setVisible(false);
		this.saveButton.setDisable(true);
		this.useForge.setVisible(false);
		this.useForge.setDisable(true);
		this.versionList.setVisible(false);
		this.versionList.setDisable(true);
		this.subTitleLabel.setVisible(false);
		this.subTitleLabel2.setVisible(false);
		this.rect.setVisible(false);
	}
	
	private void populateVersionList() 
	{
		for(int i = 1; i < versionList.getVisibleRowCount(); i++) 
		{
			switch(i) 
			{
				case 1:
					this.versionList.getItems().add("1.7.10");
					break;
				case 2:
					this.versionList.getItems().add("1.8.8");
					break;
				case 3:
					this.versionList.getItems().add("1.8.9");
					break;
				case 4:
					this.versionList.getItems().add("1.12.2");
					break;
				case 5:
					this.versionList.getItems().add("1.15.2");
					break;
				case 6:
					this.versionList.getItems().add("1.16.1");
					break;
				case 7:
					this.versionList.getItems().add("1.16.2");
					break;
				case 8:
					this.versionList.getItems().add("1.16.3");
					break;
				case 9:
					this.versionList.getItems().add("1.16.4");
					break;
			}
		}		
	}

	
}

