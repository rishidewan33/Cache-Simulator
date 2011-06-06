

public class Set {
    int associativity;
    int blocksize;
    int useCounter;
    Block[] blocks;

    public Set(int associativity, int blocksize) {
        this.associativity = associativity;
        this.blocksize = blocksize;
        useCounter = 0;
        blocks = new Block[associativity];
        for (int i = 0; i < blocks.length; i++)
            blocks[i] = new Block(blocksize);
    }

    // returns the relevant block, or null if not found
    public int read(int tag, int offset) {
        Block theBlock = findBlock(tag);
        if ( theBlock != null ) {
            return theBlock.read(offset, ++useCounter);
        } else {
            return 0; // this should not happen
        }
    }

    public void write(int tag, int offset, int data) {
        Block theBlock = findBlock(tag);
        if ( theBlock != null ) {
            theBlock.write(offset, data, ++useCounter);
        } else {
            // this shouldn't happen ...
        }
    }

    public Block getLRU() {
        Block lru = blocks[0];
        int least = blocks[0].getRecentUse();
        int temp;
        for (int i = 1; i < associativity; i++) {
            temp = blocks[i].getRecentUse();
            if ( temp < least ) {
                lru = blocks[i];
                least = temp;
            }
        }
        return lru;
    }

    // returns the Block if found, null otherwise
    public Block findBlock(int tag) {
        Block result = null;
        for (int i = 0; i < blocks.length; i++) {
            if ( blocks[i].getTag() == tag && blocks[i].isValid() )
                return blocks[i];
        }
        return result;
    }

}
