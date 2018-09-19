import org.apache.commons.net.ftp.FTPClient;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LogInFrame extends JFrame {
    private JPasswordField passwdText = new JPasswordField();
    private TextField usernameText = new TextField(10);
    private JButton button = new JButton("登录");
    final String server = "";
    final int port = 21;

    public LogInFrame(){

        setSize(400, 300);
        setResizable(false);
        GridLayout layout = new GridLayout(3,1);
        setLayout(layout);
        add(usernameText);
        add(passwdText);
        add(button);
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width/2;
        int screenHeight = screenSize.height/2;
        int height = this.getHeight();
        int width = this.getWidth();
        setLocation(screenWidth-width/2, screenHeight-height/2);

        button.addActionListener( e -> {
            try{
                String user = usernameText.getText();
                String pass = String.valueOf(passwdText.getPassword());
                FTPClient client = new FTPClient();
                client.connect(server, port);
                client.login(user, pass);
                if(client.getReplyCode() != 230 ){
                    JOptionPane.showMessageDialog(null,"用户名或密码错误！", "登录", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                dispose();
                MyFrame frame = new MyFrame(user, pass);
                frame.setVisible(true);
            }
            catch (IOException ioe){
                ioe.printStackTrace();
                JOptionPane.showMessageDialog(null,"连接异常！", "登录", JOptionPane.ERROR_MESSAGE);
            }

        });


    }
}
