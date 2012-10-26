package gwt.client.ui;

import com.google.gwt.user.client.ui.Frame;

public class CustomFrame extends Frame{

    // In additional to url, this is a link for header. No rules apply. 
    String customUrl;
    // Hack: Let iFrame has its title here for simplicity.
    String customTitle;
    
    public void setCustomUrl(String customUrl) {
        this.customUrl = customUrl;
    }
    
    public String getCustomUrl(){
        return this.customUrl;
    }
    
    public void setCustomTitle(String customTitle) {
        this.customTitle = customTitle;
    }
    
    public String getCustomTitle(){
        return this.customTitle;
    }
}
