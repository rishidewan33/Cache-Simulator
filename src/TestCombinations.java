import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Deborah Hawkins
 * @author Rishi
 */

public class TestCombinations
{
    static ArrayList<MissComb> missrate = new ArrayList<MissComb>();
    static ArrayList<EvictComb> evictions = new ArrayList<EvictComb>();
    static ArrayList<AccessComb> access = new ArrayList<AccessComb>();

    public static void main(String[] args) throws IOException
    {
        /*
         -c <capacity> with <capacity> in KB: 4, 8, 16, 32, or 64.
        -b <blocksize> with <blocksize> in bytes: 4, 8, 16, 32, 64, 128, 256, or 512.
        -a <associativity> where <associativity> is integer size of set: 1, 2, 4, 8, 16.
         */
        for(int cap = 4; cap < 128; cap*=2)
        {
            for(int block = 4; block < 1024; block*=2)
            {
                for(int associativity = 1; associativity < 32; associativity *= 2)
                {
                    MainMemory m = new MainMemory();
                    String[] a = {"-c"+cap,"-b"+block,"-a"+associativity};
                    if(cap == 4 && associativity == 16 && block == 512)
                    {

                    }
                    else
                    {
                        cache_sim.main(a);
                    }
                }
            }
        }

        TestCombinations.sortMisses(missrate);
        for(MissComb m : missrate)
            System.out.println(m.toMissString());
        System.out.println();

        TestCombinations.sortEvicts(evictions);
        for(EvictComb e : evictions)
            System.out.println(e.toEvictString());
        System.out.println();

        TestCombinations.sortAccess(access);
        for(AccessComb a : access)
            System.out.println(a.toAccessString());
        System.out.println();

    }
    public static void sortMisses(ArrayList<MissComb> al)
    {
        for(int i = 1; i < al.size(); i++)
        {
            for(int j = 0; j < al.size()-i; j++)
            {
                if(al.get(j).getMiss() > al.get(j+1).getMiss())
                {
                    MissComb temp = al.get(j);
                    al.set(j, al.get(j+1));
                    al.set(j+1, temp);
                }
            }
        }
    }
    public static void sortEvicts(ArrayList<EvictComb> al)
    {
        for(int i = 1; i < al.size(); i++)
        {
            for(int j = 0; j < al.size()-i; j++)
            {
                if(al.get(j).getEvict() > al.get(j+1).getEvict())
                {
                    EvictComb temp = al.get(j);
                    al.set(j, al.get(j+1));
                    al.set(j+1, temp);
                }
            }
        }
    }
    public static void sortAccess(ArrayList<AccessComb> al)
    {
        for(int i = 1; i < al.size(); i++)
        {
            for(int j = 0; j < al.size()-i; j++)
            {
                if(al.get(j).getAccessTime() > al.get(j+1).getAccessTime())
                {
                    AccessComb temp = al.get(j);
                    al.set(j, al.get(j+1));
                    al.set(j+1, temp);
                }
            }
        }
    }
}
