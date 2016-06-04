package actor.Listener;

import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;

/**
 * Created by zzq on 16/5/24.
 */
public class MenuSwitchListener implements MenuListener {
    Container contentPane;
    Component content;
//    Component BOTTOMComponent;
    public MenuSwitchListener(Container contentPane, Component content){
        this.contentPane=contentPane;
        this.content=content;
//        this.BOTTOMComponent=BOTTOMComponent;
    }
    @Override
    public void menuSelected(MenuEvent e) {
        for (Component c: contentPane.getComponents()) {
            c.setVisible(false);
        }
        if(content!= null) content.setVisible(true);
//        BOTTOMComponent.setVisible(true);
    }
    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }
}
