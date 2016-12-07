package opponent;

/**
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  --> @generated
 */
public abstract class Opponent {

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  --> @generated @ordered
     */

    private int id;

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  --> @generated @ordered
     */
    public String name;

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  --> @generated
     */
    public Opponent() {
        super();
    }

    public int getId() {
        return id;
    }
    
    //ABSTRACT METHODS----------------------------------------------------------
    public abstract int getNextMove();
    
    public abstract void win();

    public abstract void lose();

    public abstract void draw();

}
