package concordSprint4;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ConcordData.AutoCensor;
import ConcordData.AutoExpand;

class Sprint4Test {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void test() {
		String[] badWords = {"funny", "bug"};
		AutoCensor demoCensor = new AutoCensor(badWords);
		assertEquals("u are not me", demoCensor.CheckString(0, "u are not me"));
		assertEquals("u are not [redacted]", demoCensor.CheckString(0, "u are not funny"));
		assertEquals("[redacted] [redacted] gub", demoCensor.CheckString(0, "bug BUG gub"));
		assertEquals("AutoCensor", demoCensor.getName());
		
		String[] Abbr = {"lol", "btw"};
		String[] Full = {"laugh out loud", "by the way"};
		AutoExpand abbrDemo = new AutoExpand(Abbr, Full);
		assertEquals("laugh out loud what", abbrDemo.CheckString(0, "lol what"));
		assertEquals("laugh out loud what", abbrDemo.CheckString(0, "LOL what"));
		assertEquals("by the way by the way you", abbrDemo.CheckString(0, "btw BTW you"));
		assertEquals("AutoExpand", abbrDemo.name);
	}

}
