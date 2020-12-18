package dominio;

import dominio.Empleado;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-12-18T18:29:07")
@StaticMetamodel(Rolempleado.class)
public class Rolempleado_ { 

    public static volatile SingularAttribute<Rolempleado, Short> idrol;
    public static volatile SingularAttribute<Rolempleado, String> nombrerol;
    public static volatile ListAttribute<Rolempleado, Empleado> empleadoList;

}