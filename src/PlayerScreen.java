import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

<<<<<<< HEAD

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
=======
public class PlayerScreen {
>>>>>>> 75102de59af0d0a25b57bfffdd29e07cf23c3433

<<<<<<< HEAD

public class PlayerScreen implements KeyListener{

=======
    private JFrame frame;
    private PlayerService playerService;
    private JTable team1Table;
    private JTable team2Table;
    private List<String[]> players;  // List of players from the database
>>>>>>> 75102de59af0d0a25b57bfffdd29e07cf23c3433

   private JFrame frame;
   private PlayerService playerService;
   private JTable team1Table;
   private JTable team2Table;
   private List<String[]> players;  // List of players from the database

<<<<<<< HEAD
=======
        // Set up key bindings for the frame
        setupKeyBindings(frame);
    }
>>>>>>> 75102de59af0d0a25b57bfffdd29e07cf23c3433

   // Constructor
   public PlayerScreen(JFrame frame) {
       this.frame = frame;
       this.playerService = new PlayerService();
       this.players = playerService.getPlayers();

<<<<<<< HEAD

       frame.addKeyListener(this);
       frame.setFocusable(true);
       frame.requestFocusInWindow();
   }

=======
        // Pink Team table
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        String[] team1Columns = {"Team Pink Players"};
        DefaultTableModel team1Model = new DefaultTableModel(team1Columns, 0);
        team1Table = new JTable(team1Model);
        JScrollPane team1ScrollPane = new JScrollPane(team1Table);
        leftPanel.add(team1ScrollPane, BorderLayout.CENTER);
        customizeTable(team1Table, new Color(250, 128, 114), new Color(255, 182, 193));
      
        // Add Player Button for Pink Team
        JButton addTeam1PlayerButton = new JButton("Add Player to Pink Team");
        addTeam1PlayerButton.addActionListener(e -> addPlayerToTeam(team1Model));
        leftPanel.add(addTeam1PlayerButton, BorderLayout.SOUTH);
>>>>>>> 75102de59af0d0a25b57bfffdd29e07cf23c3433

   // Graphics for the player screen
   public void showPlayerScreen() {
       frame.getContentPane().removeAll();
       frame.setLayout(new GridLayout(1, 2));

<<<<<<< HEAD
       // Pink Team table
       JPanel leftPanel = new JPanel();
       leftPanel.setLayout(new BorderLayout());
       String[] team1Columns = {"Team Pink Players"};
       DefaultTableModel team1Model = new DefaultTableModel(team1Columns, 0);
       team1Table = new JTable(team1Model);
       JScrollPane team1ScrollPane = new JScrollPane(team1Table);
       leftPanel.add(team1ScrollPane, BorderLayout.CENTER);
       customizeTable(team1Table, new Color(250, 128, 114), new Color(255, 182, 193));
=======
        // Add Player Button for Green Team
        JButton addTeam2PlayerButton = new JButton("Add Player to Green Team");
        addTeam2PlayerButton.addActionListener(e -> addPlayerToTeam(team2Model));
        rightPanel.add(addTeam2PlayerButton, BorderLayout.SOUTH);
>>>>>>> 75102de59af0d0a25b57bfffdd29e07cf23c3433

       // Add Player Button for Pink Team
       JButton addTeam1PlayerButton = new JButton("Add Player to Pink Team");
       addTeam1PlayerButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               addPlayerToTeam(team1Model);
           }
       });
       leftPanel.add(addTeam1PlayerButton, BorderLayout.SOUTH);

<<<<<<< HEAD
       // Green Team Table
       JPanel rightPanel = new JPanel();
       rightPanel.setBackground(new Color(152, 251, 152));
       rightPanel.setLayout(new BorderLayout());
       String[] team2Columns = {"Team Green Players"};
       DefaultTableModel team2Model = new DefaultTableModel(team2Columns, 0);
       team2Table = new JTable(team2Model);
       JScrollPane team2ScrollPane = new JScrollPane(team2Table);
       rightPanel.add(team2ScrollPane, BorderLayout.CENTER);
       customizeTable(team2Table, new Color(152, 251, 152), new Color(144, 238, 144));
=======
    private void addPlayerToTeam(DefaultTableModel teamModel) {
        String inputId = JOptionPane.showInputDialog(frame, "Input Player ID:");
>>>>>>> 75102de59af0d0a25b57bfffdd29e07cf23c3433


<<<<<<< HEAD
       // Add Player Button for Green Table
        JButton addTeam2PlayerButton = new JButton("Add Player to Green Team");
       addTeam2PlayerButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               addPlayerToTeam(team2Model); // Add player to team 2
           }
       });
       rightPanel.add(addTeam2PlayerButton, BorderLayout.SOUTH);
=======
            if (player != null) {
                teamModel.addRow(new Object[]{player[1]});
>>>>>>> 75102de59af0d0a25b57bfffdd29e07cf23c3433

<<<<<<< HEAD
=======
                String equipmentCodeInput = JOptionPane.showInputDialog(frame, "Enter Equipment Code for " + player[1] + ":");
                if (equipmentCodeInput != null) {
                    try {
                        int equipmentCode = Integer.parseInt(equipmentCodeInput);
                        UDPTransmit.transmitEquipmentCode(equipmentCode);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(frame, "Invalid Equipment Code. Please enter a valid number.");
                    }
                }
            } else {
                String newCodename = JOptionPane.showInputDialog(frame, "ID not found. Enter new player's name:");
                if (newCodename != null && !newCodename.trim().isEmpty()) {
                    playerService.addNewPlayer(inputId, newCodename);
                    teamModel.addRow(new Object[]{newCodename});
>>>>>>> 75102de59af0d0a25b57bfffdd29e07cf23c3433

<<<<<<< HEAD
       // Add the two panels to the frame
       frame.add(leftPanel);
       frame.add(rightPanel);
       frame.revalidate();
       frame.repaint();
   }
=======
                    String equipmentCodeInput = JOptionPane.showInputDialog(frame, "Enter Equipment Code for " + newCodename + ":");
                    if (equipmentCodeInput != null) {
                        try {
                            int equipmentCode = Integer.parseInt(equipmentCodeInput);
                            UDPTransmit.transmitEquipmentCode(equipmentCode);
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(frame, "Invalid Equipment Code. Please enter a valid number.");
                        }
                    }
                }
            }
        }
    }
>>>>>>> 75102de59af0d0a25b57bfffdd29e07cf23c3433

<<<<<<< HEAD

   private void addPlayerToTeam(DefaultTableModel teamModel) {
   // Prompt the user to input the player's ID
       String inputId = JOptionPane.showInputDialog(frame, "Input Player ID:");
=======
    // Method to find a player by ID
    private String[] findPlayerById(String id) {
        for (String[] player : players) {
            if (player[0].equals(id)) {
                return player;  
            }
        }
        return null;  
    }
>>>>>>> 75102de59af0d0a25b57bfffdd29e07cf23c3433


<<<<<<< HEAD
       if (inputId != null && !inputId.trim().isEmpty()) {
           String[] player = findPlayerById(inputId);
=======
        Font tableFont = new Font("SansSerif", Font.PLAIN, 16);  
        table.setFont(tableFont);                                
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 18));  
    }
>>>>>>> 75102de59af0d0a25b57bfffdd29e07cf23c3433

<<<<<<< HEAD
=======
    // Method for clearing player entries
    private void clearPlayerEntries(DefaultTableModel teamModel) {
        int rowCount = teamModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            teamModel.removeRow(i); 
        }
    }
>>>>>>> 75102de59af0d0a25b57bfffdd29e07cf23c3433

<<<<<<< HEAD
           if (player != null) {
               // Player ID found, add the player to the table
               teamModel.addRow(new Object[]{player[1]});
=======
    // Switch to game display
    private void switchDisplay() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
>>>>>>> 75102de59af0d0a25b57bfffdd29e07cf23c3433


<<<<<<< HEAD
           // Prompt for the equipment code
               String equipmentCodeInput = JOptionPane.showInputDialog(frame, "Enter Equipment Code for " + player[1] + ":");
               if (equipmentCodeInput != null) {
                   try {
                       int equipmentCode = Integer.parseInt(equipmentCodeInput);
                       // Transmit the equipment code via UDP
                       UDPTransmit.transmitEquipmentCode(equipmentCode);
                   } catch (NumberFormatException e) {
                       JOptionPane.showMessageDialog(frame, "Invalid Equipment Code. Please enter a valid number.");
                   }
               }
           } else {
               // Player ID not found, prompt to add a new player
               String newCodename = JOptionPane.showInputDialog(frame, "ID not found. Enter new player's name:");
               if (newCodename != null && !newCodename.trim().isEmpty()) {
                   // Add the new player to the list and database
                   playerService.addNewPlayer(inputId, newCodename);
                   teamModel.addRow(new Object[]{newCodename});
=======
        gameScreen.setBackground(new Color(0, 0, 139));
        gameScreen.add(gameLabel);
>>>>>>> 75102de59af0d0a25b57bfffdd29e07cf23c3433


<<<<<<< HEAD
                   // Prompt for the equipment code
                   String equipmentCodeInput = JOptionPane.showInputDialog(frame, "Enter Equipment Code for " + newCodename + ":");
                   if (equipmentCodeInput != null) {
                       try {
                           int equipmentCode = Integer.parseInt(equipmentCodeInput);
                           // Transmit the equipment code via UDP
                           UDPTransmit.transmitEquipmentCode(equipmentCode);
                       } catch (NumberFormatException e) {
                           JOptionPane.showMessageDialog(frame, "Invalid Equipment Code. Please enter a valid number.");
                       }
                   }
               }
           }
       }
   }
=======
    // Setup key bindings
    private void setupKeyBindings(JFrame frame) {
        InputMap inputMap = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = frame.getRootPane().getActionMap();
>>>>>>> 75102de59af0d0a25b57bfffdd29e07cf23c3433

<<<<<<< HEAD
=======
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), "clearEntries");
        actionMap.put("clearEntries", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearPlayerEntries((DefaultTableModel) team1Table.getModel());
                clearPlayerEntries((DefaultTableModel) team2Table.getModel());
                frame.revalidate();
                frame.repaint();
            }
        });
>>>>>>> 75102de59af0d0a25b57bfffdd29e07cf23c3433

<<<<<<< HEAD
   // Method to find a player by ID
   private String[] findPlayerById(String id) {
       for (String[] player : players) {
           if (player[0].equals(id)) {
               return player; 
           }
       }
       return null; 
   }

   // Method to customize the JTable
   private void customizeTable(JTable table, Color bgColor, Color selectionColor) {
       table.setBackground(bgColor);
       table.setForeground(Color.BLACK);
       table.setSelectionBackground(selectionColor);
       table.setSelectionForeground(Color.WHITE);

       // Set custom font for the table content and headers
       Font tableFont = new Font("SansSerif", Font.PLAIN, 16); 
       table.setFont(tableFont);                               
       table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 18)); 
   }

   //Method for clearing player entries
   private void clearPlayerEntries(DefaultTableModel teamModel) {
   int rowCount = teamModel.getRowCount();
   for (int i = rowCount - 1; i >= 0; i--) {
       teamModel.removeRow(i);
   }
   }

   //Switch to game display
   private void switchDisplay()
   {
       frame.getContentPane().removeAll();
       frame.setLayout(new BorderLayout());

       JPanel gameScreen = new JPanel();
       JLabel gameLabel = new JLabel("Game Started", SwingConstants.CENTER);
       gameLabel.setForeground(Color.WHITE);
       gameLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
       gameScreen.setBackground(new Color(0,0,139));
       gameScreen.add(gameLabel);
       frame.add(gameScreen);
       frame.revalidate();
       frame.repaint();
   }


   //Key listener
   @Override
   public void keyPressed(KeyEvent e) {
       if (e.getKeyCode() == KeyEvent.VK_F12) {
           //clear player entries when F12 pressed
           clearPlayerEntries((DefaultTableModel) team1Table.getModel());
           clearPlayerEntries((DefaultTableModel) team2Table.getModel());
           frame.revalidate();
           frame.repaint();
       }
       else if(e.getKeyCode() == KeyEvent.VK_F5) {
           //switch to game display when F5 press
           switchDisplay();
       }
   }


   @Override
   public void keyReleased(KeyEvent e)  {}


   @Override
   public void keyTyped(KeyEvent e) {}
}

=======
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "switchDisplay");
        actionMap.put("switchDisplay", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchDisplay();
            }
        });
    }
}
>>>>>>> 75102de59af0d0a25b57bfffdd29e07cf23c3433