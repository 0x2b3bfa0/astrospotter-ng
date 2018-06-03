import java.io.*;
import java.lang.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.util.*;
import java.util.concurrent.*;


public class ASUserInterface extends JFrame{
	public Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public String PATH; //= new String("C:\\Users\\gonzalez\\Desktop\\Investigacio\\AstroSpotter");
	public String IMPATH = new String("\\src\\pic\\");

	public int W = screenSize.width;
	public int H = (screenSize.height-40);
	
	public JPanel top, bot, mid1, mid2a, mid2b, mid2c, mid3a, mid3b;
	
	public JTextField[] picIn = new JTextField[5];
	public JLabel errorIn;

	public String[] ImNames = new String[5];
	public String formatIt;

	public JLabel[] Images = new JLabel[5];
	public int ImVi = 0, nObj = 0;

	public JButton Right, Back;

	public JLabel StackIm, lumGIm, cenGIm;
	public int viStack = 0;
	public int viLum = 0;
	public int viCen = 0;

	public double[] decl;
	public double[] vel;
	public double[] eccen;

	public JLabel[] output1;

	public JLabel[] charg;
	public JLabel chargb, chargt;
	public int charged = 0;

	public void compileEverything2(){
		try{
			Process input = new ProcessBuilder(PATH + "\\src\\passImages.exe").start();
		}
		catch(IOException asd){
			System.out.print("PASSIMAGES ERROR");
		}
    	try{
    		TimeUnit.MILLISECONDS.sleep(1000);
		}
		catch(InterruptedException e){
			System.out.print("TIME EXCEPTION");
		}


		try{
			Process compute = new ProcessBuilder(PATH + "\\src\\ASProcess.exe").start();
		}
		catch(IOException asd){
			System.out.print("PROCESS ERROR");
		}
    	try{
    		TimeUnit.MILLISECONDS.sleep(1000);
		}
		catch(InterruptedException e){
			System.out.print("TIME EXCEPTION");
		}
	}

	public void compileEverything(){
		ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();
    	s.scheduleWithFixedDelay(new Runnable(){
			int it = 0;
			public void run(){
				System.out.println(it);
				if(it == 0) {
		    		mid2a.setVisible(false);
		    		mid2b.setVisible(false);
		    		mid3b.setVisible(false);
		    		mid3a.setVisible(true);
		    		mid2c.setVisible(true);
						
					for(int i = 0; i<12; i++) charg[i].setVisible(false);
				}
				if(it == 3){
					try{
						Process graph1 = new ProcessBuilder(PATH + "\\src\\centerGraph.exe").start();
						Process graph2 = new ProcessBuilder(PATH + "\\src\\lumGraph.exe").start();
					}
					catch(IOException asd){
						System.out.print("GRAPH ERROR");
					}
			    	try{
			    		TimeUnit.MILLISECONDS.sleep(0);//14000);
					}
					catch(InterruptedException e){
						System.out.print("TIME EXCEPTION");
					}
				}
				if(it == 12){
					mid2a.setVisible(false);
					mid3a.setVisible(false);
					mid2b.setVisible(true);
					mid3b.setVisible(true);

					throw new RuntimeException();
					//return;
				}
				charg[it].setVisible(true);
	       		it++;
       		}
    	}, 0, 1000, TimeUnit.MILLISECONDS);
	}

	public void process(){
		try{
			charged = 0;
			String args =  	"5,"  +
							ImNames[0] + ',' +
							ImNames[1] + ',' +
							ImNames[2] + ',' +			
							ImNames[3] + ',' +
							ImNames[4];
			FileWriter _in = new FileWriter(PATH + "\\fil\\names.txt");
			_in.write(args);
			_in.close();
		}
		catch(IOException asd){
			System.out.println("NAME ERROR");
		}

		compileEverything();
		compileEverything2();

		try{
		    FileReader _out;
			_out = new FileReader(PATH + "\\fil\\process.txt");

			double s = 0;
			int c; int m = 0; int it = 0;
			nObj = 0;
			int co = 0; int pc = -1;
			while((c = _out.read()) != -1){
				if(((char) c) == ','){
					if(pc != -1) s /= Math.pow(10,co-pc);
					co = 0;
					if(m == 0){
						nObj = (int) s;
						decl = new double[nObj];
						vel = new double[nObj];
						eccen = new double[nObj];
						m = 1;
					}
					else if(m == 1){
						decl[it] = s;
						m = 2;
					}
					else if(m == 2){
						vel[it] = s;
						m = 1;
						it++;
						if(it == nObj){
							it = 0;
							m = 3;
						}
					}
					else if(m == 3){
						eccen[it] = s;
						it++;
					}
					s = 0;
				}
				else{
					if(((char) c) == '.'){
						pc = co;
					}
					else if('0' <= ((char) c) && ((char) c) <= '9'){
						s *= 10;
						s += c-'0';
						co++;
					}
				}
				System.out.print((char) c);
			}

			for(int i=0; i<nObj; i++) System.out.print(" " + decl[i]);
			System.out.println("");
			for(int i=0; i<nObj; i++) System.out.print(" " + vel[i]);
			System.out.println("");
			for(int i=0; i<nObj; i++) System.out.print(" " + eccen[i]);
			System.out.println("");
			_out.close();
		}
		catch(IOException e){
			System.out.println("FILE ERROR");
		}		

		try{
			File StackFile = new File(PATH + "\\src\\pic\\stack.bmp");
			
			//System.out.println(StackFile);
			
			Image _StackImage = ImageIO.read(StackFile);			
			_StackImage = _StackImage.getScaledInstance(W/3, W/3, Image.SCALE_FAST);
			ImageIcon __StackImage = new ImageIcon(_StackImage);
			StackIm = new JLabel(__StackImage);
			StackIm.setBounds((W/2 - W/3)/2+10, (W/2 - W/3)/5, W/3, W/3);
			StackIm.setVisible(false);
			mid2b.add(StackIm);
		}
		catch(IOException e){
			System.out.println("STACK IMAGE ERROR");			
		}
		
		try{
			File[] file = new File[5];
			Image[] _image = new Image[5];
			ImageIcon[] __image = new ImageIcon[5];
			
			try{
				for(int i=0; i<5; i++) Images[i].setVisible(false);
			}
			catch(NullPointerException e){

			}

			Images = new JLabel[5];
			for(int i=0; i<5; i++){
				file[i] = new File(PATH + IMPATH + i + ".bmp");
				
				_image[i] = ImageIO.read(file[i]);
				
				_image[i] = _image[i].getScaledInstance(W/3, W/3, Image.SCALE_FAST);
				
				__image[i] = new ImageIcon(_image[i]);
				
				Images[i] = new JLabel(__image[i]);
				if(i == 0) Images[i].setVisible(true);
				ImVi = 0;
				Images[i].setBounds((W/2 - W/3)/2+10, (W/2 - W/3)/5, W/3, W/3);
				mid2b.add(Images[i]);
			}
		}
		catch(IOException e){
			System.out.println("IMAGE ERROR");			
		}
		
		JLabel Output = new JLabel("OUTPUT", JLabel.CENTER);
		Output.setFont(new Font("Arial", 7, 80));
		Output.setBounds(5, 10, W/4 - 10, 2*H/20);
		Output.setVisible(true);
		mid3b.add(Output);

		try{
			JButton stackB = new JButton("S");		
			stackB.setFont(new Font("Arial", 7, H/30));
			stackB.setBounds(W/28, H - 6*H/20 - 70, W/19, H/20);
			stackB.setBackground(Color.WHITE);
			stackB.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e){
			    	viLum = 0;
			    	viCen = 0;
			    	if(viCen == 0 && viLum == 0 && viStack == 0){
			    		System.out.print("apago imagenes");
			    		for(int i=0; i<Images.length; i++) Images[i].setVisible(false);	
			    	}
			    	else if(viCen == 0 && viLum == 0 && viStack == 1){
			    		Images[ImVi].setVisible(true);	
			    	}

			    	if(viStack == 0) viStack = 1;
			    	else viStack = 0;
			    	
			    	StackIm.setVisible(false);
			    	try{
			    		lumGIm.setVisible(false);
			    		cenGIm.setVisible(false);
			    	}
			    	catch(NullPointerException asd){

			    	}
			    	if(viStack == 1) StackIm.setVisible(true);
			    }
			});
			stackB.setVisible(true);
			mid3b.add(stackB);

			JButton cenB = new JButton("C");		
			cenB.setFont(new Font("Arial", 7, H/30));
			cenB.setBounds(W/28 + W/18, H - 6*H/20 - 70, W/19, H/20);
			cenB.setBackground(Color.WHITE);
			cenB.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e){
			    	viStack = 0;
			    	viLum = 0;
			    	if(viCen == 0 && viLum == 0 && viStack == 0){
			    		System.out.print("apago imagenes");
			    		for(int i=0; i<Images.length; i++) Images[i].setVisible(false);	
			    	}
			    	else if(viCen == 1 && viLum == 0 && viStack == 0){
			    		Images[ImVi].setVisible(true);	
			    	}

					if(viCen == 0) viCen = 1;
			    	else viCen = 0;
				    StackIm.setVisible(false);
			    	
			    	try{
				    	cenGIm.setVisible(false);
				    	lumGIm.setVisible(false);
				    	if(viCen == 1) cenGIm.setVisible(true);	
				    	System.out.println("cenG i lumG ja estaven creats");
			    	}
			    	catch(NullPointerException asd){
				    	System.out.println("creo cenG i lumG");
				    	try{
							
							File cenGFile = new File(PATH + "\\src\\pic\\Centers.png");
							Image _cenGImage = ImageIO.read(cenGFile);			
							_cenGImage = _cenGImage.getScaledInstance(W/3, W/3, Image.SCALE_SMOOTH);
							ImageIcon __cenGImage = new ImageIcon(_cenGImage);
							cenGIm = new JLabel(__cenGImage);
							cenGIm.setBounds((W/2 - W/3)/2+10, (W/2 - W/3)/5, W/3, W/3);
							cenGIm.setVisible(true);
							mid2b.add(cenGIm);

							File lumGFile = new File(PATH + "\\src\\pic\\Lumin.png");
							
							//System.out.println(StackFile);
							
							Image _lumGImage = ImageIO.read(lumGFile);			
							_lumGImage = _lumGImage.getScaledInstance(W/3, W/3, Image.SCALE_SMOOTH);
							ImageIcon __lumGImage = new ImageIcon(_lumGImage);
							lumGIm = new JLabel(__lumGImage);
							lumGIm.setBounds((W/2 - W/3)/2+10, (W/2 - W/3)/5, W/3, W/3);
							lumGIm.setVisible(false);
							mid2b.add(lumGIm);

						}
						catch(IOException asdf){
							System.out.println("cenGRAPH IMAGE ERROR");			
						}
			    	}

			    	mid2a.setVisible(false);
			    	mid2c.setVisible(false);
			    	mid2b.setVisible(false);
			    	mid2b.setVisible(true);
			    }
			});
			cenB.setVisible(true);
			mid3b.add(cenB);
		
			JButton lumB = new JButton("L");		
			lumB.setFont(new Font("Arial", 7, H/30));
			lumB.setBounds(W/28 + 2*W/18, H - 6*H/20 - 70, W/18, H/20);
			lumB.setBackground(Color.WHITE);
			lumB.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e){
			    	viStack = 0;
			    	viCen = 0;
			    	if(viCen == 0 && viLum == 0 && viStack == 0){
			    		System.out.print("apago imagenes");
			    		for(int i=0; i<Images.length; i++) Images[i].setVisible(false);	
			    	}
			    	else if(viCen == 0 && viLum == 1 && viStack == 0){
			    		Images[ImVi].setVisible(true);	
			    	}

			    	if(viLum == 0) viLum = 1;
			    	else viLum = 0;
			    	
			    	StackIm.setVisible(false);
			    	try{
				    	cenGIm.setVisible(false);
				    	lumGIm.setVisible(false);
				    	if(viLum == 1) lumGIm.setVisible(true);
			    	}
			    	catch(NullPointerException asd){
				    	try{
							File cenGFile = new File(PATH + "\\src\\pic\\Centers.png");
							
							Image _cenGImage = ImageIO.read(cenGFile);			
							_cenGImage = _cenGImage.getScaledInstance(W/3, W/3, Image.SCALE_SMOOTH);
							ImageIcon __cenGImage = new ImageIcon(_cenGImage);
							cenGIm = new JLabel(__cenGImage);
							cenGIm.setBounds((W/2 - W/3)/2+10, (W/2 - W/3)/5, W/3, W/3);
							cenGIm.setVisible(false);
							mid2b.add(cenGIm);


							File lumGFile = new File(PATH + "\\src\\pic\\Lumin.png");
							
							//System.out.println(StackFile);
							
							Image _lumGImage = ImageIO.read(lumGFile);			
							_lumGImage = _lumGImage.getScaledInstance(W/3, W/3, Image.SCALE_SMOOTH);
							ImageIcon __lumGImage = new ImageIcon(_lumGImage);
							lumGIm = new JLabel(__lumGImage);
							lumGIm.setBounds((W/2 - W/3)/2+10, (W/2 - W/3)/5, W/3, W/3);
							lumGIm.setVisible(true);
							mid2b.add(lumGIm);


						}
						catch(IOException asdf){
							System.out.println("lumGRAPH IMAGE ERROR");			
						}
			    	}

			    	mid2a.setVisible(false);
			    	mid2c.setVisible(false);
			    	mid2b.setVisible(false);
			    	mid2b.setVisible(true);
			    		
			    }
			});
			lumB.setVisible(true);
			mid3b.add(lumB);
		}
		catch(NullPointerException e){
			System.out.println("BUTTONS ERROR");
		}

		try{
			for(int i=0; i<nObj; i++){
				String s;
				String es = new String("<br>   Ecc. "+ eccen[i] +"</pre></html>");
				if(decl[i] == 0 && vel[i] == 0) s = new String("<html><pre>" + (i+1) + ": Static" + es);
				else s = new String("<html><pre>" + (i+1) + ": Decl. " + decl[i] + " angles<br>" + 
					"   Speed " + vel[i] + " pxl/sec" + es);
								
				output1[i].setText(s);
			}	
		}	
		catch(NullPointerException e){
			output1 = new JLabel[nObj]; 
			for(int i=0; i<nObj; i++){
				String s;
				String es = new String("<br>   Ecc. "+ eccen[i] +"</pre></html>");
				if(decl[i] == 0 && vel[i] == 0) s = new String("<html><pre>" + (i+1) + ": Static" + es);
				else s = new String("<html><pre>" + (i+1) + ": Decl. " + decl[i] + " angles<br>" + 
					"   Speed " + vel[i] + " pxl/sec" + es);
								
				output1[i] = new JLabel(s);
				output1[i].setBounds(30,H/11 + i*110, W/4, 100);
				output1[i].setFont(new Font("Arial", Font.BOLD, 25));
				output1[i].setVisible(true);
				mid3b.add(output1[i]);	
			}
		}
		JButton Return = new JButton("RETURN");		
		Return.setFont(new Font("Arial", 7, H/30));
		Return.setBounds(W/28, H - 6*H/20, W/6, H/20);
		Return.setBackground(Color.WHITE);
		Return.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e){
		    	StackIm.setVisible(false);
		    	cenGIm.setVisible(false);
		    	lumGIm.setVisible(false);
		    	for(int i=0; i<5; i++) Images[i].setVisible(false);
		    	
		    	for(int i=0; i<5; i++) Images[i] = null;
		    	//StackIm = null;
		    	cenGIm = null;
		    	lumGIm = null;
		    	
		    	viLum = 0;
		    	viStack = 0;
		    	viCen = 0;


		    	mid2b.setVisible(false);
		    	mid2a.setVisible(true);		
		    	mid3b.setVisible(false);
		    	mid3a.setVisible(true);
		    	
		    }
		});
		Return.setVisible(true);
		mid3b.add(Return);

/*
		mid2a.setVisible(false);
		mid2b.setVisible(false);
		mid2c.setVisible(false);
		mid3b.setVisible(false);
		mid2b.setVisible(true);
		mid3b.setVisible(true);
*/
	}

	public class actionScanner implements ActionListener{   
		public void actionPerformed(ActionEvent e){	
	    	try{
	    		StackIm.setVisible(false);
		    	cenGIm.setVisible(false);
		    	lumGIm.setVisible(false);
		    	for(int i=0; i<5; i++) Images[i].setVisible(false);
			}
			catch(NullPointerException asd){

			}
	    	for(int i=0; i<5; i++) Images[i] = null;
	    	StackIm = null;
	    	cenGIm = null;
	    	lumGIm = null;
	    	
	    	viLum = 0;
	    	viStack = 0;
	    	viCen = 0;


	    	mid2b.setVisible(false);
	    	mid2a.setVisible(true);		
	    	mid3b.setVisible(false);
	    	mid3a.setVisible(true);


	    	formatIt = new String("Error");
	    	
	    	for(int i=0; i<5; i++) ImNames[i] = picIn[i].getText();

	    	int x = 0;
	    	for(int i=ImNames[0].length()-1; i>=0; i--){
	    		if(ImNames[0].charAt(i) == '.'){
	    			x = ImNames[0].length() - 1 - i;
	    			break;
	    		}
	    	}

	    	if(x == 3){ 
	    		int b = 0;
	    		for(int i=0; i<5; i++){
	    			if(ImNames[i].length() < 4) b = 1;
	    			else if(ImNames[i].charAt(ImNames[i].length() - 1) == 'p' &&
			    		ImNames[i].charAt(ImNames[i].length() - 2) == 'm' &&
			    		ImNames[i].charAt(ImNames[i].length() - 3) == 'b' &&
			    		ImNames[i].charAt(ImNames[i].length() - 4) == '.'
			    		) continue;
			    	else b = 1;
				}
	    		if(b == 0) formatIt = new String("bmp");
	    	}
	    	else if(x == 4){
	    		int b = 0;
	    		for(int i=0; i<5; i++){
	    			if(ImNames[i].length() < 5) b = 1;
	    			else if(ImNames[i].charAt(ImNames[i].length() - 1) == 's' &&
			    		ImNames[i].charAt(ImNames[i].length() - 2) == 't' &&
			    		ImNames[i].charAt(ImNames[i].length() - 3) == 'i' &&
			    		ImNames[i].charAt(ImNames[i].length() - 4) == 'f' &&
			    		ImNames[i].charAt(ImNames[i].length() - 4) == '.'
			    		) continue;
			    	else b = 1;
				}
	    		if(b == 0) formatIt = new String("fits");
	    		mid1.setVisible(false);
	    	}
	    	
			if(formatIt.equals("Error")){
		    	errorIn.setVisible(true);
		    }
			else{
		    	errorIn.setVisible(false);
				process();	
	    	}
	    }
	}

	public void SpinImages(){
		for(int i=0; i<5; i++){
			Images[i].setVisible(false);
		}
		mid2b.setVisible(false);
		Images[ImVi].setVisible(true);
		mid2b.setVisible(true);
	}

	public ASUserInterface(){
		pack();
		try{
			PATH = new Scanner(new File("PATH.txt")).useDelimiter("\\Z").next();
			System.out.println(PATH);
		}
		catch(IOException e){

		};
		
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		getContentPane().setBackground(Color.green);
		setTitle("AstroSpotter");
		//setResizable(false);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setBounds(0, screenSize.height/10, screenSize.width, 4*screenSize.height/5);
		setBounds(0, 0, W, H);
		
		System.out.println(H + " " + W);

		top = new JPanel();
		top.setLayout(null);
		top.setBackground(Color.black);
		top.setBounds(0, 0, W, H/8); 
		add(top);

		JLabel title = new JLabel("AstroSpotter", JLabel.CENTER);
		title.setBounds(0, 5, W, H/8);
		title.setFont(new Font("Arial", 1, H/11 - 10 - (1000 - W)/50));	
		//title.setForeground(Color.white);
		title.setForeground(Color.white);
		title.setVisible(true);
		top.add(title);

		bot = new JPanel();
		bot.setLayout(null);
		bot.setBackground(Color.black);
		bot.setBounds(0, H - 3*H/16/2, W, H/16); 
		add(bot);

		JLabel copyright = new JLabel(((char) 169) + " Javier L" + '\u00f3' + "pez-Contreras & Ignasi Segura", JLabel.CENTER);
		copyright.setBounds(0, -4, W, H/16);
		copyright.setFont(new Font("Arial", 1, H/40));	
		copyright.setForeground(Color.white);
		copyright.setVisible(true);
		bot.add(copyright);

		mid1 = new JPanel();
		mid1.setBackground(new Color(183, 183, 183));
		mid1.setLayout(null);
		//_mid1 = new JScrollPane(mid1);
		//_mid1.preferredSize(W/4, H);
		//_mid1.setViewportView(mid1);
		mid1.setBounds(0, H/8, W/4, H - H/16 - H/8); 
		mid1.setVisible(true);
		
		add(mid1);
		

		JLabel Input = new JLabel("INPUT", JLabel.CENTER);
		Input.setFont(new Font("Arial", 7, 80));
		Input.setBounds(10, 10, W/4 - 10, 2*H/20);
		mid1.add(Input);

		JLabel Exp = new JLabel("<html>Insert the file names ended by their file extension</html>", JLabel.CENTER);
		Exp.setFont(new Font("Arial", 7, H/45));
		Exp.setBounds(30, 2*H/20 - 15, W/4 - 30, 2*H/20);
		mid1.add(Exp);

		for(int it=0; it<5; it++){
			//String S = new String(" Input the " + (it+1) + " image name.");
			String S = (it+1) + "prova.bmp";
			picIn[it] = new JTextField(S); 
			picIn[it].setVisible(true);
			picIn[it].setFont(new Font("Arial", 2, H/35));
			picIn[it].setBounds(20, 4*H/20 + W/100 + 20*H/215*it, W/4-45, H/20);
			mid1.add(picIn[it]);
		}	
		JLabel[] picTi = new JLabel[5];
		for(int it=0; it<5; it++){
			//String S = new String(" Input the " + (it+1) + " image name.");
			String S = new String(" Input the " + (it+1) + " image");
			picTi[it] = new JLabel(S); 
			picTi[it].setVisible(true);
			picTi[it].setForeground(Color.BLACK);
			picTi[it].setFont(new Font("Arial", 2, H/40));
			picTi[it].setBounds(20, 4*H/20 - 20 - W/100 + 20*H/215*it+10, W/4-35, H/20);
			mid1.add(picTi[it]);
		}

   		errorIn = new JLabel("INCORRECT FORMAT", JLabel.CENTER);
   		errorIn.setForeground(Color.RED);
    	errorIn.setFont(new Font("Arial", 1, H/35));
    	errorIn.setBounds(0, 300*H/480, W/4, 100);
	    errorIn.setVisible(false);
	    mid1.add(errorIn);

		JButton Send = new JButton("SEND");
		Send.setFont(new Font("Arial", 7, H/30));
		Send.setBounds(W/28, H - 6*H/20, W/6, H/20);
		Send.setBackground(Color.WHITE);
		actionScanner aL = new actionScanner();
		Send.addActionListener(aL);
		Send.setVisible(true);
		mid1.add(Send);


		mid2b = new JPanel();
		mid2b.setBackground(Color.gray);
		mid2b.setBounds(W/4, H/8, W/2, H-3*H/16);
		mid2b.setLayout(null);
		add(mid2b);

		Right = new JButton("NEXT");
		Right.setBackground(Color.WHITE);
		Right.setBounds(W/2 - W/6 - 30, H - 6*H/20, W/6, H/20);
		Right.setFont(new Font("Arial", 7, H/30));
		Right.setVisible(true);
		Right.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	                if(viStack == 0 && viCen == 0 && viLum == 0){
		                ImVi += 1;
				    	ImVi %= 5;

				    	SpinImages();
                	}
            	}
            }
        });
		Right.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e){
		    	if(viStack == 0 && viCen == 0 && viLum == 0){
		            ImVi++;
		    		ImVi %= 5;

		    		SpinImages();
		    	}
		    }
		});
		mid2b.add(Right);

		Back = new JButton("BACK");
		Back.setBackground(Color.WHITE);
		Back.setBounds(30, H - 6*H/20, W/6, H/20);
		Back.setFont(new Font("Arial", 7, H/30));
		Back.setVisible(true);
		Back.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e){
		    	ImVi += 4;
		    	ImVi %= 5;

		    	SpinImages();
		    }
		});
		mid2b.add(Back);
		
		mid2a = new JPanel();
		mid2a.setBackground(Color.gray);
		mid2a.setBounds(W/4, H/8, W/2, H-3*H/16);
		mid2a.setLayout(null);
		add(mid2a);	

		mid2c = new JPanel();
		mid2c.setBackground(Color.gray);
		mid2c.setBounds(W/4, H/8, W/2, H-7*H/32);
		mid2c.setLayout(null);
		add(mid2c);

		charg = new JLabel[12];
	
		chargb = new JLabel("");
		chargb.setBackground(Color.black);
		chargb.setOpaque(true);
		chargb.setBounds(35-10, 50 + (H-7*H/32)/2 - 38, 12*W/26 + 20, 90);
		chargb.setVisible(true);

		chargt = new JLabel("CHARGING...", JLabel.CENTER);
		chargt.setForeground(Color.white);
		chargt.setFont(new Font("Arial", Font.ITALIC, 50));
		//chargt.setOpaque(true);
		chargt.setBounds(35-10, -50 + (H-7*H/32)/2 - 38, 12*W/26 + 20, 90);
		chargt.setVisible(true);
		mid2c.add(chargt);
		
		for(int i=0; i<12; i++){
			charg[i] = new JLabel("");
			charg[i].setBackground(Color.green);
			charg[i].setOpaque(true);
			charg[i].setBounds(i*W/26+40, 50 + (H-7*H/32)/2 - 25, W/30, W/30);
			charg[i].setVisible(false);
			mid2c.add(charg[i]);
		}
		mid2c.add(chargb);

		try{
			System.out.println(PATH + "\\src\\us\\logo.png");
			File LogoFile = new File(PATH + "\\src\\us\\logo.png");
			Image _Logo = ImageIO.read(LogoFile);
			_Logo = _Logo.getScaledInstance(W/3, W/3, Image.SCALE_SMOOTH);
			ImageIcon __Logo = new ImageIcon(_Logo);
			JLabel Logo = new JLabel(__Logo);
			Logo.setBounds((W/2 - W/3)/2+10, (W/2 - W/3)/5, W/3, W/3);
			Logo.setBackground(Color.WHITE);
			Logo.setVisible(true);
			mid2a.add(Logo);
			setIconImage(_Logo);
		}
		catch(IOException e){
			System.out.println("IMAGE LOGO ERROR");
		}

		JLabel Pres = new JLabel("<html>Welcome to the AstroSpotter!</html>", JLabel.CENTER);
		Pres.setBounds((W/2 - W/3)/5, W/3 + W/25-15, 9*W/20, W/25);
		Pres.setVisible(true);
		Pres.setForeground(Color.WHITE);
		Pres.setFont(new Font("Arial", 7, H/30));
		mid2a.add(Pres);
		JLabel Pres2 = new JLabel("<html>Input the images to start.</html>", JLabel.CENTER);
		Pres2.setBounds((W/2 - W/3)/5, W/3 + W/25+15, 9*W/20, W/25);
		Pres2.setVisible(true);
		Pres2.setForeground(Color.WHITE);
		Pres2.setFont(new Font("Arial", 7, H/30));
		mid2a.add(Pres2);

		mid3a = new JPanel();
		mid3a.setBackground(new Color(183, 183, 183));
		mid3a.setBounds(3*W/4, H/8, W/4, H - H/16 - H/8); 
		mid3a.setLayout(null);
		add(mid3a);
		
		JLabel AboutUs = new JLabel("ABOUT US", JLabel.CENTER);
		AboutUs.setFont(new Font("Arial", 7, 80));
		AboutUs.setBounds(5, 10, W/4 - 10, 2*H/20);
		mid3a.add(AboutUs);

		JLabel li = new JLabel("<html>__________________________________________________________________________</html>");
		li.setBounds(0,H/2 - H/14,W/4,15);
		li.setBackground(Color.BLACK);
		li.setVisible(true);
		mid3a.add(li);

		try{
			File ___Ig = new File(PATH + "\\src\\us\\Ignasi.jpg");
			Image __Ig = ImageIO.read(___Ig);
			__Ig = __Ig.getScaledInstance(W/5, H/5, Image.SCALE_SMOOTH);
			ImageIcon _Ig = new ImageIcon(__Ig);
			JLabel Ig = new JLabel(_Ig);
			Ig.setVisible(true);
			Ig.setBounds(W/44, 19*H/40, W/5, H/5);
			mid3a.add(Ig);

			JLabel Igm = new JLabel("ignasiseguravicente@gmail.com", JLabel.CENTER);
			Igm.setVisible(true);
			Igm.setFont(new Font("Arial", Font.ITALIC, 25));
			Igm.setBounds(W/44, 19*H/40 + H/5 + 20, W/5, 30);
			mid3a.add(Igm);

			File ___Ja = new File(PATH + "\\src\\us\\Javier.JPG");
			Image __Ja = ImageIO.read(___Ja);
			__Ja = __Ja.getScaledInstance(W/5, H/5, Image.SCALE_SMOOTH);
			ImageIcon _Ja = new ImageIcon(__Ja);
			JLabel Ja = new JLabel(_Ja);
			Ja.setVisible(true);
			Ja.setBounds(W/44, 6*H/40, W/5, H/5);
			mid3a.add(Ja);

			JLabel Jam = new JLabel("javierlc2000@gmail.com", JLabel.CENTER);
			Jam.setVisible(true);
			Jam.setFont(new Font("Arial", Font.ITALIC, 25));
			Jam.setBounds(W/44, 6*H/40 + H/5 + 20, W/5, 30);
			mid3a.add(Jam);
		}
		catch(IOException e){
			System.out.println("ABOUT US IMAGE ERROR");
		}

		mid3b = new JPanel();
		mid3b.setBackground(new Color(183, 183, 183));
		mid3b.setBounds(3*W/4, H/8, W/4, H - H/16 - H/8); 
		mid3b.setLayout(null);
		add(mid3b);

		mid1.setVisible(false);
		mid2a.setVisible(false);
		mid2b.setVisible(false);
		mid2c.setVisible(false);
		mid3a.setVisible(false);
		mid3b.setVisible(false);
		bot.setVisible(false);
		top.setVisible(false);
		
		mid1.setVisible(true);
		mid2a.setVisible(true);
		//mid2b.setVisible(true);
		//mid2c.setVisible(true);
		mid3a.setVisible(true);
		//mid3b.setVisible(true);
		bot.setVisible(true);
		top.setVisible(true);
		setVisible(true);
	}

	public static void main(String arg[]) throws IOException {
		ASUserInterface S = new ASUserInterface(); 
	}
}