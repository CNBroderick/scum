package org.bklab.scum.operation;

import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.scum.ScumCode;

public class ScumCodeUpdate extends JdbcUpdateOperation {

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> {
            ScumCode code = (ScumCode) getContext().getParam("code");
            if (code == null) return;
            db.execute("UPDATE `scum` SET  `en_description` = ?, `cn_description` = ? WHERE `type` = ? AND `code` = ?;",
                    new Object[]{code.getEnDescription(), code.getCnDescription(), code.getType(), code.getCode()});
        };
    }
}
