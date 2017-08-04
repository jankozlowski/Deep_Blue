package DeepBluePackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;

public class Deep_Blue implements Runnable, ActionListener {

	
	private JFrame mainWindow,addWindow,selectWindow,editWindow,progresFrame;
	private JLayeredPane layout; 
	private JPanel componentList;
	private JButton add,edit, select,delete;
	private DefaultListModel<String> listModel;
	private BufferedImage blueprintImg;
	private JLabel Title, blueprint;
	private String selectedProject;
	private double imageScale;
	
	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	private final String DB_URL = "jdbc:mysql://localhost:3306/deep_blue";
	private final String USER = "****";
	private final String PASS = "****";
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Deep_Blue());
	}

	@Override
	public void run() {
		
		try {
			UIManager.setLookAndFeel("com.alee.laf.WebLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		mainWindow = new JFrame("Deep Blue");
	    mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    imageScale=1;
	     
	    progresFrame = new JFrame();
	    progresFrame.setLayout(new BoxLayout(progresFrame.getContentPane(),BoxLayout.PAGE_AXIS));
	    JLabel conecting = new JLabel("Connecting to remote database");
	     
		JProgressBar progress = new JProgressBar(0,100);
		progress.setIndeterminate(true);
			
		progresFrame.add(conecting);
		progresFrame.add(progress);
		progresFrame.pack();
			
	     BufferedImage backgroundImg = null;
	     blueprintImg = null;
	     BufferedImage boxImg = null;
	     BufferedImage exitImg = null;
	     BufferedImage zoomInImg = null;
	     BufferedImage zoomOutImg = null;
	     try {
	    	 backgroundImg = ImageIO.read(new File("img/circuits-electronic_00377126.png"));
	    	 blueprintImg = ImageIO.read(new File("img/blueprint.png"));
	    	 boxImg = ImageIO.read(new File("img/Electronics.png"));
	    	 exitImg = ImageIO.read(new File("img/shutdown.png"));
	    	 zoomInImg = ImageIO.read(new File("img/photo_zoom_in.png"));
	    	 zoomOutImg = ImageIO.read(new File("img/photo_zoom_out.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	     
	     JLabel background = new JLabel(new ImageIcon(backgroundImg));
	     blueprint = new JLabel(new ImageIcon(blueprintImg));
	     JLabel box = new JLabel(new ImageIcon(boxImg));
	     JLabel exit = new JLabel(new ImageIcon(exitImg));
	     JLabel zoomIn = new JLabel(new ImageIcon(zoomInImg));
	     JLabel zoomOut = new JLabel(new ImageIcon(zoomOutImg));
	     Title = new JLabel();
	     Title.setText("");
	     
	     final JScrollPane blueprintScroll = new JScrollPane(blueprint);
	     
	     background.setBounds(0,0,1024,720);
	     blueprintScroll.setBounds(50,25,675,600);
	     box.setBounds(750,25,240,400);
	     exit.setBounds(830,585,72,72);
	     zoomIn.setBounds(10,40,32,32);
	     zoomOut.setBounds(10,80,32,32);
	     Title.setBounds(20,630,500,50);
	     
	     zoomIn.addMouseListener(new MouseAdapter(){
	    	 
	    	 public void mouseClicked(MouseEvent event){
	    		 imageScale=imageScale+0.15;
	    		 BufferedImage changeBlueprintImg=Scalr.resize(blueprintImg, Method.SPEED, (int)(blueprintImg.getWidth()*imageScale), Scalr.OP_ANTIALIAS, Scalr.OP_BRIGHTER);
	    		 blueprint.setIcon(new ImageIcon(changeBlueprintImg));
	    		 mainWindow.repaint();
	    	 }
	    	 
	     });
	     
	     zoomOut.addMouseListener(new MouseAdapter(){
	    	 
	    	 public void mouseClicked(MouseEvent event){
	    		 if(imageScale>0.16){
		    		 imageScale=imageScale-0.15;
		    		 BufferedImage changeBlueprintImg=Scalr.resize(blueprintImg, Method.QUALITY, (int)(blueprintImg.getWidth()*imageScale), Scalr.OP_ANTIALIAS, Scalr.OP_BRIGHTER);
		    		 blueprint.setIcon(new ImageIcon(changeBlueprintImg));
		    		 mainWindow.repaint();
	    		 }
	    	 }
	    	 
	     });
	     
	     exit.addMouseListener(new MouseAdapter(){
	    	 
	    	 public void mouseClicked(MouseEvent event){
	    		 System.exit(0);
	    	 }
	    	 
	     });
	     
	     Font font = null;
	     try {
			font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File("img/DS-DIGI.ttf"))).deriveFont(Font.PLAIN,42);
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     Title.setFont(font);
	     Title.setForeground(new Color(33,100,255));
	     
	     add = new JButton("Add");
	     add.setBounds(755,450,100,50);
	     add.addActionListener(this);
	     edit = new JButton("Edit");
	     edit.setBounds(885,450,100,50);
	     edit.addActionListener(this);
	     delete = new JButton("Delete");
	     delete.setBounds(755,520,100,50);
	     delete.addActionListener(this);
	     select = new JButton("Select");
	     select.setBounds(885,520,100,50);
	     select.addActionListener(this);
	     
	     layout = new JLayeredPane();
	    
	     layout.add(add,2);
	     layout.add(edit,2);
	     layout.add(delete,2);
	     layout.add(select,2);
	     layout.add(exit,3);

	     layout.add(background,5);
	     layout.add(box,1);
	     layout.add(zoomIn,4);
	     layout.add(zoomOut,4);
	     layout.add(blueprintScroll,1);
	     layout.add(Title,1);
	     
	     mainWindow.add(layout);
	     mainWindow.setSize(1024, 720);
	     mainWindow.setResizable(false);
	     mainWindow.setVisible(true);
	     progresFrame.setLocationRelativeTo(mainWindow);
		
	}
	
	
	public void addSchematic(){

		addWindow = new JFrame("Add");
		 
		 BufferedImage SelectFileImage = null;

		 try {
			SelectFileImage = ImageIO.read(new File("img/open-file-icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		 
	     BoxLayout boxLay = new BoxLayout(addWindow.getContentPane(), BoxLayout.PAGE_AXIS);
	     addWindow.setLayout(boxLay);
	     
	     JLabel labelTitle = new JLabel("Title:");
	     JLabel labelImage = new JLabel("Image:");
	     JLabel labelComponents = new JLabel("Components:");
	     JLabel labelFileChoser = new JLabel(new ImageIcon(SelectFileImage));
	     final JTextField schemaTitle = new JTextField(19);
	     final JTextField schemaImage = new JTextField(19);
	     

	     listModel = new DefaultListModel<String>();

	     final JList<String> schemaComponents = new JList<String>(listModel);
	     JButton addSchema = new JButton("Add");
	     JButton closeSchema = new JButton("Close");
	     schemaComponents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	     JScrollPane schemaScroll = new JScrollPane(schemaComponents);
	     
	     labelFileChoser.addMouseListener(new MouseAdapter()
	        {
	            public void mouseClicked(MouseEvent event)
	            {
	       	     JFileChooser fc = new JFileChooser();
	       	     fc.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "bmp", "jpeg", "wbmp", "png", "gif"));
	    	     fc.setFileFilter(fc.getChoosableFileFilters()[1]);
	       	     fc.showOpenDialog(addWindow);
	    	     File selectedFile = fc.getSelectedFile();
	             if (selectedFile != null) {
	                String path = selectedFile.getAbsolutePath();
	                schemaImage.setText(path);
	             }
	            }
	        });

	     addSchema.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				JProgressBar progress = new JProgressBar(0,100);
				progress.setIndeterminate(true);
				progress.setVisible(true);
				addWindow.dispose();
				
				addToDatabaseTask task = new addToDatabaseTask(schemaTitle.getText(),schemaImage.getText());
				task.execute();
			
			}
	     });
	     
	     closeSchema.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				
				addWindow.dispose();
			}
	     });
	     
	     JPanel topContainer = new JPanel(new BorderLayout());
	     JPanel topRows = new JPanel(new GridBagLayout());

	     GridBagConstraints gbc = new GridBagConstraints();
	     gbc.weightx = 1.0;
	     gbc.weighty = 1.0;
	     gbc.insets = new Insets(3,3,3,3);
	     gbc.gridx = 0;
	     gbc.gridy = 0;
	     topRows.add(labelTitle, gbc);
	     gbc.gridx = 1;
	     gbc.gridy = 0;
	     topRows.add(schemaTitle, gbc);
	     gbc.gridx = 0;
	     gbc.gridy = 1;
	     topRows.add(labelImage, gbc);
	     gbc.gridx = 1;
	     gbc.gridy = 1;
	     topRows.add(schemaImage, gbc);
	     gbc.gridx = 2;
	     gbc.gridy = 1;
	     topRows.add(labelFileChoser, gbc);
	     gbc.gridx = 0;
	     gbc.gridy = 2;
	     gbc.gridwidth = 2;
	     gbc.anchor = GridBagConstraints.WEST;
	     topRows.add(labelComponents, gbc);
	     
	     JPanel addClose = new JPanel();
	     addClose.setLayout(new FlowLayout());
	     addClose.add(addSchema);
	     addClose.add(closeSchema);
	     JPanel schemaList = new JPanel();
	     schemaList.setBorder(new EmptyBorder(0,10,0,10));
	     schemaList.setBackground(new Color(238, 238, 238));
	     schemaList.setLayout(new BorderLayout());
	     schemaList.add(schemaScroll);
	     
	     BufferedImage addImg = null;
	     BufferedImage editImg = null;
	     BufferedImage deleteImg = null;
	     try {
	    	 addImg = ImageIO.read(new File("img/add.png"));
	    	 editImg = ImageIO.read(new File("img/document_edit.png"));
	    	 deleteImg = ImageIO.read(new File("img/fileclose.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	     
	     JLabel addLabel = new JLabel(new ImageIcon(addImg));
	     JLabel editLabel = new JLabel(new ImageIcon(editImg));
	     JLabel deleteLabel = new JLabel(new ImageIcon(deleteImg));
	     
	     addLabel.addMouseListener(new MouseAdapter()
	        {
	            public void mouseClicked(MouseEvent event)
	            {
	            	addSingleComponent();
	    	 }
	     });
	     
	     editLabel.addMouseListener(new MouseAdapter()
	        {
	            public void mouseClicked(MouseEvent event)
	            {
	            	int num = schemaComponents.getSelectedIndex();
	            	if(num == -1){
	            		JOptionPane.showMessageDialog(null, "No component selected", "No selection", 0);
	            		return;
	            	}

	            	Object[] table = getNameandNumber(num); 
	            	
	            	editSingleComponent(table[0].toString(), Integer.valueOf(table[1].toString()), num);
	    	 }
	     });
	     
	     deleteLabel.addMouseListener(new MouseAdapter()
	        {
	            public void mouseClicked(MouseEvent event)
	            {
	            	try{
	            	listModel.remove(schemaComponents.getSelectedIndex());
	            	}
	            	catch(ArrayIndexOutOfBoundsException e){
	            		JOptionPane.showMessageDialog(null, "No component selected", "No selection", 0);
	            	}
	    	 }
	     });
	     
	     JPanel addEditDelete = new JPanel();
	     addEditDelete.setBorder(new EmptyBorder(0,0,0,10));
	     addEditDelete.setLayout(new BoxLayout(addEditDelete, BoxLayout.PAGE_AXIS));
	     addEditDelete.add(addLabel);
	     addEditDelete.add(Box.createRigidArea(new Dimension(0, 5)));
	     addEditDelete.add(editLabel);
	     addEditDelete.add(Box.createRigidArea(new Dimension(0, 5)));
	     addEditDelete.add(deleteLabel);
	     
	     topContainer.add(topRows,BorderLayout.NORTH);
	     topContainer.add(schemaList,BorderLayout.CENTER);
	     topContainer.add(addEditDelete,BorderLayout.EAST);
	     
	     addWindow.add(topContainer);
	     addWindow.add(addClose);

	     addWindow.setSize(300, 300);
	     addWindow.setResizable(false);
	     addWindow.setVisible(true);
	     addWindow.setLocationRelativeTo(mainWindow);
		
	}
	
	public void editSchematic(){
		editWindow = new JFrame("Edit");
		 
		 BufferedImage SelectFileImage = null;

		 try {
			SelectFileImage = ImageIO.read(new File("img/open-file-icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	     BoxLayout boxLay = new BoxLayout(editWindow.getContentPane(), BoxLayout.PAGE_AXIS);
	     editWindow.setLayout(boxLay);
	     
	     JLabel labelTitle = new JLabel("Title:");
	     JLabel labelImage = new JLabel("Image:");
	     JLabel labelComponents = new JLabel("Components:");
	     JLabel labelFileChoser = new JLabel(new ImageIcon(SelectFileImage));
	     final JTextField schemaTitle = new JTextField(19);
	     final JTextField schemaImage = new JTextField(19);
	     
	     DefaultListModel<String> ids = searchDatabase("SELECT id FROM Project WHERE Title='"+selectedProject+"'","id");
	     int id = Integer.valueOf(ids.get(0));
	     final DefaultListModel<String> img = searchDatabase("SELECT image FROM Project WHERE Title='"+selectedProject+"'","Image");
	     
	     schemaTitle.setText(this.Title.getText());
	     schemaImage.setText(img.get(0));
	     final String findTitle=this.Title.getText();
	     
	     listModel = searchDatabase("SELECT item FROM Components Where project_id="+id, "item");
	     DefaultListModel<String> numbers = searchDatabase("SELECT quantity FROM Components WHERE project_id='"+id+"'","quantity");
	     for(int a=0; a<listModel.size(); a++){
	    	 listModel.set(a, listModel.get(a) + " x"+ numbers.get(a));
	     }
	     
	     final JList<String> schemaComponents = new JList<String>(listModel);
	     JButton EditSchema = new JButton("Edit");
	     JButton closeSchema = new JButton("Close");
	     schemaComponents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	     JScrollPane schemaScroll = new JScrollPane(schemaComponents);
	     
	     
	     labelFileChoser.addMouseListener(new MouseAdapter()
	        {
	            public void mouseClicked(MouseEvent event)
	            {
	       	     JFileChooser fc = new JFileChooser();
	       	     fc.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "bmp", "jpeg", "wbmp", "png", "gif"));
	    	     fc.setFileFilter(fc.getChoosableFileFilters()[1]);
	       	     fc.showOpenDialog(editWindow);
	    	     File selectedFile = fc.getSelectedFile();
	             if (selectedFile != null) {
	                String path = selectedFile.getAbsolutePath();
	                schemaImage.setText(path);
	             }
	            }
	        });

	     EditSchema.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				JProgressBar progress = new JProgressBar(0,100);
				progress.setIndeterminate(true);
				progress.setVisible(true);
				editWindow.dispose();
				
				String imageString =  null;
				if(!(schemaImage.getText().equals(img.get(0)))){
					imageString = schemaImage.getText();
					copyImage(schemaImage.getText());
					Path path = Paths.get("img/schemas/"+img.get(0));
		    		try {
						java.nio.file.Files.deleteIfExists(path);
					} catch (IOException e) {
						e.printStackTrace();
					}
					imageString = imageNameCut(imageString);
					
				}
				else{
					imageString = img.get(0);
				}
				progresFrame.setVisible(true);
				editProjectTask task = new editProjectTask(schemaTitle.getText(),imageString,findTitle);
				task.execute();
				Title.setText(schemaTitle.getText());
	    		try {
	    			blueprintImg = ImageIO.read(new File("img/schemas/"+imageString));
	    			blueprint.setIcon(new ImageIcon(blueprintImg));
	    			mainWindow.repaint();
	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		}
			
			}
	     });
	     
	     closeSchema.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				
				editWindow.dispose();
			}
	     });
	     
	     JPanel topContainer = new JPanel(new BorderLayout());
	     JPanel topRows = new JPanel(new GridBagLayout());

	     GridBagConstraints gbc = new GridBagConstraints();
	     gbc.weightx = 1.0;
	     gbc.weighty = 1.0;
	     gbc.insets = new Insets(3,3,3,3);
	     gbc.gridx = 0;
	     gbc.gridy = 0;
	     topRows.add(labelTitle, gbc);
	     gbc.gridx = 1;
	     gbc.gridy = 0;
	     topRows.add(schemaTitle, gbc);
	     gbc.gridx = 0;
	     gbc.gridy = 1;
	     topRows.add(labelImage, gbc);
	     gbc.gridx = 1;
	     gbc.gridy = 1;
	     topRows.add(schemaImage, gbc);
	     gbc.gridx = 2;
	     gbc.gridy = 1;
	     topRows.add(labelFileChoser, gbc);
	     gbc.gridx = 0;
	     gbc.gridy = 2;
	     gbc.gridwidth = 2;
	     gbc.anchor = GridBagConstraints.WEST;
	     topRows.add(labelComponents, gbc);
	     
	     JPanel addClose = new JPanel();
	     addClose.setLayout(new FlowLayout());
	     addClose.add(EditSchema);
	     addClose.add(closeSchema);
	     JPanel schemaList = new JPanel();
	     schemaList.setBorder(new EmptyBorder(0,10,0,10));
	     schemaList.setBackground(new Color(238, 238, 238));
	     schemaList.setLayout(new BorderLayout());
	     schemaList.add(schemaScroll);
	     
	     BufferedImage addImg = null;
	     BufferedImage editImg = null;
	     BufferedImage deleteImg = null;
	     try {
	    	 addImg = ImageIO.read(new File("img/add.png"));
	    	 editImg = ImageIO.read(new File("img/document_edit.png"));
	    	 deleteImg = ImageIO.read(new File("img/fileclose.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	     
	     JLabel addLabel = new JLabel(new ImageIcon(addImg));
	     JLabel editLabel = new JLabel(new ImageIcon(editImg));
	     JLabel deleteLabel = new JLabel(new ImageIcon(deleteImg));
	     
	     addLabel.addMouseListener(new MouseAdapter()
	        {
	            public void mouseClicked(MouseEvent event)
	            {
	            	addSingleComponent();
	    	 }
	     });
	     
	     editLabel.addMouseListener(new MouseAdapter()
	        {
	            public void mouseClicked(MouseEvent event)
	            {
	            	int num = schemaComponents.getSelectedIndex();
	            	if(num == -1){
	            		JOptionPane.showMessageDialog(null, "No component selected", "No selection", 0);
	            		return;
	            	}

	            	Object[] table = getNameandNumber(num); 
	            	
	            	editSingleComponent(table[0].toString(), Integer.valueOf(table[1].toString()), num);
	    	 }
	     });
	     
	     deleteLabel.addMouseListener(new MouseAdapter()
	        {
	            public void mouseClicked(MouseEvent event)
	            {
	            	try{
	            	listModel.remove(schemaComponents.getSelectedIndex());
	            	}
	            	catch(ArrayIndexOutOfBoundsException e){
	            		JOptionPane.showMessageDialog(null, "No component selected", "No selection", 0);
	            	}
	    	 }
	     });
	     
	     JPanel addEditDelete = new JPanel();
	     addEditDelete.setBorder(new EmptyBorder(0,0,0,10));
	     addEditDelete.setLayout(new BoxLayout(addEditDelete, BoxLayout.PAGE_AXIS));
	     addEditDelete.add(addLabel);
	     addEditDelete.add(Box.createRigidArea(new Dimension(0, 5)));
	     addEditDelete.add(editLabel);
	     addEditDelete.add(Box.createRigidArea(new Dimension(0, 5)));
	     addEditDelete.add(deleteLabel);
	     
	     topContainer.add(topRows,BorderLayout.NORTH);
	     topContainer.add(schemaList,BorderLayout.CENTER);
	     topContainer.add(addEditDelete,BorderLayout.EAST);
	     
	     editWindow.add(topContainer);
	     editWindow.add(addClose);

	     editWindow.setSize(300, 300);
	     editWindow.setResizable(false);
	     editWindow.setVisible(true);
	     editWindow.setLocationRelativeTo(mainWindow);
	     progresFrame.setVisible(false);
	}
	
	
	public void selectSchematic(){
	     selectWindow= new JFrame("Select");
	     selectWindow.setLayout(new BorderLayout());
	     DefaultListModel<String> listModel = new DefaultListModel<String>();
	     listModel = searchDatabase("SELECT Title From Project", "Title");
	 
	     	final JList<String> list = new JList<String>(listModel);
	        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	       // list.addListSelectionListener(this);
	        JScrollPane scroll = new JScrollPane(list);

	        JPanel SelectClose = new JPanel();
	        JButton Select = new JButton("Select");
	        Select.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					changeProjectTask task = new changeProjectTask(list.getModel().getElementAt(list.getSelectedIndex()));
					task.execute();
					selectWindow.dispose();
				}
	        });
	        
	        JButton Close = new JButton("Close");
	        
	        Close.addActionListener( new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					selectWindow.dispose();
				}
	        	
	        });
	        
	        SelectClose.setLayout(new FlowLayout());
	        
	        SelectClose.add(Select);
	        SelectClose.add(Close);
	        
	        
	     selectWindow.add(scroll,BorderLayout.CENTER);
	     selectWindow.add(SelectClose,BorderLayout.SOUTH);

	     selectWindow.setSize(300, 300);
	     selectWindow.setResizable(false);
	     
	     selectWindow.setVisible(true);
	     selectWindow.setLocationRelativeTo(mainWindow);
	}
	
	public void changeProject(String Title){
		
		imageScale=1;
		progresFrame.setVisible(true);
		DefaultListModel<String> img = searchDatabase("SELECT image FROM Project WHERE Title='"+Title+"'", "image");
		
		try {
			blueprintImg = ImageIO.read(new File("img/schemas/"+img.get(0)));
			blueprint.setIcon(new ImageIcon(blueprintImg));
			mainWindow.repaint();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.Title.setText(Title);
		
		if(componentList!=null){
			layout.remove(componentList);
		}
		componentList = new JPanel();
		componentList.setLayout(new BorderLayout());

		
		DefaultListModel<String> ids = searchDatabase("SELECT id FROM Project WHERE Title='"+Title+"'", "id");
		int id = Integer.valueOf(ids.get(0));
		
		DefaultListModel<String> listModel = new DefaultListModel<String>();
	    listModel = searchDatabase("SELECT Item From Components WHERE project_id="+id, "Item");
	    DefaultListModel<String> numbers = searchDatabase("SELECT quantity FROM Components WHERE project_id='"+id+"'","quantity");
	     for(int a=0; a<listModel.size(); a++){
	    	 listModel.set(a, listModel.get(a) + " x"+ numbers.get(a));
	     }
	    
	    
	    JList<String> list = new JList<String>(listModel);
	    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    JScrollPane scroll = new JScrollPane(list);
		componentList.add(scroll);
		componentList.setBounds(750,25,240,400);
		componentList.setBackground(new Color(0,0,0,65));
		
		layout.add(componentList,1);
		mainWindow.repaint();
		selectedProject=Title;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
        if (source == add) {
            addSchematic();
        }
        else if(source == edit){
        	if(selectedProject==null){
        		JOptionPane.showMessageDialog(null, "No schema selected", "No selection", 0);
        	}
        	else{
        		progresFrame.setVisible(true);
        		editSchemaTask task = new editSchemaTask();
        		task.execute();
        	}
        }
        else if(source == delete){
        	if(selectedProject==null){
        		JOptionPane.showMessageDialog(null, "No schema selected", "No selection", 0);
        	}
        	else{
        		progresFrame.setVisible(true);
        		deleteProjectTask task = new deleteProjectTask();
        		task.execute();
        	}
        }
        else if(source == select){
        	progresFrame.setVisible(true);
        	SearchDatabaseTask task = new SearchDatabaseTask();
        	task.execute();
        }
		
	}
	
	
	
	
	
	
	public String imageNameCut(String path){
		 StringBuilder tempPath = new StringBuilder();
		 tempPath.append(path);
		 int numberOfLetters = 0;
		 for(int a=tempPath.length()-1; a>0; a--){
			 if(tempPath.charAt(a)!='/' && tempPath.charAt(a)!='\\'){
				 numberOfLetters++;
			 }
			 else{
				 break;
			 }	 
		 }
		 
		 String filename = tempPath.substring(tempPath.length()-numberOfLetters, tempPath.length());
		 return filename;
	}
	
	public boolean copyImage(String path){
		 BufferedImage SelectFileImage = null;

		 String filename = imageNameCut(path);
		 String ext = filename.substring(filename.lastIndexOf('.')+1);
		 
   		 try {
   			SelectFileImage = ImageIO.read(new File(path));
   			File file = new File("img/schemas/"+filename);
   			if(file.exists()){
   				int optionPane = JOptionPane.showConfirmDialog(null,"Image With Such name already exists do you want overwrite it?",
   					    "Overwrite?",
   					    JOptionPane.YES_NO_OPTION,
   					    JOptionPane.QUESTION_MESSAGE);
   				if(optionPane == JOptionPane.OK_OPTION){
   		   			File outputfile = new File("img/schemas/"+filename);
   		   			ImageIO.write(SelectFileImage, ext, outputfile);
   		   			return true;
   				}
   				else{
   					return false;
   				}
   			}
   			else{
	   			File outputfile = new File("img/schemas/"+filename);
	   			ImageIO.write(SelectFileImage, ext, outputfile);
	   			return true;
   			}
   		} catch (IOException e) {
   			e.printStackTrace();
   		}
		return false;
	}
	
	public Object[] getNameandNumber(int num){
    	String name = listModel.get(num);
    	StringBuilder modify = new StringBuilder();
    	modify.append(name);
    	int b =0;
    	
    	for(int a=modify.length()-1; a>0; a--){
    		if(modify.charAt(a)!='x'){
    			b++;
    		}
    		else{
    			break;
    		}
    	}
    	
    	int number = Integer.valueOf(modify.substring(modify.length()-b));
    	
    	modify.delete(modify.length()-b-2, modify.length());
    	name = modify.toString();
		
    	Object[] returnTable = new Object[2];
    	returnTable[0]= name;
    	returnTable[1]= number;
    	
    	return returnTable;
		
	}
	
	public void addSingleComponent(){
		 final JFrame oneComponent = new JFrame("Add Component");
		 oneComponent.setLayout(new BoxLayout(oneComponent.getContentPane(), BoxLayout.PAGE_AXIS));
		 final JTextField newComponent = new JTextField(25);
		 final SpinnerNumberModel snm = new SpinnerNumberModel(1,0,999,1);
		 JSpinner quantity = new JSpinner(snm);
		 JButton add = new JButton("Add");
		 add.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent event) {
					listModel.addElement(newComponent.getText()+" x" +snm.getValue());
					oneComponent.dispose();
				}
		 });
		 
		 JButton close = new JButton("Close");
		 close.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent event) {
					
					oneComponent.dispose();
					
				}
		     });
		 
		 JPanel infoFields = new JPanel();
		 infoFields.setLayout(new FlowLayout());
		 infoFields.add(newComponent);
		 infoFields.add(quantity);
		 JPanel closeFields = new JPanel();
		 closeFields.setLayout(new FlowLayout());
		 closeFields.add(add);
		 closeFields.add(close);
		 
		 oneComponent.add(infoFields);
		 oneComponent.add(closeFields);
		 
		 oneComponent.setSize(400, 100);
		 oneComponent.setResizable(false);
		 oneComponent.setVisible(true);
		 oneComponent.setLocationRelativeTo(addWindow);
	}
	
	public void editSingleComponent(String name, int number, final int num){
		 final JFrame oneComponent = new JFrame("Edit Component");
		 oneComponent.setLayout(new BoxLayout(oneComponent.getContentPane(), BoxLayout.PAGE_AXIS));
		 final JTextField newComponent = new JTextField(25);
		 newComponent.setText(name);
		 final SpinnerNumberModel snm = new SpinnerNumberModel(1,0,999,1);
		 JSpinner quantity = new JSpinner(snm);
		 quantity.setValue(number);
		 JButton add = new JButton("Edit");
		 add.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent event) {
					listModel.set(num, newComponent.getText()+" x" +snm.getValue());
					oneComponent.dispose();
				}
		 });
		 
		 JButton close = new JButton("Close");
		 close.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent event) {
					
					oneComponent.dispose();
					
				}
		     });
		 
		 JPanel infoFields = new JPanel();
		 infoFields.setLayout(new FlowLayout());
		 infoFields.add(newComponent);
		 infoFields.add(quantity);
		 JPanel closeFields = new JPanel();
		 closeFields.setLayout(new FlowLayout());
		 closeFields.add(add);
		 closeFields.add(close);
		 
		 oneComponent.add(infoFields);
		 oneComponent.add(closeFields);
		 
		 oneComponent.setSize(400, 100);
		 oneComponent.setResizable(false);
		 oneComponent.setVisible(true);
		 oneComponent.setLocationRelativeTo(addWindow);
	}
	
	
	
	
	
	
	
	
	//Database Functions
	public DefaultListModel<String> searchDatabase(String order, String What){
		
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery(order);
			
			
			while(rs.next()){
				listModel.addElement(rs.getString(What));
			}
			
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();

		} catch (SQLException e1) {
			e1.printStackTrace();
		}finally{
		      try{
		         if(stmt!=null)
		            conn.close();
		      }catch(SQLException se){
		      }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		      }
		}
		return listModel;
	}
	
	public void doInDatabase(String order){
		
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			
			 stmt = conn.createStatement();
			
			stmt.executeUpdate(order);
			
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();

		} catch (SQLException e1) {
			e1.printStackTrace();
		}finally{
		      try{
		         if(stmt!=null)
		            conn.close();
		      }catch(SQLException se){
		      }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		      }
		}
	}

	
	
	
	
	
	
	
	
	//Classes for new Threads
	
	class addToDatabaseTask extends SwingWorker<Void,Void> {

		String Title, Image;
		
		addToDatabaseTask(String Title, String Image){
			
			this.Title=Title;
			this.Image=Image;

		}
		
		@Override
		protected Void doInBackground() throws Exception {
			boolean proced = copyImage(Image);
			
			Image=imageNameCut(Image);
			
			if(proced==true){
				progresFrame.setVisible(true);
				doInDatabase("INSERT INTO Project (Title,Image) VALUES" + "('" + Title +"','" + Image +"')");
				DefaultListModel<String> ids = searchDatabase("SELECT id FROM Project WHERE Title='"+Title+"'","id");
				int id = Integer.valueOf(ids.get(0));
				for(int a=0; a<listModel.size(); a++){
					Object[] table = getNameandNumber(a);
					doInDatabase("INSERT INTO Components (project_id,item,quantity) VALUES" + "('" + id +"','" + table[0].toString() + "','" + Integer.valueOf(table[1].toString()) + "')");
				}			
			}
			
			
			
			progresFrame.setVisible(false);
			return null;
		}
		
	}
	
	
	class SearchDatabaseTask extends SwingWorker<Void,Void> {

		@Override
		protected Void doInBackground() throws Exception {
			
			selectSchematic();
			progresFrame.setVisible(false);
			return null;
		}
	}
	
	
	class changeProjectTask extends SwingWorker<Void,Void> {

		String title;
		
		changeProjectTask(String title){
			this.title = title;
		}
		
		@Override
		protected Void doInBackground() throws Exception {
			
			changeProject(title);
			progresFrame.setVisible(false);
			return null;
		}
	}
	
	class deleteProjectTask extends SwingWorker<Void,Void>{

		@Override
		protected Void doInBackground() throws Exception {
    		DefaultListModel<String> img = searchDatabase("SELECT image FROM Project WHERE Title='"+selectedProject+"'","Image");
    		doInDatabase("DELETE FROM Project WHERE Title='"+selectedProject+"'");
    		selectedProject=null;
    		try {
    			blueprintImg = ImageIO.read(new File("img/blueprint.png"));
    			blueprint.setIcon(new ImageIcon(blueprintImg));
    			layout.remove(componentList);
    			mainWindow.repaint();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		
    		Path path = Paths.get("img/schemas/"+img.get(0));
    		try {
				java.nio.file.Files.deleteIfExists(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
    		Title.setText("");
    		progresFrame.setVisible(false);
			return null;
		}
		
	}
	
	class editProjectTask extends SwingWorker<Void,Void>{

		String Title;
		String Image;
		String FindTitle;
		
		editProjectTask(String Title, String Image, String FindTitle){
			this.Title = Title;
			this.Image = Image;
			this.FindTitle = FindTitle;
		}
		
		@Override
		protected Void doInBackground() throws Exception {
			DefaultListModel<String> ids = searchDatabase("SELECT id FROM Project WHERE Title='"+FindTitle+"'","id");
			int id = Integer.valueOf(ids.get(0));
			
			doInDatabase("Update Project SET Title='"+Title+"', Image='"+Image+"' WHERE Title='"+FindTitle+"'");
		    doInDatabase("DELETE FROM Components WHERE project_id="+id);
			
			for(int a=0; a<listModel.size(); a++){
				Object table[] = getNameandNumber(a);
		    	 doInDatabase("INSERT INTO Components (project_id,item,quantity) Values ('"+id+"', '"+table[0] +"', '" + table[1] +"')");
		     }
			progresFrame.setVisible(false);

			changeProjectTask task2 = new changeProjectTask(Title);
      		task2.execute();
			
			return null;
		}
		
		
	}
	
	class editSchemaTask extends SwingWorker<Void,Void>{

		@Override
		protected Void doInBackground() throws Exception {
			editSchematic();

			return null;
		}
		
	}
	
	
}










//Created Tables
/*

create table Project(
id int not null auto_increment,
Title varchar(200),
Image varchar(200),
CONSTRAINT project_pk PRIMARY KEY(id),
CONSTRAINT project_title_un UNIQUE(Title)
)

create table Components(
id int not null auto_increment,
project_id int not null,
item varchar(1000),
quantity integer,
CONSTRAINT components_pk PRIMARY KEY(id),
CONSTRAINT components_id_fk FOREIGN KEY(project_id) REFERENCES Project(id) ON UPDATE CASCADE ON DELETE CASCADE
)

*/