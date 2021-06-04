package imgProc;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * \class imgProc.imgProc
 * La classe imgProc reunie les methodes de traitement d'image. \n
 * On fais donc appel a ses diverses fonctions pour calculer le barycentre de l'objet. 
 */
public class imgProc {
	
	
	public Mat getBW(Mat mat,double hueStart,double hueStop ,double saturationStart,double saturationStop,double valueStart,double valueStop) {
		/**
		 *  On recupere les differentes valeurs des parametres de detection comme la saturation ou la teinte. \n
		 * C'est le deuxieme traitement applique sur l'image. \n
		 * Transforme l'image couleur obtenue en image Noir et Blanc. \n
		 * Les parties de l'image rentrant dans les parametres choisi sont en blanc et le reste devient noir. \n
		 * 
		 * On a donc en sortie une image en N&B avec les elements interessant (l'objet) en blanc et le fond en noir. 
		 */
		Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2HSV ); // convert the Mat image in hsv space 
        
        double hmin = hueStart;
        double hmax = hueStop;
        double smin = saturationStart;
        double smax = saturationStop;
        double vmin = valueStart;
        double vmax = valueStop;
        
        // get thresholding values from the UI
        // remember: H ranges 0-180, S and V range 0-255
        
     	Scalar minValues = new Scalar(hmin, smin,vmin);
     	Scalar maxValues = new Scalar(hmax, smax,vmax);
     					
     	// threshold HSV image to select object
     	Core.inRange(mat, minValues, maxValues, mat);
     	
        return mat;
	}
	
	public Mat preProc(Mat mat, int es_size) {
		/**
		 * Premier traitement de l'image obtenu par la webcam. \n
		 * On applique un flou sur l'image avec un element structurant de taille es_size.
		 */
		Imgproc.blur(mat, mat, new Size(es_size, es_size));
		return mat;
	}
	
	public Mat postProc(Mat mat, int es_size) {
		/**
		 * Dernier traitement de l'image. \n
		 * Cree 2 elements structurants puis realise une ouverture et une fermeture avec ces elements. \n
		 * Cette fonction supprime les petis elements (parasites) afin de ne garder que l'objet.  
		 */
		int elementType = Imgproc.CV_SHAPE_ELLIPSE; // type of structuring element
        Size elementSize1 = new Size(es_size, es_size);	// size of structuring element 
        Size elementSize2 = new Size(es_size, es_size);
        Mat kernel1 = Imgproc.getStructuringElement(elementType, elementSize1);	// creation of the structuring element
        Mat kernel2 = Imgproc.getStructuringElement(elementType, elementSize2);
        Imgproc.morphologyEx(mat, mat, Imgproc.MORPH_OPEN, kernel2);   // morphological opening
        Imgproc.morphologyEx(mat, mat, Imgproc.MORPH_CLOSE, kernel1); // morphological closing 
        return mat;
	}
	
	public int[] getBarycenter(Mat mat, int prec) {
		/**
		 * Apres avoir detache l'objet du fond on cherche le centre de cet objet.\n
		 * Cette fonction calcule le barycentre des elements blancs d'une image en N&B. \n
		 * 
		 */
		int[] barycenter = new int[2];
		int sumX = 0; 
        int sumY = 0;
        int nbPixels = 0;
        for (int x = 0; x < mat.rows(); x++)
        {
        	for (int y = 0; y < mat.cols(); y++)
        	{
        		if (mat.get(x, y)[0] != 0)
        		{
        			sumX += x;
        			sumY += y;
        			nbPixels++;
        		}
        	}
        }
        if (nbPixels > prec) {
        	barycenter[0] = sumX/nbPixels;
        	barycenter[1] = sumY/nbPixels;
        }
        else {
        	barycenter[0] = -1000000;
        	barycenter[1] = -1000000;
        }
    	return barycenter;
	}
	
	public Mat drawCircle(Mat mat, int[] coord, int size) {
		/**
		 * Represente le Barycentre calcule avec un cercle rouge. \n
		 * Affiche ce barycentre sur le dessin depuis model.MyController.grabFrame().
		 */
        Imgproc.circle(mat, new Point(coord[1], coord[0]), size, new Scalar(0, 0, 255), size);		// the barycenter is marked on the image by a red circle
        return mat;
	}
	
	public Mat drawCircleColor(Mat mat, int[] coord, int size) {
		/**
		 * Represente le Barycentre calcule avec un cercle rouge.\n
		 * Affiche ce barycentre sur l'image de la camera apres traitement.\n
		 * L'appel de cette fonction se fait depuis model.MyController.grabFrame().
		 */
		Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2BGR);
        Imgproc.circle(mat, new Point(coord[1], coord[0]), size, new Scalar(0, 0, 255), size);		
        return mat;
	}
}


