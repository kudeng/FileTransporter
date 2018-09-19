import org.apache.commons.net.ftp.FTPFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

public class MyFrame extends JFrame{

    private JButton getFileButton = new JButton("show me the file");
    private TextArea getFileText = new TextArea();
    private TextArea logText = new TextArea();


    private JButton connButton = new JButton("CONNECT");
    private JButton metaButton = new JButton("show meta info");

    private FTPThread thread;

    private JButton downloadButton = new JButton("Download");
    private JButton uploadButton = new JButton("Upload");

    private String user;
    private String pass;

    private JFileChooser chooser = new JFileChooser();
    private JOptionPane optionPane = new JOptionPane();

    public MyFrame(String a, String b){
        /* Frame configuration */
        user = a;
        pass = b;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        //frame.setResizable(false);

        /* Frame component*/

        getFileText.setEditable(false);

        /* listener */
        FTPConnect conn = new FTPConnect();


        class ButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Thread getFileThread = new Thread(() -> {
                        try {
                            FTPConnect conn = new FTPConnect();
                            int ret = conn.connect(user, pass);
                            System.out.println(ret);
                            FTPFile[] file = conn.getFile();
                            StringBuilder builder = new StringBuilder("");
                            for (FTPFile f : file) {
                                builder.append(f.getName());
                                builder.append("\n");
                            }
                            getFileText.setText(builder.toString());
                            logText.append(new Date() + "\t查看文件列表" + "\n");
                        }
                        catch (Exception conne){
                            conne.printStackTrace();
                        }
                    });
                    getFileThread.start();
                    while(!getFileThread.isAlive()){
                        getFileThread.stop();
                    }
                }catch (Exception dispe){
                    dispe.printStackTrace();
                }
            }
        }

        class DownloadListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Thread getFileThread = new Thread(() -> {
                        try {
                            FTPConnect conn = new FTPConnect();
                            int ret = conn.connect(user, pass);
                            System.out.println(ret);
                            String fileName = JOptionPane.showInputDialog(downloadButton,  "文件名");
                            if(!fileName.equals("")) {
                                String downloadPath = JOptionPane.showInputDialog(downloadButton, "下载路径");
                                if(!downloadPath.equals("")) {
                                    conn.download(fileName, downloadPath);
                                    logText.append(new Date() + "\t下载成功" +
                                            "\t" + "/Users/kudengma/Desktop/" + fileName + "\n");
                                }
                            }
                        }
                        catch (Exception conne){
                            conne.printStackTrace();
                            logText.append(new Date() + "\t下载失败" + "\n");
                        }
                    });
                    getFileThread.start();
                    while(!getFileThread.isAlive()){
                        getFileThread.stop();
                    }
                }catch (Exception dispe){
                    dispe.printStackTrace();
                }
            }
        }

        class UploadListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Thread getFileThread = new Thread(() -> {
                        try {
                            FTPConnect conn = new FTPConnect();
                            int ret = conn.connect(user, pass);
                            System.out.println(ret);
                            chooser.showOpenDialog(uploadButton);
                            File file = chooser.getSelectedFile();
                            FileInputStream in = new FileInputStream(new File(file.getPath()));
                            conn.upload(file.getName(), in);
                            logText.append(new Date() + "\t上传成功" +
                                    "\t" + "/home/" + user + "/" + file.getName() +"\n");

                            conn.connect(user, pass);
                            FTPFile[] filelist = conn.getFile();
                            StringBuilder builder = new StringBuilder("");
                            for (FTPFile f : filelist) {
                                builder.append(f.getName());
                                builder.append("\n");
                            }
                            getFileText.setText(builder.toString());
                        }
                        catch (Exception conne){
                            conne.printStackTrace();
                        }
                    });
                    getFileThread.start();
                    while(!getFileThread.isAlive()){
                        getFileThread.stop();
                    }
                }catch (Exception dispe){
                    dispe.printStackTrace();
                }
            }
        }

        class MetaButtonListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Thread getFileThread = new Thread(() -> {
                        try {
                            FTPConnect conn = new FTPConnect();
                            int ret = conn.connect(user, pass);
                            System.out.println(ret);
                            String fileName = JOptionPane.showInputDialog(downloadButton,  "文件名");
                            getFileText.setText(conn.getMeta(fileName));
                            logText.append(new Date() + "\t查看文件元信息" + fileName +"\n");
                        }
                        catch (Exception conne){
                            conne.printStackTrace();
                        }
                    });
                    getFileThread.start();
                    while(!getFileThread.isAlive()){
                        getFileThread.stop();
                    }
                }
                catch (Exception dispe){
                    dispe.printStackTrace();
                    logText.append(new Date() + "\t查看元信息失败" + "\n");
                }
            }
        }

        setLayout(new GridLayout(3,2));

        ButtonListener bl = new ButtonListener();
//        ConnectButtonListener cbl = new ConnectButtonListener();
        MetaButtonListener metaButtonListener = new MetaButtonListener();
        DownloadListener downloadListener = new DownloadListener();
        UploadListener uploadListener = new UploadListener();

        getFileButton.addActionListener(bl);
//        connButton.addActionListener(cbl);
        metaButton.addActionListener(metaButtonListener);
        downloadButton.addActionListener(downloadListener);
        uploadButton.addActionListener(uploadListener);

        add(uploadButton);
        add(downloadButton);
        add(getFileButton);
        add(metaButton);
        add(getFileText);
        add(logText);
        logText.setText(new Date() + "\t" + "log system has launched\n");
        setResizable(false);
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width/2;
        int screenHeight = screenSize.height/2;
        int height = this.getHeight();
        int width = this.getWidth();
        setLocation(screenWidth-width/2, screenHeight-height/2);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    }
}