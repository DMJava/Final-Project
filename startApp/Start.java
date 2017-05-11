package Final_project.startApp;

import Final_project.console.UserVsAdmin;

import java.io.IOException;

public class Start {
    public static void main(String[] args) throws IOException {
        UserVsAdmin userVsAdmin = new UserVsAdmin();
        userVsAdmin.console();
    }
}
