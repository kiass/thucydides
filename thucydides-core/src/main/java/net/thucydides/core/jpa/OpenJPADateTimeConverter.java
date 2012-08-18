package net.thucydides.core.jpa;

import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.JavaSQLTypes;
import org.apache.openjpa.jdbc.meta.ValueMapping;
import org.apache.openjpa.jdbc.meta.strats.AbstractValueHandler;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.ColumnIO;
import org.apache.openjpa.meta.JavaTypes;
import org.joda.time.DateTime;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: rahul
 * Date: 8/19/12
 * Time: 12:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class OpenJPADateTimeConverter  extends AbstractValueHandler {

    @Override
    public Column[] map(ValueMapping valueMapping, String name, ColumnIO columnIO, boolean adapt) {

        Column date = new Column();
        date.setJavaType(JavaSQLTypes.TIMESTAMP);
        return new Column[] { date };

    }

    @Override
    public Object toDataStoreValue(ValueMapping vm, Object val, JDBCStore store) {

        if (val == null) {
            return null;
        }

        if (val instanceof DateTime) {
            Timestamp dataStoreDate = new Timestamp(((DateTime) val).getMillis());
            return new Object[] { dataStoreDate };

        } else {
            throw new IllegalStateException("Conversion exception, value is not of DateTime type.");
        }

    }

    @Override
    public Object toObjectValue(ValueMapping vm, Object val) {

        if (val == null) {
            return null;
        }

        if (val instanceof Timestamp) {
            return new DateTime(val);
        } else {
            throw new IllegalStateException("Conversion exception, value is not of Timestamp type.");
        }

    }
}
