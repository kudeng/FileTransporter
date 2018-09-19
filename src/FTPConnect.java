import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;

import java.util.Calendar;

public class FTPConnect {
    private FTPClient ftp = new FTPClient();
    public int connect(String username, String password)
    {
        String server = "39.108.224.195";
        final int port = 21;
        try {
            ftp.connect(server, port);
            ftp.login(username, password);
            int ret = ftp.getReplyCode();
            return ret;
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public FTPFile[] getFile() throws UnknownHostException
    {
        try {
            FTPFile[] file = ftp.listFiles();
            for (int i = 0; i < file.length; i++) {
                FTPFile ff = file[i];
                /* 文件名 */
                String fileName=ff.getName();
                /* 修改时间 */
                Calendar c=ff.getTimestamp();
                /* 文件大小 */
                long size = ff.getSize();
            }
            return file;
        }catch (Exception e){
            e.printStackTrace();
            FTPFile[] ret = null;
            return ret;
        }
    }

    public void download(String fileName, String localPath) {
        try {
            FTPFile[] fs = ftp.listFiles();
            for(FTPFile ff:fs){
                if(ff.getName().equals(fileName)){
                    File localFile = new File(localPath + ff.getName());

                    OutputStream is = new FileOutputStream(localFile);
                    ftp.retrieveFile(ff.getName(), is);
                    is.close();
                }
            }
            ftp.logout();
            ftp.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void upload(String fileName, InputStream local) {
        try {
            ftp.storeFile(fileName, local);
            ftp.logout();
            ftp.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getMeta(String fileName){
        try {
            FTPFile[] fileList = ftp.listFiles();
            StringBuilder builder = new StringBuilder("");
            for (FTPFile f: fileList) {
                if(f.getName().equals(fileName)){
                    builder.append("文件名:" + f.getName() +"\n");
                    Calendar calendar = f.getTimestamp();
                    builder.append("修改时间:" + calendar.getTime() +"\n");
                    builder.append("文件大小:" + f.getSize() +"\n");
                }
            }
            ftp.logout();
            ftp.disconnect();
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}

