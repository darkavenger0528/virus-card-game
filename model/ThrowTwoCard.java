package model;

/**
* @author: Justin Thomas Moreno Solano
* ThrowTwoCard class
*/

public class ThrowTwoCard extends Card {

    public ThrowTwoCard() {super("Special", "multicolor", "Throw Two", "Allows an additional card to be played this turn");}

    // Indicates another card can be played
    public boolean getNextTurn() {return true;}

    // Real implementation would allow another card to be played
    public boolean applyEffect(Player player) {return true;}

    // This card is not played on another card
    @Override
    public boolean canPlayOn(Card targetCard) {return false;}
}