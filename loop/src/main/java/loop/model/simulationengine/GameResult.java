package loop.model.simulationengine;

import java.util.ArrayList;
import java.util.List;

/**
 * An instance of this class stores the result of a game of two agents. The result consists of:
 * (1) the agents involved
 * (2) the cooperation decisions of the involved agents
 * (3) the received payoffs of the involved agents
 * 
 * @author Peter Koepernik
 *
 */
public class GameResult {
    private Agent player1, player2;
    private boolean hasCooperated1, hasCooperated2;
    private int payoff1, payoff2;
    
    /**
     * Creates a new game result.
     * 
     * @param player1 the first player
     * @param player2 the second player
     * @param hasCooperated1 {@code true} if the first player cooperated, {@code false} otherwise
     * @param hasCooperated2 {@code true} if the second player cooperated, {@code false} otherwise
     * @param payoff1 the received payoff of the first player
     * @param payoff2 the received payoff of the second player
     */
    public GameResult(Agent player1, Agent player2, boolean hasCooperated1, boolean hasCooperated2, int payoff1, int payoff2) {
        this.player1 = player1;
        this.player2 = player2;
        this.hasCooperated1 = hasCooperated1;
        this.hasCooperated2 = hasCooperated2;
        this.payoff1 = payoff1;
        this.payoff2 = payoff2;
    }
    
    /**
     * Returns whether the given agent was one of the players of this game.
     * 
     * @param agent the agent that shall be checked
     * @return {@code true} if the given agent was one of the players of this game, {@code false} otherwise
     */
    public boolean hasAgent(final Agent agent) {
        return (player1 == agent || player2 == agent);
    }
    
    /**
     * Returns the received payoff of the given agent in this game, if he was one of the
     * players, {@code 0} otherwise.
     * 
     * @param agent the agent whose payoff shall bee returned
     * @return the received payoff of the given agent in this game, if he was one of the
     * players, {@code 0} otherwise
     */
    public int getPayoff(final Agent agent) {
        if (agent == player1) return payoff1;
        if (agent == player2) return payoff2;
        return 0;
    }
    
    /**
     * Returns {@code true} if the given agent was one of the players of this game and cooperated,
     * {@code false} otherwise.
     * 
     * @param agent the agent whose cooperation decision shall be returned
     * @return {@code true} if the given agent was one of the players of this game and cooperated,
     * {@code false} otherwise
     */
    public boolean hasCooperated(final Agent agent) {
        if (agent == player1) return hasCooperated1;
        if (agent == player2) return hasCooperated2;
        return false;
    }
    
    /**
     * Returns the two players of this game.
     * 
     * @return the two players of this game
     */
    public List<Agent> getAgents() {
        List<Agent> agents = new ArrayList<Agent>();
        agents.add(player1);
        agents.add(player2);
        return agents;
    }
    
    /**
     * If the given agent was one of the players of this game, returns the other player,
     * otherwise returns {@code null}.
     * 
     * @param agent the player to which the opponent shall be returned
     * @return the other player, if the given agent was a player of this game, otherwise {@code null}
     */
    public Agent getOtherAgent(final Agent agent) {
        if (agent == player1) return player2;
        if (agent == player2) return player1;
        return null;
    }
    
}
