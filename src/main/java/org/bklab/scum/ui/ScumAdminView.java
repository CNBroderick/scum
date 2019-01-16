package org.bklab.scum.ui;

import com.vaadin.jsclipboard.JSClipboard;
import com.vaadin.jsclipboard.JSClipboardButton;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.themes.ValoTheme;
import dataq.core.operation.AbstractOperation;
import dataq.ui.vaadin.FluentUI;
import dataq.ui.vaadin.TitleBox;
import dataq.ui.vaadin.View;
import dataq.ui.vaadin.dialog.DialogBox;
import org.bklab.scum.ScumCode;
import org.bklab.scum.operation.ScumOperationEnum;
import org.bklab.scum.ui.form.ScumCodeUpdateForm;

import java.util.List;
import java.util.stream.Collectors;

public class ScumAdminView implements View {

    private Grid<ScumCode> grid = createGrid();
    private TextField keyword = createKeyFilter();
    private ComboBox<String> type = createTypeFilter();
    private AbsoluteLayout absoluteLayout = new AbsoluteLayout();
    private TextField last = FluentUI.textField()
            .addStyleNames(ValoTheme.TEXTFIELD_BORDERLESS)
            .get();

    @Override
    public Component createComponent() {
        TitleBox box = new TitleBox("SCUM命令查询", false);
        grid.setItems(createQueryOperation().execute().asList());
        box.addToolbarItems(last, type, keyword, createQueryButton());
        grid.setSizeFull();
        box.setSizeFull();
        VerticalLayout layout = new VerticalLayout();
        layout.addComponents(grid, absoluteLayout);
        layout.setExpandRatio(grid, 1f);
        layout.setExpandRatio(absoluteLayout, 0f);
        layout.setSizeFull();
        layout.setMargin(false);
        layout.setSpacing(false);
        box.setContent(layout);
        return box;
    }

    private AbstractOperation createQueryOperation() {
        return ScumOperationEnum.ScumCodeQuery.createOperation()
                .setParam("type", type == null ? null : type.getValue());
    }

    private ComboBox<String> createTypeFilter() {
        List<String> list = ScumOperationEnum.ScumCodeQuery.createOperation()
                .execute()
                .ifException(e -> DialogBox.fromException(e).open())
                .asList().stream()
                .map(s -> ((ScumCode) s))
                .map(ScumCode::getType)
                .distinct().collect(Collectors.toList());
        return FluentUI.comboBox()
                .setEmptySelectionCaption("全部")
                .setItems(list)
                .setPageLength(list.size() + 1)
                .get();
    }

    private TextField createKeyFilter() {
        return FluentUI.textField()
                .setPlaceholder("关键字")
                .get();
    }

    private Button createQueryButton() {
        return FluentUI.button("查询")
                .addClickListener(e -> grid.setItems(createQueryOperation()
                        .execute()
                        .ifException(p -> DialogBox.fromException(p).open())
                        .asList().stream()
                        .map(s -> (ScumCode) s)
                        .filter(s -> keyword == null ? true : s.isMatch(keyword.getValue()))
                        .collect(Collectors.toList())))
                .get();
    }

    private Grid<ScumCode> createGrid() {
        Grid<ScumCode> grid = new Grid<>();
        grid.setStyleGenerator(item -> "v-align-center");
        grid.addColumn(this::createScumImage, new ComponentRenderer()).setCaption("图标");
        grid.addColumn(ScumCode::getType).setCaption("类型");
        grid.addColumn(s -> createCopyableButton(s.getCode()), new ComponentRenderer()).setCaption("代码(双击单元格快速复制)");
        grid.addColumn(ScumCode::getCnDescription).setCaption("中文描述");
        grid.addColumn(ScumCode::getEnDescription).setCaption("英文描述");
        grid.addColumn(this::createUpdateDescriptionButton, new ComponentRenderer()).setCaption("操作");
        grid.addStyleName("alarm-grid");
        grid.addStyleName("v-align-center");
        grid.addStyleName("align-center");
        grid.setSizeFull();
        grid.setWidth("99.999%");
        return grid;
    }

    private Image createScumImage(ScumCode scumCode) {
        Image image = new Image();
        String[] s = scumCode.getCode().split(" ");
        String url = "https://commands.gg/scum/items/image/32/" + s[s.length - 1].trim() + ".png";
        System.out.println(url);
        image.setSource(new ExternalResource(url));
        return image;
    }

    private Component createCopyableButton(String s) {
        final Label select = new Label(s);
        select.setValue(s);
        select.setId(s);
        absoluteLayout.addComponent(select, "right: 0px; top: 0px;");
        JSClipboardButton jsb = new JSClipboardButton(select, s, e -> System.out.println("click"));
        jsb.setDescription(s);
        jsb.setClipboardText(s);
        jsb.addSuccessListener(() -> Notification.show(getSuccessMessage(s), getPasteDescription(), Notification.Type.WARNING_MESSAGE));
        jsb.addErrorListener(() -> Notification.show("复制失败", s, Notification.Type.ERROR_MESSAGE));
        jsb.addStyleNames(ValoTheme.BUTTON_TINY, ValoTheme.BUTTON_LINK, ValoTheme.BUTTON_BORDERLESS);
        jsb.setEnabled(true);

        Button b =  FluentUI.buttonLink(s).get();
        b.addClickListener(e -> {
            JSClipboard clipboard = new JSClipboard();
            last.setValue(s);
            clipboard.apply(b, last);
            clipboard.addSuccessListener(() -> Notification.show(getSuccessMessage(s), getPasteDescription(), Notification.Type.WARNING_MESSAGE));
            clipboard.addErrorListener(() -> Notification.show("复制失败", s, Notification.Type.ERROR_MESSAGE));
        });
        return b;
    }

    private String getSuccessMessage(String content) {
        return "成功复制 " + content + " 到您的剪贴板上";
    }

    private String getPasteDescription() {
        return "请使用键盘快捷键 \"Ctrl + V\" 粘贴到相应位置";
    }

    private Button createUpdateDescriptionButton(ScumCode code) {
        return FluentUI.buttonLink("改进描述").addClickListener(e -> {
            ScumCodeUpdateForm form = new ScumCodeUpdateForm(code);
            form.setActionHandler(s ->
                    ScumOperationEnum.ScumCodeUpdate.createOperation()
                            .setParam("code", s)
                            .execute()
                            .ifException(p -> DialogBox.fromException(p).open())
                            .ifSuccess(u -> {
                                grid.getDataProvider().refreshAll();
                                form.close();
                            })
            );
            form.open();
        }).get();
    }

}
