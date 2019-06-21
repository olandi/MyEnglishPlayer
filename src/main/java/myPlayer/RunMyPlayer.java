package myPlayer;

import myPlayer.components.MainFrame;

import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;

import javax.swing.*;


public class RunMyPlayer {
    public static void main(String[] args) {
        new NativeDiscovery().discover();

        MainFrame mainFrame = new MainFrame();
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }
}
