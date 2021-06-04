package model;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import imgProc.Barycenters;
import imgProc.Utils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import imgProc.imgProc;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
/**
 * \class MyController
 * Controleur des pages de dessin \n
 * Chaque feuille possede un unique controleur independant des autres. 
 */
public class MyController implements Initializable {
	// Declaration des différents elements
				// Boutons : 
   @FXML
   private Button BoutonEnregistrer;
   @FXML
   private Button BoutonGomme;
   @FXML
   private Button BoutonGauche;
   @FXML
   private Button BoutonDroite;
   @FXML
   private Button BoutonChangerF;
   @FXML
   private Button BoutonPlus;
   @FXML
   private Button BoutonMoins;
   @FXML
   private Button BoutonClean;
   @FXML
   private Button cam;
   @FXML
   private Button camMode;
   @FXML 
   private Button UIMode;
   @FXML
   private Button Aide;
   @FXML
   private TextField TexteField;
   @FXML
   private ColorPicker BoutonCouleur;
   
   			// Slider 
   @FXML
   private Slider hueStart;
   @FXML
   private Slider hueStop;
   @FXML
   private Slider saturationStart;
   @FXML
   private Slider saturationStop;
   @FXML
   private Slider valueStart;
   @FXML
   private Slider valueStop;
   @FXML
   private Slider sensibility;
   
   			// Texte : 
   
   @FXML
   private Label Label_Page;
   @FXML 
   private Label indicGomme;
   @FXML 
   private Label indicPolice;
   
   
   		// Variables : 
   private int IndiceFeuille=0;
   private double EpaisseurCrayon=5;
   // counter for the shortcuts
   private int counter = 0;
   // the id of the camera to be used
   private static int cameraId = 0;
   // duration for the shorcut
   private long startTime = 0;
   // lift the pencil
   private boolean drawing = false;
   private boolean drawingPrevious = false;
   private boolean hasFlipped = false;
   // a flag to change the button behavior
   private boolean cameraActive = false;
   // flag to permit drawing with the camera
   private boolean cameraMode = false;
   // boolean that change the UI mode
   private boolean camUI = false;
   //previous state of the camera mode
   private boolean cameraModePrevious = false;
   // tutorial
   private boolean Help = false;
   private boolean Gomme=false; // si false -> on dessine si vraie-> on gomme

   			// Autres : 
   @FXML
   private Canvas canvas1;
   @FXML
   private Canvas canvas2;
   @FXML
   private Canvas canvas3;
   @FXML
   private Canvas canvas4;
   private GraphicsContext info;
   @FXML
   private GraphicsContext camButtons;
   @FXML
   private GraphicsContext cursor; 
   @FXML
   private AnchorPane AnchorPanel;
   @FXML
   private ImageView FlecheD;
   @FXML
   private ImageView FlecheG;
   private Stage myPrimaryStage;
  
   // the FXML image view
   @FXML
   private ImageView currentFrameColor;
   @FXML
   private ImageView currentFrameBW;
   // a timer for acquiring the video stream
   private ScheduledExecutorService timer;
   // the OpenCV object that realizes the video capture
   private VideoCapture capture = new VideoCapture();
   // Location of the barycenter of the object, and it's previous location 
   Barycenters barycenters;
   
   /*
    * List of position and dimension of the camera UI Buttons
    */
   final private int LayoutX = 50;
   final private int LayoutXbis = 160;
   final private int LayoutYGomme = 100;
   final private int LayoutYEffacer = 250;
   final private int LayoutYTaille = 400;
   final private int Width = 210;
   final private int Height = 100;
   
   public void initialize(URL location, ResourceBundle resources) { 
	   /**
		 * Fonction d'initialisation du controleur.\n
		 * Chaque controleur Mycontrolleur execute cette fonction à sa creation.  
		 */
	  AnchorPanel.setBackground(Background.EMPTY);
	  GraphicsContext gc = this.canvas1.getGraphicsContext2D();
	  gc.setFill(Color.RED);
	  BoutonCouleur.setValue(Color.BLACK);
	  this.canvas2.setVisible(false);
	  
	  this.camButtons = this.canvas2.getGraphicsContext2D();
	  this.camButtons.setFill(Color.BLACK);
	  this.camButtons.setFont(new Font("Comic-Sans-MS", 20));
	  this.camButtons.strokeRoundRect(this.LayoutX, this.LayoutYGomme, this.Width, this.Height, 30, 30);
	  this.camButtons.strokeText("Gomme/Crayon", 85, 150, 300);
	  this.camButtons.strokeRoundRect(this.LayoutX, this.LayoutYEffacer, this.Width, this.Height, 30, 30);
	  this.camButtons.strokeText("Tout effacer", 100, 300, 300);
	  this.camButtons.strokeRoundRect(this.LayoutX, this.LayoutYTaille, this.Height, this.Height, 30, 30);
	  this.camButtons.setFont(new Font("Comic-Sans-MS", 50));
	  this.camButtons.strokeText("+", 83, 460);
	  this.camButtons.strokeRoundRect(this.LayoutXbis, this.LayoutYTaille, this.Height, this.Height, 30, 30);
	  this.camButtons.strokeText("-", 200, 460);
	  
	  this.info = this.canvas4.getGraphicsContext2D();
	  this.info.setFont(new Font("", 18));
	  this.info.strokeText("Mode : Crayon", 20, 20);
	  this.info.strokeText("Epaisseur : " + Double.toString(this.EpaisseurCrayon), 200, 20);
	  this.info.setFill(Color.WHITE);
	  
	  this.cursor = this.canvas3.getGraphicsContext2D();
	  this.cursor.setFill(Color.RED);
	  this.cursor.fillOval(0, 0, 20, 20);
	  
	  
	  barycenters = new Barycenters(-1000000, -1000000, -1000000, -1000000);

   }
 
   public void Enregistrer(ActionEvent event) 
   {
	   /**
		 * Fonction permettant d'enregistrer le dessin dans un fichier desire.\n
		 * S'active lors d'un clic sur le bouton Enregistrer.
		 */
	   JFileChooser fileChooser = new JFileChooser();
       System.out.println("Button Clicked! file :");
       WritableImage wim = new WritableImage(1500, 900);
       canvas1.snapshot(null, wim);
       File file=null;
       fileChooser.setFileFilter(new FileNameExtensionFilter("*.png", "png"));
	   if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) 
	    	{
	        	file = fileChooser.getSelectedFile();
	    	} 
	   else 
	    	{
	    		System.out.println("No file choosen!");
	    	}
       try 
       		{
        		String str = new String(file.getAbsolutePath() + ".png");
        		ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", new File(str));
        		System.out.println("Enregistre Image");
       		} 
       catch (Exception s) 
       		{
   				System.out.println("Erreur pour enregitsrer image");
       		}
   }
   
   public void Clean(ActionEvent event) 
   {
	   /**
		 * Fonction permettant de supprimer tout les traces de l'image. \n
		 * S'active lors d'un clic sur le bouton "Tout effacer"
		 */
       System.out.println("Button clean");
       GraphicsContext gc = canvas1.getGraphicsContext2D();
       gc.setFill(Color.WHITE);
	   gc.fillRect(0, 0, canvas1.getWidth(), canvas1.getHeight());
   }
  
  

  public void Gomme_clic(ActionEvent event) {
	/**
	 * Fonction permettant d'activer ou de desactiver la gomme.
	 * S'active lors d'un clic sur le bouton Gomme. 
	 */
	  if (Gomme) {
		  Gomme=false;
		  this.EpaisseurCrayon = this.EpaisseurCrayon/2;
	      //myPrimaryStage.getScene().setCursor(new ImageCursor(cercle_curseur,cercle_curseur.getWidth() / 2,cercle_curseur.getHeight() /2));
		  this.info.fillRect(20, 0, 180, 30);
		  this.info.strokeText("Mode : Crayon", 20, 20);
	}
	else {
	      Gomme=true;
	      this.EpaisseurCrayon = this.EpaisseurCrayon*2;
	      //myPrimaryStage.getScene().setCursor(new ImageCursor(gomme_curseur,gomme_curseur.getWidth() / 2,gomme_curseur.getHeight() /2));
	      this.info.fillRect(20, 0, 180, 30);
		  this.info.strokeText("Mode : Gomme", 20, 20);
		  }
  }
  
  public void draw_push(MouseEvent event)
  {
	  /**
		 * Fonction permettant de debuter un trait. \n
		 * Elle s'active au moment ou l'utilisateur clique sur le dessin. \n
		 * On debute un chemin en recuperant les coordonees de la souris\n
		 * Le chemin est continue puis fini dans les fonctions :\n
		 * draw_drag() et draw_released()
		 */
      GraphicsContext gc = canvas1.getGraphicsContext2D();
      if (Gomme) 
      {
    	  gc.setStroke(Color.WHITE);
      }
      else 
      {
    	  gc.setStroke(BoutonCouleur.getValue());
      }
      gc.setLineWidth(EpaisseurCrayon);
	  gc.beginPath();
      gc.moveTo(event.getX(), event.getY());
      gc.stroke();  
  }
  
  public void draw_drag(MouseEvent event)
  {
	  /**
		 * Fonction permettant de continuer un trait \n
		 * Elle s'active au moment ou l'utilisateur bouge la souris sur la feuille en maintenant le clic. \n
		 * On continue le chemin en recuperant les coordonees de la souris\n
		 * Le chemin debute et finis dans les fonctions :\n
		 * draw_push() et draw_released()
		 */
      GraphicsContext gc = canvas1.getGraphicsContext2D();
	  gc.lineTo(event.getX(), event.getY());
      gc.stroke();
      gc.closePath();
      gc.beginPath();
      gc.moveTo(event.getX(), event.getY());
  }
  
  public void draw_released(MouseEvent event)
  {
	  /**
		 * Fonction permettant de finir un trait \n
		 * Elle s'active au moment ou l'utilisateur relache le clic de sa souris. \n
		 * On termine le chemin aux coordonees de la souris\n
		 * Le chemin debute et continue dans les fonctions :\n
		 * draw_push() et draw_drag()
		 */
      GraphicsContext gc = canvas1.getGraphicsContext2D();
      gc.lineTo(event.getX(), event.getY());
      gc.stroke();
      gc.closePath();
  }
  
  public void setMyPrimaryStage(Stage primaryStage) {
	  /**
		 * Fonction permettant de recuperer le stage controle par le controleur \n
		 */
      this.myPrimaryStage = primaryStage; // On recupere la scene dans laquelle on est pour modifier les curseur
	  myPrimaryStage.setMaximized(true);
	  myPrimaryStage.getTitle();  
  }
  
  public void ChangerFeuille(ActionEvent event)
  {
	  /**
	   * Fonction activant la fonction Main.RajoutFeuille() \n
	   * Envoie les parametres de detection et l'indice de la feuille.    		 
	   */
	  Main.RajoutFeuille(IndiceFeuille,hueStart.getValue(),hueStop.getValue(),saturationStart.getValue(),
			  saturationStop.getValue(),valueStart.getValue(),valueStop.getValue(),sensibility.getValue());
  }
  
  public void ChangerFeuilleD()
  {
	  /**
	   * Fonction activant la fonction Main.ChangerFeuilleDroite() \n
	   * Envoie l'indice de la feuille.    		 
	   */
	  Main.ChangerFeuilleDroite(IndiceFeuille);
  }
  
  public void ChangerFeuilleG()
  {
	  /**
	   * Fonction activant la fonction Main.ChangerFeuilleGauche() \n
	   * Envoie l'indice de la feuille.    		 
	   */
	  Main.ChangerFeuilleGauche(IndiceFeuille);
  }
  
  public void setIndiceI(int Indice) {
	  /**
	   * Fonction recuperant l'indice de la feuille. \n 
	   * On definie la variable IndiceFeuille. \n
	   * S'active lors de l'initialisation d'une feuille dans Main.RajoutFeuille() ou Main.start(). 		 
	   */
	  IndiceFeuille=Indice;
      //myPrimaryStage.getScene().setCursor(new ImageCursor(cercle_curseur,cercle_curseur.getWidth() / 2,cercle_curseur.getHeight() /2));
  }
  
  public int GetIndice(int Indice) {
	  /**
	   * Retourne la variable IndiceFeuille du controlleur. \n
	   * S'active dans Main.Close_All() 	 
	   */
	  return IndiceFeuille;
  }
  
  public void setTexte(int Indice) {
	  /**
	   * Modifie le texte indiquant le nombre de feuille ouverte et la localisation de la feuille actuelle. \n
	   * S'active dans Main.ChangerTexte()  	 
	   */
	  Label_Page.setText("Feuille "+ (IndiceFeuille+1) +"/"+(Indice+1));
  }
  
  public void setValeur(double hueStart_,double hueStop_,
  		double saturationStart_, double saturationStop_,double valueStart_,
  		double valueStop_,double sensibility_) {
	  /**
	   * Attribue les valeurs de la detection de cette feuille selon les valeurs de la feuille precedente \n
	   * S'active dans Main.RajoutFeuille() lorsque l'on cree une nouvelle feuille. 
	   */
	  hueStart.setValue(hueStart_);
	  hueStop.setValue(hueStop_);
	  saturationStart.setValue(saturationStart_);
	  saturationStop.setValue(saturationStop_);
	  valueStart.setValue(valueStart_);
	  valueStop.setValue(valueStop_);
	  sensibility.setValue(sensibility_);
  }

  
  public void PlusTaille ()
  {
	  /**
	   * Augmente la variable EpaisseurCrayon (avec une limite max). \n
	   * S'active lors du clic sur le bouton +.
	   */
	  if (!Gomme) {
		  if (EpaisseurCrayon < 50 )
	  	{	
		  if (EpaisseurCrayon == 0.5)
	  			{EpaisseurCrayon = 5;}
	  		else 
	  			{EpaisseurCrayon = EpaisseurCrayon + 5 ;}
	  	}
	  this.info.fillRect(200, 0, 180, 30);
	  this.info.strokeText("Epaisseur : " + Double.toString(this.EpaisseurCrayon), 200, 20);
	  }
  }
  
  public void MoinsTaille ()
  {
	  /**
	   * Diminue la variable EpaisseurCrayon (avec une limite min). \n
	   * S'active lors du clic sur le bouton -.
	   */
	  if(!Gomme) {
	  if (EpaisseurCrayon >= 10 )
	  {EpaisseurCrayon = EpaisseurCrayon - 5 ;}
	  else {
		  if (EpaisseurCrayon ==5)
		  {EpaisseurCrayon = 0.5;}
	  }
	  this.info.fillRect(200, 0, 180, 30);
	  this.info.strokeText("Epaisseur : " + Double.toString(this.EpaisseurCrayon), 200, 20);
	  }
  }
  
  public void startDrawing(ActionEvent evt) {
	  /**
	   * Modifie l'etat de la variable cameraMode. \n
	   * Cette fonction permet de lancer le dessin via la detection de l'objet a la Webcam.\n 
	   * Elle s'active lorsque l'on clique sur le bouton Start Drawing.
	   */
	  if (this.cameraMode) {
		  this.cameraMode = false;
		  this.camMode.setText("Start Drawing");
	  }
	  else {
		  this.cameraMode = true;
		  this.camMode.setText("Stop Drawing");
		  if (this.Help) {
			  JOptionPane.showMessageDialog(null, "Vous pouvez cliquer sur camera UI \n"
						+ "pour sélectionner les options de dessin avec l'objet ");
			  JOptionPane.showMessageDialog(null, "Pour simuler un levé de crayon il faut retourner l'objet. \n"
						+ "La pastille verte indique que vous pouvez dessiner. \n"
						+ "La pastille rouge simule un levé de crayon et permet d'activer les boutons.\n"
						+ "Pour activer les boutons en mode camera, il faut retourner l'objet sur le bouton desiré \n");
			  this.Help = false;
		  }
	  }
  }
  
  public void changeUI(ActionEvent evt) {
	  /**
	   * Modifie l'interface affiche afin que celle-ci soit plus simple a utiliser avec le webcam. \n
	   * On rentre dans la fonction lorsque l'on clique sur Camera UI.
	   */
	  if (!this.camUI) {
		  this.camUI = true;
		  boolean argV = false;
		  this.BoutonCouleur.setVisible(argV);
		  this.BoutonEnregistrer.setVisible(argV);
		  this.BoutonGomme.setVisible(argV);
		  this.BoutonPlus.setVisible(argV);
		  this.BoutonMoins.setVisible(argV);
		  this.BoutonClean.setVisible(argV);
		  this.BoutonChangerF.setVisible(argV);
		  this.BoutonDroite.setVisible(argV);
		  this.BoutonGauche.setVisible(argV);
		  this.FlecheD.setVisible(argV);
		  this.FlecheG.setVisible(argV);
		  this.canvas2.setVisible(!argV);
		  this.UIMode.setText("Mouse Mode");
	  }
	  
	  else {
		  this.camUI = false;
		  boolean argV = true;
		  this.BoutonCouleur.setVisible(argV);
		  this.BoutonEnregistrer.setVisible(argV);
		  this.BoutonGomme.setVisible(argV);
		  this.BoutonPlus.setVisible(argV);
		  this.BoutonMoins.setVisible(argV);
		  this.BoutonClean.setVisible(argV);
		  this.BoutonChangerF.setVisible(argV);
		  this.BoutonDroite.setVisible(argV);
		  this.BoutonGauche.setVisible(argV);
		  this.FlecheD.setVisible(argV);
		  this.FlecheG.setVisible(argV);
		  this.canvas2.setVisible(!argV);
		  this.UIMode.setText("Camera Mode");
	  }
  }
  
  public void tutorial(ActionEvent evt) {
	  /**
	   * Permet de choisir si l'on affiche ou non les messages du tutoriel.\n
	   * S'active lorsque l'on clique sur le point d'interogation. 
	   */
	  this.Help = true;
	  JOptionPane.showMessageDialog(null, "Pour commencer le dessin par la caméra, "
	  		+ "appuyez sur le bouton 'Start camera'");
	  
  }
  
  public void flipped() {
	  /**
	   * Modifie la variable hasFlipped en fonction du comportement de l'objet. \n
	   * On detecte ainsi si l'objet est cache. \n
	   * Cette fonction est utilisee dans grabFrame().
	   */
	  if ((this.barycenters.getCurrentX() >= 0) && (this.barycenters.getCurrentY() >= 0) && (this.barycenters.getPreviousX() < 0) && (this.barycenters.getPreviousY() < 0)) {
		  this.hasFlipped = true;
		  if (this.drawing) {
				this.drawing = false;
				this.cursor.setFill(Color.RED);
				this.cursor.fillOval(0, 0, 20, 20);
			}
			else {
				this.drawing = true;
				this.cursor.setFill(Color.GREEN);
				this.cursor.fillOval(0, 0, 20, 20);
			} 
	  }
	  else {
		  this.hasFlipped = false;
	  }
  }

  
  // Painting with the camera
  public void camDraw(int x, int y, int prex, int prey, int tolerance) {
	  /**
	   * Cette fonction permet de dessiner avec la camera. \n
	   * On utilise les meme principe que dans les fonction draw_released() draw_push() et draw_drag().\n
	   * Cependant, on utilise plus les mouvement et coordonnees de la souris mais les mouvement de l'objet detecte. 
 	   */
	  GraphicsContext gc = canvas1.getGraphicsContext2D();
	  
	  if (((x < 0) || (y < 0)) && ((prex >= 0) && (prey >= 0))) {
		  gc.lineTo(prex, prey);
	      gc.stroke();
	      gc.closePath();
	  }
	  
	  else if (((prex < 0) || (prey < 0)) && ((x >= 0) && (y >= 0)) || (!this.cameraModePrevious) || (!this.drawingPrevious)) {
	      if (Gomme) {gc.setStroke(Color.WHITE);
	      }
	      else { gc.setStroke(BoutonCouleur.getValue()); }
	      gc.setLineWidth(EpaisseurCrayon);
		  gc.beginPath();
	      gc.moveTo(x, y);
	      gc.stroke();  
	  }
	  
	  else if (((x >= 0) && (y >= 0)) && ((prex >= 0) && (prey >= 0)) && ((Math.abs(x-prex) > tolerance) || (Math.abs(y-prey) > tolerance)))  {
		  gc.lineTo(x, y);
	      gc.stroke();
	      gc.closePath();
	      gc.beginPath();
	      gc.moveTo(x, y);
	  }
	  
  }
  
  public void cameraButton() {
	  /**
	   * Lance des actions en fonction de la positon de l'objet. \n
	   * On utilise cette fonction uniquement avec l'interface Camera UI.\n
	   * On peut activer la gomme, tout nettoyer et modifier la taille selon la position de l'objet.
	   */
	  // for the clean button 
	  if ((2*this.barycenters.getCurrentY() > this.LayoutX) && (2*this.barycenters.getCurrentY() < this.LayoutX + this.Width) && (2*this.barycenters.getCurrentX() > this.LayoutYEffacer) && (2*this.barycenters.getCurrentX() < this.LayoutYEffacer + this.Height) && this.hasFlipped && this.camUI) {
		  System.out.println("Button clean");
	      GraphicsContext gc = canvas1.getGraphicsContext2D();
	      gc.setFill(Color.WHITE);
		  gc.fillRect(0, 0, canvas1.getWidth(), canvas1.getHeight());
		  if (this.drawing) 
		  	{
				this.drawing = false;
				this.cursor.setFill(Color.RED);
				this.cursor.fillOval(0, 0, 20, 20);
		  	}
	  }
	  //for the gomme button
	  if ((2*this.barycenters.getCurrentY() > this.LayoutX) && (2*this.barycenters.getCurrentY() < this.LayoutX + this.Width) && (2*this.barycenters.getCurrentX() > this.LayoutYGomme) && (2*this.barycenters.getCurrentX() < this.LayoutYGomme + this.Height) && this.hasFlipped && this.camUI) {
		  if (Gomme) 
		  	{
			  Gomme=false;
			  this.EpaisseurCrayon = this.EpaisseurCrayon/2;
		      //myPrimaryStage.getScene().setCursor(new ImageCursor(cercle_curseur,cercle_curseur.getWidth() / 2,cercle_curseur.getHeight() /2));

			  this.info.fillRect(20, 0, 180, 30);
			  this.info.strokeText("Mode : Crayon", 20, 20);
		}
		else {
		      Gomme=true;
		      this.EpaisseurCrayon = this.EpaisseurCrayon*2;
		      //myPrimaryStage.getScene().setCursor(new ImageCursor(gomme_curseur,gomme_curseur.getWidth() / 2,gomme_curseur.getHeight() /2));
		      this.info.fillRect(20, 0, 180, 30);
			  this.info.strokeText("Mode : Gomme", 20, 20);
			  }
		  
		if (this.drawing) {
			this.drawing = false;
			this.cursor.setFill(Color.RED);
			this.cursor.fillOval(0, 0, 20, 20);	
		}
	  }
	  // augmenter la taille
	  if ((2*this.barycenters.getCurrentY() > this.LayoutX) && (2*this.barycenters.getCurrentY() < this.LayoutX + this.Height) && (2*this.barycenters.getCurrentX() > this.LayoutYTaille) && (2*this.barycenters.getCurrentX() < this.LayoutYTaille + this.Height) && this.hasFlipped && this.camUI) {
		  this.PlusTaille();
		  if (this.drawing) {
				this.drawing = false;
				this.cursor.setFill(Color.RED);
				this.cursor.fillOval(0, 0, 20, 20);
		  }
	  }
	  // diminuer la taille
	  if ((2*this.barycenters.getCurrentY() > this.LayoutXbis) && (2*this.barycenters.getCurrentY() < this.LayoutXbis + this.Height) && (2*this.barycenters.getCurrentX() > this.LayoutYTaille) && (2*this.barycenters.getCurrentX() < this.LayoutYTaille + this.Height) && this.hasFlipped && this.camUI) {
		this.MoinsTaille(); 
		if (this.drawing) {
			this.drawing = false;
			this.cursor.setFill(Color.RED);
			this.cursor.fillOval(0, 0, 20, 20);
		}
	  }	 
  }
  
  public void shorcutsGestures() {
	  /**
	   * Cette fonction n'est plus utilisee.\n
	   * Elle permettait de realiser diverse fonction selon des actions precise de l'utilisateur.\n
	   * On pouvait par exemple passer en mode gomme en cachant 4 fois d'affile l'objet. \n
	   * Pour des raisons de praticite, on n'utilise plus ces raccourcis 
	   */
	  if (this.hasFlipped) {
			if (this.startTime == 0) {
				this.startTime = System.currentTimeMillis();
			}
			
			if (System.currentTimeMillis() - this.startTime <= 5000) {
				this.counter += 1;
				this.hasFlipped = true;
			}
			
		}
		
		// shortcut to erase all the content
		if ((this.counter >= 5) && (System.currentTimeMillis() - this.startTime >= 4000)) {
			 GraphicsContext gc = canvas1.getGraphicsContext2D();
		       gc.setFill(Color.WHITE);
			   gc.fillRect(0, 0, canvas1.getWidth(), canvas1.getHeight());
			   this.counter = 0;
			   this.startTime = 0;
		}
		
		
		if ((this.counter == 4) && (System.currentTimeMillis() - this.startTime >= 4000))
		{
			if (Gomme) {
				  Gomme=false;
				  this.EpaisseurCrayon = this.EpaisseurCrayon/2;
			      //myPrimaryStage.getScene().setCursor(new ImageCursor(cercle_curseur,cercle_curseur.getWidth() / 2,cercle_curseur.getHeight() /2));
			}
			else {
			      Gomme=true;
			      this.EpaisseurCrayon = this.EpaisseurCrayon*2;
			      //myPrimaryStage.getScene().setCursor(new ImageCursor(gomme_curseur,gomme_curseur.getWidth() / 2,gomme_curseur.getHeight() /2));
			}
			this.counter = 0;
			this.startTime = 0;	  
		}
		
		
		if ((this.hasFlipped) && (System.currentTimeMillis() - this.startTime >= 500)) {
			
			this.hasFlipped = false;
		}

		if (System.currentTimeMillis() - this.startTime > 5000) {
			this.counter = 0;
			this.startTime = 0;
		}
		
	}
  	    
	// video stream part

	public void startCamera(ActionEvent event)
	 /**
	   * Demarre l'acquisition de la camera et gere son affichage dans l'application.
	   * 
	   */
		{
			if (!this.cameraActive)
			{
				// start the video capture
				this.capture.open(cameraId);
				if (this.Help) {
					JOptionPane.showMessageDialog(null, "Ajustez les curseurs \n"
							+ "1 par 1 en partant du haut vers le bas \n"
							+ "de sorte à toujours avoir l'objet en blanc \n"
							+ "et le reste en noir");
					JOptionPane.showMessageDialog(null, "Vous pouvez cliquer sur start drawing \n"
							+ "pour commencer à dessiner ");
				}
				// is the video stream available?
				if (this.capture.isOpened())
				{
					this.cameraActive = true;
					
					// grab a frame every 33 ms (30 frames/sec)
					Runnable frameGrabber = new Runnable() {
						
						public void run()
						{
							// effectively grab and process a single frame
							Mat[] frames = grabFrame();
							Mat frame = frames[0];
							Mat frameBW = frames[1];
							// convert and show the frame
							Image imageToShowColor = Utils.mat2Image(frame);
							Image imageToShowBW = Utils.mat2Image(frameBW);
							updateImageView(currentFrameColor, imageToShowColor);
							updateImageView(currentFrameBW, imageToShowBW);
							
						}
					};
					
					this.timer = Executors.newSingleThreadScheduledExecutor();
					this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
					
					// update the button content
					this.cam.setText("Stop Camera");
				}
				else
				{
					// log the error
					System.err.println("Impossible to open the camera connection...");
				}
			}
			else
			{
				// the camera is not active at this point
				this.cameraActive = false;
				// update again the button content
				this.cam.setText("Start Camera");
				
				// stop the timer
				this.stopAcquisition(); 
			}
		}
		
		/**
		 * Get a frame from the opened video stream (if any)
		 *
		 * @return the {@link Mat} to show
		 */
		private Mat[] grabFrame()
		{
			 /**
			   * Applique les différentes methodes de imgProc sur l'image acquise par la caméra. \n
			   * On obtient alors les coordonnees du barycentre qu'on utilise pour dessiner. 
			   */
			// init everything
			Mat[] frames = new Mat[2];
			Mat frame = new Mat();
			
			// check if the capture is open
			if (this.capture.isOpened())
			{
				try
				{
					// read the current frame
					this.capture.read(frame);
					
					
					// if the frame is not empty, process it
					if (!frame.empty())
					{
						/* 
						 * In this part, the image is processed
						 */
						//Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
						
						imgProc proc = new imgProc();
						Mat procFrame = frame.clone();
						
						Core.flip(frame, frame, 1);
						Core.flip(procFrame, procFrame, 1);
						procFrame = proc.preProc(procFrame, 5);
						procFrame = proc.getBW(procFrame, hueStart.getValue(),hueStop.getValue(),saturationStart.getValue(),saturationStop.getValue(),valueStart.getValue(),valueStop.getValue());
						procFrame = proc.postProc(procFrame, 10);
						int[] barycenter = proc.getBarycenter(procFrame, 200);
						frame = proc.drawCircle(frame, barycenter,10);
						procFrame = proc.drawCircleColor(procFrame, barycenter,10);
						
						frames[0] = frame;
						frames[1] = procFrame;
						
						/*
						 * The barycenter field is modified
						 */
						this.barycenters.setPreviousX(this.barycenters.getCurrentX());
						this.barycenters.setPreviousY(this.barycenters.getCurrentY());
						
						this.barycenters.setCurrentX(barycenter[0]); 
						this.barycenters.setCurrentY(barycenter[1]); 
						
						this.barycenters.updateNotify();
						
						// behaviour after getting the barycenters
												
						
						if (this.cameraMode) {
							this.canvas3.setLayoutY(2*this.barycenters.getCurrentX()-10);
							this.canvas3.setLayoutX(2*this.barycenters.getCurrentY()-10);
							// method to draw with the coordinate of the barycenter
							this.drawingPrevious = this.drawing;
							this.flipped(); //look if the object has flipped
							this.cameraButton();
							// this.shorcutsGestures();
							if (this.drawing) {
								this.camDraw(2*this.barycenters.getCurrentY() - 270, 2*this.barycenters.getCurrentX(), 2*this.barycenters.getPreviousY() - 270, 2*this.barycenters.getPreviousX(), (int) sensibility.getValue());
							}
						}
						this.cameraModePrevious = this.cameraMode;

					}
					
				}
				catch (Exception e)
				{
					// log the error
					System.err.println("Exception during the image elaboration: " + e);
				}
				
			}
			
			return frames;
		}
		
		
		private void stopAcquisition()
		{
			/**
			 * Arrete l'acquisition de la camera et supprime ses différentes ressource. \n
			 * On l'active avec setClosed().
			 */
			if (this.timer!=null && !this.timer.isShutdown())
			{
				try
				{
					// stop the timer
					this.timer.shutdown();
					this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
				}
				catch (InterruptedException e)
				{
					// log any exception
					System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
				}
			}
			
			if (this.capture.isOpened())
			{
				// release the camera
				this.capture.release();
			}
		}
		
		/**
		 * Update the {@link ImageView} in the JavaFX main thread
		 * 
		 * @param view
		 *            the {@link ImageView} to update
		 * @param image
		 *            the {@link Image} to show
		 */
		private void updateImageView(ImageView view, Image image)
		{
			Utils.onFXThread(view.imageProperty(), image);
		}
		
		
		protected void setClosed()
		/**
		 * On Stop l'acquisition.\n
		 * Cette fonction fais appel a stopAcquisition().\n
		 * Elle s'active depuis la fonction Main.Close_All().
		 */
		{
			this.stopAcquisition();
		}
		
		
		
}




