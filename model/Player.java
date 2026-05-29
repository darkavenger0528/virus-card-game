package model;

/**
* @author: Justin Thomas Moreno Solano
* Player class
*/

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private ArrayList<Card> hand;
    private ArrayList<Card> body;
    private int id;
    
    // Constructor
    public Player(String name, int id) {
        this.name = name;
        this.id = id;
        this.hand = new ArrayList<>();
        this.body = new ArrayList<>();
    }
    
    // Method to play a card
    public boolean playCard(Card card, Player target) {
        // Check if the player has the card in hand
        if (!hand.contains(card)) {
            return false;
        }
        
        // The specific logic of playing the card will depend on the card type
        // and will be implemented in the Card subclasses
        
        // Remove the card from hand
        hand.remove(card);
        
        return true;
    }
    
    // Method to discard cards
    public void discardCards(Card[] cards) {
        for (Card card : cards) {
            hand.remove(card);
        }
    }
    
    // Method to draw cards
    public void drawCards(Deck deck, int count) {
        for (int i = 0; i < count; i++) {
            Card card = deck.drawCard(this);
            if (card != null) {
                hand.add(card);
            }
        }
    }
    
    // Method to check if the player has a complete body
    public boolean hasCompleteBody() {
        // Required organ colors to complete the body
        String[] requiredColors = {"red", "blue", "green", "yellow"};
        
        // Count how many multicolor organs are healthy/vaccinated/immune
        int multicolorCount = 0;
        for (Card card : body) {
            if (card instanceof OrganCard) {
                OrganCard organ = (OrganCard) card;
                // An organ is valid if it is multicolor and not infected
                if (organ.getColor().equalsIgnoreCase("multicolor") && !organ.isInfected()) {
                    multicolorCount++;
                }
            }
        }
        
        // Count how many specific colors are missing
        int missingColors = 0;
        
        for (String color : requiredColors) {
            boolean hasValidOrgan = false;
            
            for (Card card : body) {
                if (card instanceof OrganCard) {
                    OrganCard organ = (OrganCard) card;
                    // An organ is valid if it matches the required color and is not infected
                    // (healthy, vaccinated or immune)
                    if (organ.getColor().equalsIgnoreCase(color) && !organ.isInfected()) {
                        hasValidOrgan = true;
                        break;
                    }
                }
            }
            
            if (!hasValidOrgan) {
                missingColors++;
            }
        }
        
        // If we have enough multicolor organs to cover the missing colors, we win
        return missingColors <= multicolorCount;
    }
    
    // Method to add an organ to the body
    public boolean addToBody(Card card) {
        if (card instanceof OrganCard) {
            // Verificar si ya tiene un órgano del mismo color
            String color = card.getColor();
            for (Card bodyCard : body) {
                if (bodyCard.getColor().equals(color)) {
                    return false; // Ya tiene un órgano de este color
                }
            }
            
            // Añadir el órgano al cuerpo
            body.add(card);
            return true;
        }
        return false;
    }
    
    // Method to check if the player has an organ of a specific color
    public boolean hasOrgan(String color) {
        for (Card card : body) {
            if (card.getColor().equals(color)) {
                return true;
            }
        }
        return false;
    }
    
    // Method to get an organ of a specific color
    public Card getOrgan(String color) {
        for (Card card : body) {
            if (card.getColor().equals(color)) {
                return card;
            }
        }
        return null;
    }

    // Method to draw one card
    public void drawOneCard(Deck deck) {
        if (hand.size() < 3) {
            Card card = deck.drawCard(this);  // Card is taken from the activeDeck
            if (card != null) {
                hand.add(card);
                System.out.println("You drew: " + card.getName());
            }
        }
    }

    // Method to refill hand to 3 cards
    public void refillHand(Deck deck) {
        while (hand.size() < 3) {
            drawOneCard(deck);  // Calls the previous method until reaching 3 cards
        }
    }
    
    // Method to discard a card
    public void discardCard(Card card) {
        if (hand.contains(card)) {
            hand.remove(card); // Removes the card from hand
            System.out.println("Card discarded: " + card.getName());
        } else {
            System.out.println("This card is not in your hand.");
        }
    }
    
    // Method to remove a card from hand
    public void removeCardFromHand(Card card) {hand.remove(card);}

    public String getName() {return name;}
    public int getId() {return id;}
    public ArrayList<Card> getHand() {return hand;}
    public ArrayList<Card> getBody() {return body;}
    public List<Card> getOrgans() {return body;}
}