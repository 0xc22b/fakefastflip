package gwt.shared.model;

import java.io.Serializable;

public class SRule implements Serializable{

    public enum Comm{
        NoAllowedInIFrame
    }
    
    private static final long serialVersionUID = 1L;

	private String keyString;
	private Comm comm;
	private String arg;
	
	public SRule(){
		
	}

    public SRule(String keyString, Comm comm, String arg) {
        this.keyString = keyString;
        this.comm = comm;
        this.arg = arg;
    }

    public String getKeyString() {
        return keyString;
    }

    public Comm getComm() {
        return comm;
    }

    public String getArg() {
        return arg;
    }
	
}
