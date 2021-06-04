package imgProc;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
/**
 * \class TestImgProc
 * Classe servant aux differents tests du traitement d'image. \n
 * Cette partie n'est pas utilisee dans l'application finale. 
 */
public class TestImgProc {
	
	public static void main(String[] args) {
		
		nu.pattern.OpenCV.loadLocally();
		String source = "carnet-cam.jpg";
		try {
			File input = new File(source);					// Get the buffered image from path 
			BufferedImage image = ImageIO.read(input);
			
			Conversion convert = new Conversion();
			Mat mat = convert.im2mat(image);
			
			Mat test = new Mat();
			Imgproc.cvtColor(mat, test, Imgproc.COLOR_BGR2HSV ); 
			
			double[] hsv = test.get(640, 361);
			System.out.println(hsv[0]);
			System.out.println(hsv[1]);
			System.out.println(hsv[2]); 
			
			imgProc proc = new imgProc();
			mat = proc.preProc(mat, 5);
			mat = proc.getBW(mat, 178, 166, 20, 20, 0, 255);
			mat = proc.postProc(mat, 5);
			int[] barycenter = proc.getBarycenter(mat, 100);
			mat = proc.drawCircle(mat, barycenter, 10);
					
			
			//System.out.println(barycenter[0]);
			//System.out.println(barycenter[1]);
			
			Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2BGR ); 

			BufferedImage image1 = convert.mat2im(mat);
	        File ouptut = new File("binaryhsv.jpg");
	        
	        ImageIO.write(image1, "jpg", ouptut);

		} catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
	}
	}
}
