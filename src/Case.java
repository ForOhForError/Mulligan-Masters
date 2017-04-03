
public class Case{
	private String[] attributes;
	private int[] expected;
	private int[] counts;
	private char[] modes;
	
	/**
	 * Creates a new case from the string formated in the following way:
	 * <Arrr1>:<Count1>[+/-/=];<Arrr2>:<Count2>[+/-/=];...;<ArrrN>:<CountN>[+/-/=]
	 */
	public Case(String line){
		line=line.trim();
		String[] toks = line.split(";");
		
		int l = toks.length;
		
		attributes = new String[l];
		expected = new int[l];
		counts = new int[l];
		modes = new char[l];
		
		for(int i=0;i<l;i++)
		{
			String[] part = toks[i].split(":");
			attributes[i] = part[0].trim();
			String t = part[1].trim();
			if(t.endsWith("-")||t.endsWith("=")||t.endsWith("+"))
			{
				expected[i] = Integer.parseInt(t.substring(0, t.length()-1));
				modes[i] = t.charAt(t.length()-1);
			}
			else
			{
				expected[i] = Integer.parseInt(t);
				modes[i] = '+';
			}
		}
	}
	
	/**
	 * Resets the case between trials.
	 */
	public void reset()
	{
		for(int i=0;i<counts.length;i++)
		{
			counts[i]=0;
		}
	}
	
	/**
	 * Updates the case based on the given card.
	 */
	public void run(Card card)
	{
		for(int i=0;i<attributes.length;i++)
		{
			if(card.hasAttribute(attributes[i]))
			{
				counts[i]++;
			}
		}
	}
	
	/**
	 * Returns true if the case is satisfied, and false otherwise.
	 */
	public boolean isSatisfied()
	{
		for(int i=0;i<attributes.length;i++)
		{
			if(modes[i]=='=')
			{
				if(counts[i]!=expected[i])
				{
					return false;
				}
			}
			else if(modes[i]=='-')
			{
				if(counts[i]>expected[i])
				{
					return false;
				}
			}
			else if(modes[i]=='+')
			{
				if(counts[i]<expected[i])
				{
					return false;
				}
			}
		}
		return true;
	}
}
