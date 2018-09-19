import org.apache.commons.net.ftp.FTPFile;

public class FTPThread extends Thread
{
    private String username;
    private String passwd;
    private FTPThread(String user, String pass)
    {
        super();
        username = user;
        passwd = pass;
    }

    @Override
    public void run()
    {
        try
        {
            FTPConnect conn = new FTPConnect();
            int ret = conn.connect(username, passwd);
            System.out.println(ret);
            FTPFile[] file = conn.getFile();
            for (FTPFile f: file) {
                System.out.println(f.getName());
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args)
//    {
//        //String username = "ftptest";
//        //String passwd = "kudengtx";
//        Thread t = new FTPThread("ftptest", "kudengtx");
//        t.start();
//    }
}