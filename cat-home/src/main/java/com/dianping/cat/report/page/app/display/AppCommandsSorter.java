package com.dianping.cat.report.page.app.display;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.unidal.lookup.util.StringUtils;

import com.dianping.cat.Constants;

public class AppCommandsSorter {

	private String m_sortBy;

	private DisplayCommands m_commands;

	private boolean m_sortValue = true;

	public AppCommandsSorter(DisplayCommands commands, String type) {
		m_commands = commands;
		m_sortBy = type;

		if ("domain".equals(type) || "command".equals(type) || "bu".equals(type) || "department".equals(type)
		      || StringUtils.isEmpty(type)) {
			m_sortValue = false;
		}
	}

	public DisplayCommands getSortedCommands() {
		Map<Integer, DisplayCommand> commands = m_commands.getCommands();
		List<Entry<Integer, DisplayCommand>> tmp = new LinkedList<Entry<Integer, DisplayCommand>>(commands.entrySet());
		Map<Integer, DisplayCommand> results = new LinkedHashMap<Integer, DisplayCommand>();

		Collections.sort(tmp, new AppComparator());

		for (Entry<Integer, DisplayCommand> command : tmp) {
			results.put(command.getKey(), command.getValue());
		}
		m_commands.getCommands().clear();
		m_commands.getCommands().putAll(results);
		return m_commands;

	}

	public class AppComparator implements Comparator<Entry<Integer, DisplayCommand>> {

		@Override
		public int compare(Entry<Integer, DisplayCommand> o1, Entry<Integer, DisplayCommand> o2) {
			DisplayCommand command1 = o1.getValue();
			DisplayCommand command2 = o2.getValue();
			String domain1 = command1.getDomain();
			String domain2 = command2.getDomain();

			if (m_sortValue) {
				if (Constants.ALL.equals(domain1)) {
					return -1;
				} else if (Constants.ALL.equals(domain2)) {
					return 1;
				} else {
					return sortValue(command1, command2);
				}
			} else {
				if ("command".equals(m_sortBy)) {
					domain1 = command1.getTitle();

					if (StringUtils.isEmpty(domain1)) {
						domain1 = command1.getName();
					}

					domain2 = command2.getTitle();
					if (StringUtils.isEmpty(domain2)) {
						domain2 = command2.getName();
					}
				}
				return sortDomain(domain1, domain2);
			}
		}

		private int sortValue(DisplayCommand command1, DisplayCommand command2) {
			if ("count".equals(m_sortBy)) {
				long count1 = command1.getCount();
				long count2 = command2.getCount();

				return count2 > count1 ? 1 : (count2 < count1 ? -1 : 0);
			} else if ("avg".equals(m_sortBy)) {
				double avg1 = command1.getAvg();
				double avg2 = command2.getAvg();

				return avg2 > avg1 ? 1 : (avg2 < avg1 ? -1 : 0);
			} else if ("success".equals(m_sortBy)) {
				double ratio1 = command1.getSuccessRatio();
				double ratio2 = command2.getSuccessRatio();

				return ratio2 > ratio1 ? 1 : (ratio2 < ratio1 ? -1 : 0);
			} else if ("request".equals(m_sortBy)) {
				double avg1 = command1.getRequestAvg();
				double avg2 = command2.getRequestAvg();

				return avg2 > avg1 ? 1 : (avg2 < avg1 ? -1 : 0);
			} else if ("response".equals(m_sortBy)) {
				double avg1 = command1.getResponseAvg();
				double avg2 = command2.getResponseAvg();

				return avg2 > avg1 ? 1 : (avg2 < avg1 ? -1 : 0);
			} else {
				DisplayCode code1 = command1.findOrCreateCode(m_sortBy);
				DisplayCode code2 = command2.findOrCreateCode(m_sortBy);

				return sortCount(code1, code2);
			}
		}

		private int sortDomain(String o1, String o2) {
			if (Constants.ALL.equals(o1)) {
				return -1;
			}
			if (Constants.ALL.equals(o2)) {
				return 1;
			}
			boolean o1Empty = StringUtils.isEmpty(o1);
			boolean o2Empty = StringUtils.isEmpty(o2);

			if (o1Empty && o2Empty) {
				return 0;
			} else if (o1Empty) {
				return 1;
			} else if (o2Empty) {
				return -1;
			}
			return o1.compareTo(o2);
		}

		private int sortCount(DisplayCode c1, DisplayCode c2) {
			if (c1 == null && c2 == null) {
				return 0;
			} else if (c1 == null) {
				return 1;
			} else if (c2 == null) {
				return -1;
			} else {
				long count1 = c1.getCount();
				long count2 = c2.getCount();

				return count2 > count1 ? 1 : (count2 < count1 ? -1 : 0);
			}
		}
	}

}
