package gwt.client.view;

import com.google.gwt.user.client.ui.Widget;

public interface FastFlipView {
	
    public enum Pos {
        CURRENT,
        NEXT,
        FAR,
        FARTEST
    }
    
    public interface Presenter {
        void back();
        void next();
        void prev();    
    }

    Widget asWidget();
    void init(Presenter presenter, String blankLink);
    
    void setFrames(int index, String link1, String hLink1, String link2, String hLink2, String link3, String hLink3, int total);
    void next(int index, String link, String hLink);
    void prev(int index, String link, String hLink);
    
}
