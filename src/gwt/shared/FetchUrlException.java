package gwt.shared;

import java.io.Serializable;

public class FetchUrlException extends Exception implements Serializable{

    private static final long serialVersionUID = 1L;
    
    public FetchUrlException(){
        super();
    }
    
    public FetchUrlException(String message){
        super(message);
    }
    
    public FetchUrlException(String message, Throwable cause){
        super(message, cause);
    }
    
    public FetchUrlException(Throwable cause){
        super(cause);
    }
}
