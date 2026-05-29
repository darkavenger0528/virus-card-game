package model;

/**
* @author: Justin Thomas Moreno Solano
* MedicineCard class
*/

public class MedicineCard extends Card {

    public MedicineCard(String color, String name) {super("Medicine", color, name, "Heals or protects an organ of the same color");}

    public boolean applyEffect(OrganCard target) {
        // Verifica si los colores coinciden o si alguno es multicolor
        if (this.color.equalsIgnoreCase(target.getColor()) || 
            this.color.equalsIgnoreCase("multicolor") || 
            target.getColor().equalsIgnoreCase("multicolor")) {
    
            // Aplica el efecto según el estado del órgano
            if (target.isInfected()) {
                return target.cure();
            } else if (target.isVaccinated()) {
                return target.immunize();
            } else {
                return target.vaccinate();
            }
        }
        return false;
    }
    
    public boolean applyEffect(VirusCard target) {return this.color.equals(target.getColor());}

    @Override
    public boolean canPlayOn(Card targetCard) {
        if (targetCard instanceof OrganCard) {
            return this.color.equals(targetCard.getColor()) ||
                   this.color.equalsIgnoreCase("multicolor") ||
                   targetCard.getColor().equalsIgnoreCase("multicolor");
        } else if (targetCard instanceof VirusCard) {
            return this.color.equals(targetCard.getColor()) ||
                   this.color.equalsIgnoreCase("multicolor") ||
                   targetCard.getColor().equalsIgnoreCase("multicolor");
        }
        return false;
    }
}