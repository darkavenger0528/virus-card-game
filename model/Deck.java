package model;

/**
* @author: Justin Thomas Moreno Solano
* Deck class
*/

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> activeDeck; // List of cards in play
    private ArrayList<Card> discardPile; // List of discarded cards
    
    // Constructor
    public Deck(){
        this.activeDeck = new ArrayList<>(); // Initialize the deck of cards
        this.discardPile = new ArrayList<>(); // Initialize the discard pile
        initializeDeck(); // Initialize the deck of cards
        Collections.shuffle(activeDeck); // Shuffle the deck
    }  
    
    // Method to initialize the deck with all cards
    private void initializeDeck(){ 

        // ===== 1. INITIALIZE ORGANS (20 cards) =====  
        // 5 blue brains
        for (int i = 0; i < 5; i++) {activeDeck.add(new OrganCard("Organ", "Blue", "Brain", "Body control center"));}
        // 5 red hearts
        for (int i = 0; i < 5; i++) {activeDeck.add(new OrganCard("Organ", "Red", "Heart", "Pumps blood"));}
        // 5 green stomachs
        for (int i = 0; i < 5; i++) {activeDeck.add(new OrganCard("Organ", "Green", "Stomach", "Digests food"));}
        // 5 yellow bones
        for (int i = 0; i < 5; i++) {activeDeck.add(new OrganCard("Organ", "Yellow", "Bone", "Gives body structure"));} 
        // Multicolor
        activeDeck.add(new OrganCard("Organ", "Multicolor", "Organ", "any organ"));
            
        // ===== 2. INITIALIZE VIRUSES (17 cards) =====
        // 4 Viruses per color (16 viruses)
        for (int i = 0; i < 4; i++) {
            activeDeck.add(new VirusCard("Red", "Virus"));   
            activeDeck.add(new VirusCard("Green", "Virus"));   
            activeDeck.add(new VirusCard("Blue", "Virus"));     
            activeDeck.add(new VirusCard("Yellow", "Virus"));   
        }
        // 1 multicolor
        activeDeck.add(new VirusCard("Multicolor", "Virus"));
        
        // ===== 3. INITIALIZE MEDICINES (20 cards) =====
        // 4 medicines per color (16 medicines)
        for (int i = 0; i < 4; i++) {
            activeDeck.add(new MedicineCard( "Red", "Vaccine"));     
            activeDeck.add(new MedicineCard( "Green", "Vaccine"));   
            activeDeck.add(new MedicineCard( "Blue", "Vaccine"));     
            activeDeck.add(new MedicineCard("Yellow", "Vaccine"));
        }
        // 1 multicolor
        activeDeck.add(new MedicineCard("Multicolor", "Antibiotic"));

        // ===== 4. INITIALIZE TREATMENTS (10 cards) =====
        // 2 cards of each type
        for (int i = 0; i < 2; i++) {
        activeDeck.add(new TreatmentCard("Transplant", "Transplant"));
        activeDeck.add(new TreatmentCard("Theft", "Theft"));
        activeDeck.add(new TreatmentCard("Infection", "Infection"));
        activeDeck.add(new TreatmentCard("Latex Glove", "Latex Glove"));
        activeDeck.add(new TreatmentCard("Medical Error", "Medical Error"));
        }
    
        // ===== 5. INITIALIZE SPECIAL CARDS (2 cards) =====
        activeDeck.add(new BlockCard());    // Block the next turn
        activeDeck.add(new ThrowTwoCard()); // Play an additional card

    }

    public ArrayList<Card> handPlayer(){
        ArrayList<Card> hand = new ArrayList<Card>(); // Array of cards in hand
        if(hand.size() >= 3){
            for(int i = 0; i < 3; i++){
                hand.add(activeDeck.get(i)); // Add cards from the deck to the hand array
            }
            return hand; 
        }   
        return null; 
    }

    // Method to draw a card
    public Card drawCard(Player player) {
        if (!activeDeck.isEmpty()) {
            Card card = activeDeck.remove(0);  // Aquí sacamos la carta en la primera posición
            System.out.println("Card drawn: " + card.getName()+" By: "+ player.getName());
            return card;
        }
        System.out.println("No cards left in the deck!");
        return null;  // Retorna null si no hay cartas en el mazo
    }
    
    // Method to add a card to the discard pile
    public void addToDiscard(Card card) {
        if (card != null) {  // We verify that the card is not null to avoid errors
            discardPile.add(card); // Add the card to the discard pile
        }
    }
    
    // Method to reset the deck with cards from the discard pile
    public void resetDeckFromDiscard() {
        if (!discardPile.isEmpty()) {  
            activeDeck.addAll(discardPile); // Move cards from discard pile to the deck
            discardPile.clear(); // Clear the discard pile
            Collections.shuffle(activeDeck); // Shuffle the deck
        }
    }
    
    // Getters
    public ArrayList<Card> getActiveDeck() { return activeDeck; }
    public ArrayList<Card> getDiscardPile() { return discardPile; }
    public int getDiscardPileNum() { return discardPile.size(); }
    public int getActiveDeckNum() { return activeDeck.size(); }
}