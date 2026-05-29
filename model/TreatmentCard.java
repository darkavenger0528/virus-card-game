package model;

/**
* @author: Justin Thomas Moreno Solano
* TreatmentCard class
*/

public class TreatmentCard extends Card {
    private String treatmentType;

    public TreatmentCard(String name, String treatmentType) {
        super("Treatment", "multicolor", name, "Special treatment: " + treatmentType);
        this.treatmentType = treatmentType;
    }

    @Override
    public boolean canPlayOn(Card targetCard) {
        if (treatmentType.equals("Transplant") || treatmentType.equals("Theft") || treatmentType.equals("Infection")) {
            return targetCard instanceof OrganCard;
        }
        return true; // Latex Glove and Medical Error do not require a specific target
    }

    public boolean applyEffect(Player source, Player target) {
        switch (treatmentType) {
            case "Transplant":
            case "Theft":
            case "Infection":
            case "Latex Glove":
            case "Medical Error":
                return true;
            default:
                return false;
        }
    }


    public String getTreatmentType() {
        return treatmentType;
    }
}