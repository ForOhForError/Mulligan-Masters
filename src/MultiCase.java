import java.util.ArrayList;
import java.util.Stack;

public class MultiCase {
	public MultiCase(String content, String name, int runs, int hand, int mullTo) {
		super();
		this.name = name;
		this.runs = runs;
		this.hand = hand;
		this.mullTo = mullTo;
		parse(content);
	}

	private ArrayList<String> evalQueue;
	private ArrayList<Case> cases;
	private String name;
	private int runs;
	private int hand;
	private int mullTo;
	
	
	
	public void parse(String line)
	{
		String[] tokens = toTokens(line);
		evalQueue = new ArrayList<>();
		cases = new ArrayList<>();
		
		Stack<String> opstack = new Stack<>();
		
		int ix = 0;
		int point=0;
		while(ix<tokens.length)
		{
			String token = tokens[ix];
			if(token.startsWith("{"))
			{
				evalQueue.add(":"+point);
				cases.add(new Case(token.substring(1, token.length()-1)));
				point++;
			}
			else
			{
				int presc = getPresc(token);
				if(presc != -999)
				{
					int prescTop = 0;
					while(!opstack.isEmpty() && prescTop != -999)
					{
						prescTop = getPresc(opstack.peek());
						if(presc<=prescTop)
						{
							evalQueue.add(opstack.pop());
						}
						else
						{
							break;
						}
					}
					opstack.push(token);
				}
				else
				{
					if(token.equals("("))
					{
						opstack.push(token);
					}
					else
					{
						while(!opstack.isEmpty() && !opstack.peek().equals("("))
						{
							evalQueue.add(opstack.pop());
						}
						if(!opstack.isEmpty())
						{
							opstack.pop();
						}
						else
						{
							System.err.println("Mismatched parens");
						}
					}
				}
			}
			ix++;
		}
		while(!opstack.isEmpty())
		{
			String token = opstack.pop();
			if(getPresc(token)!=-999)
			{
				evalQueue.add(token);
			}
			else
			{
				System.err.println("Mismatched parens");
			}
		}
	}
	
	public void run(Card c)
	{
		for(Case ca:cases)
		{
			ca.run(c);
		}
	}
	
	public boolean isSatisfied()
	{
		Stack<Boolean> stack = new Stack<Boolean>();
		for(String s:evalQueue)
		{
			if(s.startsWith(":"))
			{
				int ix = Integer.parseInt(s.substring(1));
				stack.push(cases.get(ix).isSatisfied());
			}
			else
			{
				if(s.equalsIgnoreCase("AND"))
				{
					stack.push(stack.pop().booleanValue()&&stack.pop().booleanValue());
				}
				else if(s.equalsIgnoreCase("OR"))
				{
					stack.push(stack.pop().booleanValue()||stack.pop().booleanValue());
				}
				else if(s.equalsIgnoreCase("NOT"))
				{
					stack.push(!stack.pop().booleanValue());
				}
			}
		}
		return stack.pop().booleanValue();
	}
	
	public void printRPN()
	{
		for(String s:evalQueue)
		{
			System.out.println(s);
		}
	}
	
	public void reset()
	{
		for(Case ca:cases)
		{
			ca.reset();
		}
	}
	
	private static int getPresc(String s)
	{
		int i = 0;
		for(String key:KEYWORDS)
		{
			if(s.equalsIgnoreCase(key))
			{
				return PRESC[i];
			}
			i++;
		}
		return -999;
	}
	
	private static final int[] PRESC = {2,1,3,-999,-999};
	private static final String[] KEYWORDS = {"AND","OR","NOT","(",")"};
	private static String[] toTokens(String line)
	{
		ArrayList<String> tokens = new ArrayList<>();
		
		String up = line.toUpperCase();
		
		int index = 0;
		
		while(index < line.length())
		{
			char c = line.charAt(index);
			
			int kwl = hasKeyword(up,index);
			
			if(c=='{')
			{
				String phrase = line.substring(index,line.indexOf('}', index)+1);
				tokens.add(phrase);
				index += phrase.length();
			}
			else if(kwl>0)
			{
				tokens.add(up.substring(index, index+kwl).toUpperCase());
				index += kwl;
			}
			else
			{
				index++;
			}
		}
		String[] arr = new String[tokens.size()];
		return tokens.toArray(arr);
	}
	
	private static int hasKeyword(String line, int index)
	{
		for(String key:KEYWORDS)
		{
			if(line.indexOf(key, index)==index)
			{
				return key.length();
			}
		}
		return 0;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the runs
	 */
	public int getRuns() {
		return runs;
	}

	/**
	 * @return the hand
	 */
	public int getHand() {
		return hand;
	}

	/**
	 * @return the mullTo
	 */
	public int getMullTo() {
		return mullTo;
	}
}
