package cache;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class cache_sim {

    private int cache_capacity;
    private int cache_blocksize;
    private int cache_associativity;
    final static int CACHE_READ = 0;
    final static int CACHE_WRITE = 1;
    
    public static void main(String[] args) throws IOException {
        MainMemory mainmem = new MainMemory();
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        cache_sim c = new cache_sim();
	if(!c.parseParams(args)) {
	    return;
	}
        System.out.println("cache size is " + c.cache_capacity +
        ", blocksize is " + c.cache_blocksize +
        ", associativity is " + c.cache_associativity + "\n");
        //System.out.println(io.hex_to_int("003f8008"));
        Cache cache = new Cache(c.cache_capacity, c.cache_associativity, c.cache_blocksize, mainmem);

        String parseString;
        String[] strings;
        byte cc;
        int read_write;
        int address;
        int data = 0;
        // Read until a newline

        while ((cc = (byte)br.read()) != -1)
        {
            parseString = "";
            while(cc != '\n')
            {
                if(cc == -1) { break; }
                parseString += (char)cc;
                cc = (byte)br.read();
            }

            strings = parseString.split("\\s");

            if(strings.length < 2)
            {
                break;
            }

             // Read the first character of the line.
             // It determines whether to read or write to the cache.
             read_write = Integer.parseInt(strings[0]);

             // Read the address (as a hex number)
             address = IO.hex_to_int(strings[1]);

             // If it is a cache write the we have to read the data
             if(read_write == CACHE_WRITE)
             {
                 data = IO.hex_to_int(strings[2]);
                 cache.write(address, data);
                 //mainmem.write(address, data);
             }

                            // If it is a cache write the we have to read the data
             if(read_write == CACHE_READ)
             {
                 cache.read(address);
                 //mainmem.read(address);
             }
        }
        cache.print();
        try {
          //io.main(new String[0]);
        } catch (Exception ex) {
            Logger.getLogger(cache_sim.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean parseParams(String[] args)
    {
	//needed for the parsing of command line options
	int c;
	boolean c_flag, b_flag, a_flag;
	boolean errflg = false;

	c_flag = b_flag = a_flag = errflg = false;


	for(int i = 0; i < args.length; i++) {
	    c = args[i].charAt(1);

	    switch (c) {
	    case 'c':
                cache_capacity = Integer.parseInt(args[i].substring(2, args[i].length()));
                c_flag = true;
                break;
	    case 'b':
                cache_blocksize = Integer.parseInt(args[i].substring(2, args[i].length()));
                b_flag = true;
                break;
	    case 'a':
                cache_associativity = Integer.parseInt(args[i].substring(2, args[i].length()));
                a_flag = true;
                break;
	    case ':':       /* -c without operand */
		System.err.println("Option -" + c + " requires an operand\n");
                errflg = true;
                break;
	    case '?':
		System.err.println("Unrecognized option: -" + c + "\n");
                errflg=true;
	    }
	}

	//check if we have all the options and have no illegal options
	if(errflg || !c_flag || !b_flag || !a_flag) {
	    System.err.println("usage: java Cache -c<capacity> -b<blocksize> -a<associativity>\n");
	    return false;
	}

	return true;

    }

}