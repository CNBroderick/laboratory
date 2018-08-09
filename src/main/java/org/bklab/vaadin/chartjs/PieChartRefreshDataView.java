package org.bklab.vaadin.chartjs;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.ChartConfig;
import com.byteowls.vaadin.chartjs.config.PieChartConfig;
import com.byteowls.vaadin.chartjs.data.Dataset;
import com.byteowls.vaadin.chartjs.data.PieDataset;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Component;
import org.bklab.vaadin.chartjs.ui.AbstractChartView;
import org.bklab.vaadin.chartjs.ui.DemoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PieChartRefreshDataView extends AbstractChartView {

    private static final long serialVersionUID = 4894923343920891837L;
    @Override
    public Component getChart() {
        PieChartConfig config = new PieChartConfig();
        config
                .data()
                .labels("已确认", "未确认")
                .addDataset(new PieDataset().label("确认情况"))
                .and()
        ;

        config.
                options()
                .responsive(true)
                .animation()
                .animateRotate(true)
                .animateScale(true)
                .and()
                .circumference(2 * Math.PI)
                .tooltips()
                .displayColors(false)
                .and()
                .done();

        ChartJs chart = new ChartJs(config);
        chart.setJsLoggingEnabled(true);
        chart.addClickListener((a, b) -> DemoUtils.notification(a, b, config.data().getDatasets().get(a)));
        chart.setWidth(125, Sizeable.Unit.PIXELS);
        chart.setHeight(125, Sizeable.Unit.PIXELS);
        refreshChartData(chart);
        chart.getConfig().buildJson().put("displayColors", false);
        chart.getConfig().buildJson().put("display", false);
        return chart;
    }

    private void refreshChartData(ChartJs chart) {
        generateRandomData(chart.getConfig());
        chart.refreshData();
    }

    private void generateRandomData(ChartConfig chartConfig) {
        PieChartConfig config = (PieChartConfig) chartConfig;
        List<String> labels = config.data().getLabels();
        for (Dataset<?, ?> ds : config.data().getDatasets()) {
            PieDataset lds = (PieDataset) ds;
            List<Double> data = new ArrayList<>();
            List<String> colors = new ArrayList<>();

            data.add(new Random().nextInt(100) * 1.0);
            data.add(new Random().nextInt(100) * 1.0);

            for (int i = 0; i < labels.size(); i++) {
                colors.add(ColorUtils.randomColor(0.7));
            }
            lds.backgroundColor(colors.toArray(new String[0]));
            lds.dataAsList(data);
        }
    }

    public Component getChart(String provenceName) {
        return getChart();
    }

}
