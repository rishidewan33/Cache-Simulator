/**
 * A collection of adjacent word entries within the cache. The number of entries
 * is determined by blocksize. 
 *
 * @author Deborah Hawkins
 * @author Rishi Dewan
 */
public class Block {

    private int tag;
    private boolean isValid;
    private boolean isDirty;
    private int blocksize;
    private int recentUse; // higher values are more recently used
    private int[] words;

    /**
     * Class constructor specifing size of block to create in bytes. All blocks
     * are created with this constructor and then modified.
     */
    public Block(int blocksize){
        isValid = false; // doesn't contain valid data yet
        isDirty = false; // doesn't need to be written back
        recentUse = 0; // indicates not used yet
        this.blocksize = blocksize;
        words = new int[blocksize];
    }

    /**
     * Returns the word (32 bytes) of data associated with a particular word 
     * address. Both data and address are expressed as (base 10) ints at this 
     * level.
     *
     * @param address the 32-bit address (as an int) at which to enter the data
     * @param recentUse an update to the recentUse field
     * @pre Check that this block is valid and that the address exists
     * in this block before calling this function.
     */
    protected int read(int address, int useCounter) {
        recentUse = useCounter;
        return words[ (address) % (blocksize) ];
    }

    /**
     * Replaces a word of data within the block for the byte address
     * specified.
     *
     * @param address the 32-bit address (as an int) at which to enter the data
     * @param data a 32-bit datum expressed as an int
     * @param recentUse an update to the recentUse field
     * @throws NotInCacheException If the address specified does not exist
     *                             within this block or if the block is invalid.
     */
    public void write(int offset, int data, int useCounter) {
        recentUse = useCounter;
        words[offset] = data;
        isValid = true;
        isDirty = true;
    }

    /**
     * Fills the block with new data. Should only be used when the block is not
     * dirty and is about to be used for a read or write.
     *
     * @param defines the block uniquely within this set (the first part of the address)
     * @param words an array of data which represent the words in this block
     */
    protected void writeBlock(int tag, int[] words) {
        this.tag = tag;
        this.words = words;
        isValid = true;
        isDirty = false;
    }

    /**
     * Returns the array of all words in the block. Used when the block is dirty
     * and in the least recently used block, so the data can be written back to
     * memory. This allocates new space for other data.
     */
    protected int[] readAllData() {
        return words;
    }

    /**
     * Returns the tag field of the address. The containing set uses this to
     * find an exact address.
     */
    protected int getTag() {
        return tag;
    }

    /**
     * Returns an int which indicates when the block was last used. Higher
     * numbers indicate more recent use.
     */
    protected int getRecentUse() {
        return recentUse;
    }

    /**
     * Returns true if the data contained in this block is valid or false if it
     * has not been set yet.
     */
    protected boolean isValid() {
        return isValid;
    }

    /**
     * Returns true if the data contained in this block has been updated or
     * false if it is still consistent with data in main memory.
     */
    protected boolean isDirty() {
        return isDirty;
    }

    /**
     * Prints the block values to standard out.
     */
    @Override
    public String toString() {
        String result =  (isValid ? 1 : 0) + "        " + IO.int_to_hex(tag) + "   "
                + (isDirty ? 1 : 0) + "        ";
        for (int word : words)
            result += IO.int_to_hex(word) + "     ";
        return result;
    }

}