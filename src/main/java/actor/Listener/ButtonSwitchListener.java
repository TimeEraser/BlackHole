package actor.Listener;

import actor.BaseActor;
<<<<<<< HEAD
=======
import command.Request;
import command.Response;
>>>>>>> 9b3c977da614eae073e6be1ea38e19866a553054
import command.Command;
import command.Request;
import command.Response;
import command.SystemResponse;

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
    //involved button
    JButton involvedButton;
    //change element
    private ArrayList<String> textField;
    private ArrayList<ImageIcon> iconField;
    private ArrayList<BaseActor> receiverField;
    private ArrayList<Request> requestField;

    private Boolean ICON_CHANGE=false;
    private Boolean TEXT_CHANGE=false;
    private Boolean MESSAGE_CHANGE=false;
    private Integer size=0;
    private Integer current=0;
    public ButtonSwitchListener(){
        textField=new ArrayList<>();
        iconField=new ArrayList<>();
        receiverField=new ArrayList<>();
        requestField=new ArrayList<>();

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        involvedButton = (JButton) e.getSource();
        involvedButton.setEnabled(false);
        if(MESSAGE_CHANGE)
            sendRequest(receiverField.get(current),requestField.get(current));
    }
    public void setText(Integer index ,String text){
        textField.add(index,text);
        size = Math.min(
                Math.min(textField.size(),iconField.size()),
                Math.min(receiverField.size(),receiverField.size()));
        TEXT_CHANGE=true;
    }
    public void setIcon(Integer index,ImageIcon imageIcon){
        iconField.add(index,imageIcon);
        size = Math.min(
                Math.min(textField.size(),iconField.size()),
                Math.min(receiverField.size(),receiverField.size()));
        ICON_CHANGE=true;
    }
    public void setMessage(Integer index,BaseActor receiver ,Request request){
        receiverField.add(index,receiver);
        requestField.add(index,request);
        size = Math.min(
                Math.min(textField.size(),iconField.size()),
                Math.min(receiverField.size(),receiverField.size()));
        MESSAGE_CHANGE=true;
    }


    @Override
    protected boolean processActorRequest(Request requests) {
        return false;
    }

    @Override
    protected boolean processActorResponse(Response responses) {
        if(responses == SystemResponse.SYSTEM_SUCCESS) {
            current++;
            current = current % size;
            if (TEXT_CHANGE) {
                involvedButton.setText(textField.get(current));
            }
            if (ICON_CHANGE) {
                involvedButton.setIcon(iconField.get(current));
            }
        }
        if(responses == SystemResponse.SYSTEM_FAILURE){
<<<<<<< HEAD
            //JOptionPane.showMessageDialog(null,responses.getConfig().getData(),"系统错误",JOptionPane.ERROR_MESSAGE);
=======
            JOptionPane.showMessageDialog(null,responses.getConfig().getData(),"系统错误",JOptionPane.ERROR_MESSAGE);
>>>>>>> 9b3c977da614eae073e6be1ea38e19866a553054
        }
        involvedButton.setEnabled(true);
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
