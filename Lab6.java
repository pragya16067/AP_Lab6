import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;
import java.util.StringTokenizer;


class Coordinate {
	int xc;
	int yc;
	
	public Coordinate(int a, int b) {
		xc=a;
		yc=b;
	}
	
}

class Knight {
	
	String Name;
	int xpos;
	int ypos;
	Stack<Object> MBox;
	
	public Knight(String n, int x, int y, Stack<Object> s) {
		Name=n;
		xpos=x;
		ypos=y;
		MBox=s;
	}
	
	public int takeTurn(ArrayList<Knight> grid, int xq, int yq) throws FileNotFoundException, UnsupportedEncodingException{
		int indexOfremoval=0;
		try
		{
			
			if(this.MBox.isEmpty())
			{
				grid.remove(this);
				indexOfremoval=-1; //current element is removed
				throw new StackEmptyException();
			}
			Object o=this.MBox.pop();
			if(! (o instanceof Coordinate)) {
				throw new NonCoordinateException("NonCoordinateException: Not A Coordinate Exception "+o);
			}
			else
			{
				Coordinate c=(Coordinate) o;
				this.xpos=c.xc;
				this.ypos=c.yc;
				if(this.xpos==xq && this.ypos==yq) //Knight has found the queen
				{
					throw new QueenFoundException();
				}
				int i=0;
				for(Knight k : grid)
				{
					if(k.Name.equals(this.Name))
					{
						continue;
					}
					else
					{
						if(this.xpos==k.xpos && this.ypos==k.ypos)
						{
							grid.remove(k);
							indexOfremoval=i;
							throw new OverlapException("OverlapException: Knights Overlap Exception "+k.Name);
						}
					}
					i++;
				}
				throw new NoException("No Exception "+c.xc+" "+c.yc);
			}
		}
		catch(QueenFoundException e)
		{
			PrintWriter OPwrite = new PrintWriter("./src/Output.txt", "UTF-8");
			OPwrite.append(e.getMessage());
			OPwrite.close();
			System.out.println(e.getMessage());
			return -2; //queen has been found
		}
		catch(StackEmptyException e)
		{
			System.out.println(e.getMessage());
			return indexOfremoval;
		}
		catch(OverlapException e)
		{
			System.out.println(e.getMessage());
			return indexOfremoval;
		}

		catch(Exception e)
		{
			PrintWriter OPwrite = new PrintWriter("./src/Output.txt", "UTF-8");
			OPwrite.append(e.getMessage());
			OPwrite.close();
			System.out.println(e.getMessage());
			return -3; //all normal
		}
		
	}
}

class NoException extends Exception {
	
	public NoException(String msg) {
		super(msg);
	}
	
}

class NonCoordinateException extends Exception {
	
	public NonCoordinateException(String msg) {
		super(msg);
	}
	
}

class StackEmptyException extends Exception {
	
	public StackEmptyException() {
		super("StackEmptyException: Stack Empty Exception");
	}
	
}

class OverlapException extends Exception {
	
	public OverlapException(String msg) {
		super(msg);
	}
	
}

class QueenFoundException extends Exception {
	
	public QueenFoundException() {
		super("QueenFoundException: Queen has been found. Abort!");
	}
	
}

public class Lab6 {
	
	public void StartGame(ArrayList<Knight> grid, int xq, int yq, int iterations) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter OPwrite = new PrintWriter("./src/Output.txt", "UTF-8");
		boolean flag=true;
		
		while(flag){
			for(int i=1; i<=iterations; i++)
			{
				if(grid.size()==0)
				{
					flag=false;
					return;
				}
				else{
					try{
						int len=grid.size();
						for(int k=0; k<len; k++)
						{
							Knight knight=grid.get(k);
							OPwrite.append(i+" "+knight.Name+" "+knight.xpos+" "+knight.ypos);
							System.out.println(i+" "+knight.Name+" "+knight.xpos+" "+knight.ypos);
							int retVal=knight.takeTurn(grid,xq,yq);
							if(retVal==-2) ///Queen has been found
							{
								flag=false;
								return;
							}
							else if(retVal==-1 || (retVal<k && retVal!=-3)) //this index has been removed as its stack became empty or some knight has been removed as it overlapped with this
							{
								k--;
							}
						}
					}
					catch(Exception e)
					{
						continue;
					}
				}
			}
			flag=false;
		}
		
		OPwrite.close();
		return;
	}
	
	public static void main(String[] args) throws IOException {
		Reader.init(System.in);
		System.out.println("The number of knights - ");
		int K=Reader.nextInt();
		System.out.println("Total number of Iterations - ");
		int I=Reader.nextInt();
		System.out.println("Coordinates of the Queen, x and y - ");
		int xQ=Reader.nextInt();
		int yQ=Reader.nextInt();
		ArrayList<Knight> KnightGrid = new ArrayList<Knight> ();
		
		for(int i=1; i<=K; i++)
		{
			ReadFromFile.init("./src/" + i + ".txt");
			String name=ReadFromFile.reader.readLine();
			int x=ReadFromFile.nextInt();
			int y=ReadFromFile.nextInt();
			int m=ReadFromFile.nextInt();
			Stack<Object> st=new Stack<Object> ();
			
			Object o;
			while(m-- > 0)
			{
				String type=ReadFromFile.next();
				if(type.equals("String"))
				{
					o=(String) ReadFromFile.next();
					//System.out.println(o);
				}
				else if(type.equals("Integer"))
				{
					o=(int) ReadFromFile.nextInt();
				}
				else if(type.equals("Float"))
				{
					o=(float) ReadFromFile.nextFloat();
				}
				else
				{
					int xc=(int) ReadFromFile.nextInt();
					int yc=(int) ReadFromFile.nextInt();
					o=new Coordinate(xc,yc);
				}
				st.push(o);
			}
			
			Knight knight= new Knight(name,x,y,st);
			KnightGrid.add(knight);
			
		}
		
		
		Collections.sort(KnightGrid, new Comparator<Knight> (){
			@Override
			public int compare(Knight a, Knight b) {
				return a.Name.compareTo(b.Name);
			}
		});
		
		Lab6 obj=new Lab6();
		obj.StartGame(KnightGrid, xQ, yQ, I);
	}

}

/** Class for buffered reading from Input Stream */
class Reader {
    static BufferedReader reader;
    static StringTokenizer tokenizer;

    /** call this method to initialize reader for InputStream */
    static void init(InputStream input) {
        reader = new BufferedReader(
                     new InputStreamReader(input) );
        tokenizer = new StringTokenizer("");
    }

    /** get next word */
    static String next() throws IOException {
        while ( ! tokenizer.hasMoreTokens() ) {
            //TODO add check for eof if necessary
            tokenizer = new StringTokenizer(
                   reader.readLine() );
        }
        return tokenizer.nextToken();
    }

    static int nextInt() throws IOException {
        return Integer.parseInt( next() );
    }
    
    static float nextFloat() throws IOException {
        return Float.parseFloat( next() );
    }
    
    static long nextLong() throws IOException {
        return Long.parseLong( next() );
    }
	
    static double nextDouble() throws IOException {
        return Double.parseDouble( next() );
    }
}

/** Class for buffered reading from Files */
class ReadFromFile {
    static BufferedReader reader;
    static StringTokenizer tokenizer;

    /** call this method to initialize reader for InputStream 
     * @throws FileNotFoundException */
    static void init(String input) throws FileNotFoundException {
        reader = new BufferedReader(
                     new FileReader(input) );
        tokenizer = new StringTokenizer("");
    }

    /** get next word */
    static String next() throws IOException {
        while ( ! tokenizer.hasMoreTokens() ) {
            //TODO add check for eof if necessary
            tokenizer = new StringTokenizer(
                   reader.readLine() );
        }
        return tokenizer.nextToken();
    }

    static int nextInt() throws IOException {
        return Integer.parseInt( next() );
    }
    
    static float nextFloat() throws IOException {
        return Float.parseFloat( next() );
    }
    
    static long nextLong() throws IOException {
        return Long.parseLong( next() );
    }
	
    static double nextDouble() throws IOException {
        return Double.parseDouble( next() );
    }
}