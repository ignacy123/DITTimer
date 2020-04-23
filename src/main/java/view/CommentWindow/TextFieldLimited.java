package view.CommentWindow;

import java.awt.*;
import java.awt.event.*;

public class TextFieldLimited extends TextField
        implements KeyListener {
    private int maxLength;

    public TextFieldLimited() {
        super();
        addKeyListener(this);
    }
    public void setMaxLength(int max){
        this.maxLength = max;
    }
    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
        int len = getText().length();
        if (len < maxLength) {
            return;
        } else {
            if ((c == KeyEvent.VK_BACK_SPACE) ||
                    (c == KeyEvent.VK_DELETE) ||
                    (c == KeyEvent.VK_ENTER) ||
                    (c == KeyEvent.VK_TAB) ||
                    e.isActionKey())
                return;
            else {
                e.consume();
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}