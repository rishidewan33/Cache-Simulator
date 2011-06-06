/**
 *
 * @author Rishi
 */
public class MissComb
{
    int ass;
    int cap;
    int block;
    double miss;
    int readm;
    int writem;

    public MissComb(int a, int c, int b, double m)
    {
        ass = a;
        cap = c;
        block = b;
        miss = m;
    }
    public double getMiss()
    {
        return miss;
    }
    public String toMissString()
    {
        return "MissRate: "+miss+" Capacity: "+cap+" Associativity: "+ass+" Block Size: "+block;
    }
}
