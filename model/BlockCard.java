package model;

/**
 * @author: Justin Thomas Moreno Solano
 * BlockCard class
 */

public class BlockCard extends Card {

    public BlockCard() {super("Special", "multicolor", "Block Next", "Skips the next player's turn");}
    
    // Determines which players are affected
    public Player[] getPlayers() {return null;}

    // Indicates the next turn should be skipped
    public boolean getNextTurn() {return false;}

    // In real implementation, this would mark the next player to be skipped
    public boolean applyEffect(Game game) {
        game.skipPlayerTurn();
        return true;
    }

    // This card is not played on another card
    @Override
    public boolean canPlayOn(Card targetCard) {return false;}
}