package actor.Listener;

import actor.BaseActor;
import command.Request;
import command.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by zzq on 16/5/26.
 */
public class ButtonSwitchListener extends BaseActor implements ActionListener {
    private ArrayList<String> textField;
    private ArrayList<ImageIcon> iconField;
    private ArrayList<ActionListener> actionListenerField;
    private Boolean ICON_CHANGE=false;
    private Boolean LISTENER_CHANGE=false;
    private Boolean TEXT_CHANGE=false;
    private Integer size=0;
    private Integer count=0;
    public ButtonSwitchListener(){
        textField=new ArrayList<>();
        iconField=new ArrayList<>();
        actionListenerField=new ArrayList<>();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton involvedButton = (JButton) e.getSource();
        count++;
        count=count%size;
        if(LISTENER_CHANGE){
            for (ActionListener a:
                 involvedButton.getActionListeners()) {
                if(a instanceof NoticeListener){
                    involvedButton.removeActionListener(a);
                }
            }
            involvedButton.addActionListener(actionListenerField.get(count));
        }
        if(TEXT_CHANGE){
            involvedButton.setText(textField.get(count));
        }
        if(ICON_CHANGE){
            involvedButton.setIcon(iconField.get(count));
        }
    }
    public void setText(Integer index ,String text){
        textField.add(index,text);
        size = Math.min(Math.min(textField.size(),iconField.size()),actionListenerField.size());
        TEXT_CHANGE=true;
    }
    public void setIcon(Integer index,ImageIcon imageIcon){
        iconField.add(index,imageIcon);
        size = Math.min(Math.min(textField.size(),iconField.size()),actionListenerField.size());
        ICON_CHANGE=true;
    }
    public void setActionListener(Integer integer,ActionListener actionListener){
        actionListenerField.add(integer,actionListener);
        size = Math.min(Math.min(textField.size(),iconField.size()),actionListenerField.size());
        LISTENER_CHANGE=true;
    }


    @Override
    protected boolean processActorRequest(Request requests) {
        return false;
    }

    @Override
    protected boolean processActorResponse(Response responses) {

        return false;
    }

    @Override
    public boolean start() {
        return false;
    }

    @Override
    public boolean shutdown() {
        return false;
    }
}
