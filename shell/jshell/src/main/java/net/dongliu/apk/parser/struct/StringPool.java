package net.dongliu.apk.parser.struct;

/**
 * String pool.
 *
 * @author dongliu
 */
public class StringPool {
    private String[] pool;

    public StringPool(int poolSize) {
        pool = new String[poolSize];
    }

    public String get(int idx) {
        return pool[idx];
    }

    public void set(int idx, String value) {
        pool[idx] = value;
    }
    
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	//return super.toString();
    	
    	StringBuilder sb = new StringBuilder();
    	sb.append("\n[\n");
    	for (String s : pool)
    	{
    		sb.append(String.format("%s \n", s));
    	}
    	sb.append("\n]\n");
    	return sb.toString();
    }
}
