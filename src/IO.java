import java.util.*;
import java.io.*;
import java.nio.file.*;

public class IO extends PackageCommand
{	
	protected static String[] modes = { "read", "write", "create" };
	
	public IO(List<String> args, String mode)
	{
		super(args, mode);
		if(!Arrays.asList(modes).contains(mode)) {
			throw new JayInterpreterException("Syntax error: unknown IO mode.");
		}
		if(!Sys.isImported("io")) { throw Sys.getNotImported(); }
	}
	
	public void execute(State program)
	{
		if(mode.equals("read"))
		{
			if(args.size() == 2)
			{
				String pth = args.get(0);
				if(pth.startsWith("~"))
				{
					pth = pth.replace("~", System.getProperty("user.home"));
				}
				File in = new File(pth);
				if(in.exists() && !in.isDirectory())
				{
					try
					{
						program.setVar(args.get(1), String.join("\n", Files.readAllLines(in.toPath())));
					}
					catch(IOException ex)
					{
						throw new JayInterpreterException("IO error: Failed to read from file.");
					}
				}
				else
				{
					throw new JayInterpreterException("IO error: File doesn't exist or is a directory.");
				}
			}
			else
			{
				throw new JayInterpreterException("Syntax error: `io_read` requires exactly 2 arguments, " + args.size() + " given.");
			}
		}
		else if(mode.equals("create"))
		{
			if(args.size() == 1)
			{
				String pth = args.get(0);
				if(pth.startsWith("~"))
				{
					pth = pth.replace("~", System.getProperty("user.home"));
				}
				Path p = Path.of(pth);
				File in = p.toFile();
				if(in.exists())
				{
					throw new JayInterpreterException("IO error: File or directory exists.");
				}
				else
				{
					try
					{
						if(in.isDirectory())
						{
							if(!in.mkdir())
							{
								throw new JayInterpreterException("IO error: Failed to create directory.");
							}
						}
						else
						{
							if(!in.createNewFile())
							{
								throw new JayInterpreterException("IO error: Failed to create file.");
							}
						}
					}
					catch(IOException ex)
					{
						throw new JayInterpreterException("IO error: Failed to create file (2).");
					}
				}
			}
			else
			{
				throw new JayInterpreterException("Syntax error: `io_create` requires exactly 1 argument, " + args.size() + " given.");
			}
		}
		else if(mode.equals("write"))
		{
			if(args.size() == 2)
			{
				String pth = args.get(0);
				if(pth.startsWith("~"))
				{
					pth = pth.replace("~", System.getProperty("user.home"));
				}
				File in = new File(pth);
				if(in.exists() && !in.isDirectory())
				{
					try
					{
						List<String> tm = new ArrayList<>();
						tm.add(program.getVar(args.get(1)).toString());
						Files.write(in.toPath(), tm, StandardOpenOption.WRITE);
					}
					catch(IOException ex)
					{
						throw new JayInterpreterException("IO error: Failed to write to file.");
					}
				}
				else
				{
					throw new JayInterpreterException("IO error: File doesn't exist or is a directory.");
				}
			}
			else
			{
				throw new JayInterpreterException("Syntax error: `io_write` requires exactly 2 arguments, " + args.size() + " given.");
			}
		}
		else
		{
			throw new JayInterpreterException("Syntax error: unknown IO mode.");
		}
	}
}
