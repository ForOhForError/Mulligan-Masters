import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MulliganMasters extends JFrame{
	private static final long serialVersionUID = 1L;

	private JTextArea cases;
	private JTextArea deck;
	private JTextArea results;
	private JButton run;

	public MulliganMasters()
	{
		super("Mulligan Masters");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(new GridLayout(4,1));

		cases = new JTextArea(10,60);
		deck = new JTextArea(10,60);
		results = new JTextArea(10,60);

		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1,3));

		JButton save = new JButton("Save");
		JButton load = new JButton("Load");
		run = new JButton("Run");

		buttons.add(save);
		buttons.add(load);
		buttons.add(run);

		save.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				save();
			}
		});

		load.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				load();
			}
		});

		MulliganMasters mm = this;
		
		run.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				new Thread()
				{
					public void run()
					{
						mm.run();
					}
				}.start();
			}
		});

		JPanel deckbox = new JPanel();
		JPanel casebox = new JPanel();
		JPanel resbox = new JPanel();
		deckbox.setLayout(new BorderLayout());
		casebox.setLayout(new BorderLayout());
		resbox.setLayout(new BorderLayout());
		deckbox.add(new JLabel("Deck:"),BorderLayout.NORTH);
		casebox.add(new JLabel("Tests:"),BorderLayout.NORTH);
		resbox.add(new JLabel("Results:"),BorderLayout.NORTH);
		
		JScrollPane scrolldeck = new JScrollPane();
		scrolldeck.setViewportView(deck);

		JScrollPane scrollcase = new JScrollPane();
		scrollcase.setViewportView(cases);

		JScrollPane scrollres = new JScrollPane();
		scrollres.setViewportView(results);
		
		deckbox.add(scrolldeck,BorderLayout.CENTER);
		casebox.add(scrollcase,BorderLayout.CENTER);
		resbox.add(scrollres,BorderLayout.CENTER);

		results.setEditable(false);

		add(casebox);
		add(deckbox);
		add(resbox);
		add(buttons);

		pack();

		setVisible(true);
	}

	public void save()
	{
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("."));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Deck Files", "mmdek");
		fc.setFileFilter(filter);
		int returnVal = fc.showSaveDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			if(!file.getAbsolutePath().endsWith(".mmdek"))
			{
				file = new File(file.getAbsolutePath()+".mmdek");
			}
			try {
				FileOutputStream out = new FileOutputStream(file);
				String save = "v1\n"+cases.getText()+"\n~DECK~\n"+deck.getText();
				try {
					out.write(save.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public void load()
	{
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("."));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Deck Files", "mmdek");
		fc.setFileFilter(filter);
		int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
				Scanner scan = new Scanner(file);
				int version = Integer.parseInt(scan.nextLine().substring(1).trim());
				System.out.println("Loaded a file of version "+version);
				boolean readDeck = false;
				String de = "";
				String cs = "";
				while(scan.hasNextLine())
				{
					String line = scan.nextLine();
					if(line.equals("~DECK~"))
					{
						readDeck = true;
					}
					else
					{
						if(readDeck)
						{
							de += line+"\n";
						}
						else
						{
							cs += line+"\n";
						}
					}
				}
				scan.close();
				deck.setText(de);
				cases.setText(cs);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public void run()
	{
		try{
		run.setEnabled(false);
		LinkedList<MultiCase> tests = new LinkedList<>();
		Scanner scan = new Scanner(cases.getText()+"\n#");
		int casenum = 0;

		String caseline = "";
		String name = "";
		int runs = 1000;
		int hand = 7;
		int mullTo = 7;
		boolean set = false;
		while(scan.hasNextLine())
		{
			String line = scan.nextLine();
			if(line.startsWith("#"))
			{
				if(caseline.trim().length()>0)
				{
					tests.add(new MultiCase(caseline.trim(),name,runs,hand,mullTo));
				}
				casenum++;
				name = "Case "+casenum;
				runs = 1000;
				hand = 7;
				mullTo = 7;
				set = false;
				String[] args = line.replace("#", "").trim().split(",");
				for(int i=0;i<args.length;i++)
				{
					String arg = args[i].trim();
					String low = arg.toLowerCase();
					if(low.startsWith("name:"))
					{
						name = arg.substring("name:".length()).trim();
					}
					else if(low.startsWith("runs:"))
					{
						runs = Integer.parseInt(arg.substring("runs:".length()).trim());
					}
					else if(low.startsWith("hand:"))
					{
						hand = Integer.parseInt(arg.substring("hand:".length()).trim());
						if(!set)
						{
							mullTo = hand;
						}
					}
					else if(low.startsWith("mullto:"))
					{
						mullTo = Integer.parseInt(arg.substring("mullto:".length()).trim());
						set = true;
					}
				}
			}
			else
			{
				caseline+=line.trim()+" ";
			}
		}
		scan.close();

		scan = new Scanner(deck.getText());
		TrialDeck deck = new TrialDeck();
		while(scan.hasNextLine())
		{
			String line = scan.nextLine();
			int copies = Integer.parseInt(line.substring(0, line.indexOf('{')));
			for(int i=0;i<copies;i++)
			{
				deck.add(new Card(line));
			}
		}
		scan.close();
		results.setText("");
		for(MultiCase test:tests){
			int success = 0;
			for(int i=0;i<test.getRuns();i++)
			{
				int mhand = test.getHand();
				int mull = test.getMullTo();
				while(mhand>=mull)
				{
					test.reset();
					deck.init();
					for(int j=0;j<mhand;j++)
					{
						test.run(deck.drawCard());
					}
					if(test.isSatisfied())
					{
						success++;
						break;
					}
					mhand--;
				}
			}
			results.setText(results.getText()+String.format("%s: %.2f%%\n", test.getName(),100.0*success/test.getRuns()));
		}
		}
		catch(Exception e)
		{
			results.setText(results.getText()+"Error; Exection stopped. Check your syntax.\n");
		}
		run.setEnabled(true);
	}

	public static void main(String[] args)
	{
		new MulliganMasters();
	}
}
