import java.util.ArrayList;
import java.util.Random;

public class TrialDeck {
	private ArrayList<Card> source;
	private ArrayList<Card> deck;
	private Random gen;
	
	/**
	 * Creates a new trial deck.
	 */
	public TrialDeck()
	{
		source = new ArrayList<>();
		gen = new Random();
	}
	
	/**
	 * Adds the given card to the deck.
	 */
	public void add(Card c)
	{
		source.add(c);
	}
	
	/**
	 * Sets up the trial deck for a new trial.
	 */
	public void init()
	{
		deck = new ArrayList<>(source);
	}
	
	/**
	 * Removes and returns a random card from the deck.
	 */
	public Card drawCard()
	{
		return deck.remove(gen.nextInt(deck.size()));
	}
}
