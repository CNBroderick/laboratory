package org.bklab.vaadin.chartjs.ui;

import com.vaadin.navigator.View;
import com.vaadin.ui.Component;

public interface ChartView extends View {

    Component getChart();

    String getSource();

}
