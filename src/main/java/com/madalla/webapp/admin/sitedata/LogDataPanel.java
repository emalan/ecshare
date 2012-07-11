package com.madalla.webapp.admin.sitedata;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.emalan.cms.bo.log.LogData;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.madalla.webapp.admin.sitedata.SiteDataPanel.NumericCssProvider;
import com.madalla.wicket.LabelPagingNavigator;

public class LogDataPanel extends Panel{

    private static final long serialVersionUID = 1L;

    public LogDataPanel(final String id, final DateTimeZone dateTimeZone, final SortableDataProvider<LogData, String> dataProvider) {
        super(id);
        
        final MarkupContainer logContainer;
        add(logContainer = new WebMarkupContainer("logContainer"));
        logContainer.setOutputMarkupId(true);

        final DataView<LogData> dataView;
        logContainer.add(dataView = new DataView<LogData>("logSorting", dataProvider) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final Item<LogData> item) {
                LogData data = item.getModelObject();
                DateTime dateTime = data.getDateTime().toDateTime(dateTimeZone);
                item.add(new Label("date", dateTime.toString("yyyy-MM-dd")));
                item.add(new Label("time", dateTime.toString("HH:mm:ss")));
                item.add(new Label("user", data.getUser()));
                item.add(new Label("type", data.getType()));
                item.add(new Label("cmsId", data.getCmsId()));

                item.add(new AttributeModifier("class", new AbstractReadOnlyModel<Object>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Object getObject() {
                        return (item.getIndex() % 2 == 1) ? "even" : "odd";
                    }
                }));

            }

        });

        logContainer.add(new OrderByBorder("orderByDateTime", "dateTime", dataProvider, NumericCssProvider.getInstance()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSortChanged() {
                dataView.setCurrentPage(0);
            }
        });

        logContainer.add(new OrderByBorder("orderByUser", "user", dataProvider) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSortChanged() {
                dataView.setCurrentPage(0);
            }
        });

        dataView.setItemsPerPage(10);

        add(new LabelPagingNavigator("logNavigator", dataView));
    }

}
