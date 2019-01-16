package org.bklab.scum.operation;

import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.scum.ScumCode;

public class ScumCodeAdd extends JdbcUpdateOperation {
    @Override
    public IDBAccessCallback createCallBack() {
        return db -> {
            ScumCode code = (ScumCode) getContext().getParam("code");
            if (code == null)
                return;
            db.execute("INSERT INTO `scum`(`type`,`code`,`en_description`,`cn_description`)VALUES(?,?,?,?);",
                    new Object[]{code.getType(), code.getCode(), code.getEnDescription(), code.getCnDescription()});
        };
    }
}

