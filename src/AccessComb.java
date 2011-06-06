

/**
 *
 * @author Rishi
 */
public class AccessComb
{
    int ass;
    int cap;
    int block;
    int evict;
    double access;

    public AccessComb(int a, int c, int b, double ac)
    {
        ass = a;
        cap = c;
        block = b;
        access = ac;
    }
    public double getAccessTime()
    {
        return access;
    }
    public String toAccessString()
    {
        return "Average Memory Access Rate: "+access+" Capacity: "+cap+" Associativity: "+ass+" Block Size: "+block;
    }
}
