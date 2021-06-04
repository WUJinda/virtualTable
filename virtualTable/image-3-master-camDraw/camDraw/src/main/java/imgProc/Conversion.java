package imgProc;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
/**
 * \class Conversion
 * Ces fonctions nous ont permis de faire differents tests sur les images. \n
 * Elles ne sont pas utilisees dans l'application finale, c'est la classe Utils qui remplit ce role. 
 */
public class Conversion {
	public Mat im2mat(BufferedImage im) {
		/**
		 * Convertit une BufferedImage en Mat
		 */
		Mat mat = new Mat(im.getHeight(), im.getWidth(), CvType.CV_8UC3);    

	        byte[] data = ((DataBufferByte) im.getRaster().getDataBuffer()).getData(); 		
	        mat.put(0, 0, data);		// put the data from the buffered image to the a Mat type image

	    return mat;
	}
	
	public BufferedImage mat2im(Mat mat) {
		/**
		 * Convertit une image Mat en BufferedImage
		 */
		byte[] data1 = new byte[mat.rows() * mat.cols() * ((int)(mat.elemSize()))];
        mat.get(0, 0, data1);
        BufferedImage image1 = new BufferedImage(mat.cols(),mat.rows(), BufferedImage.TYPE_3BYTE_BGR);
        image1.getRaster().setDataElements(0, 0,mat.cols(), mat.rows(), data1); 

        return image1;
	}

}


