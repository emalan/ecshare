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
import org.emalan.cms.bo.email.EmailEntryData;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.madalla.webapp.admin.sitedata.SiteDataPanel.NumericCssProvider;
import com.madalla.wicket.LabelPagingNavigator;

public class EmailDataPanel extends Panel{

    private static final long serialVersionUID = 1L;

    public EmailDataPanel(final String id, final DateTimeZone dateTimeZone, final SortableDataProvider<EmailEntryData, String> dataProvider) {
        super(id);
        
        final MarkupContainer emailContainer;
        add(emailContainer = new WebMarkupContainer("emailContainer"));
        emailContainer.setOutputMarkupId(true);
        
        final DataView<EmailEntryData> dataView;
        emailContainer.add(dataView = new DataView<EmailEntryData>("emailSorting", dataProvider) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final Item<EmailEntryData> item) {
                // item.add(new Label("emailId", item.getId()));
                EmailEntryData emailEntry = item.getModelObject();
                DateTime dateTime = emailEntry.getDateTime().toDateTime(dateTimeZone);
                item.add(new Label("date", dateTime.toString("yyyy-MM-dd")));
                item.add(new Label("time", dateTime.toString("HH:mm:ss")));
                item.add(new Label("name", emailEntry.getSenderName()));
                item.add(new Label("email", emailEntry.getSenderEmailAddress()));
                item.add(new Label("comment", emailEntry.getSenderComment()));

                item.add(new AttributeModifier("class", new AbstractReadOnlyModel<Object>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Object getObject() {
                        return (item.getIndex() % 2 == 1) ? "even" : "odd";
                    }
                }));

            }

        });
        
        dataView.setItemsPerPage(10);
        
        emailContainer.add(new OrderByBorder("orderByDateTime", "dateTime", dataProvider, NumericCssProvider.getInstance()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSortChanged() {
                dataView.setCurrentPage(0);
            }
        });

        emailContainer.add(new OrderByBorder("orderByName", "senderName", dataProvider) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSortChanged() {
                dataView.setCurrentPage(0);
            }
        });

        add(new LabelPagingNavigator("emailNavigator", dataView));

    }

}
