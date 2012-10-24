package gwt.client.activity;

import java.util.ArrayList;
import java.util.List;

import gwt.client.ClientFactory;
import gwt.client.place.FastFlipPlace;
import gwt.client.place.HomePlace;
import gwt.client.view.FastFlipView;
import gwt.shared.FieldVerifier;
import gwt.shared.model.SFFF;
import gwt.shared.model.SRule;
import gwt.shared.model.SRule.Comm;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.xml.client.DOMException;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.Text;
import com.google.gwt.xml.client.XMLParser;

public class FastFlipActivity extends AbstractActivity implements FastFlipView.Presenter {

    private static final String blankLink = "/blank";
    
    private ClientFactory clientFactory;
    private FastFlipPlace place;
    
    private List<String> linkList;
    private List<SRule> ruleList;
    private int index;
    
    public FastFlipActivity(FastFlipPlace fastFlipPlace, ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
        this.place = fastFlipPlace;
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        Window.scrollTo(0, 0);
        
        clientFactory.getFastFlipView().init(this, blankLink);
        
        panel.setWidget(clientFactory.getFastFlipView().asWidget());
        
        fetchRssFeed();
    }

    @Override
    public String mayStop() {
        return null;
    }
    
    @Override
    public void onStop() {

    }
    
    @Override
    public void back() {
        clientFactory.getPlaceController().goTo(new HomePlace());
    }
    
    @Override
    public void next(){
        if(linkList == null){
            return;
        }
        
        if(index + 1 < linkList.size()){
            index += 1;
            
            String hLink = index + 2 < linkList.size() ? linkList.get(index + 2) : blankLink;
            String link =  hLink.equals(blankLink) ? blankLink : applyRule(hLink);
            clientFactory.getFastFlipView().next(index, link, hLink);
        }
    }
    
    @Override
    public void prev(){
        if(linkList == null){
            return;
        }
        
        if(index - 1 >= 0){
            index -= 1;
            
            String hLink = linkList.get(index);
            String link = applyRule(linkList.get(index));
            clientFactory.getFastFlipView().prev(index, link, hLink);
        }
    }

    private void fetchRssFeed(){
        String rssUrl = place.getToken();
        
        if(!FieldVerifier.isValidUrl(rssUrl)){
            clientFactory.getPlaceController().goTo(new HomePlace());
            return;
        }
        
        clientFactory.getRpcService().feedRss(rssUrl, new AsyncCallback<SFFF>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage() + caught.getCause());
                clientFactory.getPlaceController().goTo(new HomePlace());
            }
            
            @Override
            public void onSuccess(SFFF result) {
                
                linkList = extractLinks(result.getRss());
                if(linkList.isEmpty()){
                    Window.alert("No news from this feed. Going back...");
                    clientFactory.getPlaceController().goTo(new HomePlace());
                    return;
                }
                
                ruleList = result.getSRuleList();
                
                initializeFrame();
            }
        });
        
        //Special treat for YCombinator news
        /*if(rssUrl.equals("http://news.ycombinator.com/rss")){
            JsonRequest.get("http://api.ihackernews.com/page?format=jsonp&callback=", new JsonRequest.JsonRequestHandler(){
                @Override
                public void onRequestComplete(JavaScriptObject json) {
                    if(json==null){
                        // TODO
                        // Couldn't connect to server (could be timeout, SOP violation, etc.)
                        Window.alert("Couldn't get JSON data from ihackernews API. Going back...");
                        clientFactory.getPlaceController().goTo(new HomePlace());
                        return;
                    }
                    ArrayList<String> linkList = extractLinks(json);
                    //applyRules(linkList);
                    
                    if(linkList.isEmpty()){
                        Window.alert("No news from this feed. Going back...");
                        clientFactory.getPlaceController().goTo(new HomePlace());
                    }
                    
                    clientFactory.getFastFlipView().setFlip(linkList);
                }
            });
            return;
        }*/
    }
    
    private void initializeFrame(){
        assert linkList != null && linkList.size() > 0;
        
        index = 0;
        int size = linkList.size();
        String hLink1 = linkList.get(0);
        String link1 = applyRule(hLink1);
        String hLink2 = size > 1 ? linkList.get(1) : blankLink;
        String link2 = applyRule(hLink2);
        String hLink3 = size > 2 ? linkList.get(2) : blankLink;
        String link3 = applyRule(hLink3);
        clientFactory.getFastFlipView().setFrames(index, link1, hLink1, link2, hLink2, link3, hLink3, linkList.size());
    }
    
    private ArrayList<String> extractLinks(String rssXml){
        ArrayList<String> linkList = new ArrayList<String>();
        try{
            // parse the XML document into a DOM
            Document rssDom = XMLParser.parse(rssXml);
            NodeList itemList = rssDom.getElementsByTagName("item");
            for(int i=0; i<itemList.getLength(); i++){
                Node item = itemList.item(i);
                NodeList childNode = item.getChildNodes();
                for(int j=0; j<childNode.getLength(); j++){
                    Node child = childNode.item(j); 
                    if(child.getNodeName().equals("link")){
                        Text linkNode = (Text)child.getFirstChild();
                        String link = linkNode.getData();
                        if(link != null && !link.isEmpty()){
                            linkList.add(link);
                        }
                    }
                }
            }
        } catch (DOMException e) {
            Window.alert(e.getMessage() + ": " + e.getCause());
            clientFactory.getPlaceController().goTo(new HomePlace());
        }
        return linkList;
    }
    
    private String applyRule(String link){
        String l = link;
        
        for(SRule sRule : ruleList){
            if(sRule.getComm().equals(Comm.NoAllowedInIFrame)){
                if(link.toLowerCase().contains(sRule.getArg().toLowerCase())){
                    l = blankLink;
                }
            }
        }
        
        return l;
    }
    
    private static native Element getBody() /*-{ 
        return $doc.body; 
    }-*/;
    
    /*private ArrayList<String> extractLinks(JavaScriptObject json){
        ArrayList<String> linkList = new ArrayList<String>();
        IYCombinatorInfo yComInfo = json.cast();
        for(int i=0; i<yComInfo.getLinkSize(); i++){
            linkList.add(yComInfo.getLink(i));
        }
        return linkList;
    }*/
    
    /*private void applyRules(ArrayList<String> linkList){
        for(int i=0; i<linkList.size(); i++){
            if(linkList.get(i).contains("www.nytimes.com")){
                linkList.remove(i);
                i -= 1;
                continue;
            }
        }
    }*/
    
    
    
}
