
public class Card {
	private String[] attributes;
	private String name;
	
	/**
	 * Creates a new card object from a line of text formatted in the following way:
	 * <Number (ignored)>{<Attr1>;<Attr2>;...;<AttrN>)<Name>
	 */
	public Card(String line)
	{
		line = line.trim();
		name = line.substring(line.indexOf('}')+1).trim();
		attributes = line.substring(line.indexOf('{')+1,line.indexOf('}')).split(";");
		for(int i=0;i<attributes.length;i++)
		{
			attributes[i] = attributes[i].trim();
		}
	}
	
	/**
	 * Returns true if this card has the given attribute, and false otherwise. Case insensitive.
	 */
	public boolean hasAttribute(String attr)
	{
		for(int i=0;i<attributes.length;i++)
		{
			if(attributes[i].equalsIgnoreCase(attr))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @return This card's attributes
	 */
	public String[] getAttributes() {
		return attributes;
	}

	/**
	 * @return This card's name
	 */
	public String getName() {
		return name;
	}
}
