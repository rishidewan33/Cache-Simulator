package cache;

import java.text.DecimalFormat;

public class Cache {
    int capacity;
    int associativity;
    int blocksize;
    int numWrites;
    int numReads;
    int numWriteMisses;
    int numReadMisses;
    private int numEvictions;
    MainMemory memory; //Memory to which reads and writes will occur on
    Set[] sets;
    DecimalFormat format = new DecimalFormat("#.######"); //formats a double to 6 decimal places.

    /**
     *
     * @param capacity      the size in words of the cache
     * @param associativity the number of blocks in each set
     * @param blocksize     the size in bytes of each block
     * @param memory        a reference to main memory
     */
    public Cache( int capacity, int associativity, int blocksize, MainMemory memory) {
        
        // initialize counters
        numWrites = 0;
        numReads = 0;
        numWriteMisses = 0;
        numReadMisses = 0;
        numEvictions = 0;

        // initialize values of the cache
        this.capacity = capacity * 1024 / 4;
        this.associativity = associativity;
        this.blocksize = blocksize / 4;
        this.memory = memory;
        sets = new Set[capacity * 1024 / (associativity * blocksize)];
        //sets = new Set[ this.capacity / (this.associativity * this.blocksize) ];

        // create the sets that make up this cache
        for (int i = 0; i < sets.length; i++)
            sets[i] = new Set(this.associativity, this.blocksize);
    }

    public int read( int address ) {
        
        // if the block isn't found in the cache, put it there
        if ( !isInMemory(address) ) {
            allocate(address);
            numReadMisses++;
        }

        // calculate what set the block is in and ask it for the data
        Set set = sets[ ( address / blocksize ) % sets.length ];
        int blockoffset = address % blocksize;
        numReads++;
        return set.read( getTag(address), blockoffset );
    }

    public void write( int address, int data ) {

        // if the block isn't found in the cache, put it there
        if ( !isInMemory(address) ) {
            allocate(address);
            numWriteMisses++;
        }

        // calculate what set the block is in and ask it for the data
        int index = ( address / blocksize ) % sets.length;
        int blockoffset = address % blocksize;
        Set set = sets[index];
        numWrites++;
        set.write( getTag(address), blockoffset, data );
    }
    public void print() {
        System.out.println( "STATISTICS");
        System.out.println("Misses:");
        int totalmiss = numReadMisses + numWriteMisses;
        double totalmissrate = ((double)numReadMisses + (double)numWriteMisses)/((double)numReads+(double)numWrites);
        double totalreadmissrate = ((double)numReadMisses/(double)numReads);
        double totalwritemissrate = ((double)numWriteMisses/(double)numWrites);
        System.out.println("Total: " + totalmiss +
        " DataReads: " + numReadMisses + " DataWrites: " + numWriteMisses);
        System.out.println("Miss rate:");
        System.out.println("Total: "+ format.format(totalmissrate) + " DataReads: " + format.format(totalreadmissrate) + " DataWrites: "+ format.format(totalwritemissrate)); //TODO
        System.out.println("Number of Dirty Blocks Evicted from the Cache: " + numEvictions);
        System.out.println();

        System.out.println("CACHE CONTENTS");
        System.out.println("Set\tV\tTag\tD\tWord0\tWord1\tWord2\tWord3\tWord4\tWord5\tWord6\tWord7"); //TODO
        for (int i = 0; i < sets.length; i++)
        {
        for (Block block : sets[i].blocks)
        System.out.println((Integer.toHexString(i)) + whitespaceformat(Integer.toHexString(i).length()) + block.toString());
        }
        System.out.println();

        //NOTE: The next 3 lines of code were for testing purposes only.
        
        TestCombinations.missrate.add(new MissComb(this.associativity,this.capacity/256,this.blocksize*4,Double.valueOf(format.format(totalmissrate))));
        TestCombinations.evictions.add(new EvictComb(this.associativity,this.capacity/256,this.blocksize*4,numEvictions));
        TestCombinations.access.add(new AccessComb(this.associativity,this.capacity/256,this.blocksize*4,((double)(numEvictions+numReadMisses+numWriteMisses)/(numReads+numWrites))));

        this.flush();
        memory.print();
    }
    private String whitespaceformat(int s)
    {
        String ws = "";
        for(int i = 0; i < (11 - s); i++)
            ws += " ";
        return ws;
    }

    // should only be called if we already know the address is not in cache
    private void allocate( int address ) {

        int index = ( address / blocksize ) % sets.length;
        Set set = sets[index];
        Block evictee = set.getLRU();

        // If the least recently used block is dirty, write it back to
        // main memory.
        if ( evictee.isDirty() ) {
            numEvictions++;
            int[] evicteeData = evictee.readAllData();
            int evicteeAddress = ( evictee.getTag() * sets.length + index ) * blocksize;
            for (int i = 0; i < blocksize; i++)
                memory.write(evicteeAddress + i, evicteeData[i]);
        }

        // Then write the block with the data we need
        int[] newData = new int[blocksize];
        int SAddress = (address/blocksize)*blocksize;
        for (int i = 0; i < blocksize; i++)
            newData[i] = memory.read(SAddress + i);
        evictee.writeBlock( getTag(address), newData );
    }

    private boolean isInMemory( int address ) {
        int index = ( address / blocksize ) % sets.length;
        return sets[index].findBlock( getTag(address) ) != null;
    }

    private int getTag(int address) {
        //System.out.println((int) ( (double)address / ((double)sets.length * (double)blocksize)));
        return ( address / (sets.length * blocksize));
    }
    private void flush()
    {
            for(int i = 0; i < sets.length; i++)
            {
                for(Block block : sets[i].blocks)
                {
                    if(block.isDirty())
                        wb(block,i);
                }
            }
    }
    private void wb(Block b, int in)
    {
        int add = ((b.getTag()*sets.length)+in) * this.blocksize;
        int[] data = b.readAllData();
        for(int i = 0; i < blocksize; i++)
        {
            memory.write(add+i, data[i]);
        }
    }
}