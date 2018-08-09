package org.bklab.vaadin;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.bklab.vaadin.chartjs.PieChartRefreshDataView;

@Theme("mytheme")
public class IndexUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final HorizontalLayout layout = new HorizontalLayout();
        layout.addComponent(new PieChartRefreshDataView().getChart());
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "IndexUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = IndexUI.class, productionMode = false)
    public static class IndexUIServlet extends VaadinServlet {
    }

}
