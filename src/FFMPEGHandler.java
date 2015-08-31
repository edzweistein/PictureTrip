import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class FFMPEGHandler {

	/**Create .MP4 video with the pictures as frames in the video. The framerate and the resolution of the video
	 * (pictures will be scaled to this size) can be set. The outputFolder defines where to save the resulting video.
	 * 
	 * @param pictureFolder Path to folder with images
	 * @param framerate 
	 * @param height
	 * @param width
	 * @param outputFolder
	 */
	public static void createVideo(int framerate,int height,int width,String outputFolder){
		String sFramerate = ""+framerate;//""+framerate;
		System.out.println("Rendering MP4 video with "+sFramerate+" framerate");
		String command = "ffmpeg -f image2 -r "+sFramerate+" -i "+"temp/img%04d.jpg"+" -c:v libx264 -pix_fmt yuv420p"+" out.mp4";//"ffmpeg -f image2 -framerate "+sFramerate+" -i "+"temp/img%04d.jpg"+" -c:v libx264 -r "+sFramerate+" -pix_fmt yuv420p"+" out.mp4";
		
		String s=null;
        try {
            
        	// run the Unix ffmpeg command
            // using the Runtime exec method:
            Process process = Runtime.getRuntime().exec(command);
            
            BufferedReader stdInput = new BufferedReader(new 
                 InputStreamReader(process.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(process.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
            
            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
            
            //System.exit(0);
        }
        catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            //System.exit(-1);
        }
    }
	
}
