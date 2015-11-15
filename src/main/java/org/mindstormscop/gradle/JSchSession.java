package org.mindstormscop.gradle;

import com.jcraft.jsch.*;

import java.io.*;

public class JSchSession {

    private final JSch jsch;

    private String host;
    private Integer port;
    private String user;

    public JSchSession(String host, Integer port, String user) {
        jsch = new JSch();
        this.host = host;
        this.port = port;
        this.user = user;
    }

    public void setSshKey(String sshKey) throws JSchException {
        jsch.addIdentity(sshKey);
    }

    public void copyFile(String localFile, String remoteFile) throws IOException, JSchException, RuntimeException {
        Session session = getSession();
        session.connect();

        String command = "scp -t " + remoteFile;
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);

        OutputStream out = channel.getOutputStream();
        InputStream input = channel.getInputStream();

        channel.connect();

        if (checkAck(input) != 0) {
            throw new RuntimeException("scp command failed");
        }

        // send "C0644 filesize filename", where filename should not include '/'
        long filesize = new File(localFile).length();
        command = "C0644 " + filesize + " ";
        if (localFile.lastIndexOf('/') > 0) {
            command += localFile.substring(localFile.lastIndexOf('/') + 1);
        } else {
            command += localFile;
        }
        command += "\n";
        out.write(command.getBytes());
        out.flush();

        if (checkAck(input) != 0) {
            throw new RuntimeException("C0644 failed");
        }

        // send contents of local file
        FileInputStream fis = new FileInputStream(localFile);
        byte[] buf = new byte[1024 * 1024];

        while (true) {
            int len = fis.read(buf, 0, buf.length);
            if (len <= 0)
                break;
            out.write(buf, 0, len); // out.flush();
        }

        fis.close();
        fis = null;

        // send '\0'
        buf[0] = 0;
        out.write(buf, 0, 1);
        out.flush();

        if (checkAck(input) != 0) {
            throw new RuntimeException("send contents failed");
        }
        out.close();

        channel.disconnect();
        session.disconnect();

    }

    private Session getSession() throws JSchException {
        Session session = jsch.getSession(user, host, port);

        UserInfo ui = new DummyUserInfo();
        session.setUserInfo(ui);
        return session;
    }

    private static int checkAck(InputStream input) throws IOException {
        int b = input.read();
        // b may be 0 for success,
        // 1 for error,
        // 2 for fatal error,
        // -1
        if (b <= 0) return b;
        else {
            StringBuilder sb = new StringBuilder();
            int c;
            do {
                c = input.read();
                sb.append((char) c);
            } while (c != '\n');

            System.out.print(sb.toString());
            return b;
        }
    }

    private class DummyUserInfo implements UserInfo {

        @Override
        public String getPassphrase() {
            return null;
        }

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public boolean promptPassphrase(String arg0) {
            return false;
        }

        @Override
        public boolean promptPassword(String arg0) {
            return false;
        }

        @Override
        public boolean promptYesNo(String arg0) {
            return true;
        }

        @Override
        public void showMessage(String arg0) {
        }
    }

}