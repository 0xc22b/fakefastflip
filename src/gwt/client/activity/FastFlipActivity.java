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

    public static class RssItem {

        public String link;
        public String title;

        public RssItem(String link, String title) {
            this.link = link;
            this.title = title;
        }
    }
    
    private static final String blankLink = "/blank";
    
    private ClientFactory clientFactory;
    private FastFlipPlace place;
    
    private List<RssItem> rssItemList;
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
    public void onBackBtnClicked() {
        clientFactory.getPlaceController().goTo(new HomePlace());
    }
    
    @Override
    public void onNextBtnClicked(){
        if(rssItemList == null){
            return;
        }
        
        if(index + 1 < rssItemList.size()){
            index += 1;
            
            String[] derivedLink = deriveLink(index + 2);
            clientFactory.getFastFlipView().next(index, derivedLink[0],
                    derivedLink[1], derivedLink[2]);
        }
    }
    
    @Override
    public void onTitleListBtnClicked() {
        clientFactory.getFastFlipView().showTitleList(getTitleList(), index);
    }

    @Override
    public void onTitleListItemSelected(int index) {
        setFrames(index);
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
                
                rssItemList = extractLinks(result.getRss());
                if(rssItemList.isEmpty()){
                    Window.alert("No news from this feed. Going back...");
                    clientFactory.getPlaceController().goTo(new HomePlace());
                    return;
                }
                
                ruleList = result.getSRuleList();
                
                setFrames(0);
            }
        });
    }
    
    private void setFrames(int index){
        assert rssItemList != null && rssItemList.size() > index;
        
        this.index = index;
        
        String[] derivedLink1 = deriveLink(index); 
        String[] derivedLink2 = deriveLink(index + 1);
        String[] derivedLink3 = deriveLink(index + 2);
        clientFactory.getFastFlipView().setFrames(index,
                derivedLink1[0], derivedLink1[1], derivedLink1[2],
                derivedLink2[0], derivedLink2[1], derivedLink2[2],
                derivedLink3[0], derivedLink3[1], derivedLink3[2],
                rssItemList.size());
    }
    
    private String[] deriveLink(int index) {
        String link = rssItemList.size() > index ? rssItemList.get(index).link : "";
        String title = link.isEmpty() ? "" : rssItemList.get(index).title;
        String ruleLink = link.isEmpty() ? blankLink : applyRule(link);
        return new String[]{link, title, ruleLink};
    }
    
    private ArrayList<RssItem> extractLinks(String rssXml){
        String link;
        String title;
        ArrayList<RssItem> rssItemList = new ArrayList<RssItem>();
        try{
            // parse the XML document into a DOM
            Document rssDom = XMLParser.parse(rssXml);
            NodeList itemList = rssDom.getElementsByTagName("item");
            for(int i=0; i<itemList.getLength(); i++){
                link = null;
                title = null;
                Node item = itemList.item(i);
                NodeList childNode = item.getChildNodes();
                for(int j=0; j<childNode.getLength(); j++){
                    Node child = childNode.item(j); 
                    if(child.getNodeName().equals("link")){
                        Text linkNode = (Text)child.getFirstChild();
                        link = linkNode.getData();
                    }
                    if(child.getNodeName().equals("title")){
                        Text titleNode = (Text)child.getFirstChild();
                        title = titleNode.getData();
                    }
                }
                if ((link != null && !link.isEmpty())) {
                    rssItemList.add(new RssItem(link, title));
                }
            }
        } catch (DOMException e) {
            Window.alert(e.getMessage() + ": " + e.getCause());
            clientFactory.getPlaceController().goTo(new HomePlace());
        }
        return rssItemList;
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
    
    private ArrayList<String> getTitleList() {
        ArrayList<String> titleList = new ArrayList<String>();
        for (RssItem rssItem : rssItemList) {
            titleList.add(rssItem.title);
        }
        return titleList;
    }
}
