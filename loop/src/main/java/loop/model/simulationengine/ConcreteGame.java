package loop.model.simulationengine;

/**
 * An implementation of the {@link Game} interface using {@code int}s to store the payoffs. Also provides
 * static access to the prisoners dilemma.
 * 
 * @author Peter Koepernik
 *
 */
public class ConcreteGame implements Game {
    
    private String name;
    private String description;
    private int cc1;
    private int cn1;
    private int nc1;
    private int nn1;
    private int cc2;
    private int cn2;
    private int nc2;
    private int nn2;
    
    /**
     * Creates a new game with specified name, description and payoffs.
     * 
     * @param name the name of the game
     * @param descriptionthe description of the game
     * @param cc1 payoff of player 1 when both cooperate
     * @param cn1 payoff of player 1 when player 1 cooperates and player 2 doesn't
     * @param nc1 payoff of player 1 when player 1 doesn't cooperate and player 2 does
     * @param nn1 payoff of player 1 when both do not cooperate
     * @param cc2 payoff of player 2 when both cooperate
     * @param cn2 payoff of player 2 when player 1 cooperates and player 2 doesn't
     * @param nc2 payoff of player 2 when player 1 doesn't cooperate and player 2 does
     * @param nn2 payoff of player 2 when both do not cooperate
     */
    public ConcreteGame(final String name, final String description, final int cc1, final int cn1, final int nc1, final int nn1,
            final int cc2, final int cn2, final int nc2, final int nn2) {
        this.name = name;
        this.description = description;
        this.cc1 = cc1;
        this.cn1 = cn1;
        this.nc1 = nc1;
        this.nn1 = nn1;
        this.cc2 = cc2;
        this.cn2 = cn2;
        this.nc2 = nc2;
        this.nn2 = nn2;
    }
    
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public GameResult play(Agent player1, Agent player2, boolean p1Cooperates, boolean p2Cooperates) {      
        int payoff1 = 0;
        int payoff2 = 0;
        
        if (p1Cooperates && p2Cooperates) {
            payoff1 = this.cc1;
            payoff2 = this.cc2;
        } else if (p1Cooperates && !p2Cooperates) {
            payoff1 = this.cn1;
            payoff2 = this.cn2;
        } else if (!p1Cooperates && p2Cooperates) {
            payoff1 = this.nc1;
            payoff2 = this.nc2;
        } else if (!p1Cooperates && !p2Cooperates) {
            payoff1 = this.nn1;
            payoff2 = this.nn2;
        }
        
        player1.addCapital(payoff1);
        player2.addCapital(payoff2);
        
        return new GameResult(player1, player2, p1Cooperates, p2Cooperates, payoff1, payoff2);
    }
    
    /**
     * Returns an instance of this class representing the prisoners dilemma.
     * 
     * @return an instance of this class representing the prisoers dilemma
     */
    public static Game prisonersDilemma() {
        return new ConcreteGame("Prisoner's Dilemma", "The prisoner's dilemma is a standard example of a game analyzed in game"
                + " theory that shows why two completely rational individuals might not cooperate, even if it appears that it is"
                + " in their best interests to do so.", -1, -3, 0, -2, -1, 0, -3, -2);
    }
    
    public static Game stagHunt() {
    	return new ConcreteGame("Stag hunt", "-", 4, 0, 3, 3, 4, 3, 0, 3);
    }
    
    public static Game ChickenGame() {
    	return new ConcreteGame("Chicken game", "-", 4, 2, 6, 0, 4, 6, 2, 0);
    }

    public static Game BattleOfTheSexes() {
    	return new ConcreteGame("Battle of the sexes", "-", 3, 0, 0, 1, 1, 0, 0, 3);
    }
    
    public static Game DictatorGame() {
    	return new ConcreteGame("Dictator game", "-", 1, -1, 0, 0, 1, 2, 0, 0);
    }
    
    public static Game PenaltyShootout() {
    	return new ConcreteGame("Penalty shootout", "-", 0, 1, 1, 0, 1, 0, 0, 1);
    }
    
    /**
     * Returns the value of the local variable cc1.
     * 
     * @return the value of cc1
     */
    public int getCC1() {
        return cc1;
    }
    
    /**
     * Returns the value of the local variable cn1.
     * 
     * @return the value of cn1
     */
    public int getCN1() {
        return cn1;
    }
    
    /**
     * Returns the value of the local variable nc1.
     * 
     * @return the value of nc1
     */
    public int getNC1() {
        return nc1;
    }
    
    /**
     * Returns the value of the local variable nn1.
     * 
     * @return the value of nn1
     */
    public int getNN1() {
        return nn1;
    }
    
    /**
     * Returns the value of the local variable cc2.
     * 
     * @return the value of cc2
     */
    public int getCC2() {
        return cc2;
    }
    
    /**
     * Returns the value of the local variable cn2.
     * 
     * @return the value of cn2
     */
    public int getCN2() {
        return cn2;
    }
    
    /**
     * Returns the value of the local variable nc2.
     * 
     * @return the value of nc2
     */
    public int getNC2() {
        return nc2;
    }
    
    /**
     * Returns the value of the local variable nn2.
     * 
     * @return the value of nn2
     */
    public int getNN2() {
        return nn2;
    }
}
