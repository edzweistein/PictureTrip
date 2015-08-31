import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class PictureTripGUI {
	
	JFrame _frame;
	JPanel _mainPanel;
	JTextField _tFramerate,_tWidth,_tHeight,_tFolderPathInfo;
	JButton _bSelectFolder,_bStart;
	
	public PictureTripGUI(){
		initGUI();
		
		showGUI();
	}

	private void showGUI() {
		_frame.setVisible(true);
	}

	private void initGUI() {
		_frame = new JFrame("PictureTrip 1.0");
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//preparing content pane
		_mainPanel = new JPanel();
		JPanel grid = new JPanel(new GridLayout(0,2));
		_mainPanel.setLayout(new BorderLayout());
		
		
		_bSelectFolder = new JButton("Select picture folder");
		grid.add(_bSelectFolder);
		
		_tFolderPathInfo = new JTextField("picture folder path...");
		grid.add(_tFolderPathInfo);
		
		_tWidth = new JTextField("1920");
		_tHeight = new JTextField("1080");
		grid.add(new JLabel("width:"));
		grid.add(_tWidth);
		grid.add(new JLabel("height:"));
		grid.add(_tHeight);
		
		_tFramerate = new JTextField("10");
		grid.add(new JLabel("framerate:"));
		grid.add(_tFramerate);
		
		_mainPanel.add(grid,BorderLayout.CENTER);
		
		_bStart = new JButton("Start!");
		_mainPanel.add(_bStart,BorderLayout.SOUTH);
		
		_frame.setContentPane(_mainPanel);
		_frame.pack();
		
		
	}
	
	public JButton getSelectFolderButton(){
		return _bSelectFolder;
	}
	
	public JButton getStartButton(){
		return _bStart;
	}
	
	public JTextField getPathText(){
		return _tFolderPathInfo;
	}
	
	public JTextField getWidthText(){
		return _tWidth;
	}
	
	public JTextField getHeightText(){
		return _tHeight;
	}
	
	public JTextField getFramerateText(){
		return _tFramerate;
	}
	
	public void showFolderPath(String path){
		if(_tFolderPathInfo != null){
			_tFolderPathInfo.setText(path);
		}
	}
	
	public JFrame getFrame(){
		return _frame;
	}
}
