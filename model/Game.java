package model;

/**
* @author: Justin Thomas Moreno Solano
* Game class
*/

public class Game {
    private Player[] players;
    private Deck deck;
    private int currentPlayerIndex;
    private boolean gameOver;
    private boolean skipNextPlayer;
    private int extraCardsToPlay;

    // Constructor
    public Game(int numPlayers) {
        // Initialize players
        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            players[i] = new Player("Player " + (i + 1), i);
        }
        
        // Initialize deck
        deck = new Deck();
        
        // Initialize state variables
        currentPlayerIndex = 0;
        gameOver = false;
        skipNextPlayer = false;
        extraCardsToPlay = 0;
    }
        
    // Method to start the game
    public void startGame() {
        // Deal initial cards to each player (for example, 3 cards)
        for (Player player : players) {
            player.drawCards(deck, 3);
        }
    }
    
    // Method to play a card
    public boolean playCard(Card card, Player target, Card targetCard) {
        Player currentPlayer = getCurrentPlayer();
        boolean success = false;
        
        // Check if the card is of a special type
        if (card instanceof BlockCard) {
            success = ((BlockCard) card).applyEffect(this);
            if (success) {
                currentPlayer.discardCards(new Card[]{card});
            }
        } else if (card instanceof ThrowTwoCard) {
            success = ((ThrowTwoCard) card).applyEffect(currentPlayer);
            if (success) {
                allowExtraCard();
                currentPlayer.discardCards(new Card[]{card});
            }
        } else if (card instanceof TreatmentCard) {
            success = ((TreatmentCard) card).applyEffect(currentPlayer, target);
            if (success) {
                currentPlayer.discardCards(new Card[]{card});
            }
        } else if (card instanceof VirusCard && targetCard instanceof OrganCard) {
            success = ((VirusCard) card).applyEffect((OrganCard) targetCard);
            if (success) {
                currentPlayer.discardCards(new Card[]{card});
                // FIX: Only check for extirpation if organ should be extirpated
                OrganCard organ = (OrganCard) targetCard;
                if (organ.shouldExtirpate()) {
                    organ.extirpate();
                    removeOrganFromBody(target, targetCard);
                    // Add the organ to the discard pile
                    deck.addToDiscard(targetCard);
                }
            }
        } else if (card instanceof MedicineCard) {
            if (targetCard instanceof OrganCard) {
                success = ((MedicineCard) card).applyEffect((OrganCard) targetCard);
            } else if (targetCard instanceof VirusCard) {
                success = ((MedicineCard) card).applyEffect((VirusCard) targetCard);
            }
            if (success) {
                currentPlayer.discardCards(new Card[]{card});
            }
        } else if (card instanceof OrganCard) {
            success = currentPlayer.addToBody((OrganCard) card);
            if (success) {
                currentPlayer.discardCards(new Card[]{card});
            }
        } 
        return success;
    }

    // Method to advance to the next turn
    public void nextTurn() {
        // Check if the game has ended
        if (checkWinCondition()) {
            gameOver = true;
            return;
        }
        
        // Advance to the next player
        currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
        
        // Check if the next player should be skipped
        if (skipNextPlayer) {
            skipNextPlayer = false;
            nextTurn(); // Recursive call to skip to the next player
        }
    }
    
    // Method to check if there is a winner
    public boolean checkWinCondition() {
        for (Player player : players) {
            if (player.hasCompleteBody()) {
                return true;
            }
        }
        return false;
    }
     
    // Method to check if additional cards can be played
    public boolean canPlayExtraCard() {
        if (extraCardsToPlay > 0) {
            extraCardsToPlay--;
            return true;
        }
        return false;
    }

    // En Game.java
    public boolean removeOrganFromBody(Player player, Card organ) {return player.getBody().remove(organ);}

    public boolean isGameOver() {return gameOver;}

    // Method to mark that the next player should be skipped
    public void skipPlayerTurn() {this.skipNextPlayer = true;}

    // Method to allow playing additional cards
    public void allowExtraCard() {this.extraCardsToPlay += 1;}
   
    // Method to get the current player index
    public int getCurrentPlayerIndex() {return currentPlayerIndex;}

    public Deck getDeck() {return deck;}
    public Player getCurrentPlayer() {return players[currentPlayerIndex];}
    public Player[] getPlayers() {return players;}
}