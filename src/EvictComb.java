/**
 *
 * @author Rishi
 */
public class EvictComb
{
    int ass;
    int cap;
    int block;
    int evict;

    public EvictComb(int a, int c, int b, int e)
    {
        ass = a;
        cap = c;
        block = b;
        evict = e;
    }
    public int getEvict()
    {
        return evict;
    }
    public String toEvictString()
    {
        return "Evictions: "+evict+" Capacity: "+cap+" Associativity: "+ass+" Block Size: "+block;
    }
}
