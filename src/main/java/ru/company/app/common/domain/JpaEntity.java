package ru.company.app.common.domain;

import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;

public abstract class JpaEntity<K extends Serializable> {

    protected abstract K getId();

    protected abstract void setId(K id);

    @Override
    public final boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        Class<?> otherClass = other instanceof HibernateProxy ? ((HibernateProxy) other).getHibernateLazyInitializer().getPersistentClass() : other.getClass();
        Class<?> thisClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisClass != otherClass) return false;
        JpaEntity<?> otherObj = (JpaEntity<?>) other;
        return getId() != null && Objects.equals(getId(), otherObj.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }


}
