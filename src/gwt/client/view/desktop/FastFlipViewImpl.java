package gwt.client.view.desktop;

import gwt.client.ui.CustomFrame;
import gwt.client.view.FastFlipView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class FastFlipViewImpl extends Composite implements FastFlipView {

    @UiTemplate("FastFlipViewImpl.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, FastFlipViewImpl> {
    }
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    public interface Style extends CssResource {
        String iFrame();
        String current();
        String next();
        String far();
        String fartest();
        
        String btn();
        String leftBtn();
        String hideLeftBtn();
        String rightBtn();
        String hideRightBtn();
    }
    
    @UiField
    Style style;

    @UiField
    Button backBtn;
    @UiField
    Anchor anchor;
    @UiField
    Label pageLb;
    @UiField
    CustomFrame iFrame1;
    @UiField
    CustomFrame iFrame2;
    @UiField
    CustomFrame iFrame3;
    @UiField
    CustomFrame iFrame4;
    @UiField
    Button prevBtn;
    @UiField
    Button nextBtn;
    
    private Presenter presenter;
    private String blankLink;
    private int total;
    
    private boolean isMouseOnPrev = false;
    private boolean isMouseOnNext = false;
    
    public FastFlipViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void init(Presenter presenter, String blankLink) {
        this.presenter = presenter;
        this.blankLink = blankLink;
        
        iFrame1.setUrl(blankLink);
        iFrame1.setHUrl("");
        iFrame2.setUrl(blankLink);
        iFrame2.setHUrl("");
        iFrame3.setUrl(blankLink);
        iFrame3.setHUrl("");
        iFrame4.setUrl(blankLink);
        iFrame4.setHUrl("");
        
        iFrame1.setStyleName(style.iFrame() + " " + style.current());
        iFrame2.setStyleName(style.iFrame() + " " + style.next());
        iFrame3.setStyleName(style.iFrame() + " " + style.far());
        iFrame4.setStyleName(style.iFrame() + " " + style.fartest());
        
        anchor.setText("Loading...");
        pageLb.setText("");
        
        new Timer(){
            @Override
            public void run() {
                if(!isMouseOnPrev){
                    prevBtn.setStyleName(style.btn() + " " + style.hideLeftBtn());
                }
                if(!isMouseOnNext){
                    nextBtn.setStyleName(style.btn() + " " + style.hideRightBtn());
                }
            } 
        }.schedule(3000);
    }
    
    @Override
    public void setFrames(int index, String link1, String hLink1, String link2, String hLink2, String link3, String hLink3, int total){
        iFrame1.setUrl(link1);
        iFrame1.setHUrl(hLink1);
        iFrame2.setUrl(link2);
        iFrame2.setHUrl(hLink2);
        iFrame3.setUrl(link3);
        iFrame3.setHUrl(hLink3);
        
        this.total = total;
        updateHeader(index, iFrame1.getHUrl());
    }
    
    @Override
    public void next(int index, String link, String hLink){
        nextIFrame(iFrame1, index, link, hLink);
        nextIFrame(iFrame2, index, link, hLink);
        nextIFrame(iFrame3, index, link, hLink);
        nextIFrame(iFrame4, index, link, hLink);
    }
    
    @Override
    public void prev(int index, String link, String hLink){
        prevIFrame(iFrame1, index, link, hLink);
        prevIFrame(iFrame2, index, link, hLink);
        prevIFrame(iFrame3, index, link, hLink);
        prevIFrame(iFrame4, index, link, hLink);
    }
    
    private void nextIFrame(final CustomFrame frame, int index, String link, String hLink){
        if(frame.getStyleName().contains(style.current())){
            frame.setStyleName(style.iFrame() + " " + style.fartest());
            frame.setUrl(blankLink);
        }else if(frame.getStyleName().contains(style.next())){
            frame.setStyleName(style.iFrame() + " " + style.current());
            updateHeader(index, frame.getHUrl());
        }else if(frame.getStyleName().contains(style.far())){
            frame.setStyleName(style.iFrame() + " " + style.next());
        }else if(frame.getStyleName().contains(style.fartest())){
            frame.setStyleName(style.iFrame() + " " + style.far());
            frame.setUrl(link);
            frame.setHUrl(hLink);
        }else{
            throw new AssertionError(frame.getStyleName());
        }
    }
    
    private void prevIFrame(final CustomFrame frame, int index, String link, String hLink){
        if(frame.getStyleName().contains(style.current())){
            frame.setStyleName(style.iFrame() + " " + style.next());
        }else if(frame.getStyleName().contains(style.next())){
            frame.setStyleName(style.iFrame() + " " + style.far());
        }else if(frame.getStyleName().contains(style.far())){
            frame.setStyleName(style.iFrame() + " " + style.current());
            frame.setUrl(link);
            frame.setHUrl(hLink);
            updateHeader(index, frame.getHUrl());
        }else if(frame.getStyleName().contains(style.fartest())){
            // Do nothing
        }else{
            throw new AssertionError(frame.getStyleName());
        }
    }

    @UiHandler("nextBtn")
    void onNextBtnClicked(ClickEvent event){
        presenter.next();
    }
    
    @UiHandler("nextBtn")
    void onNextBtnMouseOver(MouseOverEvent event){
        nextBtn.setStyleName(style.btn() + " " + style.rightBtn());
        isMouseOnNext = true;
    }
    
    @UiHandler("nextBtn")
    void onNextBtnMouseOut(MouseOutEvent event){
        nextBtn.setStyleName(style.btn() + " " + style.hideRightBtn());
        isMouseOnNext = false;
    }
    
    @UiHandler("prevBtn")
    void onPrevBtnClicked(ClickEvent event){
        presenter.prev();
    }
    
    @UiHandler("prevBtn")
    void onPrevBtnMouseOver(MouseOverEvent event){
        prevBtn.setStyleName(style.btn() + " " + style.leftBtn());
        isMouseOnPrev = true;
    }
    
    @UiHandler("prevBtn")
    void onPrevBtnMouseOut(MouseOutEvent event){
        prevBtn.setStyleName(style.btn() + " " + style.hideLeftBtn());
        isMouseOnPrev = false;
    }
    
    @UiHandler("backBtn")
    void onBackBtnClicked(ClickEvent event){
        presenter.back();
    }
    
    private void updateHeader(int index, String link){
        anchor.setText(link);
        anchor.setHref(link);
        anchor.setTarget("_blank");
        
        pageLb.setText( (index + 1) + "/" + total);
    }
    
    
}
