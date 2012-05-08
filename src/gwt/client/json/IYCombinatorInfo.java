package gwt.client.json;

import com.google.gwt.core.client.JavaScriptObject;

public class IYCombinatorInfo extends JavaScriptObject {
    
    protected IYCombinatorInfo(){
    }
    
    public native final int getLinkSize() /*-{
        return this.items.length;
    }-*/;
    
    public native final String getLink(int i) /*-{
        return this.items[i].url;
    }-*/;
    
}
