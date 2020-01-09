//Jordan Wang
//Category
//Spec: Creates an array of questions and answers based off a given text file

import java.io.*;
public class Category 
{
	//Declaring variables
	private String name;
	private String[] questions;
	private String[] answers;
	public Category()
	{
		questions = new String[5];
		answers = new String[5];
	}
	public Category(InputStreamReader in, String name) throws Exception
	{
		String line = "";
		questions = new String[5];
		answers = new String[5];
		this.name = name;
		int location = 0;
		BufferedReader bf = new BufferedReader(in);
		/**Each text file has exactly ten lines of text
		 * The odd line numbers are the questions
		 * The even line numbers are the answers
		 * Assigns each of these to a spot in their respective arrays
		 */
		for(int i = 0; i < 10; i++)
		{
			line = bf.readLine();
			if(i % 2 == 0)
			{
				questions[location] = line;
				//System.out.println("q: " + location);
			}
			else
			{
				answers[location] = line;
				//System.out.println("a: " + location);
				location++;
			}
		}
	}
	
	//Returns the name of the category
	public String getName()
	{
		return name;
	}
	
	//Returns the questions array
	public String[] getQuestions()
	{
		return questions;
	}
	
	//Returns the answers array
	public String[] getAnswers()
	{
		return answers;
	}
}
