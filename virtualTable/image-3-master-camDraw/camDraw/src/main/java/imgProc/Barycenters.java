package imgProc;
/**
 * \class Barycenters
 * Cette classe Barycentre permet de gerer les differentes methode utilisees. \n
 * On utilise des objets Barycentre dans Model.MyController.  \n
 * Barycenters possede 4 attributs : \n
 * X et Y du barycentre actuel  \n
 * X et Y du barycentre precedent
 */
public class Barycenters 
{
	
	
	private int currentX;
	private int currentY;
	private int previousX;
	private int previousY;
	
	public Barycenters(int currentX, int currentY, int previousX, int previousY) {
		/**
		 * Constructeur de la classe. \n
		 * L'objet construit aura comme attributs les valeurs passees en entree. 
		 */
		super();
		this.currentX = currentX;
		this.currentY = currentY;
		this.previousX = previousX;
		this.previousY = previousY;
	}
	
	public int getCurrentX() {
		/**
		 * Retourne le X actuel. 
		 */
		return currentX;
	}
	
	public void setCurrentX(int currentX) {
		/**
		 * Definie le X actuel. 
		 */
		this.currentX = currentX;
	}
	
	public int getCurrentY() {
		/**
		 * Retourne le Y actuel. 
		 */
		return currentY;
	}
	
	public void setCurrentY(int currentY) {
		/**
		 * Definie le Y actuel. 
		 */
		this.currentY = currentY;
	}
	
	public int getPreviousX() {
		/**
		 * Retourne le X precedent. 
		 */
		return previousX;
	}
	
	public void setPreviousX(int previousX) {
		/**
		 * Defini le X precedent. 
		 */
		this.previousX = previousX;
	}
	
	public int getPreviousY() {
		/**
		 * Retourne le Y precedent. 
		 */
		return previousY;
	}
	
	public void setPreviousY(int previousY) {
		/**
		 * Defini le Y precedent. 
		 */
		this.previousY = previousY;
	}
	
	public void updateNotify() {
		/**
		 * Ecrit dans la console : \n
		 * Positions X,Y du barycentre actuel
		 * Positions X,Y du barycentre precedent 
		 */
        System.out.println("x = " + this.currentX + "; previous x = " + this.previousX);
        System.out.println("y = " + this.currentY + "; previous y = " + this.previousY);
    }
	
}
