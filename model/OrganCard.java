package model;

import java.util.ArrayList;

/**
 * @author: Justin Thomas Moreno Solano
 * OrganCard class
 */

public class OrganCard extends Card {
    private boolean isInfected;
    private boolean isVaccinated;
    private boolean isImmune;
    private ArrayList<Card> playedCards;

    public OrganCard(String type, String color, String name, String effect) {
        super("Organ", color, name, "");
        this.playedCards = new ArrayList<>();
        this.isInfected = false;
        this.isVaccinated = false;
        this.isImmune = false;
    }

    /**
     * Adds a card played on this organ and updates its state accordingly.
     */
    public void addPlayedCard(Card card) {
        if (card instanceof MedicineCard) {
            if (isVaccinated) {
                // Second vaccine makes the organ immune
                isImmune = true;
                isVaccinated = false;
                isInfected = false;
            } else if (!isImmune && !isInfected) {
                // First vaccine makes the organ vaccinated if not infected
                isVaccinated = true;
            }
        } else if (card instanceof VirusCard) {
            if (!isImmune) {
                if (isVaccinated) {
                    // A virus cancels the vaccination
                    isVaccinated = false;
                } else {
                    // No protection -> organ becomes infected
                    isInfected = true;
                }
            }
        }
        playedCards.add(card);
    }

    /**
     * Determines if the organ should be extirpated (removed) due to multiple viruses.
     */
    public boolean shouldExtirpate() {
        int virusCount = 0;
        for (Card card : playedCards) {
            if (card instanceof VirusCard) {
                virusCount++;
            }
        }
        // Two or more viruses cause the organ to be extirpated
        return virusCount >= 2;
    }

    /**
     * Plays a medicine card to vaccinate or immunize the organ.
     */
    public boolean vaccinate() {
        if (!isImmune) {
            MedicineCard medicine = new MedicineCard(this.getColor(), "Vaccine");
            addPlayedCard(medicine);
            return true;
        }
        return false;
    }

    /**
     * Plays an additional medicine card to turn a vaccinated organ into an immune one.
     */
    public boolean immunize() {
        if (isVaccinated && !isImmune) {
            MedicineCard medicine = new MedicineCard(this.getColor(), "Vaccine");
            addPlayedCard(medicine);
            return true;
        }
        return false;
    }

    /**
     * Cures an infected organ by removing all virus cards.
     */
    public boolean cure() {
        if (isInfected) {
            playedCards.removeIf(card -> card instanceof VirusCard);
            // Recalculate the status after curing
            isInfected = false;
            isVaccinated = false;
            isImmune = false;
            for (Card card : playedCards) {
                if (card instanceof MedicineCard) {
                    if (isVaccinated) {
                        isImmune = false;
                        isVaccinated = true;
                    } else {
                        isImmune = true;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Extirpates the organ by clearing all played cards and resetting its state.
     */
    public boolean extirpate() {
        playedCards.clear();
        isInfected = false;
        isVaccinated = false;
        isImmune = false;
        return true;
    }

    /**
     * Plays a virus card to attempt to infect the organ.
     */
    public boolean infect() {
        if (!isImmune) {
            VirusCard virus = new VirusCard(this.getColor(), "Virus");
            addPlayedCard(virus);
            return true;
        }
        return false;
    }

    /**
     * Organs cannot be played on other cards.
     */
    @Override
    public boolean canPlayOn(Card targetCard) {return false;}

    // Getters for organ state
    public boolean isInfected() { return isInfected; }
    public boolean isVaccinated() { return isVaccinated; }
    public boolean isImmune() { return isImmune; }
    public boolean isSane() { return !isInfected; }
}
