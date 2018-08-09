package org.bklab.vaadin.vertical;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import org.bklab.vaadin.IndexUI;

import javax.servlet.annotation.WebServlet;

@Theme("mytheme")
public class VerticalLabelUI  extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final HorizontalLayout layout = new HorizontalLayout();

        layout.addComponent(new VerticalLabel("天王盖地虎，小鸡炖蘑菇", 20));
        layout.addComponent(new VerticalLabel("我爱北京天安门", 20));
        layout.addComponent(new VerticalLabel("我爱北京天安", 20));
        layout.addComponent(new VerticalLabel("我爱北京天", 20));
        layout.addComponent(new VerticalLabel("我爱北京", 20));
        layout.addComponent(new VerticalLabel("我爱北", 20));
        layout.addComponent(new VerticalLabel("我爱", 20));
        layout.addComponent(new VerticalLabel("我", 20));

        setContent(layout);
    }

    @WebServlet(urlPatterns = "/vertical", name = "VerticalLabelUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = VerticalLabelUI.class, productionMode = false)
    public static class VerticalLabelUIServlet extends VaadinServlet {
    }

}