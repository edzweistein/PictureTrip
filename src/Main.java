import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFileChooser;


/**Create a video slideshow of your journey pictures easily.
 * Based and dependent on ffmpeg ubuntu package and BufferedImage operations.
 * 
 * 
 * Todo: dont crop images resize them taking their ratio into account
 * @author edzweistein2
 *
 */
public class Main {

	private static PictureHandler _pictureHandler;
	private static PictureTripGUI _gui;
	//private static FFMPEGHandler _ffmpegHandler;
	
	
	//PictureTrip [path to imagefolder] [video width] [video height] [video framerate]
	public static void main(String[] args){
		//**GUI: choose picturefolder, choose video size, choose framerate, choose video format, check play in vlc when finished

		_pictureHandler = new PictureHandler();
		//default-werte
		String pictureFolder="/home/edzweistein2/Bilder/Hampi Indien/101CANON/test";
		int newWidth = 1920;
		int newHeight = 1080;
		int framerate = 10;

		
		
		if(args!=null){
			if(args.length==4)//arguments set per console
			{
				//first argument: folder
				pictureFolder = args[0];
				//second argument: new width of images and video size
				newWidth = Integer.parseInt(args[1]);
				//third argument: new height of images and video height
				newHeight = Integer.parseInt(args[2]);
				//fourth argument: video framerate
				framerate = Integer.parseInt(args[3]);
				
				startProcess(pictureFolder,framerate,newWidth,newHeight,true);
				
			}
			else if(args.length==2){
				//only picturefolder and framerate (no resizing of images)
				pictureFolder = args[0];
				framerate = Integer.parseInt(args[1]);
				startProcess(pictureFolder,framerate,newWidth,newHeight,false);
			}
			else if(args == null || args.length==0){
				//start GUI
				_gui = new PictureTripGUI();
				initActionListeners();
			}
				
		}
		
	}


	private static void startProcess(String pictureFolder, int framerate, int newWidth, int newHeight, boolean resizing) {
		if(pictureFolder != null){
			//***rename and resize images in a loop (counting numbers up discretely, without holes)
			
			//get file list from folder
			File imageFolder = new File(pictureFolder);
			File[] images = imageFolder.listFiles();
			//sort pictures 
			Arrays.sort(images);
			
			System.out.println("Resizing and renaming "+images.length+" pictures...");
			//loop through files and call pictureHandler resizeAndRename Function
			for(File image : images){
				_pictureHandler.resizeAndRenameImage(resizing,image, newHeight, newWidth);
			}
			System.out.println("Finished resizing and renaming "+images.length+" pictures!");
			
			System.out.println("Rendering video...");
			//tell ffmpeg to generate video from the images in this temp folder, with framerate and format
			FFMPEGHandler.createVideo(framerate, newHeight, newWidth, null);
				//call ffmpeghandler createVideo Function
			
			System.out.println("out.mp4 is finished :)");
			System.exit(0);
			//finished (opt: update GUI or open in VLC)
			}
			else{
				System.out.println("No picture folder defined!");
			}
	}


	private static void initActionListeners() {
		if(_gui != null){
		
			
		
		//Buttons
		JButton bSelectFolder = _gui.getSelectFolderButton();
		bSelectFolder.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//Select folder and tell gui to present and save path in pathinfo textfield
				JFileChooser folderChooser = new JFileChooser();
				String path = null;
				folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				int returnVal = folderChooser.showOpenDialog(_gui.getFrame());

	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File folder = folderChooser.getSelectedFile();
	                try {
						path = folder.getCanonicalPath();
						_gui.showFolderPath(path);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	               
	            }
	            
				
			}
		});
		
		
		JButton bStart = _gui.getStartButton();
		bStart.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String folderPath = _gui.getPathText().getText();
				//second argument: new width of images and video size
				int newWidth = Integer.parseInt(_gui.getWidthText().getText());
				//third argument: new height of images and video height
				int newHeight = Integer.parseInt(_gui.getHeightText().getText());
				//fourth argument: video framerate
				int framerate = Integer.parseInt(_gui.getFramerateText().getText());
				
				
				//TODO add joptionpane messages! user has to know whats going on.
				startProcess(folderPath,framerate,newWidth,newHeight,true);
			}
		});

				
		}
	}
}
