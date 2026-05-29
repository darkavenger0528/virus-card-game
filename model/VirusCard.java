package model;

/**
* @author: Justin Thomas Moreno Solano
* VirusCard class
*/  

public class VirusCard extends Card {

    public VirusCard(String color, String name) {
        super("Virus", color, name, "Infects an organ of the same color");
    }

    @Override
    public boolean canPlayOn(Card card) {
        if (card instanceof OrganCard) {
            OrganCard organ = (OrganCard) card;
            // Can't play virus on immune organs
            if (organ.isImmune()) {
                return false;
            }
            
            return this.color.equals(card.getColor()) || 
                   this.color.equalsIgnoreCase("multicolor") || 
                   card.getColor().equalsIgnoreCase("multicolor");
        } else if (card instanceof MedicineCard) {
            return this.color.equals(card.getColor()) || 
                   this.color.equalsIgnoreCase("multicolor") || 
                   card.getColor().equalsIgnoreCase("multicolor");
        }
        return false;
    }

    public boolean applyEffect(OrganCard target) {
        if (canPlayOn(target)) {
            // First, add this virus to the organ
            target.addPlayedCard(this);
            return true; // Successfully applied the virus
        }
        return false;
    }
}