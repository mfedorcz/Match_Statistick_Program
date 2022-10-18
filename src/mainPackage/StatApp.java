//@author Michał Fedorczyk
//Version: 0.96
//Program dedicated to calculating real-time percentage of each team's percentage of having a ball. Utilizes simple GUI.

package mainPackage;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;


public class StatApp implements ActionListener{
    public boolean appRunning = true;
    public boolean resetReady = false;
    private Timer timer = new Timer();
    private String runnerState = "Default";
    private JPanel upperPanel = new JPanel();
    private JPanel lowerPanel = new JPanel();
    private String startOrStop = "START";
    private String teamAName = "A";
    private String teamBName = "B";
    private String currentTeam;
    private long timeNumber;
    private String timeString;
    private long minutes = 0;
    private long seconds = 0;
    private long milliseconds = 0;
    public long teamATime;
    public long teamBTime;
    public long teamAPercentage;
    public long teamBPercentage;
    JFrame frame = new JFrame();
    JButton startStop = new JButton(startOrStop);
    JCheckBox teamACheckBox = new JCheckBox(teamAName);
    JCheckBox teamBCheckBox = new JCheckBox(teamBName);
    JButton teamAButton = new JButton(teamAName);
    JButton teamBButton = new JButton(teamBName);
    JLabel ballLabel = new JLabel("Przy piłce:", JLabel.CENTER);
    JLabel teamAPercent = new JLabel("% " + teamAName, JLabel.CENTER);
    JLabel teamBPercent = new JLabel("% " + teamBName, JLabel.CENTER);
    JTextPane timerPane = new JTextPane();
    JTextPane teamADisplayedPercentage = new JTextPane();
    JTextPane teamBDispalayedPercentage = new JTextPane();
    SimpleAttributeSet center = new SimpleAttributeSet();
    PercentageCalculator calculator = new PercentageCalculator();

    JFrame popUpFrame = new JFrame();
    JTextField popUpATextField = new JTextField();
    JTextField popUpBTextField = new JTextField();

    public StatApp(){

        timerPane.replaceSelection("00:00.000");    
        teamADisplayedPercentage.replaceSelection("0" + "%");
        teamBDispalayedPercentage.replaceSelection("0" + "%");
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription("Menu z dodatkowymi funkcjami");
        menuBar.add(menu);
        JLabel menuItem1 = new JLabel("   Autor: Michał Fedorczyk");
        JLabel menuItem2 = new JLabel("   Wersja: 0.96");
        menuItem2.setForeground(new ColorUIResource(207,181,59));
        JMenuItem menuItem3 = new JMenuItem("Reset",KeyEvent.VK_T);
        JMenuItem menuItem4 = new JMenuItem("Wprowadź imiona drużyn",KeyEvent.VK_T);
        menu.add(menuItem1);
        menu.add(menuItem2);
        menu.addSeparator();
        menu.add(menuItem3);
        menu.add(menuItem4);
        
        StyledDocument docTimer = timerPane.getStyledDocument();
        StyledDocument docTeamA = teamADisplayedPercentage.getStyledDocument();
        StyledDocument docTeamB = teamBDispalayedPercentage.getStyledDocument();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        docTimer.setParagraphAttributes(0, docTimer.getLength(), center, false);
        docTeamA.setParagraphAttributes(0, docTeamA.getLength(), center, false);
        docTeamB.setParagraphAttributes(0, docTeamB.getLength(), center, false);
        timerPane.setEditable(false);
        teamADisplayedPercentage.setEditable(false);
        teamBDispalayedPercentage.setEditable(false);
        teamAButton.setEnabled(false);
        teamBButton.setEnabled(false);
        


        startStop.addActionListener(this);
        startStop.setActionCommand("StartStop");
        teamACheckBox.addActionListener(this);
        teamACheckBox.setActionCommand("currentTeamACheck");
        teamBCheckBox.addActionListener(this);
        teamBCheckBox.setActionCommand("currentTeamBCheck");
        teamAButton.addActionListener(this);
        teamAButton.setActionCommand("TeamAButtonPressed");
        teamBButton.addActionListener(this);
        teamBButton.setActionCommand("TeamBButtonPressed");
        menuItem3.addActionListener(this);
        menuItem3.setActionCommand("Reset");
        menuItem4.addActionListener(this);
        menuItem4.setActionCommand("NameSetter");
        upperPanel.add(teamAPercent);
        upperPanel.add(timerPane);
        upperPanel.add(teamBPercent);

        upperPanel.add(teamADisplayedPercentage);
        upperPanel.add(new JLabel());
        upperPanel.add(teamBDispalayedPercentage);

        lowerPanel.add(new JLabel());
        lowerPanel.add(teamACheckBox);
        lowerPanel.add(new JLabel());
        lowerPanel.add(ballLabel);
        lowerPanel.add(new JLabel());

        lowerPanel.add(new JLabel());
        lowerPanel.add(teamBCheckBox);
        lowerPanel.add(startStop);
        lowerPanel.add(teamAButton);
        lowerPanel.add(teamBButton);

        upperPanel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        upperPanel.setLayout(new GridLayout(2,3,5,5));
        lowerPanel.setBorder(BorderFactory.createEmptyBorder(10,30,10,30));
        lowerPanel.setLayout(new GridLayout(2,5,5,5));

        frame.add(upperPanel,  BorderLayout.NORTH);
        frame.add(new JSeparator(), BorderLayout.CENTER);
        frame.add(lowerPanel, BorderLayout.SOUTH);
        frame.setJMenuBar(menuBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Statystyki meczu");
        frame.pack();
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        switch(action){
            case "StartStop"            :   switch(startOrStop){
                                                        case "START"    :   if(teamACheckBox.isSelected() || teamBCheckBox.isSelected()){
                                                                            startStop.setText("STOP");
                                                                            teamAButton.setEnabled(true);
                                                                            teamBButton.setEnabled(true);
                                                                            teamACheckBox.setEnabled(false);
                                                                            teamBCheckBox.setEnabled(false);
                                                                            runnerState = "Start";
                                                                            startOrStop = "STOP";
                                                                            }
                                                                            else{
                                                                                errPopUp("Zaznacz rozpoczynającą drużynę.");
                                                                            }
                                                                            break;
                                                        case "STOP"     :   startStop.setText("START");
                                                                            teamAButton.setEnabled(false);
                                                                            teamBButton.setEnabled(false);
                                                                            runnerState = "Stopped";
                                                                            startOrStop = "RESUME";
                                                                            teamACheckBox.setEnabled(true);
                                                                            teamBCheckBox.setEnabled(true);
                                                                            teamBCheckBox.setSelected(false);
                                                                            teamACheckBox.setSelected(false);
                                                                            currentTeam = "";
                                                                            teamAButton.setBorder(new LineBorder(new ColorUIResource(0,0,0)));
                                                                            teamBButton.setBorder(new LineBorder(new ColorUIResource(0,0,0)));
                                                                            break;
                                                        case "RESUME"   :   if(teamACheckBox.isSelected() || teamBCheckBox.isSelected()){
                                                                                startStop.setText("STOP");
                                                                                runnerState = "Resumed";
                                                                                startOrStop = "STOP";
                                                                                teamACheckBox.setEnabled(false);
                                                                                teamBCheckBox.setEnabled(false);
                                                                                teamAButton.setEnabled(true);
                                                                                teamBButton.setEnabled(true);
                                                                                }   
                                                                            else{
                                                                                errPopUp("Zaznacz wznawiającą drużynę.");
                                                                            }
                                                                            break;
                                                        }
                                            break;
            case "currentTeamACheck"   :   teamBCheckBox.setSelected(false);
                                            currentTeam = teamAName;
                                            teamAButton.setBorder(new LineBorder(new ColorUIResource(255,0,255)));
                                            teamBButton.setBorder(new LineBorder(new ColorUIResource(0,0,0)));
                                            break;
            case "currentTeamBCheck"   :   teamACheckBox.setSelected(false);
                                            currentTeam = teamBName;
                                            teamAButton.setBorder(new LineBorder(new ColorUIResource(0,0,0)));
                                            teamBButton.setBorder(new LineBorder(new ColorUIResource(255,0,255)));
                                            break;
            case "TeamAButtonPressed"   :   currentTeam = teamAName;
                                            teamAButton.setBorder(new LineBorder(new ColorUIResource(255,0,255)));
                                            teamBButton.setBorder(new LineBorder(new ColorUIResource(0,0,0)));
                                            break;
            case "TeamBButtonPressed"   :   currentTeam = teamBName;
                                            teamAButton.setBorder(new LineBorder(new ColorUIResource(0,0,0)));
                                            teamBButton.setBorder(new LineBorder(new ColorUIResource(255,0,255)));
                                            break;
            case "Reset"                :   appRunning = false;
                                            while(!appRunning){
                                                if(resetReady){
                                                    startStop.setText("START");
                                                    startOrStop = "START";
                                                    teamACheckBox.setEnabled(true);
                                                    teamBCheckBox.setEnabled(true);
                                                    teamBCheckBox.setSelected(false);
                                                    teamACheckBox.setSelected(false);
                                                    teamAButton.setEnabled(false);
                                                    teamBButton.setEnabled(false);
                                                    teamAButton.setBorder(new LineBorder(new ColorUIResource(0,0,0)));
                                                    teamBButton.setBorder(new LineBorder(new ColorUIResource(0,0,0)));            
                                                    calculator.reset();
                                                    timer.reset();
                                                    teamADisplayedPercentage.setText(((int)calculator.teamAPercentage) + "%");
                                                    teamBDispalayedPercentage.setText(((int)calculator.teamBPercentage) + "%");
                                                    timeNumber = timer.getElapsedTime();
                                                    timerPane.setText("00:00.000");
                                                    runnerState = "Default";
                                                    appRunning = true;
                                                    resetReady = false;
                                                }
                                            }
                                            break;
            case "NameSetter"           :   namePopUp();
                                            popUpFrame.getContentPane().validate();
                                            popUpFrame.getContentPane().repaint();
                                            break;
            case "SetTheNames"          :   if(popUpATextField.getText().length() > 0 && popUpBTextField.getText().length() > 0){
                                                teamAName = popUpATextField.getText();
                                                teamBName = popUpBTextField.getText();
                                                teamAButton.setText(teamAName);
                                                teamBButton.setText(teamBName);
                                                teamACheckBox.setText(teamAName);
                                                teamBCheckBox.setText(teamBName);
                                                teamBButton.setText(teamBName);
                                                teamAPercent.setText(teamAName);
                                                teamBPercent.setText(teamBName);
                                                popUpFrame.dispose();
                                                teamBCheckBox.setSelected(false);
                                                teamACheckBox.setSelected(false);
                                                break;
                                            }
                                            else{
                                                errPopUp("Podaj poprawne nazwy.");
                                            }                                   
        }
    }


    public void errPopUp(String ErrMessage){
        JFrame errFrame = new JFrame();
        JOptionPane.showMessageDialog(errFrame, ErrMessage, "Błąd", JOptionPane.WARNING_MESSAGE);

    }

    public void namePopUp(){
        JPanel uppperPopUpPanel = new JPanel();
        JPanel lowerPopUpPanel = new JPanel();
        JLabel AName = new JLabel("Nazwa drużyny A:");
        JLabel bName = new JLabel("Nazwa drużyny B:");
        JButton saveButton = new JButton("Zapisz");
        
        saveButton.addActionListener(this);
        saveButton.setActionCommand("SetTheNames");

        uppperPopUpPanel.add(AName);
        uppperPopUpPanel.add(popUpATextField);
        uppperPopUpPanel.add(bName);
        uppperPopUpPanel.add(popUpBTextField);
        lowerPopUpPanel.add(new JLabel(""));
        lowerPopUpPanel.add(saveButton);
        lowerPopUpPanel.add(new JLabel(""));

        uppperPopUpPanel.setBorder(BorderFactory.createEmptyBorder(15,15,5,15));
        uppperPopUpPanel.setLayout(new GridLayout(2,2,5,5));
        lowerPopUpPanel.setBorder(BorderFactory.createEmptyBorder(10,30,10,30));
        lowerPopUpPanel.setLayout(new GridLayout(1,3,5,5));

        popUpFrame.add(uppperPopUpPanel,  BorderLayout.NORTH);
        popUpFrame.add(new JSeparator(), BorderLayout.CENTER);
        popUpFrame.add(lowerPopUpPanel, BorderLayout.SOUTH);
        popUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        popUpFrame.setTitle("Zmiana nazwy");
        popUpFrame.pack();
        popUpFrame.setVisible(true);
    }

    public String timeLongToString(long timeNumber){
        long rest = 0;
        String minutesString = "00";
        String secondsString = "00";
        String millisecondsString = "000";
        minutes = timeNumber/60000;
        rest = timeNumber%60000;
        seconds = rest/1000;
        milliseconds = timeNumber%1000;
        if(minutes < 10){
            minutesString = "0" + minutes;
        }
        else{
            minutesString = "" + minutes;
        }
        if(seconds < 10){
            secondsString = "0" + seconds;
        }
        else{
            secondsString = "" + seconds;
        }
        if(milliseconds < 100){
            if(milliseconds < 10){
                millisecondsString = "00" + milliseconds;
            }
            else{
                millisecondsString = "0" + milliseconds;
            }
        }
        else{
            millisecondsString = "" + milliseconds;
        }
        timeString = minutesString + ":" + secondsString + "." + millisecondsString;
        return(timeString); 
    }

    public void runner(){
        switch(runnerState){
            case "Default":     break;
            case "Start":       timer.start();
                                timeNumber = timer.getElapsedTime();
                                timerPane.setText(timeLongToString(timeNumber));
                                runnerState = "Running";
                                break;
            case "Running":     timeNumber = timer.getElapsedTime();
                                timerPane.setText(timeLongToString(timeNumber));
                                if(currentTeam == teamAName){
                                    calculator.teamATime = calculator.teamATime + timeNumber; 
                                }
                                if(currentTeam == teamBName){
                                    calculator.teamBTime = calculator.teamBTime + timeNumber;
                                }
                                calculator.calculate();
                                teamADisplayedPercentage.setText(((int) Math.round(calculator.teamAPercentage)) + "%");
                                teamBDispalayedPercentage.setText(((int) Math.round(calculator.teamBPercentage)) + "%");
                                break;
            case "Resumed":     timer.start(timeNumber);
                                timeNumber = timer.getElapsedTime();
                                timerPane.setText(timeLongToString(timeNumber));
                                runnerState = "Running";
                                break;
            case "Stopped":     timer.stop();
                                timeNumber = timer.getElapsedTime();
                                timerPane.setText(timeLongToString(timeNumber));
                                runnerState = "Default";
                                break;
        }

    }

    public static void main(String[] args){
        try{
            StatApp statApp = new StatApp();
            while(true){
                if(statApp.appRunning){
                    statApp.runner();
                    statApp.frame.getContentPane().validate();
                    statApp.frame.getContentPane().repaint();
                }
                else{
                    statApp.resetReady = true;
                }
                // System.out.println("Working");
            }
        }
        catch(Exception ex){

        }
    }
}
