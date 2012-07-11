package com.madalla.webapp.admin.sitedata;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByLink.CssProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.emalan.cms.bo.email.EmailEntryData;
import org.emalan.cms.bo.log.LogData;
import org.joda.time.DateTimeZone;

import com.madalla.webapp.CmsPanel;

public class SiteDataPanel extends CmsPanel {

    private static final long serialVersionUID = 1L;

    private final DateTimeZone dateTimeZone;

    private class SortableEmailEntryProvider extends SortableDataProvider<EmailEntryData, String> {

        private static final long serialVersionUID = 1L;
        
        SortableEmailEntryProvider() {
            setSort("id", SortOrder.ASCENDING);
        }

        public Iterator<? extends EmailEntryData> iterator(long first, long count) {
            List<EmailEntryData> list = getApplicationService().getEmailEntries();
            if ("senderName".equals(getSort().getProperty())) {
                Collections.sort(list, getNameComparator());
            } else {
                Collections.sort(list, getDateTimeComparator());
            }
            if (!getSort().isAscending()) {
                Collections.reverse(list);
            }
            return list.listIterator((int) first);
        }

        private Comparator<EmailEntryData> getNameComparator() {
            return new Comparator<EmailEntryData>() {

                public int compare(EmailEntryData o1, EmailEntryData o2) {
                    return o1.getSenderName().compareTo(o2.getSenderName());
                }

            };
        }

        private Comparator<EmailEntryData> getDateTimeComparator() {
            return new Comparator<EmailEntryData>() {

                public int compare(EmailEntryData o1, EmailEntryData o2) {
                    return o1.getDateTime().compareTo(o2.getDateTime());
                }

            };
        }

        public IModel<EmailEntryData> model(final EmailEntryData object) {
            return new AbstractReadOnlyModel<EmailEntryData>() {

                private static final long serialVersionUID = 1L;

                @Override
                public EmailEntryData getObject() {
                    return object;
                }
            };
        }

        public long size() {
            return getApplicationService().getEmailEntries().size();
        }

    }

    private class SortableLogProvider extends SortableDataProvider<LogData, String> {

        private static final long serialVersionUID = 1L;

        SortableLogProvider() {
            setSort("id", SortOrder.ASCENDING);
        }

        public Iterator<? extends LogData> iterator(long first, long count) {
            List<? extends LogData> list = getRepositoryService().getTransactionLogEntries();
            if ("user".equals(getSort().getProperty())) {
                Collections.sort(list, getUserComparator());
            } else {
                Collections.sort(list, getDateTimeComparator());
            }
            if (!getSort().isAscending()) {
                Collections.reverse(list);
            }
            return list.listIterator((int) first);
        }

        private Comparator<LogData> getUserComparator() {
            return new Comparator<LogData>() {

                public int compare(LogData o1, LogData o2) {
                    return o1.getUser().compareTo(o2.getUser());
                }

            };
        }

        private Comparator<LogData> getDateTimeComparator() {
            return new Comparator<LogData>() {

                public int compare(LogData o1, LogData o2) {
                    return o1.getDateTime().compareTo(o2.getDateTime());
                }

            };
        }

        public long size() {
            return getRepositoryService().getTransactionLogEntries().size();
        }

        public IModel<LogData> model(final LogData object) {
            return new AbstractReadOnlyModel<LogData>() {

                private static final long serialVersionUID = 1L;

                @Override
                public LogData getObject() {
                    return object;
                }
            };

        }

    }

    public SiteDataPanel(String id) {
        super(id);

        dateTimeZone = getRepositoryService().getDateTimeZone();

        add(new EmailDataPanel("emailPanel", dateTimeZone, new SortableEmailEntryProvider()));
        
        add(new LogDataPanel("logPanel", dateTimeZone, new SortableLogProvider()));
        
    }

    /**
     * Default implementation of ICssProvider
     * 
     * @author Igor Vaynberg ( ivaynberg )
     */
    public static class NumericCssProvider extends CssProvider {
        private static final long serialVersionUID = 1L;

        private static NumericCssProvider instance = new NumericCssProvider();

        private NumericCssProvider() {
            super("wicket_orderUp_n", "wicket_orderDown_n", "wicket_orderNone");
        }

        /**
         * @return singleton instance
         */
        public static NumericCssProvider getInstance() {
            return instance;
        }
    }

}
