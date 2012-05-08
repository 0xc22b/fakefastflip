package gwt.client.ui;

import com.google.gwt.user.client.ui.Frame;

public class CustomFrame extends Frame{

    String hUrl;
    
    public void setHUrl(String url){
        this.hUrl = url;
    }
    
    public String getHUrl(){
        return this.hUrl;
    }
}
