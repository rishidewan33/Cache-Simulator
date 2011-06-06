/**
 *
 * @author Deborah Hawkins
 * @author Rishi Dewan
 */
public class IO {

    public static String int_to_hex(int n) {
        String hex_string = Integer.toHexString(n);
	while(hex_string.length() < 8) {
		hex_string = "0" + hex_string;
	}
	return hex_string;
    }

    public static int hex_to_int(String s) {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return val;
    }
}
