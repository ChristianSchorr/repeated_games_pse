package loop.model.simulationengine.strategies;

/**
 * This interface extends the {@link Vector<Double>} interface as a vector that has only real components.
 * It provides additional functionality such as the calculation of euclidean or sum norm or the multiplication
 * by real scalars and the addition with other RealVectors.
 * 
 * @author Peter Koepernik
 *
 */
public interface RealVector extends Vector<Double> {
    
    /**
     * Returns the euclidean norm of the vector.
     * 
     * @return the euclidean norm of the vector
     */
    double getEuclideanNorm();
    
    /**
     * Returns the sum norm of the vector.
     * 
     * @return the sum norm of the vector
     */
    double getSumNorm();
    
    /**
     * Multiplies the vector by the given scalar and returns itself.
     * 
     * @param scalar the scalar the vector shall be multiplied with
     * @return this vector after multiplication
     */
    RealVector mutliplyBy(double scalar);
    
    /**
     * Adds the given vector to this vector and returns itself.
     * 
     * @param vector the vector that shall be added to this vector
     * @return this vector after addition
     */
    RealVector add(RealVector vector);
    
    @Override
    RealVector clone();
}
