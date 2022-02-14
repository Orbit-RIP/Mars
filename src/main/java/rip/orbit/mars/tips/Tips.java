package rip.orbit.mars.tips;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 13/02/2022 / 11:48 AM
 * Mars / rip.orbit.mars.tips
 */

@AllArgsConstructor
@Getter
public enum Tips {

//	MULTI_EXPERIENCE("&6&l"),

	EDIT_KIT("&6&lKit Editing", Arrays.asList(
			"&fYou can create a custom kit to your liking.",
			"&fThis is possible by clicking the book in the",
			"&flast slot of your inventory whilst in the lobby.",
			"&f ",
			"&fYou can edit your pre-edited trapper/raider kit."
	));

	private String title;
	private List<String> message;

}
