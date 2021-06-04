package model;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
/**
 * \class TropFenetreControleur
 * Controleur de la Fenetre "Limite feuille"
 */
public class TropFenetreControleur
{
	
	   private Stage myPrimaryStage;
	   private AnchorPane AnchorPanel2;
	   private Button BoutonClose;

	
	public void setMyPrimaryStage(Stage primaryStage) 
	{
		/**
		 * On récupere le stage de cet affichage
		 */
	      this.myPrimaryStage = primaryStage; // On recupere la scene dans laquelle on est pour modifier les curseur
	 }
	  
	public void Close (ActionEvent event) 
	{
		/**
		 * Ferme la page en cas de clic sur le bouton.
		 */
	   myPrimaryStage.close();
	}
}
