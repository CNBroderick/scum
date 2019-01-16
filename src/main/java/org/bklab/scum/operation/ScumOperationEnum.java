package org.bklab.scum.operation;

import dataq.core.jdbc.DBAccess;
import dataq.core.operation.AbstractOperation;
import dataq.core.operation.JdbcOperation;

public enum ScumOperationEnum {
    ScumCodeAdd,
    ScumCodeQuery,
    ScumCodeUpdate,
    ;

    public AbstractOperation createOperation() {
        return createJdbcOperation();
    }

    private AbstractOperation createJdbcOperation() {
        String OPERATION_PATH = "org.bklab.scum.operation";
        try {
            JdbcOperation op = (JdbcOperation) Class.forName(OPERATION_PATH + "." + this.name()).newInstance();
            op.setOperationName(this.name());
            op.setDBAccess(new DBAccess("Broderick-Beijing-TencentDB-ss"));
            return op;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}