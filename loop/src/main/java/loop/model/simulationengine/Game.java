package loop.model.simulationengine;

import java.io.Serializable;

import loop.model.Nameable;

/**
 * This interface represents a game theoretic game.
 * 
 * @author Peter Koepernik
 *
 */
public interface Game extends Nameable, Serializable {
    
    /**
     * Lets two players with given cooperation decisions play this game, adds their payoffs
     * to their capital and returns the result of the game as {@link GameResult}.
     * 
     * @param player1 the first player
     * @param player2 the second player
     * @param p1Cooperates whether the first player cooperates
     * @param p2Cooperates whether the second player cooperates
     * @return
     */
    GameResult play(Agent player1, Agent player2, boolean p1Cooperates, boolean p2Cooperates);
}
