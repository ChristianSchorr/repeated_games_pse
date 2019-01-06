package loop.controller;

public interface Repository<T> {
    T getEntityByName(String name);
}
