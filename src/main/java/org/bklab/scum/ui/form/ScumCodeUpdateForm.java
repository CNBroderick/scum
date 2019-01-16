package org.bklab.scum.ui.form;

import com.vaadin.server.ErrorMessage;
import com.vaadin.shared.ui.ErrorLevel;
import com.vaadin.ui.Component;
import dataq.ui.vaadin.XmlView;
import dataq.ui.vaadin.form.FormWindow;
import org.bklab.scum.ScumCode;
import org.bklab.scum.ui.config.ViewBase;

public class ScumCodeUpdateForm extends FormWindow<ScumCode> {
    private XmlView xv = XmlView.fromResource(ViewBase.class, "scum_update_view.xml");

    public ScumCodeUpdateForm(ScumCode t) {
        super(t);
        setWidth("560px");
        setHeight("240px");
        setTitle("改进指令[" + t.getCode() + "]的描述");
    }

    @Override
    public void readEntity() {
        xv.asFluent("en").setValue(getEntity().getEnDescription());
        xv.asFluent("cn").setValue(getEntity().getCnDescription());
    }

    @Override
    public boolean writeEntity() {
        String en = xv.asFluent("en").getValue();
        String cn = xv.asFluent("cn").getValue();
        if (en == null || en.trim().isEmpty()) {
            xv.asFluent("en").setComponentError(getError());
            return false;
        }
        if (cn == null || cn.trim().isEmpty()) {
            xv.asFluent("cn").setComponentError(getError());
            return false;
        }
        getEntity().setCnDescription(cn).setEnDescription(en);
        return true;
    }

    @Override
    public Component createContent() {
        readEntity();
        xv.asFluent("save").addClickListener(e -> doAction());
        xv.asFluent("cancel").addClickListener(e -> close());
        return xv.createComponent();
    }

    private ErrorMessage getError() {
        return new ErrorMessage() {
            @Override
            public ErrorLevel getErrorLevel() {
                return ErrorLevel.ERROR;
            }

            @Override
            public String getFormattedHtmlMessage() {
                return "请填写";
            }
        };
    }
}