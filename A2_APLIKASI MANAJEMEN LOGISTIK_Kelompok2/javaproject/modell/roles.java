package modell;
import java.util.Objects;

public class roles {
    private int idRoles;
    private String desc;
    private String name;


    public roles() {
    }

    public roles(int idRoles, String desc, String name) {
        this.idRoles = idRoles;
        this.desc = desc;
        this.name = name;
    }

    public int getIdRoles() {
        return this.idRoles;
    }

    public void setIdRoles(int idRoles) {
        this.idRoles = idRoles;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public roles idRoles(int idRoles) {
        setIdRoles(idRoles);
        return this;
    }

    public roles desc(String desc) {
        setDesc(desc);
        return this;
    }

    public roles name(String name) {
        setName(name);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof roles)) {
            return false;
        }
        roles roles = (roles) o;
        return idRoles == roles.idRoles && Objects.equals(desc, roles.desc) && Objects.equals(name, roles.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRoles, desc, name);
    }

    @Override
    public String toString() {
        return "{" +
            " idRoles='" + getIdRoles() + "'" +
            ", desc='" + getDesc() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
    
}
