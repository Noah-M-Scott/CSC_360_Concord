package concordSprint4;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
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
		assertEquals(demoCensor.CheckString(0, "u are not me"), "u are not me");
		assertEquals(demoCensor.CheckString(0, "u are not funny"), "u are not [redacted]");
		assertEquals(demoCensor.CheckString(0, "bug BUG gub"), "[redacted] [redacted] gub");
		assertEquals(demoCensor.name, "AutoCensor");
		
		String[] Abbr = {"lol", "btw"};
		String[] Full = {"laugh out loud", "by the way"};
		AutoExpand abbrDemo = new AutoExpand(Abbr, Full);
		assertEquals(abbrDemo.CheckString(0, "lol what"), "laugh out loud what");
		assertEquals(abbrDemo.CheckString(0, "LOL what"), "laugh out loud what");
		assertEquals(abbrDemo.CheckString(0, "btw BTW you"), "by the way by the way you");
		assertEquals(abbrDemo.name, "AutoExpand");
	}

}
