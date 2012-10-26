package gwt.client.view;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Widget;

public interface FastFlipView {
	
    public enum Pos {
        CURRENT,
        NEXT,
        FAR,
        FARTEST
    }
    
    public interface Presenter {
        void onBackBtnClicked();
        void onNextBtnClicked();
        
        void onTitleListBtnClicked();
        void onTitleListItemSelected(int index);
    }

    Widget asWidget();
    void init(Presenter presenter, String blankLink);
    
    void setFrames(int index, String link1, String title1, String ruleLink1,
            String link2, String title2, String ruleLink2,
            String link3, String title3, String ruleLink3, int total);
    void next(int index, String link, String title, String ruleLink);
    
    void showTitleList(ArrayList<String> titleList, int selectedIndex);
}
