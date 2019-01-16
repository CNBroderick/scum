package org.bklab.scum;

import org.bklab.scum.operation.ScumOperationEnum;
import org.bklab.util.POIUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScumConfig {
    public static void main(String[] args) {
        new ScumConfig().importData();
    }

    private void importData() {
        List<ScumCode> scumCodes = new ArrayList<>();
        try {
            POIUtil.readExcel(new File("E:\\Broderick Gaming\\scum\\SCUM.xlsx"))
                    .stream().filter(s -> s.length == 4)
                    .map(ScumCode::parse)
                    .filter(Objects::nonNull)
                    .forEach(c -> ScumOperationEnum.ScumCodeAdd.createOperation()
                            .setParam("code", c)
                            .execute()
                            .ifException(Throwable::printStackTrace));

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
