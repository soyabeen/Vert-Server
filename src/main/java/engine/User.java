package engine;


import engine.cards.Card;
import engine.cards.CardFactory;
import engine.cards.CardType;

/**
 * Created by alex on 17.03.2016.
 */
public class User {
    private String name;
    private Position position;
    private int cartridges;
    private int bulletsLeft;


    public User(String name) {
        this.name = name;
        position = new Position(0, 0);
        bulletsLeft = 6;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getPosition() {
        return new Position(position.getX(), position.getY());
    }

    public Position move(int dx, int dy) {
        return position.move(dx, dy);
    }

    public Position move(Position position){
        this.position = position;
        return position;
    }

    public int getBulletsLeft() {
        return bulletsLeft;
    }

    public void setBulletsLeft(int bulletsLeft) {
        this.bulletsLeft = bulletsLeft;
    }

    public int getCartridges() {
        return cartridges;
    }

    public void getShot() {
        this.cartridges++;
    }

    public void shoot() {
        bulletsLeft--;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\n" + position +
                "\nTimes shot: " + (6 - bulletsLeft) + "\nTimes was shot: " + cartridges;
    }

    public Card playCard(CardType type) {
        return CardFactory.createCard(type, this);
    }
}
