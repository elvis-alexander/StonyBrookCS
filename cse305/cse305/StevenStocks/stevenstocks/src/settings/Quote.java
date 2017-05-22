package settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Quote {
	private Random rand;
	private String[] stringArray;
	
	public Quote() {
		rand = new Random();
		this.stringArray = new String[9];
		hardCodeThoseQuotes();
	}

	
	private void hardCodeThoseQuotes(){
		stringArray[0] = "\"Today the financial market is no good, but the money is there.\"";
		stringArray[1] = "\"The lesson of history is that you do not get a sustained economic recovery as long as the financial system is in crisis.\"";
		stringArray[2] = "\"Rule No. 1 : Never lose money. Rule No. 2 : Never forget Rule No. 1.\"";
		stringArray[3] = "\"Wealth consists not in having great possessions, but in having few wants.\"";
		stringArray[4] = "\"The stock market is filled with individuals who know the price of everything, but the value of nothing.\"";
		stringArray[5] = "\"In investing, what is comfortable is rarely profitable.\"";
		stringArray[6] = "\"Invest in yourself. Your career is the engine of your wealth.\"";
		stringArray[7] = "\"The strong controls the weak, but the OP controls everything.\"";
		stringArray[8] = "\"If you don't like that, you don't like NBA basketball.\"";
	}
	
	
	
	
	public String getQuote() {
		int randomNum = rand.nextInt((8) + 1);
		return this.stringArray[randomNum];
	}

}



