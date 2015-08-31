import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Descriptor;
import com.drew.metadata.exif.ExifIFD0Directory;


public class PictureHandler {

	String tempFolder="temp";
	int frameCount =0;
	
	public PictureHandler(){
		//create temp folder (if it doesnt exist)
		File dir = new File(tempFolder);
		if(!dir.exists())
			dir.mkdir();
	}
	
	
	public BufferedImage resizeAndRenameImage(boolean resizing,File imageFile,int newHeight, int newWidth){
		
		BufferedImage image=null;
		try {
			image = ImageIO.read(imageFile);
			//check whether EXIF-Metadata says picture is orientated in landscape or normal mode and rotate accordingly
			if(isOrientationRotated(imageFile)){
				image = rotateImage270Degrees(image);//rotate90ToLeft(image);
				//System.out.println("image was rotated!");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(image != null){
		//resize image
		if(resizing)
			image = resizeImage(image,newHeight,newWidth);
			
		//structure newName of pictures in temp folder (count ids up)
		String newName = "img";
		if(frameCount<10)
			newName+="000"+frameCount;
		if(frameCount>=10 && frameCount<100)
			newName+="00"+frameCount;
		if(frameCount>=100 && frameCount<1000)
			newName+="0"+frameCount;
		if(frameCount>=1000)
			newName+=""+frameCount;

		newName+=".jpg";//TODO: make dynamic fileformat?!
	
		saveBufferedImageToFile(image,tempFolder+"/"+newName);
		frameCount++;
		}
		
		
		return image;
	}
	
	
	private BufferedImage rotateImage270Degrees(BufferedImage image) {
			
		  	AffineTransform tx = new AffineTransform();
		  	double angle = (Math.PI/2)*3;//270(90*3) degrees clockwise
	        tx.rotate(angle,image.getWidth() / 2, image.getHeight() / 2);//rotate in center of image

	        double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
	        int w = image.getWidth(), h = image.getHeight();
	        int neww = (int)Math.floor(w*cos+h*sin), newh = (int)Math.floor(h*cos+w*sin);
	        
	        
	        AffineTransform tat = new AffineTransform();
	        tat.translate((neww-w)/2, (newh-h)/2);
	        tx.preConcatenate(tat);
	        
	        AffineTransformOp op = new AffineTransformOp(tx,AffineTransformOp.TYPE_BILINEAR);
	        BufferedImage result = new BufferedImage(image.getHeight(),image.getWidth(),image.getType());
	        op.filter(image,result);//rotate image according to created affine transformation
	        return result;
	}
	
	public BufferedImage rotate90ToLeft( BufferedImage inputImage ){//slow naive solution
		//The most of code is same as before
			int width = inputImage.getWidth();
			int height = inputImage.getHeight();
			BufferedImage returnImage = new BufferedImage( height, width , inputImage.getType()  );
		//We have to change the width and height because when you rotate the image by 90 degree, the
		//width is height and height is width 	

			for( int x = 0; x < width; x++ ) {
				for( int y = 0; y < height; y++ ) {
					returnImage.setRGB(y, width - x - 1, inputImage.getRGB( x, y  )  );
		//Again check the Picture for better understanding
				}
				}
			return returnImage;

		}


	/**Check Orientation EXIF-Metadata of image with Metadata-Extractor library (https://github.com/drewnoakes/metadata-extractor)
	 * @param imageFile
	 * @return true if rotation because of values in EXIF-Metadata is needed, false if no EXIF-Metadata or no rotation is needed
	 */
	private boolean isOrientationRotated(File imageFile) {
		try {
			Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
			
			// obtain a specific directory
			ExifIFD0Directory exifDirectory
			    = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
			if(exifDirectory!=null){
				// create a descriptor
				ExifIFD0Descriptor descriptor
					= new ExifIFD0Descriptor(exifDirectory);
		   
				// get tag description
				String orientation = descriptor.getOrientationDescription();
				//System.out.println("orientation: "+orientation);
			
				if(orientation.startsWith("Left side, bottom")){//normal:Top, left side (Horizontal / normal) rotated:  
					return true;//okay rotate
				}
			}
			
		} catch (ImageProcessingException | IOException e) {
			// error while processing image, maybe no exif 
			e.printStackTrace();
		}
		
		return false;//no rotation needed (or no EXIF information available)
	}


	private BufferedImage resizeImage(BufferedImage original, int newHeight,
			int newWidth) {
		BufferedImage resized = new BufferedImage(newWidth, newHeight, original.getType());
	    Graphics2D g = resized.createGraphics();
	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    /*if(original.getWidth()>original.getHeight()){
	    	g.drawImage(original, 0, 0, newWidth, newHeight, 0, 0, original.getWidth(), original.getHeight(), null);
	    }
	    else{
	    	int oWidth = original.getWidth();
	    	int oHeight = original.getHeight();
	    	double ratio = oWidth/oHeight;
	    	int spacingWidth=0;
	    	int scaleWidth =;//?
	    	
	    	g.drawImage(original, 0, spacingWidth, scaleWidth,newHeight, 0, 0, oWidth, oHeight, null);
	    }*/
	    g.drawImage(original, 0, 0, newWidth, newHeight, 0, 0, original.getWidth(), original.getHeight(), null);
	    g.dispose();
	    original=null;
	    return resized;
	}


	private void saveBufferedImageToFile(BufferedImage image, String newFilePathName) {
		// create new file in tempfolder with newName
		File f = new File(newFilePathName);
		try {
			//write image to new file
			ImageIO.write(image, "JPEG", f);
		} catch (IOException e) {
			// something went wrong!
			e.printStackTrace();
		}
	}
	
}
