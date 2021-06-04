package model;
 
import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/**
 * \class model.Main 
 * Classe servant a la definition de diverses fonctions. \n
 * On y retrouve nottament les fonctions de gestion de feuille.
 */
public class Main extends Application 
{
	
	
	/**
	 * \param i indique le nombre actuel de feuille ouverte.
	 * \param TabStage Tableau de 10 Stages contenant les divers Stage(Fenetre) utilisees dans l'application.
	 * \param TabControl Tableau de 10 Mycontroller (controller du Fxml Tableau).
	 */
  static int i=0;
  static Stage[] TabStage=new Stage[10]; /// Maximum 8 Feuilles ouvertes en meme temps + Feuille STOP
  static MyController[] TabControl = new MyController[10];
    @Override
    
    public void start(Stage primaryStage) {
    	/**
    	 * Fonction s'activant au lancement de l'application. \n
    	 * Cette fonction permet de generer la premiere feuille de dessin.
    	 */
        try {
        	TabStage[i]=primaryStage;
        	FXMLLoader loader = new FXMLLoader(Main.class.getResource("Tableau.fxml"));
            Parent root = loader.load();
            /// Read file fxml and draw interface.
            TabStage[i].setTitle("Feuille Dessin n°1");
            TabStage[i].setScene(new Scene(root,1100, 500, Color.TRANSPARENT));
            TabControl[i] = loader.getController();
            TabStage[i].show();
            TabControl[i].setMyPrimaryStage(TabStage[i]); // On envoie la valeur
            TabControl[i].setIndiceI(i);
            TabControl[i].setTexte(i);
            i++;
            
            // Alert popup instruction 
            JOptionPane.showMessageDialog(null, "Bienvenue sur l'application \n"
					+ "de dessin \n"
					+ "Vous pouvez activer l'aide en cliquant sur \n"
					+ "le boutton '?' \n");
            
            // set the proper behavior on closing the application
            primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>() 
            {
             	public void handle(WindowEvent we)
             	{
             		Platform.exit();
             	     System.exit(0);
             	}
             }));      
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
    	/**
    	 * Fonction s'activant au lancement de l'application. \n
    	 * Cette fonction permet de charger la bibliotheque opencv.
    	 * Lance la fonction start()
    	 */
    	nu.pattern.OpenCV.loadLocally();
        launch(args);
    }
    
    
    public static void RajoutFeuille(int Indice,double hueStart,double hueStop,
    		double saturationStart, double saturationStop,double valueStart,
    		double valueStop,double sensibility)
    /**
	 * Fonction gerant le rajout de feuille. \n
	 * Elle prend en entree l'indice de la feuille actuelle ainsi que ses parametres de detection de l'objet.\n
	 * S'il y a deja 8 feuilles d'ouvertes, on affiche un message d'erreur. \n
	 * Elle s'active avec MyController.ChangerFeuille()
	 */
    {
    	if (i>7) 
    	{
    		if (i==8) // S'il y a deja 8 feuille ouverte
    		{ 
    			try {
    				FXMLLoader loader3 = new FXMLLoader(Main.class.getResource("TropFenetre.fxml"));
    				Parent root3 = loader3.load();
    				TabStage[8]=new Stage();
    				TabStage[8].setTitle("Limite Feuille");
    				TabStage[8].setScene(new Scene(root3));
    				TropFenetreControleur controller3 = loader3.getController();
    				controller3.setMyPrimaryStage(TabStage[8]); // On envoie la valeur
    				TabStage[8].show();
    				i++;
    				}
    			catch(Exception e) 
    			{
    				e.printStackTrace();
    			}
    		}
    		else { TabStage[8].show();} // Limite feuille est deja chargé dans TabStage[8]
    	}
    	else 
    	{
    		try 
    		{
    			FXMLLoader loader2 = new FXMLLoader(Main.class.getResource("Tableau.fxml"));
    			Parent root2 = loader2.load();
    			// Read file fxml and draw interface.
    			Stage SecondaryStage=new Stage();
    			TabStage[i]= SecondaryStage;
    			TabStage[i].setTitle("Feuille Dessin n°"+(i+1));
    			TabStage[i].setScene(new Scene(root2,1100, 500, Color.TRANSPARENT));
    			TabControl[i] = loader2.getController();
    			TabControl[i].setMyPrimaryStage(TabStage[i]); // On envoie la valeur
    			TabControl[i].setIndiceI(i);
    			TabControl[i].setValeur(hueStart,hueStop,	saturationStart,saturationStop, valueStart,valueStop,sensibility);
	  		  	ChangerTexte(i);
	  		  	TabStage[i].show();
	  		  	TabStage[Indice].hide();
	  		  	ChangerTexte(i);
			  	i++;
			  	SecondaryStage.setOnCloseRequest((new EventHandler<WindowEvent>() 
			  	{
	             	public void handle(WindowEvent we)
	             	{
	             		Platform.exit();
	             	     System.exit(0);
	             	}
	             }));
    		} 
    		catch(Exception e) 
    		{
    			e.printStackTrace();
    		}
    	}
    }
    
    public static void ChangerFeuilleDroite(int Indice)
    /**
	 * Fonction permettant d'afficher la feuille suivante.\n
	 * Elle prend en entree l'indice de la feuille actuelle. \n
	 * Elle s'active avec MyController.ChangerFeuilleD()
	 */
    {
    		if (i>Indice+1 && Indice!=7 ) 
    		{ 
    			TabStage[Indice+1].show(); 
    			TabStage[Indice].hide();
    			System.out.println("I+1");
    		}
    }
    
    public static void ChangerFeuilleGauche(int Indice)
    /**
	 * Fonction permettant d'afficher la feuille precedente.\n
	 * Elle prend en entree l'indice de la feuille actuelle. \n
	 * Elle s'active avec MyController.ChangerFeuilleG()
	 */
    {
    		if ( Indice!=0) 
    		{
    			TabStage[Indice-1].show();
    			TabStage[Indice].hide();
    			System.out.println("I-1");
    		}
    }
    
    public static void ChangerTexte(int Indice)
    /**
	 * Fonction permettant de mettre a jour le comptage des feuilles. \n
	 * ex : feuille 3/5 \n
	 * Elle prend en entree l'indice de la feuille actuelle.\n
	 * Elle s'active avec RajoutFeuille()
	 */
    {
    	for (int i=0;i<(Indice+1);i++)
    	{
    		TabControl[i].setTexte(Indice);
    	}
    }
    
    public static void Close_All(MyController[] TabControl) 
    /**
	 * Fonction permettant d'arreter completement l'application. \n
	 * Arrete toutes les pages ouvertes. 
	 */
    {
    	for (int i=0;i<8;i++)
 		{
 			if (TabControl[i].GetIndice(i) != 0) {TabControl[i].setClosed();}
 		}
	}
    
}