public interface Attackable<T> {
    boolean isAlive();
    int getCurrentHealth();
    void takeDamage(int damage);
    void attack(T target);

}
