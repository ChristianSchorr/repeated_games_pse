package loop.model.simulationengine;

import java.util.List;

/**
 * Realises the "Absolutkapital" success quantification.
 * 
 * @author Peter Koepernik
 *
 */
public class TotalCapital implements SuccessQuantifier {

    @Override
    public List<Agent> createRanking(final List<Agent> agents, final SimulationHistory history) {
        agents.sort((a1, a2) -> (a2.getCapital() - a1.getCapital()));
        return agents;
    }
}
