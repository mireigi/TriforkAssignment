package trifork.common.core;

import java.util.Objects;

public abstract class ValueObject {
    
    protected abstract Object[] getEqualityComponents();

    @Override
    public boolean equals(Object obj) {

        if (obj == null)
            return false;

        if (!(obj instanceof ValueObject))
            return false;
         
        ValueObject valueObject = (ValueObject)obj;

        Object[] equalityComponents = valueObject.getEqualityComponents();

        return compareEqualityComponents(equalityComponents, getEqualityComponents());
    }

    @Override
    public int hashCode()
    {
        int hashCode = 0;
        
        Object[] equalityComponents = getEqualityComponents();

        for (int i = 0; i < equalityComponents.length; i++) {
            int current = 0;

            if (equalityComponents[i] != null)
                current = equalityComponents[i].hashCode();

            hashCode = hashCode * 23 + current;
        }

        return hashCode;
    }

    private boolean compareEqualityComponents(Object[] equalityComponentsA, Object[] equalityComponentsB)
    {
        if (equalityComponentsA.length != equalityComponentsB.length)
            return false;
        
        for (int i = 0; i < equalityComponentsA.length; i++) {
            Object a = equalityComponentsA[i];
            Object b = equalityComponentsB[i];

            if (a.getClass() != b.getClass())
                return false;
            
            if (!Objects.equals(a, b))
                return false;
        }

        return true;
    }
}
