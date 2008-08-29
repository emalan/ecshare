package com.madalla.util.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class CalendarUtils {
	private static DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MMMMM yyyy");
	
	/**
	 * @param rootTitle - Text to be displayed for Root of Tree
	 * @param list - List of ICalendarTreeInput Items
	 * @return Populated TreeModel that displays items in Yearly, Monthly tree
	 */
	public static TreeModel createMonthlyTree(String rootTitle, List<? extends ICalendarTreeInput> list){
		return createMonthlyTree(rootTitle, list, dateFormatter);
	}

	/**
	 * @param rootTitle - Text to be displayed for Root of Tree
	 * @param list - List of ICalendarTreeInput Items
	 * @param dateFormatPattern - joda DateTimeFormatter
	 * @return Populated TreeModel that displays items in Yearly, Monthly tree
	 */
	public static TreeModel createMonthlyTree(String rootTitle, List<? extends ICalendarTreeInput> list,
			String dateFormatPattern){
		DateTimeFormatter newdf = DateTimeFormat.forPattern(dateFormatPattern);
		return createMonthlyTree(rootTitle, list, newdf);
	}

	/**
	 * @param rootTitle - Text to be displayed for Root of Tree
	 * @param list - List of ICalendarTreeInput Items
	 * @param df - joda DateTimeFormatter
	 * @return Populated TreeModel that displays items in Yearly, Monthly tree
	 * 
	 */
	public static TreeModel createMonthlyTree(String rootTitle, List<? extends ICalendarTreeInput> list,
			DateTimeFormatter df){
		
		//sort with most recent first
		Collections.sort(list, new Comparator<ICalendarTreeInput>(){
			public int compare(ICalendarTreeInput o1, ICalendarTreeInput o2) {
				return o2.getDateTime().compareTo(o1.getDateTime());
			}
		});
		
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootTitle);
        TreeModel model = new DefaultTreeModel(rootNode);
        
        //Create and add first node. Beginning of current month.
        LocalDate currentBeginningOfMonth = new LocalDate().withDayOfMonth(1);
        DefaultMutableTreeNode currentMonthNode = new DefaultMutableTreeNode(df.print(currentBeginningOfMonth));

        //looping through entries, most recent first
        for (ICalendarTreeInput entry: list) {
			LocalDate next = entry.getDateTime().toLocalDate();
			
			//This one is not in this Month, so lets go back one month until it is
			while (next.isBefore(currentBeginningOfMonth)){
				currentBeginningOfMonth = currentBeginningOfMonth.minusMonths(1);
				currentMonthNode = new DefaultMutableTreeNode(df.print(currentBeginningOfMonth));
			}
			//add list item to current month
			currentMonthNode.add(new DefaultMutableTreeNode(entry));
			//add current month to tree
			rootNode.add(currentMonthNode);
			
		}
        
        return model;
	}
	
}
