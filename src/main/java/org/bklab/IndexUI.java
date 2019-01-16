package org.bklab;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.*;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import dataq.core.xml.XmlObject;
import dataq.ui.vaadin.AppLayoutView;
import dataq.ui.vaadin.FluentUI;
import dataq.ui.vaadin.dialog.DialogBox;
import dataq.ui.vaadin.dialog.YesnoBox;
import dataq.ui.vaadin.sidemenu.SideMenu;
import org.bklab.scum.ui.config.ViewBase;
import org.bklab.scum.ui.image.ImageBase;

import javax.servlet.annotation.WebServlet;
import java.io.File;
import java.net.URISyntaxException;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@Title("SCUM 管理员命令查询 --布克约森·探索者实验室")
@SuppressWarnings("unused")
public class IndexUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(createIndexUI());
    }

    private Component createIndexUI() {
        final AppLayoutView view = new AppLayoutView();
        SideMenu menu = SideMenu.fromXmlObject(XmlObject.fromURL(
                ViewBase.class.getResource("menu-config.xml")).getChildByName("explorer"),
                view.getContentPanel());
        view.setLeftMenu(menu);
        view.addLeftItems(createTopIcon());
        view.addRightItems(createNavigateToBroderickLabs(), createGitHubLinks(), createTopRightItems());
        return view.createComponent();
    }

    private Component createMenuTopIcon() {
        Image image = null;
        try {
            image = new Image("", new FileResource(new File(ImageBase.class.getResource("ooopic_1467425091.png").toURI())));
            image.setWidth(128, Unit.PIXELS);
        } catch (URISyntaxException e) {
            DialogBox.fromException(e).open();
        }
        VerticalLayout topIconAndBlank = new VerticalLayout();
        topIconAndBlank.addComponents(image, new Label());
        topIconAndBlank.setMargin(false);

        return topIconAndBlank;
    }

    private Component createTopIcon() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(false);
        layout.setDefaultComponentAlignment(Alignment.TOP_LEFT);
        try {
            layout.addComponent(FluentUI.image(null,
                    new FileResource(new File(ImageBase.class.getResource("broderick.cn.png").toURI())))
                    .setWidth(260, Unit.PIXELS).setId("app-icon").get());
        } catch (URISyntaxException e) {
            DialogBox.fromException(e).open();
        }
        return layout;
    }

    private Component createTopRightItems() {
        Button button = new Button(null, VaadinIcons.EXIT_O);
        button.addClickListener(e -> YesnoBox.create().setMessage("退出当前登录").setYesListener(y -> Page.getCurrent().reload()).open());
        button.addStyleNames(ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_HUGE, ValoTheme.BUTTON_LINK);
        button.setDescription("退出当前用户");
        return button;
    }

    private Component createGitHubLinks() {
        return FluentUI.buttonLink(new ExternalResource("https://github.com/favicon.ico"))
                .addClickListener(e -> YesnoBox.create().setMessage("View Source Code In GitHub IF YOU HAVE PERMISSIONS!")
                        .setYesListener(y -> Page.getCurrent().open("https://github.com/CNBroderick/asset", "_blank")).open())
                .addStyleNames(ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_LARGE)
                .setDescription("View Source Code In GitHub <b>IF YOU HAVE PERMISSIONS</b>!", ContentMode.HTML).get();
    }

    @Deprecated
    private Component createGitHubLinksUsingFluentUI() {
        return FluentUI.image(null, new ExternalResource("https://github.com/favicon.ico"))
                .addClickListener(e -> Page.getCurrent().open("https://github.com/CNBroderick/asset", "_blank"))
                .setWidth(25, Unit.PIXELS).setHeight(25, Unit.PIXELS)
                .addStyleNames(ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_TINY)
                .setDescription("View Source Code In GitHub IF YOU HAVE PERMISSIONS!").get();
    }

    private Component createNavigateToBroderickLabs() {
        return FluentUI.button(null, new ThemeResource("image/favicon_32.png"))
                .addClickListener(y -> Page.getCurrent().open("https://bbkki.com/explorer", "_blank"))
                .addStyleNames(ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_LARGE, ValoTheme.BUTTON_LINK)
                .setDescription("View this in Broderick Labs Website.").get();
    }

    @WebServlet(urlPatterns = "/*", name = "IndexUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = IndexUI.class, productionMode = true)
    public static class IndexUIServlet extends VaadinServlet {

    }

}
