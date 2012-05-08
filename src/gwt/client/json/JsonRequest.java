package gwt.client.json;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;

public class JsonRequest {
    
    public interface JsonRequestHandler {
        public void onRequestComplete(JavaScriptObject json);
    }

    public static void get(String url, JsonRequestHandler handler) {
        Date date = new Date();
        String callbackName = "JSONCallback" + handler.hashCode() + date.getTime();
        get(url + callbackName, handler, callbackName);
    }

    public native static void get(String url, JsonRequestHandler handler, String callbackName)/*-{
        
        var scr = document.createElement("script");
        scr.setAttribute("type", "text/javascript");
        scr.setAttribute("src", url);
        
        window[callbackName] = function(j) {
            handler.@gwt.client.json.JsonRequest$JsonRequestHandler::onRequestComplete(Lcom/google/gwt/core/client/JavaScriptObject;)(j);
            window[callbackName + "done"] = true;
        };
        
        // JSON download has 60-second timeout.
        setTimeout(function() {
            if (!window[callbackName + "done"]) {
                handler.@gwt.client.json.JsonRequest$JsonRequestHandler::onRequestComplete(Lcom/google/gwt/core/client/JavaScriptObject;)(null);
            }

            // Cleanup. Remove script and callback elements.
            document.body.removeChild(scr);
            delete window[callback];
            delete window[callback + "done"];
        }, 60000);
        
        //document.getElementsByTagName("body")[0].appendChild(scr);
        document.body.appendChild(scr);
    }-*/;
}