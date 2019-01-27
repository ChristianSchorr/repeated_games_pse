package loop.model.simulationengine;

/**
 * An implementation of the {@link Game} interface using {@code int}s to store the payoffs. Also provides
 * static access to the prisoners dilemma.
 * 
 * @author Peter Koepernik
 *
 */
public class ConcreteGame implements Game {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -1076584176070345040L;
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
     * @return an instance of this class representing the prisoners dilemma
     */
    public static Game prisonersDilemma() {
        return new ConcreteGame("Prisoner's Dilemma", "Two members of a criminal gang are arrested and imprisoned. Each prisoner is in solitary confinement "
        		+ "with no means of communicating with the other. The prosecutors lack sufficient evidence to convict the pair on the principal charge, "
        		+ "but they have enough to convict both on a lesser charge. Simultaneously, the prosecutors offer each prisoner a bargain. Each prisoner is given the opportunity either to betray the other by testifying that the other committed the crime, or to cooperate with the other by remaining silent. The offer is:\n" + 
        		"If A and B each betray the other, each of them serves two years in prison\n" + 
        		"If A betrays B but B remains silent, A will be set free and B will serve three years in prison (and vice versa)\n" + 
        		"If A and B both remain silent, both of them will only serve one year in prison (on the lesser charge).", -1, -3, 0, -2, -1, 0, -3, -2);
    }
    
    public static Game stagHunt() {
    	return new ConcreteGame("Stag hunt", "The Stag hunt game described a situation in which two individuals go out on a hunt. Each can individually choose "
    			+ "to hunt a stag or hunt a hare. Each player must choose an action without knowing the choice of the other. If an individual hunts a stag, "
    			+ "they must have the cooperation of their partner in order to succeed. An individual can get a hare by himself, but a hare is worth less "
    			+ "than a stag.", 4, 0, 3, 3, 4, 3, 0, 3);
    }
    
    public static Game ChickenGame() {
    	return new ConcreteGame("Chicken game", "The game of chicken models two drivers, both headed for a single-lane bridge from opposite directions. The first "
    			+ "to swerve away yields the bridge to the other. If neither player swerves, the result is a costly deadlock in the middle of the bridge, or a "
    			+ "potentially fatal head-on collision. It is presumed that the best thing for each driver is to stay straight while the other swerves (since the "
    			+ "other is the \"chicken\" while a crash is avoided). Additionally, a crash is presumed to be the worst outcome for both players. This yields a "
    			+ "situation where each player, in attempting to secure their best outcome, risks the worst. ", 4, 2, 6, 0, 4, 6, 2, 0);
    }

    public static Game BattleOfTheSexes() {
    	return new ConcreteGame("Battle of the sexes", "In game theory, battle of the sexes (BoS) is a two-player coordination game. Imagine a couple that agreed "
    			+ "to meet this evening, but cannot recall if they will be attending the opera or a football game (and the fact that they forgot is common "
    			+ "knowledge). The husband would prefer to go to the football game. The wife would rather go to the opera. Both would prefer to go to the same "
    			+ "place rather than different ones. If they cannot communicate, where should they go? The payoff matrix labeled \"Battle of the Sexes (1)\" "
    			+ "is an example of Battle of the Sexes, where the wife chooses a row and the husband chooses a column. In each cell, the first number represents "
    			+ "the payoff to the wife and the second number represents the payoff to the husband.", 3, 0, 0, 1, 1, 0, 0, 3);
    }
    
    public static Game PenaltyShootout() {
    	return new ConcreteGame("Penalty shootout", "The penalty shootout game describes the situation of a penalty shootout. The first player is the scorer and the "
    			+ "second player the goalkeeper. The left side of the goal is cooperative and the right side of the goal is not cooperative. "
    			+ "The scorer want to achieve a goal. To get a goal he need to choose the other side than the goalkeeper (cooperate/not cooperate or not cooperate/cooperate"
    			+ " . The goalkeeper want to catch the ball (cooperate/cooperate or not cooperate/not cooperate).", 0, 1, 1, 0, 1, 0, 0, 1);
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
