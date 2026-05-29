package view;

/**
 * @author: Justin Thomas Moreno Solano
 * @author: María Alejandra Rozo Ayala
 * @author: David Santiago Figueroa Mendoza
 * @author: Maria Valentina Caro Ovalle
 * VirusGameGUI class
 */

import java.awt.*;
import javax.swing.*;
import model.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class VirusGameGUI extends JFrame {
    
    // Main panels
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel gameInfoPanel;
    private JPanel playedCardsPanel;
    private JPanel playersPanel;
    
    // UI components
    private JButton discardButton;
    private JLabel currentPlayerLabel;
    private ArrayList<PlayerPanel> playerPanels;
    
    // Game logic
    private Game game;
    private Player currentPlayer;
    private boolean waitingForTarget = false;
    private Card selectedCard = null;
    private String actionType = "";
    
    public VirusGameGUI() {
        initializeWindow();
        createInterface();
        setVisible(true);
    }
    
    private void initializeWindow() {
        setTitle("Virus Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
    }
    
    private void createInterface() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        createLeftPanel();
        createRightPanel();
        
        // Add left panel (25% width)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.25;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(leftPanel, gbc);
        
        // Add right panel (75% width)
        gbc.gridx = 1;
        gbc.weightx = 0.75;
        mainPanel.add(rightPanel, gbc);
        
        add(mainPanel);
    }
    
    private void createLeftPanel() {
        leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Game Information"));
        
        // Game info section
        gameInfoPanel = new JPanel(new GridLayout(0, 1));
        gameInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        discardButton = new JButton("Discard Cards");
        discardButton.addActionListener(e -> showDiscardDialog());
        discardButton.setEnabled(false);
        gameInfoPanel.add(discardButton);
        
        // Played cards history
        playedCardsPanel = new JPanel();
        playedCardsPanel.setLayout(new BoxLayout(playedCardsPanel, BoxLayout.Y_AXIS));
        playedCardsPanel.setBorder(BorderFactory.createTitledBorder("Last Played Cards"));
        
        JScrollPane scrollPane = new JScrollPane(playedCardsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        leftPanel.add(gameInfoPanel, BorderLayout.NORTH);
        leftPanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void createRightPanel() {
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Players"));
        
        // Current player label
        currentPlayerLabel = new JLabel("Turn: -");
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        currentPlayerLabel.setHorizontalAlignment(JLabel.CENTER);
        currentPlayerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        currentPlayerLabel.setForeground(new Color(0, 100, 0));
        
        // Players panel
        playersPanel = new JPanel();
        playerPanels = new ArrayList<>();
        
        rightPanel.add(currentPlayerLabel, BorderLayout.NORTH);
        rightPanel.add(playersPanel, BorderLayout.CENTER);
    }
    
    public void setupGame(int numPlayers) {
        game = new Game(numPlayers);
        game.startGame();
        setupPlayerPanels(numPlayers);
        currentPlayer = game.getCurrentPlayer();
        updateCurrentPlayerDisplay();
        highlightCurrentPlayer();
    }
    
    private void setupPlayerPanels(int numPlayers) {
        playersPanel.removeAll();
        playerPanels.clear();
        
        // Calculate layout
        int rows = (numPlayers <= 3) ? 1 : 2;
        int cols = (numPlayers <= 3) ? numPlayers : (int) Math.ceil((double) numPlayers / rows);
        playersPanel.setLayout(new GridLayout(rows, cols, 5, 5));
        
        // Create player panels
        Player[] players = game.getPlayers();
        for (int i = 0; i < numPlayers; i++) {
            PlayerPanel playerPanel = new PlayerPanel(players[i]);
            playerPanels.add(playerPanel);
            playersPanel.add(playerPanel);
        }
        
        playersPanel.revalidate();
        playersPanel.repaint();
    }
    
    private void updateCurrentPlayerDisplay() {
        if (currentPlayer != null) {
            currentPlayerLabel.setText("Current Turn: " + currentPlayer.getName());
        }
    }
    
    private void highlightCurrentPlayer() {
        for (PlayerPanel panel : playerPanels) {
            panel.setHighlighted(panel.getPlayer() == currentPlayer);
        }
    }
    
    public void processPlayerTurn() {
        updateCurrentPlayerDisplay();
        highlightCurrentPlayer();
        
        for (PlayerPanel panel : playerPanels) {
            panel.setInteractive(panel.getPlayer() == currentPlayer);
        }
        discardButton.setEnabled(true);
    }
    
    public void nextTurn() {
        game.nextTurn();
        currentPlayer = game.getCurrentPlayer();
        processPlayerTurn();
    }
    
    private void addPlayedCardToHistory(String playerName, Card card, String targetInfo) {
        JPanel cardEntry = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cardEntry.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        cardEntry.setBackground(new Color(240, 240, 240));
        
        JLabel playerLabel = new JLabel("<html><b>" + playerName + "</b> played:</html>");
        playerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel cardLabel = new JLabel("<html>" + card.getName() + 
            " (<font color='" + getColorCode(card.getColor()) + "'>" + card.getColor() + "</font>)" + 
            " on: " + targetInfo + "</html>");
        cardLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        
        cardEntry.add(playerLabel);
        cardEntry.add(cardLabel);
        
        playedCardsPanel.add(cardEntry);
        playedCardsPanel.revalidate();
        playedCardsPanel.repaint();
        
        // Auto-scroll to bottom
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = ((JScrollPane) playedCardsPanel.getParent().getParent()).getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }
    
    private String getColorCode(String color) {
        switch (color.toLowerCase()) {
            case "red": return "#FF0000";
            case "green": return "#00CC00";
            case "blue": return "#0000FF";
            case "yellow": return "#CCCC00";
            case "multicolor": return "#9900CC";
            default: return "#000000";
        }
    }
    
    private void updateAllPlayers() {
        for (PlayerPanel panel : playerPanels) {
            panel.updateOrgans();
            panel.updateHand();
        }
    }
    
    private void checkGameOver() {
        for (Player player : game.getPlayers()) {
            if (player.hasCompleteBody()) {
                JOptionPane.showMessageDialog(this,
                    "Congratulations, " + player.getName() + "! You have completed a healthy body.",
                    "Game Over!", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }
    }
    
    private void showDiscardDialog() {
        if (currentPlayer == null) {
            showError("No current player to discard cards.");
            return;
        }
        
        List<Card> hand = currentPlayer.getHand();
        if (hand.isEmpty()) {
            showMessage("Your hand is empty. You cannot discard.", "Empty Hand");
            return;
        }
        
        int numCardsToDiscard = getDiscardAmount();
        if (numCardsToDiscard == -1) return; // User cancelled
        
        switch (numCardsToDiscard) {
            case 1:
                discardSingleCard(hand);
                break;
            case 2:
                discardMultipleCards(hand, 2);
                break;
            case 3:
                discardAllCards(hand);
                break;
        }
        
        currentPlayer.refillHand(game.getDeck());
        updateAllPlayers();
        nextTurn();
    }
    
    private int getDiscardAmount() {
        while (true) {
            String input = JOptionPane.showInputDialog(this,
                "How many cards do you want to discard? (1, 2, or 3)\n" +
                "(If you discard 3, your entire hand will be discarded)",
                "Discard Cards", JOptionPane.QUESTION_MESSAGE);
            
            if (input == null) return -1; // User cancelled
            
            try {
                int amount = Integer.parseInt(input);
                if (amount >= 1 && amount <= 3) {
                    return amount;
                } else {
                    showError("Invalid number. Please enter 1, 2, or 3.");
                }
            } catch (NumberFormatException ex) {
                showError("Invalid input. Please enter a number.");
            }
        }
    }
    
    private void discardSingleCard(List<Card> hand) {
        Card[] cardsArray = hand.toArray(new Card[0]);
        Card cardToDiscard = (Card) JOptionPane.showInputDialog(this,
            "Select one card to discard:", "Discard Card",
            JOptionPane.QUESTION_MESSAGE, null, cardsArray, cardsArray[0]);
        
        if (cardToDiscard != null) {
            currentPlayer.removeCardFromHand(cardToDiscard);
            showMessage(cardToDiscard.getName() + " discarded.", "Card Discarded");
        } else {
            showDiscardDialog(); // Re-show dialog if cancelled
        }
    }
    
    private void discardMultipleCards(List<Card> hand, int count) {
        if (hand.size() < count) {
            showMessage("You don't have " + count + " cards to discard.", "Not Enough Cards");
            showDiscardDialog();
            return;
        }
        
        List<Card> selectedCards = selectMultipleCards(hand, count);
        if (selectedCards == null) {
            showDiscardDialog(); // Re-show if cancelled
            return;
        }
        
        for (Card card : selectedCards) {
            currentPlayer.removeCardFromHand(card);
        }
        showMessage(count + " cards discarded.", "Cards Discarded");
    }
    
    private List<Card> selectMultipleCards(List<Card> hand, int count) {
        JPanel selectionPanel = new JPanel(new GridLayout(0, 1));
        JCheckBox[] checkboxes = new JCheckBox[hand.size()];
        
        for (int i = 0; i < hand.size(); i++) {
            checkboxes[i] = new JCheckBox(hand.get(i).getName() + " (" + hand.get(i).getColor() + ")");
            checkboxes[i].putClientProperty("card", hand.get(i));
            selectionPanel.add(checkboxes[i]);
        }
        
        int result = JOptionPane.showConfirmDialog(this, new JScrollPane(selectionPanel),
            "Select " + count + " cards to discard:", JOptionPane.OK_CANCEL_OPTION);
        
        if (result != JOptionPane.OK_OPTION) return null;
        
        List<Card> selectedCards = new ArrayList<>();
        for (JCheckBox cb : checkboxes) {
            if (cb.isSelected()) {
                selectedCards.add((Card) cb.getClientProperty("card"));
            }
        }
        
        if (selectedCards.size() != count) {
            showError("You must select exactly " + count + " cards. Please try again.");
            return selectMultipleCards(hand, count);
        }
        
        return selectedCards;
    }
    
    private void discardAllCards(List<Card> hand) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to discard your entire hand (" + hand.size() + " cards)?",
            "Confirm Discard All", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            List<Card> cardsToRemove = new ArrayList<>(hand);
            for (Card card : cardsToRemove) {
                currentPlayer.removeCardFromHand(card);
            }
            showMessage("All " + cardsToRemove.size() + " cards discarded.", "Hand Discarded");
        } else {
            showDiscardDialog();
        }
    }
    
    private void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    // Inner class for player panels
    private class PlayerPanel extends JPanel {
        private Player player;
        private JPanel organsPanel;
        private JPanel handPanel;
        private boolean isHighlighted = false;
        private boolean isInteractive = false;
        
        public PlayerPanel(Player player) {
            this.player = player;
            setupPanel();
        }
        
        private void setupPanel() {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createLineBorder(Color.GRAY));
            
            // Player name
            JLabel nameLabel = new JLabel(player.getName());
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            nameLabel.setHorizontalAlignment(JLabel.CENTER);
            nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            
            // Organs panel
            organsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
            organsPanel.setBorder(BorderFactory.createTitledBorder("Organs"));
            
            // Hand panel
            handPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
            handPanel.setBorder(BorderFactory.createTitledBorder("Hand"));
            
            JScrollPane handScrollPane = new JScrollPane(handPanel);
            handScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            handScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            handScrollPane.setPreferredSize(new Dimension(0, 100));
            
            add(nameLabel, BorderLayout.NORTH);
            add(organsPanel, BorderLayout.CENTER);
            add(handScrollPane, BorderLayout.SOUTH);
            
            updateOrgans();
            updateHand();
        }
        
        public void updateOrgans() {
            organsPanel.removeAll();
            
            List<Card> organs = player.getOrgans();
            if (organs.isEmpty()) {
                organsPanel.add(new JLabel("(none)"));
            } else {
                for (Card card : organs) {
                    if (card instanceof OrganCard) {
                        OrganCard organ = (OrganCard) card;
                        CardButton organButton = new CardButton(organ);
                        organButton.addActionListener(new OrganClickListener(organ));
                        organsPanel.add(organButton);
                    }
                }
            }
            
            organsPanel.revalidate();
            organsPanel.repaint();
        }
        
        public void updateHand() {
            handPanel.removeAll();
            
            List<Card> hand = player.getHand();
            if (hand.isEmpty()) {
                handPanel.add(new JLabel("(none)"));
            } else {
                for (Card card : hand) {
                    CardButton cardButton = new CardButton(card);
                    cardButton.addActionListener(new HandCardClickListener(card));
                    cardButton.setEnabled(isInteractive);
                    handPanel.add(cardButton);
                }
            }
            
            handPanel.revalidate();
            handPanel.repaint();
        }
        
        public void setHighlighted(boolean highlighted) {
            this.isHighlighted = highlighted;
            if (highlighted) {
                setBorder(BorderFactory.createLineBorder(new Color(0, 100, 0), 3));
            } else {
                setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }
            repaint();
        }
        
        public void setInteractive(boolean interactive) {
            this.isInteractive = interactive;
            updateHand();
        }
        
        public Player getPlayer() {
            return player;
        }
        
        // Listener for hand cards
        private class HandCardClickListener implements ActionListener {
            private Card card;
            
            public HandCardClickListener(Card card) {
                this.card = card;
            }
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isInteractive) return;
                
                if (waitingForTarget) {
                    showError("You must first select a target for the already selected card.");
                    return;
                }
                
                selectedCard = card;
                handleCardPlay();
            }
            
            private void handleCardPlay() {
                if (selectedCard instanceof OrganCard) {
                    playOrganCard();
                } else if (selectedCard instanceof VirusCard) {
                    startTargetSelection("virus", "Select an organ to infect.");
                } else if (selectedCard instanceof MedicineCard) {
                    startTargetSelection("medicine", "Select an organ to cure or vaccinate.");
                } else if (selectedCard instanceof TreatmentCard) {
                    handleTreatmentCard((TreatmentCard) selectedCard);
                } else if (selectedCard instanceof BlockCard) {
                    playBlockCard();
                } else if (selectedCard instanceof ThrowTwoCard) {
                    playThrowTwoCard();
                }
            }
            
            private void startTargetSelection(String action, String message) {
                waitingForTarget = true;
                actionType = action;
                showMessage(message, "Target Selection");
            }
            
            private void playOrganCard() {
                boolean success = game.playCard(selectedCard, currentPlayer, null);
                if (success) {
                    finishCardPlay("Own board");
                    checkGameOver();
                    if (!game.canPlayExtraCard()) {
                        nextTurn();
                    }
                } else {
                    showError("You cannot play this organ.");
                }
                selectedCard = null;
            }
            
            private void playBlockCard() {
                boolean success = game.playCard(selectedCard, null, null);
                if (success) {
                    finishCardPlay("Block next turn");
                    game.skipPlayerTurn();
                    showMessage("Block card played. The next player will lose their turn.", "Block Activated");
                    nextTurn();
                } else {
                    showError("You cannot play this block card.");
                }
                selectedCard = null;
            }
            
            private void playThrowTwoCard() {
                boolean success = game.playCard(selectedCard, null, null);
                if (success) {
                    finishCardPlay("Play two cards");
                    game.allowExtraCard();
                    showMessage("'Play Two' card activated. You can play another card.", "Additional Action");
                } else {
                    showError("You cannot play this 'Play Two' card.");
                }
                selectedCard = null;
            }
            
            private void handleTreatmentCard(TreatmentCard treatment) {
                String treatmentType = treatment.getTreatmentType();
                
                switch (treatmentType) {
                    case "Transplant":
                    case "Theft":
                    case "Infection":
                    case "Medical Error":
                        startTargetSelection(treatmentType.toLowerCase(), 
                            "Select a target for the treatment: " + treatmentType);
                        break;
                    case "Latex Glove":
                        playLatexGlove();
                        break;
                    default:
                        showError("Unknown treatment type: " + treatmentType);
                        selectedCard = null;
                }
            }
            
            private void playLatexGlove() {
                boolean success = game.playCard(selectedCard, currentPlayer, null);
                if (success) {
                    finishCardPlay("Personal protection");
                    showMessage("Latex Glove applied. You are protected against infections this turn.", 
                        "Protection Activated");
                    nextTurn();
                } else {
                    showError("You cannot play this treatment card.");
                }
                selectedCard = null;
            }
            
            private void finishCardPlay(String targetInfo) {
                addPlayedCardToHistory(currentPlayer.getName(), selectedCard, targetInfo);
                currentPlayer.removeCardFromHand(selectedCard);
                currentPlayer.refillHand(game.getDeck());
                updateAllPlayers();
            }
        }
        
        // Listener for organ clicks (targets)
        private class OrganClickListener implements ActionListener {
            private OrganCard organ;
            
            public OrganClickListener(OrganCard organ) {
                this.organ = organ;
            }
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!waitingForTarget) return;
                
                Player targetPlayer = player;
                
                switch (actionType) {
                    case "virus":
                        playVirusOnOrgan(targetPlayer);
                        break;
                    case "medicine":
                        playMedicineOnOrgan(targetPlayer);
                        break;
                    case "transplant":
                        playTransplant(targetPlayer);
                        break;
                    case "theft":
                        playTheft(targetPlayer);
                        break;
                    case "infection":
                        playInfection(targetPlayer);
                        break;
                    default:
                        showError("Unknown action type for target.");
                        resetTargetSelection();
                }
            }
            
            private void playVirusOnOrgan(Player targetPlayer) {
                VirusCard virus = (VirusCard) selectedCard;
                
                if (!virus.canPlayOn(organ)) {
                    showError("You cannot infect this organ with this virus.");
                    resetTargetSelection();
                    return;
                }
                
                boolean success = game.playCard(selectedCard, targetPlayer, organ);
                if (success) {
                    finishTargetedCardPlay(targetPlayer.getName() + " - " + organ.getName());
                    if (!game.canPlayExtraCard()) {
                        nextTurn();
                    }
                } else {
                    showError("You cannot infect this organ.");
                }
                resetTargetSelection();
            }
            
            private void playMedicineOnOrgan(Player targetPlayer) {
                MedicineCard medicine = (MedicineCard) selectedCard;
                
                if (!medicine.canPlayOn(organ)) {
                    showError("You cannot cure or vaccinate this organ with this medicine.");
                    resetTargetSelection();
                    return;
                }
                
                boolean success = game.playCard(selectedCard, targetPlayer, organ);
                if (success) {
                    finishTargetedCardPlay(targetPlayer.getName() + " - " + organ.getName());
                    if (!game.canPlayExtraCard()) {
                        nextTurn();
                    }
                } else {
                    showError("You cannot cure or vaccinate this organ.");
                }
                resetTargetSelection();
            }
            
            private void playTransplant(Player sourcePlayer) {
                // Select destination player
                String[] playerNames = new String[game.getPlayers().length];
                for (int i = 0; i < game.getPlayers().length; i++) {
                    playerNames[i] = game.getPlayers()[i].getName();
                }
                
                String targetPlayerName = (String) JOptionPane.showInputDialog(
                    VirusGameGUI.this, "Select the destination player:",
                    "Transplant - Step 2", JOptionPane.QUESTION_MESSAGE,
                    null, playerNames, playerNames[0]);
                
                if (targetPlayerName == null) {
                    waitingForTarget = true;
                    return;
                }
                
                Player targetPlayer = findPlayerByName(targetPlayerName);
                if (targetPlayer == null || targetPlayer.hasOrgan(organ.getColor())) {
                    showError(targetPlayer == null ? "Error selecting destination player." : 
                        "The destination player already has an organ of this color.");
                    waitingForTarget = true;
                    return;
                }
                
                // Perform transplant
                sourcePlayer.getBody().remove(organ);
                boolean success = targetPlayer.addToBody(organ);
                
                if (success) {
                    finishTargetedCardPlay("Transplant: " + sourcePlayer.getName() + " -> " + targetPlayer.getName());
                    checkGameOver();
                    if (!game.canPlayExtraCard()) {
                        nextTurn();
                    }
                } else {
                    sourcePlayer.getBody().add(organ);
                    showError("Could not perform the transplant.");
                }
                resetTargetSelection();
            }
            
            private void playTheft(Player targetPlayer) {
                if (currentPlayer.hasOrgan(organ.getColor())) {
                    showError("You already have an organ of this color.");
                    resetTargetSelection();
                    return;
                }
                
                targetPlayer.getBody().remove(organ);
                boolean success = currentPlayer.addToBody(organ);
                
                if (success) {
                    finishTargetedCardPlay("Theft: " + targetPlayer.getName() + " - " + organ.getName());
                    checkGameOver();
                    if (!game.canPlayExtraCard()) {
                        nextTurn();
                    }
                } else {
                    targetPlayer.getBody().add(organ);
                    showError("Could not perform the theft.");
                }
                resetTargetSelection();
            }
            
            private void playInfection(Player targetPlayer) {
                if (organ.isInfected()) {
                    showError("This organ is already infected.");
                    resetTargetSelection();
                    return;
                }
                
                if (organ.isImmune()) {
                    showError("This organ is immune and cannot be infected.");
                    resetTargetSelection();
                    return;
                }
                
                boolean success = organ.infect();
                if (success) {
                    finishTargetedCardPlay("Infection: " + targetPlayer.getName() + " - " + organ.getName());
                    if (!game.canPlayExtraCard()) {
                        nextTurn();
                    }
                } else {
                    showError("Could not perform the infection.");
                }
                resetTargetSelection();
            }
            
            private Player findPlayerByName(String name) {
                for (Player p : game.getPlayers()) {
                    if (p.getName().equals(name)) {
                        return p;
                    }
                }
                return null;
            }
            
            private void finishTargetedCardPlay(String targetInfo) {
                addPlayedCardToHistory(currentPlayer.getName(), selectedCard, targetInfo);
                currentPlayer.removeCardFromHand(selectedCard);
                currentPlayer.refillHand(game.getDeck());
                updateAllPlayers();
            }
            
            private void resetTargetSelection() {
                waitingForTarget = false;
                selectedCard = null;
                actionType = "";
            }
        }
    }
    
    // Card button component
    private class CardButton extends JButton {
        private Card card;
        
        public CardButton(Card card) {
            this.card = card;
            setupButton();
        }
        
        private void setupButton() {
            setText(card.getName());
            setToolTipText(card.getType() + " - " + card.getColor());
            setBackground(getCardColor(card.getColor()));
            setForeground(Color.BLACK);
            setPreferredSize(new Dimension(80, 60));
            setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
            setFocusPainted(false);
            
            if (card instanceof OrganCard) {
                updateOrganDisplay((OrganCard) card);
            }
        }
        
        private void updateOrganDisplay(OrganCard organ) {
            String statusText = "";
            Color statusColor = Color.BLACK;
            
            if (organ.isInfected()) {
                statusText = "INFECTED";
                statusColor = Color.RED;
            } else if (organ.isImmune()) {
                statusText = "IMMUNE";
                statusColor = Color.BLUE;
            } else if (organ.isVaccinated()) {
                statusText = "VACCINATED";
                statusColor = Color.GREEN;
            } else {
                statusText = "HEALTHY";
                statusColor = Color.YELLOW.darker();
            }
            
            setText("<html>" + card.getName() + "<br><font size='-2' color='" + 
                colorToHex(statusColor) + "'><i>" + statusText + "</i></font></html>");
        }
        
        private Color getCardColor(String colorName) {
            switch (colorName.toLowerCase()) {
                case "red": return new Color(255, 100, 100);
                case "green": return new Color(100, 255, 100);
                case "blue": return new Color(100, 100, 255);
                case "yellow": return new Color(255, 255, 100);
                case "multicolor": return new Color(200, 100, 200);
                default: return Color.LIGHT_GRAY;
            }
        }
        
        private String colorToHex(Color color) {
            return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        }
    }
    
    // Main method for launching the game
    public static void main(String[] args) {
        // Set system look and feel for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }
        
        // Create and show the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Show welcome dialog and get number of players
            int numPlayers = getNumberOfPlayers();
            if (numPlayers == -1) {
                System.exit(0); // User cancelled
                return;
            }
            
            // Create and setup the game
            VirusGameGUI gameGUI = new VirusGameGUI();
            gameGUI.setupGame(numPlayers);
            gameGUI.processPlayerTurn();
            
            // Show game instructions
            showGameInstructions(gameGUI);
        });
    }
    
    /**
     * Shows a dialog to get the number of players for the game
     */
    private static int getNumberOfPlayers() {
        while (true) {
            String input = JOptionPane.showInputDialog(
                null,
                "Welcome to Virus Game!\n\n" +
                "How many players will participate? (2-6 players)",
                "Game Setup",
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (input == null) {
                return -1; // User cancelled
            }
            
            try {
                int numPlayers = Integer.parseInt(input.trim());
                if (numPlayers >= 2 && numPlayers <= 6) {
                    return numPlayers;
                } else {
                    JOptionPane.showMessageDialog(
                        null,
                        "Invalid number of players. Please enter a number between 2 and 6.",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE
                    );
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Please enter a valid number.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }
    
    /**
     * Shows game instructions to the user
     */
    private static void showGameInstructions(JFrame parent) {
        String instructions = 
            "<html><body style='width: 400px; font-family: Arial, sans-serif;'>" +
            "<h2 style='color: #2E7D32;'>How to Play Virus Game</h2>" +
            
            "<h3>Objective:</h3>" +
            "<p>Be the first player to complete a healthy body with 4 different colored organs " +
            "(Red, Green, Blue, Yellow).</p>" +
            
            "<h3>Card Types:</h3>" +
            "<ul>" +
            "<li><b>Organ Cards:</b> Place in your body to build it</li>" +
            "<li><b>Virus Cards:</b> Infect opponents' organs</li>" +
            "<li><b>Medicine Cards:</b> Cure infections or create immunity</li>" +
            "<li><b>Treatment Cards:</b> Special actions (Transplant, Theft, etc.)</li>" +
            "<li><b>Block Cards:</b> Skip the next player's turn</li>" +
            "<li><b>Play Two Cards:</b> Allows playing an additional card</li>" +
            "</ul>" +
            
            "<h3>How to Play:</h3>" +
            "<ol>" +
            "<li>Click on cards in your hand to play them</li>" +
            "<li>For targeted cards, click on the target organ after selecting the card</li>" +
            "<li>Use the 'Discard Cards' button to discard 1, 2, or 3 cards and end your turn</li>" +
            "<li>The current player is highlighted in green</li>" +
            "</ol>" +
            
            "<h3>Organ States:</h3>" +
            "<ul>" +
            "<li><b>Healthy:</b> Can be infected by viruses</li>" +
            "<li><b>Infected:</b> Can be cured with medicine</li>" +
            "<li><b>Vaccinated:</b> Protected from one infection</li>" +
            "<li><b>Immune:</b> Cannot be infected (vaccinated + cured)</li>" +
            "</ul>" +
            
            "<p><b>Good luck and have fun!</b></p>" +
            "</body></html>";
        
        JOptionPane.showMessageDialog(
            parent,
            instructions,
            "Game Instructions",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}