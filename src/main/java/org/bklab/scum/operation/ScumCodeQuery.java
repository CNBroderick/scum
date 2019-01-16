package org.bklab.scum.operation;

import dataq.core.jdbc.IRowMapper;
import dataq.core.operation.JdbcQueryOperation;
import org.bklab.scum.ScumCode;

import java.sql.ResultSet;

public class ScumCodeQuery extends JdbcQueryOperation {

    public ScumCodeQuery() {
        setQueryFor(QueryFor.ForList);
        setRowMapper(ScumCode.getIRowMapper());
    }

    @Override
    public String createSQLSelect() {
        return "SELECT * FROM scum WHERE " + createWhereCondition();
    }

    private String createWhereCondition() {
        StringBuilder b = new StringBuilder(" 1=1");

        String type = getContext().getString("type");
        if (type != null)
            b.append(" && type=\"").append(type).append("\"");

        String code = getContext().getString("code");
        if (code != null)
            b.append(" && code=\"").append(code).append("\"");

        String en_description = getContext().getString("en_description");
        if (en_description != null)
            b.append(" && en_description=\"").append(en_description).append("\"");

        String cn_description = getContext().getString("cn_description");
        if (cn_description != null)
            b.append(" && cn_description=\"").append(cn_description).append("\"");

        return b.toString();
    }
}
