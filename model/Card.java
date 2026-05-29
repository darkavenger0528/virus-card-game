package model;

/**
* @author: Justin Thomas Moreno Solano
* Card class
*/

public abstract class Card {
    protected String type;
    protected String color;
    protected String name;
    protected String effect;

    public Card(String type, String color, String name, String effect) {
        this.type = type;
        this.color = color;
        this.name = name;
        this.effect = effect;
    }

    public String getType() { return type; }
    public String getColor() { return color; }
    public String getName() { return name; }
    public String getEffect() { return effect; }

    // Method to determine if this card can be played on a target card
    public abstract boolean canPlayOn(Card targetCard);

}
